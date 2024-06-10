package edu.wisc.library.sdg.preservation.manager.db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("organization")
public class Organization implements Persistable<String>{

    @Id
    @Column("org_name")
    private String orgName;

    @Column("display_name")
    private String displayName;

    @Column("contact_name")
    private String contactName;

    @Column("contact_email")
    private String contactEmail;

    @Column("contact_phone")
    private String contactPhone;

    @Column("created_timestamp")
    private LocalDateTime createdTimestamp;

    @Column("updated_timestamp")
    private LocalDateTime updatedTimestamp;

    @Transient
    private boolean isNew = false;

    public Organization() {
    }

    @PersistenceCreator
    public Organization(String orgName,
                        String displayName,
                        String contactName,
                        String contactEmail,
                        String contactPhone,
                        LocalDateTime createdTimestamp,
                        LocalDateTime updatedTimestamp) {
        this.orgName = orgName;
        this.displayName = displayName;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
        this.contactPhone = contactPhone;
        this.createdTimestamp = createdTimestamp;
        this.updatedTimestamp = updatedTimestamp;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @Override
    public String getId() {
        return orgName;
    }

    public String getOrgName() {
        return orgName;
    }

    public Organization setOrgName(String orgName) {
        this.orgName = orgName;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Organization setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public String getContactName() {
        return contactName;
    }

    public Organization setContactName(String contactName) {
        this.contactName = contactName;
        return this;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public Organization setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
        return this;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public Organization setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
        return this;
    }

    public LocalDateTime getCreatedTimestamp() {
        return createdTimestamp;
    }

    public Organization setCreatedTimestamp(LocalDateTime createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
        return this;
    }

    public LocalDateTime getUpdatedTimestamp() {
        return updatedTimestamp;
    }

    public Organization setUpdatedTimestamp(LocalDateTime updatedTimestamp) {
        this.updatedTimestamp = updatedTimestamp;
        return this;
    }

    public Organization setNew(boolean isNew) {
        this.isNew = isNew;
        return this;
    }

    @Override
    public String toString() {
        return "Organization{" +
                "orgName='" + orgName + '\'' +
                ", displayName='" + displayName + '\'' +
                ", contactName='" + contactName + '\'' +
                ", contactEmail='" + contactEmail + '\'' +
                ", createdTimestamp=" + createdTimestamp +
                ", updatedTimestamp=" + updatedTimestamp +
                ", isNew=" + isNew +
                '}';
    }

}
