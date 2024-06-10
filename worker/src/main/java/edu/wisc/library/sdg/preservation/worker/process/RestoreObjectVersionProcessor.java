package edu.wisc.library.sdg.preservation.worker.process;

import com.google.common.hash.Hashing;
import com.google.common.io.MoreFiles;
import edu.wisc.library.sdg.preservation.common.exception.SafeRuntimeException;
import edu.wisc.library.sdg.preservation.common.util.Archiver;
import edu.wisc.library.sdg.preservation.common.util.ArgCheck;
import edu.wisc.library.sdg.preservation.common.util.UncheckedFiles;
import edu.wisc.library.sdg.preservation.common.util.UuidUtils;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.DataStore;
import edu.wisc.library.sdg.preservation.worker.storage.ocfl.RawOcflStore;
import edu.wisc.library.sdg.preservation.worker.storage.remote.RemoteDataStore;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;

@Component
public class RestoreObjectVersionProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(RestoreObjectVersionProcessor.class);

    private final RemoteDataStore glacierDataStore;
    private final RawOcflStore rawOcflStore;
    private final ManagerInternalApi managerClient;
    private final Path tempDir;

    @Autowired
    public RestoreObjectVersionProcessor(RemoteDataStore glacierDataStore,
                                         RawOcflStore rawOcflStore,
                                         ManagerInternalApi managerClient,
                                         @Value("#{appConfig.tempDir}") Path tempDir) {
        this.glacierDataStore = glacierDataStore;
        this.rawOcflStore = rawOcflStore;
        this.managerClient = managerClient;
        this.tempDir = tempDir;
    }

    /**
     * Restores an object version from a remote source to the local repo. If the data is not ready in the remote source
     * false is returned.
     *
     * @param objectId the internal object id
     * @param persistenceVersion the ocfl version
     * @param source the remote data source
     * @param key the key in the remote
     * @param sha256Digest the expected digest of the remote file
     * @return false if the remote is not ready
     */
    public boolean restore(Long jobId,
                           String objectId,
                           String persistenceVersion,
                           DataStore source,
                           String key,
                           String sha256Digest) {
        ArgCheck.notNull(jobId, "jobId");
        ArgCheck.notBlank(objectId, "objectId");
        ArgCheck.notBlank(persistenceVersion, "persistenceVersion");
        ArgCheck.notNull(source, "source");
        ArgCheck.notBlank(key, "key");
        ArgCheck.notBlank(sha256Digest, "sha256Digest");

        if (source != DataStore.GLACIER) {
            throw new SafeRuntimeException("Currently, only restoring from Glacier is supported. Found: " + source);
        }

        ensureNoStorageProblems(objectId, persistenceVersion, source);

        var jobDir = UncheckedFiles.createDirectories(tempDir.resolve(String.format("job-%s", jobId)));
        var unprefixedId = UuidUtils.withoutPrefix(objectId);

        try {
            var file = jobDir.resolve(String.format("%s-%s.zip", unprefixedId, persistenceVersion));

            var isDownloaded = glacierDataStore.download(key, file);

            // The file wasn't available in Glacier, restore was requested, try again later
            if (!isDownloaded) {
                return false;
            }

            var actualDigest = sha256Digest(file);

            if (!sha256Digest.equalsIgnoreCase(actualDigest)) {
                throw new SafeRuntimeException(String.format(
                        "File %s from %s failed checksum validation. Expected: %s; Actual: %s",
                        key, source, sha256Digest, actualDigest));
            }

            Archiver.extract(file, jobDir);

            var sourceVersionDir = jobDir.resolve(unprefixedId).resolve(persistenceVersion);

            rawOcflStore.installVersion(objectId, persistenceVersion, sourceVersionDir);

            return true;
        } finally {
            UncheckedFiles.deleteDirectory(jobDir);
        }
    }

    private void ensureNoStorageProblems(String objectId, String persistenceVersion, DataStore source) {
        var response = managerClient.getObjectStorageProblems(objectId);

        if (response.getProblems() != null) {
            for (var problem : response.getProblems()) {
                if (problem.getDataStore() == source
                        && (StringUtils.isEmpty(problem.getPersistenceVersion())
                        || persistenceVersion.equals(problem.getPersistenceVersion()))) {
                    throw new SafeRuntimeException(String.format("Cannot restore object %s version %s from %s due to storage problems",
                            objectId, persistenceVersion, source));
                }
            }
        }
    }

    private String sha256Digest(Path file) {
        try {
            return MoreFiles.asByteSource(file).hash(Hashing.sha256()).toString();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
