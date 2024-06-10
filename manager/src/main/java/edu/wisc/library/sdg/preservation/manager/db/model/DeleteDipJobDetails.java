package edu.wisc.library.sdg.preservation.manager.db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("delete_dip_job_details")
public class DeleteDipJobDetails {

    @Id
    @Column("delete_dip_job_details_id")
    private Long deleteDipJobDetailsId;

    @Column("job_id")
    private Long jobId;

    @Column("retrieve_request_id")
    private Long retrieveRequestId;

    public DeleteDipJobDetails() {
    }

    @PersistenceCreator
    public DeleteDipJobDetails(Long deleteDipJobDetailsId,
                               Long jobId,
                               Long retrieveRequestId) {
        this.deleteDipJobDetailsId = deleteDipJobDetailsId;
        this.jobId = jobId;
        this.retrieveRequestId = retrieveRequestId;
    }

    public Long getDeleteDipJobDetailsId() {
        return deleteDipJobDetailsId;
    }

    public DeleteDipJobDetails setDeleteDipJobDetailsId(Long deleteDipJobDetailsId) {
        this.deleteDipJobDetailsId = deleteDipJobDetailsId;
        return this;
    }

    public Long getJobId() {
        return jobId;
    }

    public DeleteDipJobDetails setJobId(Long jobId) {
        this.jobId = jobId;
        return this;
    }

    public Long getRetrieveRequestId() {
        return retrieveRequestId;
    }

    public DeleteDipJobDetails setRetrieveRequestId(Long retrieveRequestId) {
        this.retrieveRequestId = retrieveRequestId;
        return this;
    }

    @Override
    public String toString() {
        return "DeleteDipJobDetails{" +
                "deleteDipJobDetailsId=" + deleteDipJobDetailsId +
                ", jobId='" + jobId + '\'' +
                ", retrieveRequestId=" + retrieveRequestId +
                '}';
    }

}
