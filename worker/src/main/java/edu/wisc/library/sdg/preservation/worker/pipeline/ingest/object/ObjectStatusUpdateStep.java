package edu.wisc.library.sdg.preservation.worker.pipeline.ingest.object;

import edu.wisc.library.sdg.preservation.common.util.ArgCheck;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ObjectCompleteAnalysisRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.Outcome;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RegisterIngestObjectRequest;
import edu.wisc.library.sdg.preservation.worker.pipeline.NextStep;
import edu.wisc.library.sdg.preservation.worker.pipeline.PipelineStep;
import edu.wisc.library.sdg.preservation.worker.util.ManagerRetrier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObjectStatusUpdateStep implements PipelineStep<IngestObjectContext> {

    private static final Logger LOG = LoggerFactory.getLogger(ObjectStatusUpdateStep.class);

    private final ManagerInternalApi managerClient;

    public ObjectStatusUpdateStep(ManagerInternalApi managerClient) {
        this.managerClient = ArgCheck.notNull(managerClient, "managerClient");
    }

    @Override
    public void execute(IngestObjectContext context, NextStep<IngestObjectContext> nextStep) {
        LOG.debug("Executing {}", this.getClass().getSimpleName());

        var outcome = Outcome.SUCCESS;

        try {
            var response = ManagerRetrier.retry(() -> {
                return managerClient.registerIngestObject(new RegisterIngestObjectRequest()
                        .ingestId(context.getIngestId())
                        .externalObjectId(context.getExternalId())
                        .objectRootPath(context.getObjectSourcePath().toString()));
            });

            context.setIngestObjectId(response.getIngestObjectId());

            nextStep.execute();
        } catch (RuntimeException e) {
            outcome = Outcome.FAILURE;
            throw e;
        } finally {
            if (context.failed()) {
                outcome = Outcome.FAILURE;
            }

            LOG.debug("Completing analysis of <{};{}>. Outcome: <{}>",
                    context.getIngestId(), context.getExternalId(), outcome);

            try {
                var request = new ObjectCompleteAnalysisRequest()
                        .ingestId(context.getIngestId())
                        .externalObjectId(context.getExternalId())
                        .outcome(outcome);
                ManagerRetrier.retry(() -> {
                    managerClient.objectCompleteAnalysis(request);
                });
            } catch (RuntimeException e) {
                LOG.error("Failed to update Ingest Object <{};{}> state",
                        context.getIngestId(), context.getExternalId(), e);
            }
        }
    }

}
