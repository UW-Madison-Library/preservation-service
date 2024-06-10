package edu.wisc.library.sdg.preservation.worker.process;

import edu.wisc.library.sdg.preservation.manager.client.internal.model.IngestBatch;
import edu.wisc.library.sdg.preservation.worker.log.EventRecorder;
import edu.wisc.library.sdg.preservation.worker.pipeline.Pipeline;
import edu.wisc.library.sdg.preservation.worker.pipeline.ingest.bag.IngestBagContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BatchAnalyzeProcessor implements BatchProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(BatchAnalyzeProcessor.class);

    private final Pipeline<IngestBagContext> analyzePipeline;
    private final EventRecorder eventRecorder;

    @Autowired
    public BatchAnalyzeProcessor(Pipeline<IngestBagContext> analyzePipeline,
                                 EventRecorder eventRecorder) {
        this.analyzePipeline = analyzePipeline;
        this.eventRecorder = eventRecorder;
    }

    @Override
    public void process(IngestBatch batch) {
        // TODO this pipeline might be more complicated than it's worth
        analyzePipeline.execute(IngestBagContext.fromIngestBatch(batch, eventRecorder));
    }

}
