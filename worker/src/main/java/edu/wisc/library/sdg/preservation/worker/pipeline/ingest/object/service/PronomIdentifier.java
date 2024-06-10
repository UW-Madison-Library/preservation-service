package edu.wisc.library.sdg.preservation.worker.pipeline.ingest.object.service;

import edu.wisc.library.sdg.preservation.worker.tools.fits.model.FitsValue;
import nu.xom.Builder;
import nu.xom.ParsingException;
import nu.xom.XPathContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;

@Component
public class PronomIdentifier {
    private static final String DROID_TOOL_NAME = "Droid";
    private static final String FITS_NS = "http://hul.harvard.edu/ois/xml/ns/fits/fits_output";
    private static final String DROID_PRONOM = "/fits:fits/fits:toolOutput/fits:tool[@name='Droid']/results/result/filePuid";
    private static final String DROID_VERSION = "/fits:fits/fits:toolOutput/fits:tool[@name='Droid']/@version";

    private static final Logger LOG = LoggerFactory.getLogger(PronomIdentifier.class);

    public FitsValue getDroidPronom(final Path fitsOutputPath, final String fitsFilename) {
        FitsValue rawDroidOutput = new FitsValue();
        rawDroidOutput.setToolName(DROID_TOOL_NAME);

        try {
            var fitsFile = fitsOutputPath.resolve(fitsFilename).toFile();
            var fitsXML = new Builder().build(fitsFile);

            var xpathContext = XPathContext.makeNamespaceContext(fitsXML.getRootElement());
            xpathContext.addNamespace("fits", FITS_NS);

            var pronomNodes = fitsXML.query(DROID_PRONOM, xpathContext);
            var versionNodes = fitsXML.query(DROID_VERSION, xpathContext);

            if (pronomNodes.size() > 0 && versionNodes.size() > 0) {
                rawDroidOutput.setValue(pronomNodes.get(0).getValue());
                rawDroidOutput.setToolVersion(versionNodes.get(0).getValue());
            }
        } catch (ParsingException | IOException exception) {
            LOG.error("Failed to extract Droid PRONOM", exception);
        }

        return rawDroidOutput;
    }
}
