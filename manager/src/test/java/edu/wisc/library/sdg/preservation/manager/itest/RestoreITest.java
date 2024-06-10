package edu.wisc.library.sdg.preservation.manager.itest;

import edu.wisc.library.sdg.preservation.common.exception.NotFoundException;
import edu.wisc.library.sdg.preservation.manager.client.model.ApproveIngestBatchRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.IngestObjectState;
import edu.wisc.library.sdg.preservation.manager.client.model.JobType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static edu.wisc.library.sdg.preservation.manager.itest.InternalAlias.INTERNAL_ANALYZING;
import static edu.wisc.library.sdg.preservation.manager.itest.InternalAlias.INTERNAL_INGESTING;
import static edu.wisc.library.sdg.preservation.manager.itest.InternalAlias.INTERNAL_OBJ_INGESTING;
import static edu.wisc.library.sdg.preservation.manager.itest.InternalAlias.INTERNAL_OBJ_PENDING_REVIEW;
import static edu.wisc.library.sdg.preservation.manager.itest.InternalAlias.INTERNAL_PENDING_REVIEW;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RestoreITest extends ITestBase {

    @Test
    public void restoreAllVersionsOfObjectWhenExistInRemote() {
        setupBaseline();

        restoreObject(defaultObject1, null);
        processJobs();

        var job1Id = verifyWorkerRestore(defaultObject1);
        var job2Id = verifyWorkerRestore(defaultObject1v2);

        completeJob(job1Id);
        completeJob(job2Id);

        invalidateCacheAndProcessJobs();

        completeJob(verifyWorkerFinalize(defaultObject1));

        completeJob(verifyValidateObject(defaultObject1));

        var events = defaultObject1Events();
        events.add(restoreSuccessEvent(defaultObject1));
        events.add(restoreSuccessEvent(defaultObject1v2));
        assertPreservationEvents(defaultObject1v2, events);
    }

    @Test
    public void restoreSingleVersionOfObjectWhenExistInRemoteAndOnlyOneAskedFor() {
        setupBaseline();

        restoreObject(defaultObject1v2, List.of(defaultObject1v2.persistenceVersion));
        processJobs();

        completeJob(verifyWorkerRestore(defaultObject1v2));

        invalidateCacheAndProcessJobs();

        completeJob(verifyWorkerFinalize(defaultObject1v2));

        completeJob(verifyValidateObject(defaultObject1v2));
    }

    @Test
    public void failRestoreWhenNotReplicatedToRemote() {
        var bagName = "single-valid.zip";

        var ingestId = publicClient.ingestBag(DEFAULT_VAULT, bag(bagName))
                .getIngestId();
        updateBatchState(ingestId, INTERNAL_ANALYZING);
        registerObjectWithFiles(ingestId, defaultObject1);
        registerObjectWithFiles(ingestId, defaultObject2);
        updateObjectStates(ingestId,
                INTERNAL_OBJ_PENDING_REVIEW,
                defaultObject1, defaultObject2);
        updateBatchState(ingestId, INTERNAL_PENDING_REVIEW);
        assertIngestObject(ingestId, IngestObjectState.PENDING_REVIEW, defaultObject1);
        assertIngestObject(ingestId, IngestObjectState.PENDING_REVIEW, defaultObject2);
        activeClient.approveIngestBatch(new ApproveIngestBatchRequest().ingestId(ingestId));

        updateBatchState(ingestId, INTERNAL_INGESTING);
        updateObjectStates(ingestId, INTERNAL_OBJ_INGESTING, defaultObject1, defaultObject2);

        createVersionAndFinalize(defaultObject1);
        replicate(ingestId, defaultObject1);

        createVersionAndFinalize(defaultObject2);
        replicateFailed(ingestId, defaultObject2);

        assertThat(
                assertThrows(NotFoundException.class, () -> {
                    restoreObject(defaultObject2, List.of(defaultObject2.persistenceVersion));
                }).getMessage(),
                containsString("persistence version v1 was not found in GLACIER"));
    }

    @Test
    public void failRestoreWhenRestoreJobFails() {
        setupBaseline();

        restoreObject(defaultObject1, null);
        processJobs();

        var job1Id = verifyWorkerRestore(defaultObject1);
        var job2Id = verifyWorkerRestore(defaultObject1v2);

        failJob(job1Id);
        completeJob(job2Id);

        invalidateCacheAndProcessJobs();

        verifyNoJobsOfType(edu.wisc.library.sdg.preservation.manager.client.internal.model.JobType.FINALIZE_RESTORE);

        var jobs = listJobs();

        for (var job : jobs) {
            if (job.getType() == JobType.FINALIZE_RESTORE) {
                assertEquals(edu.wisc.library.sdg.preservation.manager.client.model.JobState.FAILED, job.getState());
            }
        }

        var events = defaultObject1Events();
        events.add(restoreFailureEvent(defaultObject1));
        events.add(restoreSuccessEvent(defaultObject1v2));
        events.add(finalizeRestoreFailureEvent());
        assertPreservationEvents(defaultObject1v2, events);
    }

}
