package edu.wisc.library.sdg.preservation.manager.db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("ingest_batch")
public class IngestBatch {

    @Id
    @Column("ingest_id")
    private Long ingestId;

    @Column("org_name")
    private String orgName;

    @Column("vault")
    private String vault;

    @Column("created_by")
    private String createdBy;

    @Column("reviewed_by")
    private String reviewedBy;

    @Column("state")
    private IngestBatchState state;

    @Column("original_filename")
    private String originalFilename;

    @Column("file_path")
    private String filePath;

    @Column("file_size")
    private Long fileSize;

    @Column("has_analysis_errors")
    private boolean hasAnalysisErrors;

    @Column("has_analysis_warnings")
    private boolean hasAnalysisWarnings;

    @Column("received_timestamp")
    private LocalDateTime receivedTimestamp;

    @Column("updated_timestamp")
    private LocalDateTime updatedTimestamp;

    @Version
    @Column("record_version")
    private Integer recordVersion;

    public IngestBatch() {
    }

    @PersistenceCreator
    public IngestBatch(Long ingestId,
                       String orgName,
                       String vault,
                       String createdBy,
                       String reviewedBy,
                       IngestBatchState state,
                       String originalFilename,
                       String filePath,
                       Long fileSize,
                       boolean hasAnalysisWarnings,
                       boolean hasAnalysisErrors,
                       LocalDateTime receivedTimestamp,
                       LocalDateTime updatedTimestamp,
                       Integer recordVersion) {
        this.ingestId = ingestId;
        this.orgName = orgName;
        this.vault = vault;
        this.createdBy = createdBy;
        this.reviewedBy = reviewedBy;
        this.state = state;
        this.originalFilename = originalFilename;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.hasAnalysisWarnings = hasAnalysisWarnings;
        this.hasAnalysisErrors = hasAnalysisErrors;
        this.receivedTimestamp = receivedTimestamp;
        this.updatedTimestamp = updatedTimestamp;
        this.recordVersion = recordVersion;
    }

    public Long getIngestId() {
        return ingestId;
    }

    public IngestBatch setIngestId(Long ingestId) {
        this.ingestId = ingestId;
        return this;
    }

    public String getOrgName() {
        return orgName;
    }

    public IngestBatch setOrgName(String orgName) {
        this.orgName = orgName;
        return this;
    }

    public String getVault() {
        return vault;
    }

    public IngestBatch setVault(String vault) {
        this.vault = vault;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public IngestBatch setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public String getReviewedBy() {
        return reviewedBy;
    }

    public IngestBatch setReviewedBy(String reviewedBy) {
        this.reviewedBy = reviewedBy;
        return this;
    }

    public IngestBatchState getState() {
        return state;
    }

    public IngestBatch setState(IngestBatchState state) {
        this.state = state;
        return this;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public IngestBatch setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
        return this;
    }

    public String getFilePath() {
        return filePath;
    }

    public IngestBatch setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public IngestBatch setFileSize(Long fileSize) {
        this.fileSize = fileSize;
        return this;
    }

    public boolean hasAnalysisErrors() {
        return hasAnalysisErrors;
    }

    public IngestBatch setHasAnalysisErrors(boolean hasAnalysisErrors) {
        this.hasAnalysisErrors = hasAnalysisErrors;
        return this;
    }

    public boolean hasAnalysisWarnings() {
        return hasAnalysisWarnings;
    }

    public IngestBatch setHasAnalysisWarnings(boolean hasAnalysisWarnings) {
        this.hasAnalysisWarnings = hasAnalysisWarnings;
        return this;
    }

    public LocalDateTime getReceivedTimestamp() {
        return receivedTimestamp;
    }

    public IngestBatch setReceivedTimestamp(LocalDateTime receivedTimestamp) {
        this.receivedTimestamp = receivedTimestamp;
        return this;
    }

    public LocalDateTime getUpdatedTimestamp() {
        return updatedTimestamp;
    }

    public IngestBatch setUpdatedTimestamp(LocalDateTime updatedTimestamp) {
        this.updatedTimestamp = updatedTimestamp;
        return this;
    }

    public Integer getRecordVersion() {
        return recordVersion;
    }

    public IngestBatch setRecordVersion(Integer recordVersion) {
        this.recordVersion = recordVersion;
        return this;
    }

    @Override
    public String toString() {
        return "IngestBatch{" +
                "ingestId=" + ingestId +
                ", orgName='" + orgName + '\'' +
                ", vault='" + vault + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", reviewedBy='" + reviewedBy + '\'' +
                ", state=" + state +
                ", originalFilename='" + originalFilename + '\'' +
                ", filePath='" + filePath + '\'' +
                ", fileSize='" + fileSize + '\'' +
                ", hasAnalysisErrors=" + hasAnalysisErrors +
                ", hasAnalysisWarnings=" + hasAnalysisWarnings +
                ", receivedTimestamp=" + receivedTimestamp +
                ", updatedTimestamp=" + updatedTimestamp +
                ", recordVersion=" + recordVersion +
                '}';
    }

}
