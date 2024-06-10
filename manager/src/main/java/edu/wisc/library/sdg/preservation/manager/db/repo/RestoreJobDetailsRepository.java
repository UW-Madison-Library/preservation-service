package edu.wisc.library.sdg.preservation.manager.db.repo;

import edu.wisc.library.sdg.preservation.manager.db.model.RestoreJobDetails;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RestoreJobDetailsRepository extends CrudRepository<RestoreJobDetails, Long> {

    Optional<RestoreJobDetails> findByJobId(Long jobId);

}
