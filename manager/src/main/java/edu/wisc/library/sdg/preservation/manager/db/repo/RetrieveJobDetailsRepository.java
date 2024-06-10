package edu.wisc.library.sdg.preservation.manager.db.repo;

import edu.wisc.library.sdg.preservation.manager.db.model.RetrieveJobDetails;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RetrieveJobDetailsRepository extends CrudRepository<RetrieveJobDetails, Long> {

    List<RetrieveJobDetails> findAllByJobId(Long jobId);

}
