package edu.wisc.library.sdg.preservation.worker.event.listener;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import edu.wisc.library.sdg.preservation.common.metrics.Outcome;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.IngestBatchState;
import edu.wisc.library.sdg.preservation.worker.event.model.EventBusManager;
import edu.wisc.library.sdg.preservation.worker.event.model.ProcessBatchEvent;
import edu.wisc.library.sdg.preservation.worker.lock.LockManager;
import edu.wisc.library.sdg.preservation.worker.log.JobLogger;
import edu.wisc.library.sdg.preservation.worker.process.BatchAnalyzeProcessor;
import edu.wisc.library.sdg.preservation.worker.process.BatchCleanupProcessor;
import edu.wisc.library.sdg.preservation.worker.process.BatchIngestProcessor;
import edu.wisc.library.sdg.preservation.worker.process.BatchNoOpProcessor;
import edu.wisc.library.sdg.preservation.worker.process.BatchProcessor;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class BatchEventListener extends JobEventListener<ProcessBatchEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(BatchEventListener.class);

    private static final String NAME = "ProcessBatch";

    private final EventBusManager eventBusManager;
    private final LockManager lockManager;
    private final MeterRegistry registry;
    private final ManagerInternalApi internalClient;
    private final Map<IngestBatchState, BatchProcessor> processorMap;
    private final Map<IngestBatchState, String> processorNameMap;
    private final BatchProcessor noOpProcessor;

    @Autowired
    public BatchEventListener(EventBusManager eventBusManager,
                              LockManager lockManager,
                              MeterRegistry registry,
                              ManagerInternalApi internalClient,
                              BatchAnalyzeProcessor analyzeProcessor,
                              BatchCleanupProcessor cleanupProcessor,
                              BatchIngestProcessor ingestProcessor) {
        super(internalClient, NAME, registry);
        this.eventBusManager = eventBusManager;
        this.lockManager = lockManager;
        this.registry = registry;
        this.internalClient = internalClient;

        processorMap = Map.of(
                IngestBatchState.RECEIVED, analyzeProcessor,
                IngestBatchState.PENDING_INGESTION, ingestProcessor,
                IngestBatchState.PENDING_REJECTION, cleanupProcessor
        );
        processorNameMap = Map.of(
            IngestBatchState.RECEIVED, "AnalyzeBatch",
            IngestBatchState.PENDING_INGESTION, "IngestBatch",
            IngestBatchState.PENDING_REJECTION, "RejectBatch"
        );
        this.noOpProcessor = new BatchNoOpProcessor();

        this.eventBusManager.register(this);
    }

    @Subscribe
    @AllowConcurrentEvents
    public void process(ProcessBatchEvent event) {
        eventBusManager.process(() -> {
            super.receive(event);
        });
    }

    @Override
    protected boolean processEvent(ProcessBatchEvent event, JobLogger logger) {
        lockManager.doInBatchLock(event.getIngestId(), () -> {
            var ingestId = event.getIngestId();

            var batch = internalClient.retrieveBatch(ingestId).getIngestBatch();
            var processor = processorMap.getOrDefault(batch.getState(), noOpProcessor);

            var outcome = Outcome.SUCCESS;
            var timer = Timer.start();

            try {
                processor.process(batch);
            } catch (RuntimeException e) {
                outcome = Outcome.FAILURE;
                throw e;
            } finally {
                // Unfortunately, we need to record a metric here because the listener leave metrics cannot differentiate
                // between batch operations
                timer.stop(Timer.builder("processEvent")
                        .tag("event", processorNameMap.getOrDefault(batch.getState(), "NoOp"))
                        .tag(Outcome.NAME, outcome.toString())
                        .publishPercentileHistogram()
                        .register(registry));
            }
        });

        return true;
    }

}
