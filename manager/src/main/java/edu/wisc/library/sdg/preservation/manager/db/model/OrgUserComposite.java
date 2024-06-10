package edu.wisc.library.sdg.preservation.manager.db.model;

public class OrgUserComposite {

    private String username;
    private String externalId;
    private String displayName;
    private PreservationUserType userType;
    private OrgRole role;
    private boolean enabled;
    private boolean enabledInOrg;

    public String getUsername() {
        return username;
    }

    public OrgUserComposite setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getExternalId() {
        return externalId;
    }

    public OrgUserComposite setExternalId(String externalId) {
        this.externalId = externalId;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public OrgUserComposite setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public PreservationUserType getUserType() {
        return userType;
    }

    public OrgUserComposite setUserType(PreservationUserType userType) {
        this.userType = userType;
        return this;
    }

    public OrgRole getRole() {
        return role;
    }

    public OrgUserComposite setRole(OrgRole role) {
        this.role = role;
        return this;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public OrgUserComposite setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public boolean isEnabledInOrg() {
        return enabledInOrg;
    }

    public OrgUserComposite setEnabledInOrg(boolean enabledInOrg) {
        this.enabledInOrg = enabledInOrg;
        return this;
    }

    @Override
    public String toString() {
        return "OrgUserComposite{" +
                "username='" + username + '\'' +
                ", externalId='" + externalId + '\'' +
                ", displayName='" + displayName + '\'' +
                ", userType=" + userType +
                ", role=" + role +
                ", enabled=" + enabled +
                ", enabledInOrg=" + enabledInOrg +
                '}';
    }

}
