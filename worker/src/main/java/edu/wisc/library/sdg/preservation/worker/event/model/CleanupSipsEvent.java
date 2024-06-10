package edu.wisc.library.sdg.preservation.worker.event.model;

public class CleanupSipsEvent extends JobEvent {

    public CleanupSipsEvent(Long jobId) {
        super(jobId);
    }

    @Override
    public String toString() {
        return "CleanupSipsEvent{" +
                "jobId=" + getJobId() +
                '}';
    }

}
