package edu.wisc.library.sdg.preservation.manager.db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Table("preservation_object_version")
public class PreservationObjectVersion {

    @Id
    @Column("object_version_id")
    private Long objectVersionId;

    @Column("object_id")
    private UUID objectId;

    @Column("version")
    private Integer version;

    /**
     * OCFL version at the time the preservation version was created
     */
    @Column("initial_persistence_version")
    private String initialPersistenceVersion;

    /**
     * Current OCFL version. This must be updated if the backing OCFL object is updated in any way that does not
     * result in a new preservation version. For example, if FITS is run again.
     */
    @Column("persistence_version")
    private String persistenceVersion;

    @Column("ingest_id")
    private Long ingestId;

    @Column("created_timestamp")
    private LocalDateTime createdTimestamp;

    @Column("updated_timestamp")
    private LocalDateTime updatedTimestamp;

    public PreservationObjectVersion() {
    }

    @PersistenceCreator
    public PreservationObjectVersion(Long objectVersionId,
                                     UUID objectId,
                                     Integer version,
                                     String initialPersistenceVersion,
                                     String persistenceVersion,
                                     Long ingestId,
                                     LocalDateTime createdTimestamp,
                                     LocalDateTime updatedTimestamp) {
        this.objectVersionId = objectVersionId;
        this.objectId = objectId;
        this.version = version;
        this.initialPersistenceVersion = initialPersistenceVersion;
        this.persistenceVersion = persistenceVersion;
        this.ingestId = ingestId;
        this.createdTimestamp = createdTimestamp;
        this.updatedTimestamp = updatedTimestamp;
    }

    public Long getObjectVersionId() {
        return objectVersionId;
    }

    public PreservationObjectVersion setObjectVersionId(Long objectVersionId) {
        this.objectVersionId = objectVersionId;
        return this;
    }

    public UUID getObjectId() {
        return objectId;
    }

    public PreservationObjectVersion setObjectId(UUID objectId) {
        this.objectId = objectId;
        return this;
    }

    public Integer getVersion() {
        return version;
    }

    public PreservationObjectVersion setVersion(Integer version) {
        this.version = version;
        return this;
    }

    public String getInitialPersistenceVersion() {
        return initialPersistenceVersion;
    }

    public PreservationObjectVersion setInitialPersistenceVersion(String initialPersistenceVersion) {
        this.initialPersistenceVersion = initialPersistenceVersion;
        return this;
    }

    public String getPersistenceVersion() {
        return persistenceVersion;
    }

    public PreservationObjectVersion setPersistenceVersion(String persistenceVersion) {
        this.persistenceVersion = persistenceVersion;
        return this;
    }

    public Long getIngestId() {
        return ingestId;
    }

    public PreservationObjectVersion setIngestId(Long ingestId) {
        this.ingestId = ingestId;
        return this;
    }

    public LocalDateTime getCreatedTimestamp() {
        return createdTimestamp;
    }

    public PreservationObjectVersion setCreatedTimestamp(LocalDateTime createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
        return this;
    }

    public LocalDateTime getUpdatedTimestamp() {
        return updatedTimestamp;
    }

    public PreservationObjectVersion setUpdatedTimestamp(LocalDateTime updatedTimestamp) {
        this.updatedTimestamp = updatedTimestamp;
        return this;
    }

    @Override
    public String toString() {
        return "PreservationObjectVersion{" +
                "objectVersionId=" + objectVersionId +
                ", objectId='" + objectId + '\'' +
                ", version=" + version +
                ", initialPersistenceVersion='" + initialPersistenceVersion + '\'' +
                ", persistenceVersion='" + persistenceVersion + '\'' +
                ", ingestId='" + ingestId + '\'' +
                ", createdTimestamp=" + createdTimestamp +
                ", updatedTimestamp=" + updatedTimestamp +
                '}';
    }

}
