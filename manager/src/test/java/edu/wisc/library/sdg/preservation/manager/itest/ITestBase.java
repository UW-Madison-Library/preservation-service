package edu.wisc.library.sdg.preservation.manager.itest;

import edu.wisc.library.sdg.preservation.manager.client.api.PreservationManagerApi;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.BatchCompleteAnalysisRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.BatchCompleteIngestingRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.BatchCompleteRejectingRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.BatchStartAnalysisRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.BatchStartIngestingRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.BatchStartRejectingRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.CreateObjectVersionRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.DataStore;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.EventOutcome;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.EventType;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.FinalizeObjectVersionRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.FormatRegistry;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.JobPollResponse;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.JobState;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.JobType;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.LogEntry;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.LogLevel;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ObjectCompleteAnalysisRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ObjectCompleteIngestingRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ObjectInfo;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ObjectStartIngestingRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ObjectVersionReplicatedRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.Outcome;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RecordIngestEventRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RecordJobLogsRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RecordPreservationEventRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RegisterIngestObjectFileDetailsRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RegisterIngestObjectFileRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RegisterIngestObjectRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RetrieveJob;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.SetObjectStorageProblemsRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.StorageProblemType;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.UpdateJobStateRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.VersionStorageProblem;
import edu.wisc.library.sdg.preservation.manager.client.model.ApproveIngestBatchRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.BatchRetryIngestRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.BatchRetryReplicateRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.DeleteObjectRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.DescribePreservationObjectResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.Event;
import edu.wisc.library.sdg.preservation.manager.client.model.FileEncoding;
import edu.wisc.library.sdg.preservation.manager.client.model.FileFormat;
import edu.wisc.library.sdg.preservation.manager.client.model.FileValidity;
import edu.wisc.library.sdg.preservation.manager.client.model.IngestBatchState;
import edu.wisc.library.sdg.preservation.manager.client.model.IngestBatchSummary;
import edu.wisc.library.sdg.preservation.manager.client.model.IngestObjectState;
import edu.wisc.library.sdg.preservation.manager.client.model.JobDetails;
import edu.wisc.library.sdg.preservation.manager.client.model.ObjectStorageProblem;
import edu.wisc.library.sdg.preservation.manager.client.model.ReplicateObjectRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.RestorePreservationObjectRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.RetrieveObjectsRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.RetrieveRequestPart;
import edu.wisc.library.sdg.preservation.manager.client.model.ValidateObjectRemoteRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.ValidatePreservationObjectRequest;
import edu.wisc.library.sdg.preservation.manager.job.SimpleJobBroker;
import edu.wisc.library.sdg.preservation.manager.matcher.ObjectFileMatcher;
import edu.wisc.library.sdg.preservation.manager.scheduling.BatchReplicationMonitor;
import edu.wisc.library.sdg.preservation.manager.util.Agent;
import org.apache.commons.codec.digest.DigestUtils;
import org.flywaydb.core.Flyway;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static edu.wisc.library.sdg.preservation.manager.itest.InternalAlias.INTERNAL_ANALYZING;
import static edu.wisc.library.sdg.preservation.manager.itest.InternalAlias.INTERNAL_INGESTING;
import static edu.wisc.library.sdg.preservation.manager.itest.InternalAlias.INTERNAL_OBJ_INGESTED;
import static edu.wisc.library.sdg.preservation.manager.itest.InternalAlias.INTERNAL_OBJ_INGESTING;
import static edu.wisc.library.sdg.preservation.manager.itest.InternalAlias.INTERNAL_OBJ_PENDING_REVIEW;
import static edu.wisc.library.sdg.preservation.manager.itest.InternalAlias.INTERNAL_PENDING_REVIEW;
import static edu.wisc.library.sdg.preservation.manager.itest.InternalAlias.INTERNAL_REPLICATING;
import static edu.wisc.library.sdg.preservation.manager.matcher.PreservationMatchers.ingestBatchObject;
import static edu.wisc.library.sdg.preservation.manager.matcher.PreservationMatchers.objectFile;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "logging.level.edu.wisc.library.sdg.preservation=WARN"
        })
@ActiveProfiles({"default", "itest"})
public abstract class ITestBase {

    protected static final String TEST_ORG = "test-organization";
    protected static final String ANOTHER_ORG = "another-org";
    protected static final String DEFAULT_VAULT = "test-vault";

    private static final String SERVICE_ADMIN_1 = "service-admin-1@example.com";
    private static final String ADMIN_1 = "admin-1@example.com";
    private static final String USER_1 = "user-1@example.com";

    public static final String FEDORA_USER = "fedora-object-preserver";
    public static final String SERVICE_ADMIN_1_USER = "service-admin-1";
    public static final String ADMIN_1_USER = "admin-1";
    public static final String USER_1_USER = "user-1";

    protected static final String FITS_ID = "FITS-1.6.0";
    protected static final String JHOVE_ID = FITS_ID + ":Jhove-1.26.1";
    protected static final String DROID_ID = FITS_ID + ":Droid-6.5.2";
    protected static final String TIKA_ID = FITS_ID + ":Tika-2.6.0";

    @LocalServerPort
    protected Integer port;

    @Autowired
    protected Flyway flyway;

    @Autowired
    protected BatchReplicationMonitor replicationMonitor;

    @Autowired
    protected SimpleJobBroker jobBroker;

    protected DbHelper dbHelper;

    protected PreservationManagerApi activeClient;
    protected PreservationManagerApi serviceAdminUserClient;
    protected PreservationManagerApi adminUserClient;
    protected PreservationManagerApi regularUserClient;
    protected PreservationManagerApi publicClient;
    protected ManagerInternalApi internalClient;

    protected TestObject defaultObject1;
    protected TestObject defaultObject1v2;
    protected TestObject defaultObject2;

    private List<JobPollResponse> jobs;

