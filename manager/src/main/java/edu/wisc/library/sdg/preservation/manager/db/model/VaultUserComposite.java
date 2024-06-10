package edu.wisc.library.sdg.preservation.manager.db.model;

import java.util.ArrayList;
import java.util.List;

public class VaultUserComposite {

    private String username;
    private String externalId;
    private String displayName;
    private PreservationUserType userType;
    private OrgRole role;
    private boolean enabled;
    private boolean enabledInOrg;
    // This is populated by the query
    private Permission permission;
    // This is must be constructed by collapsing the query results
    private List<Permission> permissions = new ArrayList<>();

    public String getUsername() {
        return username;
    }

    public VaultUserComposite setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getExternalId() {
        return externalId;
    }

    public VaultUserComposite setExternalId(String externalId) {
        this.externalId = externalId;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public VaultUserComposite setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public PreservationUserType getUserType() {
        return userType;
    }

    public VaultUserComposite setUserType(PreservationUserType userType) {
        this.userType = userType;
        return this;
    }

    public OrgRole getRole() {
        return role;
    }

    public VaultUserComposite setRole(OrgRole role) {
        this.role = role;
        return this;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public VaultUserComposite setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public boolean isEnabledInOrg() {
        return enabledInOrg;
    }

    public VaultUserComposite setEnabledInOrg(boolean enabledInOrg) {
        this.enabledInOrg = enabledInOrg;
        return this;
    }

    public Permission getPermission() {
        return permission;
    }

    public VaultUserComposite setPermission(Permission permission) {
        this.permission = permission;
        return this;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public void addPermission(Permission permission) {
        this.permissions.add(permission);
    }

    @Override
    public String toString() {
        return "VaultUserComposite{" +
                "username='" + username + '\'' +
                ", externalId='" + externalId + '\'' +
                ", displayName='" + displayName + '\'' +
                ", userType=" + userType +
                ", role=" + role +
                ", enabled=" + enabled +
                ", enabledInOrg=" + enabledInOrg +
                ", permission=" + permission +
                ", permissions=" + permissions +
                '}';
    }

}
