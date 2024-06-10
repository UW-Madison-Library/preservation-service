package edu.wisc.library.sdg.preservation.manager.db.repo;

import edu.wisc.library.sdg.preservation.manager.db.model.DataStore;
import edu.wisc.library.sdg.preservation.manager.db.model.PreservationObjectVersionLocation;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PreservationObjectVersionLocationRepository extends CrudRepository<PreservationObjectVersionLocation, Long> {

    Optional<PreservationObjectVersionLocation> findByObjectIdAndPersistenceVersionAndDataStore(UUID objectId,
                                                                                                String persistenceVersion,
                                                                                                DataStore dataStore);

    List<PreservationObjectVersionLocation> findAllByObjectIdAndDataStore(UUID objectId,
                                                                          DataStore dataStore);

    List<PreservationObjectVersionLocation> findAllByObjectIdAndPersistenceVersion(UUID objectId,
                                                                                   String persistenceVersion);

    @Query("""
            SELECT l.* FROM preservation_object_version_location l
             INNER JOIN preservation_object o on l.object_id = o.object_id
             WHERE l.datastore = :dataStore
             AND o.state = 1
             AND (l.last_check_timestamp <= :lastChecked OR (l.last_check_timestamp IS NULL AND l.written_timestamp <= :lastChecked))
            """)
    List<PreservationObjectVersionLocation> findAllNeedingValidation(@Param("dataStore") DataStore dataStore,
                                                                     @Param("lastChecked") LocalDateTime lastChecked);

}
