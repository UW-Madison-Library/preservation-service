package edu.wisc.library.sdg.preservation.worker.event.model;

public class FinalizeRestoreEvent extends JobEvent {

    private final String objectId;

    public FinalizeRestoreEvent(Long jobId,
                                String objectId) {
        super(jobId);
        this.objectId = objectId;
    }

    public String getObjectId() {
        return objectId;
    }

    @Override
    public String toString() {
        return "FinalizeRestoreEvent{" +
                "jobId='" + getJobId() + '\'' +
                ", objectId='" + objectId + '\'' +
                '}';
    }

}
