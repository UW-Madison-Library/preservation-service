package edu.wisc.library.sdg.preservation.manager.db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("validate_remote_job_details")
public class ValidateRemoteJobDetails {

    @Id
    @Column("validate_remote_job_details_id")
    private Long validateRemoteJobId;

    @Column("job_id")
    private Long jobId;

    @Column("object_version_location_id")
    private Long objectVersionLocationId;

    public ValidateRemoteJobDetails() {
    }

    @PersistenceCreator
    public ValidateRemoteJobDetails(Long validateRemoteJobId,
                                    Long jobId,
                                    Long objectVersionLocationId) {
        this.validateRemoteJobId = validateRemoteJobId;
        this.jobId = jobId;
        this.objectVersionLocationId = objectVersionLocationId;
    }

    public Long getValidateRemoteJobId() {
        return validateRemoteJobId;
    }

    public ValidateRemoteJobDetails setValidateRemoteJobId(Long validateRemoteJobId) {
        this.validateRemoteJobId = validateRemoteJobId;
        return this;
    }

    public Long getJobId() {
        return jobId;
    }

    public ValidateRemoteJobDetails setJobId(Long jobId) {
        this.jobId = jobId;
        return this;
    }

    public Long getObjectVersionLocationId() {
        return objectVersionLocationId;
    }

    public ValidateRemoteJobDetails setObjectVersionLocationId(Long objectVersionLocationId) {
        this.objectVersionLocationId = objectVersionLocationId;
        return this;
    }

    @Override
    public String toString() {
        return "ValidateRemoteJobDetails{" +
                "validateRemoteJobId=" + validateRemoteJobId +
                ", jobId=" + jobId +
                ", objectVersionLocationId=" + objectVersionLocationId +
                '}';
    }

}
