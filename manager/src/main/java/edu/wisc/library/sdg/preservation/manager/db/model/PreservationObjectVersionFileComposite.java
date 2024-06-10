package edu.wisc.library.sdg.preservation.manager.db.model;

import java.time.LocalDateTime;
import java.util.List;

public class PreservationObjectVersionFileComposite {

    private Long objectFileId;
    private Long objectVersionFileId;

    private String sha256Digest;
    private Long fileSize;

    private String filePath;

    private LocalDateTime createdTimestamp;

    private List<PreservationObjectFileFormatComposite> formats;
    private List<PreservationObjectFileEncodingComposite> encoding;
    private List<PreservationObjectFileValidityComposite> validity;

    public Long getObjectFileId() {
        return objectFileId;
    }

    public PreservationObjectVersionFileComposite setObjectFileId(Long objectFileId) {
        this.objectFileId = objectFileId;
        return this;
    }

    public Long getObjectVersionFileId() {
        return objectVersionFileId;
    }

    public PreservationObjectVersionFileComposite setObjectVersionFileId(Long objectVersionFileId) {
        this.objectVersionFileId = objectVersionFileId;
        return this;
    }

    public String getSha256Digest() {
        return sha256Digest;
    }

    public PreservationObjectVersionFileComposite setSha256Digest(String sha256Digest) {
        this.sha256Digest = sha256Digest;
        return this;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public PreservationObjectVersionFileComposite setFileSize(Long fileSize) {
        this.fileSize = fileSize;
        return this;
    }

    public String getFilePath() {
        return filePath;
    }

    public PreservationObjectVersionFileComposite setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public LocalDateTime getCreatedTimestamp() {
        return createdTimestamp;
    }

    public PreservationObjectVersionFileComposite setCreatedTimestamp(LocalDateTime createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
        return this;
    }

    public List<PreservationObjectFileFormatComposite> getFormats() {
        return formats;
    }

    public PreservationObjectVersionFileComposite setFormats(List<PreservationObjectFileFormatComposite> formats) {
        this.formats = formats;
        return this;
    }

    public List<PreservationObjectFileEncodingComposite> getEncoding() {
        return encoding;
    }

    public PreservationObjectVersionFileComposite setEncoding(List<PreservationObjectFileEncodingComposite> encoding) {
        this.encoding = encoding;
        return this;
    }

    public List<PreservationObjectFileValidityComposite> getValidity() {
        return validity;
    }

    public PreservationObjectVersionFileComposite setValidity(List<PreservationObjectFileValidityComposite> validity) {
        this.validity = validity;
        return this;
    }

    @Override
    public String toString() {
        return "PreservationObjectVersionFileComposite{" +
                "objectFileId=" + objectFileId +
                ", objectVersionFileId=" + objectVersionFileId +
                ", sha256Digest='" + sha256Digest + '\'' +
                ", fileSize=" + fileSize +
                ", filePath='" + filePath + '\'' +
                ", createdTimestamp=" + createdTimestamp +
                ", formats=" + formats +
                ", encoding=" + encoding +
                ", validity=" + validity +
                '}';
    }

}
