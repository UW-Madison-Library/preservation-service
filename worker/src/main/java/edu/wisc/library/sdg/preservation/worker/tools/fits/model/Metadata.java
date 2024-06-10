package edu.wisc.library.sdg.preservation.worker.tools.fits.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * Minimal representation of FITS metadata -- exact contents is unspecified
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Metadata {

    @JacksonXmlProperty(isAttribute = false, localName = "text")
    private MetadataText text;

    public MetadataText getText() {
        return text;
    }

    public void setText(MetadataText text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Metadata{" +
                "text=" + text +
                '}';
    }
}
