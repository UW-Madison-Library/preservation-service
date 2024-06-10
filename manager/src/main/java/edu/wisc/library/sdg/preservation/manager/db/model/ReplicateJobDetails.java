package edu.wisc.library.sdg.preservation.manager.db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("replicate_job_details")
public class ReplicateJobDetails {

    @Id
    @Column("replicate_job_details_id")
    private Long replicateJobId;

    @Column("job_id")
    private Long jobId;

    @Column("object_id")
    private UUID objectId;

    @Column("persistence_version")
    private String persistenceVersion;

    @Column("source")
    private DataStore source;

    @Column("destination")
    private DataStore destination;

    @Column("ingest_id")
    private Long ingestId;

    @Column("external_object_id")
    private String externalObjectId;

    public ReplicateJobDetails() {
    }

    @PersistenceCreator
    public ReplicateJobDetails(Long replicateJobId,
                               Long jobId,
                               UUID objectId,
                               String persistenceVersion,
                               DataStore source,
                               DataStore destination,
                               Long ingestId,
                               String externalObjectId) {
        this.replicateJobId = replicateJobId;
        this.jobId = jobId;
        this.objectId = objectId;
        this.persistenceVersion = persistenceVersion;
        this.source = source;
        this.destination = destination;
        this.ingestId = ingestId;
        this.externalObjectId = externalObjectId;
    }

    public Long getReplicateJobId() {
        return replicateJobId;
    }

    public ReplicateJobDetails setReplicateJobId(Long replicateJobId) {
        this.replicateJobId = replicateJobId;
        return this;
    }

    public Long getJobId() {
        return jobId;
    }

    public ReplicateJobDetails setJobId(Long jobId) {
        this.jobId = jobId;
        return this;
    }

    public UUID getObjectId() {
        return objectId;
    }

    public ReplicateJobDetails setObjectId(UUID objectId) {
        this.objectId = objectId;
        return this;
    }

    public String getPersistenceVersion() {
        return persistenceVersion;
    }

    public ReplicateJobDetails setPersistenceVersion(String persistenceVersion) {
        this.persistenceVersion = persistenceVersion;
        return this;
    }

    public DataStore getSource() {
        return source;
    }

    public ReplicateJobDetails setSource(DataStore source) {
        this.source = source;
        return this;
    }

    public DataStore getDestination() {
        return destination;
    }

    public ReplicateJobDetails setDestination(DataStore destination) {
        this.destination = destination;
        return this;
    }

    public Long getIngestId() {
        return ingestId;
    }

    public ReplicateJobDetails setIngestId(Long ingestId) {
        this.ingestId = ingestId;
        return this;
    }

    public String getExternalObjectId() {
        return externalObjectId;
    }

    public ReplicateJobDetails setExternalObjectId(String externalObjectId) {
        this.externalObjectId = externalObjectId;
        return this;
    }

    @Override
    public String toString() {
        return "ReplicateJobDetails{" +
                "replicateJobId=" + replicateJobId +
                ", jobId='" + jobId + '\'' +
                ", objectId='" + objectId + '\'' +
                ", persistenceVersion='" + persistenceVersion + '\'' +
                ", source=" + source +
                ", destination=" + destination +
                ", ingestId=" + ingestId +
                ", externalObjectId='" + externalObjectId + '\'' +
                '}';
    }

}
