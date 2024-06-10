package edu.wisc.library.sdg.preservation.manager.common;

import edu.wisc.library.sdg.preservation.common.util.ArgCheck;

public class OperationContext {

    private final String username;
    private final String orgName;
    private final String vault;

    public static OperationContext create(String username, String orgName) {
        return new OperationContext(username, orgName, null);
    }

    public static OperationContext create(String username, String orgName, String vault) {
        return new OperationContext(username, orgName, vault);
    }

    private OperationContext(String username, String orgName, String vault) {
        this.username = ArgCheck.notBlank(username, "username");
        this.orgName = ArgCheck.notBlank(orgName, "orgName");
        this.vault = vault;
    }

    public String getUsername() {
        return username;
    }

    public String getOrgName() {
        return orgName;
    }

    public String getVault() {
        return vault;
    }

    @Override
    public String toString() {
        return "OperationContext{" +
                "username='" + username + '\'' +
                ", orgName='" + orgName + '\'' +
                ", vault='" + vault + '\'' +
                '}';
    }

}
