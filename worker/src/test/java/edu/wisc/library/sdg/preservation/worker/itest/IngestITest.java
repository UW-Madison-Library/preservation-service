package edu.wisc.library.sdg.preservation.worker.itest;

import edu.wisc.library.sdg.preservation.common.PreservationConstants;
import edu.wisc.library.sdg.preservation.common.util.Archiver;
import edu.wisc.library.sdg.preservation.common.util.UuidUtils;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.CreateObjectVersionRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.EventOutcome;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.EventType;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.FormatRegistry;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.IngestBatchState;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.LogLevel;
import io.ocfl.api.model.ObjectVersionId;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;

public class IngestITest extends ITestBase {

    // https://www.eicar.org/?page_id=3950
    private static final String VIRUS_TEST_STR = "X5O!P%@AP[4\\PZX54(P^)7CC)7}$EICAR-STANDARD-ANTIVIRUS-TEST-FILE!$H+H*";

    @Test
    public void ingestBagSingleObjectNoProblems() {
        var bagPath = setupBag("single-valid.zip");

        var obj = testObject1(ingestId, "47c70a8a70b3b94b74e5f1670f3f4e0d");
        obj.approverName = "John Smith";
        obj.approverAddress = "mailto:john.smith@example.com";

        // Analyze

        var batch = batch(bagPath);

        mockRetrieveBatch(batch);
        mockRegisterObject(obj);
        mockRegisterObjectFiles(obj);

        processBatch(ingestId);

        verifyRegisterFileDetails(obj);
        verifyBatchStartAnalysis();
        verifyObjectCompleteAnalysis(obj.externalId, true);
        verifyBatchCompleteAnalysis(true);

        verifyNormalAnalysisEvents(obj);

        // Ingest

        reset(managerApi);

        batch.state(IngestBatchState.PENDING_INGESTION).reviewedBy("Peter");

        mockRetrieveBatch(batch);
        mockRetrieveIngestBatch(obj);
        mockCreateVersion(obj);

        processBatch(ingestId);

        verifyNormalOcflEvent(obj);
        verifyCleanupEvent(ingestId);

        var ocflIds = ocflRepository.listObjectIds().toList();
        assertEquals(1, ocflIds.size());
        verifySuccessfulObjectIngest(obj);

        verifySuccessfulBatchIngest();

        assertOcflFiles(obj);
        assertCleanup(bagPath);
    }

    @Test
    public void ingestBagMultipleObjectNoProblems() {
        var bagPath = setupBag("multiple-valid.zip");

        var obj1 = testObject1(ingestId, "obj1");
        var obj2 = testObject2(ingestId, "obj2");

        // Analyze

        var batch = batch(bagPath);

        mockRetrieveBatch(batch);
        mockRegisterObject(obj1);
        mockRegisterObjectFiles(obj1);
        mockRegisterObject(obj2);
        mockRegisterObjectFiles(obj2);

        processBatch(ingestId);

        verifyRegisterFileDetails(obj1);
        verifyRegisterFileDetails(obj2);
        verifyBatchStartAnalysis();
        verifyObjectCompleteAnalysis(obj1.externalId, true);
        verifyObjectCompleteAnalysis(obj2.externalId, true);
        verifyBatchCompleteAnalysis(true);

        verifyNormalAnalysisEvents(obj1, obj2);

        // Ingest

        reset(managerApi);

        batch.state(IngestBatchState.PENDING_INGESTION).reviewedBy("Peter");

        mockRetrieveBatch(batch);
        mockRetrieveIngestBatch(obj1, obj2);
        mockCreateVersion(obj1);
        mockCreateVersion(obj2);

        processBatch(ingestId);

        verifyNormalOcflEvent(obj1);
        verifyNormalOcflEvent(obj2);
        verifyCleanupEvent(ingestId);

        var ocflIds = ocflRepository.listObjectIds().toList();
        assertEquals(2, ocflIds.size());
        verifySuccessfulObjectIngest(obj1);
        verifySuccessfulObjectIngest(obj2);
        assertOcflFiles(obj1);
        assertOcflFiles(obj2);

        verifySuccessfulBatchIngest();

        assertCleanup(bagPath);
    }

