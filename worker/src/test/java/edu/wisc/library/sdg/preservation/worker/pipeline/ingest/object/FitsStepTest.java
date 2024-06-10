package edu.wisc.library.sdg.preservation.worker.pipeline.ingest.object;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import edu.wisc.library.sdg.preservation.common.PreservationConstants;
import edu.wisc.library.sdg.preservation.common.util.UncheckedFiles;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.EventOutcome;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.EventType;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.FormatRegistry;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.IngestBatch;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.LogEntry;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.LogLevel;
import edu.wisc.library.sdg.preservation.worker.itest.ITestBase;
import edu.wisc.library.sdg.preservation.worker.log.EventRecorder;
import edu.wisc.library.sdg.preservation.worker.pipeline.NextStep;
import edu.wisc.library.sdg.preservation.worker.pipeline.ingest.object.postprocessor.PhotoshopPropertiesFilter;
import edu.wisc.library.sdg.preservation.worker.pipeline.ingest.object.service.PronomIdentifier;
import edu.wisc.library.sdg.preservation.worker.tools.fits.Fits;
import io.micrometer.core.instrument.MeterRegistry;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
public class FitsStepTest {

    @TempDir
    Path tempDir;

    @Mock
    Fits fits;

    @Mock
    NextStep<IngestObjectContext> nextStep;

    @Mock
    ManagerInternalApi managerApi;

    @Mock
    MeterRegistry meterRegistry;

    @Mock
    PhotoshopPropertiesFilter photoshopPropertiesFilter;

    private FitsStep fitsStep;

    private IngestObjectContext context;

    private Path sourcePath;
    private Path fitsPath;

    private long serial;

    @BeforeEach
    public void setup() {
        var xmlMapper = XmlMapper.builder().defaultUseWrapper(false).build();
        var pronomIdentifier = new PronomIdentifier();
        fitsStep = new FitsStep(fits, xmlMapper, meterRegistry, true, photoshopPropertiesFilter, pronomIdentifier);

        context = new IngestObjectContext(
                new IngestBatch().vault("vault").ingestId(1L),
                "eid1",
                UncheckedFiles.createDirectories(tempDir.resolve("source")),
                UncheckedFiles.createDirectories(tempDir.resolve("work")),
                null,
                new EventRecorder(managerApi));
        context.setIngestObjectId(2L);

        sourcePath = context.getObjectSourcePath();
        fitsPath = UncheckedFiles.createDirectories(context.getObjectWorkPath()
                .resolve(PreservationConstants.OCFL_OBJECT_TECHMD_DIR_NAME)
                .resolve(PreservationConstants.OCFL_OBJECT_FITS_DIR_NAME));

        serial = 1;
    }

    @Test
    public void runsPostProcessingOnFitsReport() {
        addFile("droid-pronom.tif");
        fitsStep.execute(context, nextStep);
        verify(photoshopPropertiesFilter).process(any(), eq("droid-pronom.tif.fits.xml"));
    }

    @Test
    public void noPostProcessingOnFitsReport() {
        addFile("no-id.txt");
        fitsStep.execute(context, nextStep);
        verifyNoInteractions(photoshopPropertiesFilter);
    }

    @Test
    public void failWhenFitsFails() {
        doThrow(new RuntimeException("blah")).when(fits).execute(sourcePath, fitsPath);

        fitsStep.execute(context, nextStep);

        assertEvent(EventOutcome.FAILURE,
                List.of(logEntry(LogLevel.ERROR, "Failed to execute FITS"))
        );

        verifyNextStepCalled();
    }

    @Test
    public void failWhenFitsReportReadFailure() {
        addFile("does-not-exist.txt");

        fitsStep.execute(context, nextStep);

        assertEvent(EventOutcome.FAILURE,
                List.of(logEntry(LogLevel.ERROR, "Failed to process FITS report for file does-not-exist.txt"))
        );

        verifyNextStepCalled();
    }

    @Test
    public void continueWhenOneFileHasErrors() {
        addFile("does-not-exist.txt");

        addFile("valid.txt");

        fitsStep.execute(context, nextStep);

        assertEvent(EventOutcome.FAILURE,
                List.of(logEntry(LogLevel.ERROR, "Failed to process FITS report for file does-not-exist.txt"))
        );

        assertDetails(
                new FileDetails("does-not-exist.txt", 1L),
                new FileDetails("valid.txt", 2L)
                        .addFileFormat(FormatRegistry.MIME, ITestBase.DROID_ID, "text/plain")
                        .addFileFormat(FormatRegistry.PRONOM, ITestBase.DROID_ID, "x-fmt/111")
                        .addFileEncoding("US-ASCII", ITestBase.FILEUTIL_ID)
                        .addFileEncoding("ISO-8859-1", ITestBase.TIKA_ID)
                        .addFileValidity(true, true, ITestBase.JHOVE_ID));

        verifyNextStepCalled();
    }

    @Test
    public void warnWhenHasNoIdentification() {
        addFile("no-id.txt");

        fitsStep.execute(context, nextStep);

        assertEvent(EventOutcome.SUCCESS_WITH_WARNINGS,
                List.of(logEntry(LogLevel.WARN, "Failed to identify format for file no-id.txt"))
        );

        assertDetails(new FileDetails("no-id.txt", 1L)
                .addFileEncoding("US-ASCII", ITestBase.FILEUTIL_ID)
                .addFileEncoding("ISO-8859-1", ITestBase.TIKA_ID)
                .addFileValidity(true, true, ITestBase.JHOVE_ID));

        verifyNextStepCalled();
    }

