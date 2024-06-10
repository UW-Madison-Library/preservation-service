package edu.wisc.library.sdg.preservation.manager.job.handler;

import edu.wisc.library.sdg.preservation.manager.db.model.Job;
import edu.wisc.library.sdg.preservation.manager.db.model.JobState;
import edu.wisc.library.sdg.preservation.manager.service.JobService;

/**
 * Base job handler that provides the common implementations of failJob and completeJob
 */
public abstract class BaseJobHandler implements JobHandler {

    protected final JobService jobService;

    public BaseJobHandler(JobService jobService) {
        this.jobService = jobService;
    }

    /**
     * Marks the job as failed
     *
     * @param job the job
     */
    @Override
    public void failJob(Job job) {
        jobService.updateJobState(job.getJobId(), JobState.FAILED);
    }

    /**
     * Marks the job as complete
     *
     * @param job the job
     */
    @Override
    public void completeJob(Job job) {
        jobService.updateJobState(job.getJobId(), JobState.COMPLETE);
    }
}
