package edu.wisc.library.sdg.preservation.manager.db.repo;

import edu.wisc.library.sdg.preservation.manager.db.mapper.RetrieveRequestJobCompositeMapper;
import edu.wisc.library.sdg.preservation.manager.db.model.RetrieveRequestJob;
import edu.wisc.library.sdg.preservation.manager.db.model.RetrieveRequestJobComposite;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RetrieveRequestJobRepository extends CrudRepository<RetrieveRequestJob, Long> {

    @Query(value = """
           SELECT
            r.retrieve_request_job_id,
            r.retrieve_request_id,
            r.job_id,
            r.last_downloaded_timestamp,
            j.state
            FROM retrieve_request_job r INNER JOIN job j ON r.job_id = j.job_id
            WHERE r.retrieve_request_id = :requestId
           """, rowMapperClass = RetrieveRequestJobCompositeMapper.class)
    List<RetrieveRequestJobComposite> findAllByRetrieveRequestId(@Param("requestId") Long requestId);

    Optional<RetrieveRequestJob> findByJobId(Long jobId);

}
