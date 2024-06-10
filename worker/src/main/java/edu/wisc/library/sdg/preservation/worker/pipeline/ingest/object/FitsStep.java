package edu.wisc.library.sdg.preservation.worker.pipeline.ingest.object;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import edu.wisc.library.sdg.preservation.common.PreservationConstants;
import edu.wisc.library.sdg.preservation.common.util.ArgCheck;
import edu.wisc.library.sdg.preservation.common.util.UncheckedFiles;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.EventType;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.FormatRegistry;
import edu.wisc.library.sdg.preservation.worker.log.EventLogger;
import edu.wisc.library.sdg.preservation.worker.pipeline.NextStep;
import edu.wisc.library.sdg.preservation.worker.pipeline.PipelineStep;
import edu.wisc.library.sdg.preservation.worker.pipeline.ingest.object.postprocessor.PhotoshopPropertiesFilter;
import edu.wisc.library.sdg.preservation.worker.pipeline.ingest.object.service.PronomIdentifier;
import edu.wisc.library.sdg.preservation.worker.tools.fits.Fits;
import edu.wisc.library.sdg.preservation.worker.tools.fits.model.FileStatus;
import edu.wisc.library.sdg.preservation.worker.tools.fits.model.FitsReport;
import edu.wisc.library.sdg.preservation.worker.tools.fits.model.FitsValue;
import edu.wisc.library.sdg.preservation.worker.tools.fits.model.ToolStatus;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * This step must be run after all of an object's files have been identified and registered. It executes FITS on the
 * object directory, and process each file's FITS report, noting file formats and validation problems. The FITS
 * reports are stored as part of the preservation metadata inside the preserved object. It also does some post-processing
 * on each FITS file, in the order of supplied post-processors. This happens before reports are processed.
 *
 * FITS must be installed on the system in order for this step to run.
 */
public class FitsStep implements PipelineStep<IngestObjectContext> {
    private static final Logger LOG = LoggerFactory.getLogger(FitsStep.class);

    private static final String FITS_SUFFIX = ".fits.xml";

    private static final String PRES_WORKER = "Preservation Worker";

    private static final String TIFF_MIME = "image/tiff";

    // At this time, FITS cannot identify the PRONOM for these MIME types
    private static final Map<String, String> MIME_TO_PRONOM = Map.of(
            "text/xml", "fmt/101",
            "text/html", "fmt/100",
            "audio/x-aiff","fmt/414",
            "application/vnd.google-earth.kml+xml", "fmt/244"
    );

    private final Fits fits;
    private final XmlMapper xmlMapper;
    private final boolean required;
    private final Counter typeConflictCounter;

    private final PhotoshopPropertiesFilter photoshopPropertiesFilter;

    private final PronomIdentifier pronomIdentifier;

    public FitsStep(Fits fits,
                    XmlMapper xmlMapper,
                    MeterRegistry meterRegistry,
                    boolean required,
                    PhotoshopPropertiesFilter photoshopPropertiesFilter,
                    PronomIdentifier pronomIdentifier) {
        this.fits = ArgCheck.notNull(fits, "fits");
        this.xmlMapper = ArgCheck.notNull(xmlMapper, "xmlMapper");
        this.required = required;
        this.typeConflictCounter = Counter.builder("fitsConflictingTypes")
                .register(meterRegistry);
        this.photoshopPropertiesFilter = photoshopPropertiesFilter;
        this.pronomIdentifier = pronomIdentifier;
    }

