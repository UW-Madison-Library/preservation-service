package edu.wisc.library.sdg.preservation.manager.itest;

import edu.wisc.library.sdg.preservation.manager.client.internal.model.EventOutcome;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.EventType;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.LogEntry;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.LogLevel;
import edu.wisc.library.sdg.preservation.manager.client.model.DataStore;
import edu.wisc.library.sdg.preservation.manager.client.model.StorageProblemType;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValidateObjectITest extends ITestBase {

    @Test
    public void validateObjectSuccess() {
        setupBaseline();

        validateObject(defaultObject1);

        processJobs();

        var jobId = verifyValidateObject(defaultObject1);
        var events = defaultObject1Events();

        completeJob(jobId);

        setStorageProblems(defaultObject1, InternalAlias.PROBLEM_NONE, null);
        events.add(recordPreservationEvent(defaultObject1.prefixedInternalId(), EventType.VALIDATE_OBJ_LOCAL, EventOutcome.SUCCESS, null));

        assertEquals(0, getStorageProblems(defaultObject1).size());
        assertEquals(0, retrieveJobLogs(jobId).size());
        assertPreservationEvents(defaultObject1, events);
    }

    @Test
    public void validateObjectFailureObjectOnly() {
        setupBaseline();

        validateObject(defaultObject1);

        processJobs();

        var jobId = verifyValidateObject(defaultObject1);
        var events = defaultObject1Events();

        completeJob(jobId);

        setStorageProblems(defaultObject1, InternalAlias.PROBLEM_MISSING, null);
        var logs = List.of(log("object not found"));
        recordJobLogs(jobId, logs);
        events.add(recordPreservationEvent(defaultObject1.prefixedInternalId(), EventType.VALIDATE_OBJ_LOCAL, EventOutcome.FAILURE, logs));

        assertProblems(defaultObject1, problemIbm(StorageProblemType.MISSING, null));
        assertJobLogs(jobId, logs);
        assertPreservationEvents(defaultObject1, events);
    }

    @Test
    public void validateObjectFailureWithVersionFailures() {
        setupBaseline();

        validateObject(defaultObject1);

        processJobs();

        var jobId = verifyValidateObject(defaultObject1);

        completeJob(jobId);

        setStorageProblems(defaultObject1, InternalAlias.PROBLEM_CORRUPT, Map.of(
                "v1", InternalAlias.PROBLEM_CORRUPT
        ));
        var logs = List.of(log("object corrupt"), log("version corrupt"));
        recordJobLogs(jobId, logs);

        assertProblems(defaultObject1,
                problemIbm(StorageProblemType.CORRUPT, null),
                problemIbm(StorageProblemType.CORRUPT, "v1"));
        assertJobLogs(jobId, logs);
    }

    @Test
    public void validateObjectFailureWithOnlyVersionFailuresAndClearExisting() {
        setupBaseline();

        setStorageProblems(defaultObject1, InternalAlias.PROBLEM_CORRUPT, Map.of(
                "v1", InternalAlias.PROBLEM_CORRUPT,
                "v2", InternalAlias.PROBLEM_MISSING
        ));

        assertProblems(defaultObject1,
                problemIbm(StorageProblemType.CORRUPT, null),
                problemIbm(StorageProblemType.CORRUPT, "v1"),
                problemIbm(StorageProblemType.MISSING, "v2"));

        validateObject(defaultObject1);

        processJobs();

        var jobId = verifyValidateObject(defaultObject1);

        completeJob(jobId);

        setStorageProblems(defaultObject1, InternalAlias.PROBLEM_NONE, Map.of(
                "v1", InternalAlias.PROBLEM_CORRUPT
        ));
        var logs = List.of(log("version corrupt"));
        recordJobLogs(jobId, logs);

        assertProblems(defaultObject1,
                problemIbm(StorageProblemType.CORRUPT, "v1"));
        assertJobLogs(jobId, logs);
    }

    @Test
    public void validateObjectRemoteAllVersionsSuccess() {
        setupBaseline();

        validateObjectRemote(defaultObject1, null, DataStore.GLACIER);

        processJobs();

        var jobId1 = verifyValidateObjectRemote(defaultObject1, "v1", InternalAlias.GLACIER);
        var jobId2 = verifyValidateObjectRemote(defaultObject1, "v2", InternalAlias.GLACIER);
        var events = defaultObject1Events();

        completeJob(jobId1);
        completeJob(jobId2);

        setStorageProblems(defaultObject1, InternalAlias.PROBLEM_NONE, null);
        events.add(recordPreservationEvent(defaultObject1.prefixedInternalId(), EventType.VALIDATE_OBJ_VERSION_REMOTE, EventOutcome.SUCCESS, null));
        events.add(recordPreservationEvent(defaultObject1v2.prefixedInternalId(), EventType.VALIDATE_OBJ_VERSION_REMOTE, EventOutcome.SUCCESS, null));

        assertEquals(0, getStorageProblems(defaultObject1).size());
        assertEquals(0, retrieveJobLogs(jobId1).size());
        assertEquals(0, retrieveJobLogs(jobId2).size());
        assertPreservationEvents(defaultObject1, events);
    }

    @Test
    public void validateObjectRemoteSingleVersionSuccess() {
        setupBaseline();

        validateObjectRemote(defaultObject1, List.of("v2"), DataStore.GLACIER);

        processJobs();

        var jobId1 = verifyValidateObjectRemote(defaultObject1, "v2", InternalAlias.GLACIER);
        var events = defaultObject1Events();

        completeJob(jobId1);

        setStorageProblems(defaultObject1, InternalAlias.PROBLEM_NONE, null);
        events.add(recordPreservationEvent(defaultObject1.prefixedInternalId(), EventType.VALIDATE_OBJ_VERSION_REMOTE, EventOutcome.SUCCESS, null));

        assertEquals(0, getStorageProblems(defaultObject1).size());
        assertEquals(0, retrieveJobLogs(jobId1).size());
        assertPreservationEvents(defaultObject1, events);
    }

    private LogEntry log(String message) {
        return new LogEntry()
                .level(LogLevel.ERROR)
                .message(message)
                .createdTimestamp(OffsetDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.MICROS));
    }

}
