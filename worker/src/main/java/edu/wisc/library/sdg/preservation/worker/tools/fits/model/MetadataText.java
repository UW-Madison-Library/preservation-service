package edu.wisc.library.sdg.preservation.worker.tools.fits.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

/**
 * Minimal representation of FITS text metadata -- exact contents is unspecified
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MetadataText {

    @JacksonXmlProperty(isAttribute = false, localName = "charset")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<FitsValue> charset;

    public List<FitsValue> getCharset() {
        return charset;
    }

    public void setCharset(List<FitsValue> charset) {
        this.charset = charset;
    }

    @Override
    public String toString() {
        return "MetadataText{" +
                "charset=" + charset +
                '}';
    }
}
