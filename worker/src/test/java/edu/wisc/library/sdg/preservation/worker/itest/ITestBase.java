package edu.wisc.library.sdg.preservation.worker.itest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.Hashing;
import com.google.common.net.UrlEscapers;
import edu.wisc.library.sdg.preservation.common.PreservationConstants;
import edu.wisc.library.sdg.preservation.common.model.PreservationMetadata;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.BatchCompleteAnalysisRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.BatchCompleteIngestingRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.BatchCompleteRejectingRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.BatchStartAnalysisRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.BatchStartIngestingRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.BatchStartRejectingRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.CleanupSipsJob;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.CreateObjectVersionResponse;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.DataStore;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.DeleteDipJob;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.DescribeObjectVersionStatesResponse;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.EventOutcome;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.EventType;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.FinalizeObjectVersionRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.FinalizeRestoreJob;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.FormatRegistry;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.GetObjectStorageProblemsResponse;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.IngestBatch;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.IngestBatchState;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.JobPollResponse;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.JobState;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.JobType;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.LogEntry;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.LogLevel;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ObjectCompleteAnalysisRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ObjectCompleteIngestingRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ObjectFile;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ObjectStartIngestingRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ObjectStorageProblem;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ObjectVersionState;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ObjectVersionStateState;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.Outcome;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ProcessBatchJob;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RegisterIngestObjectFileRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RegisterIngestObjectFileResponse;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RegisterIngestObjectRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RegisterIngestObjectResponse;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ReplicateJob;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RestoreJob;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RetrieveBatchIngestResponse;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RetrieveBatchResponse;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RetrieveJob;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RetrieveNewInVersionFilesResponse;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.SetObjectStorageProblemsRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ShouldDeleteBatchRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ShouldDeleteBatchResponse;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.StorageProblemType;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.UpdateJobStateRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ValidateLocalJob;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ValidateRemoteJob;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.VersionStorageProblem;
import edu.wisc.library.sdg.preservation.worker.job.JobPoller;
import edu.wisc.library.sdg.preservation.worker.tools.clamav.ClamAV;
import edu.wisc.library.sdg.preservation.worker.tools.fits.Fits;
import edu.wisc.library.sdg.preservation.worker.util.WorkDirectory;
import edu.wisc.library.sdg.preservation.worker.validation.GlacierVersionValidator;
import io.ocfl.api.OcflRepository;
import io.ocfl.api.model.DigestAlgorithm;
import io.ocfl.api.model.ObjectVersionId;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "logging.level.edu.wisc.library.sdg.preservation=WARN"
        })
@ActiveProfiles({"default", "itest"})
public class ITestBase {

    protected static final String TEST_ORG = "test-organization";
    protected static final String DEFAULT_VAULT = "test-vault";

    protected static final String S3_BUCKET = "s3-test-bucket";

    private static final AtomicLong SERIAL = new AtomicLong(1L);

    public static final String FITS_ID = "FITS-1.6.0";
    public static final String JHOVE_ID = FITS_ID + ":Jhove-1.26.1";
    public static final String DROID_ID = FITS_ID + ":Droid-6.5.2";
    public static final String TIKA_ID = FITS_ID + ":Tika-2.6.0";
    public static final String FILEUTIL_ID = FITS_ID + ":file utility-5.43";
    public static final String EXIFTOOL_ID = FITS_ID + ":Exiftool-12.50";
    public static final String PRES_WORKER_ID = "Preservation Worker";

    @LocalServerPort
    protected Integer port;

    @MockBean
    protected ManagerInternalApi managerApi;

    @MockBean
    protected GlacierVersionValidator glacierVersionValidator;

    @Value("${app.ocfl.root}")
    protected String ocflRepoRoot;

    @Autowired
    protected WorkDirectory workDirectory;

    @Autowired
    @Qualifier("localOcflRepo")
    protected OcflRepository ocflRepository;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected S3Client s3Client;

    @Autowired
    protected Fits fits;

    @Autowired
    protected ClamAV clamav;

    @Autowired
    protected JobPoller jobPoller;

    @TempDir
    public Path tempDir;

    protected Long ingestId;

