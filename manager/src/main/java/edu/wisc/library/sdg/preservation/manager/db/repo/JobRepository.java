package edu.wisc.library.sdg.preservation.manager.db.repo;

import edu.wisc.library.sdg.preservation.manager.db.mapper.JobCountMapper;
import edu.wisc.library.sdg.preservation.manager.db.model.Job;
import edu.wisc.library.sdg.preservation.manager.db.model.JobCount;
import edu.wisc.library.sdg.preservation.manager.db.model.JobState;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

public interface JobRepository extends CrudRepository<Job, Long> {

    List<Job> findAllByOrgName(String orgName);

    List<Job> findAllByState(JobState state);

    /**
     * Returns ids of all of the PENDING jobs that have a next_attempt_timestamp before now.
     * Now must be specified due to annoying db timezone issues.
     *
     * @param now the current timestamp, in utc
     * @return pending job ids
     */
    @Query("SELECT job_id FROM job" +
            " WHERE state = 1" +
            " AND background = :background" +
            " AND (next_attempt_timestamp IS NULL OR next_attempt_timestamp <= :now)")
    List<Long> findAllJobIdsPendingExecution(LocalDateTime now, boolean background);

    @Query("SELECT j.* FROM job j" +
            " INNER JOIN job_dependency d ON j.job_id = d.depends_on_job_id" +
            " WHERE d.job_id = :jobId")
    List<Job> findAllDependenciesForJob(@Param("jobId") Long jobId);

    @Query("SELECT j.state FROM job j" +
            " INNER JOIN job_dependency d ON j.job_id = d.depends_on_job_id" +
            " WHERE d.job_id = :jobId")
    List<JobState> findAllDependencyStatesForJob(@Param("jobId") Long jobId);

    @Query("""
           SELECT COUNT(*) FROM job j
            INNER JOIN replicate_job_details d ON j.job_id = d.job_id
            WHERE d.ingest_id = :ingestId
            AND j.state IN (1, 2)
           """)
    long countIncompleteIngestReplicationJobs(@Param("ingestId") Long ingestId);

    @Query(value = "SELECT type, COUNT(*) FROM job" +
            " WHERE state = :state" +
            " GROUP BY type", rowMapperClass = JobCountMapper.class)
    List<JobCount> countJobsInStateByType(@Param("state") JobState jobState);

    @Query(value = "SELECT type, COUNT(*) FROM job" +
            " WHERE state = :state" +
            " AND updated_timestamp < :dateTime" +
            " GROUP BY type", rowMapperClass = JobCountMapper.class)
    List<JobCount> countJobsInStateAndUpdatedTimestampBeforeByType(@Param("state") JobState jobState, @Param("dateTime") OffsetDateTime updatedTimestamp);


}
