package edu.wisc.library.sdg.preservation.manager.db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Version;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Table("preservation_object")
public class PreservationObject implements Persistable<UUID> {

    @Id
    @Column("object_id")
    private UUID objectId;

    @Column("vault")
    private String vault;

    @Column("external_object_id")
    private String externalObjectId;

    @Column("head_object_version_id")
    private Long headObjectVersionId;

    @Column("state")
    private PreservationObjectState state;

    @Column("last_shallow_check_timestamp")
    private LocalDateTime lastShallowCheckTimestamp;

    @Column("last_deep_check_timestamp")
    private LocalDateTime lastDeepCheckTimestamp;

    @Column("created_timestamp")
    private LocalDateTime createdTimestamp;

    @Column("updated_timestamp")
    private LocalDateTime updatedTimestamp;

    @Version
    @Column("record_version")
    private Integer recordVersion;

    public PreservationObject() {
    }

    @PersistenceCreator
    public PreservationObject(UUID objectId,
                              String vault,
                              String externalObjectId,
                              Long headObjectVersionId,
                              PreservationObjectState state,
                              LocalDateTime lastShallowCheckTimestamp,
                              LocalDateTime lastDeepCheckTimestamp,
                              LocalDateTime createdTimestamp,
                              LocalDateTime updatedTimestamp,
                              Integer recordVersion) {
        this.objectId = objectId;
        this.vault = vault;
        this.externalObjectId = externalObjectId;
        this.headObjectVersionId = headObjectVersionId;
        this.state = state;
        this.lastShallowCheckTimestamp = lastShallowCheckTimestamp;
        this.lastDeepCheckTimestamp = lastDeepCheckTimestamp;
        this.createdTimestamp = createdTimestamp;
        this.updatedTimestamp = updatedTimestamp;
        this.recordVersion = recordVersion;
    }

    @Override
    public UUID getId() {
        return getObjectId();
    }

    @Override
    public boolean isNew() {
        return recordVersion == null;
    }

    public UUID getObjectId() {
        return objectId;
    }

    public PreservationObject setObjectId(UUID objectId) {
        this.objectId = objectId;
        return this;
    }

    public String getVault() {
        return vault;
    }

    public PreservationObject setVault(String vault) {
        this.vault = vault;
        return this;
    }

    public String getExternalObjectId() {
        return externalObjectId;
    }

    public PreservationObject setExternalObjectId(String externalObjectId) {
        this.externalObjectId = externalObjectId;
        return this;
    }

    public Long getHeadObjectVersionId() {
        return headObjectVersionId;
    }

    public PreservationObject setHeadObjectVersionId(Long headObjectVersionId) {
        this.headObjectVersionId = headObjectVersionId;
        return this;
    }

    public PreservationObjectState getState() {
        return state;
    }

    public PreservationObject setState(PreservationObjectState state) {
        this.state = state;
        return this;
    }

    public LocalDateTime getLastShallowCheckTimestamp() {
        return lastShallowCheckTimestamp;
    }

    public PreservationObject setLastShallowCheckTimestamp(LocalDateTime lastShallowCheckTimestamp) {
        this.lastShallowCheckTimestamp = lastShallowCheckTimestamp;
        return this;
    }

    public LocalDateTime getLastDeepCheckTimestamp() {
        return lastDeepCheckTimestamp;
    }

    public PreservationObject setLastDeepCheckTimestamp(LocalDateTime lastDeepCheckTimestamp) {
        this.lastDeepCheckTimestamp = lastDeepCheckTimestamp;
        return this;
    }

    public LocalDateTime getCreatedTimestamp() {
        return createdTimestamp;
    }

    public PreservationObject setCreatedTimestamp(LocalDateTime createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
        return this;
    }

    public LocalDateTime getUpdatedTimestamp() {
        return updatedTimestamp;
    }

    public PreservationObject setUpdatedTimestamp(LocalDateTime updatedTimestamp) {
        this.updatedTimestamp = updatedTimestamp;
        return this;
    }

    public Integer getRecordVersion() {
        return recordVersion;
    }

    public PreservationObject setRecordVersion(Integer recordVersion) {
        this.recordVersion = recordVersion;
        return this;
    }

    @Override
    public String toString() {
        return "PreservationObject{" +
                "objectId='" + objectId + '\'' +
                ", vault='" + vault + '\'' +
                ", externalObjectId='" + externalObjectId + '\'' +
                ", state=" + state +
                ", headObjectVersionId=" + headObjectVersionId +
                ", lastShallowCheckTimestamp=" + lastShallowCheckTimestamp +
                ", lastDeepCheckTimestamp=" + lastDeepCheckTimestamp +
                ", createdTimestamp=" + createdTimestamp +
                ", updatedTimestamp=" + updatedTimestamp +
                ", recordVersion=" + recordVersion +
                '}';
    }
}
