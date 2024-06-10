package edu.wisc.library.sdg.preservation.manager.db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("process_batch_job_details")
public class ProcessBatchJobDetails {

    @Id
    @Column("process_batch_job_details_id")
    private Long processBatchJobId;

    @Column("job_id")
    private Long jobId;

    @Column("ingest_id")
    private Long ingestId;

    public ProcessBatchJobDetails() {
    }

    @PersistenceCreator
    public ProcessBatchJobDetails(Long processBatchJobId,
                                  Long jobId,
                                  Long ingestId) {
        this.processBatchJobId = processBatchJobId;
        this.jobId = jobId;
        this.ingestId = ingestId;
    }

    public Long getProcessBatchJobId() {
        return processBatchJobId;
    }

    public ProcessBatchJobDetails setProcessBatchJobId(Long processBatchJobId) {
        this.processBatchJobId = processBatchJobId;
        return this;
    }

    public Long getJobId() {
        return jobId;
    }

    public ProcessBatchJobDetails setJobId(Long jobId) {
        this.jobId = jobId;
        return this;
    }

    public Long getIngestId() {
        return ingestId;
    }

    public ProcessBatchJobDetails setIngestId(Long ingestId) {
        this.ingestId = ingestId;
        return this;
    }

    @Override
    public String toString() {
        return "ProcessBatchJobDetails{" +
                "processBatchJobId=" + processBatchJobId +
                ", jobId='" + jobId + '\'' +
                ", ingestId='" + ingestId + '\'' +
                '}';
    }

}