    @Test
    public void ingestBagWithUrlEncodedId() {
        var bagPath = setupBag("url-encoded.zip");

        var obj1 = testObject1(ingestId, "info:fedora/ag/obj1");

        // Analyze

        var batch = batch(bagPath);

        mockRetrieveBatch(batch);
        mockRegisterObject(obj1);
        mockRegisterObjectFiles(obj1);

        processBatch(ingestId);

        verifyRegisterFileDetails(obj1);
        verifyBatchStartAnalysis();
        verifyObjectCompleteAnalysis(obj1.externalId, true);
        verifyBatchCompleteAnalysis(true);

        verifyNormalAnalysisEvents(obj1);

        // Ingest

        reset(managerApi);

        batch.state(IngestBatchState.PENDING_INGESTION).reviewedBy("Peter");

        mockRetrieveBatch(batch);
        mockRetrieveIngestBatch(obj1);
        mockCreateVersion(obj1);

        processBatch(ingestId);

        verifyNormalOcflEvent(obj1);

        var ocflIds = ocflRepository.listObjectIds().toList();
        assertEquals(1, ocflIds.size());
        verifySuccessfulObjectIngest(obj1);
        assertOcflFiles(obj1);

        verifySuccessfulBatchIngest();

        assertCleanup(bagPath);
    }

    @Test
    public void failWhenBagInvalid() {
        var bagPath = setupBag("single-invalid-bag.zip");
        var batch = batch(bagPath);

        mockRetrieveBatch(batch);

        processBatch(ingestId);

        verifyBatchStartAnalysis();
        verifyBatchCompleteAnalysis(false);

        verifyVirusEvent(ingestId);
        verifyEvent(EventType.VALIDATE_BAG, EventOutcome.FAILURE, ingestId, List.of(
            logEntry(LogLevel.ERROR, "Invalid BagIt bag: File [/bag/manifest-md5.txt] is suppose to have a [MD5] hash of [deffd0d816041d989156373d7a1b6298] but was computed [ceffd0d816041d989156373d7a1b6298].")
        ));
    }

    @Test
    public void failObjectWhenObjectFails() {
        var bagPath = setupBag("multiple-valid.zip");

        var obj1 = testObject1(ingestId, "obj1");
        var obj2 = testObject2(ingestId, "obj2");

        // Analyze

        var batch = batch(bagPath);

        mockRetrieveBatch(batch);
        mockRegisterObject(obj1);
        mockRegisterObjectFiles(obj1);
        mockRegisterObject(obj2);
        mockRegisterObjectFiles(obj2.ingestObjectId, List.of(obj2.files.get(1)));

        doThrow(new RuntimeException("broke!")).when(managerApi).registerIngestObjectFile(argThat(arg -> {
            return Objects.equals(obj2.ingestObjectId, arg.getIngestObjectId())
                    && Objects.equals(obj2.files.get(0).path, arg.getFilePath());
        }));

        processBatch(ingestId);

        verifyRegisterFileDetails(obj1);
        verifyBatchStartAnalysis();
        verifyObjectCompleteAnalysis(obj1.externalId, true);
        verifyObjectCompleteAnalysis(obj2.externalId, false);
        verifyBatchCompleteAnalysis(true);

        verifyNormalAnalysisEvents(obj1);
        verifyEvent(EventType.IDENTIFY_OBJ, EventOutcome.FAILURE, ingestId, obj2.externalId, List.of());

        // Ingest

        reset(managerApi);

        batch.state(IngestBatchState.PENDING_INGESTION).reviewedBy("Peter");

        mockRetrieveBatch(batch);
        mockRetrieveIngestBatch(obj1);
        mockCreateVersion(obj1);

        processBatch(ingestId);

        verifyNormalOcflEvent(obj1);
        verifyCleanupEvent(ingestId);

        var ocflIds = ocflRepository.listObjectIds().toList();
        assertEquals(1, ocflIds.size());
        verifySuccessfulObjectIngest(obj1);
        assertOcflFiles(obj1);

        verifySuccessfulBatchIngest();

        assertCleanup(bagPath);
    }

