package edu.wisc.library.sdg.preservation.common.model;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;

/**
 * System metadata about an object that's stored inside the OCFL object
 */
public class PreservationMetadata {

    public enum Version {
        V1_0("1.0");

        private String value;

        private Version(String value) {
            this.value = value;
        }

        @JsonValue
        @Override
        public String toString() {
            return value;
        }
    }

    private Version metadataVersion;
    private String vault;
    private String externalObjectId;
    private Integer version;

    public Version getMetadataVersion() {
        return metadataVersion;
    }

    public PreservationMetadata setMetadataVersion(Version metadataVersion) {
        this.metadataVersion = metadataVersion;
        return this;
    }

    public String getVault() {
        return vault;
    }

    public PreservationMetadata setVault(String vault) {
        this.vault = vault;
        return this;
    }

    public String getExternalObjectId() {
        return externalObjectId;
    }

    public PreservationMetadata setExternalObjectId(String externalObjectId) {
        this.externalObjectId = externalObjectId;
        return this;
    }

    public Integer getVersion() {
        return version;
    }

    public PreservationMetadata setVersion(Integer version) {
        this.version = version;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PreservationMetadata that = (PreservationMetadata) o;
        return metadataVersion == that.metadataVersion
                && Objects.equals(vault, that.vault)
                && Objects.equals(externalObjectId, that.externalObjectId)
                && Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(metadataVersion, vault, externalObjectId, version);
    }

    @Override
    public String toString() {
        return "PreservationMetadata{" +
                "metadataVersion=" + metadataVersion +
                ", vault='" + vault + '\'' +
                ", externalObjectId='" + externalObjectId + '\'' +
                ", version=" + version +
                '}';
    }

}
