package edu.wisc.library.sdg.preservation.worker.process;

import edu.wisc.library.sdg.preservation.common.exception.SafeRuntimeException;
import edu.wisc.library.sdg.preservation.common.exception.ValidationException;
import edu.wisc.library.sdg.preservation.common.util.ArgCheck;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.DataStore;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.Event;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.EventOutcome;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.EventType;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.LogEntry;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.LogLevel;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RecordPreservationEventRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.SetObjectStorageProblemsRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.StorageProblemType;
import edu.wisc.library.sdg.preservation.worker.storage.remote.RemoteDataStore;
import edu.wisc.library.sdg.preservation.worker.util.ManagerRetrier;
import edu.wisc.library.sdg.preservation.worker.validation.GlacierVersionValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;

@Component
public class ValidateObjectRemoteProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(ValidateObjectRemoteProcessor.class);

    private final RemoteDataStore glacierDataStore;
    private final ManagerInternalApi managerClient;
    private final GlacierVersionValidator glacierVersionValidator;

    @Autowired
    public ValidateObjectRemoteProcessor(RemoteDataStore glacierDataStore,
                                         ManagerInternalApi managerClient,
                                         GlacierVersionValidator glacierVersionValidator) {
        this.glacierDataStore = glacierDataStore;
        this.managerClient = managerClient;
        this.glacierVersionValidator = glacierVersionValidator;
    }

    /**
     * Validates an object version archive in a remote data store. If the archive is not ready to be validated, false
     * is returned, and the validation should be tried again later.
     *
     * @param jobId the id of the validation job
     * @param objectId the internal object id
     * @param persistenceVersion the OCFL version
     * @param dataStore the data store to check
     * @param key the key for the archive in Glacier
     * @param sha256Digest the expected digest of the archive
     * @return true if the validation was performed; false if it should be deferred to try later
     */
    public boolean validate(Long jobId,
                            String objectId,
                            String persistenceVersion,
                            DataStore dataStore,
                            String key,
                            String sha256Digest) {
        ArgCheck.notNull(jobId, "jobId");
        ArgCheck.notBlank(objectId, "objectId");
        ArgCheck.notBlank(persistenceVersion, "persistenceVersion");
        ArgCheck.notNull(dataStore, "dataStore");
        ArgCheck.notBlank(key, "key");
        ArgCheck.notBlank(sha256Digest, "sha256Digest");

        if (dataStore != DataStore.GLACIER) {
            throw new SafeRuntimeException("Currently, only restoring from Glacier is supported. Found: " + dataStore);
        }

        var updateRequest = new SetObjectStorageProblemsRequest()
                .objectProblem(StorageProblemType.NONE)
                .objectId(objectId)
                .dataStore(dataStore);
        var outcome = EventOutcome.SUCCESS;
        var logs = new ArrayList<LogEntry>();
        logs.add(info(String.format("Validate OCFL version %s in %s", persistenceVersion, dataStore)));
        var now = OffsetDateTime.now(ZoneOffset.UTC);

        try {
            var isAvailable = glacierDataStore.initiateObjectVersionRestoration(key);

            // The file wasn't available in Glacier, restore was requested, try again later
            if (!isAvailable) {
                return false;
            }

            var expectedFiles = managerClient.retrieveNewInVersionFiles(objectId, persistenceVersion).getFiles();

            try {
                glacierVersionValidator.validate(objectId, persistenceVersion, key, sha256Digest, expectedFiles);
            } catch (ValidationException e) {
                outcome = EventOutcome.FAILURE;
                logs.add(error(e.getMessage()));
                updateRequest.setObjectProblem(StorageProblemType.CORRUPT);
            }
        } catch (NoSuchKeyException e) {
            outcome = EventOutcome.FAILURE;
            logs.add(error(String.format("OCFL version %s was not found in %s", persistenceVersion, dataStore)));
            updateRequest.setObjectProblem(StorageProblemType.MISSING);
        }

        var event = new Event()
                .type(EventType.VALIDATE_OBJ_VERSION_REMOTE)
                .outcome(outcome)
                .eventTimestamp(now)
                .logs(logs);

        ManagerRetrier.retry(() -> {
            managerClient.recordPreservationEvent(new RecordPreservationEventRequest()
                    .objectId(objectId)
                    .event(event));
        });

        ManagerRetrier.retry(() -> {
            managerClient.setObjectStorageProblems(updateRequest);
        });

        return true;
    }

    private LogEntry error(String message) {
        return new LogEntry()
                .level(LogLevel.ERROR)
                .message(message)
                .createdTimestamp(OffsetDateTime.now(ZoneOffset.UTC));
    }

    private LogEntry info(String message) {
        return new LogEntry()
                .level(LogLevel.INFO)
                .message(message)
                .createdTimestamp(OffsetDateTime.now(ZoneOffset.UTC));
    }

}
