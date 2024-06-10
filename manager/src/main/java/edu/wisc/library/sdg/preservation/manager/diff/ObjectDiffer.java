package edu.wisc.library.sdg.preservation.manager.diff;

import edu.wisc.library.sdg.preservation.common.util.ArgCheck;
import edu.wisc.library.sdg.preservation.manager.db.model.IngestBatchObjectFileComposite;
import edu.wisc.library.sdg.preservation.manager.db.model.IngestBatchObjectFileEncodingComposite;
import edu.wisc.library.sdg.preservation.manager.db.model.IngestBatchObjectFileFormatComposite;
import edu.wisc.library.sdg.preservation.manager.db.model.IngestBatchObjectFileValidity;
import edu.wisc.library.sdg.preservation.manager.db.model.IngestBatchObjectFileValidityComposite;
import edu.wisc.library.sdg.preservation.manager.db.model.PreservationObjectFileEncodingComposite;
import edu.wisc.library.sdg.preservation.manager.db.model.PreservationObjectFileFormatComposite;
import edu.wisc.library.sdg.preservation.manager.db.model.PreservationObjectFileValidity;
import edu.wisc.library.sdg.preservation.manager.db.model.PreservationObjectFileValidityComposite;
import edu.wisc.library.sdg.preservation.manager.db.model.PreservationObjectVersionFileComposite;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Diffs objects in ingest batches against preserved objects
 */
@Component
public class ObjectDiffer {

    /**
     * Diffs an ingest batch object against a preserved object
     *
     * @param newFiles files in the inget batch object
     * @param currentFiles files in the preserved object
     * @return the diff
     */
    public ObjectDiff diffObject(List<IngestBatchObjectFileComposite> newFiles,
                                 List<PreservationObjectVersionFileComposite> currentFiles) {
        ArgCheck.notNull(newFiles, "newFiles");
        ArgCheck.notNull(currentFiles, "currentFiles");

        var diff = new ObjectDiff();

        var newFilesMap = newFiles.stream()
                .collect(Collectors.toMap(IngestBatchObjectFileComposite::getFilePath, Function.identity()));
        var currentFilesMap = currentFiles.stream()
                .collect(Collectors.toMap(PreservationObjectVersionFileComposite::getFilePath, Function.identity()));

        newFilesMap.forEach((path, newFile) -> {
            var currentFile = currentFilesMap.get(path);

            if (currentFile == null) {
                diff.fileAdded(path, newFile.getSha256Digest())
                        .formatDiff(diffFormats(newFile.getFormats(), null))
                        .encodingDiff(diffEncodings(newFile.getEncoding(), null))
                        .validityDiff(diffValidities(newFile.getValidity(), null));
            } else {
                if (Objects.equals(newFile.getSha256Digest(), currentFile.getSha256Digest())) {
                    diff.fileUnchanged(path, newFile.getSha256Digest())
                            .formatDiff(diffFormats(newFile.getFormats(), currentFile.getFormats()))
                            .encodingDiff(diffEncodings(newFile.getEncoding(), currentFile.getEncoding()))
                            .validityDiff(diffValidities(newFile.getValidity(), currentFile.getValidity()));
                } else {
                    diff.fileModified(path, newFile.getSha256Digest(), currentFile.getSha256Digest())
                            .formatDiff(diffFormats(newFile.getFormats(), currentFile.getFormats()))
                            .encodingDiff(diffEncodings(newFile.getEncoding(), currentFile.getEncoding()))
                            .validityDiff(diffValidities(newFile.getValidity(), currentFile.getValidity()));
                }
            }
        });

        currentFilesMap.forEach((path, currentFile) -> {
            if (!newFilesMap.containsKey(path)) {
                diff.fileRemoved(path, currentFile.getSha256Digest())
                        .formatDiff(diffFormats(null, currentFile.getFormats()))
                        .encodingDiff(diffEncodings(null, currentFile.getEncoding()))
                        .validityDiff(diffValidities(null, currentFile.getValidity()));
            }
        });

        return diff;
    }

