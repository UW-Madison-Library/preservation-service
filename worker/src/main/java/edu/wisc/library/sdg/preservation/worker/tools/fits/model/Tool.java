package edu.wisc.library.sdg.preservation.worker.tools.fits.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Tool {

    @JacksonXmlProperty(isAttribute = true, localName = "toolname")
    private String toolName;

    @JacksonXmlProperty(isAttribute = true, localName = "toolversion")
    private String toolVersion;

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

    @JsonIgnore
    public String getToolId() {
        return String.format("%s-%s", toolName, toolVersion);
    }

    @Override
    public String toString() {
        return "Tool{" +
                "toolName='" + toolName + '\'' +
                ", toolVersion='" + toolVersion + '\'' +
                '}';
    }

}
