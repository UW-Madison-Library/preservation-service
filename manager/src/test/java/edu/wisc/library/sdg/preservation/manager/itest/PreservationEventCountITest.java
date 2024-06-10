package edu.wisc.library.sdg.preservation.manager.itest;

import edu.wisc.library.sdg.preservation.common.exception.AccessDeniedException;
import edu.wisc.library.sdg.preservation.manager.client.model.EventCounts;
import edu.wisc.library.sdg.preservation.manager.client.model.EventOutcome;
import edu.wisc.library.sdg.preservation.manager.client.model.EventType;
import edu.wisc.library.sdg.preservation.manager.client.model.SystemEventCountsResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

public class PreservationEventCountITest extends ITestBase {

    @BeforeEach
    public void setup() {
        setupBaseline();
    }

    private void setupEventsFor(edu.wisc.library.sdg.preservation.manager.client.internal.model.EventType type,
                               edu.wisc.library.sdg.preservation.manager.client.internal.model.EventOutcome outcome) {

        setupEventsFor(type, outcome, OffsetDateTime.now());
    }

    private void setupEventsFor(edu.wisc.library.sdg.preservation.manager.client.internal.model.EventType type,
                                edu.wisc.library.sdg.preservation.manager.client.internal.model.EventOutcome outcome,
                                OffsetDateTime timestamp) {

        var event = new edu.wisc.library.sdg.preservation.manager.client.internal.model.Event()
                .type(type)
                .outcome(outcome)
                .eventTimestamp(timestamp);
        recordPreservationEvent(defaultObject1.prefixedInternalId(), event);
    }

    private SystemEventCountsResponse getResponse() {
        return getResponse(LocalDate.now(), LocalDate.now(), null, null, null);
    }

    private SystemEventCountsResponse getResponse(List<EventType> types, List<EventOutcome> outcomes) {
        return getResponse(LocalDate.now(), LocalDate.now(), types, outcomes, null);
    }

    private SystemEventCountsResponse getResponse(LocalDate start, LocalDate end, List<EventType> types, List<EventOutcome> outcomes, String org) {
        return serviceAdminUserClient.getPreservationEventCounts(start, end, types, outcomes, org);
    }

    private EventCounts getCountsForType(SystemEventCountsResponse response, EventType type) {
        return response.getEventCounts().stream().filter(eventCounts -> eventCounts.getEventType().equals(type)).findFirst().orElse(null);
    }

    private void assertCountsFor(EventCounts eventCounts, Long success, Long failure, Long warnings, Long approved, Long rejected, Long notExecuted) {
        var outcomeCounts = eventCounts.getEventCountsByOutcome();

        assertAll("Asserting counts of type " + eventCounts.getEventType(),
                () -> assertThat(outcomeCounts.getSuccess(), is(success)),
                () -> assertThat(outcomeCounts.getFailure(), is(failure)),
                () -> assertThat(outcomeCounts.getSuccessWithWarnings(), is(warnings)),
                () -> assertThat(outcomeCounts.getApproved(), is(approved)),
                () -> assertThat(outcomeCounts.getRejected(), is(rejected)),
                () -> assertThat(outcomeCounts.getNotExecuted(), is(notExecuted))
        );
    }

    @Test
    public void regularUserIsDeniedAccess() {
        assertThrows(AccessDeniedException.class, () ->
                regularUserClient.getPreservationEventCounts(LocalDate.now().minusDays(1),
                        LocalDate.now(), null, null, null));
    }

    @Test
    public void adminUserIsDeniedAccess() {
        assertThrows(AccessDeniedException.class, () ->
                adminUserClient.getPreservationEventCounts(LocalDate.now().minusDays(1),
                        LocalDate.now(), null, null, null));
    }

    @Test
    public void serviceAdminUserIsAllowed() {
        assertDoesNotThrow(() ->
                serviceAdminUserClient.getPreservationEventCounts(LocalDate.now().minusDays(1),
                LocalDate.now(), null, null, null));
    }

    @Test
    public void returnsSystemEventCountsResponse() {
        assertThat(getResponse(), instanceOf(SystemEventCountsResponse.class));
    }

    @Test
    public void returnsExpectedCounts() {
        setupEventsFor(edu.wisc.library.sdg.preservation.manager.client.internal.model.EventType.RECEIVE_BAG,
                edu.wisc.library.sdg.preservation.manager.client.internal.model.EventOutcome.SUCCESS);
        var counts = getResponse(List.of(EventType.RECEIVE_BAG), List.of(EventOutcome.SUCCESS, EventOutcome.FAILURE));

        var receiveBagCounts = getCountsForType(counts, EventType.RECEIVE_BAG);

        assertCountsFor(receiveBagCounts, 1L, 0L, null, null, null, null);
    }

    @Test
    public void dateFiltersAreInclusive() {
        var eventTime = LocalDateTime.now().minusDays(3);
        var dayBefore = LocalDate.now().minusDays(4);
        var dayOf = LocalDate.now().minusDays(3);
        var dayAfter = LocalDate.now().minusDays(2);
        var eventTypes = List.of(EventType.CREATE_OBJ);
        var eventOutcomes = List.of(EventOutcome.SUCCESS);

        setupEventsFor(
                edu.wisc.library.sdg.preservation.manager.client.internal.model.EventType.CREATE_OBJ,
                edu.wisc.library.sdg.preservation.manager.client.internal.model.EventOutcome.SUCCESS,
                OffsetDateTime.of(eventTime, ZoneOffset.systemDefault().getRules().getOffset(eventTime))
        );

        assertAll(() -> {
            var wideResult = getResponse(dayBefore, dayAfter, eventTypes, eventOutcomes, null);
            var eventCounts = wideResult.getEventCounts().get(0);
            assertCountsFor(eventCounts, 1L, null, null, null, null, null);

            var startBorderResult = getResponse(dayOf, dayAfter, eventTypes, eventOutcomes, null);
            eventCounts = startBorderResult.getEventCounts().get(0);
            assertCountsFor(eventCounts, 1L, null, null, null, null, null);

            var endBorderResult = getResponse(dayBefore, dayOf, eventTypes, eventOutcomes, null);
            eventCounts = endBorderResult.getEventCounts().get(0);
            assertCountsFor(eventCounts, 1L, null, null, null, null, null);

            var outsideResult = getResponse(dayAfter, dayAfter, eventTypes, eventOutcomes, null);
            eventCounts = outsideResult.getEventCounts().get(0);
            assertCountsFor(eventCounts, 0L, null, null, null, null, null);
        });
    }
}
