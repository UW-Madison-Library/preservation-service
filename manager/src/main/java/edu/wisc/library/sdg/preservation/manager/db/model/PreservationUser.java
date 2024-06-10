package edu.wisc.library.sdg.preservation.manager.db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("preservation_user")
public class PreservationUser {

    @Id
    @Column("username")
    private String username;

    @Column("external_id")
    private String externalId;

    @Column("display_name")
    private String displayName;

    @Column("enabled")
    private boolean enabled;

    @Column("user_type")
    private PreservationUserType userType;

    @Column("created_timestamp")
    private LocalDateTime createdTimestamp;

    @Column("updated_timestamp")
    private LocalDateTime updatedTimestamp;

    public PreservationUser() {
    }

    @PersistenceCreator
    public PreservationUser(String username,
                            String externalId,
                            String displayName,
                            boolean enabled,
                            PreservationUserType userType,
                            LocalDateTime createdTimestamp,
                            LocalDateTime updatedTimestamp) {
        this.username = username;
        this.externalId = externalId;
        this.displayName = displayName;
        this.enabled = enabled;
        this.userType = userType;
        this.createdTimestamp = createdTimestamp;
        this.updatedTimestamp = updatedTimestamp;
    }

    public String getUsername() {
        return username;
    }

    public PreservationUser setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getExternalId() {
        return externalId;
    }

    public PreservationUser setExternalId(String externalId) {
        this.externalId = externalId;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public PreservationUser setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public PreservationUser setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public PreservationUserType getUserType() {
        return userType;
    }

    public PreservationUser setUserType(PreservationUserType userType) {
        this.userType = userType;
        return this;
    }

    public LocalDateTime getCreatedTimestamp() {
        return createdTimestamp;
    }

    public PreservationUser setCreatedTimestamp(LocalDateTime createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
        return this;
    }

    public LocalDateTime getUpdatedTimestamp() {
        return updatedTimestamp;
    }

    public PreservationUser setUpdatedTimestamp(LocalDateTime updatedTimestamp) {
        this.updatedTimestamp = updatedTimestamp;
        return this;
    }

    @Override
    public String toString() {
        return "PreservationUser{" +
                "username='" + username + '\'' +
                ", externalId='" + externalId + '\'' +
                ", displayName='" + displayName + '\'' +
                ", enabled=" + enabled +
                ", user_type=" + userType +
                ", createdTimestamp=" + createdTimestamp +
                ", updatedTimestamp=" + updatedTimestamp +
                '}';
    }
}
