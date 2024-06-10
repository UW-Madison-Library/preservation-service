package edu.wisc.library.sdg.preservation.manager.db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("preservation_object_file_encoding")
public class PreservationObjectFileEncoding {

    @Id
    @Column("object_file_encoding_id")
    private Long objectFileEncodingId;

    @Column("object_file_id")
    private Long objectFileId;

    @Column("file_encoding_id")
    private Long fileEncodingId;

    @Column("meta_source_id")
    private Long metaSourceId;

    public PreservationObjectFileEncoding() {
    }

    @PersistenceCreator
    public PreservationObjectFileEncoding(Long objectFileEncodingId,
                                          Long objectFileId,
                                          Long fileEncodingId,
                                          Long metaSourceId) {
        this.objectFileEncodingId = objectFileEncodingId;
        this.objectFileId = objectFileId;
        this.fileEncodingId = fileEncodingId;
        this.metaSourceId = metaSourceId;
    }

    public Long getObjectFileEncodingId() {
        return objectFileEncodingId;
    }

    public PreservationObjectFileEncoding setObjectFileEncodingId(Long objectFileEncodingId) {
        this.objectFileEncodingId = objectFileEncodingId;
        return this;
    }

    public Long getObjectFileId() {
        return objectFileId;
    }

    public PreservationObjectFileEncoding setObjectFileId(Long objectFileId) {
        this.objectFileId = objectFileId;
        return this;
    }

    public Long getFileEncodingId() {
        return fileEncodingId;
    }

    public PreservationObjectFileEncoding setFileEncodingId(Long fileEncodingId) {
        this.fileEncodingId = fileEncodingId;
        return this;
    }

    public Long getMetaSourceId() {
        return metaSourceId;
    }

    public PreservationObjectFileEncoding setMetaSourceId(Long metaSourceId) {
        this.metaSourceId = metaSourceId;
        return this;
    }

    @Override
    public String toString() {
        return "PreservationObjectFileEncoding{" +
                "objectFileEncodingId=" + objectFileEncodingId +
                ", objectFileId=" + objectFileId +
                ", fileEncodingId=" + fileEncodingId +
                ", metaSourceId=" + metaSourceId +
                '}';
    }

}
