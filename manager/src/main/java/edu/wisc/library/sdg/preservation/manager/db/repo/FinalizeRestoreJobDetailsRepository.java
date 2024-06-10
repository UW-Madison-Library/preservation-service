package edu.wisc.library.sdg.preservation.manager.db.repo;

import edu.wisc.library.sdg.preservation.manager.db.model.FinalizeRestoreJobDetails;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface FinalizeRestoreJobDetailsRepository extends CrudRepository<FinalizeRestoreJobDetails, Long> {

    Optional<FinalizeRestoreJobDetails> findByJobId(Long jobId);

}
