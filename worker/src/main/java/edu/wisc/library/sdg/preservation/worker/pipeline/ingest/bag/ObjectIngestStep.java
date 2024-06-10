package edu.wisc.library.sdg.preservation.worker.pipeline.ingest.bag;

import edu.wisc.library.sdg.preservation.common.util.ArgCheck;
import edu.wisc.library.sdg.preservation.worker.pipeline.NextStep;
import edu.wisc.library.sdg.preservation.worker.pipeline.Pipeline;
import edu.wisc.library.sdg.preservation.worker.pipeline.PipelineStep;
import edu.wisc.library.sdg.preservation.worker.pipeline.ingest.object.IngestObjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * This step kicks off the processing of all of the objects that are contained in the SIP.
 */
public class ObjectIngestStep implements PipelineStep<IngestBagContext> {

    private static final Logger LOG = LoggerFactory.getLogger(ObjectIngestStep.class);

    private final Pipeline<IngestObjectContext> ingestObjectPipeline;
    private final int objectParallelism;

    public ObjectIngestStep(Pipeline<IngestObjectContext> ingestObjectPipeline, int objectParallelism) {
        this.ingestObjectPipeline = ArgCheck.notNull(ingestObjectPipeline, "ingestObjectPipeline");
        this.objectParallelism = ArgCheck.condition(objectParallelism, objectParallelism > 0,
                "objectParallelism must be greater than 0");
    }

    @Override
    public void execute(IngestBagContext context, NextStep<IngestBagContext> nextStep) {
        LOG.debug("Executing {}", this.getClass().getSimpleName());

        var executor = Executors.newWorkStealingPool(objectParallelism);

        try (var files = Files.list(context.getBagDataPath())) {
            files.forEach(file -> {
                if (Files.isDirectory(file)) {
                    var externalId = URLDecoder.decode(file.getFileName().toString(), StandardCharsets.UTF_8);

                    LOG.info("Ingesting object <{}:{}> at {}", context.getIngestBatch().getVault(), externalId, file);

                    executor.submit(() -> {
                        try {
                            ingestObjectPipeline.execute(context.createIngestObjectContext(externalId, file));
                        } catch (RuntimeException e) {
                            LOG.error("Failed to analyze object <{}:{}> at {}",
                                    context.getIngestBatch().getVault(), externalId, file, e);
                        }
                    });
                } else {
                    LOG.warn(String.format("Skipping %s because it is not a directory", file));
                }
            });
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } finally {
            executor.shutdown();

            try {
                executor.awaitTermination(1, TimeUnit.DAYS);
            } catch (InterruptedException e) {
                LOG.error("Interrupted while waiting for object analysis to complete");
                Thread.currentThread().interrupt();
            }
        }

        nextStep.execute();
    }

}
