package edu.wisc.library.sdg.preservation.worker.pipeline.ingest.bag;

import edu.wisc.library.sdg.preservation.worker.pipeline.NextStep;
import edu.wisc.library.sdg.preservation.worker.pipeline.PipelineStep;
import edu.wisc.library.sdg.preservation.worker.tools.fits.Fits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * The RestartFitsStep is required because fits-web runs out of memory if it is not restarted.
 * If the next batch is small then fits-web may not have time to restart before the next fits analysis,
 * so this step builds in a delay with waitFor() after executing the restart command.
 */
public class RestartFitsStep implements PipelineStep<IngestBagContext> {

    private static final Logger LOG = LoggerFactory.getLogger(RestartFitsStep.class);

    private static final String RESTART_COMMAND = "sudo systemctl restart container-fits-web.service";

    private final Fits fits;

    private final boolean fitsRequired;

    public RestartFitsStep(final Fits fits, final boolean fitsRequired) {
        this.fits = fits;
        this.fitsRequired = fitsRequired;
    }

    @Override
    public void execute(final IngestBagContext context, final NextStep<IngestBagContext> nextStep) {
        LOG.debug("Executing {}", this.getClass().getSimpleName());

        if (fitsRequired && fits.exists()) {
            try {
                Runtime.getRuntime().exec(RESTART_COMMAND).waitFor();
            } catch (IOException | InterruptedException e) {
                LOG.error("Failure restarting fits-web", e);
            }
        }

        nextStep.execute();
    }

}
