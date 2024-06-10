package edu.wisc.library.sdg.preservation.manager.db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Table("preservation_object_version_location")
public class PreservationObjectVersionLocation {

    @Id
    @Column("object_version_location_id")
    private Long objectVersionLocationId;

    @Column("object_id")
    private UUID objectId;

    @Column("persistence_version")
    private String persistenceVersion;

    @Column("datastore")
    private DataStore dataStore;

    @Column("datastore_key")
    private String dataStoreKey;

    @Column("sha256_digest")
    private String sha256Digest;

    @Column("written_timestamp")
    private LocalDateTime writtenTimestamp;

    @Column("last_check_timestamp")
    private LocalDateTime lastCheckTimestamp;

    public PreservationObjectVersionLocation() {
    }

    @PersistenceCreator
    public PreservationObjectVersionLocation(Long objectVersionLocationId,
                                             UUID objectId,
                                             String persistenceVersion,
                                             DataStore dataStore,
                                             String dataStoreKey,
                                             String sha256Digest,
                                             LocalDateTime writtenTimestamp,
                                             LocalDateTime lastCheckTimestamp) {
        this.objectVersionLocationId = objectVersionLocationId;
        this.objectId = objectId;
        this.persistenceVersion = persistenceVersion;
        this.dataStore = dataStore;
        this.dataStoreKey = dataStoreKey;
        this.sha256Digest = sha256Digest;
        this.writtenTimestamp = writtenTimestamp;
        this.lastCheckTimestamp = lastCheckTimestamp;
    }

    public Long getObjectVersionLocationId() {
        return objectVersionLocationId;
    }

    public PreservationObjectVersionLocation setObjectVersionLocationId(Long objectVersionLocationId) {
        this.objectVersionLocationId = objectVersionLocationId;
        return this;
    }

    public UUID getObjectId() {
        return objectId;
    }

    public PreservationObjectVersionLocation setObjectId(UUID objectId) {
        this.objectId = objectId;
        return this;
    }

    public String getPersistenceVersion() {
        return persistenceVersion;
    }

    public PreservationObjectVersionLocation setPersistenceVersion(String persistenceVersion) {
        this.persistenceVersion = persistenceVersion;
        return this;
    }

    public DataStore getDataStore() {
        return dataStore;
    }

    public PreservationObjectVersionLocation setDataStore(DataStore dataStore) {
        this.dataStore = dataStore;
        return this;
    }

    public String getDataStoreKey() {
        return dataStoreKey;
    }

    public PreservationObjectVersionLocation setDataStoreKey(String dataStoreKey) {
        this.dataStoreKey = dataStoreKey;
        return this;
    }


    public String getSha256Digest() {
        return sha256Digest;
    }

    public PreservationObjectVersionLocation setSha256Digest(String sha256Digest) {
        this.sha256Digest = sha256Digest;
        return this;
    }

    public LocalDateTime getWrittenTimestamp() {
        return writtenTimestamp;
    }

    public PreservationObjectVersionLocation setWrittenTimestamp(LocalDateTime writtenTimestamp) {
        this.writtenTimestamp = writtenTimestamp;
        return this;
    }

    public LocalDateTime getLastCheckTimestamp() {
        return lastCheckTimestamp;
    }

    public PreservationObjectVersionLocation setLastCheckTimestamp(LocalDateTime lastCheckTimestamp) {
        this.lastCheckTimestamp = lastCheckTimestamp;
        return this;
    }

    @Override
    public String toString() {
        return "PreservationObjectVersionLocation{" +
                "objectVersionLocationId=" + objectVersionLocationId +
                ", objectId=" + objectId +
                ", persistenceVersion=" + persistenceVersion +
                ", dataStore=" + dataStore +
                ", dataStoreKey=" + dataStoreKey +
                ", sha256Digest=" + sha256Digest +
                ", writtenTimestamp=" + writtenTimestamp +
                ", lastCheckTimestamp=" + lastCheckTimestamp +
                '}';
    }

}
