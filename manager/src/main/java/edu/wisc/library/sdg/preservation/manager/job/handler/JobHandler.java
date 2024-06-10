package edu.wisc.library.sdg.preservation.manager.job.handler;

import edu.wisc.library.sdg.preservation.manager.db.model.Job;

/**
 * Handler for acting on a job
 */
public interface JobHandler {

    /**
     * Called when a job is PENDING and ready to be processed. Implementations should create the appropriate request
     * object for the job as defined in the pollForJob internal API.
     *
     * @param job the job
     * @return the job request object
     */
    // FIXME have to use Object due to lack of polymorphism https://github.com/OpenAPITools/openapi-generator/issues/10514
    Object createWorkerJob(Job job);

    /**
     * Called when a job has FAILED. At the minimum, implementations should update the state of the job in the DB.
     *
     * @param job the job
     */
    void failJob(Job job);

    /**
     * Called when a job is COMPLETE. At the minimum, implementations should update the state of the job in the DB.
     *
     * @param job the job
     */
    void completeJob(Job job);

}
