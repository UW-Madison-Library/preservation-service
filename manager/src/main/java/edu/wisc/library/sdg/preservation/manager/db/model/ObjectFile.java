package edu.wisc.library.sdg.preservation.manager.db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * ObjectFile is similar to the IngestBatchObjectFile. ObjectFile represents the ingest_batch_object_file table.
 * ObjectFile should be used for better performance when the mapped collections in IngestBatchObjectFile are not needed.
 */
@Table("ingest_batch_object_file")
public class ObjectFile {
    @Id
    @Column("ingest_file_id")
    private Long ingestFileId;

    @Column("ingest_object_id")
    private Long ingestObjectId;

    @Column("file_path")
    private String filePath;

    @Column("sha256_digest")
    private String sha256Digest;

    @Column("file_size")
    private Long fileSize;

    public Long getIngestFileId() {
        return ingestFileId;
    }

    public void setIngestFileId(final Long ingestFileId) {
        this.ingestFileId = ingestFileId;
    }

    public Long getIngestObjectId() {
        return ingestObjectId;
    }

    public void setIngestObjectId(final Long ingestObjectId) {
        this.ingestObjectId = ingestObjectId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(final String filePath) {
        this.filePath = filePath;
    }

    public String getSha256Digest() {
        return sha256Digest;
    }

    public void setSha256Digest(final String sha256Digest) {
        this.sha256Digest = sha256Digest;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(final Long fileSize) {
        this.fileSize = fileSize;
    }

    @Override
    public String toString() {
        return "ObjectFile{" +
                "ingestFileId=" + ingestFileId +
                ", ingestObjectId=" + ingestObjectId +
                ", filePath='" + filePath + '\'' +
                ", sha256Digest='" + sha256Digest + '\'' +
                ", fileSize=" + fileSize +
                '}';
    }
}
