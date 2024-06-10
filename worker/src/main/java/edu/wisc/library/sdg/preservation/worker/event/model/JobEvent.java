package edu.wisc.library.sdg.preservation.worker.event.model;

public class JobEvent {

    private final Long jobId;

    public JobEvent(Long jobId) {
        this.jobId = jobId;
    }

    public Long getJobId() {
        return jobId;
    }

    @Override
    public String toString() {
        return "JobEvent{" +
                "jobId='" + jobId + '\'' +
                '}';
    }

}
