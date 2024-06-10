package edu.wisc.library.sdg.preservation.worker.tools.fits.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

public class FitsValue {

    @JacksonXmlProperty(isAttribute = true, localName = "toolname")
    private String toolName;

    @JacksonXmlProperty(isAttribute = true, localName = "toolversion")
    private String toolVersion;

    @JacksonXmlProperty(isAttribute = true, localName = "status")
    private Status status;

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

    @JsonIgnore
    public String getToolId() {
        return String.format("%s-%s", toolName, toolVersion);
    }

    @Override
    public String toString() {
        return "FitsValue{" +
                "toolName='" + toolName + '\'' +
                ", toolVersion='" + toolVersion + '\'' +
                ", status=" + status +
                ", value='" + value + '\'' +
                '}';
    }

}
