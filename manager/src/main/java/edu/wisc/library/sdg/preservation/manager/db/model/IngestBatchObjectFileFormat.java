package edu.wisc.library.sdg.preservation.manager.db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("ingest_file_format")
public class IngestBatchObjectFileFormat {

    @Id
    @Column("ingest_file_format_id")
    private Long ingestFileFormatId;

    @Column("ingest_file_id")
    private Long ingestFileId;

    @Column("file_format_id")
    private Long fileFormatId;

    @Column("meta_source_id")
    private Long metaSourceId;

    public IngestBatchObjectFileFormat() {

    }

    @PersistenceCreator
    public IngestBatchObjectFileFormat(Long ingestFileFormatId,
                                       Long ingestFileId,
                                       Long fileFormatId,
                                       Long metaSourceId) {
        this.ingestFileFormatId = ingestFileFormatId;
        this.ingestFileId = ingestFileId;
        this.fileFormatId = fileFormatId;
        this.metaSourceId = metaSourceId;
    }

    public Long getIngestFileFormatId() {
        return ingestFileFormatId;
    }

    public IngestBatchObjectFileFormat setIngestFileFormatId(Long ingestFileFormatId) {
        this.ingestFileFormatId = ingestFileFormatId;
        return this;
    }

    public Long getIngestFileId() {
        return ingestFileId;
    }

    public IngestBatchObjectFileFormat setIngestFileId(Long ingestFileId) {
        this.ingestFileId = ingestFileId;
        return this;
    }

    public Long getFileFormatId() {
        return fileFormatId;
    }

    public IngestBatchObjectFileFormat setFileFormatId(Long fileFormatId) {
        this.fileFormatId = fileFormatId;
        return this;
    }

    public Long getMetaSourceId() {
        return metaSourceId;
    }

    public IngestBatchObjectFileFormat setMetaSourceId(Long metaSourceId) {
        this.metaSourceId = metaSourceId;
        return this;
    }

    @Override
    public String toString() {
        return "IngestBatchObjectFileFormat{" +
                "ingestFileFormatId=" + ingestFileFormatId +
                ", ingestFileId=" + ingestFileId +
                ", fileFormatId=" + fileFormatId +
                ", metaSourceId=" + metaSourceId +
                '}';
    }

}