    @Test
    public void failWhenDigestMismatchInvalid() {
        var bagPath = setupBag("single-invalid-file.zip");
        var batch = batch(bagPath);

        mockRetrieveBatch(batch);

        processBatch(ingestId);

        verifyBatchStartAnalysis();
        verifyBatchCompleteAnalysis(false);
        verifyVirusEvent(ingestId);
        verifyEvent(EventType.VALIDATE_BAG, EventOutcome.FAILURE, ingestId, List.of(
                logEntry(LogLevel.ERROR,
                        "Invalid BagIt bag: File [/bag/data/47c70a8a70b3b94b74e5f1670f3f4e0d/DC] is suppose to have a [MD5] hash of [62ed2707ca6f9a193386eb21b5e061f6] but was computed [84e19ade8b27337f8f17d6501cd2bb11].")
        ));
    }

    @Test
    public void failWhenEmptyBag() {
        var bagPath = setupBag("empty-bag.zip");
        var batch = batch(bagPath);

        mockRetrieveBatch(batch);

        processBatch(ingestId);

        verifyBatchStartAnalysis();
        verifyBatchCompleteAnalysis(false);
        verifyVirusEvent(ingestId);
        verifyEvent(EventType.VALIDATE_BAG, EventOutcome.FAILURE, ingestId, List.of(
                logEntry(LogLevel.ERROR,
                        "Invalid BagIt bag: Missing file /bag/bagit.txt")
        ));
    }

    @Test
    public void failWhenEmptyData() {
        var bagPath = setupBag("empty-data.zip");
        var batch = batch(bagPath);

        mockRetrieveBatch(batch);

        processBatch(ingestId);

        verifyBatchStartAnalysis();
        verifyBatchCompleteAnalysis(false);
        verifyVirusEvent(ingestId);
        verifyEvent(EventType.VALIDATE_BAG, EventOutcome.FAILURE, ingestId, List.of(
                logEntry(LogLevel.ERROR,
                        "The bag does not contain any objects.")
        ));
    }

    @Test
    public void shouldErrorWhenThereAreFilesInDataDir() {
        var bagPath = setupBag("files-in-data.zip");

        var obj = testObject1(ingestId, "47c70a8a70b3b94b74e5f1670f3f4e0d");

        var batch = batch(bagPath);

        mockRetrieveBatch(batch);
        mockRegisterObject(obj);
        mockRegisterObjectFiles(obj);

        processBatch(ingestId);

        verifyRegisterFileDetails(obj);
        verifyBatchStartAnalysis();
        verifyBatchCompleteAnalysis(false);

        verifyVirusEvent(ingestId);
        verifyEvent(EventType.VALIDATE_BAG, EventOutcome.FAILURE, ingestId, List.of(
                logEntry(LogLevel.ERROR, "The bag's data directory may only contain object directories. Found: file1.txt"),
                logEntry(LogLevel.ERROR, "The bag's data directory may only contain object directories. Found: file2.txt")
        ));
    }

    @Test
    public void cleanupRejectedBatch() {
        var bagPath = setupBag("single-valid.zip");

        var obj = testObject1(ingestId, "47c70a8a70b3b94b74e5f1670f3f4e0d");

        // Analyze

        var batch = batch(bagPath);

        mockRetrieveBatch(batch);
        mockRegisterObject(obj);
        mockRegisterObjectFiles(obj);

        processBatch(ingestId);

        verifyRegisterFileDetails(obj);
        verifyBatchStartAnalysis();
        verifyObjectCompleteAnalysis(obj.externalId, true);
        verifyBatchCompleteAnalysis(true);

        verifyNormalAnalysisEvents(obj);

        // Cleanup

        reset(managerApi);

        batch.state(IngestBatchState.PENDING_REJECTION);

        mockRetrieveBatch(batch);

        processBatch(ingestId);

        verifySuccessfulBatchReject();
        var ocflIds = ocflRepository.listObjectIds().toList();
        assertEquals(0, ocflIds.size());
        assertCleanup(bagPath);
    }

