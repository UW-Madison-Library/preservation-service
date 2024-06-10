package edu.wisc.library.sdg.preservation.worker.tools.fits.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class ToolStats {

    @JacksonXmlProperty(isAttribute = true, localName = "toolname")
    private String toolName;

    @JacksonXmlProperty(isAttribute = true, localName = "toolversion")
    private String toolVersion;

    @JacksonXmlProperty(isAttribute = true, localName = "status")
    private ToolStatus status;

    @JacksonXmlProperty(isAttribute = true, localName = "executionTime")
    private Long executionTime;

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

    public ToolStatus getStatus() {
        return status;
    }

    public void setStatus(ToolStatus status) {
        this.status = status;
    }

    public Long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Long executionTime) {
        this.executionTime = executionTime;
    }

    @JsonIgnore
    public String getToolId() {
        return String.format("%s-%s", toolName, toolVersion);
    }

    @Override
    public String toString() {
        return "ToolStats{" +
                "toolName='" + toolName + '\'' +
                ", toolVersion='" + toolVersion + '\'' +
                ", status=" + status +
                ", executionTime=" + executionTime +
                '}';
    }

}
