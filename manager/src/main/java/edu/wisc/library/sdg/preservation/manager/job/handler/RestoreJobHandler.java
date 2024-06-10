package edu.wisc.library.sdg.preservation.manager.job.handler;

import edu.wisc.library.sdg.preservation.common.util.Time;
import edu.wisc.library.sdg.preservation.common.util.UuidUtils;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.DataStore;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RestoreJob;
import edu.wisc.library.sdg.preservation.manager.db.model.EventOutcome;
import edu.wisc.library.sdg.preservation.manager.db.model.EventType;
import edu.wisc.library.sdg.preservation.manager.db.model.Job;
import edu.wisc.library.sdg.preservation.manager.db.model.PreservationEvent;
import edu.wisc.library.sdg.preservation.manager.service.JobService;
import edu.wisc.library.sdg.preservation.manager.service.PreservationService;
import edu.wisc.library.sdg.preservation.manager.util.Agent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestoreJobHandler extends BaseJobHandler {

    private static final Logger LOG = LoggerFactory.getLogger(RestoreJobHandler.class);

    private final PreservationService preservationService;

    public RestoreJobHandler(JobService jobService,
                               PreservationService preservationService) {
        super(jobService);
        this.preservationService = preservationService;
    }

    @Override
    public Object createWorkerJob(Job job) {
        var details = jobService.getRestoreJobDetails(job.getJobId());
        var location = preservationService.getObjectVersionLocation(details.getObjectVersionLocationId());

        return new RestoreJob()
                .jobId(job.getJobId())
                .internalObjectId(UuidUtils.withPrefix(location.getObjectId()))
                .persistenceVersion(location.getPersistenceVersion())
                .source(DataStore.fromValue(location.getDataStore().toString()))
                .key(location.getDataStoreKey())
                .sha256Digest(location.getSha256Digest());
    }

    @Override
    public void completeJob(Job job) {
        super.completeJob(job);

        recordEvent(job, EventOutcome.SUCCESS);
    }

    @Override
    public void failJob(Job job) {
        super.failJob(job);

        recordEvent(job, EventOutcome.FAILURE);
    }

    private void recordEvent(Job job, EventOutcome outcome) {
        try {
            var details = jobService.getRestoreJobDetails(job.getJobId());
            var location = preservationService.getObjectVersionLocation(details.getObjectVersionLocationId());

            var now = Time.now();
            var event = new PreservationEvent()
                    .setObjectId(location.getObjectId())
                    .setType(EventType.RESTORE_OBJ_VERSION)
                    .setOutcome(outcome)
                    .setAgent(Agent.PRESERVATION_SERVICE_VERSION)
                    .setEventTimestamp(now);

            if (outcome == EventOutcome.SUCCESS) {
                event.info(String.format("Restored OCFL version %s from %s",
                        location.getPersistenceVersion(), location.getDataStore()), now);
            } else {
                event.error(String.format("Failed to restore OCFL version %s from %s",
                        location.getPersistenceVersion(), location.getDataStore()), now);
            }

            preservationService.recordEvent(event);
        } catch (RuntimeException e) {
            LOG.error("Failed to record restoration event for job {}", job.getJobId(), e);
        }
    }

}
