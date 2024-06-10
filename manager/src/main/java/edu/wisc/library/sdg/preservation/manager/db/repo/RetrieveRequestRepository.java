package edu.wisc.library.sdg.preservation.manager.db.repo;

import edu.wisc.library.sdg.preservation.manager.db.model.RetrieveRequest;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface RetrieveRequestRepository extends CrudRepository<RetrieveRequest, Long> {

    /**
     * Finds all of the retrieve requests that are not deleted and created at or before the specified date
     *
     * @param created the cutoff date
     * @return list of expired ids
     */
    @Query("""
           SELECT retrieve_request_id FROM retrieve_request
            WHERE NOT deleted
            AND created_timestamp <= :created
           """)
    List<Long> findAllExpiredAndNotDeleted(@Param("created") LocalDateTime created);

    @Query("""
           SELECT v.org_name FROM retrieve_request r
            INNER JOIN vault v
            ON r.vault = v.name
            WHERE r.retrieve_request_id = :requestId
           """)
    String findOrgNameForRequest(@Param("requestId") Long requestId);

}