    @Override
    public void execute(IngestObjectContext context, NextStep<IngestObjectContext> nextStep) {
        LOG.debug("Executing {}", this.getClass().getSimpleName());

        // Skip the FITS analysis if it isn't required and FITS isn't installed on the system
        if (!required && !fits.cachedExists()) {
            LOG.info("Skipping FITS analysis");
            nextStep.execute();
            return;
        }

        var objectPath = context.getObjectSourcePath();
        var fitsOutputPath = UncheckedFiles.createDirectories(context.getObjectWorkPath()
                .resolve(PreservationConstants.OCFL_OBJECT_TECHMD_DIR_NAME)
                .resolve(PreservationConstants.OCFL_OBJECT_FITS_DIR_NAME));

        context.recordEvent(EventType.IDENTIFY_FILE_FORMAT, logger -> {
            try {
                // TODO info log for format detection? no way of knowing the FITS version
                fits.execute(objectPath, fitsOutputPath);

                for (var filePath : context.getFilePaths()) {
                    try {
                        var fitsReport = parseFitsReport(fitsOutputPath, filePath);

                        postProcessFitsReport(fitsOutputPath, filePath, fitsReport);

                        processIdentities(fitsOutputPath, filePath, fitsReport, context, logger);
                        processFileStatus(filePath, fitsReport, context, logger);
                        processMetadata(filePath, fitsReport, context);
                        validateToolStatus(filePath, fitsReport, logger);
                    } catch (RuntimeException e) {
                        LOG.error("Failed to process FITS report for <{};{}>", context.getIngestObjectId(), filePath, e);
                        logger.error("Failed to process FITS report for file %s", filePath);
                    }
                }
            } catch (RuntimeException e) {
                LOG.error("Failed to execute FITS for <{}>", context.getIngestObjectId(), e);
                logger.error("Failed to execute FITS");
            }
        });

        nextStep.execute();
    }

    private void postProcessFitsReport(final Path fitsOutputPath, final String filePath, final FitsReport fitsReport) {
        var identification = fitsReport.getIdentification();

        if (identification != null & identification.getIdentities() != null) {
            var identities = identification.getIdentities();
            var mimeTypes = identities.stream().map(identity -> identity.getMimeType()).toList();
            if (mimeTypes.contains(TIFF_MIME)) {
                photoshopPropertiesFilter.process(fitsOutputPath, filePath + FITS_SUFFIX);
            }
        }
    }

    private void processIdentities(Path fitsOutputPath,
                                   String filePath,
                                   FitsReport fitsReport,
                                   IngestObjectContext context,
                                   EventLogger logger) {
        if (fitsReport.getIdentification() == null
                || CollectionUtils.isEmpty(fitsReport.getIdentification().getIdentities())) {
            logger.warn("Failed to identify format for file %s", filePath);
            return;
        }

        var identities = fitsReport.getIdentification().getIdentities();

        if (identities.stream().map(identity -> identity.getMimeType()).distinct().toList().size() > 1) {
            // These are logged because FITS ideally only identifies a single MIME type.
            // This info is useful for configuring FITS.
            var types = fitsReport.getIdentification()
                    .getIdentities().stream().map(i -> {
                        return String.format("[%s:%s]", i.getTools(), i.getMimeType());
                    }).collect(Collectors.toList());
            typeConflictCounter.increment();
            LOG.info("FITS identified multiple types for batch <{}> object <{}> file <{}>: {}",
                    context.getIngestId(), context.getExternalId(), filePath, types);
        }

        var pronomFound = new AtomicBoolean(false);

        try {
            identities.forEach(identity -> {
                // We currently only care about the first tool that identified the format, which is the tool
                // with the highest precedence
                var firstTool = identity.getTools().get(0);
                var mimeType = identity.getMimeType();
                var toolId = toolId(fitsReport, firstTool.getToolId());
                context.addFileFormat(filePath, FormatRegistry.MIME, toolId, mimeType);

                if (identity.getExternalIdentifiers() != null) {
                    identity.getExternalIdentifiers().forEach(externalIdentifier -> {
                        if ("puid".equals(externalIdentifier.getType())) {
                            pronomFound.set(true);
                            var extToolId = toolId(fitsReport, externalIdentifier.getToolId());
                            context.addFileFormat(filePath, FormatRegistry.PRONOM,
                                    extToolId, externalIdentifier.getValue());
                        }
                    });
                }

                // if the PRONOM is not in the identity check the DROID tool output
                if (!pronomFound.get()) {
                    var rawDroidOutput = pronomIdentifier.getDroidPronom(fitsOutputPath, filePath + FITS_SUFFIX);

                    if (rawDroidOutput.getValue() != null && !rawDroidOutput.getValue().isBlank()) {
                        pronomFound.set(true);
                        var droidId = toolId(fitsReport, rawDroidOutput.getToolId());
                        context.addFileFormat(filePath, FormatRegistry.PRONOM, droidId, rawDroidOutput.getValue());
                    }
                }

                // if FITS did not identify the PRONOM for certain MIME types, add the PRONOM
                if (!pronomFound.get() && MIME_TO_PRONOM.containsKey(mimeType)) {
                    pronomFound.set(true);
                    context.addFileFormat(filePath, FormatRegistry.PRONOM, PRES_WORKER, MIME_TO_PRONOM.get(mimeType));
                }
            });
        } catch (RuntimeException e) {
            LOG.warn("Failed to process FITS format identification for {}", filePath, e);
            logger.error("Failed to process FITS format identification for %s", filePath);
        }

        if (!pronomFound.get()) {
            LOG.warn("FITS failed to identify a PRONOM id for ingest object <{}> file <{}>",
                    context.getIngestObjectId(), filePath);
        }
    }

