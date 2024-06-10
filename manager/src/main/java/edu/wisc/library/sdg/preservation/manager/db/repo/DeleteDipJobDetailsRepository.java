package edu.wisc.library.sdg.preservation.manager.db.repo;

import edu.wisc.library.sdg.preservation.manager.db.model.DeleteDipJobDetails;
import edu.wisc.library.sdg.preservation.manager.db.model.ProcessBatchJobDetails;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface DeleteDipJobDetailsRepository extends CrudRepository<DeleteDipJobDetails, Long> {

    Optional<DeleteDipJobDetails> findByJobId(Long jobId);

}
