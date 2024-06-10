package edu.wisc.library.sdg.preservation.worker.tools.fits.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "fits", namespace = "http://hul.harvard.edu/ois/xml/ns/fits/fits_output")
@JsonIgnoreProperties(ignoreUnknown = true)
public class FitsReport {

    @JacksonXmlProperty(isAttribute = true, localName = "schemaLocation", namespace = "xsi")
    private String schemaLocation;

    @JacksonXmlProperty(isAttribute = true, localName = "version")
    private String version;

    @JacksonXmlProperty(isAttribute = true, localName = "timestamp")
    private String timestamp;

    @JacksonXmlProperty(isAttribute = false, localName = "identification")
    private Identification identification;

    @JacksonXmlProperty(isAttribute = false, localName = "fileinfo")
    private FileInfo fileInfo;

    @JacksonXmlProperty(isAttribute = false, localName = "filestatus")
    private FileStatus fileStatus;

    @JacksonXmlProperty(isAttribute = false, localName = "metadata")
    private Metadata metadata;

    @JacksonXmlProperty(isAttribute = false, localName = "statistics")
    private Statistics statistics;

    public String getSchemaLocation() {
        return schemaLocation;
    }

    public void setSchemaLocation(String schemaLocation) {
        this.schemaLocation = schemaLocation;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Identification getIdentification() {
        return identification;
    }

    public void setIdentification(Identification identification) {
        this.identification = identification;
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }

    public FileStatus getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(FileStatus fileStatus) {
        this.fileStatus = fileStatus;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    @JsonIgnore
    public String getId() {
        return "FITS-" + version;
    }

    @Override
    public String toString() {
        return "FitsReport{" +
                "schemaLocation='" + schemaLocation + '\'' +
                ", version='" + version + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", identification=" + identification +
                ", fileInfo=" + fileInfo +
                ", fileStatus=" + fileStatus +
                ", metadata=" + metadata +
                ", statistics=" + statistics +
                '}';
    }

}
