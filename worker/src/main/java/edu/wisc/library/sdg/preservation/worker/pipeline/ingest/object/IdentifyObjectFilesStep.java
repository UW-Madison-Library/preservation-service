package edu.wisc.library.sdg.preservation.worker.pipeline.ingest.object;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.hash.Hashing;
import com.google.common.io.ByteSource;
import com.google.common.io.MoreFiles;
import edu.wisc.library.sdg.preservation.common.exception.SafeRuntimeException;
import edu.wisc.library.sdg.preservation.common.util.ArgCheck;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.EventType;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RegisterIngestObjectFileRequest;
import edu.wisc.library.sdg.preservation.worker.pipeline.NextStep;
import edu.wisc.library.sdg.preservation.worker.pipeline.PipelineStep;
import edu.wisc.library.sdg.preservation.worker.util.ManagerRetrier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * This step identifies all of the files in an object and registers them with the preservation manager.
 */
public class IdentifyObjectFilesStep implements PipelineStep<IngestObjectContext> {

    private static final Logger LOG = LoggerFactory.getLogger(IdentifyObjectFilesStep.class);

    private final ManagerInternalApi managerClient;

    public IdentifyObjectFilesStep(ManagerInternalApi managerClient) {
        this.managerClient = ArgCheck.notNull(managerClient, "managerClient");
    }

    @Override
    public void execute(IngestObjectContext context, NextStep<IngestObjectContext> nextStep) {
        LOG.debug("Executing {}", this.getClass().getSimpleName());

        context.recordEvent(EventType.IDENTIFY_OBJ, logger -> {
            var objectPath = context.getObjectSourcePath();
            var files = findFiles(objectPath);

            files.forEach(file -> {
                var filePath = objectPath.relativize(file).toString();

                try {
                    var response = ManagerRetrier.retry(() -> {
                        return managerClient.registerIngestObjectFile(new RegisterIngestObjectFileRequest()
                                .ingestObjectId(context.getIngestObjectId())
                                .filePath(filePath));
                    });
                    context.addFile(filePath, response.getIngestFileId());
                } catch (RuntimeException e) {
                    throw new RuntimeException(String.format("Failed to register file <%s;%s>",
                            context.getIngestObjectId(), filePath), e);
                }

                try {
                    var digest = computeDigest(file);
                    var size = fileSize(file);

                    if (size == 0) {
                        logger.warn("File %s contains 0 bytes", filePath);
                    }

                    context.addFileDetails(filePath, digest, size);
                } catch (RuntimeException e) {
                    throw new RuntimeException(String.format("Failed to identify details for file <%s;%s>",
                            context.getIngestObjectId(), filePath), e);
                }
            });
        });

        nextStep.execute();
    }

    private List<Path> findFiles(Path path) {
        var files = new ArrayList<Path>();

        if (Files.isDirectory(path)) {
            try (var paths = Files.walk(path)) {
                paths.filter(Files::isRegularFile)
                    .forEach(files::add);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        } else {
            files.add(path);
        }

        return files;
    }

    private String computeDigest(Path path) {
        try {
            return fileByteSource(path).hash(Hashing.sha256()).toString();
        } catch (IOException e) {
            throw new SafeRuntimeException("Failed to compute digest for file " + path.getFileName(), e);
        }
    }

    private long fileSize(Path path) {
        try {
            return Files.size(path);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @VisibleForTesting
    protected ByteSource fileByteSource(Path filePath) {
        return MoreFiles.asByteSource(filePath);
    }


}
