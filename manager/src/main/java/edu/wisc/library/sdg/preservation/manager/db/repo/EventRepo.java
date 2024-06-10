package edu.wisc.library.sdg.preservation.manager.db.repo;

import edu.wisc.library.sdg.preservation.manager.db.model.Event;
import edu.wisc.library.sdg.preservation.manager.db.model.EventCount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.time.LocalDateTime;
import java.util.List;

@NoRepositoryBean
public interface EventRepo<E extends Event> extends CrudRepository<E, Long> {
    List<EventCount> countByTypeAndOutcomeAndTimestamp(List<Short> types, List<Short> outcomes, LocalDateTime start, LocalDateTime end);
    List<EventCount> countByTypeAndOutcomeAndTimestampForOrg(List<Short> types, List<Short> outcomes, LocalDateTime start, LocalDateTime end, String organization);
}
