package edu.wisc.library.sdg.preservation.worker.event.model;

import java.util.List;

public class DeleteDipEvent extends JobEvent {

    private final Long retrieveRequestId;
    private final List<Long> retrieveJobIds;

    public DeleteDipEvent(Long jobId,
                          Long retrieveRequestId,
                          List<Long> retrieveJobIds) {
        super(jobId);
        this.retrieveRequestId = retrieveRequestId;
        this.retrieveJobIds = retrieveJobIds;
    }

    public Long getRetrieveRequestId() {
        return retrieveRequestId;
    }

    public List<Long> getRetrieveJobIds() {
        return retrieveJobIds;
    }

    @Override
    public String toString() {
        return "DeleteDipEvent{" +
                "jobId=" + getJobId() +
                "retrieveRequestId=" + retrieveRequestId +
                ", retrieveJobIds=" + retrieveJobIds +
                '}';
    }

}
