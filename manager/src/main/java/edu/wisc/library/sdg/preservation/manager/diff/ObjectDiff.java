package edu.wisc.library.sdg.preservation.manager.diff;

import edu.wisc.library.sdg.preservation.manager.db.model.FormatRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Describes the differences between two versions of the same object
 */
public class ObjectDiff {

    private boolean hasChanges = false;
    private final List<FileDiff> files;

    public ObjectDiff() {
        files = new ArrayList<>();
    }

    public FileDiff fileAdded(String path, String sha256Digest) {
        hasChanges = true;
        var diff = new FileDiff(path, sha256Digest, null, DiffType.ADDED);
        files.add(diff);
        return diff;
    }

    public FileDiff fileRemoved(String path, String sha256Digest) {
        hasChanges = true;
        var diff = new FileDiff(path, null, sha256Digest, DiffType.REMOVED);
        files.add(diff);
        return diff;
    }

    public FileDiff fileModified(String path, String newSha256Digest, String currentSha256Digest) {
        hasChanges = true;
        var diff = new FileDiff(path, newSha256Digest, currentSha256Digest, DiffType.MODIFIED);
        files.add(diff);
        return diff;
    }

    public FileDiff fileUnchanged(String path, String sha256Digest) {
        var diff = new FileDiff(path, sha256Digest, sha256Digest, DiffType.UNCHANGED);
        files.add(diff);
        return diff;
    }

    /**
     * @return the union of all of the files in both versions
     */
    public List<FileDiff> getFiles() {
        return files;
    }

    /**
     * @return true if there are any file level changes
     */
    public boolean hasChanges() {
        return hasChanges;
    }

    @Override
    public String toString() {
        return "ObjectDiff{" +
                "hasChanges=" + hasChanges +
                ", files=" + files +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ObjectDiff that = (ObjectDiff) o;
        return Objects.equals(files, that.files);
    }

    @Override
    public int hashCode() {
        return Objects.hash(files);
    }

    public enum DiffType {
        ADDED,
        REMOVED,
        MODIFIED,
        UNCHANGED,
    }

    /**
     * The file level diff between files at the same path
     */
    public static class FileDiff {
        private final String path;
        private final String newSha256Digest;
        private final String oldSha256Digest;
        private final DiffType diff;
        private PropertyDiff<Format> formatDiff;
        private PropertyDiff<String> encodingDiff;
        private PropertyDiff<Validity> validityDiff;

        public FileDiff(String path,
                        String newSha256Digest,
                        String oldSha256Digest,
                        DiffType diff) {
            this.path = path;
            this.newSha256Digest = newSha256Digest;
            this.oldSha256Digest = oldSha256Digest;
            this.diff = diff;
        }

        public FileDiff formatDiff(PropertyDiff<Format> formatDiff) {
            this.formatDiff = formatDiff;
            return this;
        }

        public FileDiff encodingDiff(PropertyDiff<String> encodingDiff) {
            this.encodingDiff = encodingDiff;
            return this;
        }

        public FileDiff validityDiff(PropertyDiff<Validity> validityDiff) {
            this.validityDiff = validityDiff;
            return this;
        }

        /**
         * @return the object relative path to the file
         */
        public String getPath() {
            return path;
        }

        /**
         * @return the sha256 digest of the file in the newer version or null if the file was removed
         */
        public String getNewSha256Digest() {
            return newSha256Digest;
        }

        /**
         * @return the sha256 digest of the file in the older version or null if the file was added
         */
        public String getOldSha256Digest() {
            return oldSha256Digest;
        }

        /**
         * @return if the file was added, removed, modified, or unchanged
         */
        public DiffType getDiff() {
            return diff;
        }

        /**
         * @return format differences
         */
        public PropertyDiff<Format> getFormatDiff() {
            return formatDiff;
        }

        /**
         * @return encoding differences
         */
        public PropertyDiff<String> getEncodingDiff() {
            return encodingDiff;
        }

        /**
         * @return validity differences
         */
        public PropertyDiff<Validity> getValidityDiff() {
            return validityDiff;
        }

