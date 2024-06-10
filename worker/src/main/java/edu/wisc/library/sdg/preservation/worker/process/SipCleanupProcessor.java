package edu.wisc.library.sdg.preservation.worker.process;

import edu.wisc.library.sdg.preservation.common.exception.SafeRuntimeException;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.BatchMarkDeletedRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.EventType;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.IngestBatchState;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ShouldDeleteBatchRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ShouldDeleteBatchResponse;
import edu.wisc.library.sdg.preservation.worker.log.EventRecorder;
import edu.wisc.library.sdg.preservation.worker.util.WorkDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class SipCleanupProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(SipCleanupProcessor.class);

    private final Path uploadDir;
    private final WorkDirectory workDir;
    private final ManagerInternalApi managerClient;
    private final EventRecorder eventRecorder;

    @Autowired
    public SipCleanupProcessor(@Value("${app.upload.dir}") Path uploadDir,
                               WorkDirectory workDir,
                               ManagerInternalApi managerClient,
                               EventRecorder eventRecorder) {
        this.uploadDir = uploadDir;
        this.workDir = workDir;
        this.managerClient = managerClient;
        this.eventRecorder = eventRecorder;
    }

    /**
     * Deletes old SIPs that the system no longer needs
     */
    public void cleanupSips() {
        LOG.info("Looking for SIPs to delete");

        var hasFailures = new AtomicBoolean(false);

        var deletedIds = deleteOldSips(hasFailures);
        deleteOldWork(deletedIds, hasFailures);

        if (hasFailures.get()) {
            throw new SafeRuntimeException("Failed to successfully cleanup all expired SIPs");
        }
    }

    private List<Long> deleteOldSips(AtomicBoolean hasFailures) {
        var deletedIds = new ArrayList<Long>();

        try (var files = Files.list(uploadDir)) {
            files.forEach(file -> {
                try {
                    var response = shouldDelete(file.toString());
                    if (response.getVerdict() != ShouldDeleteBatchResponse.VerdictEnum.KEEP) {
                        Files.deleteIfExists(file);
                        if (response.getIngestId() != null) {
                            deletedIds.add(response.getIngestId());
                            markDeleted(response.getIngestId());
                        }
                    }
                } catch (Exception e) {
                    LOG.error("Failed to cleanup sip {}", file, e);
                }
            });
        } catch (IOException e) {
            LOG.error("Failed to list files in upload dir", e);
            hasFailures.set(true);
        }

        return deletedIds;
    }

    private void deleteOldWork(List<Long> deletedIds, AtomicBoolean hasFailures) {
        try {
            var workIds = workDir.listIngestIds();
            for (var id : workIds) {
                try {
                    if (deletedIds.contains(id) || shouldDelete(id)) {
                        workDir.deleteBatchDirectory(id);
                        markDeleted(id);
                    }
                } catch (Exception e) {
                    LOG.error("Failed to cleanup work directory for batch {}", id, e);
                    hasFailures.set(true);
                }
            }
        } catch (Exception e) {
            LOG.error("Failed to cleanup old batch work directories", e);
            hasFailures.set(true);
        }
    }

    private boolean shouldDelete(Long ingestId) {
        var response = managerClient.shouldDeleteBatch(
                new ShouldDeleteBatchRequest().ingestId(ingestId));
        return response.getVerdict() != ShouldDeleteBatchResponse.VerdictEnum.KEEP;
    }

    private ShouldDeleteBatchResponse shouldDelete(String filePath) {
        return managerClient.shouldDeleteBatch(new ShouldDeleteBatchRequest().sipPath(filePath));
    }

    private void markDeleted(Long ingestId) {
        try {
            // get the batch state before it's marked DELETED
            var batchState = managerClient.retrieveBatch(ingestId).getIngestBatch().getState();
            managerClient.batchMarkDeleted(new BatchMarkDeletedRequest().ingestId(ingestId));

            // only record the transition to DELETED if it isn't already DELETED
            if (batchState != IngestBatchState.DELETED) {
                eventRecorder.recordEvent(EventType.DELETE_BAG, ingestId, null, logger -> {
                    logger.info("Stale batch content automatically transitioned from %s to %s",
                            batchState.toString(),
                            IngestBatchState.DELETED.toString());
                });
            }
        } catch (RuntimeException e) {
            LOG.error("Failed to mark batch {} as deleted", ingestId);
        }
    }

}
