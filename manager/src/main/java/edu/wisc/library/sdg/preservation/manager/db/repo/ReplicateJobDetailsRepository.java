package edu.wisc.library.sdg.preservation.manager.db.repo;

import edu.wisc.library.sdg.preservation.manager.db.model.ReplicateJobDetails;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ReplicateJobDetailsRepository extends CrudRepository<ReplicateJobDetails, Long> {

    Optional<ReplicateJobDetails> findByJobId(Long jobId);

}