    private void processFileStatus(String filePath,
                                   FitsReport fitsReport,
                                   IngestObjectContext context,
                                   EventLogger logger) {
        if (fitsReport.getFileStatus() != null) {
            // I think JHOVE is the only tool that is supplying this info
            var status = fitsReport.getFileStatus();
            validateStatusValues(filePath, fitsReport,
                    status.getWellFormed(),
                    "%s reported that %s is not well-formed", logger);
            validateStatusValues(filePath, fitsReport,
                    status.getValid(),
                    "%s reported that %s is not valid", logger);

            var statuses = collectStatuses(status);

            statuses.forEach(s -> {
                context.addFileValidity(filePath, s.valid, s.wellFormed, toolId(fitsReport, s.toolId));
            });

            if (status.getMessages() != null) {
                status.getMessages().forEach(message -> {
                    var text = message.getValue();
                    var logMessage = String.format("%s reported for file %s: %s",
                            toolId(fitsReport, message.getToolId()), filePath, text);

                    if (text.contains("severity=error") || text.contains("severity=warn")) {
                        logger.warn(logMessage);
                    } else {
                        logger.info(logMessage);
                    }
                });
            }
        }
    }

    private void processMetadata(String filePath,
                                 FitsReport fitsReport,
                                 IngestObjectContext context) {
        if (fitsReport.getMetadata() != null && fitsReport.getMetadata().getText() != null) {
            var meta = fitsReport.getMetadata().getText();

            if (meta.getCharset() != null) {
                meta.getCharset().forEach(charset -> {
                    context.addFileEncoding(filePath, charset.getValue(), toolId(fitsReport, charset.getToolId()));
                });
            }
        }
    }

    private void validateStatusValues(String filePath,
                                      FitsReport fitsReport,
                                      List<FitsValue> values,
                                      String messageTmpl,
                                      EventLogger logger) {
        if (values != null) {
            var notWellFormed = values.stream()
                    .filter(value -> "false".equals(value.getValue()))
                    .findFirst();
            if (notWellFormed.isPresent()) {
                var toolId = toolId(fitsReport, notWellFormed.get().getToolId());
                logger.warn(messageTmpl, toolId, filePath);
            }
        }
    }

    private void validateToolStatus(String filePath,
                                    FitsReport fitsReport,
                                    EventLogger logger) {
        fitsReport.getStatistics().getTools().stream()
                .filter(tool -> tool.getStatus() == ToolStatus.FAILED)
                .forEach(tool -> {
                    logger.warn("Failed to run %s for file %s",
                            toolId(fitsReport, tool.getToolId()), filePath);
                });
    }

    private FitsReport parseFitsReport(Path outputPath, String filePath) {
        var fitsReportPath = outputPath.resolve(filePath + FITS_SUFFIX);
        try {
            return xmlMapper.readValue(fitsReportPath.toFile(), FitsReport.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private Collection<Status> collectStatuses(FileStatus original) {
        var statusMap = new HashMap<String, Status>();

        if (original.getValid() != null) {
            original.getValid().forEach(validity -> {
                var status = statusMap.computeIfAbsent(validity.getToolId(), Status::new);
                status.valid = Boolean.valueOf(validity.getValue());
            });
        }

        if (original.getWellFormed() != null) {
            original.getWellFormed().forEach(wellFormed -> {
                var status = statusMap.computeIfAbsent(wellFormed.getToolId(), Status::new);
                status.wellFormed = Boolean.valueOf(wellFormed.getValue());
            });
        }

        return statusMap.values();
    }

    private String toolId(FitsReport fitsReport, String toolId) {
        return fitsReport.getId() + ":" + toolId;
    }

    private static class Status {
        String toolId;
        Boolean valid;
        Boolean wellFormed;

        Status(String toolId) {
            this.toolId = toolId;
        }
    }

}