    @Test
    public void shouldUpdateExistingObject() {
        var bagPath = setupBag("single-valid.zip");

        var obj = testObject1(ingestId, "47c70a8a70b3b94b74e5f1670f3f4e0d");

        var batch = batch(bagPath);

        // Analyze Batch 1

        mockRetrieveBatch(batch);
        mockRegisterObject(obj);
        mockRegisterObjectFiles(obj);

        processBatch(ingestId);

        verifyRegisterFileDetails(obj);
        verifyBatchStartAnalysis();
        verifyObjectCompleteAnalysis(obj.externalId, true);
        verifyBatchCompleteAnalysis(true);
        verifyNormalAnalysisEvents(obj);

        // Ingest Batch 1

        reset(managerApi);

        batch.state(IngestBatchState.PENDING_INGESTION).reviewedBy("Peter");

        mockRetrieveBatch(batch);
        mockRetrieveIngestBatch(obj);
        mockCreateVersion(obj);

        processBatch(ingestId);

        verifyNormalOcflEvent(obj);
        verifyCleanupEvent(ingestId);

        var ocflIds = ocflRepository.listObjectIds().toList();
        assertEquals(1, ocflIds.size());
        verifySuccessfulObjectIngest(obj);
        assertOcflFiles(obj);
        verifySuccessfulBatchIngest();
        assertCleanup(bagPath);

        // Analyze Batch 2

        reset(managerApi);

        ingestId = randomSerialId();

        var objV2 = testObject1V2(ingestId, "47c70a8a70b3b94b74e5f1670f3f4e0d");

        var bagPathV2 = setupBag("single-update.zip");
        var batchV2 = batch(bagPathV2);
        objV2.internalObjectId = obj.internalObjectId;
        objV2.headPersistenceVersion = "v1";

        mockRetrieveBatch(batchV2);
        mockRegisterObject(objV2);
        mockRegisterObjectFiles(objV2);

        processBatch(ingestId);

        verifyRegisterFileDetails(objV2);
        verifyBatchStartAnalysis();
        verifyObjectCompleteAnalysis(objV2.externalId, true);
        verifyBatchCompleteAnalysis(true);
        verifyNormalAnalysisEvents(objV2);

        // Ingest Batch 2

        reset(managerApi);

        batchV2.state(IngestBatchState.PENDING_INGESTION).reviewedBy("Peter");

        mockRetrieveBatch(batchV2);
        mockRetrieveIngestBatch(objV2);
        mockCreateVersion(objV2);

        processBatch(ingestId);

        verifyNormalOcflEvent(objV2);
        verifyCleanupEvent(ingestId);

        ocflIds = ocflRepository.listObjectIds().toList();
        assertEquals(1, ocflIds.size());
        verifySuccessfulObjectIngest(objV2);
        assertOcflFiles(objV2);
        verifySuccessfulBatchIngest();
        assertCleanup(bagPath);
    }

