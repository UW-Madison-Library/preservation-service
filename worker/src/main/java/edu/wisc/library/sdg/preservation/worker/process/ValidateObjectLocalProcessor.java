package edu.wisc.library.sdg.preservation.worker.process;

import edu.wisc.library.sdg.preservation.common.util.ArgCheck;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.DataStore;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.Event;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.EventOutcome;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.EventType;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.LogEntry;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.LogLevel;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RecordJobLogsRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RecordPreservationEventRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.SetObjectStorageProblemsRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.StorageProblemType;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.VersionStorageProblem;
import edu.wisc.library.sdg.preservation.worker.util.ManagerRetrier;
import edu.wisc.library.sdg.preservation.worker.validation.OcflObjectValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;

@Component
public class ValidateObjectLocalProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(ValidateObjectLocalProcessor.class);

    private final OcflObjectValidator objectValidator;
    private final ManagerInternalApi managerClient;

    public ValidateObjectLocalProcessor(OcflObjectValidator objectValidator,
                                        ManagerInternalApi managerClient) {
        this.objectValidator = objectValidator;
        this.managerClient = managerClient;
    }

    public void validateObject(String objectId,
                               boolean contentFixityCheck) {
        ArgCheck.notBlank(objectId, "objectId");

        var response = managerClient.getObjectVersionStates(objectId);

        var result = objectValidator.validateObject(objectId,
                response.getVault(),
                response.getExternalObjectId(),
                response.getVersionStates(),
                contentFixityCheck);

        var updateRequest = new SetObjectStorageProblemsRequest()
                .objectId(objectId)
                .dataStore(DataStore.IBM_COS);

        var now = OffsetDateTime.now(ZoneOffset.UTC);
        var logs = new ArrayList<LogEntry>();

        if (result.hasObjectProblems() || result.hasVersionProblems()) {
            LOG.info("Object <{}> failed validation: {}", objectId, result);

            if (result.hasObjectProblems()) {
                updateRequest.objectProblem(mapProblem(result.getStatus()));
                result.getProblems().forEach(problem -> {
                    logs.add(log(problem));
                });
            } else {
                updateRequest.objectProblem(StorageProblemType.NONE);
            }

            if (result.hasVersionProblems()) {
                var versionProblems = new ArrayList<VersionStorageProblem>();

                result.getVersionResults().forEach(versionResult -> {
                    versionProblems.add(new VersionStorageProblem()
                            .persistenceVersion(versionResult.getPersistenceVersion())
                            .problem(mapProblem(versionResult.getStatus())));

                    versionResult.getProblems().forEach(problem -> {
                        logs.add(log(problem));
                    });
                });

                updateRequest.versionProblems(versionProblems);
            }
        } else {
            LOG.info("Object <{}> is valid", objectId);
            updateRequest.objectProblem(StorageProblemType.NONE);
        }

        var outcome = result.hasObjectProblems() || result.hasVersionProblems()
                ? EventOutcome.FAILURE : EventOutcome.SUCCESS;

        ManagerRetrier.retry(() -> {
            managerClient.recordPreservationEvent(new RecordPreservationEventRequest()
                    .objectId(objectId)
                    .event(new Event()
                            .type(EventType.VALIDATE_OBJ_LOCAL)
                            .outcome(outcome)
                            .eventTimestamp(now)
                            .logs(logs)));
        });

        ManagerRetrier.retry(() -> {
            managerClient.setObjectStorageProblems(updateRequest);
        });
    }

    private StorageProblemType mapProblem(OcflObjectValidator.ValidationProblem status) {
        return switch (status) {
            case NONE -> StorageProblemType.NONE;
            case MISSING -> StorageProblemType.MISSING;
            case CORRUPT -> StorageProblemType.CORRUPT;
        };
    }

    private LogEntry log(String message) {
        return new LogEntry()
                .level(LogLevel.ERROR)
                .message(message)
                .createdTimestamp(OffsetDateTime.now(ZoneOffset.UTC));
    }

}