    @Test
    public void warnWhenInvalidAndNotWellFormed() {
        // invalid.xml
        //<?xml version="1.0" encoding="UTF-8"?>
        //                                         invalid xml

        addFile("invalid.xml");

        fitsStep.execute(context, nextStep);

        assertEvent(EventOutcome.SUCCESS_WITH_WARNINGS, List.of(
                logEntry(LogLevel.WARN, ITestBase.JHOVE_ID + " reported that invalid.xml is not well-formed"),
                logEntry(LogLevel.WARN, ITestBase.JHOVE_ID + " reported that invalid.xml is not valid"),
                logEntry(LogLevel.WARN, ITestBase.JHOVE_ID + " reported for file invalid.xml: SAXParseException Content is not allowed in prolog. Line = 2, Column = 42. severity=error"),
                logEntry(LogLevel.WARN, "Failed to run " + ITestBase.FITS_ID + ":OIS XML Metadata-0.2 for file invalid.xml")
                )
        );

        assertDetails(new FileDetails("invalid.xml", 1L)
                .addFileFormat(FormatRegistry.MIME, ITestBase.DROID_ID, "text/xml")
                .addFileFormat(FormatRegistry.PRONOM, ITestBase.DROID_ID, "fmt/101")
                .addFileValidity(false, false, ITestBase.JHOVE_ID));

        verifyNextStepCalled();
    }

    @Test
    public void addPronomFromDroid() {
        addFile("droid-pronom.tif");

        fitsStep.execute(context, nextStep);

        assertDetails(new FileDetails("droid-pronom.tif", 1L)
                .addFileFormat(FormatRegistry.MIME, ITestBase.EXIFTOOL_ID, "image/tiff")
                .addFileFormat(FormatRegistry.PRONOM, ITestBase.DROID_ID, "fmt/353"));

        verifyNextStepCalled();
    }

    @Test
    public void psWorkerAddsXmlPronom() {
        // Fedora3 DC Datastreams should be used to generate output with no PRONOM
        addFile("no-pronom.xml");

        fitsStep.execute(context, nextStep);

        assertDetails(new FileDetails("no-pronom.xml", 1L)
                .addFileFormat(FormatRegistry.MIME, ITestBase.TIKA_ID, "text/xml")
                .addFileFormat(FormatRegistry.PRONOM, ITestBase.PRES_WORKER_ID, "fmt/101")
                .addFileEncoding("UTF-8", ITestBase.JHOVE_ID)
                .addFileValidity(true, true, ITestBase.JHOVE_ID));

        verifyNextStepCalled();
    }

    @Test
    public void psWorkerDoesNotAddPronom() {
        addFile("invalid.xml");

        fitsStep.execute(context, nextStep);

        assertDetails(new FileDetails("invalid.xml", 1L)
                .addFileFormat(FormatRegistry.MIME, ITestBase.DROID_ID, "text/xml")
                .addFileFormat(FormatRegistry.PRONOM, ITestBase.DROID_ID, "fmt/101")
                .addFileValidity(false, false, ITestBase.JHOVE_ID));

        verifyNextStepCalled();
    }

    @Test
    public void psWorkerAddsAifPronom() {
        addFile("no-pronom.aif");

        fitsStep.execute(context, nextStep);

        assertDetails(new FileDetails("no-pronom.aif", 1L)
                .addFileFormat(FormatRegistry.MIME, ITestBase.JHOVE_ID, "audio/x-aiff")
                .addFileFormat(FormatRegistry.PRONOM, ITestBase.PRES_WORKER_ID, "fmt/414")
                .addFileValidity(true, true, ITestBase.JHOVE_ID));

        verifyNextStepCalled();
    }

    @Test
    public void psWorkerAddsHtmlPronom() {
        addFile("no-pronom.html");

        fitsStep.execute(context, nextStep);

        assertDetails(new FileDetails("no-pronom.html", 1L)
                .addFileFormat(FormatRegistry.MIME, ITestBase.TIKA_ID, "text/html")
                .addFileFormat(FormatRegistry.PRONOM, ITestBase.PRES_WORKER_ID, "fmt/100")
                .addFileEncoding("UTF-8", ITestBase.FILEUTIL_ID)
                .addFileValidity(true, true, ITestBase.JHOVE_ID));

        verifyNextStepCalled();
    }

    private void addFile(String fileName) {
        context.addFile(fileName, serial++);
        var report = Paths.get(String.format("src/test/resources/fits/%s.fits.xml", fileName));
        if (Files.exists(report)) {
            try {
                Files.copy(report, fitsPath.resolve(report.getFileName()));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }

    protected LogEntry logEntry(LogLevel level, String message) {
        return new LogEntry().level(level).message(message);
    }

    private void assertEvent(EventOutcome outcome, List<LogEntry> logs) {
        verify(managerApi).recordIngestEvent(argThat(arg -> {
            var match = Objects.equals(1L, arg.getIngestId())
                    && Objects.equals("eid1", arg.getExternalObjectId());

            if (!match) {
                return false;
            }

            var event = arg.getEvent();

            match = Objects.equals(EventType.IDENTIFY_FILE_FORMAT, event.getType())
                    && Objects.equals(outcome, event.getOutcome());

            if (!match) {
                return false;
            }

            if (CollectionUtils.isNotEmpty(event.getLogs())) {
                event.getLogs().forEach(log -> log.setCreatedTimestamp(null));
                return logs.containsAll(event.getLogs());
            } else {
                return CollectionUtils.isEmpty(logs);
            }
        }));
    }

    private void assertDetails(FileDetails... expected) {
        var actual = context.getFileDetails().stream()
                .map(FileDetails::toRequest)
                .collect(Collectors.toList());

        var expectedTransformed = Arrays.stream(expected)
                .map(FileDetails::toRequest)
                .toArray();

        assertThat(actual, containsInAnyOrder(expectedTransformed));
    }

    private void verifyNextStepCalled() {
        verify(nextStep).execute();
    }

}
