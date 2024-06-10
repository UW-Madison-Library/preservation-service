package edu.wisc.library.sdg.preservation.manager.job;

import edu.wisc.library.sdg.preservation.manager.db.model.Job;
import edu.wisc.library.sdg.preservation.manager.db.model.JobState;
import edu.wisc.library.sdg.preservation.manager.db.model.JobType;
import edu.wisc.library.sdg.preservation.manager.job.handler.CleanupSipsJobHandler;
import edu.wisc.library.sdg.preservation.manager.job.handler.DeleteDipJobHandler;
import edu.wisc.library.sdg.preservation.manager.job.handler.FinalizeRestoreJobHandler;
import edu.wisc.library.sdg.preservation.manager.job.handler.JobHandler;
import edu.wisc.library.sdg.preservation.manager.job.handler.ProcessBatchJobHandler;
import edu.wisc.library.sdg.preservation.manager.job.handler.ReplicateJobHandler;
import edu.wisc.library.sdg.preservation.manager.job.handler.RestoreJobHandler;
import edu.wisc.library.sdg.preservation.manager.job.handler.RetrieveJobHandler;
import edu.wisc.library.sdg.preservation.manager.job.handler.ValidateLocalJobHandler;
import edu.wisc.library.sdg.preservation.manager.job.handler.ValidateRemoteJobHandler;
import edu.wisc.library.sdg.preservation.manager.service.IngestService;
import edu.wisc.library.sdg.preservation.manager.service.JobService;
import edu.wisc.library.sdg.preservation.manager.service.PreservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Executes job state transitions and farms jobs out to workers to process.
 */
@Component
public class JobManager {

    private static final Logger LOG = LoggerFactory.getLogger(JobManager.class);

    private final JobService jobService;
    private final JobBroker jobBroker;

    private final Map<JobType, JobHandler> jobHandlers;
    private final Set<JobType> enabledTypes;

    @Autowired
    public JobManager(JobService jobService,
                      JobBroker jobBroker,
                      PreservationService preservationService,
                      IngestService ingestService) {
        this.jobService = jobService;
        this.jobBroker = jobBroker;

        this.jobHandlers = Map.of(
                JobType.RETRIEVE_OBJECTS, new RetrieveJobHandler(jobService, preservationService),
                JobType.REPLICATE, new ReplicateJobHandler(jobService, preservationService, ingestService),
                JobType.RESTORE, new RestoreJobHandler(jobService, preservationService),
                JobType.FINALIZE_RESTORE, new FinalizeRestoreJobHandler(jobService, preservationService),
                JobType.VALIDATE_LOCAL, new ValidateLocalJobHandler(jobService, preservationService),
                JobType.VALIDATE_REMOTE, new ValidateRemoteJobHandler(jobService, preservationService),
                JobType.PROCESS_BATCH, new ProcessBatchJobHandler(jobService),
                JobType.DELETE_DIP, new DeleteDipJobHandler(jobService, preservationService),
                JobType.CLEANUP_SIPS, new CleanupSipsJobHandler(jobService)
        );

        this.enabledTypes = Arrays.stream(JobType.values()).collect(Collectors.toSet());
    }

    /**
     * Attempts to create a worker job for the specified job if it is ready to be processed. If it is not read, null
     * is returned.
     *
     * @param jobId the id of the job to process
     * @return the job request or null
     */
    public Object createWorkerJobWhenReady(Long jobId) {
        return createWorkerJobWhenReady(jobService.retrieveJob(jobId));
    }

    /**
     * Executes logic in response to a job failing
     *
     * @param jobId the id of the job to process
     */
    public void failJob(Long jobId) {
        var job = jobService.retrieveJob(jobId);
        LOG.debug("Failing job: {}", job);
        var handler = getJobHandler(job);
        handler.failJob(job);
        jobBroker.checkPendingJobs();
    }

    /**
     * Executes logic in response to a job completing
     *
     * @param jobId the id of the job to process
     */
    public void completeJob(Long jobId) {
        var job = jobService.retrieveJob(jobId);
        LOG.debug("Completing job: {}", job);
        var handler = getJobHandler(job);
        handler.completeJob(job);
        jobBroker.checkPendingJobs();
    }

    private Object createWorkerJobWhenReady(Job job) {
        if (job.getState() != JobState.PENDING) {
            LOG.debug("Skipping job <{}> because it is not in a PENDING state.", job.getJobId());
            return null;
        }

        if (!enabledTypes.contains(job.getType())) {
            LOG.debug("Skipping job <{}> because jobs of type {} are currently not allowed to be processed",
                    job.getJobId(), job.getType());
            return null;
        }

        try {
            var handler = getJobHandler(job);
            var dependencyState = checkDependencies(job);

            if (dependencyState == DependencyState.COMPLETE) {
                return handler.createWorkerJob(job);
            } else if (dependencyState == DependencyState.FAILED) {
                handler.failJob(job);
            } else {
                LOG.debug("Skipping job <{}> because its dependencies are not complete", job.getJobId());
            }
        } catch (RuntimeException e) {
            LOG.error("Failed to process job <{}>", job.getJobId(), e);
        }

        return null;
    }

    /**
     * Jobs of the specified types will no longer be processed.
     *
     * @param types the types of jobs to disable
     */
    public void disableJobsOfType(Collection<JobType> types) {
        LOG.debug("Jobs of the following types are no longer eligible to be processed: {}", types);
        enabledTypes.removeAll(types);
    }

    /**
     * Jobs of the specified types will be eligible to be processed.
     *
     * @param types the types of jobs to enable
     */
    public void enableJobsOfType(Collection<JobType> types) {
        LOG.debug("Jobs of the following types are now eligible to be processed: {}", types);
        enabledTypes.addAll(types);
    }

    /**
     * @return the job types that are currently enabled to be processed
     */
    public Set<JobType> getEnabledJobTypes() {
        return enabledTypes;
    }

    private JobHandler getJobHandler(Job job) {
        var handler = jobHandlers.get(job.getType());

        if (handler == null) {
            throw new IllegalStateException("Handler not found for job: " + job);
        }

        return handler;
    }

    private DependencyState checkDependencies(Job job) {
        var dependencies = jobService.getDependencyStates(job.getJobId());

        if (dependencies.isEmpty()) {
            return DependencyState.COMPLETE;
        }

        var failedCount = 0;
        var completeCount = 0;

        for (var dependency : dependencies) {
            if (dependency == JobState.FAILED) {
                failedCount++;
            } else if (dependency == JobState.COMPLETE) {
                completeCount++;
            }
        }

        LOG.debug("Job <{}> has {} dependencies. Complete: {}; Failed: {}; Pending: {}",
                job.getJobId(), dependencies.size(), completeCount, failedCount,
                dependencies.size() - (failedCount + completeCount));

        if (dependencies.size() == failedCount + completeCount) {
            if (failedCount > 0) {
                return DependencyState.FAILED;
            } else {
                return DependencyState.COMPLETE;
            }
        }

        return DependencyState.PENDING;
    }

    private enum DependencyState {
        PENDING, COMPLETE, FAILED
    }

}
