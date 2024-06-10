package edu.wisc.library.sdg.preservation.manager.db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("retrieve_request")
public class RetrieveRequest {

    @Id
    @Column("retrieve_request_id")
    private Long retrieveRequestId;

    @Column("username")
    private String username;

    @Column("vault")
    private String vault;

    @Column("external_object_ids")
    private String externalObjectIds;

    @Column("all_versions")
    private boolean allVersions;

    @Column("deleted")
    private boolean deleted;

    @Column("created_timestamp")
    private LocalDateTime createdTimestamp;

    @Column("deleted_timestamp")
    private LocalDateTime deletedTimestamp;

    public RetrieveRequest() {

    }

    @PersistenceCreator
    public RetrieveRequest(Long retrieveRequestId,
                           String username,
                           String vault,
                           String externalObjectIds,
                           boolean allVersions,
                           boolean deleted,
                           LocalDateTime createdTimestamp,
                           LocalDateTime deletedTimestamp) {
        this.retrieveRequestId = retrieveRequestId;
        this.username = username;
        this.vault = vault;
        this.externalObjectIds = externalObjectIds;
        this.allVersions = allVersions;
        this.deleted = deleted;
        this.createdTimestamp = createdTimestamp;
        this.deletedTimestamp = deletedTimestamp;
    }

    public Long getRetrieveRequestId() {
        return retrieveRequestId;
    }

    public RetrieveRequest setRetrieveRequestId(Long retrieveRequestId) {
        this.retrieveRequestId = retrieveRequestId;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public RetrieveRequest setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getVault() {
        return vault;
    }

    public RetrieveRequest setVault(String vault) {
        this.vault = vault;
        return this;
    }

    public String getExternalObjectIds() {
        return externalObjectIds;
    }

    public RetrieveRequest setExternalObjectIds(String externalObjectIds) {
        this.externalObjectIds = externalObjectIds;
        return this;
    }

    public boolean isAllVersions() {
        return allVersions;
    }

    public RetrieveRequest setAllVersions(boolean allVersions) {
        this.allVersions = allVersions;
        return this;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public RetrieveRequest setDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public LocalDateTime getCreatedTimestamp() {
        return createdTimestamp;
    }

    public RetrieveRequest setCreatedTimestamp(LocalDateTime createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
        return this;
    }

    public LocalDateTime getDeletedTimestamp() {
        return deletedTimestamp;
    }

    public RetrieveRequest setDeletedTimestamp(LocalDateTime deletedTimestamp) {
        this.deletedTimestamp = deletedTimestamp;
        return this;
    }

    @Override
    public String toString() {
        return "RetrieveRequest{" +
                "retrieveRequestId=" + retrieveRequestId +
                ", username='" + username + '\'' +
                ", vault='" + vault + '\'' +
                ", externalObjectIds='" + externalObjectIds + '\'' +
                ", allVersions=" + allVersions +
                ", deleted=" + deleted +
                ", createdTimestamp=" + createdTimestamp +
                ", deletedTimestamp=" + deletedTimestamp +
                '}';
    }
}
