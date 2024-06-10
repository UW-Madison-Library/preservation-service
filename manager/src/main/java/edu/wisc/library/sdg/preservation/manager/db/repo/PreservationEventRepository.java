package edu.wisc.library.sdg.preservation.manager.db.repo;

import edu.wisc.library.sdg.preservation.manager.db.model.EventCount;
import edu.wisc.library.sdg.preservation.manager.db.model.EventOutcome;
import edu.wisc.library.sdg.preservation.manager.db.model.EventType;
import edu.wisc.library.sdg.preservation.manager.db.model.PreservationEvent;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PreservationEventRepository extends EventRepo<PreservationEvent> {

    @Query("SELECT e.* FROM preservation_event e" +
            " JOIN preservation_object o ON e.object_id = o.object_id" +
            " WHERE o.vault = :vault AND o.external_object_id = :externalObjectId" +
            " ORDER BY e.event_timestamp")
    List<PreservationEvent> findAllByExternalObjectId(
            @Param("vault") String vault,
            @Param("externalObjectId") String externalObjectId);

    @Override
    @Query("select type, outcome, count(*) as count from preservation_event " +
            "where type in (:types) and outcome in (:outcomes) and event_timestamp >= :start and event_timestamp <= :end " +
            "group by type, outcome")
    List<EventCount> countByTypeAndOutcomeAndTimestamp(List<Short> types,
                                                       List<Short> outcomes,
                                                       LocalDateTime start,
                                                       LocalDateTime end);

    @Override
    @Query("select e.type as type, e.outcome as outcome, count(*) as count from preservation_event e " +
            "join preservation_object o on o.object_id = e.object_id " +
            "join vault v on v.name = o.vault " +
            "where v.org_name = :organization and type in (:types) and outcome in (:outcomes) and event_timestamp >= :start and event_timestamp <= :end " +
            "group by e.type, e.outcome")
    List<EventCount> countByTypeAndOutcomeAndTimestampForOrg(List<Short> types,
                                                             List<Short> outcomes,
                                                             LocalDateTime start,
                                                             LocalDateTime end,
                                                             String organization);
}
