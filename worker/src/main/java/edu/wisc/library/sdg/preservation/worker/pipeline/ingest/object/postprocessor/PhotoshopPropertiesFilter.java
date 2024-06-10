package edu.wisc.library.sdg.preservation.worker.pipeline.ingest.object.postprocessor;

import nu.xom.Builder;
import nu.xom.Element;
import nu.xom.ParsingException;
import nu.xom.Serializer;
import nu.xom.Text;
import nu.xom.XPathContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

@Component
public class PhotoshopPropertiesFilter implements FitsPostProcessor {
    private static final String FITS_NS = "http://hul.harvard.edu/ois/xml/ns/fits/fits_output";
    private static final String JHOVE_PS_PROPS_XPATH = "/fits:fits/fits:toolOutput/fits:tool[@name='Jhove']" +
            "/repInfo/properties/property[name[1]='TIFFMetadata']/values/property[name[1]='IFDs']" +
            "/values/property[name[1]='IFD']/values/property[name[1]='Entries']/values";

    private static final Logger LOG = LoggerFactory.getLogger(PhotoshopPropertiesFilter.class);

    @Override
    public void process(Path fitsOutputPath, String fitsFilename) {
        process(fitsOutputPath, fitsFilename, fitsFilename);
    }

    @Override
    public void process(Path fitsOutputPath, String fitsFilename, String outputFilename) {
        try {
            var fitsFile = fitsOutputPath.resolve(fitsFilename).toFile();
            LOG.debug("Running {} against {}", this.getClass().getSimpleName(), fitsFile);

            var fitsXML = new Builder().build(fitsFile);
            var xpathContext = XPathContext.makeNamespaceContext(fitsXML.getRootElement());
            xpathContext.addNamespace("fits", FITS_NS);

            boolean updated = false;

            var jhoveIFDs = fitsXML.query(JHOVE_PS_PROPS_XPATH, xpathContext);
            for (var ifdList : jhoveIFDs) {
                if (ifdList instanceof Element ifds) {
                    for (var property : ifds.getChildElements("property")) {
                        var propertyName = property.getFirstChildElement("name");
                        if (propertyName != null && "PhotoshopProperties".equals(propertyName.getValue())) {
                            var childIndex = ifds.indexOf(property);
                            var followingChild = ifds.getChild(childIndex + 1);
                            if (followingChild instanceof Text followingChildText && followingChildText.getValue().isBlank()) {
                                ifds.removeChild(childIndex + 1);
                            }
                            ifds.removeChild(childIndex);
                            updated = true;
                        }
                    }
                }
            }

            if (updated) {
                var outputFile = fitsOutputPath.resolve(outputFilename).toFile();
                try (FileOutputStream outputStream = new FileOutputStream(outputFile, false)) {
                    var serializer = new Serializer(outputStream);
                    serializer.write(fitsXML);
                }
            }
        } catch (IOException | ParsingException e) {
            LOG.error("Failed to run post processing for {} in {}: {}", fitsFilename, fitsOutputPath.toUri(), e.getMessage());
        }
    }
}