    protected boolean fitsExists;

    @BeforeEach
    public void setupBase() {
        ingestId = randomSerialId();
        fitsExists = fits.exists();
    }

    @AfterEach
    public void afterBase() {
        workDirectory.deleteBatchDirectory(ingestId);
        ocflRepository.listObjectIds().forEach(id -> {
            ocflRepository.purgeObject(id);
        });
    }

    protected Long randomSerialId() {
        return Math.abs(ThreadLocalRandom.current().nextLong());
    }

    protected TestObject testObject1(Long ingestId, String extId) {
        return new TestObject(ingestId, extId, 1, new ArrayList<>(List.of(
                new TestFile("DC", "8e0a08d4b5e2d05019859082c60b7a589ffc12f95a55b17b521bca2ad6bf9926", 407L)
                        .withFormat(FormatRegistry.MIME, JHOVE_ID, "text/xml")
                        .withEncoding(ITestBase.JHOVE_ID, "UTF-8")
                        .withEncoding(ITestBase.FILEUTIL_ID, "US-ASCII")
                        .withEncoding(ITestBase.TIKA_ID, "ISO-8859-1")
                        .withValidity(ITestBase.JHOVE_ID, true, true),
                new TestFile("METHODMAP", "23beae1f8f82b83f6be9c40445fa535cd4c73f14b8433640ecda6a76911af558", 399L)
                        .withFormat(FormatRegistry.MIME, JHOVE_ID, "text/xml")
                        .withEncoding(ITestBase.JHOVE_ID, "UTF-8")
                        .withEncoding(ITestBase.FILEUTIL_ID, "US-ASCII")
                        .withEncoding(ITestBase.TIKA_ID, "ISO-8859-1")
                        .withValidity(ITestBase.JHOVE_ID, true, true),
                new TestFile("RELS-EXT", "9d603228bc05e508744dbe525da9bfd50693d58123c51e3089ae67d596012de8", 355L)
                        .withFormat(FormatRegistry.MIME, JHOVE_ID, "text/xml")
                        .withEncoding(ITestBase.JHOVE_ID, "UTF-8")
                        .withEncoding(ITestBase.FILEUTIL_ID, "US-ASCII")
                        .withEncoding(ITestBase.TIKA_ID, "ISO-8859-1")
                        .withValidity(ITestBase.JHOVE_ID, true, true)
        )));
    }

    protected TestObject testObject1V2(Long ingestId, String extId) {
        return new TestObject(ingestId, extId, 2, new ArrayList<>(List.of(
                new TestFile("DC", "c895a23cfa06e7c79ae235434f0a7ac989c1cfe8448ae15d426752780a5ddde6", 417L)
                        .withFormat(FormatRegistry.MIME, JHOVE_ID, "text/xml")
                        .withEncoding(ITestBase.JHOVE_ID, "UTF-8")
                        .withEncoding(ITestBase.FILEUTIL_ID, "US-ASCII")
                        .withEncoding(ITestBase.TIKA_ID, "ISO-8859-1")
                        .withValidity(ITestBase.JHOVE_ID, true, true),
                new TestFile("RELS-EXT", "9d603228bc05e508744dbe525da9bfd50693d58123c51e3089ae67d596012de8", 355L)
                        .withFormat(FormatRegistry.MIME, JHOVE_ID, "text/xml")
                        .withEncoding(ITestBase.JHOVE_ID, "UTF-8")
                        .withEncoding(ITestBase.FILEUTIL_ID, "US-ASCII")
                        .withEncoding(ITestBase.TIKA_ID, "ISO-8859-1")
                        .withValidity(ITestBase.JHOVE_ID, true, true)
        )));
    }

