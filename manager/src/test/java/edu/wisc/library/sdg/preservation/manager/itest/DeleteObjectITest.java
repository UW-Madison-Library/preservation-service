package edu.wisc.library.sdg.preservation.manager.itest;

import edu.wisc.library.sdg.preservation.common.exception.ValidationException;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.JobState;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.JobType;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RetrieveJob;
import edu.wisc.library.sdg.preservation.manager.client.model.ApproveIngestBatchRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.DataStore;
import edu.wisc.library.sdg.preservation.manager.client.model.PreservationObjectState;
import org.junit.jupiter.api.Test;

import java.util.List;

import static edu.wisc.library.sdg.preservation.manager.itest.InternalAlias.INTERNAL_ANALYZING;
import static edu.wisc.library.sdg.preservation.manager.itest.InternalAlias.INTERNAL_OBJ_PENDING_REVIEW;
import static edu.wisc.library.sdg.preservation.manager.itest.InternalAlias.INTERNAL_PENDING_REVIEW;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeleteObjectITest extends ITestBase {

    @Test
    public void deleteObjectShouldShowAsDeleted() {
        setupBaseline();

        deleteObject(defaultObject1.externalId, "testing");

        var object = describePreservationObject(defaultObject1.externalId, null);
        assertEquals(PreservationObjectState.DELETED, object.getState());
        assertPreservationEventsSubset(defaultObject1, List.of(deleteObjectEvent(defaultObject1, "testing")));
    }

    @Test
    public void deletedObjectCannotBeUpdated() {
        setupBaseline();
        deleteObject(defaultObject1.externalId, "testing");

        var bagName = "single-valid.zip";
        var ingestId = publicClient.ingestBag(DEFAULT_VAULT, bag(bagName)).getIngestId();
        updateBatchState(ingestId, INTERNAL_ANALYZING);
        registerObjectWithFiles(ingestId, defaultObject1);
        updateObjectStates(ingestId,
                INTERNAL_OBJ_PENDING_REVIEW,
                defaultObject1);
        updateBatchState(ingestId, INTERNAL_PENDING_REVIEW);

        assertEquals("Cannot ingest object o1 because it is marked as deleted.",
                assertThrows(ValidationException.class, () -> {
                    activeClient.approveIngestBatch(new ApproveIngestBatchRequest().ingestId(ingestId));
                }).getMessage());
    }

    @Test
    public void deletedObjectCannotBeReplicated() {
        setupBaseline();
        deleteObject(defaultObject1.externalId, "testing");
        var expectedMessage = String.format(
                "Cannot perform actions on object <%s> in vault <%s> because it is not in an active state",
                defaultObject1.externalId,
                DEFAULT_VAULT
        );

        assertEquals(expectedMessage,
                assertThrows(ValidationException.class, () -> {
                    replicateObject(defaultObject1, null, DataStore.GLACIER);
                }).getMessage());
    }

    @Test
    public void deletedObjectCannotBeRestored() {
        setupBaseline();
        deleteObject(defaultObject1.externalId, "testing");
        var expectedMessage = String.format(
                "Cannot perform actions on object <%s> in vault <%s> because it is not in an active state",
                defaultObject1.externalId,
                DEFAULT_VAULT
        );

        assertEquals(expectedMessage,
                assertThrows(ValidationException.class, () -> {
                    restoreObject(defaultObject1, null);
                }).getMessage());
    }

    @Test
    public void deletedObjectCannotBeValidated() {
        setupBaseline();
        deleteObject(defaultObject1.externalId, "testing");
        var expectedMessage = String.format(
                "Cannot perform actions on object <%s> in vault <%s> because it is not in an active state",
                defaultObject1.externalId,
                DEFAULT_VAULT
        );

        assertEquals(expectedMessage,
                assertThrows(ValidationException.class, () -> {
                    validateObject(defaultObject1);
                }).getMessage());
    }

    @Test
    public void deletedObjectCannotBeRetrieved() {
        setupBaseline();
        deleteObject(defaultObject1.externalId, "testing");

        var jobId = retrieveObjects(true, defaultObject1, defaultObject2).get(0);

        assertJobState(jobId, JobState.PENDING.getValue());
        updateJobState(jobId, JobState.EXECUTING);
        updateJobState(jobId, JobState.COMPLETE);
        assertJobState(jobId, JobState.COMPLETE.getValue());

        List<RetrieveJob> jobs = getJobsOfType(JobType.RETRIEVE_OBJECTS);
        assertEquals(1, jobs.size());

        assertRetrieveObjectsRequest(jobs.get(0), jobId, defaultObject2);
    }

}
