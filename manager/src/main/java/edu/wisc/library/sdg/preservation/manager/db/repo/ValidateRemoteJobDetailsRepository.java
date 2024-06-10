package edu.wisc.library.sdg.preservation.manager.db.repo;

import edu.wisc.library.sdg.preservation.manager.db.model.ValidateRemoteJobDetails;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ValidateRemoteJobDetailsRepository extends CrudRepository<ValidateRemoteJobDetails, Long> {

    Optional<ValidateRemoteJobDetails> findByJobId(Long jobId);

    /**
     * Identifies the ids of all of the locations that currently have an incomplete validation job
     *
     * @return list of location ids
     */
    @Query("""
           SELECT d.object_version_location_id FROM validate_remote_job_details d
            INNER JOIN job j ON d.job_id = j.job_id
            WHERE j.state IN (1, 2)
           """)
    List<Long> findAllLocationIdsForActiveJobs();

}
