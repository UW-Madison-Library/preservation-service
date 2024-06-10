package edu.wisc.library.sdg.preservation.manager.db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("vault")
public class Vault implements Persistable<String>{

    @Id
    @Column("name")
    private String name;

    @Column("description")
    private String description;

    @Column("org_name")
    private String orgName;

    @Column("objects")
    private Long objects;

    @Column("storage_mb")
    private Long storageMb;

    @Column("created_timestamp")
    private LocalDateTime createdTimestamp;

    @Column("updated_timestamp")
    private LocalDateTime updatedTimestamp;

    @Transient
    private boolean isNew = false;

    public Vault() {
        this.objects = 0L;
        this.storageMb = 0L;
    }

    @PersistenceCreator
    public Vault(String name,
                 String orgName,
                 LocalDateTime createdTimestamp,
                 LocalDateTime updatedTimestamp) {
      this.name = name;
      this.orgName = orgName;
      this.createdTimestamp = createdTimestamp;
      this.updatedTimestamp = updatedTimestamp;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @Override
    public String getId() {
        return getName();
    }

    public String getName() {
        return name;
    }

    public Vault setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Vault setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getOrgName() {
        return orgName;
    }

    public Vault setOrgName(String orgName) {
        this.orgName = orgName;
        return this;
    }

    public Long getObjects() {
        return objects;
    }

    public Vault setObjects(Long objects) {
        this.objects = objects;
        return this;
    }

    public Long getStorageMb() {
        return storageMb;
    }

    public Vault setStorageMb(Long storageMb) {
        this.storageMb = storageMb;
        return this;
    }

    public LocalDateTime getCreatedTimestamp() {
        return createdTimestamp;
    }

    public Vault setCreatedTimestamp(LocalDateTime createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
        return this;
    }

    public LocalDateTime getUpdatedTimestamp() {
        return updatedTimestamp;
    }

    public Vault setUpdatedTimestamp(LocalDateTime updatedTimestamp) {
        this.updatedTimestamp = updatedTimestamp;
        return this;
    }

    public Vault setNew(boolean isNew) {
        this.isNew = isNew;
        return this;
    }

    @Override
    public String toString() {
        return "Vault{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", orgName='" + orgName + '\'' +
                ", objects=" + objects +
                ", storageMb=" + storageMb +
                ", createdTimestamp=" + createdTimestamp +
                ", updatedTimestamp=" + updatedTimestamp +
                '}';
    }
}
