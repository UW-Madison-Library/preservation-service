package edu.wisc.library.sdg.preservation.worker.pipeline.ingest.bag;

import edu.wisc.library.sdg.preservation.common.exception.SafeRuntimeException;
import edu.wisc.library.sdg.preservation.common.util.ArgCheck;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.EventType;
import edu.wisc.library.sdg.preservation.worker.pipeline.NextStep;
import edu.wisc.library.sdg.preservation.worker.pipeline.PipelineStep;
import edu.wisc.library.sdg.preservation.worker.tools.clamav.ClamAV;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Executes ClamAV on the unzipped bag. The entire bag is rejected if a virus is detected.
 */
public class ClamAVBagStep implements PipelineStep<IngestBagContext> {

    private static final Logger LOG = LoggerFactory.getLogger(ClamAVBagStep.class);

    private final ClamAV clamav;
    private final boolean required;
    private final Counter counter;

    public ClamAVBagStep(ClamAV clamav, MeterRegistry registry, boolean required) {
        this.clamav = ArgCheck.notNull(clamav, "clamav");
        this.required = required;
        this.counter = Counter.builder("virusDetected")
                .register(registry);
    }

    @Override
    public void execute(IngestBagContext context, NextStep<IngestBagContext> nextStep) {
        LOG.debug("Executing {}", this.getClass().getSimpleName());

        // Skip the ClamAV scan if it isn't required and ClamAV isn't installed on the system
        if (!required && !clamav.exists()) {
            LOG.info("Skipping ClamAV scan");
            nextStep.execute();
            return;
        }

        context.recordEvent(EventType.VIRUS_SCAN_BAG, logger -> {
            var infectedFiles = clamav.scan(context.getBagArchivedPath());

            if (!infectedFiles.isEmpty()) {
                counter.increment();
                throw new SafeRuntimeException("Viruses detected in bag");
            }
        });

        nextStep.execute();
    }

}
