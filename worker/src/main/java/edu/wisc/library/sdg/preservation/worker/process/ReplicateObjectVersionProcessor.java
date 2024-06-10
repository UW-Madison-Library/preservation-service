package edu.wisc.library.sdg.preservation.worker.process;

import com.google.common.hash.Hashing;
import com.google.common.io.MoreFiles;
import edu.wisc.library.sdg.preservation.common.PreservationConstants;
import edu.wisc.library.sdg.preservation.common.exception.SafeRuntimeException;
import edu.wisc.library.sdg.preservation.common.util.Archiver;
import edu.wisc.library.sdg.preservation.common.util.ArgCheck;
import edu.wisc.library.sdg.preservation.common.util.UncheckedFiles;
import edu.wisc.library.sdg.preservation.common.util.UuidUtils;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.DataStore;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ObjectVersionReplicatedRequest;
import edu.wisc.library.sdg.preservation.worker.storage.remote.RemoteDataStore;
import edu.wisc.library.sdg.preservation.worker.util.ManagerRetrier;
import edu.wisc.library.sdg.preservation.worker.validation.VersionArchiveValidator;
import io.ocfl.api.OcflRepository;
import io.ocfl.api.model.ObjectVersionId;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class ReplicateObjectVersionProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(ReplicateObjectVersionProcessor.class);

    private static final String DESC_FILE_TMPL = """
            # This directory contains a single version of an OCFL object.
            object-id: %s
            vault: %s
            external-object-id: %s
            version: %s
            ocfl-version: %s""";

    private final OcflRepository localOcflRepo;
    private final ManagerInternalApi managerClient;
    private final RemoteDataStore glacierDataStore;
    private final VersionArchiveValidator archiveValidator;
    private final Path tempDir;

    @Autowired
    public ReplicateObjectVersionProcessor(@Qualifier("localOcflRepo") OcflRepository localOcflRepo,
                                           ManagerInternalApi managerClient,
                                           RemoteDataStore glacierDataStore,
                                           VersionArchiveValidator archiveValidator,
                                           @Value("#{appConfig.tempDir}") Path tempDir) {
        this.localOcflRepo = localOcflRepo;
        this.managerClient = managerClient;
        this.glacierDataStore = glacierDataStore;
        this.archiveValidator = archiveValidator;
        this.tempDir = tempDir;
    }

    public void replicateObjectVersion(Long jobId,
                                       String objectId,
                                       String vault,
                                       String externalObjectId,
                                       String persistenceVersion,
                                       DataStore source,
                                       DataStore destination) {

        ArgCheck.notNull(jobId, "jobId");
        ArgCheck.notBlank(objectId, "objectId");
        ArgCheck.notBlank(vault, "vault");
        ArgCheck.notBlank(externalObjectId, "externalObjectId");
        ArgCheck.notBlank(persistenceVersion, "persistenceVersion");

        if (source != DataStore.IBM_COS) {
            throw new UnsupportedOperationException("Currently, only replicating from IBM COS is supported. Found: " + source);
        }
        if (destination != DataStore.GLACIER) {
            throw new UnsupportedOperationException("Currently, only replicating to Glacier is supported. Found: " + destination);
        }

        ensureNoStorageProblems(objectId, persistenceVersion, source);

        var unprefixedId = UuidUtils.withoutPrefix(objectId);
        var jobDir = tempDir.resolve(String.format("job-%s", jobId));
        var exportDir = jobDir.resolve(unprefixedId);
        var exportVersionDir = exportDir.resolve(persistenceVersion);
        var archive = jobDir.resolve(unprefixedId + "-" + persistenceVersion + ".zip");

        // ensure the state of the work directories
        UncheckedFiles.deleteDirectory(jobDir);
        UncheckedFiles.createDirectories(exportDir);

        try {
            LOG.info("Exporting <{}, {}> to {} from {}", objectId, persistenceVersion, exportVersionDir, source);

            localOcflRepo.exportVersion(ObjectVersionId.version(objectId, persistenceVersion), exportVersionDir);

            writeDescriptionFile(objectId, vault, externalObjectId, persistenceVersion, exportDir);
            Archiver.archive(exportDir, archive, false);

            archiveValidator.validate(objectId, persistenceVersion, archive);

            var sha256Digest = sha256Digest(archive);

            LOG.info("Replicating <{}, {}> to {}", objectId, persistenceVersion, destination);

            var remoteId = glacierDataStore.writeObjectVersion(objectId, persistenceVersion, archive);

            try {
                ManagerRetrier.retry(() -> {
                    managerClient.objectVersionReplicated(new ObjectVersionReplicatedRequest()
                            .objectId(objectId)
                            .persistenceVersion(persistenceVersion)
                            .dataStore(edu.wisc.library.sdg.preservation.manager.client.internal.model.DataStore.fromValue(destination.getValue()))
                            .dataStoreKey(remoteId)
                            .sha256Digest(sha256Digest));
                });
            } catch (RuntimeException e) {
                throw new SafeRuntimeException(String.format("Successfully replicated <%s, %s> to %s," +
                                " but failed to update its status in the database." +
                                " The archive still exists remotely, and the database may need to be updated manually.",
                        objectId, persistenceVersion, destination), e);
            }
        } finally {
            UncheckedFiles.deleteDirectory(jobDir);
        }
    }

    private void writeDescriptionFile(String objectId,
                                      String vault,
                                      String externalObjectId,
                                      String persistenceVersion,
                                      Path directory) {
        var content = String.format(DESC_FILE_TMPL,
                objectId,
                vault,
                externalObjectId,
                persistenceVersion,
                localOcflRepo.config().getOcflVersion().getRawVersion());
        try {
            Files.writeString(directory.resolve(PreservationConstants.ARCHIVE_OCFL_CONTENT_FILENAME), content);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void ensureNoStorageProblems(String objectId, String persistenceVersion, DataStore source) {
        var response = managerClient.getObjectStorageProblems(objectId);

        if (response.getProblems() != null) {
            for (var problem : response.getProblems()) {
                if (problem.getDataStore() == source
                        && (StringUtils.isEmpty(problem.getPersistenceVersion())
                            || persistenceVersion.equals(problem.getPersistenceVersion()))) {
                    throw new SafeRuntimeException(String.format("Cannot replicate object %s version %s from %s due to storage problems",
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
