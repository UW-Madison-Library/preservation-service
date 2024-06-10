package edu.wisc.library.sdg.preservation.manager.db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("restore_job_details")
public class RestoreJobDetails {

    @Id
    @Column("restore_job_details_id")
    private Long restoreJobId;

    @Column("job_id")
    private Long jobId;

    @Column("object_version_location_id")
    private Long objectVersionLocationId;

    public RestoreJobDetails() {
    }

    @PersistenceCreator
    public RestoreJobDetails(Long restoreJobId,
                             Long jobId,
                             Long objectVersionLocationId) {
        this.restoreJobId = restoreJobId;
        this.jobId = jobId;
        this.objectVersionLocationId = objectVersionLocationId;
    }

    public Long getRestoreJobId() {
        return restoreJobId;
    }

    public RestoreJobDetails setRestoreJobId(Long restoreJobId) {
        this.restoreJobId = restoreJobId;
        return this;
    }

    public Long getJobId() {
        return jobId;
    }

    public RestoreJobDetails setJobId(Long jobId) {
        this.jobId = jobId;
        return this;
    }

    public Long getObjectVersionLocationId() {
        return objectVersionLocationId;
    }

    public RestoreJobDetails setObjectVersionLocationId(Long objectVersionLocationId) {
        this.objectVersionLocationId = objectVersionLocationId;
        return this;
    }

    @Override
    public String toString() {
        return "RestoreJobDetails{" +
                "restoreJobId=" + restoreJobId +
                ", jobId='" + jobId + '\'' +
                ", objectVersionLocationId=" + objectVersionLocationId +
                '}';
    }

}
