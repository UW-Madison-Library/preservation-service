package edu.wisc.library.sdg.preservation.manager.db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("ingest_batch_object")
public class IngestBatchObject {

    @Id
    @Column("ingest_object_id")
    private Long ingestObjectId;

    @Column("ingest_id")
    private Long ingestId;

    @Column("external_object_id")
    private String externalObjectId;

    @Column("object_root_path")
    private String objectRootPath;

    @Column("state")
    private IngestObjectState state;

    @Column("head_object_version_id")
    private Long headObjectVersionId;

    @Column("reviewed_by")
    private String reviewedBy;

    @Column("has_analysis_errors")
    private boolean hasAnalysisErrors;

    @Column("has_analysis_warnings")
    private boolean hasAnalysisWarnings;

    @Version
    @Column("record_version")
    private Integer recordVersion;

    public IngestBatchObject() {
    }

    @PersistenceCreator
    public IngestBatchObject(Long ingestObjectId, Long ingestId, String externalObjectId,
                             String objectRootPath, IngestObjectState state, Long headObjectVersionId,
                             String reviewedBy, boolean hasAnalysisErrors, boolean hasAnalysisWarnings,
                             Integer recordVersion) {
        this.ingestObjectId = ingestObjectId;
        this.ingestId = ingestId;
        this.externalObjectId = externalObjectId;
        this.objectRootPath = objectRootPath;
        this.state = state;
        this.headObjectVersionId = headObjectVersionId;
        this.reviewedBy = reviewedBy;
        this.hasAnalysisErrors = hasAnalysisErrors;
        this.hasAnalysisWarnings = hasAnalysisWarnings;
        this.recordVersion = recordVersion;
    }

    public Long getIngestObjectId() {
        return ingestObjectId;
    }

    public IngestBatchObject setIngestObjectId(Long ingestObjectId) {
        this.ingestObjectId = ingestObjectId;
        return this;
    }

    public Long getIngestId() {
        return ingestId;
    }

    public IngestBatchObject setIngestId(Long ingestId) {
        this.ingestId = ingestId;
        return this;
    }

    public String getExternalObjectId() {
        return externalObjectId;
    }

    public IngestBatchObject setExternalObjectId(String externalObjectId) {
        this.externalObjectId = externalObjectId;
        return this;
    }

    public String getObjectRootPath() {
        return objectRootPath;
    }

    public IngestBatchObject setObjectRootPath(String objectRootPath) {
        this.objectRootPath = objectRootPath;
        return this;
    }

    public IngestObjectState getState() {
        return state;
    }

    public IngestBatchObject setState(IngestObjectState state) {
        this.state = state;
        return this;
    }

    public Long getHeadObjectVersionId() {
        return headObjectVersionId;
    }

    public IngestBatchObject setHeadObjectVersionId(Long headObjectVersionId) {
        this.headObjectVersionId = headObjectVersionId;
        return this;
    }

    public String getReviewedBy() {
        return reviewedBy;
    }

    public IngestBatchObject setReviewedBy(String reviewedBy) {
        this.reviewedBy = reviewedBy;
        return this;
    }

    public boolean isHasAnalysisErrors() {
        return hasAnalysisErrors;
    }

    public IngestBatchObject setHasAnalysisErrors(boolean hasAnalysisErrors) {
        this.hasAnalysisErrors = hasAnalysisErrors;
        return this;
    }

    public boolean isHasAnalysisWarnings() {
        return hasAnalysisWarnings;
    }

    public IngestBatchObject setHasAnalysisWarnings(boolean hasAnalysisWarnings) {
        this.hasAnalysisWarnings = hasAnalysisWarnings;
        return this;
    }

    public Integer getRecordVersion() {
        return recordVersion;
    }

    public IngestBatchObject setRecordVersion(Integer recordVersion) {
        this.recordVersion = recordVersion;
        return this;
    }

    @Override
    public String toString() {
        return "IngestBatchObject{" +
                "ingestObjectId=" + ingestObjectId +
                ", ingestId='" + ingestId + '\'' +
                ", externalObjectId='" + externalObjectId + '\'' +
                ", objectRootPath='" + objectRootPath + '\'' +
                ", state=" + state +
                ", headObjectVersionId='" + headObjectVersionId + '\'' +
                ", reviewedBy='" + reviewedBy + '\'' +
                ", hasAnalysisErrors=" + hasAnalysisErrors +
                ", hasAnalysisWarnings=" + hasAnalysisWarnings +
                ", recordVersion='" + recordVersion + '\'' +
                '}';
    }

}
