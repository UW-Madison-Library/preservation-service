package edu.wisc.library.sdg.preservation.manager.db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("preservation_object_version_file")
public class PreservationObjectVersionFile {

    @Id
    @Column("object_version_file_id")
    private Long objectVersionFileId;

    @Column("object_file_id")
    private Long objectFileId;

    @Column("object_version_id")
    private Long objectVersionId;

    @Column("file_path")
    private String filePath;

    @Column("created_timestamp")
    private LocalDateTime createdTimestamp;

    public PreservationObjectVersionFile() {
    }

    @PersistenceCreator
    public PreservationObjectVersionFile(Long objectVersionFileId,
                                         Long objectFileId,
                                         Long objectVersionId,
                                         String filePath,
                                         LocalDateTime createdTimestamp) {
        this.objectVersionFileId = objectVersionFileId;
        this.objectFileId = objectFileId;
        this.objectVersionId = objectVersionId;
        this.filePath = filePath;
        this.createdTimestamp = createdTimestamp;
    }

    public Long getObjectVersionFileId() {
        return objectVersionFileId;
    }

    public PreservationObjectVersionFile setObjectVersionFileId(Long objectVersionFileId) {
        this.objectVersionFileId = objectVersionFileId;
        return this;
    }

    public Long getObjectFileId() {
        return objectFileId;
    }

    public PreservationObjectVersionFile setObjectFileId(Long objectFileId) {
        this.objectFileId = objectFileId;
        return this;
    }

    public Long getObjectVersionId() {
        return objectVersionId;
    }

    public PreservationObjectVersionFile setObjectVersionId(Long objectVersionId) {
        this.objectVersionId = objectVersionId;
        return this;
    }

    public String getFilePath() {
        return filePath;
    }

    public PreservationObjectVersionFile setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public LocalDateTime getCreatedTimestamp() {
        return createdTimestamp;
    }

    public PreservationObjectVersionFile setCreatedTimestamp(LocalDateTime createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
        return this;
    }

    @Override
    public String toString() {
        return "PreservationObjectVersionFile{" +
                "objectVersionFileId=" + objectVersionFileId +
                ", objectFileId=" + objectFileId +
                ", objectVersionId=" + objectVersionId +
                ", filePath='" + filePath + '\'' +
                ", createdTimestamp=" + createdTimestamp +
                '}';
    }

}
