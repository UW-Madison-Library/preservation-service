package edu.wisc.library.sdg.preservation.worker.pipeline.ingest.object;

import edu.wisc.library.sdg.preservation.common.util.ArgCheck;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.FileEncoding;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.FileFormat;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.FileValidity;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.FormatRegistry;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RegisterIngestObjectFileDetailsRequest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FileDetails {

    private final String filePath;
    private final Long ingestFileId;
    private String sha256Digest;
    private Long fileSize;
    private List<FileFormat> fileFormats;
    private List<FileEncoding> fileEncodings;
    private List<FileValidity> fileValidity;
    private Set<String> formatKeys;

    public FileDetails(String filePath, Long ingestFileId) {
        this.filePath = ArgCheck.notBlank(filePath, "filePath");
        this.ingestFileId = ArgCheck.notNull(ingestFileId, "ingestFileId");
        this.fileFormats = new ArrayList<>();
        this.fileEncodings = new ArrayList<>();
        this.fileValidity = new ArrayList<>();
        this.formatKeys = new HashSet<>();
    }

    /**
     * Adds the format to the file details IF the format has not already been registered to the file
     */
    public FileDetails addFileFormat(FormatRegistry registry, String source, String format) {
        var key = registry + ":" + format;

        if (!formatKeys.contains(key)) {
            fileFormats.add(new FileFormat()
                    .formatRegistry(registry)
                    .source(source)
                    .format(format));
        }

        return this;
    }

    public FileDetails addFileEncoding(String encoding, String source) {
        fileEncodings.add(new FileEncoding()
                .encoding(encoding)
                .source(source));
        return this;
    }

    public FileDetails addFileValidity(Boolean valid, Boolean wellFormed, String source) {
        fileValidity.add(new FileValidity()
                .valid(valid)
                .wellFormed(wellFormed)
                .source(source));
        return this;
    }

    public void setSha256Digest(String sha256Digest) {
        this.sha256Digest = ArgCheck.notBlank(sha256Digest, "sha256Digest");
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = ArgCheck.notNull(fileSize, "fileSize");
    }

    public String getFilePath() {
        return filePath;
    }

    public Long getIngestFileId() {
        return ingestFileId;
    }

    public String getSha256Digest() {
        return sha256Digest;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public List<FileFormat> getFileFormats() {
        return fileFormats;
    }

    public List<FileEncoding> getFileEncodings() {
        return fileEncodings;
    }

    public List<FileValidity> getFileValidity() {
        return fileValidity;
    }

    public RegisterIngestObjectFileDetailsRequest toRequest() {
        var request = new RegisterIngestObjectFileDetailsRequest();
        request.ingestFileId(ingestFileId)
                .sha256Digest(sha256Digest)
                .fileSize(fileSize)
                .formats(fileFormats)
                .encoding(fileEncodings)
                .validity(fileValidity);
        return request;
    }

    @Override
    public String toString() {
        return "FileDetails{" +
                "filePath='" + filePath + '\'' +
                ", ingestFileId=" + ingestFileId +
                ", sha256Digest='" + sha256Digest + '\'' +
                ", fileSize=" + fileSize +
                ", fileFormats=" + fileFormats +
                ", fileEncodings=" + fileEncodings +
                ", fileValidity=" + fileValidity +
                '}';
    }

}
