package edu.wisc.library.sdg.preservation.manager.db.repo;

import edu.wisc.library.sdg.preservation.manager.db.model.ProcessBatchJobDetails;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProcessBatchJobDetailsRepository extends CrudRepository<ProcessBatchJobDetails, Long> {

    Optional<ProcessBatchJobDetails> findByJobId(Long jobId);

}
