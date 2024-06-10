package edu.wisc.library.sdg.preservation.manager.db.model;

import java.time.LocalDateTime;

public class RetrieveRequestJobComposite {

    private Long retrieveRequestJobId;

    private Long retrieveRequestId;

    private Long jobId;

    private LocalDateTime lastDownloadedTimestamp;

    private JobState state;

    public RetrieveRequestJobComposite() {

    }

    public Long getRetrieveRequestJobId() {
        return retrieveRequestJobId;
    }

    public RetrieveRequestJobComposite setRetrieveRequestJobId(Long retrieveRequestJobId) {
        this.retrieveRequestJobId = retrieveRequestJobId;
        return this;
    }

    public Long getRetrieveRequestId() {
        return retrieveRequestId;
    }

    public RetrieveRequestJobComposite setRetrieveRequestId(Long retrieveRequestId) {
        this.retrieveRequestId = retrieveRequestId;
        return this;
    }

    public Long getJobId() {
        return jobId;
    }

    public RetrieveRequestJobComposite setJobId(Long jobId) {
        this.jobId = jobId;
        return this;
    }

    public LocalDateTime getLastDownloadedTimestamp() {
        return lastDownloadedTimestamp;
    }

    public RetrieveRequestJobComposite setLastDownloadedTimestamp(LocalDateTime lastDownloadedTimestamp) {
        this.lastDownloadedTimestamp = lastDownloadedTimestamp;
        return this;
    }

    public JobState getState() {
        return state;
    }

    public RetrieveRequestJobComposite setState(JobState state) {
        this.state = state;
        return this;
    }

    @Override
    public String toString() {
        return "RetrieveRequestJobComposite{" +
                "retrieveRequestJobId=" + retrieveRequestJobId +
                ", retrieveRequestId=" + retrieveRequestId +
                ", jobId=" + jobId +
                ", lastDownloadedTimestamp=" + lastDownloadedTimestamp +
                ", state=" + state +
                '}';
    }
}
