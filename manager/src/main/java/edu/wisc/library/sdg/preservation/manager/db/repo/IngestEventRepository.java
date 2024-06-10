package edu.wisc.library.sdg.preservation.manager.db.repo;

import edu.wisc.library.sdg.preservation.manager.db.model.EventCount;
import edu.wisc.library.sdg.preservation.manager.db.model.EventOutcome;
import edu.wisc.library.sdg.preservation.manager.db.model.EventType;
import edu.wisc.library.sdg.preservation.manager.db.model.IngestEvent;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface IngestEventRepository extends EventRepo<IngestEvent> {

    List<IngestEvent> findAllByIngestIdAndExternalObjectIdIsNull(Long ingestId);

    List<IngestEvent> findAllByIngestIdAndExternalObjectIdOrderByEventTimestamp(Long ingestId, String externalObjectId);

    @Override
    @Query("select type, outcome, count(*) as count from ingest_event " +
            "where type in (:types) and outcome in (:outcomes) and event_timestamp >= :start and event_timestamp <= :end " +
            "group by type, outcome")
    List<EventCount> countByTypeAndOutcomeAndTimestamp(List<Short> types,
                                                       List<Short> outcomes,
                                                       LocalDateTime start,
                                                       LocalDateTime end);

    @Override
    @Query("select e.type as type, e.outcome as outcome, count(*) as count from ingest_event e " +
            "join ingest_batch b on b.ingest_id = e.ingest_id " +
            "where b.org_name = :organization and type in (:types) and outcome in (:outcomes) and event_timestamp >= :start and event_timestamp <= :end " +
            "group by type, outcome")
    List<EventCount> countByTypeAndOutcomeAndTimestampForOrg(List<Short> types,
                                                             List<Short> outcomes,
                                                             LocalDateTime start,
                                                             LocalDateTime end,
                                                             String organization);
}
