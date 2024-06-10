package edu.wisc.library.sdg.preservation.manager.scheduling;

import edu.wisc.library.sdg.preservation.manager.db.model.IngestBatchState;
import edu.wisc.library.sdg.preservation.manager.db.model.IngestObjectState;
import edu.wisc.library.sdg.preservation.manager.service.IngestService;
import edu.wisc.library.sdg.preservation.manager.service.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BatchReplicationMonitor {

    private static final Logger LOG = LoggerFactory.getLogger(BatchReplicationMonitor.class);

    private final IngestService ingestService;
    private final JobService jobService;

    @Autowired
    public BatchReplicationMonitor(IngestService ingestService, JobService jobService) {
        this.ingestService = ingestService;
        this.jobService = jobService;
    }

    /**
     * Identifies all of the batches that are currently in a REPLICATING state, and then checks each of them to see
     * if they contain any objects that are still replicating. If not, the batch state is updated to indicate replication
     * success or failure based on whether any of the batch's objects failed replication.
     */
    public void checkReplicationStatuses() {
        var ids = ingestService.getAllBatchesInState(IngestBatchState.REPLICATING);

        LOG.debug("The following batches are replicating: {}", ids);

        ids.forEach(this::checkReplicationStatus);
    }

    /**
     * Checks if all of the objects in the specified batch have finished replication and updates the batch state accordingly.
     *
     * @param ingestId the id of the batch
     */
    public void checkReplicationStatus(Long ingestId) {
        try {
            var incompleteJobCount = jobService.countIncompleteIngestReplicationJobs(ingestId);
            LOG.debug("Batch <{}> has {} outstanding replication jobs", ingestId, incompleteJobCount);

            if (incompleteJobCount == 0) {
                var failedReplications = ingestService.countObjectsByIngestIdAndState(ingestId, IngestObjectState.REPLICATION_FAILED);

                if (failedReplications == 0) {
                    LOG.info("Batch <{}> all objects replicated; marking complete", ingestId);
                    ingestService.updateBatchCompleteReplication(ingestId);
                } else {
                    LOG.info("Batch <{}> contains {} replication failures; marking failed", ingestId, failedReplications);
                    ingestService.updateBatchFailReplication(ingestId);
                }
            }
        } catch (RuntimeException e) {
            LOG.error("Failed to process replication status for batch <{}>", ingestId, e);
        }
    }

}
