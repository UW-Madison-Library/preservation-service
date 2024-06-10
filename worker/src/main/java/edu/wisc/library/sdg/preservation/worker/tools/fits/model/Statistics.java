package edu.wisc.library.sdg.preservation.worker.tools.fits.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public class Statistics {

    @JacksonXmlProperty(isAttribute = true, localName = "fitsExecutionTime")
    private Long fitsExecutionTime;

    @JacksonXmlProperty(isAttribute = false, localName = "tool")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<ToolStats> tools;

    public Long getFitsExecutionTime() {
        return fitsExecutionTime;
    }

    public void setFitsExecutionTime(Long fitsExecutionTime) {
        this.fitsExecutionTime = fitsExecutionTime;
    }

    public List<ToolStats> getTools() {
        return tools;
    }

    public void setTools(List<ToolStats> tools) {
        this.tools = tools;
    }

    @Override
    public String toString() {
        return "Statistics{" +
                "fitsExecutionTime=" + fitsExecutionTime +
                ", tools=" + tools +
                '}';
    }

}
