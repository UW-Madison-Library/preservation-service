package edu.wisc.library.sdg.preservation.manager.db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("validate_local_job_details")
public class ValidateLocalJobDetails {

    @Id
    @Column("validate_local_job_details_id")
    private Long validateLocalJobId;

    @Column("job_id")
    private Long jobId;

    @Column("object_id")
    private UUID objectId;

    @Column("content_fixity_check")
    private Boolean contentFixityCheck;

    public ValidateLocalJobDetails() {
    }

    @PersistenceCreator
    public ValidateLocalJobDetails(Long validateLocalJobId,
                                   Long jobId,
                                   UUID objectId,
                                   Boolean contentFixityCheck) {
        this.validateLocalJobId = validateLocalJobId;
        this.jobId = jobId;
        this.objectId = objectId;
        this.contentFixityCheck = contentFixityCheck;
    }

    public Long getValidateLocalJobId() {
        return validateLocalJobId;
    }

    public ValidateLocalJobDetails setValidateLocalJobId(Long validateLocalJobId) {
        this.validateLocalJobId = validateLocalJobId;
        return this;
    }

    public Long getJobId() {
        return jobId;
    }

    public ValidateLocalJobDetails setJobId(Long jobId) {
        this.jobId = jobId;
        return this;
    }

    public UUID getObjectId() {
        return objectId;
    }

    public ValidateLocalJobDetails setObjectId(UUID objectId) {
        this.objectId = objectId;
        return this;
    }

    public Boolean getContentFixityCheck() {
        return contentFixityCheck;
    }

    public ValidateLocalJobDetails setContentFixityCheck(Boolean contentFixityCheck) {
        this.contentFixityCheck = contentFixityCheck;
        return this;
    }

    @Override
    public String toString() {
        return "ValidateLocalJobDetails{" +
                "validateLocalJobId=" + validateLocalJobId +
                ", jobId='" + jobId + '\'' +
                ", objectId='" + objectId + '\'' +
                ", contentFixityCheck=" + contentFixityCheck +
                '}';
    }

}
