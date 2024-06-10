package edu.wisc.library.sdg.preservation.manager.db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.HashSet;
import java.util.Set;

/**
 * IngestBatchObjectFile is similar to ObjectFile. IngestBatchObjectFile represents the ingest_batch_object_file plus
 * ingest_file_format, ingest_file_encoding, and ingest_file_validity.
 */
@Table("ingest_batch_object_file")
public class IngestBatchObjectFile {

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

    @MappedCollection(idColumn = "ingest_file_id", keyColumn = "ingest_file_id")
    private Set<IngestBatchObjectFileFormat> formats;

    @MappedCollection(idColumn = "ingest_file_id", keyColumn = "ingest_file_id")
    private Set<IngestBatchObjectFileEncoding> encoding;

    @MappedCollection(idColumn = "ingest_file_id", keyColumn = "ingest_file_id")
    private Set<IngestBatchObjectFileValidity> validity;

    public IngestBatchObjectFile() {
        formats = new HashSet<>();
        encoding = new HashSet<>();
        validity = new HashSet<>();
    }

    @PersistenceCreator
    public IngestBatchObjectFile(Long ingestFileId,
                                 Long ingestObjectId,
                                 String filePath,
                                 String sha256Digest,
                                 Long fileSize,
                                 Set<IngestBatchObjectFileFormat> formats,
                                 Set<IngestBatchObjectFileEncoding> encoding,
                                 Set<IngestBatchObjectFileValidity> validity) {
        this.ingestFileId = ingestFileId;
        this.ingestObjectId = ingestObjectId;
        this.filePath = filePath;
        this.sha256Digest = sha256Digest;
        this.fileSize = fileSize;
        this.formats = formats == null ? new HashSet<>() : formats;
        this.encoding = encoding == null ? new HashSet<>() : encoding;
        this.validity = validity == null ? new HashSet<>() : validity;
    }

    public Long getIngestFileId() {
        return ingestFileId;
    }

    public IngestBatchObjectFile setIngestFileId(Long ingestFileId) {
        this.ingestFileId = ingestFileId;
        return this;
    }

    public Long getIngestObjectId() {
        return ingestObjectId;
    }

    public IngestBatchObjectFile setIngestObjectId(Long ingestObjectId) {
        this.ingestObjectId = ingestObjectId;
        return this;
    }

    public String getFilePath() {
        return filePath;
    }

    public IngestBatchObjectFile setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public String getSha256Digest() {
        return sha256Digest;
    }

    public IngestBatchObjectFile setSha256Digest(String sha256Digest) {
        this.sha256Digest = sha256Digest;
        return this;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public IngestBatchObjectFile setFileSize(Long fileSize) {
        this.fileSize = fileSize;
        return this;
    }

    public Set<IngestBatchObjectFileFormat> getFormats() {
        return formats;
    }

    public IngestBatchObjectFile setFormats(Set<IngestBatchObjectFileFormat> formats) {
        this.formats = formats == null ? new HashSet<>() : formats;
        return this;
    }

    public Set<IngestBatchObjectFileEncoding> getEncoding() {
        return encoding;
    }

    public IngestBatchObjectFile setEncoding(Set<IngestBatchObjectFileEncoding> encoding) {
        this.encoding = encoding == null ? new HashSet<>() : encoding;
        return this;
    }

    public Set<IngestBatchObjectFileValidity> getValidity() {
        return validity;
    }

    public IngestBatchObjectFile setValidity(Set<IngestBatchObjectFileValidity> validity) {
        this.validity = validity == null ? new HashSet<>() : validity;
        return this;
    }

    @Override
    public String toString() {
        return "IngestBatchObjectFile{" +
                "ingestFileId=" + ingestFileId +
                ", ingestObjectId=" + ingestObjectId +
                ", filePath='" + filePath + '\'' +
                ", sha256Digest='" + sha256Digest + '\'' +
                ", fileSize='" + fileSize + '\'' +
                ", formats=" + formats +
                ", encoding=" + encoding +
                ", validity=" + validity +
                '}';
    }

}
