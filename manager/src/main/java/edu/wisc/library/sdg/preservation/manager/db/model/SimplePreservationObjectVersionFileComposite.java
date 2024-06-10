package edu.wisc.library.sdg.preservation.manager.db.model;

public class SimplePreservationObjectVersionFileComposite {

    private Long objectFileId;
    private Long objectVersionFileId;

    private String sha256Digest;
    private Long fileSize;

    private String filePath;

    public Long getObjectFileId() {
        return objectFileId;
    }

    public SimplePreservationObjectVersionFileComposite setObjectFileId(Long objectFileId) {
        this.objectFileId = objectFileId;
        return this;
    }

    public Long getObjectVersionFileId() {
        return objectVersionFileId;
    }

    public SimplePreservationObjectVersionFileComposite setObjectVersionFileId(Long objectVersionFileId) {
        this.objectVersionFileId = objectVersionFileId;
        return this;
    }

    public String getSha256Digest() {
        return sha256Digest;
    }

    public SimplePreservationObjectVersionFileComposite setSha256Digest(String sha256Digest) {
        this.sha256Digest = sha256Digest;
        return this;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public SimplePreservationObjectVersionFileComposite setFileSize(Long fileSize) {
        this.fileSize = fileSize;
        return this;
    }

    public String getFilePath() {
        return filePath;
    }

    public SimplePreservationObjectVersionFileComposite setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    @Override
    public String toString() {
        return "SimplePreservationObjectVersionFileComposite{" +
                "objectFileId=" + objectFileId +
                ", objectVersionFileId=" + objectVersionFileId +
                ", sha256Digest='" + sha256Digest + '\'' +
                ", fileSize=" + fileSize +
                ", filePath='" + filePath + '\'' +
                '}';
    }

}
