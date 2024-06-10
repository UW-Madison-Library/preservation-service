package edu.wisc.library.sdg.preservation.worker.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.zaxxer.hikari.HikariDataSource;
import edu.wisc.library.sdg.preservation.common.client.RestTemplates;
import edu.wisc.library.sdg.preservation.common.spring.GlobalExceptionHandler;
import edu.wisc.library.sdg.preservation.common.spring.RequestIdFilter;
import edu.wisc.library.sdg.preservation.common.util.UncheckedFiles;
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;
import edu.wisc.library.sdg.preservation.worker.bag.FileMetadataSerializer;
import edu.wisc.library.sdg.preservation.worker.lock.DefaultLockManager;
import edu.wisc.library.sdg.preservation.worker.lock.LockManager;
import edu.wisc.library.sdg.preservation.worker.lock.RedisLockManager;
import edu.wisc.library.sdg.preservation.worker.metrics.OcflRepoMetrics;
import edu.wisc.library.sdg.preservation.worker.pipeline.Pipeline;
import edu.wisc.library.sdg.preservation.worker.pipeline.ingest.bag.CheckFitsStep;
import edu.wisc.library.sdg.preservation.worker.pipeline.ingest.bag.ClamAVBagStep;
import edu.wisc.library.sdg.preservation.worker.pipeline.ingest.bag.DirectorySetupStep;
import edu.wisc.library.sdg.preservation.worker.pipeline.ingest.bag.ExplodeAndValidateBagStep;
import edu.wisc.library.sdg.preservation.worker.pipeline.ingest.bag.IngestBagContext;
import edu.wisc.library.sdg.preservation.worker.pipeline.ingest.bag.IngestBatchStatusUpdateStep;
import edu.wisc.library.sdg.preservation.worker.pipeline.ingest.bag.ObjectIngestStep;
import edu.wisc.library.sdg.preservation.worker.pipeline.ingest.bag.ReadMetadataStep;
import edu.wisc.library.sdg.preservation.worker.pipeline.ingest.bag.RestartFitsStep;
import edu.wisc.library.sdg.preservation.worker.pipeline.ingest.object.FitsStep;
import edu.wisc.library.sdg.preservation.worker.pipeline.ingest.object.IdentifyObjectFilesStep;
import edu.wisc.library.sdg.preservation.worker.pipeline.ingest.object.IngestObjectContext;
import edu.wisc.library.sdg.preservation.worker.pipeline.ingest.object.ObjectDirectorySetupStep;
import edu.wisc.library.sdg.preservation.worker.pipeline.ingest.object.ObjectStatusUpdateStep;
import edu.wisc.library.sdg.preservation.worker.pipeline.ingest.object.RegisterObjectFileDetailsStep;
import edu.wisc.library.sdg.preservation.worker.pipeline.ingest.object.postprocessor.PhotoshopPropertiesFilter;
import edu.wisc.library.sdg.preservation.worker.pipeline.ingest.object.service.PronomIdentifier;
import edu.wisc.library.sdg.preservation.worker.storage.ocfl.FsRawOcflStore;
import edu.wisc.library.sdg.preservation.worker.storage.ocfl.RawOcflStore;
import edu.wisc.library.sdg.preservation.worker.storage.ocfl.S3RawOcflStore;
import edu.wisc.library.sdg.preservation.worker.storage.remote.GlacierRemoteDataStore;
import edu.wisc.library.sdg.preservation.worker.tools.clamav.ClamAV;
import edu.wisc.library.sdg.preservation.worker.tools.fits.Fits;
import edu.wisc.library.sdg.preservation.worker.tools.fits.FitsWeb;
import edu.wisc.library.sdg.preservation.worker.util.WorkDirectory;
import edu.wisc.library.sdg.preservation.worker.validation.DefaultGlacierVersionValidator;
import edu.wisc.library.sdg.preservation.worker.validation.GlacierVersionValidator;
import edu.wisc.library.sdg.preservation.worker.validation.lambda.LambdaFunction;
import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import io.ocfl.api.OcflConfig;
import io.ocfl.api.OcflRepository;
import io.ocfl.api.model.DigestAlgorithm;
import io.ocfl.api.model.OcflVersion;
import io.ocfl.aws.OcflS3Client;
import io.ocfl.core.OcflRepositoryBuilder;
import io.ocfl.core.extension.OcflExtensionConfig;
import io.ocfl.core.extension.storage.layout.HashedNTupleLayoutExtension;
import io.ocfl.core.extension.storage.layout.NTupleOmitPrefixStorageLayoutExtension;
import io.ocfl.core.extension.storage.layout.OcflStorageLayoutExtension;
import io.ocfl.core.extension.storage.layout.config.HashedNTupleLayoutConfig;
import io.ocfl.core.extension.storage.layout.config.NTupleOmitPrefixStorageLayoutConfig;
import io.ocfl.core.path.constraint.ContentPathConstraints;
import io.ocfl.core.storage.cloud.CloudClient;
import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.metrics.AutoTimer;
import org.springframework.boot.actuate.metrics.web.client.MetricsRestTemplateCustomizer;
import org.springframework.boot.actuate.metrics.web.client.RestTemplateExchangeTagsProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import software.amazon.awssdk.auth.credentials.AnonymousCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.s3.S3Client;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Configuration
public class AppConfig {

