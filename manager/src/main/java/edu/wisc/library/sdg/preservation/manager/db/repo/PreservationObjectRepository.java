package edu.wisc.library.sdg.preservation.manager.db.repo;

import edu.wisc.library.sdg.preservation.manager.db.model.PreservationObject;
import edu.wisc.library.sdg.preservation.manager.db.model.PreservationObjectState;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface PreservationObjectRepository extends CrudRepository<PreservationObject, UUID> {

    Optional<PreservationObject> findByVaultAndExternalObjectId(String vault, String externalObjectId);

    Stream<PreservationObject> findAllByVault(String vault);

    @Query("SELECT v.org_name FROM vault v" +
            " INNER JOIN preservation_object o ON v.name = o.vault" +
            " WHERE o.object_id = :objectId")
    Optional<String> lookupOrgNameByObjectId(@Param("objectId") UUID objectId);

    @Query("""
            SELECT object_id FROM preservation_object
             WHERE (last_deep_check_timestamp <= :lastChecked
               OR (last_deep_check_timestamp IS NULL AND created_timestamp <= :lastChecked))
             AND state = 1
            """)
    List<UUID> findAllNeedingDeepValidation(@Param("lastChecked") LocalDateTime lastChecked);

    @Query("""
            SELECT object_id FROM preservation_object
             WHERE ((last_shallow_check_timestamp <= :lastChecked AND last_deep_check_timestamp <= :lastChecked) 
               OR (last_shallow_check_timestamp IS NULL AND created_timestamp <= :lastChecked))
             AND state = 1
            """)
    List<UUID> findAllNeedingShallowValidation(@Param("lastChecked") LocalDateTime lastChecked);

    @Query("""
           SELECT DISTINCT o.external_object_id FROM preservation_object o
            INNER JOIN storage_problem p ON o.object_id = p.object_id
            WHERE o.vault = :vault
            AND p.problem != 3
            AND o.state = 1
            ORDER BY o.external_object_id
           """)
    List<String> findAllExternalIdsByVaultWithStorageProblems(@Param("vault") String vault);

}
