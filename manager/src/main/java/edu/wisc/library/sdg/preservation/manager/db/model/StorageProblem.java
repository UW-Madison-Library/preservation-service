package edu.wisc.library.sdg.preservation.manager.db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Table("storage_problem")
public class StorageProblem {

    @Id
    @Column("storage_problem_id")
    private Long storageProblemId;

    @Column("object_id")
    private UUID objectId;

    @Column("datastore")
    private DataStore dataStore;

    @Column("persistence_version")
    private String persistenceVersion;

    @Column("problem")
    private StorageProblemType problem;

    @Column("reported_timestamp")
    private LocalDateTime reportedTimestamp;

    public StorageProblem() {
    }

    @PersistenceCreator
    public StorageProblem(Long storageProblemId,
                          UUID objectId,
                          DataStore dataStore,
                          String persistenceVersion,
                          StorageProblemType problem,
                          LocalDateTime reportedTimestamp) {
        this.storageProblemId = storageProblemId;
        this.objectId = objectId;
        this.dataStore = dataStore;
        this.persistenceVersion = persistenceVersion;
        this.problem = problem;
        this.reportedTimestamp = reportedTimestamp;
    }

    public Long getStorageProblemId() {
        return storageProblemId;
    }

    public StorageProblem setStorageProblemId(Long storageProblemId) {
        this.storageProblemId = storageProblemId;
        return this;
    }

    public UUID getObjectId() {
        return objectId;
    }

    public StorageProblem setObjectId(UUID objectId) {
        this.objectId = objectId;
        return this;
    }

    public DataStore getDataStore() {
        return dataStore;
    }

    public StorageProblem setDataStore(DataStore dataStore) {
        this.dataStore = dataStore;
        return this;
    }

    public String getPersistenceVersion() {
        return persistenceVersion;
    }

    public StorageProblem setPersistenceVersion(String persistenceVersion) {
        this.persistenceVersion = persistenceVersion;
        return this;
    }

    public StorageProblemType getProblem() {
        return problem;
    }

    public StorageProblem setProblem(StorageProblemType problem) {
        this.problem = problem;
        return this;
    }

    public LocalDateTime getReportedTimestamp() {
        return reportedTimestamp;
    }

    public StorageProblem setReportedTimestamp(LocalDateTime reportedTimestamp) {
        this.reportedTimestamp = reportedTimestamp;
        return this;
    }

    @Override
    public String toString() {
        return "StorageProblem{" +
                "storageProblemId=" + storageProblemId +
                ", objectId='" + objectId + '\'' +
                ", datastore=" + dataStore +
                ", persistenceVersion='" + persistenceVersion + '\'' +
                ", problem=" + problem +
                ", reportedTimestamp=" + reportedTimestamp +
                '}';
    }
}
