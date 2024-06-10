package edu.wisc.library.sdg.preservation.manager.job;

import java.util.concurrent.TimeUnit;

/**
 * Responsible for keeping track of pending jobs and vending them when they're requested
 */
public interface JobBroker {

    /**
     * Indicates to the broker that it should look for more jobs. This is a suggestion and does not mean that it
     * happens immediately.
     *
     * This method should be called outside of any transaction that could result in the creation of new jobs or
     * existing jobs becoming eligible for processing. This ensures that the jobs are executed as soon as possible,
     * rather than waiting for the next scheduled check.
     */
    void checkPendingJobs();

    /**
     * Returns the id of a pending job or null if no pending jobs are found. If no jobs are immediately available, it will
     * wait for the specified timeout to see if any become available during that time.
     *
     * Jobs with incomplete dependencies are still eligible to be returned from this method. Dependencies must be checked
     * by the caller to ensure the job is actually ready to be processed.
     *
     * @param timeout how long to wait if there are currently no jobs available
     * @param unit the unit of the timeout
     * @return a pending job id or null
     */
    Long pollJob(long timeout, TimeUnit unit);

}
