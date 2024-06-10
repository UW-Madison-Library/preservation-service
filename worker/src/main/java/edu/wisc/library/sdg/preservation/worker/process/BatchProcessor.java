package edu.wisc.library.sdg.preservation.worker.process;

import edu.wisc.library.sdg.preservation.manager.client.internal.model.IngestBatch;

public interface BatchProcessor {

    void process(IngestBatch batch);

}