    @BeforeEach
    public void setupBase() {
        this.publicClient = ClientUtil.defaultPublicManagerClient(port);
        this.serviceAdminUserClient = ClientUtil.proxyingManagerClient(port, SERVICE_ADMIN_1);
        this.adminUserClient = ClientUtil.proxyingManagerClient(port, ADMIN_1);
        this.regularUserClient = ClientUtil.proxyingManagerClient(port, USER_1);
        this.activeClient = publicClient;
        this.internalClient = ClientUtil.defaultInternalManagerClient(port);
        this.dbHelper = new DbHelper(flyway);
        this.dbHelper.baseline();
        this.jobs = new ArrayList<>();
        jobBroker.invalidateCache();

        defaultObject1 = new TestObject("o1")
                .addFileWithFormat("MASTER0", 100L,
                        new TestFormat(edu.wisc.library.sdg.preservation.manager.db.model.FormatRegistry.MIME, JHOVE_ID, "image/tiff"))
                .addFileWithFormat("BIB0", 50L,
                        new TestFormat(edu.wisc.library.sdg.preservation.manager.db.model.FormatRegistry.MIME, JHOVE_ID, "text/xml"),
                        new TestFormat(edu.wisc.library.sdg.preservation.manager.db.model.FormatRegistry.PRONOM, DROID_ID, "fmt/101"));
        defaultObject1v2 = new TestObject("o1")
                .version(2)
                .addFileWithFormat("MASTER0", 100L,
                        new TestFormat(edu.wisc.library.sdg.preservation.manager.db.model.FormatRegistry.MIME, JHOVE_ID, "image/tiff"))
                .addFileWithFormat("BIB0", 100L,
                        new TestFormat(edu.wisc.library.sdg.preservation.manager.db.model.FormatRegistry.MIME, JHOVE_ID, "text/xml"),
                        new TestFormat(edu.wisc.library.sdg.preservation.manager.db.model.FormatRegistry.PRONOM, DROID_ID, "fmt/101"));
        defaultObject1v2.internalId = defaultObject1.internalId;
        defaultObject2 = new TestObject("o2")
                .addFileWithFormat("ENCTEXT", 200L,
                        new TestFormat(edu.wisc.library.sdg.preservation.manager.db.model.FormatRegistry.MIME, JHOVE_ID, "text/xml"),
                        new TestFormat(edu.wisc.library.sdg.preservation.manager.db.model.FormatRegistry.PRONOM, DROID_ID, "fmt/101"))
                .addFileWithFormat("TECH0", 20L,
                        new TestFormat(edu.wisc.library.sdg.preservation.manager.db.model.FormatRegistry.MIME, JHOVE_ID, "text/xml"),
                        new TestFormat(edu.wisc.library.sdg.preservation.manager.db.model.FormatRegistry.PRONOM, DROID_ID, "fmt/101"));
    }

    @AfterEach
    public void afterEach() {
        // cleanup any outstanding jobs before next test
        processJobs();
    }

    /**
     * Changes the client that's used by default by the test infrastructure to the client that's tied to the user
     * with org admin access to uwdcc
     */
    protected void asAdminUser() {
        activeClient = adminUserClient;
    }

    /**
     * Changes the client that's used by default by the test infrastructure to the client that's tied to the user
     * with org user access to uwdcc
     */
    protected void asRegularUser() {
        activeClient = regularUserClient;
    }

    /**
     * Changes the client that's used by default by the test infrastructure to the client that's tied to the programmatic
     * uwdcc user
     */
    protected void asProgrammaticUser() {
        activeClient = publicClient;
    }

    protected void retrieveAndAssertDefaultBatch(Long ingestId, String bagName, IngestBatchState state) {
        var ingestBatch = activeClient.retrieveBatch(ingestId)
                .getIngestBatch();

        assertBatchDefault(ingestBatch, ingestId, bagName, state);
    }

    protected void assertBatchDefault(IngestBatchSummary actualBatch, Long ingestId, String filename, IngestBatchState state) {
        assertEquals(ingestId, actualBatch.getIngestId());
        assertEquals(TEST_ORG, actualBatch.getOrgName());
        assertEquals(DEFAULT_VAULT, actualBatch.getVault());
        assertEquals(filename, actualBatch.getOriginalFilename());
        assertEquals(state, actualBatch.getState());
        assertEquals(ClientUtil.FEDORA_USER, actualBatch.getCreatedBy());
    }

    protected void assertBatches(Long... ingestIds) {
        var batches = activeClient.searchBatches(TEST_ORG, null, null, 100, 0);
        var ids = batches.getBatches().stream()
                .map(IngestBatchSummary::getIngestId)
                .toList();
        assertThat(ids, containsInAnyOrder(ingestIds));
    }

    protected void assertPreservationObjectDefault(DescribePreservationObjectResponse response,
                                                 Long ingestId, String externalId, int version) {
        assertEquals(DEFAULT_VAULT, response.getVault());
        assertEquals(ingestId, response.getIngestId());
        assertEquals(externalId, response.getExternalObjectId());
        assertEquals(version, response.getVersion());
    }

    protected void assertPreservationObject(Long ingestId, TestObject object) {
        var matchers = new ArrayList<ObjectFileMatcher>();

        object.files.forEach(file -> {
            matchers.add(objectFile(f -> {
                f.filePath(file.path)
                        .sha256Digest(file.digest)
                        .fileSize(file.size);

                file.formats.forEach(format -> {
                    f.addFormatsItem(new FileFormat()
                            .formatRegistry(edu.wisc.library.sdg.preservation.manager.client.model.FormatRegistry.fromValue(format.registry.toString()))
                            .source(format.source)
                            .format(format.format));
                });

                file.encodings.forEach(e -> {
                    f.addEncodingItem(new FileEncoding()
                            .source(e.source)
                            .encoding(e.encoding));
                });

                file.validity.forEach(v -> {
                    f.addValidityItem(new FileValidity()
                            .source(v.source)
                            .valid(v.valid)
                            .wellFormed(v.wellFormed));
                });
            }));
        });

        var describeResponse = publicClient.describePreservationObject(
                DEFAULT_VAULT, object.externalId, null);

        assertPreservationObjectDefault(describeResponse, ingestId, object.externalId, object.version);
        assertThat(describeResponse.getFiles(), containsInAnyOrder(
                matchers.toArray(new ObjectFileMatcher[matchers.size()])
        ));
    }

