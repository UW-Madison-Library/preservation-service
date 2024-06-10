package edu.wisc.library.sdg.preservation.manager.service.job;

import edu.wisc.library.sdg.preservation.manager.db.model.PreservationObjectState;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class ObjectDetails {

    private UUID objectId;
    private PreservationObjectState state;
    private long size;
    private List<ObjectVersionDetails> versions;

    public ObjectDetails(UUID objectId, PreservationObjectState state) {
        this.objectId = objectId;
        this.state = state;
        this.size = 0;
        this.versions = new ArrayList<>();
    }

    public void addVersion(ObjectVersionDetails details) {
        size += details.size();
        versions.add(details);
    }

    public void sort() {
        versions.sort(Comparator.comparing(ObjectVersionDetails::getVersion));
    }

    public long size() {
        return size;
    }

    public UUID getObjectId() {
        return objectId;
    }

    public PreservationObjectState getState() {
        return state;
    }

    public List<ObjectVersionDetails> getVersions() {
        return versions;
    }

    @Override
    public String toString() {
        return "ObjectDetails{" +
                "objectId='" + objectId + '\'' +
                ", state=" + state +
                ", size=" + size +
                ", versions=" + versions +
                '}';
    }
}
