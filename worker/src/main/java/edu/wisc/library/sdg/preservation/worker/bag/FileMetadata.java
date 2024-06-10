package edu.wisc.library.sdg.preservation.worker.bag;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import edu.wisc.library.sdg.preservation.common.util.ArgCheck;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * User provided metadata about files in a bag
 */
@JsonPropertyOrder({
        "objectId",
        "filePath",
        "mimeTypes",
        "pronomIds",
        "encodings",
})

public class FileMetadata {

    private final String objectId;
    private final String filePath;
    private final Set<String> mimeTypes;
    private final Set<String> pronomIds;
    private final Set<String> encodings;

    @JsonCreator
    public FileMetadata(
            @JsonProperty("objectId") String objectId,
            @JsonProperty("filePath") String filePath,
            @JsonProperty("mimeTypes") List<String> mimeTypes,
            @JsonProperty("pronomIds") List<String> pronomIds,
            @JsonProperty("encodings") List<String> encodings) {
        this.objectId = ArgCheck.notBlank(objectId, "objectId");
        this.filePath = ArgCheck.notBlank(filePath, "filePath");
        this.mimeTypes = mimeTypes == null ? Collections.emptySet() : new HashSet<>(mimeTypes);
        this.pronomIds = pronomIds == null ? Collections.emptySet() : new HashSet<>(pronomIds);
        this.encodings = encodings == null ? Collections.emptySet() : new HashSet<>(encodings);
    }

    public String getObjectId() {
        return objectId;
    }

    public String getFilePath() {
        return filePath;
    }

    public Set<String> getMimeTypes() {
        return mimeTypes;
    }

    public Set<String> getPronomIds() {
        return pronomIds;
    }

    public Set<String> getEncodings() {
        return encodings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileMetadata that = (FileMetadata) o;
        return Objects.equals(objectId, that.objectId)
                && Objects.equals(filePath, that.filePath)
                && Objects.equals(mimeTypes, that.mimeTypes)
                && Objects.equals(pronomIds, that.pronomIds)
                && Objects.equals(encodings, that.encodings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectId, filePath, mimeTypes, pronomIds, encodings);
    }

    @Override
    public String toString() {
        return "FileMetadata{" +
                "objectId='" + objectId + '\'' +
                ", filePath='" + filePath + '\'' +
                ", mimeTypes=" + mimeTypes +
                ", pronomIds=" + pronomIds +
                ", encodings=" + encodings +
                '}';
    }
}
