package edu.wisc.library.sdg.preservation.manager.itest;

import edu.wisc.library.sdg.preservation.manager.client.internal.model.Event;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.EventOutcome;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.EventType;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.IngestBatchState;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.LogEntry;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.LogLevel;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RecordIngestEventRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RegisterIngestObjectRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.ApproveIngestBatchRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.BatchSearchResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.CreateVaultRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.IngestBatchObject;
import edu.wisc.library.sdg.preservation.manager.client.model.IngestBatchSummary;
import edu.wisc.library.sdg.preservation.manager.client.model.RejectIngestBatchRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.RetrieveBatchObjectsResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.VaultObject;
import edu.wisc.library.sdg.preservation.manager.controller.model.HasProblems;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BatchSearchITest extends ITestBase {

    private static final String VAULT_1 = "vault_1";
    private static final String VAULT_2 = "vault_2";
    private static final String VAULT_3 = "vault_3";

    @BeforeEach
    public void setup() {
        List.of(VAULT_1, VAULT_2, VAULT_3).forEach(vault -> {
            adminUserClient.createVault(new CreateVaultRequest()
                    .orgName(TEST_ORG)
                    .vault(new VaultObject().name(vault).description(vault)));
        });
    }

    @Test
    public void findNoBatchesWhenThereAreNone() {
        var results = searchBatches(null, null, 10, 0);
        assertNoResults(results);
    }

    @Test
    public void findBatchesWhenVaultsFilteredAndFitsInSinglePage() {
        var batch1 = createBatch(VAULT_1);
        var batch2 = createBatch(VAULT_2);
        var batch3 = createBatch(VAULT_2);
        var batch4 = createBatch(VAULT_3);

        transitions(batch2, IngestBatchState.ANALYZING, IngestBatchState.PENDING_REVIEW);
        transitions(batch3, IngestBatchState.ANALYZING);
        transitions(batch4, IngestBatchState.ANALYZING, IngestBatchState.PENDING_REVIEW,
                IngestBatchState.PENDING_INGESTION, IngestBatchState.INGESTING, IngestBatchState.REPLICATING);

        var results = searchBatches(List.of(VAULT_1, VAULT_2), null, 10, 0);

        assertResults(results, 3, 1, 0, batch1, batch2, batch3);
        assertEquals(3, results.getTotalResults());
        assertEquals(1, results.getTotalPages());
        assertEquals(0, results.getPage());
        assertEquals(3, results.getBatches().size());
    }

    @Test
    public void findBatchesWhenStatesFilteredAndFitsInSinglePage() {
        var batch1 = createBatch(VAULT_1);
        var batch2 = createBatch(VAULT_2);
        var batch3 = createBatch(VAULT_2);
        var batch4 = createBatch(VAULT_3);

        transitions(batch2, IngestBatchState.ANALYZING, IngestBatchState.PENDING_REVIEW);
        transitions(batch3, IngestBatchState.ANALYZING);
        transitions(batch4, IngestBatchState.ANALYZING, IngestBatchState.PENDING_REVIEW,
                IngestBatchState.PENDING_INGESTION, IngestBatchState.INGESTING, IngestBatchState.REPLICATING);

        var results = searchBatches(null, List.of(IngestBatchState.RECEIVED, IngestBatchState.REPLICATING), 10, 0);

        assertResults(results, 2, 1, 0, batch1, batch4);
    }

    @Test
    public void findBatchesWhenStatesAndVaultsFilteredAndFitsInSinglePage() {
        var batch1 = createBatch(VAULT_1);
        var batch2 = createBatch(VAULT_2);
        var batch3 = createBatch(VAULT_2);
        var batch4 = createBatch(VAULT_3);

        transitions(batch2, IngestBatchState.ANALYZING, IngestBatchState.PENDING_REVIEW);
        transitions(batch3, IngestBatchState.ANALYZING);
        transitions(batch4, IngestBatchState.ANALYZING, IngestBatchState.PENDING_REVIEW,
                IngestBatchState.PENDING_INGESTION, IngestBatchState.INGESTING, IngestBatchState.REPLICATING);

        var results = searchBatches(List.of(VAULT_3), List.of(IngestBatchState.RECEIVED, IngestBatchState.REPLICATING), 10, 0);

        assertResults(results, 1, 1, 0, batch4);
    }

    @Test
    public void findBatchesWhenMultiplePages() {
        var vault1Ids = createBatches(VAULT_1, 16);
        var vault2Ids = createBatches(VAULT_2, 16);
        var vault3Ids = createBatches(VAULT_3, 16);

        vault2Ids.addAll(vault3Ids);
        // results will be ordered newst to oldest
        vault2Ids.sort(Comparator.<Long>naturalOrder().reversed());
        var expectedArray = vault2Ids.toArray(new Long[] {});

        var results = searchBatches(List.of(VAULT_2, VAULT_3), null, 10, 0);
        assertResults(results, 32, 4, 0, Arrays.copyOfRange(expectedArray, 0, 10));

        results = searchBatches(List.of(VAULT_2, VAULT_3), null, 10, 1);
        assertResults(results, 32, 4, 1, Arrays.copyOfRange(expectedArray, 10, 20));

        results = searchBatches(List.of(VAULT_2, VAULT_3), null, 10, 2);
        assertResults(results, 32, 4, 2, Arrays.copyOfRange(expectedArray, 20, 30));

        results = searchBatches(List.of(VAULT_2, VAULT_3), null, 10, 3);
        assertResults(results, 32, 4, 3, Arrays.copyOfRange(expectedArray, 30, 32));
    }

    @Test
    public void findObjectsInBatch() {
        var batch1 = createBatch(VAULT_1);
        var batch2 = createBatch(VAULT_2);

        addObject(batch1, "obj1", false, false);
        addObject(batch1, "obj2", true, false);
        addObject(batch1, "obj3", true, false);
        addObject(batch1, "obj4", false, true);
        addObject(batch1, "obj5", true, true);
        addObject(batch1, "obj6", false, true);

        addObject(batch2, "obj21", false, false);
        addObject(batch2, "obj22", true, false);
        addObject(batch2, "obj23", false, true);

        var warnings = extractIds(searchObjects(batch1, HasProblems.WARNINGS));
        var errors = extractIds(searchObjects(batch1, HasProblems.ERRORS));
        var none = extractIds(searchObjects(batch1, HasProblems.NONE));
        var all = extractIds(searchObjects(batch1, null));

        assertThat(warnings, containsInAnyOrder("obj4", "obj6"));
        assertThat(errors, containsInAnyOrder("obj2", "obj3", "obj5"));
        assertThat(none, containsInAnyOrder("obj1"));
        assertThat(all, containsInAnyOrder("obj1", "obj2", "obj3", "obj4", "obj5", "obj6"));
    }

    private BatchSearchResponse searchBatches(List<String> vaults, List<IngestBatchState> states, int pageSize, int page) {
        return adminUserClient.searchBatches(TEST_ORG,
                vaults,
                states == null ? null :
                    states.stream().map(IngestBatchState::toString).collect(Collectors.toList()),
                pageSize,
                page);
    }

    private RetrieveBatchObjectsResponse searchObjects(Long ingestId, HasProblems hasProblems) {
        return adminUserClient.retrieveBatchObjects(ingestId, 100, 0,
                hasProblems == null ? null : hasProblems.toString());
    }

    private List<String> extractIds(RetrieveBatchObjectsResponse response) {
        return response.getBatchObjects().stream()
                .map(IngestBatchObject::getExternalObjectId)
                .collect(Collectors.toList());
    }

    private List<Long> createBatches(String vault, int count) {
        var ids = new ArrayList<Long>();
        for (int i = 0; i < count; i++) {
            ids.add(createBatch(vault));
        }
        return ids;
    }

    private long createBatch(String vault) {
        return adminUserClient.ingestBag(vault, bag("single-valid.zip")).getIngestId();
    }

    private void addObject(Long ingestId, String externalId, boolean hasErrors, boolean hasWarnings) {
        var id = internalClient.registerIngestObject(new RegisterIngestObjectRequest()
                .ingestId(ingestId)
                .externalObjectId(externalId)
                .objectRootPath("path")).getIngestObjectId();

        var outcome = EventOutcome.SUCCESS;
        var logs = new ArrayList<LogEntry>();

        if (hasErrors) {
            outcome = EventOutcome.FAILURE;
            logs.add(new LogEntry()
                    .level(LogLevel.ERROR)
                    .message("error")
                    .createdTimestamp(OffsetDateTime.now(ZoneOffset.UTC)));
        } else if (hasWarnings) {
            outcome = EventOutcome.SUCCESS_WITH_WARNINGS;
        }

        if (hasWarnings) {
            logs.add(new LogEntry()
                    .level(LogLevel.WARN)
                    .message("warning")
                    .createdTimestamp(OffsetDateTime.now(ZoneOffset.UTC)));
        }

        internalClient.recordIngestEvent(new RecordIngestEventRequest()
                .ingestId(ingestId)
                .externalObjectId(externalId)
                .event(new Event()
                        .type(EventType.IDENTIFY_FILE_FORMAT)
                        .outcome(outcome)
                        .eventTimestamp(OffsetDateTime.now(ZoneOffset.UTC))
                        .logs(logs)));
    }

    private void transitions(long ingestId, IngestBatchState... states) {
        for (IngestBatchState state : states) {
            if (state == IngestBatchState.PENDING_INGESTION) {
                adminUserClient.approveIngestBatch(new ApproveIngestBatchRequest().ingestId(ingestId));
            } else if (state == IngestBatchState.PENDING_REJECTION) {
                adminUserClient.rejectIngestBatch(new RejectIngestBatchRequest().ingestId(ingestId));
            } else {
                updateBatchState(ingestId,
                        edu.wisc.library.sdg.preservation.manager.client.internal.model.IngestBatchState.fromValue(state.getValue()));
            }
        }
    }

    private void assertResults(BatchSearchResponse results,
                               int totalResults,
                               int totalPages,
                               int currentPage,
                               Long... batchIds) {
        var actualIds = results.getBatches().stream()
                .map(IngestBatchSummary::getIngestId)
                .collect(Collectors.toList());

        assertThat(actualIds, containsInAnyOrder(batchIds));
        assertEquals(totalResults, results.getTotalResults());
        assertEquals(totalPages, results.getTotalPages());
        assertEquals(currentPage, results.getPage());
    }

    private void assertNoResults(BatchSearchResponse results) {
        assertEquals(0, results.getTotalResults());
        assertEquals(0, results.getTotalPages());
        assertEquals(0, results.getPage());
        assertEquals(0, results.getBatches().size());
    }

}
