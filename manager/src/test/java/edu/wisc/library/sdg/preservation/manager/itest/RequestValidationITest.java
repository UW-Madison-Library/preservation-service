package edu.wisc.library.sdg.preservation.manager.itest;

import com.google.common.io.Files;
import edu.wisc.library.sdg.preservation.common.exception.ValidationException;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.CreateObjectVersionRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.DataStore;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.DeferJobRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.DeleteObjectVersionRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.Event;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.EventOutcome;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.EventType;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.FileEncoding;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.FileFormat;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.FileValidity;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.FinalizeObjectVersionRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.FormatRegistry;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.JobState;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.LogEntry;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.LogLevel;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ObjectVersionReplicatedRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RecordIngestEventRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RecordJobLogsRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RegisterIngestObjectFileDetailsRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RegisterIngestObjectFileRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RegisterIngestObjectRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.SetObjectStorageProblemsRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.StorageProblemType;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.UpdateJobStateRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.ApproveIngestBatchRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.ApproveIngestObjectRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.RejectIngestBatchRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.RejectIngestObjectRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.RetrieveObjectsRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.ValidatePreservationObjectRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RequestValidationITest extends ITestBase {

    @TempDir
    public Path tempDir;

    @Test
    public void retrieveBatchObjectValidation() {
        expectValidationException("externalObjectId", () -> {
            publicClient.retrieveBatchObject(1L, "");
        });
    }

    @Test
    public void approveIngestBatchValidation() {
        expectValidationException("ingestId", () -> {
            publicClient.approveIngestBatch(new ApproveIngestBatchRequest().ingestId(null));
        });
    }

    @Test
    public void rejectIngestBatchValidation() {
        expectValidationException("ingestId", () -> {
            publicClient.rejectIngestBatch(new RejectIngestBatchRequest().ingestId(null));
        });
    }

    @Test
    public void approveIngestObjectValidation() {
        expectValidationException("ingestId", () -> {
            publicClient.approveIngestObject(new ApproveIngestObjectRequest()
                    .ingestId(null).externalObjectId("id"));
        });
        expectValidationException("externalObjectId", () -> {
            publicClient.approveIngestObject(new ApproveIngestObjectRequest()
                    .ingestId(1L).externalObjectId(""));
        });
    }

    @Test
    public void rejectIngestObjectValidation() {
        expectValidationException("ingestId", () -> {
            publicClient.rejectIngestObject(new RejectIngestObjectRequest()
                    .ingestId(null).externalObjectId("id"));
        });
        expectValidationException("externalObjectId", () -> {
            publicClient.rejectIngestObject(new RejectIngestObjectRequest()
                    .ingestId(1L).externalObjectId(""));
        });
    }

    @Test
    public void ingestBagValidation() throws IOException {
        var tempFile = tempDir.resolve("test.zip");
        Files.touch(tempFile.toFile());

        expectValidationException("vault", () -> {
            publicClient.ingestBag("", tempFile.toFile());
        });
    }

    @Test
    public void retrieveObjectsValidation() {
        expectValidationException("vault", () -> {
            publicClient.retrieveObjects(new RetrieveObjectsRequest()
                    .vault("").externalObjectIds(List.of()).allVersions(true));
        });
    }

    @Test
    public void validateObjectValidation() {
        expectValidationException("vault", () -> {
            publicClient.validateObject(new ValidatePreservationObjectRequest()
                    .vault("").externalObjectId("id").contentFixityCheck(false));
        });
        expectValidationException("externalObjectId", () -> {
            publicClient.validateObject(new ValidatePreservationObjectRequest()
                    .vault("vault").externalObjectId("").contentFixityCheck(false));
        });
        expectValidationException("contentFixityCheck", () -> {
            publicClient.validateObject(new ValidatePreservationObjectRequest()
                    .vault("vault").externalObjectId("id").contentFixityCheck(null));
        });
    }

    @Test
    public void describePreservationObjectValidation() {
        expectValidationException("vault", () -> {
            publicClient.describePreservationObject("", "id", null);
        });
        expectValidationException("externalObjectId", () -> {
            publicClient.describePreservationObject("vault", "", null);
        });
    }

    @Test
    public void retrieveObjectLogsValidation() {
        expectValidationException("vault", () -> {
            publicClient.retrieveObjectEvents("", "id");
        });
        expectValidationException("externalObjectId", () -> {
            publicClient.retrieveObjectEvents("vault", "");
        });
    }

    @Test
    public void retrieveObjectStorageProblemsValidation() {
        expectValidationException("vault", () -> {
            publicClient.retrieveObjectStorageProblems("", "id");
        });
        expectValidationException("externalObjectId", () -> {
            publicClient.retrieveObjectStorageProblems("vault", "");
        });
    }

    @Test
    public void registerIngestObjectValidation() {
        expectValidationException("ingestId", () -> {
            internalClient.registerIngestObject(new RegisterIngestObjectRequest()
                            .ingestId(null)
                            .externalObjectId("id")
                            .objectRootPath("path"));
        });
        expectValidationException("externalObjectId", () -> {
            internalClient.registerIngestObject(new RegisterIngestObjectRequest()
                            .ingestId(1L)
                            .externalObjectId("")
                            .objectRootPath("path"));
        });
        expectValidationException("objectRootPath", () -> {
            internalClient.registerIngestObject(new RegisterIngestObjectRequest()
                            .ingestId(1L)
                            .externalObjectId("id")
                            .objectRootPath(""));
        });
    }

    @Test
    public void registerObjectFileValidation() {
        expectValidationException("ingestObjectId", () -> {
            internalClient.registerIngestObjectFile(new RegisterIngestObjectFileRequest()
                    .ingestObjectId(null).filePath("path"));
        });
        expectValidationException("filePath", () -> {
            internalClient.registerIngestObjectFile(new RegisterIngestObjectFileRequest()
                    .ingestObjectId(1L).filePath(""));
        });
    }

    @Test
    public void registerObjectFileDetailsValidation() {
        expectValidationException("ingestFileId", () -> {
            internalClient.registerIngestObjectFileDetails(new RegisterIngestObjectFileDetailsRequest()
                    .ingestFileId(null).sha256Digest("digest").fileSize(1L).formats(List.of()));
        });
        expectValidationException("sha256Digest", () -> {
            internalClient.registerIngestObjectFileDetails(new RegisterIngestObjectFileDetailsRequest()
                    .ingestFileId(1L).sha256Digest("").fileSize(1L).formats(List.of()));
        });
        expectValidationException("fileSize", () -> {
            internalClient.registerIngestObjectFileDetails(new RegisterIngestObjectFileDetailsRequest()
                    .ingestFileId(1L).sha256Digest("digest").fileSize(null).formats(List.of()));
        });
        expectValidationException("formatRegistry", () -> {
            internalClient.registerIngestObjectFileDetails(new RegisterIngestObjectFileDetailsRequest()
                    .ingestFileId(1L).sha256Digest("digest").fileSize(1L).formats(List.of(
                            new FileFormat().formatRegistry(null).source("source").format("format")
                    )));
        });
        expectValidationException("source", () -> {
            internalClient.registerIngestObjectFileDetails(new RegisterIngestObjectFileDetailsRequest()
                    .ingestFileId(1L).sha256Digest("digest").fileSize(1L).formats(List.of(
                            new FileFormat().formatRegistry(FormatRegistry.MIME).source("").format("format")
                    )));
        });
        expectValidationException("format", () -> {
            internalClient.registerIngestObjectFileDetails(new RegisterIngestObjectFileDetailsRequest()
                    .ingestFileId(1L).sha256Digest("digest").fileSize(1L).formats(List.of(
                            new FileFormat().formatRegistry(FormatRegistry.MIME).source("source").format("")
                    )));
        });
        expectValidationException("source", () -> {
            internalClient.registerIngestObjectFileDetails(new RegisterIngestObjectFileDetailsRequest()
                    .ingestFileId(1L).sha256Digest("digest").fileSize(1L).encoding(List.of(
                            new FileEncoding().source("").encoding("UTF-8")
                    )));
        });
        expectValidationException("encoding", () -> {
            internalClient.registerIngestObjectFileDetails(new RegisterIngestObjectFileDetailsRequest()
                    .ingestFileId(1L).sha256Digest("digest").fileSize(1L).encoding(List.of(
                            new FileEncoding().source("source").encoding("")
                    )));
        });
        expectValidationException("validity", () -> {
            internalClient.registerIngestObjectFileDetails(new RegisterIngestObjectFileDetailsRequest()
                    .ingestFileId(1L).sha256Digest("digest").fileSize(1L).validity(List.of(
                            new FileValidity().source("source")
                    )));
        });
    }

    @Test
    public void recordIngestLogsValidation() {
        expectValidationException("ingestId", () -> {
            internalClient.recordIngestEvent(new RecordIngestEventRequest());
        });
        expectValidationException("ingestId", () -> {
            internalClient.recordIngestEvent(new RecordIngestEventRequest()
                            .event(new Event()
                                    .type(EventType.RECEIVE_BAG)
                                    .outcome(EventOutcome.SUCCESS)
                                    .eventTimestamp(OffsetDateTime.now())
                                    .addLogsItem(new LogEntry()
                                            .level(LogLevel.INFO)
                                            .message("message")
                                            .createdTimestamp(OffsetDateTime.now())))
            );
        });
        expectValidationException("event", () -> {
            internalClient.recordIngestEvent(new RecordIngestEventRequest()
                    .ingestId(1L)
            );
        });
        expectValidationException("type", () -> {
            internalClient.recordIngestEvent(new RecordIngestEventRequest()
                    .ingestId(1L)
                    .event(new Event()
                            .outcome(EventOutcome.SUCCESS)
                            .eventTimestamp(OffsetDateTime.now())
                            .addLogsItem(new LogEntry()
                                    .level(LogLevel.INFO)
                                    .message("message")
                                    .createdTimestamp(OffsetDateTime.now())))
            );
        });
        expectValidationException("outcome", () -> {
            internalClient.recordIngestEvent(new RecordIngestEventRequest()
                    .ingestId(1L)
                    .event(new Event()
                            .type(EventType.RECEIVE_BAG)
                            .eventTimestamp(OffsetDateTime.now())
                            .addLogsItem(new LogEntry()
                                    .level(LogLevel.INFO)
                                    .message("message")
                                    .createdTimestamp(OffsetDateTime.now())))
            );
        });
        expectValidationException("eventTimestamp", () -> {
            internalClient.recordIngestEvent(new RecordIngestEventRequest()
                    .ingestId(1L)
                    .event(new Event()
                            .type(EventType.RECEIVE_BAG)
                            .outcome(EventOutcome.SUCCESS)
                            .addLogsItem(new LogEntry()
                                    .level(LogLevel.INFO)
                                    .message("message")
                                    .createdTimestamp(OffsetDateTime.now())))
            );
        });
        expectValidationException("level", () -> {
            internalClient.recordIngestEvent(new RecordIngestEventRequest()
                    .ingestId(1L)
                    .event(new Event()
                            .type(EventType.RECEIVE_BAG)
                            .outcome(EventOutcome.SUCCESS)
                            .eventTimestamp(OffsetDateTime.now())
                            .addLogsItem(new LogEntry()
                                    .message("message")
                                    .createdTimestamp(OffsetDateTime.now())))
            );
        });
        expectValidationException("message", () -> {
            internalClient.recordIngestEvent(new RecordIngestEventRequest()
                    .ingestId(1L)
                    .event(new Event()
                            .type(EventType.RECEIVE_BAG)
                            .outcome(EventOutcome.SUCCESS)
                            .eventTimestamp(OffsetDateTime.now())
                            .addLogsItem(new LogEntry()
                                    .level(LogLevel.INFO)
                                    .createdTimestamp(OffsetDateTime.now())))
            );
        });
        expectValidationException("createdTimestamp", () -> {
            internalClient.recordIngestEvent(new RecordIngestEventRequest()
                    .ingestId(1L)
                    .event(new Event()
                            .type(EventType.RECEIVE_BAG)
                            .outcome(EventOutcome.SUCCESS)
                            .eventTimestamp(OffsetDateTime.now())
                            .addLogsItem(new LogEntry()
                                    .level(LogLevel.INFO)
                                    .message("message")))
            );
        });
    }

    @Test
    public void createObjectVersionValidation() {
        expectValidationException("objectId", () -> {
            internalClient.createObjectVersion(new CreateObjectVersionRequest()
                    .objectId("").ingestObjectId(1L));
        });
        expectValidationException("ingestObjectId", () -> {
            internalClient.createObjectVersion(new CreateObjectVersionRequest()
                    .objectId("id").ingestObjectId(null));
        });
    }

    @Test
    public void deleteObjectVersionValidation() {
        expectValidationException("objectVersionId", () -> {
            internalClient.deleteObjectVersion(new DeleteObjectVersionRequest().objectVersionId(null));
        });
    }

    @Test
    public void finalizeObjectVersionValidation() {
        expectValidationException("ingestObjectId", () -> {
            internalClient.finalizeObjectVersion(new FinalizeObjectVersionRequest()
                    .ingestObjectId(null).objectVersionId(1L).persistenceVersion("v1"));
        });
        expectValidationException("objectVersionId", () -> {
            internalClient.finalizeObjectVersion(new FinalizeObjectVersionRequest()
                    .ingestObjectId(1L).objectVersionId(null).persistenceVersion("v1"));
        });
        expectValidationException("persistenceVersion", () -> {
            internalClient.finalizeObjectVersion(new FinalizeObjectVersionRequest()
                    .ingestObjectId(1L).objectVersionId(1L).persistenceVersion(""));
        });
    }

    @Test
    public void objectVersionReplicatedValidation() {
        expectValidationException("objectId", () -> {
            internalClient.objectVersionReplicated(new ObjectVersionReplicatedRequest()
                    .objectId("")
                    .persistenceVersion("v1")
                    .dataStore(DataStore.GLACIER)
                    .dataStoreKey("key")
                    .sha256Digest("digest"));
        });
        expectValidationException("persistenceVersion", () -> {
            internalClient.objectVersionReplicated(new ObjectVersionReplicatedRequest()
                    .objectId("id")
                    .persistenceVersion("")
                    .dataStore(DataStore.GLACIER)
                    .dataStoreKey("key")
                    .sha256Digest("digest"));
        });
        expectValidationException("dataStore", () -> {
            internalClient.objectVersionReplicated(new ObjectVersionReplicatedRequest()
                    .objectId("id")
                    .persistenceVersion("v1")
                    .dataStore(null)
                    .dataStoreKey("key")
                    .sha256Digest("digest"));
        });
        expectValidationException("dataStoreKey", () -> {
            internalClient.objectVersionReplicated(new ObjectVersionReplicatedRequest()
                    .objectId("id")
                    .persistenceVersion("v1")
                    .dataStore(DataStore.GLACIER)
                    .dataStoreKey("")
                    .sha256Digest("digest"));
        });
        expectValidationException("sha256Digest", () -> {
            internalClient.objectVersionReplicated(new ObjectVersionReplicatedRequest()
                    .objectId("id")
                    .persistenceVersion("v1")
                    .dataStore(DataStore.GLACIER)
                    .dataStoreKey("key")
                    .sha256Digest(""));
        });
    }

    @Test
    public void getPremisDocumentValidation() {
        expectValidationException("internalObjectId", () -> {
            internalClient.getPremisDocument("", List.of(1));
        });
    }

    @Test
    public void setObjectStorageProblemsValidation() {
        expectValidationException("objectId", () -> {
            internalClient.setObjectStorageProblems(new SetObjectStorageProblemsRequest()
                    .objectId("").objectProblem(StorageProblemType.NONE).dataStore(DataStore.GLACIER));
        });
        expectValidationException("objectProblem", () -> {
            internalClient.setObjectStorageProblems(new SetObjectStorageProblemsRequest()
                    .objectId("id").objectProblem(null).dataStore(DataStore.GLACIER));
        });
        expectValidationException("dataStore", () -> {
            internalClient.setObjectStorageProblems(new SetObjectStorageProblemsRequest()
                    .objectId("id").objectProblem(StorageProblemType.NONE).dataStore(null));
        });
    }

    @Test
    public void updateJobStateValidation() {
        expectValidationException("jobId", () -> {
            internalClient.updateJobState(new UpdateJobStateRequest().jobId(null).state(JobState.COMPLETE));
        });
        expectValidationException("state", () -> {
            internalClient.updateJobState(new UpdateJobStateRequest().jobId(1L).state(null));
        });
    }

    @Test
    public void deferJobValidation() {
        expectValidationException("jobId", () -> {
            internalClient.defer(new DeferJobRequest().jobId(null));
        });
    }

    @Test
    public void recordJobLogsValidation() {
        expectValidationException("logEntries", () -> {
            internalClient.recordJobLogs(new RecordJobLogsRequest().jobId(1L).logEntries(null));
        });
        expectValidationException("jobId", () -> {
            internalClient.recordJobLogs(new RecordJobLogsRequest()
                    .jobId(null)
                    .addLogEntriesItem(new LogEntry()
                            .level(LogLevel.ERROR)
                            .message("message")
                            .createdTimestamp(OffsetDateTime.now())));
        });
        expectValidationException("level", () -> {
            internalClient.recordJobLogs(new RecordJobLogsRequest()
                    .jobId(1L)
                    .addLogEntriesItem(new LogEntry()
                            .level(null)
                            .message("message")
                            .createdTimestamp(OffsetDateTime.now())));
        });
        expectValidationException("message", () -> {
            internalClient.recordJobLogs(new RecordJobLogsRequest()
                    .jobId(1L)
                    .addLogEntriesItem(new LogEntry()
                            .level(LogLevel.ERROR)
                            .message("")
                            .createdTimestamp(OffsetDateTime.now())));
        });
        expectValidationException("createdTimestamp", () -> {
            internalClient.recordJobLogs(new RecordJobLogsRequest()
                    .jobId(1L)
                    .addLogEntriesItem(new LogEntry()
                            .level(LogLevel.ERROR)
                            .message("message")
                            .createdTimestamp(null)));
        });
    }

    private void expectValidationException(String field, Runnable runnable) {
        assertThat(assertThrows(ValidationException.class, runnable::run).getMessage(),
                containsString(String.format("'%s'", field)));
    }

}
