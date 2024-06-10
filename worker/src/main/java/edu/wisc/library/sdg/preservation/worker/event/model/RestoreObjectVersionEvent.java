package edu.wisc.library.sdg.preservation.worker.event.model;

import edu.wisc.library.sdg.preservation.manager.client.internal.model.DataStore;

public class RestoreObjectVersionEvent extends JobEvent {

    private final String objectId;
    private final String persistenceVersion;
    private final DataStore source;
    private final String key;
    private final String sha256Digest;

    public RestoreObjectVersionEvent(Long jobId,
                                     String objectId,
                                     String persistenceVersion,
                                     DataStore source,
                                     String key,
                                     String sha256Digest) {
        super(jobId);
        this.objectId = objectId;
        this.persistenceVersion = persistenceVersion;
        this.source = source;
        this.key = key;
        this.sha256Digest = sha256Digest;
    }

    public String getObjectId() {
        return objectId;
    }

    public String getPersistenceVersion() {
        return persistenceVersion;
    }

    public DataStore getSource() {
        return source;
    }

    public String getKey() {
        return key;
    }

    public String getSha256Digest() {
        return sha256Digest;
    }

    @Override
    public String toString() {
        return "RestoreObjectVersionEvent{" +
                "jobId='" + getJobId() + '\'' +
                ", objectId='" + objectId + '\'' +
                ", persistenceVersion='" + persistenceVersion + '\'' +
                ", source=" + source +
                ", key='" + key + '\'' +
                ", sha256Digest='" + sha256Digest + '\'' +
                '}';
    }
}
