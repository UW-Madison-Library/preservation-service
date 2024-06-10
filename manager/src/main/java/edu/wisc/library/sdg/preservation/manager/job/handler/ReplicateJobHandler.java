package edu.wisc.library.sdg.preservation.manager.job.handler;

import edu.wisc.library.sdg.preservation.common.util.Time;
import edu.wisc.library.sdg.preservation.common.util.UuidUtils;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.DataStore;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ReplicateJob;
import edu.wisc.library.sdg.preservation.manager.db.model.EventOutcome;
import edu.wisc.library.sdg.preservation.manager.db.model.EventType;
import edu.wisc.library.sdg.preservation.manager.db.model.IngestObjectState;
import edu.wisc.library.sdg.preservation.manager.db.model.Job;
import edu.wisc.library.sdg.preservation.manager.db.model.PreservationEvent;
import edu.wisc.library.sdg.preservation.manager.db.model.ReplicateJobDetails;
import edu.wisc.library.sdg.preservation.manager.db.model.StorageProblemType;
import edu.wisc.library.sdg.preservation.manager.service.IngestService;
import edu.wisc.library.sdg.preservation.manager.service.JobService;
import edu.wisc.library.sdg.preservation.manager.service.PreservationService;
import edu.wisc.library.sdg.preservation.manager.util.Agent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReplicateJobHandler extends BaseJobHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ReplicateJobHandler.class);

    private final PreservationService preservationService;
    private final IngestService ingestService;

    public ReplicateJobHandler(JobService jobService,
                               PreservationService preservationService,
                               IngestService ingestService) {
        super(jobService);
        this.preservationService = preservationService;
        this.ingestService = ingestService;
    }

    @Override
    public Object createWorkerJob(Job job) {
        var details = jobService.getReplicateJobDetails(job.getJobId());
        var object = preservationService.getObject(details.getObjectId());

        return new ReplicateJob()
                .jobId(job.getJobId())
                .internalObjectId(UuidUtils.withPrefix(details.getObjectId()))
                .vault(object.getVault())
                .externalObjectId(object.getExternalObjectId())
                .persistenceVersion(details.getPersistenceVersion())
                .source(DataStore.fromValue(details.getSource().toString()))
                .destination(DataStore.fromValue(details.getDestination().toString()));
    }

    @Override
    public void failJob(Job job) {
        super.failJob(job);

        var details = jobService.getReplicateJobDetails(job.getJobId());

        recordEvent(details, EventOutcome.FAILURE);
        updateIngestObjectState(details, IngestObjectState.REPLICATION_FAILED);

        createStorageProblem(details);
    }

    @Override
    public void completeJob(Job job) {
        super.completeJob(job);

        var details = jobService.getReplicateJobDetails(job.getJobId());

        recordEvent(details, EventOutcome.SUCCESS);
        updateIngestObjectState(details, IngestObjectState.REPLICATED);
        createValidationJob(details);
    }

    private void recordEvent(ReplicateJobDetails details, EventOutcome outcome) {
        try {
            var now = Time.now();
            var event = new PreservationEvent()
                    .setObjectId(details.getObjectId())
                    .setType(EventType.REPLICATE_OBJ_VERSION)
                    .setOutcome(outcome)
                    .setAgent(Agent.PRESERVATION_SERVICE_VERSION)
                    .setEventTimestamp(now);

            if (outcome == EventOutcome.SUCCESS) {
                event.info(String.format("Replicated OCFL version %s to %s",
                        details.getPersistenceVersion(), details.getDestination()), now);
            } else {
                event.error(String.format("Failed to replicated OCFL version %s to %s",
                        details.getPersistenceVersion(), details.getDestination()), now);
            }

            preservationService.recordEvent(event);

            if (details.getIngestId() != null) {
                ingestService.recordIngestEvent(event.toIngestEvent()
                        .setIngestId(details.getIngestId())
                        .setExternalObjectId(details.getExternalObjectId()));
            }
        } catch (RuntimeException e) {
            LOG.error("Failed to record replication event for job: {}", details, e);
        }
    }

    private void updateIngestObjectState(ReplicateJobDetails details, IngestObjectState state) {
        try {
            if (details.getIngestId() != null && details.getExternalObjectId() != null) {
                var object = ingestService.getIngestObject(details.getIngestId(), details.getExternalObjectId());

                // If replicating to multiple remotes after an ingest, consider the replication failed if any subset
                // of the replications fails
                if (object.getState() != state && object.getState() != IngestObjectState.REPLICATION_FAILED) {
                    if (state == IngestObjectState.REPLICATED) {
                        ingestService.updateObjectCompleteReplication(details.getIngestId(), details.getExternalObjectId());
                    } else {
                        ingestService.updateObjectFailReplication(details.getIngestId(), details.getExternalObjectId());
                    }
                }
            }
        } catch (RuntimeException e) {
            LOG.error("Failed to update ingest object state after replication: {}", details, e);
        }
    }

    private void createStorageProblem(ReplicateJobDetails details) {
        try {
            var existsInDataSource = preservationService.existsInDataStore(details.getObjectId(),
                    details.getPersistenceVersion(),
                    details.getDestination());
            if (!existsInDataSource) {
                preservationService.setObjectVersionStorageProblem(details.getObjectId(),
                        details.getPersistenceVersion(),
                        details.getDestination(),
                        StorageProblemType.MISSING);
            }
        } catch (RuntimeException e) {
            LOG.error("Failed to create storage problem after failed replication: {}", details, e);
        }
    }

    private void createValidationJob(ReplicateJobDetails details) {
        try {
            var location = preservationService.getStorageLocation(
                    details.getObjectId(), details.getPersistenceVersion(), details.getDestination());
            jobService.createValidateRemoteJob(location, true);
        } catch (RuntimeException e) {
            LOG.error("Failed to create remote validation job for {}", details, e);
        }
    }

}
