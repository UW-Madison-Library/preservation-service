package edu.wisc.library.sdg.preservation.manager.job.handler;

import edu.wisc.library.sdg.preservation.common.util.Time;
import edu.wisc.library.sdg.preservation.common.util.UuidUtils;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.FinalizeRestoreJob;
import edu.wisc.library.sdg.preservation.manager.db.model.EventOutcome;
import edu.wisc.library.sdg.preservation.manager.db.model.EventType;
import edu.wisc.library.sdg.preservation.manager.db.model.FinalizeRestoreJobDetails;
import edu.wisc.library.sdg.preservation.manager.db.model.Job;
import edu.wisc.library.sdg.preservation.manager.db.model.PreservationEvent;
import edu.wisc.library.sdg.preservation.manager.service.JobService;
import edu.wisc.library.sdg.preservation.manager.service.PreservationService;
import edu.wisc.library.sdg.preservation.manager.util.Agent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FinalizeRestoreJobHandler extends BaseJobHandler {

    private static final Logger LOG = LoggerFactory.getLogger(FinalizeRestoreJobHandler.class);

    private final PreservationService preservationService;

    public FinalizeRestoreJobHandler(JobService jobService,
                                     PreservationService preservationService) {
        super(jobService);
        this.preservationService = preservationService;
    }

    @Override
    public Object createWorkerJob(Job job) {
        var details = jobService.getFinalizeJobDetails(job.getJobId());

        return new FinalizeRestoreJob()
                .jobId(job.getJobId())
                .internalObjectId(UuidUtils.withPrefix(details.getObjectId()));
    }

    /**
     * After finalizing a restore, create a job to validate the object
     *
     * @param job the job
     */
    @Override
    public void completeJob(Job job) {
        super.completeJob(job);

        var details = jobService.getFinalizeJobDetails(job.getJobId());

        jobService.createValidateLocalJob(details.getObjectId(), true, false);
    }

    @Override
    public void failJob(Job job) {
        super.failJob(job);

        var details = jobService.getFinalizeJobDetails(job.getJobId());

        recordEvent(details);
    }

    private void recordEvent(FinalizeRestoreJobDetails details) {
        try {
            var now = Time.now();
            var event = new PreservationEvent()
                    .setObjectId(details.getObjectId())
                    .setType(EventType.RESTORE_OBJ_VERSION)
                    .setOutcome(EventOutcome.FAILURE)
                    .setEventTimestamp(now)
                    .setAgent(Agent.PRESERVATION_SERVICE_VERSION)
                    .error("Failed to finalize object restoration.", now);

            preservationService.recordEvent(event);
        } catch (RuntimeException e) {
            LOG.error("Failed to record restoration event for job {}", details.getJobId(), e);
        }
    }

}
