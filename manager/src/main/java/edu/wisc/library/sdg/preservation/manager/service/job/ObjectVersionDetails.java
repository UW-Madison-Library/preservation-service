package edu.wisc.library.sdg.preservation.manager.service.job;

public class ObjectVersionDetails {

    private long objectVersionId;
    private long size;
    private int version;

    public ObjectVersionDetails(long objectVersionId, long size, int version) {
        this.objectVersionId = objectVersionId;
        this.size = size;
        this.version = version;
    }

    public long getObjectVersionId() {
        return objectVersionId;
    }

    public long size() {
        return size;
    }

    public int getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "ObjectVersionDetails{" +
                "objectVersionId=" + objectVersionId +
                ", size=" + size +
                ", version=" + version +
                '}';
    }

}
