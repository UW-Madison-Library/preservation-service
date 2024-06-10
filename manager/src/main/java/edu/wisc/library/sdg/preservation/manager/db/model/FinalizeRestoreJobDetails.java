package edu.wisc.library.sdg.preservation.manager.db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("finalize_restore_job_details")
public class FinalizeRestoreJobDetails {

    @Id
    @Column("finalize_restore_job_details_id")
    private Long finalizeRestoreJobId;

    @Column("job_id")
    private Long jobId;

    @Column("object_id")
    private UUID objectId;

    public FinalizeRestoreJobDetails() {
    }

    @PersistenceCreator
    public FinalizeRestoreJobDetails(Long finalizeRestoreJobId,
                                     Long jobId,
                                     UUID objectId) {
        this.finalizeRestoreJobId = finalizeRestoreJobId;
        this.jobId = jobId;
        this.objectId = objectId;
    }

    public Long getFinalizeRestoreJobId() {
        return finalizeRestoreJobId;
    }

    public FinalizeRestoreJobDetails setFinalizeRestoreJobId(Long finalizeRestoreJobId) {
        this.finalizeRestoreJobId = finalizeRestoreJobId;
        return this;
    }

    public Long getJobId() {
        return jobId;
    }

    public FinalizeRestoreJobDetails setJobId(Long jobId) {
        this.jobId = jobId;
        return this;
    }

    public UUID getObjectId() {
        return objectId;
    }

    public FinalizeRestoreJobDetails setObjectId(UUID objectId) {
        this.objectId = objectId;
        return this;
    }

    @Override
    public String toString() {
        return "FinalizeRestoreJobDetails{" +
                "finalizeRestoreJobId=" + finalizeRestoreJobId +
                ", jobId='" + jobId + '\'' +
                ", objectId='" + objectId + '\'' +
                '}';
    }

}
