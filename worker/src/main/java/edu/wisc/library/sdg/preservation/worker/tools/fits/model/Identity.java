package edu.wisc.library.sdg.preservation.worker.tools.fits.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public class Identity {

    @JacksonXmlProperty(isAttribute = true, localName = "format")
    private String format;

    @JacksonXmlProperty(isAttribute = true, localName = "mimetype")
    private String mimeType;

    @JacksonXmlProperty(isAttribute = true, localName = "toolname")
    private String toolName;

    @JacksonXmlProperty(isAttribute = true, localName = "toolversion")
    private String toolVersion;

    @JacksonXmlProperty(isAttribute = false, localName = "tool")
    private List<Tool> tools;

    @JacksonXmlProperty(isAttribute = false, localName = "version")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<FitsValue> versions;

    @JacksonXmlProperty(isAttribute = false, localName = "externalIdentifier")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<ExternalIdentifier> externalIdentifiers;

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getToolName() {
        return toolName;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    public String getToolVersion() {
        return toolVersion;
    }

    public void setToolVersion(String toolVersion) {
        this.toolVersion = toolVersion;
    }

    public List<Tool> getTools() {
        return tools;
    }

    public void setTools(List<Tool> tools) {
        this.tools = tools;
    }

    public List<FitsValue> getVersions() {
        return versions;
    }

    public void setVersions(List<FitsValue> versions) {
        this.versions = versions;
    }

    public List<ExternalIdentifier> getExternalIdentifiers() {
        return externalIdentifiers;
    }

    public void setExternalIdentifiers(List<ExternalIdentifier> externalIdentifiers) {
        this.externalIdentifiers = externalIdentifiers;
    }

    @JsonIgnore
    public String getToolId() {
        return String.format("%s-%s", toolName, toolVersion);
    }

    @Override
    public String toString() {
        return "Identity{" +
                "format='" + format + '\'' +
                ", mimeType='" + mimeType + '\'' +
                ", toolName='" + toolName + '\'' +
                ", toolVersion='" + toolVersion + '\'' +
                ", tools=" + tools +
                ", versions=" + versions +
                ", externalIdentifiers=" + externalIdentifiers +
                '}';
    }

}
