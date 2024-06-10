package edu.wisc.library.sdg.preservation.worker.event.model;

import edu.wisc.library.sdg.preservation.manager.client.internal.model.DataStore;

public class ValidateObjectRemoteEvent extends JobEvent {

    private final String objectId;
    private final String persistenceVersion;
    private final DataStore dataStore;
    private final String dataStoreKey;
    private final String sha256Digest;

    public ValidateObjectRemoteEvent(Long jobId,
                                     String objectId,
                                     String persistenceVersion,
                                     DataStore dataStore,
                                     String dataStoreKey,
                                     String sha256Digest) {
        super(jobId);
        this.objectId = objectId;
        this.persistenceVersion = persistenceVersion;
        this.dataStore = dataStore;
        this.dataStoreKey = dataStoreKey;
        this.sha256Digest = sha256Digest;
    }

    public String getObjectId() {
        return objectId;
    }

    public String getPersistenceVersion() {
        return persistenceVersion;
    }

    public DataStore getDataStore() {
        return dataStore;
    }

    public String getDataStoreKey() {
        return dataStoreKey;
    }

    public String getSha256Digest() {
        return sha256Digest;
    }

    @Override
    public String toString() {
        return "ValidateObjectRemoteEvent{" +
                "objectId='" + objectId + '\'' +
                ", persistenceVersion='" + persistenceVersion + '\'' +
                ", dataStore=" + dataStore +
                ", dataStoreKey='" + dataStoreKey + '\'' +
                ", sha256Digest='" + sha256Digest + '\'' +
                '}';
    }
}
