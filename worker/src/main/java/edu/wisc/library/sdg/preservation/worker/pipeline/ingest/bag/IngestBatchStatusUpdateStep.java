package edu.wisc.library.sdg.preservation.worker.pipeline.ingest.bag;

import edu.wisc.library.sdg.preservation.common.util.ArgCheck;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.BatchCompleteAnalysisRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.BatchStartAnalysisRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.Outcome;
import edu.wisc.library.sdg.preservation.worker.pipeline.NextStep;
import edu.wisc.library.sdg.preservation.worker.pipeline.PipelineStep;
import edu.wisc.library.sdg.preservation.worker.util.ManagerRetrier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This step manages the state of the ingest batch. It transitions it to ANALYZING when processing starts, and then to
 * ANALYZING_FAILED or PENDING_APPROVAL when processing finished.
 */
public class IngestBatchStatusUpdateStep implements PipelineStep<IngestBagContext> {

    private static final Logger LOG = LoggerFactory.getLogger(IngestBatchStatusUpdateStep.class);

    private final ManagerInternalApi managerClient;

    public IngestBatchStatusUpdateStep(ManagerInternalApi managerClient) {
        this.managerClient = ArgCheck.notNull(managerClient, "managerClient");
    }

    @Override
    public void execute(IngestBagContext context, NextStep<IngestBagContext> nextStep) {
        LOG.debug("Executing {}", this.getClass().getSimpleName());
        LOG.debug("Starting analysis of batch <{}>", context.getIngestId());

        managerClient.batchStartAnalysis(new BatchStartAnalysisRequest()
                .ingestId(context.getIngestId()));

        var outcome = Outcome.SUCCESS;

        try {
            nextStep.execute();
        } catch (RuntimeException e) {
            outcome = Outcome.FAILURE;
            throw e;
        } finally {
            if (context.failed()) {
                outcome = Outcome.FAILURE;
            }

            try {
                LOG.info("Completing analysis of batch <{}>. Outcome: <{}>", context.getIngestId(), outcome);
                var request = new BatchCompleteAnalysisRequest()
                        .ingestId(context.getIngestId())
                        .outcome(outcome);
                ManagerRetrier.retry(() -> {
                    managerClient.batchCompleteAnalysis(request);
                });
            } catch (RuntimeException e) {
                LOG.error("Failed mark analysis complete for batch <{}> outcome <{}>", context.getIngestId(), outcome, e);
            }
        }
    }

}