    private ObjectDiff.PropertyDiff<ObjectDiff.Format> diffFormats(
            List<IngestBatchObjectFileFormatComposite> newFormats,
            List<PreservationObjectFileFormatComposite> currentFormats) {
        var diff = new ObjectDiff.PropertyDiff<ObjectDiff.Format>();

        if (newFormats == null) {
            currentFormats.stream().map(format -> {
                return new ObjectDiff.Format(format.getFormat(), format.getFormatRegistry());
            }).forEach(diff::removed);
        } else if (currentFormats == null) {
            newFormats.stream().map(format -> {
                return new ObjectDiff.Format(format.getFormat(), format.getFormatRegistry());
            }).forEach(diff::added);
        } else {
            newFormats.forEach(newFormat -> {
                var exists = currentFormats.stream().anyMatch(currentFormat -> {
                    return Objects.equals(newFormat.getFormatRegistry(), currentFormat.getFormatRegistry())
                            && Objects.equals(newFormat.getFormat(), currentFormat.getFormat());
                });

                var format = new ObjectDiff.Format(newFormat.getFormat(), newFormat.getFormatRegistry());

                if (exists) {
                    diff.unchanged(format);
                } else {
                    diff.added(format);
                }
            });

            currentFormats.forEach(currentFormat -> {
                var exists = newFormats.stream().anyMatch(newFormat -> {
                    return Objects.equals(newFormat.getFormatRegistry(), currentFormat.getFormatRegistry())
                            && Objects.equals(newFormat.getFormat(), currentFormat.getFormat());
                });

                if (!exists) {
                    diff.removed(new ObjectDiff.Format(currentFormat.getFormat(), currentFormat.getFormatRegistry()));
                }
            });
        }

        return diff;
    }

    private ObjectDiff.PropertyDiff<String> diffEncodings(
            List<IngestBatchObjectFileEncodingComposite> newEncodings,
            List<PreservationObjectFileEncodingComposite> currentEncodings) {
        var diff = new ObjectDiff.PropertyDiff<String>();

        if (newEncodings == null) {
            currentEncodings.stream().map(PreservationObjectFileEncodingComposite::getEncoding)
                    .forEach(diff::removed);
        } else if (currentEncodings == null) {
            newEncodings.stream().map(IngestBatchObjectFileEncodingComposite::getEncoding)
                    .forEach(diff::added);
        } else {
            newEncodings.forEach(newEncoding -> {
                var exists = currentEncodings.stream().anyMatch(currentEncoding -> {
                    return Objects.equals(newEncoding.getEncoding(), currentEncoding.getEncoding());
                });

                if (exists) {
                    diff.unchanged(newEncoding.getEncoding());
                } else {
                    diff.added(newEncoding.getEncoding());
                }
            });

            currentEncodings.forEach(currentEncoding -> {
                var exists = newEncodings.stream().anyMatch(newEncoding -> {
                    return Objects.equals(newEncoding.getEncoding(), currentEncoding.getEncoding());
                });

                if (!exists) {
                    diff.removed(currentEncoding.getEncoding());
                }
            });
        }

        return diff;
    }

    private ObjectDiff.PropertyDiff<ObjectDiff.Validity> diffValidities(
            List<IngestBatchObjectFileValidityComposite> newValidities,
            List<PreservationObjectFileValidityComposite> currentValidities) {
        var diff = new ObjectDiff.PropertyDiff<ObjectDiff.Validity>();

        if (newValidities == null) {
            currentValidities.stream().map(validity -> {
                return new ObjectDiff.Validity(validity.getValid(), validity.getWellFormed());
            }).forEach(diff::removed);
        } else if (currentValidities == null) {
            newValidities.stream().map(validity -> {
                return new ObjectDiff.Validity(validity.getValid(), validity.getWellFormed());
            }).forEach(diff::added);
        } else {
            newValidities.forEach(newValidity -> {
                var exists = currentValidities.stream().anyMatch(currentValidity -> {
                    return Objects.equals(newValidity.getValid(), currentValidity.getValid())
                            && Objects.equals(newValidity.getWellFormed(), currentValidity.getWellFormed());
                });

                var format = new ObjectDiff.Validity(newValidity.getValid(), newValidity.getWellFormed());

                if (exists) {
                    diff.unchanged(format);
                } else {
                    diff.added(format);
                }
            });

            currentValidities.forEach(currentValidity -> {
                var exists = newValidities.stream().anyMatch(newValidity -> {
                    return Objects.equals(newValidity.getValid(), currentValidity.getValid())
                            && Objects.equals(newValidity.getWellFormed(), currentValidity.getWellFormed());
                });

                if (!exists) {
                    diff.removed(new ObjectDiff.Validity(currentValidity.getValid(), currentValidity.getWellFormed()));
                }
            });
        }

        return diff;
    }

}