    private static final long KB = 1024;
    private static final long MB = KB * 1024;
    private static final long GB = MB * 1024;

    private static final Duration LAMBDA_TIMEOUT_MINUTES = Duration.ofMinutes(4);

    @Value("${app.client.internal.mgr.base.path}")
    private String internalMgrBasePath;

    @Value("${app.client.internal.mgr.username}")
    private String internalMgrUsername;

    @Value("${app.client.internal.mgr.password}")
    private String internalMgrPassword;

    @Value("${app.lock.acquire.timeout.ms}")
    private long acquireLockTimeout;

    @Value("${app.work.dir}")
    private Path workDir;

    @Value("${app.temp.dir}")
    public Path tempDir;

    @Value("${app.ocfl.root}")
    private String ocflRepoRoot;

    @Value("${app.ocfl.work}")
    private Path ocflWorkDir;

    @Value("${app.ocfl.ibm.endpoint}")
    private String ibmCosEndpoint;

    @Value("${app.ocfl.ibm.access-key}")
    private String ibmCosAccessKey;

    @Value("${app.ocfl.ibm.secret-key}")
    private String ibmCosSecretKey;

    @Value("${app.ocfl.ibm.bucket}")
    private String ibmCosBucket;

    @Value("${app.ocfl.ibm.prefix}")
    private String ibmCosPrefix;

    @Value("${app.aws.access-key}")
    private String awsAccessKey;

    @Value("${app.aws.secret-key}")
    private String awsSecretKey;

    @Value("${app.aws.region}")
    private Region awsRegion;

    @Value("${app.aws.s3.bucket}")
    public String s3GlacierBucket;

    @Value("${app.ocfl.db.url}")
    private String ocflDbUrl;

    @Value("${app.ocfl.db.user}")
    private String ocflDbUser;

    @Value("${app.ocfl.db.password}")
    private String ocflDbPassword;

    @Value("${app.fits.required}")
    private boolean fitsRequired;

    @Value("${app.clamav.required}")
    private boolean clamavRequired;

    @Value("${app.fits.url}")
    private String fitsUrl;

    @Value("${app.batch.object.parallelism}")
    private int objectParallelism;

    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    @Order(1)
    public RequestIdFilter requestIdFilter() {
        return new RequestIdFilter();
    }

