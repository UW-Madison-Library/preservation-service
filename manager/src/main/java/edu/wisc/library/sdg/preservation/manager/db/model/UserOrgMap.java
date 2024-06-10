package edu.wisc.library.sdg.preservation.manager.db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("user_organization_map")
public class UserOrgMap {

    @Id
    @Column("user_org_id")
    private Long userOrgId;

    @Column("username")
    private String username;

    @Column("org_name")
    private String orgName;

    @Column("role")
    private OrgRole role;

    @Column("enabled")
    private boolean enabled;

    @Column("created_timestamp")
    private LocalDateTime createdTimestamp;

    @Column("updated_timestamp")
    private LocalDateTime updatedTimestamp;

    public UserOrgMap() {

    }

    @PersistenceCreator
    public UserOrgMap(Long userOrgId, String username,
                      String orgName,
                      OrgRole role,
                      boolean enabled,
                      LocalDateTime createdTimestamp,
                      LocalDateTime updatedTimestamp) {
        this.userOrgId = userOrgId;
        this.username = username;
        this.role = role;
        this.orgName = orgName;
        this.enabled = enabled;
        this.createdTimestamp = createdTimestamp;
        this.updatedTimestamp = updatedTimestamp;
    }

    public Long getUserOrgId() {
        return userOrgId;
    }

    public UserOrgMap setUserOrgId(Long userOrgId) {
        this.userOrgId = userOrgId;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserOrgMap setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getOrgName() {
        return orgName;
    }

    public UserOrgMap setOrgName(String orgName) {
        this.orgName = orgName;
        return this;
    }

    public OrgRole getRole() {
        return role;
    }

    public UserOrgMap setRole(OrgRole role) {
        this.role = role;
        return this;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public UserOrgMap setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public LocalDateTime getCreatedTimestamp() {
        return createdTimestamp;
    }

    public UserOrgMap setCreatedTimestamp(LocalDateTime createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
        return this;
    }

    public LocalDateTime getUpdatedTimestamp() {
        return updatedTimestamp;
    }

    public UserOrgMap setUpdatedTimestamp(LocalDateTime updatedTimestamp) {
        this.updatedTimestamp = updatedTimestamp;
        return this;
    }

    @Override
    public String toString() {
        return "UserOrganizationMap{" +
                "userOrgId=" + userOrgId +
                ", username='" + username + '\'' +
                ", orgName='" + orgName + '\'' +
                ", role='" + role + '\'' +
                ", enabled=" + enabled +
                ", createdTimestamp=" + createdTimestamp +
                ", updatedTimestamp=" + updatedTimestamp +
                '}';
    }

}
