package edu.wisc.library.sdg.preservation.worker.tools.fits.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public class Identification {

    @JacksonXmlProperty(isAttribute = true, localName = "status")
    private Status status;

    @JacksonXmlProperty(isAttribute = false, localName = "identity")
    private List<Identity> identities;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Identity> getIdentities() {
        return identities;
    }

    public void setIdentities(List<Identity> identities) {
        this.identities = identities;
    }

    @Override
    public String toString() {
        return "Identification{" +
                "status=" + status +
                ", identities=" + identities +
                '}';
    }

}
