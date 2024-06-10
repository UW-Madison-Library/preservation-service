package edu.wisc.library.sdg.preservation.worker.pipeline.ingest.bag;

import edu.wisc.library.sdg.preservation.worker.pipeline.NextStep;
import edu.wisc.library.sdg.preservation.worker.pipeline.PipelineStep;
import edu.wisc.library.sdg.preservation.worker.tools.fits.Fits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * The CheckFitsStep is required because of the RestartFitsStep. It checks to see if fits-web is running before
 * evaluating objects with FITS.
 */

public class CheckFitsStep implements PipelineStep<IngestBagContext> {
    private static final Logger LOG = LoggerFactory.getLogger(CheckFitsStep.class);

    private static final int MAX_TRIES = 12;

    private static final long SLEEP_DURATION = 5;

    private final Fits fits;

    private final boolean fitsRequired;

    public CheckFitsStep(final Fits fits, final boolean fitsRequired) {
        this.fits = fits;
        this.fitsRequired = fitsRequired;
    }

    @Override
    public void execute(final IngestBagContext context, final NextStep<IngestBagContext> nextStep) {
        LOG.debug("Executing {}", this.getClass().getSimpleName());

        if (fitsRequired) {
            var currentAttempt = 0;

            while (!fits.exists() && currentAttempt < MAX_TRIES) {
                currentAttempt++;
                try {
                    TimeUnit.SECONDS.sleep(SLEEP_DURATION);
                } catch (InterruptedException e) {
                    LOG.error("Error checking if FITS is running", e);
                }
            }
        }

        nextStep.execute();
    }
}
