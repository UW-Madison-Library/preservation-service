package edu.wisc.library.sdg.preservation.worker.pipeline.ingest.object;

import edu.wisc.library.sdg.preservation.common.util.ArgCheck;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;
import edu.wisc.library.sdg.preservation.worker.pipeline.NextStep;
import edu.wisc.library.sdg.preservation.worker.pipeline.PipelineStep;
import edu.wisc.library.sdg.preservation.worker.util.ManagerRetrier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This step must be executed after all of the files in an object have been identified, registered, and analyzed.
 * It reports all of the accumulated details about the files back to the preservation manager.
 */
public class RegisterObjectFileDetailsStep implements PipelineStep<IngestObjectContext> {

    private static final Logger LOG = LoggerFactory.getLogger(RegisterObjectFileDetailsStep.class);

    private final ManagerInternalApi managerClient;

    public RegisterObjectFileDetailsStep(ManagerInternalApi managerClient) {
        this.managerClient = ArgCheck.notNull(managerClient, "managerClient");
    }

    @Override
    public void execute(IngestObjectContext context, NextStep<IngestObjectContext> nextStep) {
        LOG.debug("Executing {}", this.getClass().getSimpleName());

        context.getFileDetails().forEach(fileDetails -> {
            try {
                ManagerRetrier.retry(() -> {
                    managerClient.registerIngestObjectFileDetails(fileDetails.toRequest());
                });
            } catch (RuntimeException e) {
                throw new RuntimeException(String.format("Failed to register file details for <%s;%s>",
                        context.getIngestObjectId(), fileDetails.getFilePath()), e);
            }
        });

        nextStep.execute();
    }

}