    @Test
    public void shouldUpdateExistingObjectWhenOnlyFilesRemoved() {
        var bagPath = setupBag("single-valid.zip");

        var obj = testObject1(ingestId, "47c70a8a70b3b94b74e5f1670f3f4e0d");

        var batch = batch(bagPath);

        // Analyze Batch 1

        mockRetrieveBatch(batch);
        mockRegisterObject(obj);
        mockRegisterObjectFiles(obj);

        processBatch(ingestId);

        verifyRegisterFileDetails(obj);
        verifyBatchStartAnalysis();
        verifyObjectCompleteAnalysis(obj.externalId, true);
        verifyBatchCompleteAnalysis(true);
        verifyNormalAnalysisEvents(obj);

        // Ingest Batch 1

        reset(managerApi);

        batch.state(IngestBatchState.PENDING_INGESTION).reviewedBy("Peter");

        mockRetrieveBatch(batch);
        mockRetrieveIngestBatch(obj);
        mockCreateVersion(obj);

        processBatch(ingestId);

        verifyNormalOcflEvent(obj);
        verifyCleanupEvent(ingestId);

        var ocflIds = ocflRepository.listObjectIds().toList();
        assertEquals(1, ocflIds.size());
        verifySuccessfulObjectIngest(obj);
        assertOcflFiles(obj);
        verifySuccessfulBatchIngest();
        assertCleanup(bagPath);

        // Analyze Batch 2

        reset(managerApi);

        ingestId = randomSerialId();

        var objV2 = new TestObject(ingestId, "47c70a8a70b3b94b74e5f1670f3f4e0d", 2, new ArrayList<>(List.of(
                new TestFile("DC", "8e0a08d4b5e2d05019859082c60b7a589ffc12f95a55b17b521bca2ad6bf9926", 407L)
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

        var bagPathV2 = setupBag("single-update-remove.zip");
        var batchV2 = batch(bagPathV2);
        objV2.internalObjectId = obj.internalObjectId;
        objV2.headPersistenceVersion = "v1";

        mockRetrieveBatch(batchV2);
        mockRegisterObject(objV2);
        mockRegisterObjectFiles(objV2);

        processBatch(ingestId);

        verifyRegisterFileDetails(objV2);
        verifyBatchStartAnalysis();
        verifyObjectCompleteAnalysis(objV2.externalId, true);
        verifyBatchCompleteAnalysis(true);
        verifyNormalAnalysisEvents(objV2);

        // Ingest Batch 2

        reset(managerApi);

        batchV2.state(IngestBatchState.PENDING_INGESTION).reviewedBy("Peter");

        mockRetrieveBatch(batchV2);
        mockRetrieveIngestBatch(objV2);
        mockCreateVersion(objV2);

        processBatch(ingestId);

        verifyNormalOcflEvent(objV2);
        verifyCleanupEvent(ingestId);

        ocflIds = ocflRepository.listObjectIds().toList();
        assertEquals(1, ocflIds.size());
        verifySuccessfulObjectIngest(objV2);
        assertOcflFiles(objV2);
        verifySuccessfulBatchIngest();
        assertCleanup(bagPath);
    }

    @Test
    public void markObjectAsUnchangedWhenHasNoChanges() {
        var bagPath = setupBag("single-valid.zip");

        var obj = testObject1(ingestId, "47c70a8a70b3b94b74e5f1670f3f4e0d");

        var batch = batch(bagPath);

        // Analyze Batch 1

        mockRetrieveBatch(batch);
        mockRegisterObject(obj);
        mockRegisterObjectFiles(obj);

        processBatch(ingestId);

        verifyRegisterFileDetails(obj);
        verifyBatchStartAnalysis();
        verifyObjectCompleteAnalysis(obj.externalId, true);
        verifyBatchCompleteAnalysis(true);
        verifyNormalAnalysisEvents(obj);

        // Ingest Batch 1

        reset(managerApi);

        batch.state(IngestBatchState.PENDING_INGESTION).reviewedBy("Peter");

        mockRetrieveBatch(batch);
        mockRetrieveIngestBatch(obj);
        mockCreateVersion(obj);

        processBatch(ingestId);

        verifyNormalOcflEvent(obj);
        verifyCleanupEvent(ingestId);

        var ocflIds = ocflRepository.listObjectIds().toList();
        assertEquals(1, ocflIds.size());
        verifySuccessfulObjectIngest(obj);
        assertOcflFiles(obj);
        verifySuccessfulBatchIngest();
        assertCleanup(bagPath);

        // Analyze Batch 2

        reset(managerApi);

        ingestId = randomSerialId();

        var objV2 = testObject1(ingestId, "47c70a8a70b3b94b74e5f1670f3f4e0d");

        var bagPathV2 = setupBag("single-valid.zip");
        var batchV2 = batch(bagPathV2);
        objV2.internalObjectId = obj.internalObjectId;
        objV2.headPersistenceVersion = "v1";

        mockRetrieveBatch(batchV2);
        mockRegisterObject(objV2);
        mockRegisterObjectFiles(objV2);

        processBatch(ingestId);

        verifyRegisterFileDetails(objV2);
        verifyBatchStartAnalysis();
        verifyObjectCompleteAnalysis(objV2.externalId, true);
        verifyBatchCompleteAnalysis(true);
        verifyNormalAnalysisEvents(objV2);

        // Ingest Batch 2

        reset(managerApi);

        batchV2.state(IngestBatchState.PENDING_INGESTION).reviewedBy("Peter");

        mockRetrieveBatch(batchV2);
        mockRetrieveIngestBatch(objV2);
        mockCreateVersion(objV2);

        processBatch(ingestId);

        verifyObjectCompleteIngestingNoChange(objV2.externalId);

        verifyEvent(EventType.WRITE_OBJ_LOCAL, EventOutcome.NOT_EXECUTED, objV2.ingestId, objV2.externalId, List.of(
                logEntry(LogLevel.INFO, "Object was not written because it contains no changes")
        ));

        ocflIds = ocflRepository.listObjectIds().toList();
        assertEquals(1, ocflIds.size());
        assertOcflFiles(obj);
        verifySuccessfulBatchIngest();
        assertCleanup(bagPath);
    }

    @Test
    public void shouldFailWhenObjectUpdatedConcurrently() {
        var bagPath = setupBag("single-valid.zip");

        var obj = testObject1(ingestId, "47c70a8a70b3b94b74e5f1670f3f4e0d");
        obj.internalObjectId = UuidUtils.newWithPrefix();
        obj.headPersistenceVersion = "v0";

        var batch = batch(bagPath);

        // Analyze Batch 1

        mockRetrieveBatch(batch);
        mockRegisterObject(obj);
        mockRegisterObjectFiles(obj);

        processBatch(ingestId);

        verifyRegisterFileDetails(obj);
        verifyBatchStartAnalysis();
        verifyObjectCompleteAnalysis(obj.externalId, true);
        verifyBatchCompleteAnalysis(true);
        verifyNormalAnalysisEvents(obj);

        // Ingest Batch 1

        reset(managerApi);

        ocflRepository.updateObject(ObjectVersionId.head(obj.internalObjectId), null, updater -> {
            updater.writeFile(new ByteArrayInputStream("test".getBytes()), "file1.txt");
        });

        batch.state(IngestBatchState.PENDING_INGESTION).reviewedBy("Peter");

        mockRetrieveBatch(batch);
        mockRetrieveIngestBatch(obj);
        mockCreateVersion(obj);

        processBatch(ingestId);

        verifyBatchStartIngesting();
        verifyObjectCompleteIngesting(obj.externalId, false);
        verifyBatchCompleteIngesting(false);
        verifyEvent(EventType.WRITE_OBJ_LOCAL, EventOutcome.FAILURE, ingestId, obj.externalId, List.of());

        var ocflIds = ocflRepository.listObjectIds().toList();
        assertEquals(1, ocflIds.size());

        var desc = ocflRepository.describeVersion(ObjectVersionId.head(obj.internalObjectId));

        assertEquals("v1", desc.getVersionNum().toString());
        assertEquals(1, desc.getFiles().size());
        assertEquals("file1.txt", desc.getFile("file1.txt").getPath());

        assertNotCleanup(bagPath);
    }

    @Test
    public void shouldPurgeObjectWhenV1AndRegistrationFails() {
        var bagPath = setupBag("single-valid.zip");

        var obj = testObject1(ingestId, "47c70a8a70b3b94b74e5f1670f3f4e0d");
        obj.internalObjectId = UuidUtils.newWithPrefix();
        obj.headPersistenceVersion = "v0";

        var batch = batch(bagPath);

        // Analyze Batch 1

        mockRetrieveBatch(batch);
        mockRegisterObject(obj);
        mockRegisterObjectFiles(obj);

        processBatch(ingestId);

        verifyRegisterFileDetails(obj);
        verifyBatchStartAnalysis();
        verifyObjectCompleteAnalysis(obj.externalId, true);
        verifyBatchCompleteAnalysis(true);
        verifyNormalAnalysisEvents(obj);

        // Ingest Batch 1

        reset(managerApi);

        batch.state(IngestBatchState.PENDING_INGESTION).reviewedBy("Peter");

        mockRetrieveBatch(batch);
        mockRetrieveIngestBatch(obj);

        doThrow(new RuntimeException("foo!")).when(managerApi).createObjectVersion(new CreateObjectVersionRequest()
                .ingestObjectId(obj.ingestObjectId)
                .objectId(obj.internalObjectId));

        processBatch(ingestId);

        verifyBatchStartIngesting();
        verifyObjectCompleteIngesting(obj.externalId, false);
        verifyBatchCompleteIngesting(false);
        verifyNoEvents();

        var ocflIds = ocflRepository.listObjectIds().toList();
        assertEquals(0, ocflIds.size());

        assertNotCleanup(bagPath);
    }

    @Test
    public void failObjectWhenVirusDetected() throws IOException {
        if (!clamav.exists()) {
            // Skip this test if ClamAV isn't setup on the system
            return;
        }

        var src = Paths.get("src/test/resources/itest/bags/virus-bag");
        var dst = tempDir.resolve("bag");
        FileUtils.copyDirectory(src.toFile(), dst.toFile());
        Files.writeString(dst.resolve("data/47c70a8a70b3b94b74e5f1670f3f4e0d/DC"), VIRUS_TEST_STR);

        var bagPath = dst.getParent().resolve("virus-bag.zip");

        Archiver.archive(dst, bagPath, true);

        var obj1 = new TestObject(ingestId, "47c70a8a70b3b94b74e5f1670f3f4e0d", 1, new ArrayList<>(List.of(
                new TestFile("DC", "131f95c51cc819465fa1797f6ccacf9d494aaaff46fa3eac73ae63ffbdfd8267", 69L)
                        .withFormat(FormatRegistry.MIME, JHOVE_ID, "text/xml"),
                new TestFile("METHODMAP", "23beae1f8f82b83f6be9c40445fa535cd4c73f14b8433640ecda6a76911af558", 399L)
                        .withFormat(FormatRegistry.MIME, JHOVE_ID, "text/xml"),
                new TestFile("RELS-EXT", "9d603228bc05e508744dbe525da9bfd50693d58123c51e3089ae67d596012de8", 355L)
                        .withFormat(FormatRegistry.MIME, JHOVE_ID, "text/xml")
        )));

        // Analyze

        var batch = batch(bagPath);

        mockRetrieveBatch(batch);

        processBatch(ingestId);

        verifyBatchStartAnalysis();
        verifyBatchCompleteAnalysis(false);

        verifyEvent(EventType.VIRUS_SCAN_BAG, EventOutcome.FAILURE, ingestId, List.of(
                logEntry(LogLevel.ERROR, "Viruses detected in bag")
        ));
    }

    @Test
    public void ingestBagMultipleObjectWithMetadata() {
        var bagPath = setupBag("multiple-with-meta.zip");

        var obj1 = testObject1(ingestId, "obj1");
        var obj2 = testObject2(ingestId, "obj2");

        // from file-meta.csv
        obj1.file("DC")
                .withFormat(FormatRegistry.MIME, PreservationConstants.USER_SOURCE, "application/x-custom-1")
                .withFormat(FormatRegistry.PRONOM, PreservationConstants.USER_SOURCE, "fmt/5000")
                .withEncoding(PreservationConstants.USER_SOURCE, "UTF-8");
        obj1.file("METHODMAP")
                .withFormat(FormatRegistry.MIME, PreservationConstants.USER_SOURCE, "application/x-custom-2")
                .withFormat(FormatRegistry.PRONOM, PreservationConstants.USER_SOURCE, "fmt/5000")
                .withFormat(FormatRegistry.PRONOM, PreservationConstants.USER_SOURCE, "fmt/5001")
                .withEncoding(PreservationConstants.USER_SOURCE, "UTF-8");
        obj1.file("RELS-EXT")
                .withEncoding(PreservationConstants.USER_SOURCE, "US-ASCII");
        obj2.file("file1.txt")
                .withFormat(FormatRegistry.MIME, PreservationConstants.USER_SOURCE, "application/x-custom-3")
                .withEncoding(PreservationConstants.USER_SOURCE, "US-ASCII");
        obj2.file("sub/file2.txt")
                .withFormat(FormatRegistry.MIME, PreservationConstants.USER_SOURCE, "application/x-custom-3");

        // Analyze

        var batch = batch(bagPath);

        mockRetrieveBatch(batch);
        mockRegisterObject(obj1);
        mockRegisterObjectFiles(obj1);
        mockRegisterObject(obj2);
        mockRegisterObjectFiles(obj2);

        processBatch(ingestId);

        verifyRegisterFileDetails(obj1);
        verifyRegisterFileDetails(obj2);
        verifyBatchStartAnalysis();
        verifyObjectCompleteAnalysis(obj1.externalId, true);
        verifyObjectCompleteAnalysis(obj2.externalId, true);
        verifyBatchCompleteAnalysis(true);
        verifyNormalAnalysisEvents(obj1, obj2);

        // Ingest

        reset(managerApi);

        batch.state(IngestBatchState.PENDING_INGESTION).reviewedBy("Peter");

        mockRetrieveBatch(batch);
        mockRetrieveIngestBatch(obj1, obj2);
        mockCreateVersion(obj1);
        mockCreateVersion(obj2);

        processBatch(ingestId);

        verifyNormalOcflEvent(obj1);
        verifyNormalOcflEvent(obj2);
        verifyCleanupEvent(ingestId);

        var ocflIds = ocflRepository.listObjectIds().toList();
        assertEquals(2, ocflIds.size());
        verifySuccessfulObjectIngest(obj1);
        verifySuccessfulObjectIngest(obj2);
        assertOcflFiles(obj1);
        assertOcflFiles(obj2);

        verifySuccessfulBatchIngest();

        assertCleanup(bagPath);
    }

    private void verifyNormalAnalysisEvents(TestObject... objects) {
        verifyVirusEvent(ingestId);
        verifyEvent(EventType.VALIDATE_BAG, EventOutcome.SUCCESS, ingestId, List.of());

        for (TestObject object : objects) {
            verifyEvent(EventType.IDENTIFY_OBJ, EventOutcome.SUCCESS, ingestId, object.externalId, List.of());
            verifyFitsEvent(ingestId, object.externalId);
        }
    }

    private void verifyNormalOcflEvent(TestObject object) {
        verifyEvent(EventType.WRITE_OBJ_LOCAL, EventOutcome.SUCCESS, object.ingestId, object.externalId, List.of(
                logEntry(LogLevel.INFO, "Written to OCFL object " + object.internalObjectId)
        ));
    }

    private void verifyCleanupEvent(Long ingestId) {
        verifyEvent(EventType.DELETE_BAG, EventOutcome.SUCCESS, ingestId, null, null);
    }

    private void verifyVirusEvent(Long ingestId) {
        if (clamav.exists()) {
            verifyEvent(EventType.VIRUS_SCAN_BAG, EventOutcome.SUCCESS, ingestId, List.of());
        }
    }

    private void verifyFitsEvent(Long ingestId, String externalObjectId) {
        if (fitsExists) {
            verifyEvent(EventType.IDENTIFY_FILE_FORMAT, EventOutcome.SUCCESS, ingestId, externalObjectId, List.of());
        }
    }

}
