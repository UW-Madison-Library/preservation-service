package edu.wisc.library.sdg.preservation.manager.itest;

import edu.wisc.library.sdg.preservation.common.PreservationConstants;
import edu.wisc.library.sdg.preservation.common.exception.ConcurrentUpdateException;
import edu.wisc.library.sdg.preservation.common.exception.IllegalOperationException;
import edu.wisc.library.sdg.preservation.common.exception.NotFoundException;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.CreateObjectVersionRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.DeleteObjectVersionRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.Event;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.EventOutcome;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.EventType;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.JobState;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.LogLevel;
import edu.wisc.library.sdg.preservation.manager.client.model.ApproveIngestBatchRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.EncodingDiff;
import edu.wisc.library.sdg.preservation.manager.client.model.FileDiff;
import edu.wisc.library.sdg.preservation.manager.client.model.Format;
import edu.wisc.library.sdg.preservation.manager.client.model.FormatDiff;
import edu.wisc.library.sdg.preservation.manager.client.model.IngestBatchState;
import edu.wisc.library.sdg.preservation.manager.client.model.IngestObjectState;
import edu.wisc.library.sdg.preservation.manager.client.model.RejectIngestObjectRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.Validity;
import edu.wisc.library.sdg.preservation.manager.client.model.ValidityDiff;
import edu.wisc.library.sdg.preservation.manager.db.model.FormatRegistry;
import edu.wisc.library.sdg.preservation.manager.db.repo.JobRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static edu.wisc.library.sdg.preservation.manager.itest.InternalAlias.INTERNAL_ANALYZING;
import static edu.wisc.library.sdg.preservation.manager.itest.InternalAlias.INTERNAL_INGESTING;
import static edu.wisc.library.sdg.preservation.manager.itest.InternalAlias.INTERNAL_INGEST_FAILED;
import static edu.wisc.library.sdg.preservation.manager.itest.InternalAlias.INTERNAL_OBJ_ANALYSIS_FAILED;
import static edu.wisc.library.sdg.preservation.manager.itest.InternalAlias.INTERNAL_OBJ_INGESTED;
import static edu.wisc.library.sdg.preservation.manager.itest.InternalAlias.INTERNAL_OBJ_INGESTING;
import static edu.wisc.library.sdg.preservation.manager.itest.InternalAlias.INTERNAL_OBJ_INGEST_FAILED;
import static edu.wisc.library.sdg.preservation.manager.itest.InternalAlias.INTERNAL_OBJ_PENDING_REVIEW;
import static edu.wisc.library.sdg.preservation.manager.itest.InternalAlias.INTERNAL_PENDING_REVIEW;
import static edu.wisc.library.sdg.preservation.manager.itest.InternalAlias.INTERNAL_REPLICATING;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class IngestBagITest extends ITestBase {

    @Autowired
    JobRepository jobRepo;

    @Test
    public void ingestObjectWhenSingleValidAndNew() {
        var bagName = "single-valid.zip";

        var testObject = new TestObject("o1", List.of(
                new TestFile("MASTER0", 100L)
                        .withFormat(FormatRegistry.MIME, JHOVE_ID, "image/tiff")
                        .withValidity(JHOVE_ID, true, true),
                new TestFile("BIB0", 50L)
                        .withFormat(FormatRegistry.MIME, JHOVE_ID, "text/xml")
                        .withFormat(FormatRegistry.PRONOM, DROID_ID, "fmt/101")
                        .withEncoding(JHOVE_ID, "UTF-8")
                        .withValidity(JHOVE_ID, true, true)
        ));

        var ingestId = publicClient.ingestBag(DEFAULT_VAULT, bag(bagName))
                .getIngestId();

        var batchEvents = new ArrayList<Event>();
        var objEvents = new ArrayList<Event>();

        retrieveAndAssertDefaultBatch(ingestId, bagName, IngestBatchState.RECEIVED);

        updateBatchState(ingestId, INTERNAL_ANALYZING);

        batchEvents.add(event(EventType.RECEIVE_BAG, EventOutcome.SUCCESS, FEDORA_USER, List.of(logEntry(LogLevel.INFO, "Received SIP single-valid.zip"))));
        batchEvents.add(recordIngestEvent(ingestId, null, EventType.VIRUS_SCAN_BAG, EventOutcome.SUCCESS, null, null));
        batchEvents.add(recordIngestEvent(ingestId, null, EventType.VALIDATE_BAG, EventOutcome.SUCCESS, null, null));

        registerObjectWithFiles(ingestId, testObject);
        objEvents.add(recordIngestEvent(ingestId, testObject.externalId, EventType.IDENTIFY_OBJ, EventOutcome.SUCCESS, null, null));
        objEvents.add(recordIngestEvent(ingestId, testObject.externalId, EventType.IDENTIFY_FILE_FORMAT, EventOutcome.SUCCESS_WITH_WARNINGS, null,
                List.of(logEntry(LogLevel.WARN, "File file1.xml is invalid"))));

        updateObjectState(ingestId, testObject.externalId, INTERNAL_OBJ_PENDING_REVIEW);
        updateBatchState(ingestId, INTERNAL_PENDING_REVIEW);

        retrieveAndAssertDefaultBatch(ingestId, bagName, IngestBatchState.PENDING_REVIEW);

        assertIngestObject(ingestId, IngestObjectState.PENDING_REVIEW, testObject);

        publicClient.approveIngestBatch(new ApproveIngestBatchRequest().ingestId(ingestId));
        batchEvents.add(batchApprovedEvent());
        objEvents.add(objectApprovedEvent());

        updateBatchState(ingestId, INTERNAL_INGESTING);
        updateObjectStates(ingestId, INTERNAL_OBJ_INGESTING, testObject);
        createVersionAndFinalize(testObject);
        var localWriteEvent = recordIngestEvent(ingestId, testObject.externalId, EventType.WRITE_OBJ_LOCAL, EventOutcome.SUCCESS, null,
                List.of(logEntry(LogLevel.INFO, "Ingested object into local OCFL repo")));
        objEvents.add(localWriteEvent);
        objEvents.add(metadataUpdatedEvent());
        objEvents.add(objectCreatedEvent(testObject));
        replicate(ingestId, testObject);
        objEvents.add(replicatedEvent(testObject));
        objEvents.add(objectCompleteEvent());
        updateBatchState(ingestId, INTERNAL_REPLICATING);
        checkBatchReplications();
        batchEvents.add(batchCompleteEvent());

        retrieveAndAssertDefaultBatch(ingestId, bagName, IngestBatchState.COMPLETE);

        assertPreservationObject(ingestId, testObject);

        assertBatches(ingestId);

        assertBatchEvents(ingestId, batchEvents);
        assertBatchObjectEvents(ingestId, testObject, objEvents);
        assertPreservationEvents(testObject, List.of(
                localWriteEvent,
                metadataUpdatedEvent(),
                objectCreatedEvent(testObject),
                replicatedEvent(testObject)));
    }

    @Test
    public void ingestObjectWhenMultipleOneRejected() {
        var bagName = "single-valid.zip";

        var testObject1 = new TestObject("o1", List.of(
                new TestFile("MASTER0", 100L)
                        .withFormat(FormatRegistry.MIME, JHOVE_ID, "image/tiff")
                        .withValidity(JHOVE_ID, true, null),
                new TestFile("BIB0", 50L)
                        .withFormat(FormatRegistry.MIME, JHOVE_ID, "text/xml")
                        .withFormat(FormatRegistry.PRONOM, DROID_ID, "fmt/101")
                        .withEncoding(JHOVE_ID, "UTF-8")
                        .withEncoding(TIKA_ID, "ASCII")
                        .withValidity(JHOVE_ID, false, false)
        ));
        var testObject2 = new TestObject("o2", List.of(
                new TestFile("TEXT0", 1000L)
                        .withFormat(FormatRegistry.MIME, JHOVE_ID, "text/plain"),
                new TestFile("ENCTEXT", 2000L)
                        .withFormat(FormatRegistry.MIME, JHOVE_ID, "text/xml")
                        .withFormat(FormatRegistry.PRONOM, DROID_ID, "fmt/101")
        ));
        var testObject3 = new TestObject("o3");

        var ingestId = publicClient.ingestBag(DEFAULT_VAULT, bag(bagName))
                .getIngestId();

        retrieveAndAssertDefaultBatch(ingestId, bagName, IngestBatchState.RECEIVED);

        updateBatchState(ingestId, INTERNAL_ANALYZING);

        registerObjectWithFiles(ingestId, testObject1);
        registerObjectWithFiles(ingestId, testObject2);
        registerObject(ingestId, testObject3.externalId);

        updateObjectStates(ingestId, INTERNAL_OBJ_PENDING_REVIEW, testObject1, testObject2);
        updateObjectState(ingestId, testObject3.externalId, INTERNAL_OBJ_ANALYSIS_FAILED);
        updateBatchState(ingestId, INTERNAL_PENDING_REVIEW);

        retrieveAndAssertDefaultBatch(ingestId, bagName, IngestBatchState.PENDING_REVIEW);

        assertIngestObject(ingestId, IngestObjectState.PENDING_REVIEW, testObject1);
        assertIngestObject(ingestId, IngestObjectState.PENDING_REVIEW, testObject2);
        assertIngestObject(ingestId, IngestObjectState.ANALYSIS_FAILED, testObject3);

        publicClient.rejectIngestObject(new RejectIngestObjectRequest()
                .ingestId(ingestId)
                .externalObjectId(testObject1.externalId));
        assertIngestObject(ingestId, IngestObjectState.PENDING_REJECTION, testObject1);

        publicClient.approveIngestBatch(new ApproveIngestBatchRequest().ingestId(ingestId));

        assertIngestObject(ingestId, IngestObjectState.REJECTED, testObject1);
        assertIngestObject(ingestId, IngestObjectState.PENDING_INGESTION, testObject2);
        assertIngestObject(ingestId, IngestObjectState.ANALYSIS_FAILED, testObject3);

        updateBatchState(ingestId, INTERNAL_INGESTING);
        updateObjectStates(ingestId, INTERNAL_OBJ_INGESTING, testObject2);
        assertIngestObject(ingestId, IngestObjectState.INGESTING, testObject2);

        createVersionAndFinalize(testObject2);

        replicate(ingestId, testObject2);

        completeBatch(ingestId);

        retrieveAndAssertDefaultBatch(ingestId, bagName, IngestBatchState.COMPLETE);

        assertPreservationObject(ingestId, testObject2);

        assertBatches(ingestId);
    }

    @Test
    public void ingestObjectWhenMultipleOneFailsReplication() {
        var bagName = "single-valid.zip";

        var testObject1 = new TestObject("o1", List.of(
                new TestFile("MASTER0", 100L)
                        .withFormat(FormatRegistry.MIME, JHOVE_ID, "image/tiff")
                        .withValidity(JHOVE_ID, true, null),
                new TestFile("BIB0", 50L)
                        .withFormat(FormatRegistry.MIME, JHOVE_ID, "text/xml")
                        .withFormat(FormatRegistry.PRONOM, DROID_ID, "fmt/101")
                        .withEncoding(JHOVE_ID, "UTF-8")
                        .withEncoding(TIKA_ID, "ASCII")
                        .withValidity(JHOVE_ID, false, false)
        ));
        var testObject2 = new TestObject("o2", List.of(
                new TestFile("TEXT0", 1000L)
                        .withFormat(FormatRegistry.MIME, JHOVE_ID, "text/plain"),
                new TestFile("ENCTEXT", 2000L)
                        .withFormat(FormatRegistry.MIME, JHOVE_ID, "text/xml")
                        .withFormat(FormatRegistry.PRONOM, DROID_ID, "fmt/101")
        ));

        var ingestId = publicClient.ingestBag(DEFAULT_VAULT, bag(bagName))
                .getIngestId();

        retrieveAndAssertDefaultBatch(ingestId, bagName, IngestBatchState.RECEIVED);

        updateBatchState(ingestId, INTERNAL_ANALYZING);

        registerObjectWithFiles(ingestId, testObject1);
        registerObjectWithFiles(ingestId, testObject2);

        updateObjectStates(ingestId, INTERNAL_OBJ_PENDING_REVIEW, testObject1, testObject2);
        updateBatchState(ingestId, INTERNAL_PENDING_REVIEW);

        retrieveAndAssertDefaultBatch(ingestId, bagName, IngestBatchState.PENDING_REVIEW);

        assertIngestObject(ingestId, IngestObjectState.PENDING_REVIEW, testObject1);
        assertIngestObject(ingestId, IngestObjectState.PENDING_REVIEW, testObject2);

        publicClient.approveIngestBatch(new ApproveIngestBatchRequest().ingestId(ingestId));

        assertIngestObject(ingestId, IngestObjectState.PENDING_INGESTION, testObject1);
        assertIngestObject(ingestId, IngestObjectState.PENDING_INGESTION, testObject2);

        updateBatchState(ingestId, INTERNAL_INGESTING);
        updateObjectStates(ingestId, INTERNAL_OBJ_INGESTING, testObject1, testObject2);
        assertIngestObject(ingestId, IngestObjectState.INGESTING, testObject1);
        assertIngestObject(ingestId, IngestObjectState.INGESTING, testObject2);

        createVersionAndFinalize(testObject1);
        createVersionAndFinalize(testObject2);

        replicate(ingestId, testObject1);
        replicateFailed(ingestId, testObject2);

        completeBatch(ingestId);

        retrieveAndAssertDefaultBatch(ingestId, bagName, IngestBatchState.REPLICATION_FAILED);
    }

    @Test
    public void ingestObjectWhenWhenUpdate() {
        var bagName = "single-valid.zip";

        var testObject1 = new TestObject("o1", List.of(
                new TestFile("MASTER0", 100L)
                        .withFormat(FormatRegistry.MIME, JHOVE_ID, "image/tiff"),
                new TestFile("BIB0", 50L)
                        .withFormat(FormatRegistry.MIME, JHOVE_ID, "text/xml")
                        .withFormat(FormatRegistry.PRONOM, DROID_ID, "fmt/101")
        ));
        var testObject1v2 = new TestObject("o1", List.of(
                new TestFile("MASTER0", 100L)
                        .withFormat(FormatRegistry.MIME, JHOVE_ID, "image/tiff"),
                new TestFile("BIB0", 100L)
                        .withFormat(FormatRegistry.MIME, JHOVE_ID, "text/xml")
                        .withFormat(FormatRegistry.PRONOM, DROID_ID, "fmt/101")
        )).version(2);

        testObject1v2.internalId = testObject1.internalId;

        // create v1
        var ingestId = publicClient.ingestBag(DEFAULT_VAULT, bag(bagName))
                .getIngestId();
        updateBatchState(ingestId, INTERNAL_ANALYZING);
        registerObjectWithFiles(ingestId, testObject1);
        updateObjectStates(ingestId, INTERNAL_OBJ_PENDING_REVIEW, testObject1);
        updateBatchState(ingestId, INTERNAL_PENDING_REVIEW);
        assertIngestObject(ingestId, IngestObjectState.PENDING_REVIEW, testObject1);
        publicClient.approveIngestBatch(new ApproveIngestBatchRequest().ingestId(ingestId));
        updateBatchState(ingestId, INTERNAL_INGESTING);
        updateObjectStates(ingestId, INTERNAL_OBJ_INGESTING, testObject1);
        createVersionAndFinalize(testObject1);
        replicate(ingestId, testObject1);
        completeBatch(ingestId);

        // create v2
        var ingestId2 = publicClient.ingestBag(DEFAULT_VAULT, bag(bagName))
                .getIngestId();

        updateBatchState(ingestId2, INTERNAL_ANALYZING);
        registerObjectWithFiles(ingestId2, testObject1v2);

        updateObjectStates(ingestId2, INTERNAL_OBJ_PENDING_REVIEW, testObject1v2);
        updateBatchState(ingestId2, INTERNAL_PENDING_REVIEW);

        assertIngestObject(ingestId2, IngestObjectState.PENDING_REVIEW, testObject1v2);

        publicClient.approveIngestBatch(new ApproveIngestBatchRequest().ingestId(ingestId2));

        assertIngestObject(ingestId2, IngestObjectState.PENDING_INGESTION, testObject1v2);

        updateBatchState(ingestId2, INTERNAL_INGESTING);
        updateObjectStates(ingestId2, INTERNAL_OBJ_INGESTING, testObject1v2);
        assertIngestObject(ingestId2, IngestObjectState.INGESTING, testObject1v2);

        createVersionAndFinalize(testObject1v2);

        replicate(ingestId2, testObject1v2);
        completeBatch(ingestId2);

        retrieveAndAssertDefaultBatch(ingestId2, bagName, IngestBatchState.COMPLETE);

        assertPreservationObject(ingestId2, testObject1v2);

        assertBatches(ingestId, ingestId2);
        assertPreservationEvents(testObject1v2, List.of(
                metadataUpdatedEvent(),
                objectCreatedEvent(testObject1),
                replicatedEvent(testObject1),
                metadataUpdatedEvent(),
                objectUpdatedEvent(testObject1v2),
                replicatedEvent(testObject1v2)
        ));
    }

    @Test
    public void failConcurrentV1Creation() {
        var bagName = "single-valid.zip";

        var testObject1 = new TestObject("o1", List.of(
                new TestFile("MASTER0", 100L)
                        .withFormat(FormatRegistry.MIME, JHOVE_ID, "image/tiff"),
                new TestFile("BIB0", 50L)
                        .withFormat(FormatRegistry.MIME, JHOVE_ID, "text/xml")
                        .withFormat(FormatRegistry.PRONOM, DROID_ID, "fmt/101")
        ));
        var testObject2 = new TestObject("o1", List.of(
                new TestFile("MASTER0", 101L)
                        .withFormat(FormatRegistry.MIME, JHOVE_ID, "image/tiff"),
                new TestFile("BIB0", 51L)
                        .withFormat(FormatRegistry.MIME, JHOVE_ID, "text/xml")
                        .withFormat(FormatRegistry.PRONOM, DROID_ID, "fmt/101")
        ));

        var ingestId1 = publicClient.ingestBag(DEFAULT_VAULT, bag(bagName))
                .getIngestId();
        updateBatchState(ingestId1, INTERNAL_ANALYZING);
        registerObjectWithFiles(ingestId1, testObject1);
        updateObjectStates(ingestId1, INTERNAL_OBJ_PENDING_REVIEW, testObject1);
        updateBatchState(ingestId1, INTERNAL_PENDING_REVIEW);
        assertIngestObject(ingestId1, IngestObjectState.PENDING_REVIEW, testObject1);
        publicClient.approveIngestBatch(new ApproveIngestBatchRequest().ingestId(ingestId1));
        updateBatchState(ingestId1, INTERNAL_INGESTING);
        updateObjectStates(ingestId1, INTERNAL_OBJ_INGESTING, testObject1);

        var ingestId2 = publicClient.ingestBag(DEFAULT_VAULT, bag(bagName))
                .getIngestId();
        updateBatchState(ingestId2, INTERNAL_ANALYZING);
        registerObjectWithFiles(ingestId2, testObject2);
        updateObjectStates(ingestId2, INTERNAL_OBJ_PENDING_REVIEW, testObject2);
        updateBatchState(ingestId2, INTERNAL_PENDING_REVIEW);
        assertIngestObject(ingestId2, IngestObjectState.PENDING_REVIEW, testObject2);
        publicClient.approveIngestBatch(new ApproveIngestBatchRequest().ingestId(ingestId2));
        updateBatchState(ingestId2, INTERNAL_INGESTING);
        updateObjectStates(ingestId2, INTERNAL_OBJ_INGESTING, testObject2);

        var objectVersionId = internalClient.createObjectVersion(new CreateObjectVersionRequest()
                .ingestObjectId(testObject1.ingestObjectId)
                .objectId(testObject1.unprefixedInternalId())).getObjectVersionId();

        assertThrows(ConcurrentUpdateException.class, () -> {
            internalClient.createObjectVersion(new CreateObjectVersionRequest()
                    .ingestObjectId(testObject2.ingestObjectId)
                    .objectId(testObject2.unprefixedInternalId()));
        });

        assertBatches(ingestId1, ingestId2);
    }

    @Test
    public void deleteObjectWhenCreatedButNotFinalized() {
        var bagName = "single-valid.zip";

        var testObject = new TestObject("o1", List.of(
                new TestFile("MASTER0", 100L)
                        .withFormat(FormatRegistry.MIME, JHOVE_ID, "image/tiff"),
                new TestFile("BIB0", 50L)
                        .withFormat(FormatRegistry.MIME, JHOVE_ID, "text/xml")
                        .withFormat(FormatRegistry.PRONOM, DROID_ID, "fmt/101")
        ));

        var ingestId = publicClient.ingestBag(DEFAULT_VAULT, bag(bagName))
                .getIngestId();

        retrieveAndAssertDefaultBatch(ingestId, bagName, IngestBatchState.RECEIVED);

        updateBatchState(ingestId, INTERNAL_ANALYZING);

        registerObjectWithFiles(ingestId, testObject);

        updateObjectState(ingestId, testObject.externalId, INTERNAL_OBJ_PENDING_REVIEW);
        updateBatchState(ingestId, INTERNAL_PENDING_REVIEW);

        retrieveAndAssertDefaultBatch(ingestId, bagName, IngestBatchState.PENDING_REVIEW);

        assertIngestObject(ingestId, IngestObjectState.PENDING_REVIEW, testObject);

        publicClient.approveIngestBatch(new ApproveIngestBatchRequest().ingestId(ingestId));

        updateBatchState(ingestId, INTERNAL_INGESTING);
        updateObjectStates(ingestId, INTERNAL_OBJ_INGESTING, testObject);

        var objectVersionId = internalClient.createObjectVersion(new CreateObjectVersionRequest()
                .ingestObjectId(testObject.ingestObjectId)
                .objectId(testObject.unprefixedInternalId())).getObjectVersionId();

        internalClient.deleteObjectVersion(new DeleteObjectVersionRequest().objectVersionId(objectVersionId));

        assertThrows(NotFoundException.class, () -> {
            publicClient.describePreservationObject(DEFAULT_VAULT, testObject.externalId, null);
        });

        assertBatches(ingestId);
    }

    @Test
    public void deleteObjectVersionWhenCreatedButNotFinalized() {
        var bagName = "single-valid.zip";

        var testObject1 = new TestObject("o1", List.of(
                new TestFile("MASTER0", 100L)
                        .withFormat(FormatRegistry.MIME, JHOVE_ID, "image/tiff"),
                new TestFile("BIB0", 50L)
                        .withFormat(FormatRegistry.MIME, JHOVE_ID, "text/xml")
                        .withFormat(FormatRegistry.PRONOM, DROID_ID, "fmt/101")
        ));
        var testObject1v2 = new TestObject("o1", List.of(
                new TestFile("MASTER0", 100L)
                        .withFormat(FormatRegistry.MIME, JHOVE_ID, "image/tiff"),
                new TestFile("BIB0", 100L)
                        .withFormat(FormatRegistry.MIME, JHOVE_ID, "text/xml")
                        .withFormat(FormatRegistry.PRONOM, DROID_ID, "fmt/101")
        )).version(2);

        // create v1
        var ingestId = publicClient.ingestBag(DEFAULT_VAULT, bag(bagName))
                .getIngestId();
        updateBatchState(ingestId, INTERNAL_ANALYZING);
        registerObjectWithFiles(ingestId, testObject1);
        updateObjectStates(ingestId, INTERNAL_OBJ_PENDING_REVIEW, testObject1);
        updateBatchState(ingestId, INTERNAL_PENDING_REVIEW);
        assertIngestObject(ingestId, IngestObjectState.PENDING_REVIEW, testObject1);
        publicClient.approveIngestBatch(new ApproveIngestBatchRequest().ingestId(ingestId));
        updateBatchState(ingestId, INTERNAL_INGESTING);
        updateObjectStates(ingestId, INTERNAL_OBJ_INGESTING, testObject1);
        createVersionAndFinalize(testObject1);
        replicate(ingestId, testObject1);
        updateBatchState(ingestId, INTERNAL_REPLICATING);
        checkBatchReplications();

        // create v2
        var ingestId2 = publicClient.ingestBag(DEFAULT_VAULT, bag(bagName))
                .getIngestId();

        updateBatchState(ingestId2, INTERNAL_ANALYZING);
        registerObjectWithFiles(ingestId2, testObject1v2);

        updateObjectStates(ingestId2, INTERNAL_OBJ_PENDING_REVIEW, testObject1v2);
        updateBatchState(ingestId2, INTERNAL_PENDING_REVIEW);

        assertIngestObject(ingestId2, IngestObjectState.PENDING_REVIEW, testObject1v2);

        publicClient.approveIngestBatch(new ApproveIngestBatchRequest().ingestId(ingestId2));

        assertIngestObject(ingestId2, IngestObjectState.PENDING_INGESTION, testObject1v2);

        updateBatchState(ingestId2, INTERNAL_INGESTING);
        updateObjectStates(ingestId2, INTERNAL_OBJ_INGESTING, testObject1v2);
        assertIngestObject(ingestId2, IngestObjectState.INGESTING, testObject1v2);

        var objectVersionId2 = internalClient.createObjectVersion(new CreateObjectVersionRequest()
                .ingestObjectId(testObject1v2.ingestObjectId)
                .objectId(testObject1.unprefixedInternalId())).getObjectVersionId();

        internalClient.deleteObjectVersion(new DeleteObjectVersionRequest().objectVersionId(objectVersionId2));

        assertPreservationObject(ingestId, testObject1);

        assertBatches(ingestId, ingestId2);
    }

    @Test
    public void diffObjectWithExisting() {
        var bagName = "single-valid.zip";

        var testObject1 = new TestObject("o1", List.of(
                new TestFile("MASTER0", 100L)
                        .withFormat(FormatRegistry.MIME, JHOVE_ID, "image/tiff"),
                new TestFile("BIB0", 50L)
                        .withFormat(FormatRegistry.MIME, JHOVE_ID, "text/xml")
                        .withValidity(JHOVE_ID, false, false),
                new TestFile("REF0", 150L)
                        .withFormat(FormatRegistry.MIME, JHOVE_ID, "application/pdf")
                        .withValidity(JHOVE_ID, false, false)
        ));
        var testObject1v2 = new TestObject("o1", List.of(
                new TestFile("MASTER0", 100L)
                        .withFormat(FormatRegistry.MIME, JHOVE_ID, "image/tiff"),
                new TestFile("BIB0", 51L)
                        .withFormat(FormatRegistry.MIME, JHOVE_ID, "text/xml")
                        .withFormat(FormatRegistry.PRONOM, DROID_ID, "fmt/101")
                        .withEncoding(JHOVE_ID, "UTF-8")
                        .withValidity(JHOVE_ID, true, true),
                new TestFile("TEXT0", 200L)
                        .withFormat(FormatRegistry.MIME, JHOVE_ID, "text/plain")
                        .withEncoding(JHOVE_ID, "UTF-8")
        )).version(2);

        testObject1v2.internalId = testObject1.internalId;

        // create v1
        var ingestId = publicClient.ingestBag(DEFAULT_VAULT, bag(bagName))
                .getIngestId();
        updateBatchState(ingestId, INTERNAL_ANALYZING);
        registerObjectWithFiles(ingestId, testObject1);
        updateObjectStates(ingestId, INTERNAL_OBJ_PENDING_REVIEW, testObject1);
        updateBatchState(ingestId, INTERNAL_PENDING_REVIEW);
        assertIngestObject(ingestId, IngestObjectState.PENDING_REVIEW, testObject1);
        publicClient.approveIngestBatch(new ApproveIngestBatchRequest().ingestId(ingestId));
        updateBatchState(ingestId, INTERNAL_INGESTING);
        updateObjectStates(ingestId, INTERNAL_OBJ_INGESTING, testObject1);
        createVersionAndFinalize(testObject1);
        replicate(ingestId, testObject1);
        completeBatch(ingestId);

        // create v2
        var ingestId2 = publicClient.ingestBag(DEFAULT_VAULT, bag(bagName))
                .getIngestId();

        updateBatchState(ingestId2, INTERNAL_ANALYZING);
        registerObjectWithFiles(ingestId2, testObject1v2);

        updateObjectStates(ingestId2, INTERNAL_OBJ_PENDING_REVIEW, testObject1v2);
        updateBatchState(ingestId2, INTERNAL_PENDING_REVIEW);

        assertIngestObject(ingestId2, IngestObjectState.PENDING_REVIEW, testObject1v2);

        var diff = activeClient.diffBatchObject(ingestId2, testObject1v2.externalId);

        assertThat(diff.getFiles(), Matchers.containsInAnyOrder(
                new FileDiff()
                        .path("TEXT0")
                        .newSha256Digest("519e3b2a1ca30148faca5bd61b1a1069faef29b5ec475230c2a50783f66922db")
                        .diff(FileDiff.DiffEnum.ADDED)
                        .formatDiff(formatDiff()
                                .addAddedItem(new Format()
                                        .format("text/plain")
                                        .registry(edu.wisc.library.sdg.preservation.manager.client.model.FormatRegistry.MIME)))
                        .encodingDiff(encodingDiff().addAddedItem("UTF-8"))
                        .validityDiff(validityDiff()),
                new FileDiff()
                        .path("BIB0")
                        .newSha256Digest("9f15de59cfaaccf082d18ab73e312a57d1fcb9d6a380a6c2d371d5fdb8899e46")
                        .oldSha256Digest("7285b8eecb8a892b835af8959f0a481f56932500954771063d75c8e09046fa80")
                        .diff(FileDiff.DiffEnum.MODIFIED)
                        .formatDiff(formatDiff()
                                .addAddedItem(new Format()
                                        .format("fmt/101")
                                        .registry(edu.wisc.library.sdg.preservation.manager.client.model.FormatRegistry.PRONOM))
                                .addUnchangedItem(new Format()
                                        .format("text/xml")
                                        .registry(edu.wisc.library.sdg.preservation.manager.client.model.FormatRegistry.MIME)))
                        .encodingDiff(encodingDiff().addAddedItem("UTF-8"))
                        .validityDiff(validityDiff()
                                .addAddedItem(new Validity().valid(true).wellFormed(true))
                                .addRemovedItem(new Validity().valid(false).wellFormed(false))),
                new FileDiff()
                        .path("MASTER0")
                        .newSha256Digest("2f4606a4f210dc9707e35aa4cf367e402b4214f0658bd92c777b7bbfbca5663f")
                        .oldSha256Digest("2f4606a4f210dc9707e35aa4cf367e402b4214f0658bd92c777b7bbfbca5663f")
                        .diff(FileDiff.DiffEnum.UNCHANGED)
                        .formatDiff(formatDiff()
                                .addUnchangedItem(new Format()
                                        .format("image/tiff")
                                        .registry(edu.wisc.library.sdg.preservation.manager.client.model.FormatRegistry.MIME)))
                        .encodingDiff(encodingDiff())
                        .validityDiff(validityDiff()),
                new FileDiff()
                        .path("REF0")
                        .oldSha256Digest("0b6635c5a45fe9c8ac3edb430f527f0f0b53dafc48dffff2cee4e834d3541da4")
                        .diff(FileDiff.DiffEnum.REMOVED)
                        .formatDiff(formatDiff()
                                .addRemovedItem(new Format()
                                        .format("application/pdf")
                                        .registry(edu.wisc.library.sdg.preservation.manager.client.model.FormatRegistry.MIME)))
                        .encodingDiff(encodingDiff())
                        .validityDiff(validityDiff()
                                .addRemovedItem(new Validity().valid(false).wellFormed(false)))
        ));

        diff = activeClient.diffBatchObject(ingestId, testObject1.externalId);

        assertThat(diff.getFiles(), Matchers.containsInAnyOrder(
                new FileDiff()
                        .path("BIB0")
                        .newSha256Digest("7285b8eecb8a892b835af8959f0a481f56932500954771063d75c8e09046fa80")
                        .diff(FileDiff.DiffEnum.ADDED)
                        .formatDiff(formatDiff()
                                .addAddedItem(new Format()
                                        .format("text/xml")
                                        .registry(edu.wisc.library.sdg.preservation.manager.client.model.FormatRegistry.MIME)))
                        .encodingDiff(encodingDiff())
                        .validityDiff(validityDiff()
                                .addAddedItem(new Validity().valid(false).wellFormed(false))),
                new FileDiff()
                        .path("MASTER0")
                        .newSha256Digest("2f4606a4f210dc9707e35aa4cf367e402b4214f0658bd92c777b7bbfbca5663f")
                        .diff(FileDiff.DiffEnum.ADDED)
                        .formatDiff(formatDiff()
                                .addAddedItem(new Format()
                                        .format("image/tiff")
                                        .registry(edu.wisc.library.sdg.preservation.manager.client.model.FormatRegistry.MIME)))
                        .encodingDiff(encodingDiff())
                        .validityDiff(validityDiff()),
                new FileDiff()
                        .path("REF0")
                        .newSha256Digest("0b6635c5a45fe9c8ac3edb430f527f0f0b53dafc48dffff2cee4e834d3541da4")
                        .diff(FileDiff.DiffEnum.ADDED)
                        .formatDiff(formatDiff()
                                .addAddedItem(new Format()
                                        .format("application/pdf")
                                        .registry(edu.wisc.library.sdg.preservation.manager.client.model.FormatRegistry.MIME)))
                        .encodingDiff(encodingDiff())
                        .validityDiff(validityDiff()
                                .addAddedItem(new Validity().valid(false).wellFormed(false)))
        ));
    }

    @Test
    public void diffObjectNew() {
        var bagName = "single-valid.zip";

        var testObject1 = new TestObject("o1", List.of(
                new TestFile("MASTER0", 100L)
                        .withFormat(FormatRegistry.MIME, JHOVE_ID, "image/tiff"),
                new TestFile("BIB0", 50L)
                        .withFormat(FormatRegistry.MIME, JHOVE_ID, "text/xml")
                        .withValidity(JHOVE_ID, false, false),
                new TestFile("REF0", 150L)
                        .withFormat(FormatRegistry.MIME, JHOVE_ID, "application/pdf")
                        .withValidity(JHOVE_ID, false, false)
        ));

        // create v1
        var ingestId = publicClient.ingestBag(DEFAULT_VAULT, bag(bagName))
                .getIngestId();
        updateBatchState(ingestId, INTERNAL_ANALYZING);
        registerObjectWithFiles(ingestId, testObject1);
        updateObjectStates(ingestId, INTERNAL_OBJ_PENDING_REVIEW, testObject1);
        updateBatchState(ingestId, INTERNAL_PENDING_REVIEW);
        assertIngestObject(ingestId, IngestObjectState.PENDING_REVIEW, testObject1);

        var diff = activeClient.diffBatchObject(ingestId, testObject1.externalId);

        assertThat(diff.getFiles(), Matchers.containsInAnyOrder(
                new FileDiff()
                        .path("BIB0")
                        .newSha256Digest("7285b8eecb8a892b835af8959f0a481f56932500954771063d75c8e09046fa80")
                        .diff(FileDiff.DiffEnum.ADDED)
                        .formatDiff(formatDiff()
                                .addAddedItem(new Format()
                                        .format("text/xml")
                                        .registry(edu.wisc.library.sdg.preservation.manager.client.model.FormatRegistry.MIME)))
                        .encodingDiff(encodingDiff())
                        .validityDiff(validityDiff()
                                .addAddedItem(new Validity().valid(false).wellFormed(false))),
                new FileDiff()
                        .path("MASTER0")
                        .newSha256Digest("2f4606a4f210dc9707e35aa4cf367e402b4214f0658bd92c777b7bbfbca5663f")
                        .diff(FileDiff.DiffEnum.ADDED)
                        .formatDiff(formatDiff()
                                .addAddedItem(new Format()
                                        .format("image/tiff")
                                        .registry(edu.wisc.library.sdg.preservation.manager.client.model.FormatRegistry.MIME)))
                        .encodingDiff(encodingDiff())
                        .validityDiff(validityDiff()),
                new FileDiff()
                        .path("REF0")
                        .newSha256Digest("0b6635c5a45fe9c8ac3edb430f527f0f0b53dafc48dffff2cee4e834d3541da4")
                        .diff(FileDiff.DiffEnum.ADDED)
                        .formatDiff(formatDiff()
                                .addAddedItem(new Format()
                                        .format("application/pdf")
                                        .registry(edu.wisc.library.sdg.preservation.manager.client.model.FormatRegistry.MIME)))
                        .encodingDiff(encodingDiff())
                        .validityDiff(validityDiff()
                                .addAddedItem(new Validity().valid(false).wellFormed(false)))
        ));
    }

    @Test
    public void diffFailsWhenObjectInBadState() {
        var bagName = "single-valid.zip";

        var testObject1 = new TestObject("o1", List.of(
                new TestFile("MASTER0", 100L)
                        .withFormat(FormatRegistry.MIME, JHOVE_ID, "image/tiff"),
                new TestFile("BIB0", 50L)
                        .withFormat(FormatRegistry.MIME, JHOVE_ID, "text/xml")
                        .withValidity(JHOVE_ID, false, false),
                new TestFile("REF0", 150L)
                        .withFormat(FormatRegistry.MIME, JHOVE_ID, "application/pdf")
                        .withValidity(JHOVE_ID, false, false)
        ));

        // create v1
        var ingestId = publicClient.ingestBag(DEFAULT_VAULT, bag(bagName))
                .getIngestId();
        updateBatchState(ingestId, INTERNAL_ANALYZING);
        registerObjectWithFiles(ingestId, testObject1);
        updateObjectStates(ingestId, INTERNAL_OBJ_ANALYSIS_FAILED, testObject1);
        updateBatchState(ingestId, INTERNAL_PENDING_REVIEW);
        assertIngestObject(ingestId, IngestObjectState.ANALYSIS_FAILED, testObject1);

        assertThrows(IllegalOperationException.class, () -> {
            activeClient.diffBatchObject(ingestId, testObject1.externalId);
        });
    }

    @Test
    public void dedupFileAttributes() {
        var bagName = "single-valid.zip";

        var testObject1 = new TestObject("o1", List.of(
                new TestFile("MASTER0", 100L)
                        .withFormat(FormatRegistry.MIME, JHOVE_ID, "image/tiff"),
                new TestFile("BIB0", 50L)
                        .withFormat(FormatRegistry.MIME, JHOVE_ID, "text/xml")
                        .withFormat(FormatRegistry.PRONOM, DROID_ID, "fmt/101")
                        .withFormat(FormatRegistry.MIME, PreservationConstants.USER_SOURCE, "application/x-xml")
                        .withFormat(FormatRegistry.PRONOM, PreservationConstants.USER_SOURCE, "x-fmt/101")
                        .withEncoding(PreservationConstants.USER_SOURCE, "UTF-8")
        ));

        var testObject1v2 = new TestObject("o1", List.of(
                new TestFile("MASTER0", 100L)
                        .withFormat(FormatRegistry.MIME, JHOVE_ID, "image/tiff"),
                new TestFile("BIB0", 50L)
                        .withFormat(FormatRegistry.MIME, JHOVE_ID, "text/xml-2")
                        .withFormat(FormatRegistry.PRONOM, DROID_ID, "fmt/101-2")
                        .withFormat(FormatRegistry.MIME, PreservationConstants.USER_SOURCE, "application/x-xml")
                        .withFormat(FormatRegistry.PRONOM, PreservationConstants.USER_SOURCE, "x-fmt/101")
                        .withEncoding(PreservationConstants.USER_SOURCE, "UTF-8")
        )).version(2);

        testObject1v2.internalId = testObject1.internalId;

        // create v1
        var ingestId = publicClient.ingestBag(DEFAULT_VAULT, bag(bagName))
                .getIngestId();
        updateBatchState(ingestId, INTERNAL_ANALYZING);
        registerObjectWithFiles(ingestId, testObject1);
        updateObjectStates(ingestId, INTERNAL_OBJ_PENDING_REVIEW, testObject1);
        updateBatchState(ingestId, INTERNAL_PENDING_REVIEW);
        assertIngestObject(ingestId, IngestObjectState.PENDING_REVIEW, testObject1);
        publicClient.approveIngestBatch(new ApproveIngestBatchRequest().ingestId(ingestId));
        updateBatchState(ingestId, INTERNAL_INGESTING);
        updateObjectStates(ingestId, INTERNAL_OBJ_INGESTING, testObject1);
        createVersionAndFinalize(testObject1);
        replicate(ingestId, testObject1);
        updateBatchState(ingestId, INTERNAL_REPLICATING);
        checkBatchReplications();

        // create v2
        var ingestId2 = publicClient.ingestBag(DEFAULT_VAULT, bag(bagName))
                .getIngestId();

        updateBatchState(ingestId2, INTERNAL_ANALYZING);
        registerObjectWithFiles(ingestId2, testObject1v2);

        updateObjectStates(ingestId2, INTERNAL_OBJ_PENDING_REVIEW, testObject1v2);
        updateBatchState(ingestId2, INTERNAL_PENDING_REVIEW);

        assertIngestObject(ingestId2, IngestObjectState.PENDING_REVIEW, testObject1v2);

        publicClient.approveIngestBatch(new ApproveIngestBatchRequest().ingestId(ingestId2));

        assertIngestObject(ingestId2, IngestObjectState.PENDING_INGESTION, testObject1v2);

        updateBatchState(ingestId2, INTERNAL_INGESTING);
        updateObjectStates(ingestId2, INTERNAL_OBJ_INGESTING, testObject1v2);
        assertIngestObject(ingestId2, IngestObjectState.INGESTING, testObject1v2);

        createVersionAndFinalize(testObject1v2);
        replicate(ingestId2, testObject1v2);

        updateBatchState(ingestId2, INTERNAL_REPLICATING);
        checkBatchReplications();

        retrieveAndAssertDefaultBatch(ingestId2, bagName, IngestBatchState.COMPLETE);

        assertPreservationObject(ingestId2, testObject1v2);

        assertBatches(ingestId, ingestId2);
    }

    @Test
    public void retryFailedReplication() {
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
        approveBatch(ingestId);

        updateBatchState(ingestId, INTERNAL_INGESTING);
        updateObjectStates(ingestId, INTERNAL_OBJ_INGESTING, defaultObject1, defaultObject2);

        createVersionAndFinalize(defaultObject1);
        replicate(ingestId, defaultObject1);

        updateBatchState(ingestId, INTERNAL_REPLICATING);

        createVersionAndFinalize(defaultObject2);
        updateObjectState(ingestId, defaultObject2.externalId, INTERNAL_OBJ_INGESTED);
        processJobs();
        var jobId = verifyWorkerReplicate(defaultObject2);
        updateJobState(jobId, JobState.EXECUTING);
        updateJobState(jobId, JobState.FAILED);
        assertIngestObjectState(ingestId, defaultObject2, IngestObjectState.REPLICATION_FAILED);

        assertBatchObjectEventsSubset(ingestId, defaultObject1, List.of(
                replicatedEvent(defaultObject1)
        ));
        assertBatchObjectEventsSubset(ingestId, defaultObject2, List.of(
                replicatedEventFailed(defaultObject2)
        ));

        checkBatchReplications();
        clearJobs();
        retryReplication(ingestId);

        assertIngestObjectState(ingestId, defaultObject1, IngestObjectState.COMPLETE);
        assertIngestObjectState(ingestId, defaultObject2, IngestObjectState.REPLICATING);

        processJobs();
        jobId = verifyWorkerReplicate(defaultObject2);
        updateJobState(jobId, JobState.EXECUTING);
        updateJobState(jobId, JobState.COMPLETE);
        var digest = DigestUtils.sha256Hex(defaultObject2.prefixedInternalId());
        markReplicated(defaultObject2, digest);

        assertIngestObjectState(ingestId, defaultObject2, IngestObjectState.COMPLETE);

        assertBatchObjectEventsSubset(ingestId, defaultObject2, List.of(
                replicatedEvent(defaultObject2)
        ));
    }

    @Test
    public void retryFailedIngest() {
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
        approveBatch(ingestId);

        updateBatchState(ingestId, INTERNAL_INGESTING);
        updateObjectStates(ingestId, INTERNAL_OBJ_INGESTING, defaultObject1, defaultObject2);

        createVersionAndFinalize(defaultObject1);
        replicate(ingestId, defaultObject1);

        updateObjectState(ingestId, defaultObject2.externalId, INTERNAL_OBJ_INGEST_FAILED);
        updateBatchState(ingestId, INTERNAL_INGEST_FAILED);

        clearJobs();
        retryIngest(ingestId);

        assertIngestObjectState(ingestId, defaultObject1, IngestObjectState.COMPLETE);
        assertIngestObjectState(ingestId, defaultObject2, IngestObjectState.PENDING_INGESTION);

        updateObjectState(ingestId, defaultObject2.externalId, INTERNAL_OBJ_INGESTING);
        createVersionAndFinalize(defaultObject2);
        replicate(ingestId, defaultObject2);

        assertIngestObjectState(ingestId, defaultObject2, IngestObjectState.COMPLETE);
    }

    private FormatDiff formatDiff() {
        return new FormatDiff()
                .added(new ArrayList<>())
                .removed(new ArrayList<>())
                .unchanged(new ArrayList<>());
    }

    private EncodingDiff encodingDiff() {
        return new EncodingDiff()
                .added(new ArrayList<>())
                .removed(new ArrayList<>())
                .unchanged(new ArrayList<>());
    }

    private ValidityDiff validityDiff() {
        return new ValidityDiff()
                .added(new ArrayList<>())
                .removed(new ArrayList<>())
                .unchanged(new ArrayList<>());
    }

}
