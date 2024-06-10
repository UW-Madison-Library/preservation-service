package edu.wisc.library.sdg.preservation.manager.db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("ingest_file_encoding")
public class IngestBatchObjectFileEncoding {

    @Id
    @Column("ingest_file_encoding_id")
    private Long ingestFileEncodingId;

    @Column("ingest_file_id")
    private Long ingestFileId;

    @Column("file_encoding_id")
    private Long fileEncodingId;

    @Column("meta_source_id")
    private Long metaSourceId;

    public IngestBatchObjectFileEncoding() {

    }

    @PersistenceCreator
    public IngestBatchObjectFileEncoding(Long ingestFileEncodingId,
                                         Long ingestFileId,
                                         Long fileEncodingId,
                                         Long metaSourceId) {
        this.ingestFileEncodingId = ingestFileEncodingId;
        this.ingestFileId = ingestFileId;
        this.fileEncodingId = fileEncodingId;
        this.metaSourceId = metaSourceId;
    }

    public Long getIngestFileEncodingId() {
        return ingestFileEncodingId;
    }

    public IngestBatchObjectFileEncoding setIngestFileEncodingId(Long ingestFileEncodingId) {
        this.ingestFileEncodingId = ingestFileEncodingId;
        return this;
    }

    public Long getIngestFileId() {
        return ingestFileId;
    }

    public IngestBatchObjectFileEncoding setIngestFileId(Long ingestFileId) {
        this.ingestFileId = ingestFileId;
        return this;
    }

    public Long getFileEncodingId() {
        return fileEncodingId;
    }

    public IngestBatchObjectFileEncoding setFileEncodingId(Long fileEncodingId) {
        this.fileEncodingId = fileEncodingId;
        return this;
    }

    public Long getMetaSourceId() {
        return metaSourceId;
    }

    public IngestBatchObjectFileEncoding setMetaSourceId(Long metaSourceId) {
        this.metaSourceId = metaSourceId;
        return this;
    }

    @Override
    public String toString() {
        return "IngestBatchObjectFileEncoding{" +
                "ingestFileEncodingId=" + ingestFileEncodingId +
                ", ingestFileId=" + ingestFileId +
                ", fileEncodingId=" + fileEncodingId +
                ", metaSourceId=" + metaSourceId +
                '}';
    }

}
