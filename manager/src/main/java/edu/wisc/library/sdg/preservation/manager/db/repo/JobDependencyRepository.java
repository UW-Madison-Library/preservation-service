package edu.wisc.library.sdg.preservation.manager.db.repo;

import edu.wisc.library.sdg.preservation.manager.db.model.JobDependency;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface JobDependencyRepository extends CrudRepository<JobDependency, Long> {

    List<JobDependency> findByJobId(Long jobId);

}
