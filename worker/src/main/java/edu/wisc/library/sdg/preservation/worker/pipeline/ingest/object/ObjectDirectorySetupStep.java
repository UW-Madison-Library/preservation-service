package edu.wisc.library.sdg.preservation.worker.pipeline.ingest.object;

import edu.wisc.library.sdg.preservation.common.util.UncheckedFiles;
import edu.wisc.library.sdg.preservation.worker.pipeline.NextStep;
import edu.wisc.library.sdg.preservation.worker.pipeline.PipelineStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This step creates an object specific WIP directory to be used to write temporary files that are generated as part of
 * processing the object.
 * <p>
 * Output:
 * <ul>
 *     <li>[WIP-object-dir]
 * </ul>
 */
public class ObjectDirectorySetupStep implements PipelineStep<IngestObjectContext> {

    private static final Logger LOG = LoggerFactory.getLogger(ObjectDirectorySetupStep.class);

    @Override
    public void execute(IngestObjectContext context, NextStep<IngestObjectContext> nextStep) {
        LOG.debug("Executing {}", this.getClass().getSimpleName());

        LOG.debug("Object <{}:{}> using wip dir {}.",
            context.getVault(), context.getExternalId(), context.getObjectWorkPath());

        UncheckedFiles.createDirectories(context.getObjectWorkPath());

        nextStep.execute();
    }

}
