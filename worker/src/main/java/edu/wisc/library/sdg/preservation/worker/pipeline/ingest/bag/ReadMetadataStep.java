package edu.wisc.library.sdg.preservation.worker.pipeline.ingest.bag;

import edu.wisc.library.sdg.preservation.common.PreservationConstants;
import edu.wisc.library.sdg.preservation.common.exception.SafeRuntimeException;
import edu.wisc.library.sdg.preservation.common.util.ArgCheck;
import edu.wisc.library.sdg.preservation.worker.bag.FileMetadataSerializer;
import edu.wisc.library.sdg.preservation.worker.pipeline.NextStep;
import edu.wisc.library.sdg.preservation.worker.pipeline.PipelineStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;

/**
 * Reads the file-meta.csv file in the root of the bag's data directory, if it exists. This must happen after the
 * bag has been exploded and validated.
 */
public class ReadMetadataStep implements PipelineStep<IngestBagContext> {

    private static final Logger LOG = LoggerFactory.getLogger(ReadMetadataStep.class);

    private final FileMetadataSerializer fileMetadataSerializer;

    public ReadMetadataStep(FileMetadataSerializer fileMetadataSerializer) {
        this.fileMetadataSerializer = ArgCheck.notNull(fileMetadataSerializer, "fileMetadataSerializer");
    }

    @Override
    public void execute(IngestBagContext context, NextStep<IngestBagContext> nextStep) {
        LOG.debug("Executing {}", this.getClass().getSimpleName());

        var fileMetaPath = context.getBagDataPath().resolve(PreservationConstants.BAG_FILE_META);

        if (Files.exists(fileMetaPath)) {
            LOG.debug("Found file metadata at {}", fileMetaPath);
            try {
                context.setObjectMetadataMap(fileMetadataSerializer.deserialize(fileMetaPath));
            } catch (RuntimeException e) {
                throw new SafeRuntimeException("Failed to parse file-meta.csv", e);
            }
        } else {
            LOG.debug("No file metadata found");
        }

        nextStep.execute();
    }

}
