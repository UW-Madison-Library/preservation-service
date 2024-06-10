package edu.wisc.library.sdg.preservation.worker.event.model;

import edu.wisc.library.sdg.preservation.manager.client.internal.model.DataStore;

public class ReplicateObjectVersionEvent extends JobEvent {

    private final String objectId;
    private final String vault;
    private final String externalObjectId;
    private final String persistenceVersion;
    private final DataStore source;
    private final DataStore destination;

    public ReplicateObjectVersionEvent(Long jobId,
                                       String objectId,
                                       String vault,
                                       String externalObjectId,
                                       String persistenceVersion,
                                       DataStore source,
                                       DataStore destination) {
        super(jobId);
        this.objectId = objectId;
        this.vault = vault;
        this.externalObjectId = externalObjectId;
        this.persistenceVersion = persistenceVersion;
        this.source = source;
        this.destination = destination;
    }

    public String getObjectId() {
        return objectId;
    }

    public String getVault() {
        return vault;
    }

    public String getExternalObjectId() {
        return externalObjectId;
    }

    public String getPersistenceVersion() {
        return persistenceVersion;
    }

    public DataStore getSource() {
        return source;
    }

    public DataStore getDestination() {
        return destination;
    }

    @Override
    public String toString() {
        return "ReplicateObjectVersionEvent{" +
                "jobId='" + getJobId() + '\'' +
                ", objectId='" + objectId + '\'' +
                ", vault='" + vault + '\'' +
                ", externalObjectId='" + externalObjectId + '\'' +
                ", persistenceVersion='" + persistenceVersion + '\'' +
                ", source=" + source +
                ", destination=" + destination +
                '}';
    }

}