    protected void assertIngestObject(Long ingestId, IngestObjectState state, TestObject object) {
        var batchObjectResponse = activeClient.retrieveBatchObject(ingestId, object.externalId);
        var batchObjectFilesResponse = activeClient.retrieveBatchObjectFiles(ingestId, object.externalId);

        assertThat(batchObjectResponse.getBatchObject(), is(ingestBatchObject(o -> o.ingestId(ingestId)
                .externalObjectId(object.externalId)
                .state(state)
                .reviewedBy(Set.of(IngestObjectState.PENDING_INGESTION, IngestObjectState.INGESTING, IngestObjectState.INGESTED,
                        IngestObjectState.PENDING_REJECTION, IngestObjectState.REJECTING, IngestObjectState.REJECTED).contains(state)
                        ? ClientUtil.FEDORA_USER : null))));

        var matchers = new ArrayList<ObjectFileMatcher>();

        object.files.forEach(file -> {
            matchers.add(objectFile(f -> f.filePath(file.path).sha256Digest(file.digest).fileSize(file.size)));
        });

        assertThat(batchObjectFilesResponse.getFiles(), containsInAnyOrder(
                matchers.toArray(new ObjectFileMatcher[matchers.size()])
        ));
    }

    protected void assertIngestObjectState(Long ingestId, TestObject object, IngestObjectState state) {
        var batchObjectResponse = activeClient.retrieveBatchObject(ingestId, object.externalId);
        assertEquals(state, batchObjectResponse.getBatchObject().getState());
    }

    protected void registerObjectWithFiles(Long ingestId, TestObject object) {
        var ingestObjectId = registerObject(ingestId, object.externalId);
        object.ingestObjectId = ingestObjectId;
        object.files.forEach(file -> {
            registerFile(ingestObjectId, file);
        });
    }

    protected Long registerObject(Long ingestId, String externalId) {
        return internalClient.registerIngestObject(new RegisterIngestObjectRequest()
                .ingestId(ingestId)
                .externalObjectId(externalId)
                .objectRootPath("/var/tmp/" + externalId)).getIngestObjectId();
    }

    protected void registerFile(Long ingestObjectId, TestFile file) {
        var ingestFileId = internalClient.registerIngestObjectFile(new RegisterIngestObjectFileRequest()
                .ingestObjectId(ingestObjectId)
                .filePath(file.path))
                .getIngestFileId();

        file.ingestFileId = ingestFileId;

        var request = new RegisterIngestObjectFileDetailsRequest()
                .ingestFileId(ingestFileId)
                .sha256Digest(file.digest)
                .fileSize(file.size);

        file.formats.forEach(format -> {
            request.addFormatsItem(new edu.wisc.library.sdg.preservation.manager.client.internal.model.FileFormat()
                    .formatRegistry(FormatRegistry.fromValue(format.registry.toString()))
                    .source(format.source)
                    .format(format.format));
        });

        file.encodings.forEach(e -> {
            request.addEncodingItem(new edu.wisc.library.sdg.preservation.manager.client.internal.model.FileEncoding()
                    .source(e.source)
                    .encoding(e.encoding));
        });

        file.validity.forEach(v -> {
            request.addValidityItem(new edu.wisc.library.sdg.preservation.manager.client.internal.model.FileValidity()
                    .source(v.source)
                    .valid(v.valid)
                    .wellFormed(v.wellFormed));
        });

        internalClient.registerIngestObjectFileDetails(request);
    }

    protected edu.wisc.library.sdg.preservation.manager.client.internal.model.Event recordIngestEvent(Long ingestId,
                                                                                                      String externalObjectId,
                                                                                                      EventType type,
                                                                                                      EventOutcome outcome,
                                                                                                      String username,
                                                                                                      List<LogEntry> logs) {
        var event = event(type, outcome, username, logs);
        internalClient.recordIngestEvent(new RecordIngestEventRequest()
                .ingestId(ingestId)
                .externalObjectId(externalObjectId)
                .event(event));
        return event;
    }

    protected edu.wisc.library.sdg.preservation.manager.client.internal.model.Event recordIngestEvent(Long ingestId,
                                                                                                      String externalObjectId,
                                                                                                      edu.wisc.library.sdg.preservation.manager.client.internal.model.Event event) {
        internalClient.recordIngestEvent(new RecordIngestEventRequest()
                .ingestId(ingestId)
                .externalObjectId(externalObjectId)
                .event(event));
        return event;
    }

    protected edu.wisc.library.sdg.preservation.manager.client.internal.model.Event recordPreservationEvent(String objectId,
                                                                                                            EventType type,
                                                                                                            EventOutcome outcome,
                                                                                                            List<LogEntry> logs) {
        var event = event(type, outcome, null, logs);
        internalClient.recordPreservationEvent(new RecordPreservationEventRequest()
                .objectId(objectId)
                .event(event));
        return event;
    }

    protected edu.wisc.library.sdg.preservation.manager.client.internal.model.Event recordPreservationEvent(String objectId,
                                                                                                            edu.wisc.library.sdg.preservation.manager.client.internal.model.Event event) {
        internalClient.recordPreservationEvent(new RecordPreservationEventRequest()
                .objectId(objectId)
                .event(event));
        return event;
    }

    protected edu.wisc.library.sdg.preservation.manager.client.internal.model.Event event(EventType type,
                                                                                          EventOutcome outcome,
                                                                                          String username,
                                                                                          List<LogEntry> logs) {
        var event = new edu.wisc.library.sdg.preservation.manager.client.internal.model.Event()
                .type(type)
                .outcome(outcome)
                .eventTimestamp(OffsetDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.MICROS))
                .username(username)
                .logs(logs == null ? Collections.emptyList() : logs);

        if (username == null) {
            event.setAgent(Agent.PRESERVATION_SERVICE_VERSION);
        }