    protected TestObject testObject2(Long ingestId, String extId) {
        return new TestObject(ingestId, extId, 1, new ArrayList<>(List.of(
                new TestFile("file1.txt", "649b8b471e7d7bc175eec758a7006ac693c434c8297c07db15286788c837154a", 21L)
                        .withFormat(FormatRegistry.MIME, DROID_ID, "text/plain")
                        .withFormat(FormatRegistry.PRONOM, DROID_ID, "x-fmt/111")
                        .withEncoding(ITestBase.FILEUTIL_ID, "US-ASCII")
                        .withEncoding(ITestBase.TIKA_ID, "ISO-8859-1")
                        .withValidity(ITestBase.JHOVE_ID, true, true),
                new TestFile("sub/file2.txt", "54e2a0fdb7dd042137a115e5944adfab10f3bafa9fab4450cb876330b2a889c5", 28L)
                        .withFormat(FormatRegistry.MIME, DROID_ID, "text/plain")
                        .withFormat(FormatRegistry.PRONOM, DROID_ID, "x-fmt/111")
                        .withEncoding(ITestBase.FILEUTIL_ID, "US-ASCII")
                        .withEncoding(ITestBase.TIKA_ID, "ISO-8859-1")
                        .withValidity(ITestBase.JHOVE_ID, true, true)
        )));
    }

