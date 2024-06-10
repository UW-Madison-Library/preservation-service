package edu.wisc.library.sdg.preservation.manager.db.model;

import java.util.Collections;
import java.util.List;

/**
 * Same as IngestBatchObjectFile except it contains IngestBatchObjectFileFormatComposite
 */
public class IngestBatchObjectFileComposite {

    private Long ingestFileId;
    private Long ingestObjectId;
    private String filePath;
    private String sha256Digest;
    private Long fileSize;
    private List<IngestBatchObjectFileFormatComposite> formats;
    private List<IngestBatchObjectFileEncodingComposite> encoding;
    private List<IngestBatchObjectFileValidityComposite> validity;

    public IngestBatchObjectFileComposite() {
        formats = Collections.emptyList();
        encoding = Collections.emptyList();
        validity = Collections.emptyList();
    }

    public IngestBatchObjectFileComposite(IngestBatchObjectFile file,
                                          List<IngestBatchObjectFileFormatComposite> formats,
                                          List<IngestBatchObjectFileEncodingComposite> encoding,
                                          List<IngestBatchObjectFileValidityComposite> validity) {
        this.ingestFileId = file.getIngestFileId();
        this.ingestObjectId = file.getIngestObjectId();
        this.filePath = file.getFilePath();
        this.sha256Digest = file.getSha256Digest();
        this.fileSize = file.getFileSize();
        this.validity = validity == null ? Collections.emptyList() : validity;
        this.formats = formats == null ? Collections.emptyList() : formats;
        this.encoding = encoding == null ? Collections.emptyList() : encoding;
    }

    public Long getIngestFileId() {
        return ingestFileId;
    }

    public IngestBatchObjectFileComposite setIngestFileId(Long ingestFileId) {
        this.ingestFileId = ingestFileId;
        return this;
    }

    public Long getIngestObjectId() {
        return ingestObjectId;
    }

    public IngestBatchObjectFileComposite setIngestObjectId(Long ingestObjectId) {
        this.ingestObjectId = ingestObjectId;
        return this;
    }

    public String getFilePath() {
        return filePath;
    }

    public IngestBatchObjectFileComposite setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public String getSha256Digest() {
        return sha256Digest;
    }

    public IngestBatchObjectFileComposite setSha256Digest(String sha256Digest) {
        this.sha256Digest = sha256Digest;
        return this;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public IngestBatchObjectFileComposite setFileSize(Long fileSize) {
        this.fileSize = fileSize;
        return this;
    }

    public List<IngestBatchObjectFileFormatComposite> getFormats() {
        return formats;
    }

    public IngestBatchObjectFileComposite setFormats(List<IngestBatchObjectFileFormatComposite> formats) {
        this.formats = formats == null ? Collections.emptyList() : formats;
        return this;
    }

    public List<IngestBatchObjectFileEncodingComposite> getEncoding() {
        return encoding;
    }

    public IngestBatchObjectFileComposite setEncoding(List<IngestBatchObjectFileEncodingComposite> encoding) {
        this.encoding = encoding == null ? Collections.emptyList() : encoding;
        return this;
    }

    public List<IngestBatchObjectFileValidityComposite> getValidity() {
        return validity;
    }

    public IngestBatchObjectFileComposite setValidity(List<IngestBatchObjectFileValidityComposite> validity) {
        this.validity = validity == null ? Collections.emptyList() : validity;
        return this;
    }

    @Override
    public String toString() {
        return "IngestBatchObjectFileComposite{" +
                "ingestFileId=" + ingestFileId +
                ", ingestObjectId=" + ingestObjectId +
                ", objectRelativePath='" + filePath + '\'' +
                ", sha256Digest='" + sha256Digest + '\'' +
                ", fileSize='" + fileSize + '\'' +
                ", formats=" + formats +
                ", encoding=" + encoding +
                ", validity=" + validity +
                '}';
    }

}
