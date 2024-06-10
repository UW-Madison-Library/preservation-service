package edu.wisc.library.sdg.preservation.manager.db.repo;

import edu.wisc.library.sdg.preservation.manager.db.model.ValidateLocalJobDetails;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ValidateLocalJobDetailsRepository extends CrudRepository<ValidateLocalJobDetails, Long> {

    Optional<ValidateLocalJobDetails> findByJobId(Long jobId);

    /**
     * Identifies the ids of all of the objects that currently have an incomplete validation job
     *
     * @param contentFixityCheck filter for jobs with/without content fixity checks
     * @return list of object ids
     */
    @Query("""
           SELECT d.object_id FROM validate_local_job_details d
            INNER JOIN job j ON d.job_id = j.job_id
            WHERE j.state IN (1, 2) AND d.content_fixity_check = :contentFixityCheck
           """)
    List<UUID> findAllObjectIdsForActiveJobs(Boolean contentFixityCheck);

}
