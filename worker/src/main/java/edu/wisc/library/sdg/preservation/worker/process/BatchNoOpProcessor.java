package edu.wisc.library.sdg.preservation.worker.process;

import edu.wisc.library.sdg.preservation.manager.client.internal.model.IngestBatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BatchNoOpProcessor implements BatchProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(BatchNoOpProcessor.class);

    @Override
    public void process(IngestBatch batch) {
        LOG.info("Nothing to do for batch: {}", batch);
    }

}