        @Override
        public String toString() {
            return "FileDiff{" +
                    "path='" + path + '\'' +
                    ", newSha256Digest='" + newSha256Digest + '\'' +
                    ", existingSha256Digest='" + oldSha256Digest + '\'' +
                    ", diff=" + diff +
                    ", formatDiff=" + formatDiff +
                    ", encodingDiff=" + encodingDiff +
                    ", validityDiff=" + validityDiff +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FileDiff fileDiff = (FileDiff) o;
            return Objects.equals(path, fileDiff.path) && Objects.equals(newSha256Digest, fileDiff.newSha256Digest)
                    && Objects.equals(oldSha256Digest, fileDiff.oldSha256Digest)
                    && diff == fileDiff.diff
                    && Objects.equals(formatDiff, fileDiff.formatDiff)
                    && Objects.equals(encodingDiff, fileDiff.encodingDiff)
                    && Objects.equals(validityDiff, fileDiff.validityDiff);
        }

        @Override
        public int hashCode() {
            return Objects.hash(path, newSha256Digest, oldSha256Digest, diff, formatDiff, encodingDiff, validityDiff);
        }
    }

    /**
     * The differences in properties associated to the files
     */
    public static class PropertyDiff<T> {
        private final List<T> added;
        private final List<T> removed;
        private final List<T> unchanged;

        public PropertyDiff() {
            added = new ArrayList<>();
            removed = new ArrayList<>();
            unchanged = new ArrayList<>();
        }

        public PropertyDiff<T> added(T value) {
            if (!added.contains(value)) {
                added.add(value);
            }
            return this;
        }

        public PropertyDiff<T> removed(T value) {
            if (!removed.contains(value)) {
                removed.add(value);
            }
            return this;
        }

        public PropertyDiff<T> unchanged(T value) {
            if (!unchanged.contains(value)) {
                unchanged.add(value);
            }
            return this;
        }

        public PropertyDiff<T> added(List<T> value) {
            value.forEach(this::added);
            return this;
        }

        public PropertyDiff<T> removed(List<T> value) {
            value.forEach(this::removed);
            return this;
        }

        public PropertyDiff<T> unchanged(List<T> value) {
            value.forEach(this::unchanged);
            return this;
        }

        /**
         * @return properties that were added in the new version
         */
        public List<T> getAdded() {
            return added;
        }

        /**
         * @return properties that were removed in the new version
         */
        public List<T> getRemoved() {
            return removed;
        }

        /**
         * @return properties that were unchanged between versions
         */
        public List<T> getUnchanged() {
            return unchanged;
        }

        @Override
        public String toString() {
            return "PropertyDiff{" +
                    "added=" + added +
                    ", removed=" + removed +
                    ", unchanged=" + unchanged +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PropertyDiff<?> that = (PropertyDiff<?>) o;
            return Objects.equals(added, that.added) && Objects.equals(removed, that.removed) && Objects.equals(unchanged, that.unchanged);
        }

        @Override
        public int hashCode() {
            return Objects.hash(added, removed, unchanged);
        }
    }

    public static class Format {
        private final String format;
        private final FormatRegistry registry;

        public Format(String format, FormatRegistry registry) {
            this.format = format;
            this.registry = registry;
        }

        public String getFormat() {
            return format;
        }

        public FormatRegistry getRegistry() {
            return registry;
        }

        @Override
        public String toString() {
            return "Format{" +
                    "format='" + format + '\'' +
                    ", registry=" + registry +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Format format1 = (Format) o;
            return Objects.equals(format, format1.format) && registry == format1.registry;
        }

        @Override
        public int hashCode() {
            return Objects.hash(format, registry);
        }
    }

    public static class Validity {
        private final Boolean valid;
        private final Boolean wellFormed;

        public Validity(Boolean valid, Boolean wellFormed) {
            this.valid = valid;
            this.wellFormed = wellFormed;
        }

        public Boolean getValid() {
            return valid;
        }

        public Boolean getWellFormed() {
            return wellFormed;
        }

        @Override
        public String toString() {
            return "Validity{" +
                    "valid=" + valid +
                    ", wellFormed=" + wellFormed +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Validity validity = (Validity) o;
            return Objects.equals(valid, validity.valid) && Objects.equals(wellFormed, validity.wellFormed);
        }

        @Override
        public int hashCode() {
            return Objects.hash(valid, wellFormed);
        }
    }
}