        return event;
    }

    protected edu.wisc.library.sdg.preservation.manager.client.internal.model.Event receivedEvent(String bagName) {
        return event(EventType.RECEIVE_BAG, EventOutcome.SUCCESS, FEDORA_USER, List.of(logEntry(LogLevel.INFO, "Received SIP " + bagName)));
    }

    protected edu.wisc.library.sdg.preservation.manager.client.internal.model.Event batchApprovedEvent() {
        return event(EventType.REVIEW_BATCH, EventOutcome.APPROVED, FEDORA_USER, null);
    }

    protected edu.wisc.library.sdg.preservation.manager.client.internal.model.Event batchCompleteEvent() {
        return event(EventType.COMPLETE_BATCH_INGEST, EventOutcome.SUCCESS, null, null);
    }

    protected edu.wisc.library.sdg.preservation.manager.client.internal.model.Event objectCompleteEvent() {
        return event(EventType.COMPLETE_OBJ_INGEST, EventOutcome.SUCCESS, null, null);
    }

    protected edu.wisc.library.sdg.preservation.manager.client.internal.model.Event objectApprovedEvent() {
        return event(EventType.REVIEW_OBJ, EventOutcome.APPROVED, FEDORA_USER, null);
    }

    protected edu.wisc.library.sdg.preservation.manager.client.internal.model.Event metadataUpdatedEvent() {
        return event(EventType.UPDATE_OBJ_METADATA, EventOutcome.SUCCESS, null, null);
    }

    protected edu.wisc.library.sdg.preservation.manager.client.internal.model.Event objectWrittenSuccessEvent(TestObject object) {
        return event(EventType.WRITE_OBJ_LOCAL, EventOutcome.SUCCESS, null,
                List.of(logEntry(LogLevel.INFO, "Ingested object into local OCFL repo")));
    }

    protected edu.wisc.library.sdg.preservation.manager.client.internal.model.Event objectWrittenFailureEvent(TestObject object) {
        return event(EventType.WRITE_OBJ_LOCAL, EventOutcome.FAILURE, null,
                List.of(logEntry(LogLevel.ERROR, "Failted to ingest object into local OCFL repo")));
    }

    protected edu.wisc.library.sdg.preservation.manager.client.internal.model.Event objectCreatedEvent(TestObject object) {
        return event(EventType.CREATE_OBJ, EventOutcome.SUCCESS, null,
                List.of(logEntry(LogLevel.INFO, String.format("Created version %s", object.version))));
    }

    protected edu.wisc.library.sdg.preservation.manager.client.internal.model.Event objectUpdatedEvent(TestObject object) {
        return event(EventType.UPDATE_OBJ, EventOutcome.SUCCESS, null,
                List.of(logEntry(LogLevel.INFO, String.format("Created version %s", object.version))));
    }

    protected edu.wisc.library.sdg.preservation.manager.client.internal.model.Event replicatedEvent(TestObject object) {
        return event(EventType.REPLICATE_OBJ_VERSION, EventOutcome.SUCCESS, null,
                List.of(logEntry(LogLevel.INFO, String.format("Replicated OCFL version %s to %s",
                        object.persistenceVersion, edu.wisc.library.sdg.preservation.manager.db.model.DataStore.GLACIER))));
    }

    protected edu.wisc.library.sdg.preservation.manager.client.internal.model.Event replicatedEventFailed(TestObject object) {
        return event(EventType.REPLICATE_OBJ_VERSION, EventOutcome.FAILURE, null,
                List.of(logEntry(LogLevel.ERROR, String.format("Failed to replicated OCFL version %s to %s",
                        object.persistenceVersion, edu.wisc.library.sdg.preservation.manager.db.model.DataStore.GLACIER))));
    }

    protected edu.wisc.library.sdg.preservation.manager.client.internal.model.Event restoreSuccessEvent(TestObject object) {
        return event(EventType.RESTORE_OBJ_VERSION, EventOutcome.SUCCESS, null,
                List.of(logEntry(LogLevel.INFO, String.format("Restored OCFL version %s from %s",
                        object.persistenceVersion, edu.wisc.library.sdg.preservation.manager.db.model.DataStore.GLACIER))));
    }

    protected edu.wisc.library.sdg.preservation.manager.client.internal.model.Event restoreFailureEvent(TestObject object) {
        return event(EventType.RESTORE_OBJ_VERSION, EventOutcome.FAILURE, null,
                List.of(logEntry(LogLevel.ERROR, String.format("Failed to restore OCFL version %s from %s",
                        object.persistenceVersion, edu.wisc.library.sdg.preservation.manager.db.model.DataStore.GLACIER))));
    }

    protected edu.wisc.library.sdg.preservation.manager.client.internal.model.Event finalizeRestoreFailureEvent() {
        return event(EventType.RESTORE_OBJ_VERSION, EventOutcome.FAILURE, null,
                List.of(logEntry(LogLevel.ERROR, "Failed to finalize object restoration.")));
    }

    protected edu.wisc.library.sdg.preservation.manager.client.internal.model.Event deleteObjectEvent(TestObject object, String reason) {
        return event(EventType.DELETE_OBJ, EventOutcome.SUCCESS, SERVICE_ADMIN_1_USER,
                List.of(logEntry(LogLevel.INFO, reason)));
    }

    protected List<edu.wisc.library.sdg.preservation.manager.client.internal.model.Event> defaultObject1Events() {
        var events = new ArrayList<edu.wisc.library.sdg.preservation.manager.client.internal.model.Event>();
        events.add(metadataUpdatedEvent());
        events.add(objectCreatedEvent(defaultObject1));
        events.add(replicatedEvent(defaultObject1));
        events.add(metadataUpdatedEvent());
        events.add(objectUpdatedEvent(defaultObject1v2));
        events.add(replicatedEvent(defaultObject1v2));
        return events;
    }

    protected LogEntry logEntry(LogLevel level, String message) {
        return new LogEntry()
                .level(level)
                .message(message)
                .createdTimestamp(OffsetDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.MICROS));
    }

    protected void approveBatch(Long ingestId) {
        activeClient.approveIngestBatch(new ApproveIngestBatchRequest().ingestId(ingestId));
    }

    protected void updateBatchState(Long ingestId,
                                    edu.wisc.library.sdg.preservation.manager.client.internal.model.IngestBatchState state) {
        switch (state) {
            case ANALYZING -> {
                internalClient.batchStartAnalysis(new BatchStartAnalysisRequest().ingestId(ingestId));
            }
            case ANALYSIS_FAILED -> {
                internalClient.batchCompleteAnalysis(new BatchCompleteAnalysisRequest()
                        .ingestId(ingestId).outcome(Outcome.FAILURE));
            }
            case PENDING_REVIEW -> {
                internalClient.batchCompleteAnalysis(new BatchCompleteAnalysisRequest()
                        .ingestId(ingestId).outcome(Outcome.SUCCESS));
            }
            case INGESTING -> {
                internalClient.batchStartIngesting(new BatchStartIngestingRequest().ingestId(ingestId));
            }
            case INGEST_FAILED -> {
                internalClient.batchCompleteIngesting(new BatchCompleteIngestingRequest()
                        .ingestId(ingestId).outcome(Outcome.FAILURE));
            }
            case REPLICATING -> {
                internalClient.batchCompleteIngesting(new BatchCompleteIngestingRequest()
                        .ingestId(ingestId).outcome(Outcome.SUCCESS));
            }
            case REJECTING -> {
                internalClient.batchStartRejecting(new BatchStartRejectingRequest().ingestId(ingestId));
            }
            case REJECTED -> {
                internalClient.batchCompleteRejecting(new BatchCompleteRejectingRequest().ingestId(ingestId));
            }
            default -> {
                throw new IllegalStateException(String.format("Transitioning to state %s is not supported", state));
            }
        }
    }

    protected void updateObjectState(Long ingestId,
                                     String externalId,
                                     edu.wisc.library.sdg.preservation.manager.client.internal.model.IngestObjectState state) {
        switch (state) {
            case ANALYSIS_FAILED -> {
                internalClient.objectCompleteAnalysis(new ObjectCompleteAnalysisRequest()
                        .ingestId(ingestId)
                        .externalObjectId(externalId)
                        .outcome(Outcome.FAILURE));
            }
            case PENDING_REVIEW -> {
                internalClient.objectCompleteAnalysis(new ObjectCompleteAnalysisRequest()
                        .ingestId(ingestId)
                        .externalObjectId(externalId)
                        .outcome(Outcome.SUCCESS));
            }
            case INGESTING -> {
                internalClient.objectStartIngesting(new ObjectStartIngestingRequest()
                        .ingestId(ingestId)
                        .externalObjectId(externalId));
            }
            case INGESTED -> {
                internalClient.objectCompleteIngesting(new ObjectCompleteIngestingRequest()
                        .ingestId(ingestId)
                        .externalObjectId(externalId)
                        .outcome(ObjectCompleteIngestingRequest.OutcomeEnum.SUCCESS));
            }
            case INGEST_FAILED -> {
                internalClient.objectCompleteIngesting(new ObjectCompleteIngestingRequest()
                        .ingestId(ingestId)
                        .externalObjectId(externalId)
                        .outcome(ObjectCompleteIngestingRequest.OutcomeEnum.FAILURE));
            }
            case NO_CHANGE -> {
                internalClient.objectCompleteIngesting(new ObjectCompleteIngestingRequest()
                        .ingestId(ingestId)
                        .externalObjectId(externalId)
                        .outcome(ObjectCompleteIngestingRequest.OutcomeEnum.NO_CHANGE));
            }
            default -> {
                throw new IllegalStateException(String.format("Transitioning to state %s is not supported", state));
            }
        }
    }

    protected void updateObjectStates(Long ingestId,
                                      edu.wisc.library.sdg.preservation.manager.client.internal.model.IngestObjectState state,
                                      TestObject... objects) {
        for (var object : objects) {
            updateObjectState(ingestId, object.externalId, state);
        }
    }

    protected void createVersionAndFinalize(TestObject testObject) {
        var objectVersionId = internalClient.createObjectVersion(new CreateObjectVersionRequest()
                .ingestObjectId(testObject.ingestObjectId)
                .objectId(testObject.unprefixedInternalId())).getObjectVersionId();
        testObject.objectVersionId = objectVersionId;
        internalClient.finalizeObjectVersion(new FinalizeObjectVersionRequest()
                .objectVersionId(objectVersionId)
                .ingestObjectId(testObject.ingestObjectId)
                .persistenceVersion(testObject.persistenceVersion));
    }

    protected File bag(String name) {
        return new File("src/test/resources/itest/bags/" + name);
    }

    protected List<Long> retrieveObjects(boolean allVersions, TestObject... objects) {
        var request = new RetrieveObjectsRequest()
                .vault(DEFAULT_VAULT)
                .allVersions(allVersions);
        for (TestObject object : objects) {
            request.addExternalObjectIdsItem(object.externalId);
        }

        var requestId = serviceAdminUserClient.retrieveObjects(request).getRequestId();

        var jobIds = serviceAdminUserClient.describeRetrieveRequest(requestId)
                .getParts().stream()
                .map(RetrieveRequestPart::getJobId)
                .toList();

        processJobs();

        return jobIds;
    }

    protected File getPremisDocument(TestObject... objects) {
        var internal = objects[0].unprefixedInternalId();

        var versions = Arrays.asList(objects).stream().map(o -> o.version).toList();

        return internalClient.getPremisDocument(internal, versions);
    }

    protected void validateObject(TestObject object) {
        serviceAdminUserClient.validateObject(new ValidatePreservationObjectRequest()
                .vault(DEFAULT_VAULT)
                .externalObjectId(object.externalId)
                .contentFixityCheck(true));
    }

    protected void validateObjectRemote(TestObject object,
                                        List<String> versions,
                                        edu.wisc.library.sdg.preservation.manager.client.model.DataStore dataStore) {
        serviceAdminUserClient.validateObjectRemote(new ValidateObjectRemoteRequest()
                .vault(DEFAULT_VAULT)
                .externalObjectId(object.externalId)
                .versions(versions)
                .dataStore(dataStore));
    }

    protected void setStorageProblems(TestObject object,
                                      StorageProblemType objectProblem,
                                      Map<String, StorageProblemType> versionProblems) {
        var request = new SetObjectStorageProblemsRequest()
                .objectId(object.unprefixedInternalId())
                .dataStore(edu.wisc.library.sdg.preservation.manager.client.internal.model.DataStore.IBM_COS)
                .objectProblem(objectProblem);

        if (versionProblems != null) {
            versionProblems.forEach((version, problem) -> {
                request.addVersionProblemsItem(new VersionStorageProblem()
                        .persistenceVersion(version)
                        .problem(problem));
            });
        }

        internalClient.setObjectStorageProblems(request);
    }

    protected List<ObjectStorageProblem> getStorageProblems(TestObject object) {
        return publicClient.retrieveObjectStorageProblems(DEFAULT_VAULT, object.externalId).getProblems();
    }

    protected void recordJobLogs(Long jobId, LogEntry... jobLogEntries) {
        recordJobLogs(jobId, Arrays.asList(jobLogEntries));
    }

    protected void recordJobLogs(Long jobId, List<LogEntry> jobLogEntries) {
        internalClient.recordJobLogs(new RecordJobLogsRequest().jobId(jobId).logEntries(jobLogEntries));
    }

    protected void updateJobState(Long jobId, JobState state) {
        internalClient.updateJobState(new UpdateJobStateRequest().jobId(jobId).state(state));
        processJobs();
    }

    protected List<JobDetails> listJobs() {
        return serviceAdminUserClient.listJobs(TEST_ORG).getJobs();
    }

    protected void assertRetrieveObjectsRequest(RetrieveJob job, Long jobId, TestObject... objects) {
        assertEquals(jobId, job.getJobId());
        assertEquals(objects.length, job.getObjects().size());

        var map = new HashMap<String, ObjectInfo>();

        job.getObjects().forEach(object -> map.put(object.getInternalId() + object.getVersion(), object));

        for (var object : objects) {
            var info = map.get(object.prefixedInternalId() + object.version);
            assertNotNull(info, String.format("Expected %s to contain %s", job.getObjects(), object));
            assertEquals(object.externalId, info.getExternalId());
            assertEquals(object.prefixedInternalId(), info.getInternalId());
            assertEquals(object.version, info.getVersion());
            assertEquals(object.persistenceVersion, info.getPersistenceVersion());
        }
    }

    protected void assertJobState(Long jobId, String jobState) {
        var jobDesc = serviceAdminUserClient.describeJob(jobId);
        assertEquals(jobState, jobDesc.getJob().getState().getValue());
    }

    protected List<Event> retrieveEvents(TestObject object) {
        var events = publicClient.retrieveObjectEvents(DEFAULT_VAULT, object.externalId).getEvents();
        events.sort(Comparator.comparing(Event::getEventTimestamp));
        return events;
    }

    protected List<edu.wisc.library.sdg.preservation.manager.client.model.LogEntry> retrieveJobLogs(Long jobId) {
        return serviceAdminUserClient.retrieveJobLogs(jobId).getLogEntries();
    }

    protected void markReplicated(TestObject object, String sha256Digest) {
        internalClient.objectVersionReplicated(new ObjectVersionReplicatedRequest()
                .objectId(object.prefixedInternalId())
                .persistenceVersion(object.persistenceVersion)
                .dataStore(DataStore.GLACIER)
                .dataStoreKey(computeDataStoreKey(object))
                .sha256Digest(sha256Digest));
        processJobs();
    }

    protected String computeDataStoreKey(TestObject object) {
        return String.format("%s/%s-%s.zip", object.internalId, object.internalId, object.persistenceVersion);
    }

    protected Long verifyWorkerRestore(TestObject object) {
        for (var job : jobs) {
            if (job.getJobType() == JobType.RESTORE) {
                var restoreJob = job.getRestoreJob();

                if (object.prefixedInternalId().equals(restoreJob.getInternalObjectId())
                        && object.persistenceVersion.equals(restoreJob.getPersistenceVersion())
                        && DataStore.GLACIER == restoreJob.getSource()
                        && computeDataStoreKey(object).equals(restoreJob.getKey())
                        && DigestUtils.sha256Hex(object.prefixedInternalId()).equals(restoreJob.getSha256Digest())) {
                    return restoreJob.getJobId();
                }
            }
        }

        throw new RuntimeException(String.format("No restore job found for id=%s; version=%s; source=%s; key=%s; hash=%s. Actual jobs: %s",
                object.internalId, object.persistenceVersion, DataStore.GLACIER, computeDataStoreKey(object),
                DigestUtils.sha256Hex(object.prefixedInternalId()), jobs));
    }

    protected Long verifyWorkerFinalize(TestObject object) {
        for (var job : jobs) {
            if (job.getJobType() == JobType.FINALIZE_RESTORE) {
                var finalizeJob = job.getFinalizeRestoreJob();

                if (object.prefixedInternalId().equals(finalizeJob.getInternalObjectId())) {
                    return finalizeJob.getJobId();
                }
            }
        }

        throw new RuntimeException(String.format("No finalize restore job found for id=%s. Actual jobs: %s",
                object.internalId, jobs));
    }

    protected Long verifyValidateObject(TestObject object) {
        for (var job : jobs) {
            if (job.getJobType() == JobType.VALIDATE_LOCAL) {
                var validateJob = job.getValidateLocalJob();

                if (object.prefixedInternalId().equals(validateJob.getInternalObjectId())) {
                    return validateJob.getJobId();
                }
            }
        }

        throw new RuntimeException(String.format("No validate job found for id=%s. Actual jobs: %s",
                object.internalId, jobs));
    }

    protected Long verifyValidateObjectRemote(TestObject object, String persistenceVersion, DataStore dataStore) {
        for (var job : jobs) {
            if (job.getJobType() == JobType.VALIDATE_REMOTE) {
                var validateJob = job.getValidateRemoteJob();

                if (object.prefixedInternalId().equals(validateJob.getInternalObjectId())
                        && persistenceVersion.equals(validateJob.getPersistenceVersion())
                        && dataStore == validateJob.getDataStore()) {
                    return validateJob.getJobId();
                }
            }
        }

        throw new RuntimeException(String.format("No validate job found for id=%s. Actual jobs: %s",
                object.internalId, jobs));
    }

    protected Long verifyWorkerReplicate(TestObject object) {
        for (var job : jobs) {
            if (job.getJobType() == JobType.REPLICATE) {
                var replicateJob = job.getReplicateJob();

                if (object.externalId.equals(replicateJob.getExternalObjectId())
                        && object.prefixedInternalId().equals(replicateJob.getInternalObjectId())
                        && object.persistenceVersion.equals(replicateJob.getPersistenceVersion())
                        && DEFAULT_VAULT.equals(replicateJob.getVault())
                        && DataStore.IBM_COS == replicateJob.getSource()
                        && DataStore.GLACIER == replicateJob.getDestination()) {
                    return replicateJob.getJobId();
                }
            }
        }

        throw new RuntimeException(String.format("No replicate job found for extId=%s; id=%s; version=%s; vault=%s; src=%s; dst=%s. Actual jobs: %s",
                object.externalId, object.internalId, object.persistenceVersion, DEFAULT_VAULT, DataStore.IBM_COS, DataStore.GLACIER, jobs));
    }

    protected void verifyNoJobsOfType(JobType type) {
        for (var job : jobs) {
            assertNotEquals(type, job.getJobType(), String.format("Expected no jobs of type %s", type));
        }
    }

    protected <T> List<T> getJobsOfType(JobType type) {
        return (List<T>) jobs.stream().filter(j -> j.getJobType() == type).map(j -> {
            switch (type) {
                case RETRIEVE_OBJECTS -> {
                    return j.getRetrieveJob();
                }
                case REPLICATE -> {
                    return j.getReplicateJob();
                }
                case RESTORE -> {
                    return j.getRestoreJob();
                }
                case FINALIZE_RESTORE -> {
                    return j.getFinalizeRestoreJob();
                }
                case VALIDATE_LOCAL -> {
                    return j.getValidateLocalJob();
                }
                case PROCESS_BATCH -> {
                    return j.getProcessBatchJob();
                }
                default -> {
                    throw new RuntimeException("Unknown type: " + type);
                }
            }
        }).toList();
    }

    protected void assertJobLogs(Long jobId, List<LogEntry> logs) {
        var actualLogs = retrieveJobLogs(jobId);
        var expectedLogs = logs.stream().map(this::mapLog).toList();
        actualLogs.forEach(log -> log.setCreatedTimestamp(null));
        assertThat(actualLogs, containsInAnyOrder(expectedLogs.toArray()));
    }

    private edu.wisc.library.sdg.preservation.manager.client.model.LogEntry mapLog(LogEntry logEntry) {
        return new edu.wisc.library.sdg.preservation.manager.client.model.LogEntry()
                .level(edu.wisc.library.sdg.preservation.manager.client.model.LogLevel.fromValue(logEntry.getLevel().toString()))
                .message(logEntry.getMessage());
    }

    protected void assertBatchEvents(Long ingestId, List<edu.wisc.library.sdg.preservation.manager.client.internal.model.Event> events) {
        var actualEvents = activeClient.retrieveBatchEvents(ingestId).getEvents();
        var expectedEvents = events.stream().map(this::mapEvent).toList();
        actualEvents.forEach(event -> {
            event.setEventTimestamp(null);
            event.getLogs().forEach(log -> log.setCreatedTimestamp(null));
        });
        assertThat(actualEvents, containsInAnyOrder(expectedEvents.toArray()));
    }

    protected void assertBatchObjectEvents(Long ingestId, TestObject obj, List<edu.wisc.library.sdg.preservation.manager.client.internal.model.Event> events) {
        var actualEvents = activeClient.retrieveBatchObjectEvents(ingestId, obj.externalId).getEvents();
        var expectedEvents = events.stream().map(this::mapEvent).toList();
        actualEvents.forEach(event -> {
            event.setEventTimestamp(null);
            event.getLogs().forEach(log -> log.setCreatedTimestamp(null));
        });
        assertThat(actualEvents, containsInAnyOrder(expectedEvents.toArray()));
    }

    protected void assertBatchObjectEventsSubset(Long ingestId, TestObject obj, List<edu.wisc.library.sdg.preservation.manager.client.internal.model.Event> events) {
        var actualEvents = activeClient.retrieveBatchObjectEvents(ingestId, obj.externalId).getEvents();
        var expectedEvents = events.stream().map(this::mapEvent).toList();
        actualEvents.forEach(event -> {
            event.setEventTimestamp(null);
            event.getLogs().forEach(log -> log.setCreatedTimestamp(null));
        });
        assertThat(actualEvents, hasItems(expectedEvents.toArray(new Event[0])));
    }

    protected void assertPreservationEvents(TestObject obj, List<edu.wisc.library.sdg.preservation.manager.client.internal.model.Event> events) {
        var actualEvents = activeClient.retrieveObjectEvents(DEFAULT_VAULT, obj.externalId).getEvents();
        var expectedEvents = events.stream().map(this::mapEvent).toList();
        actualEvents.forEach(event -> {
            event.setEventTimestamp(null);
            event.getLogs().forEach(log -> log.setCreatedTimestamp(null));
        });
        assertThat(actualEvents, containsInAnyOrder(expectedEvents.toArray()));
    }

    protected void assertPreservationEventsSubset(TestObject obj, List<edu.wisc.library.sdg.preservation.manager.client.internal.model.Event> events) {
        var actualEvents = activeClient.retrieveObjectEvents(DEFAULT_VAULT, obj.externalId).getEvents();
        var expectedEvents = events.stream().map(this::mapEvent).toList();
        actualEvents.forEach(event -> {
            event.setEventTimestamp(null);
            event.getLogs().forEach(log -> log.setCreatedTimestamp(null));
        });
        assertThat(actualEvents, hasItems(expectedEvents.toArray(new Event[0])));
    }

    private Event mapEvent(edu.wisc.library.sdg.preservation.manager.client.internal.model.Event event) {
        var mapped = new Event()
                .type(edu.wisc.library.sdg.preservation.manager.client.model.EventType.fromValue(event.getType().toString()))
                .outcome(edu.wisc.library.sdg.preservation.manager.client.model.EventOutcome.fromValue(event.getOutcome().toString()))
                .username(event.getUsername())
                .agent(event.getAgent());

        if (event.getLogs() != null) {
            mapped.logs(event.getLogs().stream().map(this::mapLog).toList());
        } else {
            mapped.logs(Collections.emptyList());
        }

        return mapped;
    }

    protected Long setupBaseline() {
        var bagName = "single-valid.zip";

        // create v1
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
        replicate(ingestId, defaultObject2);

        updateBatchState(ingestId, INTERNAL_REPLICATING);

        // create v2
        ingestId = publicClient.ingestBag(DEFAULT_VAULT, bag(bagName))
                .getIngestId();

        updateBatchState(ingestId, INTERNAL_ANALYZING);
        registerObjectWithFiles(ingestId, defaultObject1v2);

        updateObjectStates(ingestId,
                INTERNAL_OBJ_PENDING_REVIEW,
                defaultObject1v2);
        updateBatchState(ingestId, INTERNAL_PENDING_REVIEW);

        assertIngestObject(ingestId, IngestObjectState.PENDING_REVIEW, defaultObject1v2);

        activeClient.approveIngestBatch(new ApproveIngestBatchRequest().ingestId(ingestId));

        updateBatchState(ingestId, INTERNAL_INGESTING);
        updateObjectStates(ingestId, INTERNAL_OBJ_INGESTING, defaultObject1v2);

        createVersionAndFinalize(defaultObject1v2);
        replicate(ingestId, defaultObject1v2);

        updateBatchState(ingestId, INTERNAL_REPLICATING);

        clearJobs();

        return ingestId;
    }

    protected void checkBatchReplications() {
        replicationMonitor.checkReplicationStatuses();
    }

    protected void processJobs() {
        var response = internalClient.pollForJob();
        while (response.getJobType() != null) {
            jobs.add(response);
            response = internalClient.pollForJob();
        }
    }

    protected void invalidateCacheAndProcessJobs() {
        jobBroker.invalidateCache();
        jobBroker.checkPendingJobs();
        processJobs();
    }

    protected void clearJobs() {
        jobs.clear();
    }

    protected void completeJob(Long jobId) {
        assertJobState(jobId, JobState.PENDING.getValue());

        updateJobState(jobId, JobState.EXECUTING);
        updateJobState(jobId, JobState.COMPLETE);

        assertJobState(jobId, JobState.COMPLETE.getValue());
    }

    protected void failJob(Long jobId) {
        assertJobState(jobId, JobState.PENDING.getValue());

        updateJobState(jobId, JobState.EXECUTING);
        updateJobState(jobId, JobState.FAILED);

        assertJobState(jobId, JobState.FAILED.getValue());
    }

    protected void replicate(Long ingestId, TestObject object) {
        updateObjectState(ingestId, object.externalId, INTERNAL_OBJ_INGESTED);
        processJobs();
        var jobId = verifyWorkerReplicate(object);
        updateJobState(jobId, JobState.EXECUTING);
        updateJobState(jobId, JobState.COMPLETE);
        var digest = DigestUtils.sha256Hex(object.prefixedInternalId());
        markReplicated(object, digest);
        assertIngestObjectState(ingestId, object, IngestObjectState.COMPLETE);
    }

    protected void replicateFailed(Long ingestId, TestObject object) {
        updateObjectState(ingestId, object.externalId, INTERNAL_OBJ_INGESTED);
        processJobs();
        var jobId = verifyWorkerReplicate(object);
        updateJobState(jobId, JobState.EXECUTING);
        updateJobState(jobId, JobState.FAILED);
        assertIngestObjectState(ingestId, object, IngestObjectState.REPLICATION_FAILED);
    }

    protected void completeBatch(Long ingestId) {
        updateBatchState(ingestId, INTERNAL_REPLICATING);
        checkBatchReplications();
    }

    protected void retryIngest(Long ingestId) {
        serviceAdminUserClient.retryBatchIngest(new BatchRetryIngestRequest().ingestId(ingestId));
    }

    protected void retryReplication(Long ingestId) {
        serviceAdminUserClient.retryBatchReplicate(new BatchRetryReplicateRequest().ingestId(ingestId));
    }

    protected void restoreObject(TestObject object, List<String> versions) {
        serviceAdminUserClient.restoreObject(new RestorePreservationObjectRequest()
                .vault(DEFAULT_VAULT)
                .externalObjectId(object.externalId)
                .versions(versions));
    }

    protected void replicateObject(TestObject object,
                                   List<String> versions,
                                   edu.wisc.library.sdg.preservation.manager.client.model.DataStore dataStore) {
        serviceAdminUserClient.replicateObject(new ReplicateObjectRequest()
                .vault(DEFAULT_VAULT)
                .externalObjectId(object.externalId)
                .versions(versions)
                .destination(dataStore));
    }

    protected void deleteObject(String externalObjectId, String reason) {
        serviceAdminUserClient.deleteObject(new DeleteObjectRequest()
                .vault(DEFAULT_VAULT)
                .externalObjectId(externalObjectId)
                .reason(reason));
    }

    protected DescribePreservationObjectResponse describePreservationObject(String externalId, Integer version) {
        return publicClient.describePreservationObject(
                DEFAULT_VAULT, externalId, version);
    }

    protected ObjectStorageProblem problemIbm(edu.wisc.library.sdg.preservation.manager.client.model.StorageProblemType type,
                                              String persistenceVersion) {

        return problem(type, persistenceVersion, edu.wisc.library.sdg.preservation.manager.client.model.DataStore.IBM_COS);
    }

    protected ObjectStorageProblem problem(edu.wisc.library.sdg.preservation.manager.client.model.StorageProblemType type,
                                           String persistenceVersion,
                                           edu.wisc.library.sdg.preservation.manager.client.model.DataStore dataStore) {
        return new ObjectStorageProblem()
                .dataStore(dataStore)
                .problem(type)
                .persistenceVersion(persistenceVersion);
    }

    protected void assertProblems(TestObject object, ObjectStorageProblem... problems) {
        var actualProblems = getStorageProblems(object);
        actualProblems.forEach(problem -> problem.setReportedTimestamp(null));
        assertThat(actualProblems, Matchers.containsInAnyOrder(problems));
    }

}
