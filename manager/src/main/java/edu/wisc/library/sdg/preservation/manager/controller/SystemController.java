package edu.wisc.library.sdg.preservation.manager.controller;

import edu.wisc.library.sdg.preservation.common.util.Time;
import edu.wisc.library.sdg.preservation.manager.auth.PreservationAuth;
import edu.wisc.library.sdg.preservation.manager.client.model.*;
import edu.wisc.library.sdg.preservation.manager.db.model.*;
import edu.wisc.library.sdg.preservation.manager.db.model.Event;
import edu.wisc.library.sdg.preservation.manager.db.model.EventOutcome;
import edu.wisc.library.sdg.preservation.manager.db.model.EventType;
import edu.wisc.library.sdg.preservation.manager.db.repo.EventRepo;
import edu.wisc.library.sdg.preservation.manager.db.repo.IngestEventRepository;
import edu.wisc.library.sdg.preservation.manager.db.repo.PreservationEventRepository;
import edu.wisc.library.sdg.preservation.manager.service.OrganizationService;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/system")
@Timed(histogram = true)
public class SystemController {

    private static final Logger LOG = LoggerFactory.getLogger(SystemController.class);

    private final PreservationAuth preservationAuth;
    private final OrganizationService organizationService;

    private final IngestEventRepository ingestEventRepository;
    private final PreservationEventRepository preservationEventRepository;

    @Autowired
    public SystemController(PreservationAuth preservationAuth,
                            OrganizationService organizationService,
                            IngestEventRepository ingestEventRepository,
                            PreservationEventRepository preservationEventRepository) {
        this.preservationAuth = preservationAuth;
        this.organizationService = organizationService;
        this.ingestEventRepository = ingestEventRepository;
        this.preservationEventRepository = preservationEventRepository;
    }

    @GetMapping("storage")
    public SystemStorageResponse getSystemStorage() {
        return preservationAuth.applyIfServiceAdmin(() -> {
            var totalObjects = organizationService.getObjectCount();
            var totalStorage = organizationService.getTotalStorageMb();

            var orgDetails = organizationService.getAllOrganizations().stream()
                    .map(org -> {
                        var count = organizationService.getObjectCountByOrg(org.getOrgName());
                        var storage = organizationService.getTotalStorageMbByOrg(org.getOrgName());

                        return new SystemStorageOrgDetails()
                                .orgName(org.getOrgName())
                                .displayName(org.getDisplayName())
                                .objectCount(count)
                                .storageMegabytes(storage);
                    }).toList();

            return new SystemStorageResponse()
                    .totalObjectCount(totalObjects)
                    .totalStorageMegabytes(totalStorage)
                    .organizations(orgDetails);
        });
    }

    @GetMapping("ingestEventCounts")
    public SystemEventCountsResponse getIngestEventCounts(
            @RequestParam Optional<List<EventType>> eventType,
            @RequestParam Optional<List<EventOutcome>> eventOutcome,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam Optional<String> organizationName) {

        return getCountsFor("ingest", ingestEventRepository, eventType, eventOutcome, startDate, endDate, organizationName);
    }

    @GetMapping("preservationEventCounts")
    public SystemEventCountsResponse getPreservationEventCounts(
            @RequestParam Optional<List<EventType>> eventType,
            @RequestParam Optional<List<EventOutcome>> eventOutcome,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam Optional<String> organizationName) {

        return getCountsFor("preservation", preservationEventRepository, eventType, eventOutcome, startDate, endDate, organizationName);
    }

    private SystemEventCountsResponse getCountsFor(
            String type,
            EventRepo<? extends Event> repo,
            Optional<List<EventType>> eventType,
            Optional<List<EventOutcome>> eventOutcome,
            String startDate,
            String endDate,
            Optional<String> organizationName) {

        LOG.debug("Retrieving {} event counts", type);
        return preservationAuth.applyIfServiceAdmin(() -> {
            LOG.debug("User is authorized");
            var eventTypes = eventType.orElse(List.of(EventType.values()));
            var eventDbTypes = eventTypes.stream().map(EventType::asShort).toList();
            var eventOutcomes = eventOutcome.orElse(List.of(EventOutcome.values()));
            var eventDbOutcomes = eventOutcomes.stream().map(EventOutcome::asShort).toList();
            var organization = organizationName.orElse("");
            var start = Time.startOf(LocalDate.parse(startDate));
            var end = Time.endOf(LocalDate.parse(endDate));

            LOG.debug("Retrieving counts of type {}, with outcomes {} at org {} between {} and {}", eventTypes, eventOutcomes, organization, start, end);

            var counts = organization.isBlank() ?
                    repo.countByTypeAndOutcomeAndTimestamp(eventDbTypes, eventDbOutcomes, start, end) :
                    repo.countByTypeAndOutcomeAndTimestampForOrg(eventDbTypes, eventDbOutcomes, start, end, organization);

            return compileCounts(counts, eventTypes, eventOutcomes);
        });
    }

    private SystemEventCountsResponse compileCounts(List<EventCount> counts, List<EventType> eventTypes, List<EventOutcome> eventOutcomes) {
        var result = new SystemEventCountsResponse();

        var grouped = counts.stream().collect(Collectors.groupingBy(EventCount::getType));

        for (var type : eventTypes) {
            var eventTypeCounts = new EventCounts();
            eventTypeCounts.setEventType(edu.wisc.library.sdg.preservation.manager.client.model.EventType.valueOf(type.name()));

            var eventOutcomeCounts = new EventOutcomeCount();
            eventTypeCounts.setEventCountsByOutcome(eventOutcomeCounts);

            var groupedType = grouped.get(type);
            if (groupedType != null) {
                for (var outcome : eventOutcomes) {
                    var outcomeCount = groupedType.stream().filter(it -> it.getOutcome().equals(outcome)).findFirst().orElse(null);
                    if (outcomeCount != null) {
                        setOutcomeCount(eventOutcomeCounts, outcome, (long) outcomeCount.getCount());
                    } else {
                        setOutcomeCount(eventOutcomeCounts, outcome, 0L);
                    }
                }
            } else {
                for (var outcome : eventOutcomes) {
                    setOutcomeCount(eventOutcomeCounts, outcome, 0L);
                }
            }

            result.addEventCountsItem(eventTypeCounts);
        }

        return result;
    }

    private void setOutcomeCount(EventOutcomeCount outcomeCounts, EventOutcome outcome, Long count) {
        switch (outcome) {
            case FAILURE -> outcomeCounts.setFailure(count);
            case SUCCESS -> outcomeCounts.setSuccess(count);
            case APPROVED -> outcomeCounts.setApproved(count);
            case REJECTED -> outcomeCounts.setRejected(count);
            case NOT_EXECUTED -> outcomeCounts.setNotExecuted(count);
            case SUCCESS_WITH_WARNINGS -> outcomeCounts.setSuccessWithWarnings(count);
        }
    }
}