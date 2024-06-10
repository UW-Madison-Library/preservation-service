package edu.wisc.library.sdg.preservation.manager.db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("preservation_object_file_format")
public class PreservationObjectFileFormat {

    @Id
    @Column("object_file_format_id")
    private Long objectFileFormatId;

    @Column("object_file_id")
    private Long objectFileId;

    @Column("file_format_id")
    private Long fileFormatId;

    @Column("meta_source_id")
    private Long metaSourceId;

    public PreservationObjectFileFormat() {
    }

    @PersistenceCreator
    public PreservationObjectFileFormat(Long objectFileFormatId,
                                        Long objectFileId,
                                        Long fileFormatId,
                                        Long metaSourceId) {
        this.objectFileFormatId = objectFileFormatId;
        this.objectFileId = objectFileId;
        this.fileFormatId = fileFormatId;
        this.metaSourceId = metaSourceId;
    }

    public Long getObjectFileFormatId() {
        return objectFileFormatId;
    }

    public PreservationObjectFileFormat setObjectFileFormatId(Long objectFileFormatId) {
        this.objectFileFormatId = objectFileFormatId;
        return this;
    }

    public Long getObjectFileId() {
        return objectFileId;
    }

    public PreservationObjectFileFormat setObjectFileId(Long objectFileId) {
        this.objectFileId = objectFileId;
        return this;
    }

    public Long getFileFormatId() {
        return fileFormatId;
    }

    public PreservationObjectFileFormat setFileFormatId(Long fileFormatId) {
        this.fileFormatId = fileFormatId;
        return this;
    }

    public Long getMetaSourceId() {
        return metaSourceId;
    }

    public PreservationObjectFileFormat setMetaSourceId(Long metaSourceId) {
        this.metaSourceId = metaSourceId;
        return this;
    }

    @Override
    public String toString() {
        return "PreservationObjectFileFormat{" +
                "objectFileFormatId=" + objectFileFormatId +
                ", objectFileId=" + objectFileId +
                ", fileFormatId=" + fileFormatId +
                ", metaSourceId=" + metaSourceId +
                '}';
    }

}
