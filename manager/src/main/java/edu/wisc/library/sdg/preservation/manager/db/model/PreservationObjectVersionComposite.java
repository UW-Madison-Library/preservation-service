package edu.wisc.library.sdg.preservation.manager.db.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PreservationObjectVersionComposite {

    private UUID objectId;
    private Long objectVersionId;

    private PreservationObjectState state;

    private String vault;
    private String externalObjectId;
    private Integer version;

    private String persistenceVersion;
    private String initialPersistenceVersion;

    private Long ingestId;

    private List<PreservationObjectVersionFileComposite> files;

    private LocalDateTime createdTimestamp;

    public PreservationObjectVersionComposite() {
        files = new ArrayList<>();
    }

    public UUID getObjectId() {
        return objectId;
    }

    public PreservationObjectVersionComposite setObjectId(UUID objectId) {
        this.objectId = objectId;
        return this;
    }

    public Long getObjectVersionId() {
        return objectVersionId;
    }

    public PreservationObjectVersionComposite setObjectVersionId(Long objectVersionId) {
        this.objectVersionId = objectVersionId;
        return this;
    }

    public PreservationObjectState getState() {
        return state;
    }

    public PreservationObjectVersionComposite setState(PreservationObjectState state) {
        this.state = state;
        return this;
    }

    public String getVault() {
        return vault;
    }

    public PreservationObjectVersionComposite setVault(String vault) {
        this.vault = vault;
        return this;
    }

    public String getExternalObjectId() {
        return externalObjectId;
    }

    public PreservationObjectVersionComposite setExternalObjectId(String externalObjectId) {
        this.externalObjectId = externalObjectId;
        return this;
    }

    public Integer getVersion() {
        return version;
    }

    public PreservationObjectVersionComposite setVersion(Integer version) {
        this.version = version;
        return this;
    }

    public String getPersistenceVersion() {
        return persistenceVersion;
    }

    public PreservationObjectVersionComposite setPersistenceVersion(String persistenceVersion) {
        this.persistenceVersion = persistenceVersion;
        return this;
    }

    public String getInitialPersistenceVersion() {
        return initialPersistenceVersion;
    }

    public PreservationObjectVersionComposite setInitialPersistenceVersion(String initialPersistenceVersion) {
        this.initialPersistenceVersion = initialPersistenceVersion;
        return this;
    }

    public Long getIngestId() {
        return ingestId;
    }

    public PreservationObjectVersionComposite setIngestId(Long ingestId) {
        this.ingestId = ingestId;
        return this;
    }

    public List<PreservationObjectVersionFileComposite> getFiles() {
        return files;
    }

    public PreservationObjectVersionComposite setFiles(List<PreservationObjectVersionFileComposite> files) {
        this.files = files;
        return this;
    }

    public LocalDateTime getCreatedTimestamp() {
        return createdTimestamp;
    }

    public PreservationObjectVersionComposite setCreatedTimestamp(LocalDateTime createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
        return this;
    }

    @Override
    public String toString() {
        return "PreservationObjectVersionComposite{" +
                "objectId='" + objectId + '\'' +
                ", objectVersionId=" + objectVersionId +
                ", state=" + state +
                ", vault='" + vault + '\'' +
                ", externalObjectId='" + externalObjectId + '\'' +
                ", version=" + version +
                ", persistenceVersion='" + persistenceVersion + '\'' +
                ", initialPersistenceVersion='" + initialPersistenceVersion + '\'' +
                ", ingestId='" + ingestId + '\'' +
                ", files=" + files +
                ", createdTimestamp=" + createdTimestamp +
                '}';
    }

}
