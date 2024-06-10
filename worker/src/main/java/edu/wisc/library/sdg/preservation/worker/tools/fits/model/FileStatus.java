package edu.wisc.library.sdg.preservation.worker.tools.fits.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public class FileStatus {

    @JacksonXmlProperty(isAttribute = false, localName = "well-formed")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<FitsValue> wellFormed;

    @JacksonXmlProperty(isAttribute = false, localName = "valid")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<FitsValue> valid;

    @JacksonXmlProperty(isAttribute = false, localName = "message")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<FitsValue> messages;

    public List<FitsValue> getWellFormed() {
        return wellFormed;
    }

    public void setWellFormed(List<FitsValue> wellFormed) {
        this.wellFormed = wellFormed;
    }

    public List<FitsValue> getValid() {
        return valid;
    }

    public void setValid(List<FitsValue> valid) {
        this.valid = valid;
    }

    public List<FitsValue> getMessages() {
        return messages;
    }

    public void setMessages(List<FitsValue> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "FileStatus{" +
                "wellFormed=" + wellFormed +
                ", valid=" + valid +
                ", message=" + messages +
                '}';
    }

}
