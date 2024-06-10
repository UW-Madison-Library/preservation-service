package edu.wisc.library.sdg.preservation.worker.event.model;

public class ProcessBatchEvent extends JobEvent {

    private final Long ingestId;

    public ProcessBatchEvent(Long jobId, Long ingestId) {
        super((jobId));
        this.ingestId = ingestId;
    }

    public Long getIngestId() {
        return ingestId;
    }

    @Override
    public String toString() {
        return "ProcessBatchEvent{" +
                "jobId=" + getJobId() +
                ", ingestId=" + ingestId +
                '}';
    }
}