    protected void assertOcflFiles(TestObject object) {
        var desc = ocflRepository.describeVersion(ObjectVersionId.head(object.internalObjectId));

        assertEquals(object.approverName, desc.getVersionInfo().getUser().getName());
        assertEquals(object.approverAddress, desc.getVersionInfo().getUser().getAddress());

        var totalFiles = object.files.size() + 2;

        if (fitsExists) {
            // There should be a FITS file for every file plus metadata.json and premis.xml
            totalFiles = object.files.size() * 2 + 2;
        }

        assertEquals(totalFiles, desc.getFiles().size());

        object.files.forEach(file -> {
            var ocflPath = PreservationConstants.OCFL_OBJECT_OBJECT_DIR + "/" + file.path;
            assertTrue(desc.containsFile(ocflPath), "Contains " + ocflPath);
            assertEquals(file.digest, desc.getFile(ocflPath).getFixity().get(DigestAlgorithm.sha256));
        });

        assertTrue(desc.containsFile(PreservationConstants.SYSTEM_METADATA_PATH),
                "Contains metadata");
        try {
            var meta = objectMapper.readValue(
                    ocflRepository.getObject(ObjectVersionId.head(object.internalObjectId))
                            .getFile(PreservationConstants.SYSTEM_METADATA_PATH).getStream(),
                    PreservationMetadata.class);
            assertEquals(object.externalId, meta.getExternalObjectId());
            assertEquals(DEFAULT_VAULT, meta.getVault());
            assertEquals(object.version, meta.getVersion());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        assertTrue(desc.containsFile(PreservationConstants.SYSTEM_METADATA_PREMIS_PATH),
                "Contains premis file");

        if (fitsExists) {
            object.files.forEach(file -> {
                var path = String.format("%s/%s.fits.xml", PreservationConstants.OCFL_OBJECT_FITS_PATH, file.path);
                assertTrue(desc.containsFile( path), () -> String.format("%s should exist", path));
            });
        }
    }

    protected void assertCleanup(Path bagPath) {
        assertTrue(Files.notExists(bagPath), "Bag should not exist after ingest");
        assertTrue(Files.notExists(Paths.get("target/work", "batch-" + ingestId)), "Work dir should not exist");
    }

    protected void assertNotCleanup(Path bagPath) {
        assertTrue(Files.exists(bagPath), "Bag should exist after failed ingest");
        assertTrue(Files.exists(Paths.get("target/work", "batch-" + ingestId)), "Work dir should exist");
    }

    protected void mockRetrieveBatch(IngestBatch batch) {
        doReturn(new RetrieveBatchResponse().ingestBatch(batch))
                .when(managerApi).retrieveBatch(ingestId);
    }

    protected void mockRetrieveIngestBatch(TestObject... objects) {
        doReturn(new RetrieveBatchIngestResponse()
                .approvedObjects(Arrays.stream(objects).map(TestObject::asBatchObject).collect(Collectors.toList())))
                .when(managerApi).retrieveBatchIngest(ingestId);
    }

    protected void mockRegisterObject(TestObject object) {
        doReturn(new RegisterIngestObjectResponse().ingestObjectId(object.ingestObjectId))
                .when(managerApi).registerIngestObject(new RegisterIngestObjectRequest()
                        .ingestId(ingestId)
                        .externalObjectId(object.externalId)
                        .objectRootPath(objectRootPath(ingestId, object.externalId)));
    }

    protected void mockRegisterObjectFiles(TestObject object) {
        mockRegisterObjectFiles(object.ingestObjectId, object.files);
    }

    protected void mockRegisterObjectFiles(Long ingestObjectId, List<TestFile> files) {
        files.forEach(file -> {
            file.ingestFileId = SERIAL.getAndIncrement();
            doReturn(new RegisterIngestObjectFileResponse().ingestFileId(file.ingestFileId))
                    .when(managerApi).registerIngestObjectFile(new RegisterIngestObjectFileRequest()
                    .ingestObjectId(ingestObjectId)
                    .filePath(file.path));
        });
    }

    protected void mockCreateVersion(TestObject object) {
        doReturn(new CreateObjectVersionResponse()
                .objectVersionId(object.objectVersionId)
                .version(object.version))
                .when(managerApi).createObjectVersion(argThat(arg -> {
            if (arg.getIngestObjectId().equals(object.ingestObjectId)) {
                object.internalObjectId = arg.getObjectId();
                mockPremisFileAllVersions(object);
                return true;
            }
            return false;
        }));
    }

    protected void mockPremisFile(TestObject... objects) {
        var external = objects[0].externalId;
        var internal = objects[0].internalObjectId;

        var versions = Arrays.stream(objects).map(o -> o.version).collect(Collectors.toList());

        Path file;
        try {
            file = Files.createTempFile(tempDir, "premis", ".xml");
            Files.writeString(file, external);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        doReturn(file.toFile()).when(managerApi)
                .getPremisDocument(internal, versions);
    }

    protected void mockPremisFileAllVersions(TestObject object) {
        var external = object.externalId;
        var internal = object.internalObjectId;

        var versions = IntStream.range(1, object.version + 1).boxed().toList();

        Path file;
        try {
            file = Files.createTempFile(tempDir, "premis", ".xml");
            Files.writeString(file, external);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        doReturn(file.toFile()).when(managerApi)
                .getPremisDocument(internal, versions);
    }

    protected void mockGetVersionStates(TestObject object) {
        var response = new DescribeObjectVersionStatesResponse()
                .vault(DEFAULT_VAULT)
                .externalObjectId(object.externalId);

        var versionState = new ObjectVersionState()
                        .version(object.version)
                        .persistenceVersion("v1");

        object.files.forEach(file -> {
            versionState.putStateItem(file.path, new ObjectVersionStateState()
                    .sha256Digest(file.digest)
                    .fileSize(file.size));
        });

        response.addVersionStatesItem(versionState);

        doReturn(response).when(managerApi)
                .getObjectVersionStates(object.internalObjectId);
    }

    protected void mockNewInVersion(TestObject object, String persistenceVersion, String... newFiles) {
        List<String> files;
        if (newFiles.length != 0) {
            files = Arrays.asList(newFiles);
        } else {
            files = object.files.stream().map(f -> f.path).toList();
        }

        var response = new RetrieveNewInVersionFilesResponse().files(
                object.files.stream()
                        .filter(f -> files.contains(f.path))
                        .map(f -> new ObjectFile().filePath(f.path).sha256Digest(f.digest).fileSize(f.size))
                        .toList());

        doReturn(response).when(managerApi)
                .retrieveNewInVersionFiles(object.internalObjectId, persistenceVersion);
    }

    protected void mockShouldDeleteBatch(Path sipPath,
                                         Long ingestId,
                                         ShouldDeleteBatchResponse.VerdictEnum verdict) {
        var response = new ShouldDeleteBatchResponse()
                .ingestId(ingestId)
                .verdict(verdict);
        doReturn(response)
                .when(managerApi)
                .shouldDeleteBatch(new ShouldDeleteBatchRequest()
                        .sipPath(sipPath.toString()));
    }

    protected void mockShouldDeleteBatch(Long ingestId,
                                         ShouldDeleteBatchResponse.VerdictEnum verdict) {
        var response = new ShouldDeleteBatchResponse()
                .ingestId(ingestId)
                .verdict(verdict);
        doReturn(response)
                .when(managerApi)
                .shouldDeleteBatch(new ShouldDeleteBatchRequest()
                        .ingestId(ingestId));
    }

    protected void mockNoStorageProblems(TestObject object) {
        doReturn(new GetObjectStorageProblemsResponse())
                .when(managerApi)
                .getObjectStorageProblems(object.internalObjectId);
    }

    protected void mockStorageProblems(TestObject object,
                                       DataStore dataStore,
                                       StorageProblemType objectProblem,
                                       Map<String, StorageProblemType> versionProblems) {
        var response = new GetObjectStorageProblemsResponse();

        if (objectProblem != StorageProblemType.NONE) {
            response.addProblemsItem(new ObjectStorageProblem()
                    .problem(objectProblem)
                    .dataStore(dataStore));
        }

        versionProblems.forEach((version, problem) -> {
            response.addProblemsItem(new ObjectStorageProblem()
                    .persistenceVersion(version)
                    .problem(problem)
                    .dataStore(dataStore));
        });

        doReturn(response)
                .when(managerApi)
                .getObjectStorageProblems(object.internalObjectId);
    }

    protected void verifyBatchStartAnalysis() {
        verify(managerApi).batchStartAnalysis(new BatchStartAnalysisRequest()
                .ingestId(ingestId));
    }

    protected void verifyBatchCompleteAnalysis(boolean success) {
        verify(managerApi).batchCompleteAnalysis(new BatchCompleteAnalysisRequest()
                .ingestId(ingestId)
                .outcome(success ? Outcome.SUCCESS : Outcome.FAILURE));
    }

    protected void verifyBatchStartIngesting() {
        verify(managerApi).batchStartIngesting(new BatchStartIngestingRequest()
                .ingestId(ingestId));
    }

    protected void verifyBatchCompleteIngesting(boolean success) {
        verify(managerApi).batchCompleteIngesting(new BatchCompleteIngestingRequest()
                .ingestId(ingestId)
                .outcome(success ? Outcome.SUCCESS : Outcome.FAILURE));
    }

    protected void verifyBatchStartRejecting() {
        verify(managerApi).batchStartRejecting(new BatchStartRejectingRequest()
                .ingestId(ingestId));
    }

    protected void verifyBatchCompleteRejecting() {
        verify(managerApi).batchCompleteRejecting(new BatchCompleteRejectingRequest()
                .ingestId(ingestId));
    }

    protected void verifyObjectStartIngesting(String externalObjectId) {
        verify(managerApi).objectStartIngesting(new ObjectStartIngestingRequest()
                .ingestId(ingestId)
                .externalObjectId(externalObjectId));
    }

    protected void verifyObjectCompleteIngesting(String externalObjectId, boolean success) {
        verify(managerApi).objectCompleteIngesting(new ObjectCompleteIngestingRequest()
                .ingestId(ingestId)
                .externalObjectId(externalObjectId)
                .outcome(success ? ObjectCompleteIngestingRequest.OutcomeEnum.SUCCESS
                        : ObjectCompleteIngestingRequest.OutcomeEnum.FAILURE));
    }

    protected void verifyObjectCompleteIngestingNoChange(String externalObjectId) {
        verify(managerApi).objectCompleteIngesting(new ObjectCompleteIngestingRequest()
                .ingestId(ingestId)
                .externalObjectId(externalObjectId)
                .outcome(ObjectCompleteIngestingRequest.OutcomeEnum.NO_CHANGE));
    }

    protected void verifyObjectCompleteAnalysis(String externalObjectId, boolean success) {
        verify(managerApi).objectCompleteAnalysis(new ObjectCompleteAnalysisRequest()
                .ingestId(ingestId)
                .externalObjectId(externalObjectId)
                .outcome(success ? Outcome.SUCCESS : Outcome.FAILURE));
    }

    protected void verifyRegisterFileDetails(TestObject object) {
        verifyRegisterFileDetails(object.files);
    }

    protected void verifyRegisterFileDetails(List<TestFile> files) {
        files.forEach(file -> {
            verify(managerApi).registerIngestObjectFileDetails(argThat(arg -> {
                var match = Objects.equals(file.ingestFileId, arg.getIngestFileId())
                        && Objects.equals(file.digest, arg.getSha256Digest())
                        && Objects.equals(file.size, arg.getFileSize());

                if (match && fitsExists) {
                    match = arg.getFormats().size() == file.formats.size() &&
                            arg.getFormats().containsAll(file.formats.stream()
                                    .map(TestFormat::asIngestFileFormat)
                                    .collect(Collectors.toList()));
                    match &= arg.getEncoding().size() == file.encodings.size() &&
                            arg.getEncoding().containsAll(file.encodings.stream()
                                    .map(TestEncoding::asIngestFileEncoding)
                                    .collect(Collectors.toList()));
                    match &= arg.getValidity().size() == file.validity.size() &&
                            arg.getValidity().containsAll(file.validity.stream()
                                    .map(TestValidity::asIngestFileValidity)
                                    .collect(Collectors.toList()));
                }

                return match;
            }));
        });
    }

    protected void verifySetJobState(Long jobId, JobState state) {
        verify(managerApi).updateJobState(new UpdateJobStateRequest()
                .jobId(jobId)
                .state(state));
    }

    protected void verifyEvent(EventType type,
                               EventOutcome outcome,
                               Long ingestId,
                               List<LogEntry> logs) {
        verifyEvent(type, outcome, ingestId, null, logs);
    }

    protected void verifyEvent(EventType type,
                               EventOutcome outcome,
                               Long ingestId,
                               String externalObjectId,
                               List<LogEntry> logs) {
        verify(managerApi).recordIngestEvent(argThat(arg -> {
            if (!(Objects.equals(ingestId, arg.getIngestId())
                    && Objects.equals(externalObjectId, arg.getExternalObjectId()))) {
                return false;
            }

            var event = arg.getEvent();

            if (!(Objects.equals(type, event.getType())
                    && Objects.equals(outcome, event.getOutcome()))) {
                return false;
            }

            if (CollectionUtils.isNotEmpty(event.getLogs())) {
                event.getLogs().forEach(log -> log.setCreatedTimestamp(null));
                return logs.containsAll(event.getLogs());
            } else {
                return CollectionUtils.isEmpty(logs);
            }
        }));
    }

    protected void verifyNoEvents() {
        verify(managerApi, never()).recordIngestEvent(any());
    }

    protected void verifyPreservationEvent(EventType type,
                                           EventOutcome outcome,
                                           String objectId,
                                           List<LogEntry> logs) {
        verify(managerApi).recordPreservationEvent(argThat(arg -> {
            if (!Objects.equals(objectId, arg.getObjectId())) {
                return false;
            }

            var event = arg.getEvent();

            if (!(Objects.equals(type, event.getType())
                    && Objects.equals(outcome, event.getOutcome()))) {
                return false;
            }

            if (CollectionUtils.isNotEmpty(event.getLogs())) {
                event.getLogs().forEach(log -> log.setCreatedTimestamp(null));
                return logs.containsAll(event.getLogs());
            } else {
                return CollectionUtils.isEmpty(logs);
            }
        }));
    }

    protected void verifyNoPreservationEvents() {
        verify(managerApi, never()).recordPreservationEvent(any());
    }

    protected void verifyJobLogging(long jobId, LogEntry... logs) {
        if (logs == null || logs.length == 0) {
            verify(managerApi, never()).recordJobLogs(any());
        } else {
            verify(managerApi).recordJobLogs(argThat(arg -> {
                if (arg.getJobId() != jobId || arg.getLogEntries() == null) {
                    return false;
                }
                arg.getLogEntries().forEach(log -> log.setCreatedTimestamp(null));
                return arg.getLogEntries().containsAll(Arrays.asList(logs));
            }));
        }
    }

    protected LogEntry logEntry(LogLevel level, String message) {
        return new LogEntry().level(level).message(message);
    }

    protected void verifySetStorageProblemsLocal(TestObject object,
                                                 StorageProblemType objectProblem,
                                                 Map<String, StorageProblemType> versionProblems) {
        verifySetStorageProblems(object, DataStore.IBM_COS, objectProblem, versionProblems);
    }
    
    protected void verifySetStorageProblemsGlacier(TestObject object,
                                                   StorageProblemType objectProblem,
                                                   Map<String, StorageProblemType> versionProblems) {
        verifySetStorageProblems(object, DataStore.GLACIER, objectProblem, versionProblems);
    }

    protected void verifySetStorageProblems(TestObject object,
                                            DataStore dataStore,
                                            StorageProblemType objectProblem,
                                            Map<String, StorageProblemType> versionProblems) {
        var request = new SetObjectStorageProblemsRequest()
                .objectId(object.internalObjectId)
                .dataStore(dataStore)
                .objectProblem(objectProblem);

        if (versionProblems != null) {
            versionProblems.forEach((version, problem) -> {
                request.addVersionProblemsItem(new VersionStorageProblem()
                        .persistenceVersion(version)
                        .problem(problem));
            });
        }

        verify(managerApi).setObjectStorageProblems(request);
    }

    protected void verifyVersionFinalize(TestObject object) {
        verify(managerApi).finalizeObjectVersion(new FinalizeObjectVersionRequest()
                .objectVersionId(object.objectVersionId)
                .ingestObjectId(object.ingestObjectId)
                .persistenceVersion("v" + object.version));
    }

    protected void verifySuccessfulBatchIngest() {
        verifyBatchStartIngesting();
        verifyBatchCompleteIngesting(true);
    }

    protected void verifySuccessfulBatchReject() {
        verifyBatchStartRejecting();
        verifyBatchCompleteRejecting();
    }

    protected void verifySuccessfulObjectIngest(TestObject object) {
        verifyObjectCompleteIngesting(object.externalId, true);
        verifyVersionFinalize(object);
    }

    protected Path setupBag(String name) {
        var src = Paths.get("src/test/resources/itest/bags/" + name);
        var dst = tempDir.resolve(name);
        try {
            Files.copy(src, dst);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return dst;
    }

    protected String objectRootPath(Long ingestId, String objectId) {
        return Paths.get("target/work", "batch-" + ingestId, "source/bag/data",
                UrlEscapers.urlFormParameterEscaper().escape(objectId)).toAbsolutePath().toString();
    }

    protected IngestBatch batch(Path bagPath) {
        return new IngestBatch()
                .ingestId(ingestId)
                .filePath(bagPath.toString())
                .vault(DEFAULT_VAULT)
                .orgName(TEST_ORG)
                .state(IngestBatchState.RECEIVED);
    }

    protected void retrieveObjects(Long jobId, TestObject... objects) {
        var job = new RetrieveJob().jobId(jobId);
        for (var object : objects) {
            job.addObjectsItem(object.asObjectInfo());
        }
        jobPoller.processJob(new JobPollResponse()
                .jobType(JobType.RETRIEVE_OBJECTS)
                .retrieveJob(job));
    }

    protected void replicateObject(Long jobId, TestObject object) {
        mockNoStorageProblems(object);
        replicateObjectNoProblemMock(jobId, object);
    }

    protected void replicateObjectNoProblemMock(Long jobId, TestObject object) {
        jobPoller.processJob(new JobPollResponse()
                        .jobType(JobType.REPLICATE)
                        .replicateJob(new ReplicateJob()
                                .jobId(jobId)
                                .internalObjectId(object.internalObjectId)
                                .vault(DEFAULT_VAULT)
                                .externalObjectId(object.externalId)
                                .persistenceVersion("v" + object.version)
                                .source(DataStore.IBM_COS)
                                .destination(DataStore.GLACIER)));
    }

    protected void restoreObject(Long jobId, TestObject object, String digest) {
        mockNoStorageProblems(object);
        restoreObjectNoProblemMock(jobId, object, digest);
    }

    protected void restoreObjectNoProblemMock(Long jobId, TestObject object, String digest) {
        jobPoller.processJob(new JobPollResponse()
                        .jobType(JobType.RESTORE)
                        .restoreJob(new RestoreJob()
                                .jobId(jobId)
                                .internalObjectId(object.internalObjectId)
                                .persistenceVersion("v" + object.version)
                                .source(DataStore.GLACIER)
                                .key(glacierKey(object))
                                .sha256Digest(digest)));
    }

    protected void finalizeRestore(Long jobId, TestObject object) {
        jobPoller.processJob(new JobPollResponse()
                        .jobType(JobType.FINALIZE_RESTORE)
                        .finalizeRestoreJob(new FinalizeRestoreJob()
                                .jobId(jobId)
                                .internalObjectId(object.internalObjectId)));
    }

    protected void validateObject(Long jobId, TestObject object) {
        jobPoller.processJob(new JobPollResponse()
                .jobType(JobType.VALIDATE_LOCAL)
                .validateLocalJob(new ValidateLocalJob()
                        .internalObjectId(object.internalObjectId)
                        .jobId(jobId)
                        .contentFixityCheck(true)));
    }

    protected void validateRemote(Long jobId, TestObject object) {
        jobPoller.processJob(new JobPollResponse()
                .jobType(JobType.VALIDATE_REMOTE)
                .validateRemoteJob(new ValidateRemoteJob()
                        .internalObjectId(object.internalObjectId)
                        .jobId(jobId)
                        .persistenceVersion("v" + object.version)
                        .dataStore(DataStore.GLACIER)
                        .dataStoreKey(glacierKey(object))
                        .sha256Digest(glacierDigest(object))));
    }

    protected void deleteDip(Long jobId, Long retrieveRequestId, List<Long> retrieveJobIds) {
        jobPoller.processJob(new JobPollResponse()
                .jobType(JobType.DELETE_DIP)
                .deleteDipJob(new DeleteDipJob()
                        .jobId(jobId)
                        .retrieveRequestId(retrieveRequestId)
                        .retrieveJobIds(retrieveJobIds)));
    }

    protected void cleanupSips(Long jobId) {
        jobPoller.processJob(new JobPollResponse()
                .jobType(JobType.CLEANUP_SIPS)
                .cleanupSipsJob(new CleanupSipsJob()
                        .jobId(jobId)));
    }

    protected void processBatch(Long ingestId) {
        jobPoller.processJob(new JobPollResponse()
                        .jobType(JobType.PROCESS_BATCH)
                        .processBatchJob(new ProcessBatchJob()
                                .jobId(randomSerialId())
                                .ingestId(ingestId)));
    }

    protected String verifyReplication(TestObject object, String key) {
        var digest = new AtomicReference<String>();
        verify(managerApi).objectVersionReplicated(argThat(arg -> {
            if (object.internalObjectId.equals(arg.getObjectId())
                    && ("v" + object.version).equals(arg.getPersistenceVersion())
                    && key.equals(arg.getDataStoreKey())) {
                digest.set(arg.getSha256Digest());
                return true;
            }
            return false;
        }));
        return digest.get();
    }

    protected void ingestObjects(String bag, TestObject... objects) {
        var bagPath = setupBag(bag);
        ingestObjects(bagPath, objects);
    }

    protected void ingestObjects(Path bagPath, TestObject... objects) {
        var batch = batch(bagPath);

        // Analyze Batch

        mockRetrieveBatch(batch);
        for (var object : objects) {
            mockRegisterObject(object);
            mockRegisterObjectFiles(object);
        }

        processBatch(ingestId);

        // Ingest Batch

        reset(managerApi);

        batch.state(IngestBatchState.PENDING_INGESTION).reviewedBy("Peter");

        mockRetrieveBatch(batch);
        mockRetrieveIngestBatch(objects);
        for (var object : objects) {
            mockCreateVersion(object);
        }

        processBatch(ingestId);
    }

    protected String glacierKey(TestObject object) {
        return String.format("%s/%s-v%s.zip",
                object.unprefixedInternalObjectId(), object.unprefixedInternalObjectId(), object.version);
    }

    protected String glacierDigest(TestObject object) {
        var key = glacierKey(object);
        return Hashing.sha256().hashString(key, StandardCharsets.UTF_8).toString();
    }
    
}
