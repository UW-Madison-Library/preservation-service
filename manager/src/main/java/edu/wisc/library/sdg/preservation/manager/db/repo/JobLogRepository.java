package edu.wisc.library.sdg.preservation.manager.db.repo;

import edu.wisc.library.sdg.preservation.manager.db.model.JobLog;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface JobLogRepository extends CrudRepository<JobLog, Long> {

    List<JobLog> findAllByJobId(Long jobId);

}