    @Bean
    @Order(2)
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }

    @Bean
    @Profile({"!itest"})
    public ExecutorService eventBusExecutor() {
        // Spring will handle shutdown
        return Executors.newCachedThreadPool();
    }

    @Bean
    @Profile({"!itest"})
    public EventBus eventBus() {
        return new AsyncEventBus(eventBusExecutor());
    }

    @Bean
    @Profile("itest")
    public EventBus syncEventBus() {
        // sync is needed so the tests work
        return new EventBus();
    }

    @Bean
    @Profile({"default", "itest", "docker"})
    public LockManager lockManager() {
        return new DefaultLockManager(acquireLockTimeout, TimeUnit.MILLISECONDS);
    }

    @Bean
    @Profile({"test", "prod"})
    public LockManager distributedLockManger(@Value("${app.redis.url}") String redisUrl) {
        var config = new Config();
        config.useSingleServer().setAddress(redisUrl);
        // Hold on to a lock for two minutes before releasing it due to inactivity
        config.setLockWatchdogTimeout(2 * 60 * 1000);
        return new RedisLockManager(Redisson.create(config), acquireLockTimeout, TimeUnit.MILLISECONDS);
    }

    @Bean
    public GlacierRemoteDataStore glacierRemoteDataStore(S3Client s3Client) {
        return new GlacierRemoteDataStore(s3Client, s3GlacierBucket);
    }

    @Bean
    @Profile({"!itest"})
    public LambdaClient lambdaClient() {
        return LambdaClient.builder()
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(awsAccessKey, awsSecretKey)))
                .region(awsRegion)
                .httpClientBuilder(ApacheHttpClient.builder().socketTimeout(LAMBDA_TIMEOUT_MINUTES))
                .build();
    }

    @Bean
    @Profile({"test", "docker", "default"})
    public GlacierVersionValidator glacierVersionValidatorTest(GlacierRemoteDataStore glacierRemoteDataStore,
                                                           LambdaClient lambdaClient) {
        return glacierVersionValidator(glacierRemoteDataStore, lambdaClient, "test");
    }

    @Bean
    @Profile({"prod"})
    public GlacierVersionValidator glacierVersionValidatorProd(GlacierRemoteDataStore glacierRemoteDataStore,
                                                           LambdaClient lambdaClient) {
        return glacierVersionValidator(glacierRemoteDataStore, lambdaClient, "prod");
    }

    private GlacierVersionValidator glacierVersionValidator(GlacierRemoteDataStore glacierRemoteDataStore,
                                                            LambdaClient lambdaClient,
                                                            String env) {
        return new DefaultGlacierVersionValidator(glacierRemoteDataStore,
                lambdaClient,
                s3GlacierBucket,
                List.of(
                        new LambdaFunction(256 * MB, "preservation-validator-256MB-" + env),
                        new LambdaFunction(512 * MB, "preservation-validator-512MB-" + env),
                        new LambdaFunction(1 * GB, "preservation-validator-1GB-" + env),
                        new LambdaFunction(2 * GB, "preservation-validator-2GB-" + env),
                        new LambdaFunction(5 * GB, "preservation-validator-5GB-" + env),
                        new LambdaFunction(10 * GB, "preservation-validator-10GB-" + env)
                ));
    }

    @Bean
    @Profile({"default", "test", "prod", "docker"})
    public S3Client s3GlacierClient() {
        return S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(awsAccessKey, awsSecretKey)))
                .region(awsRegion)
                .httpClientBuilder(ApacheHttpClient.builder())
                .build();
    }

    @Bean
    @Primary
    @Profile("itest")
    public S3Client testS3GlacierClient() {
        return S3Client.builder()
                .serviceConfiguration(config -> {
                    config.pathStyleAccessEnabled(true);
                })
                .endpointOverride(URI.create("http://localhost:7878"))
                .credentialsProvider(AnonymousCredentialsProvider.create())
                .region(Region.US_EAST_2)
                .httpClientBuilder(ApacheHttpClient.builder())
                .build();
    }

    @Bean
    @Profile({"!itest"})
    public OcflExtensionConfig storageLayoutConfig() {
        return new NTupleOmitPrefixStorageLayoutConfig().setDelimiter(":");
    }

    @Bean
    @Profile({"itest"})
    public OcflExtensionConfig testStorageLayoutConfig() {
        // keep the old config for tests because not all of the ids are uuids
        return new HashedNTupleLayoutConfig();
    }

    @Bean
    @Profile({"!itest"})
    public OcflStorageLayoutExtension ocflStorageLayout(OcflExtensionConfig storageLayoutConfig) {
        var layout = new NTupleOmitPrefixStorageLayoutExtension();
        layout.init(storageLayoutConfig);
        return layout;
    }

    @Bean
    @Profile({"itest"})
    public OcflStorageLayoutExtension testOcflStorageLayout(OcflExtensionConfig storageLayoutConfig) {
        var layout = new HashedNTupleLayoutExtension();
        layout.init(storageLayoutConfig);
        return layout;
    }

    @Bean(name = "localOcflRepo")
    @Profile({"default", "docker"})
    public OcflRepository localFsOcflRepository(OcflExtensionConfig storageLayoutConfig, MeterRegistry registry) throws IOException {
        var repo = new OcflRepositoryBuilder()
                .contentPathConstraints(ContentPathConstraints.cloud())
                .defaultLayoutConfig(storageLayoutConfig)
                .storage(storage -> storage.fileSystem(UncheckedFiles.createDirectories(Paths.get(ocflRepoRoot))))
                .workDir(Files.createDirectories(ocflWorkDir))
                .ocflConfig(new OcflConfig().setDefaultDigestAlgorithm(DigestAlgorithm.sha256)
                        .setOcflVersion(OcflVersion.OCFL_1_1))
                .build();

        return new OcflRepoMetrics(repo, "Filesystem", registry);
    }

    @Bean
    @Profile({"default", "docker"})
    public RawOcflStore localFsRawOcflStore(OcflStorageLayoutExtension layout) {
        return new FsRawOcflStore(Paths.get(ocflRepoRoot), layout);
    }

    @Bean
    @Profile({"test", "prod"})
    public CloudClient ibmCloudClient() {
        var s3Client = S3Client.builder()
                .region(Region.of("us"))
                .endpointOverride(URI.create(ibmCosEndpoint))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(ibmCosAccessKey, ibmCosSecretKey)))
                .serviceConfiguration(config -> config.pathStyleAccessEnabled(true))
                .build();

        return OcflS3Client.builder()
                .s3Client(s3Client)
                .bucket(ibmCosBucket)
                .repoPrefix(ibmCosPrefix)
                .build();
    }

    @Bean(name = "localOcflRepo")
    @Profile({"test", "prod"})
    public OcflRepository localCloudOcflRepository(CloudClient ibmCloudClient,
                                                   DataSource ocflDataSource,
                                                   OcflExtensionConfig storageLayoutConfig,
                                                   MeterRegistry registry) throws IOException {
        var repo = new OcflRepositoryBuilder()
                .contentPathConstraints(ContentPathConstraints.cloud())
                .defaultLayoutConfig(storageLayoutConfig)
                .objectDetailsDb(db -> db.dataSource(ocflDataSource))
                .objectLock(lock -> lock.dataSource(ocflDataSource))
                .storage(storage -> storage.cloud(ibmCloudClient))
                .workDir(Files.createDirectories(ocflWorkDir))
                .ocflConfig(config -> config.setDefaultDigestAlgorithm(DigestAlgorithm.sha256)
                        .setOcflVersion(OcflVersion.OCFL_1_1))
                .build();

        return new OcflRepoMetrics(repo, "IbmCos", registry);
    }

    @Bean
    @Profile({"test", "prod"})
    public RawOcflStore localCloudRawOcflStore(CloudClient ibmCloudClient, OcflStorageLayoutExtension layout) {
        return new S3RawOcflStore(ibmCloudClient, layout);
    }

    @Bean("ocflDataSource")
    @Profile({"default", "docker"})
    public DataSource testOcflDataSource() {
        var dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:h2:mem:test");
        return dataSource;
    }

    @Bean("ocflDataSource")
    @Profile({"test", "prod"})
    public DataSource ocflDataSource() {
        var dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(ocflDbUrl);
        dataSource.setUsername(ocflDbUser);
        dataSource.setPassword(ocflDbPassword);
        return dataSource;
    }

    @Bean
    public Pipeline<IngestBagContext> ingestBagPipeline(ManagerInternalApi managerClient, MeterRegistry registry) {
        return Pipeline.<IngestBagContext>builder()
                .addStep(new IngestBatchStatusUpdateStep(managerClient))
                .addStep(new ClamAVBagStep(clamav(), registry, clamavRequired))
                .addStep(new DirectorySetupStep(workDir()))
                .addStep(new ExplodeAndValidateBagStep())
                .addStep(new ReadMetadataStep(new FileMetadataSerializer()))
                .addStep(new CheckFitsStep(fits(), fitsRequired))
                .addStep(new ObjectIngestStep(ingestObjectPipeline(managerClient, registry), objectParallelism))
                .addStep(new RestartFitsStep(fits(), fitsRequired))
                .build();
    }

    @Bean
    public Pipeline<IngestObjectContext> ingestObjectPipeline(ManagerInternalApi managerClient,
                                                              MeterRegistry meterRegistry) {
        return Pipeline.<IngestObjectContext>builder()
                .addStep(new ObjectStatusUpdateStep(managerClient))
                .addStep(new ObjectDirectorySetupStep())
                .addStep(new IdentifyObjectFilesStep(managerClient))
                .addStep(new FitsStep(fits(),
                        XmlMapper.builder().defaultUseWrapper(false).build(),
                        meterRegistry,
                        fitsRequired,
                        new PhotoshopPropertiesFilter(),
                        new PronomIdentifier()))
                .addStep(new RegisterObjectFileDetailsStep(managerClient))
                .build();
    }

    @Bean
    public Fits fits() {
        return new FitsWeb(fitsUrl);
    }

    @Bean
    public ClamAV clamav() {
        return new ClamAV();
    }

    @Bean
    public WorkDirectory workDir() {
        return new WorkDirectory(workDir);
    }

    @Bean
    public ManagerInternalApi managerClient(MeterRegistry meterRegistry, RestTemplateExchangeTagsProvider tagProvider) {
        var restTemplate = RestTemplates.restTemplate(
                internalMgrUsername,
                internalMgrPassword,
                tempDir,
                objectMapper);
        new MetricsRestTemplateCustomizer(meterRegistry, tagProvider, "http.client.requests", AutoTimer.ENABLED)
                .customize(restTemplate);
        var apiClient = new ApiClient(restTemplate);
        apiClient.setBasePath(internalMgrBasePath);
        return new ManagerInternalApi(apiClient);
    }

}
