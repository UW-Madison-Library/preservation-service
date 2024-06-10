package edu.wisc.library.sdg.preservation.manager.diff;

import edu.wisc.library.sdg.preservation.manager.db.model.FormatRegistry;
import edu.wisc.library.sdg.preservation.manager.db.model.IngestBatchObjectFileComposite;
import edu.wisc.library.sdg.preservation.manager.db.model.IngestBatchObjectFileEncodingComposite;
import edu.wisc.library.sdg.preservation.manager.db.model.IngestBatchObjectFileFormatComposite;
import edu.wisc.library.sdg.preservation.manager.db.model.IngestBatchObjectFileValidityComposite;
import edu.wisc.library.sdg.preservation.manager.db.model.PreservationObjectFileEncodingComposite;
import edu.wisc.library.sdg.preservation.manager.db.model.PreservationObjectFileFormatComposite;
import edu.wisc.library.sdg.preservation.manager.db.model.PreservationObjectFileValidityComposite;
import edu.wisc.library.sdg.preservation.manager.db.model.PreservationObjectVersionFileComposite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ObjectDifferTest {

    private ObjectDiffer differ;

    @BeforeEach
    public void setup() {
        differ = new ObjectDiffer();
    }

    @Test
    public void objectAllAdds() {
        var newFiles = List.of(
                batchFile("file2.txt", "b",
                        List.of(batchFormat("text/plain", FormatRegistry.MIME)),
                        List.of(batchEncoding("UTF-8")),
                        List.of(batchValidity(true, true))),
                batchFile("file1.txt", "a",
                        List.of(batchFormat("text/plain", FormatRegistry.MIME)),
                        List.of(batchEncoding("UTF-8")),
                        List.of(batchValidity(true, true)))
        );
        var currentFiles = List.of(
                preservedFile("file1.txt", "a",
                        List.of(preservedFormat("text/plain", FormatRegistry.MIME)),
                        List.of(preservedEncoding("UTF-8")),
                        List.of(preservedValidity(true, true)))
        );

        var diff = differ.diffObject(newFiles, currentFiles);

        assertTrue(diff.hasChanges());
        assertThat(diff.getFiles(), containsInAnyOrder(
                new ObjectDiff.FileDiff("file2.txt", "b", null, ObjectDiff.DiffType.ADDED)
                        .formatDiff(propDiff(
                                List.of(new ObjectDiff.Format("text/plain", FormatRegistry.MIME)),
                                List.of(),
                                List.of()))
                        .encodingDiff(propDiff(List.of("UTF-8"), List.of(), List.of()))
                        .validityDiff(propDiff(List.of(new ObjectDiff.Validity(true, true)), List.of(), List.of())),
                new ObjectDiff.FileDiff("file1.txt", "a", "a", ObjectDiff.DiffType.UNCHANGED)
                        .formatDiff(propDiff(
                                List.of(),
                                List.of(),
                                List.of(new ObjectDiff.Format("text/plain", FormatRegistry.MIME))))
                        .encodingDiff(propDiff(List.of(), List.of(), List.of("UTF-8")))
                        .validityDiff(propDiff(List.of(), List.of(), List.of(new ObjectDiff.Validity(true, true))))
                ));
    }

    @Test
    public void objectAllRemoved() {
        var newFiles = List.of(
                batchFile("file2.txt", "b",
                        List.of(batchFormat("text/plain", FormatRegistry.MIME)),
                        List.of(batchEncoding("UTF-8")),
                        List.of(batchValidity(true, true)))
        );
        var currentFiles = List.of(
                preservedFile("file1.txt", "a",
                        List.of(preservedFormat("text/plain", FormatRegistry.MIME)),
                        List.of(preservedEncoding("UTF-8")),
                        List.of(preservedValidity(true, true)))
        );

        var diff = differ.diffObject(newFiles, currentFiles);

        assertTrue(diff.hasChanges());
        assertThat(diff.getFiles(), containsInAnyOrder(
                new ObjectDiff.FileDiff("file2.txt", "b", null, ObjectDiff.DiffType.ADDED)
                        .formatDiff(propDiff(
                                List.of(new ObjectDiff.Format("text/plain", FormatRegistry.MIME)),
                                List.of(),
                                List.of()))
                        .encodingDiff(propDiff(List.of("UTF-8"), List.of(), List.of()))
                        .validityDiff(propDiff(List.of(new ObjectDiff.Validity(true, true)), List.of(), List.of())),
                new ObjectDiff.FileDiff("file1.txt", null, "a", ObjectDiff.DiffType.REMOVED)
                        .formatDiff(propDiff(
                                List.of(),
                                List.of(new ObjectDiff.Format("text/plain", FormatRegistry.MIME)),
                                List.of()))
                        .encodingDiff(propDiff(List.of(), List.of("UTF-8"), List.of()))
                        .validityDiff(propDiff(List.of(), List.of(new ObjectDiff.Validity(true, true)), List.of()))
                ));
    }

    @Test
    public void modifiedMixed() {
        var newFiles = List.of(
                batchFile("file1.txt", "b",
                        List.of(batchFormat("text/plain", FormatRegistry.MIME),
                                batchFormat("fmt/2", FormatRegistry.PRONOM)),
                        List.of(batchEncoding("UTF-8"), batchEncoding("ISO-8859-1")),
                        List.of(batchValidity(true, true), batchValidity(false, false)))
        );
        var currentFiles = List.of(
                preservedFile("file1.txt", "a",
                        List.of(preservedFormat("text/plain", FormatRegistry.MIME),
                                preservedFormat("fmt/1", FormatRegistry.PRONOM)),
                        List.of(preservedEncoding("UTF-8"), preservedEncoding("US-ASCII")),
                        List.of(preservedValidity(true, true), preservedValidity(false, true)))
        );

        var diff = differ.diffObject(newFiles, currentFiles);

        assertTrue(diff.hasChanges());
        assertThat(diff.getFiles(), containsInAnyOrder(
                new ObjectDiff.FileDiff("file1.txt", "b", "a", ObjectDiff.DiffType.MODIFIED)
                        .formatDiff(propDiff(
                                List.of(new ObjectDiff.Format("fmt/2", FormatRegistry.PRONOM)),
                                List.of(new ObjectDiff.Format("fmt/1", FormatRegistry.PRONOM)),
                                List.of(new ObjectDiff.Format("text/plain", FormatRegistry.MIME))))
                        .encodingDiff(propDiff(List.of("ISO-8859-1"), List.of("US-ASCII"), List.of("UTF-8")))
                        .validityDiff(propDiff(List.of(new ObjectDiff.Validity(false, false)),
                                List.of(new ObjectDiff.Validity(false, true)),
                                List.of(new ObjectDiff.Validity(true, true))))
                ));
    }

    @Test
    public void noFileChanges() {
        var newFiles = List.of(
                batchFile("file1.txt", "a",
                        List.of(batchFormat("text/plain", FormatRegistry.MIME),
                                batchFormat("fmt/2", FormatRegistry.PRONOM)),
                        List.of(batchEncoding("UTF-8"), batchEncoding("ISO-8859-1")),
                        List.of(batchValidity(true, true), batchValidity(false, false)))
        );
        var currentFiles = List.of(
                preservedFile("file1.txt", "a",
                        List.of(preservedFormat("text/plain", FormatRegistry.MIME),
                                preservedFormat("fmt/1", FormatRegistry.PRONOM)),
                        List.of(preservedEncoding("UTF-8"), preservedEncoding("US-ASCII")),
                        List.of(preservedValidity(true, true), preservedValidity(false, true)))
        );

        var diff = differ.diffObject(newFiles, currentFiles);

        assertFalse(diff.hasChanges());
        assertThat(diff.getFiles(), containsInAnyOrder(
                new ObjectDiff.FileDiff("file1.txt", "a", "a", ObjectDiff.DiffType.UNCHANGED)
                        .formatDiff(propDiff(
                                List.of(new ObjectDiff.Format("fmt/2", FormatRegistry.PRONOM)),
                                List.of(new ObjectDiff.Format("fmt/1", FormatRegistry.PRONOM)),
                                List.of(new ObjectDiff.Format("text/plain", FormatRegistry.MIME))))
                        .encodingDiff(propDiff(List.of("ISO-8859-1"), List.of("US-ASCII"), List.of("UTF-8")))
                        .validityDiff(propDiff(List.of(new ObjectDiff.Validity(false, false)),
                                List.of(new ObjectDiff.Validity(false, true)),
                                List.of(new ObjectDiff.Validity(true, true))))
                ));
    }

    private IngestBatchObjectFileComposite batchFile(String path,
                                                     String digest,
                                                     List<IngestBatchObjectFileFormatComposite> formats,
                                                     List<IngestBatchObjectFileEncodingComposite> encodings,
                                                     List<IngestBatchObjectFileValidityComposite> validities) {
        return new IngestBatchObjectFileComposite()
                .setFilePath(path)
                .setSha256Digest(digest)
                .setFormats(formats)
                .setEncoding(encodings)
                .setValidity(validities);
    }

    private IngestBatchObjectFileFormatComposite batchFormat(String format, FormatRegistry registry) {
        return new IngestBatchObjectFileFormatComposite().setFormat(format).setFormatRegistry(registry);
    }

    private IngestBatchObjectFileEncodingComposite batchEncoding(String encoding) {
        return new IngestBatchObjectFileEncodingComposite().setEncoding(encoding);
    }

    private IngestBatchObjectFileValidityComposite batchValidity(boolean valid, boolean wellFormed) {
        return new IngestBatchObjectFileValidityComposite().setValid(valid).setWellFormed(wellFormed);
    }

    private PreservationObjectVersionFileComposite preservedFile(String path,
                                                                 String digest,
                                                                 List<PreservationObjectFileFormatComposite> formats,
                                                                 List<PreservationObjectFileEncodingComposite> encodings,
                                                                 List<PreservationObjectFileValidityComposite> validities) {
        return new PreservationObjectVersionFileComposite()
                .setFilePath(path)
                .setSha256Digest(digest)
                .setFormats(formats)
                .setEncoding(encodings)
                .setValidity(validities);
    }

    private PreservationObjectFileFormatComposite preservedFormat(String format, FormatRegistry registry) {
        return new PreservationObjectFileFormatComposite().setFormat(format).setFormatRegistry(registry);
    }

    private PreservationObjectFileEncodingComposite preservedEncoding(String encoding) {
        return new PreservationObjectFileEncodingComposite().setEncoding(encoding);
    }

    private PreservationObjectFileValidityComposite preservedValidity(boolean valid, boolean wellFormed) {
        return new PreservationObjectFileValidityComposite().setValid(valid).setWellFormed(wellFormed);
    }

    private <T> ObjectDiff.PropertyDiff<T> propDiff(List<T> added,
                                                    List<T> removed,
                                                    List<T> unchanged) {
        return new ObjectDiff.PropertyDiff<T>()
                .added(added)
                .removed(removed)
                .unchanged(unchanged);
    }

}
