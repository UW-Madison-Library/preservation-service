package edu.wisc.library.sdg.preservation.worker.pipeline.ingest.bag;

import edu.wisc.library.sdg.preservation.common.PreservationConstants;
import edu.wisc.library.sdg.preservation.common.exception.SafeRuntimeException;
import edu.wisc.library.sdg.preservation.common.exception.ValidationException;
import edu.wisc.library.sdg.preservation.common.util.Archiver;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.EventType;
import edu.wisc.library.sdg.preservation.worker.exception.TerminateNoLogException;
import edu.wisc.library.sdg.preservation.worker.log.EventLogger;
import edu.wisc.library.sdg.preservation.worker.pipeline.NextStep;
import edu.wisc.library.sdg.preservation.worker.pipeline.PipelineStep;
import gov.loc.repository.bagit.domain.Bag;
import gov.loc.repository.bagit.reader.BagReader;
import gov.loc.repository.bagit.verify.BagVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This step explodes a compressed SIP tarball to a work directory to that it can be processed for ingestion.
 *
 * Additionally, it validates the general structure of a SIP and that all of the object ids that it contains are legal.
 * However, the objects themselves are not validated yet. Processing will not continue if problems are found during this step.
 *
 * <p>
 * Output:
 * <ul>
 *     <li>Bag exploded to [work-dir]/[ingest-id]-sip
 * </ul>
 */
public class ExplodeAndValidateBagStep implements PipelineStep<IngestBagContext> {

    private static final Logger LOG = LoggerFactory.getLogger(ExplodeAndValidateBagStep.class);

    private final Set<String> allowedFilesInData = Set.of(PreservationConstants.BAG_FILE_META);

    @Override
    public void execute(IngestBagContext context, NextStep<IngestBagContext> nextStep) {
        LOG.debug("Executing {}", this.getClass().getSimpleName());

        context.recordEvent(EventType.VALIDATE_BAG, logger -> {
            explodeBag(context.getBagArchivedPath(), context.getBagExplodedPath());
            var bagDir = identifyBagDirectory(context.getBagExplodedPath());
            context.setBagRootPath(bagDir);

            validateBag(context);
            validateDataDir(context, logger);
        });

        nextStep.execute();
    }

    private void explodeBag(Path archivePath, Path destinationPath) {
        try {
            Archiver.extract(archivePath, destinationPath);
        } catch (RuntimeException e) {
            throw new SafeRuntimeException("Failed to extract bag from archive. Make sure the bag is archived in a zip file.", e);
        }
    }

    private Path identifyBagDirectory(Path explodeDestinationPath) {
        var files = explodeDestinationPath.toFile().listFiles();

        if (files.length != 1) {
            throw new ValidationException("There should be a single root directory per bag. Found " + files.length + " files.");
        }

        if (!files[0].isDirectory()) {
            throw new ValidationException(String.format("File in bag root '%s' is not a directory", files[0].getName()));
        }

        return files[0].toPath();
    }

    private void validateBag(IngestBagContext context) {
        try (BagVerifier verifier = new BagVerifier(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()))) {
            BagReader reader = new BagReader();
            Bag bag = reader.read(context.getBagRootPath());
            verifier.isValid(bag, false);
        } catch (NoSuchFileException e) {
            throw new ValidationException("Invalid BagIt bag: Missing file " + sanitize(e.getMessage(), context), e);
        } catch (Exception e) {
            // Pass along the messages from the BagIt validator
            throw new ValidationException("Invalid BagIt bag: " + sanitize(e.getMessage(), context), e);
        }
    }

    private void validateDataDir(IngestBagContext context, EventLogger eventLogger) {
        var hasChildren = new AtomicBoolean(false);
        var hasErrors = new AtomicBoolean(false);
        var dataPath = context.getBagDataPath();

        try (var children = Files.list(dataPath)) {
            children.forEach(child -> {
                if (Files.isRegularFile(child)) {
                    if (!allowedFilesInData.contains(child.getFileName().toString())) {
                        eventLogger.error("The bag's data directory may only contain object directories. Found: "
                                + dataPath.toAbsolutePath().relativize(child));
                    }
                } else {
                    try (var walk = Files.walk(child)) {
                        if (walk.filter(Files::isRegularFile).findFirst().isEmpty()) {
                            eventLogger.error("No files were found for object "
                                    + dataPath.toAbsolutePath().relativize(child));
                            hasErrors.set(true);
                        } else {
                            hasChildren.set(true);
                        }
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                }
            });
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        if (!hasChildren.get()) {
            throw new ValidationException("The bag does not contain any objects.");
        }
        if (hasErrors.get()) {
            throw new TerminateNoLogException(String.format("Bag %s contains objects without any files.", dataPath));
        }
    }

    private String sanitize(String original, IngestBagContext context) {
        return original.replaceAll(context.getBagRootPath().toAbsolutePath().getParent().toString(), "");
    }

}
