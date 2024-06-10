package edu.wisc.library.sdg.preservation.manager.itest;

import edu.wisc.library.sdg.preservation.manager.client.internal.model.EventOutcome;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.EventType;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.LogLevel;
import edu.wisc.library.sdg.preservation.manager.client.model.ApproveIngestBatchRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.DataStore;
import edu.wisc.library.sdg.preservation.manager.client.model.IngestObjectState;
import edu.wisc.library.sdg.preservation.manager.client.model.StorageProblemType;
import edu.wisc.library.sdg.preservation.manager.db.repo.PreservationObjectVersionLocationRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static edu.wisc.library.sdg.preservation.manager.itest.InternalAlias.GLACIER;
import static edu.wisc.library.sdg.preservation.manager.itest.InternalAlias.INTERNAL_ANALYZING;
import static edu.wisc.library.sdg.preservation.manager.itest.InternalAlias.INTERNAL_INGESTING;
import static edu.wisc.library.sdg.preservation.manager.itest.InternalAlias.INTERNAL_OBJ_INGESTING;
import static edu.wisc.library.sdg.preservation.manager.itest.InternalAlias.INTERNAL_OBJ_PENDING_REVIEW;
import static edu.wisc.library.sdg.preservation.manager.itest.InternalAlias.INTERNAL_PENDING_REVIEW;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReplicateITest extends ITestBase {

    @Autowired
    private PreservationObjectVersionLocationRepository versionLocationRepo;

    private static final edu.wisc.library.sdg.preservation.manager.db.model.DataStore GLACIER_DATASTORE =
            edu.wisc.library.sdg.preservation.manager.db.model.DataStore.GLACIER;

    @Test
    public void replicateWhenObjectVersionExists() {
        setupBaseline();

        var digest = DigestUtils.sha256Hex(defaultObject1v2.prefixedInternalId());
        assertReplicatedDbRecord(defaultObject1v2, digest);
    }

    @Test
    public void markFailureWhenReplicationFails() {
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

        assertNoReplicatedDbRecord(defaultObject2);
    }

    @Test
    public void createStorageProblemWhenReplicationFailsAndVersionIsNotInDataStore() {
        var bagName = "single-valid.zip";

        var ingestId = publicClient.ingestBag(DEFAULT_VAULT, bag(bagName))
                .getIngestId();
        updateBatchState(ingestId, INTERNAL_ANALYZING);
        registerObjectWithFiles(ingestId, defaultObject1);
        updateObjectStates(ingestId,
                INTERNAL_OBJ_PENDING_REVIEW,
                defaultObject1);
        updateBatchState(ingestId, INTERNAL_PENDING_REVIEW);
        approveBatch(ingestId);

        updateBatchState(ingestId, INTERNAL_INGESTING);
        updateObjectStates(ingestId, INTERNAL_OBJ_INGESTING, defaultObject1);

        createVersionAndFinalize(defaultObject1);
        replicateFailed(ingestId, defaultObject1);

        assertProblems(defaultObject1, problem(StorageProblemType.MISSING, "v1", DataStore.GLACIER));
    }

    @Test
    public void doNotCreateStorageProblemWhenReplicationFailsAndVersionIsInDataStore() {
        setupBaseline();

        replicateObject(defaultObject1, null, DataStore.GLACIER);

        processJobs();

        var jobId1 = verifyWorkerReplicate(defaultObject1);
        var jobId2 = verifyWorkerReplicate(defaultObject1v2);

        completeJob(jobId1);
        failJob(jobId2);

        var events = defaultObject1Events();
        events.add(event(EventType.REPLICATE_OBJ_VERSION, EventOutcome.SUCCESS, null,
                List.of(logEntry(LogLevel.INFO, "Replicated OCFL version v1 to GLACIER"))));
        events.add(event(EventType.REPLICATE_OBJ_VERSION, EventOutcome.FAILURE, null,
                List.of(logEntry(LogLevel.ERROR, "Failed to replicated OCFL version v2 to GLACIER"))));

        assertPreservationEvents(defaultObject1, events);
        assertProblems(defaultObject1v2);
    }

    @Test
    public void replicateAllVersionsWhenAlreadyReplicated() {
        setupBaseline();

        replicateObject(defaultObject1, null, DataStore.GLACIER);

        processJobs();

        var jobId1 = verifyWorkerReplicate(defaultObject1);
        var jobId2 = verifyWorkerReplicate(defaultObject1v2);

        completeJob(jobId1);
        completeJob(jobId2);

        var events = defaultObject1Events();
        events.add(event(EventType.REPLICATE_OBJ_VERSION, EventOutcome.SUCCESS, null,
                List.of(logEntry(LogLevel.INFO, "Replicated OCFL version v1 to GLACIER"))));
        events.add(event(EventType.REPLICATE_OBJ_VERSION, EventOutcome.SUCCESS, null,
                List.of(logEntry(LogLevel.INFO, "Replicated OCFL version v2 to GLACIER"))));

        assertPreservationEvents(defaultObject1, events);
        verifyValidateObjectRemote(defaultObject1, "v1", GLACIER);
        verifyValidateObjectRemote(defaultObject1, "v2", GLACIER);
    }

    private void assertNoReplicatedDbRecord(TestObject object) {
        assertTrue(versionLocationRepo.findByObjectIdAndPersistenceVersionAndDataStore(
                object.internalId, object.persistenceVersion, GLACIER_DATASTORE).isEmpty(),
                "replication record should not exist");
    }

    private void assertReplicatedDbRecord(TestObject object, String digest) {
        var location = versionLocationRepo.findByObjectIdAndPersistenceVersionAndDataStore(
                object.internalId, object.persistenceVersion, GLACIER_DATASTORE)
                .get();
        assertEquals(edu.wisc.library.sdg.preservation.manager.db.model.DataStore.GLACIER, location.getDataStore());
        assertEquals(computeDataStoreKey(object), location.getDataStoreKey());
        assertEquals(digest, location.getSha256Digest());
    }

}
