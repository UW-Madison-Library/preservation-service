package edu.wisc.library.sdg.preservation.worker.tools.fits.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

public class ExternalIdentifier {

    @JacksonXmlProperty(isAttribute = true, localName = "toolname")
    private String toolName;

    @JacksonXmlProperty(isAttribute = true, localName = "toolversion")
    private String toolVersion;

    @JacksonXmlProperty(isAttribute = true, localName = "status")
    private Status status;

    @JacksonXmlProperty(isAttribute = true, localName = "type")
    private String type;

    @JacksonXmlText
    private String value;

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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @JsonIgnore
    public String getToolId() {
        return String.format("%s-%s", toolName, toolVersion);
    }

    @Override
    public String toString() {
        return "ExternalIdentifier{" +
                "toolName='" + toolName + '\'' +
                ", toolVersion='" + toolVersion + '\'' +
                ", status=" + status +
                ", type='" + type + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

}
