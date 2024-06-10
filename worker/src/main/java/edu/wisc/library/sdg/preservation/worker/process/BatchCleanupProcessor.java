package edu.wisc.library.sdg.preservation.worker.process;

import edu.wisc.library.sdg.preservation.common.exception.SafeRuntimeException;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.BatchCompleteRejectingRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.BatchStartRejectingRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.EventType;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.IngestBatch;
import edu.wisc.library.sdg.preservation.worker.log.EventRecorder;
import edu.wisc.library.sdg.preservation.worker.util.ManagerRetrier;
import edu.wisc.library.sdg.preservation.worker.util.WorkDirectory;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class BatchCleanupProcessor implements BatchProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(BatchCleanupProcessor.class);

    private final WorkDirectory workDir;
    private final ManagerInternalApi managerClient;
    private final EventRecorder eventRecorder;

    @Autowired
    public BatchCleanupProcessor(WorkDirectory workDir,
                                 ManagerInternalApi managerClient,
                                 EventRecorder eventRecorder) {
        this.workDir = workDir;
        this.managerClient = managerClient;
        this.eventRecorder = eventRecorder;
    }

    @Override
    public void process(IngestBatch batch) {
        var ingestId = batch.getIngestId();

        LOG.info("Cleaning up batch <{}>", ingestId);

        startRejecting(ingestId);

        try {
            eventRecorder.recordEvent(EventType.DELETE_BAG, ingestId, null, eventLogger -> {
                if (Strings.isNotBlank(batch.getFilePath())) {
                    LOG.info("Deleting bag: {}", batch.getFilePath());
                    try {
                        Files.deleteIfExists(Paths.get(batch.getFilePath()));
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                }

                workDir.deleteBatchDirectory(ingestId);
            });
        } catch (RuntimeException e) {
            throw new SafeRuntimeException(String.format("Failed to cleanup batch <%s>", ingestId), e);
        } finally {
            safeCompleteRejecting(ingestId);
        }
    }

    private void startRejecting(Long ingestId) {
        managerClient.batchStartRejecting(new BatchStartRejectingRequest().ingestId(ingestId));
    }

    private void safeCompleteRejecting(Long ingestId) {
        try {
            ManagerRetrier.retry(() -> {
                managerClient.batchCompleteRejecting(new BatchCompleteRejectingRequest()
                        .ingestId(ingestId));
            });
        } catch (RuntimeException e) {
            LOG.error("Failed mark rejection complete for batch <{}>", ingestId, e);
        }
    }

}
