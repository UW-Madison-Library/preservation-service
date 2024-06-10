package edu.wisc.library.sdg.preservation.worker.event.model;

import edu.wisc.library.sdg.preservation.manager.client.internal.model.ObjectInfo;

import java.util.List;

public class RetrieveObjectsEvent extends JobEvent {

    private final List<ObjectInfo> objects;

    public RetrieveObjectsEvent(Long jobId, List<ObjectInfo> objects) {
        super(jobId);
        this.objects = objects;
    }

    public List<ObjectInfo> getObjects() {
        return objects;
    }

    @Override
    public String toString() {
        return "RetrieveObjectsEvent{" +
                "jobId='" + getJobId() + '\'' +
                ", objects=" + objects +
                '}';
    }

}
