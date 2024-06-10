package edu.wisc.library.sdg.preservation.manager.db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("retrieve_job_details")
public class RetrieveJobDetails {

    @Id
    @Column("retrieve_job_details_id")
    private Long retrieveJobId;

    @Column("job_id")
    private Long jobId;

    @Column("object_version_id")
    private Long objectVersionId;

    public RetrieveJobDetails() {
    }

    @PersistenceCreator
    public RetrieveJobDetails(Long retrieveJobId,
                              Long jobId,
                              Long objectVersionId) {
        this.retrieveJobId = retrieveJobId;
        this.jobId = jobId;
        this.objectVersionId = objectVersionId;
    }

    public Long getRetrieveJobId() {
        return retrieveJobId;
    }

    public RetrieveJobDetails setRetrieveJobId(Long retrieveJobId) {
        this.retrieveJobId = retrieveJobId;
        return this;
    }

    public Long getJobId() {
        return jobId;
    }

    public RetrieveJobDetails setJobId(Long jobId) {
        this.jobId = jobId;
        return this;
    }

    public Long getObjectVersionId() {
        return objectVersionId;
    }

    public RetrieveJobDetails setObjectVersionId(Long objectVersionId) {
        this.objectVersionId = objectVersionId;
        return this;
    }

    @Override
    public String toString() {
        return "RetrieveJobDetails{" +
                "retrieveJobId=" + retrieveJobId +
                ", jobId='" + jobId + '\'' +
                ", objectVersionId=" + objectVersionId +
                '}';
    }

}
