package edu.wisc.library.sdg.preservation.manager.db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("retrieve_request_job")
public class RetrieveRequestJob {

    @Id
    @Column("retrieve_request_job_id")
    private Long retrieveRequestJobId;

    @Column("retrieve_request_id")
    private Long retrieveRequestId;

    @Column("job_id")
    private Long jobId;

    @Column("last_downloaded_timestamp")
    private LocalDateTime lastDownloadedTimestamp;

    public RetrieveRequestJob() {

    }

    @PersistenceCreator
    public RetrieveRequestJob(Long retrieveRequestJobId,
                              Long retrieveRequestId,
                              Long jobId,
                              LocalDateTime lastDownloadedTimestamp) {
        this.retrieveRequestJobId = retrieveRequestJobId;
        this.retrieveRequestId = retrieveRequestId;
        this.jobId = jobId;
        this.lastDownloadedTimestamp = lastDownloadedTimestamp;
    }

    public Long getRetrieveRequestJobId() {
        return retrieveRequestJobId;
    }

    public RetrieveRequestJob setRetrieveRequestJobId(Long retrieveRequestJobId) {
        this.retrieveRequestJobId = retrieveRequestJobId;
        return this;
    }

    public Long getRetrieveRequestId() {
        return retrieveRequestId;
    }

    public RetrieveRequestJob setRetrieveRequestId(Long retrieveRequestId) {
        this.retrieveRequestId = retrieveRequestId;
        return this;
    }

    public Long getJobId() {
        return jobId;
    }

    public RetrieveRequestJob setJobId(Long jobId) {
        this.jobId = jobId;
        return this;
    }

    public LocalDateTime getLastDownloadedTimestamp() {
        return lastDownloadedTimestamp;
    }

    public RetrieveRequestJob setLastDownloadedTimestamp(LocalDateTime lastDownloadedTimestamp) {
        this.lastDownloadedTimestamp = lastDownloadedTimestamp;
        return this;
    }

    @Override
    public String toString() {
        return "RetrieveRequestJob{" +
                "retrieveRequestJobId=" + retrieveRequestJobId +
                ", retrieveRequestId=" + retrieveRequestId +
                ", jobId=" + jobId +
                ", lastDownloadedTimestamp=" + lastDownloadedTimestamp +
                '}';
    }
}
