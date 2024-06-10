package edu.wisc.library.sdg.preservation.manager.db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Table("preservation_object_file")
public class PreservationObjectFile {

    @Id
    @Column("object_file_id")
    private Long objectFileId;

    @Column("object_id")
    private UUID objectId;

    @Column("sha256_digest")
    private String sha256Digest;

    @Column("file_size")
    private Long fileSize;

    @Column("created_timestamp")
    private LocalDateTime createdTimestamp;

    public PreservationObjectFile() {
    }

    @PersistenceCreator
    public PreservationObjectFile(Long objectFileId, UUID objectId, String sha256Digest,
                                  Long fileSize, LocalDateTime createdTimestamp) {
        this.objectFileId = objectFileId;
        this.objectId = objectId;
        this.sha256Digest = sha256Digest;
        this.fileSize = fileSize;
        this.createdTimestamp = createdTimestamp;
    }

    public Long getObjectFileId() {
        return objectFileId;
    }

    public PreservationObjectFile setObjectFileId(Long objectFileId) {
        this.objectFileId = objectFileId;
        return this;
    }

    public UUID getObjectId() {
        return objectId;
    }

    public PreservationObjectFile setObjectId(UUID objectId) {
        this.objectId = objectId;
        return this;
    }

    public String getSha256Digest() {
        return sha256Digest;
    }

    public PreservationObjectFile setSha256Digest(String sha256Digest) {
        this.sha256Digest = sha256Digest;
        return this;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public PreservationObjectFile setFileSize(Long fileSize) {
        this.fileSize = fileSize;
        return this;
    }

    public LocalDateTime getCreatedTimestamp() {
        return createdTimestamp;
    }

    public PreservationObjectFile setCreatedTimestamp(LocalDateTime createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
        return this;
    }

    @Override
    public String toString() {
        return "PreservationObjectFile{" +
                "objectFileId=" + objectFileId +
                ", objectId='" + objectId + '\'' +
                ", sha256Digest='" + sha256Digest + '\'' +
                ", fileSize='" + fileSize + '\'' +
                ", createdTimestamp=" + createdTimestamp +
                '}';
    }

}
