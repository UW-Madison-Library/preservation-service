package edu.wisc.library.sdg.preservation.manager.db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("vault_permission")
public class VaultPermission {

    @Id
    @Column("vault_permission_id")
    private Long vaultPermissionId;

    @Column("vault")
    private String vault;

    @Column("username")
    private String username;

    @Column("permission")
    private Permission permission;

    public VaultPermission() {
    }

    @PersistenceCreator
    public VaultPermission(Long vaultPermissionId, String vault, String username, Permission permission) {
        this.vaultPermissionId = vaultPermissionId;
        this.vault = vault;
        this.username = username;
        this.permission = permission;
    }

    public Long getVaultPermissionId() {
        return vaultPermissionId;
    }

    public VaultPermission setVaultPermissionId(Long vaultPermissionId) {
        this.vaultPermissionId = vaultPermissionId;
        return this;
    }

    public String getVault() {
        return vault;
    }

    public VaultPermission setVault(String vault) {
        this.vault = vault;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public VaultPermission setUsername(String username) {
        this.username = username;
        return this;
    }

    public Permission getPermission() {
        return permission;
    }

    public VaultPermission setPermission(Permission permission) {
        this.permission = permission;
        return this;
    }

    @Override
    public String toString() {
        return "VaultPermission{" +
                "vaultPermissionId=" + vaultPermissionId +
                ", vault='" + vault + '\'' +
                ", username='" + username + '\'' +
                ", permission=" + permission +
                '}';
    }

}
