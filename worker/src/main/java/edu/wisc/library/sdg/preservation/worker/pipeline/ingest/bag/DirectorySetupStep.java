package edu.wisc.library.sdg.preservation.worker.pipeline.ingest.bag;

import edu.wisc.library.sdg.preservation.common.util.ArgCheck;
import edu.wisc.library.sdg.preservation.common.util.UncheckedFiles;
import edu.wisc.library.sdg.preservation.worker.pipeline.NextStep;
import edu.wisc.library.sdg.preservation.worker.pipeline.PipelineStep;
import edu.wisc.library.sdg.preservation.worker.util.WorkDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This step creates ingest batch level directories that will contain object level directories to be used when processing
 * objects for ingestion. When processing of the batch has completed, the AIP directory should contain an archival version
 * of the ingest batch. The WIP directory is used to store temporary files generated as part of processing an object.
 * <p>
 * Output:
 * <ul>
 *     <li>[work-dir]/[ingest-id-aip]
 *     <li>[work-dir]/[ingest-id-wip]
 * </ul>
 */
public class DirectorySetupStep implements PipelineStep<IngestBagContext> {

    private static final Logger LOG = LoggerFactory.getLogger(DirectorySetupStep.class);

    private final WorkDirectory workDir;

    public DirectorySetupStep(WorkDirectory workDir) {
        this.workDir = ArgCheck.notNull(workDir, "workDir");
    }

    @Override
    public void execute(IngestBagContext context, NextStep<IngestBagContext> nextStep) {
        LOG.debug("Executing {}", this.getClass().getSimpleName());

        workDir.deleteBatchDirectory(context.getIngestId());

        var explodePath = workDir.batchSourceDirectory(context.getIngestId());
        var workPath = workDir.batchWipDirectory(context.getIngestId());

        context.setBagExplodedPath(UncheckedFiles.createDirectories(explodePath));
        context.setBatchWorkPath(UncheckedFiles.createDirectories(workPath));

        nextStep.execute();
    }

}
