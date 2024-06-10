package edu.wisc.library.sdg.preservation.worker.event.model;

public class ValidateObjectLocalEvent extends JobEvent {

    private final String objectId;
    private final boolean contentFixityCheck;

    public ValidateObjectLocalEvent(Long jobId,
                                    String objectId,
                                    boolean contentFixityCheck) {
        super(jobId);
        this.objectId = objectId;
        this.contentFixityCheck = contentFixityCheck;
    }

    public String getObjectId() {
        return objectId;
    }

    public boolean isContentFixityCheck() {
        return contentFixityCheck;
    }

    @Override
    public String toString() {
        return "ValidateObjectLocalEvent{" +
                "jobId='" + getJobId() + '\'' +
                ", objectId='" + objectId + '\'' +
                ", contentFixityCheck=" + contentFixityCheck +
                '}';
    }

}
