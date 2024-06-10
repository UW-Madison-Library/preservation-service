package edu.wisc.library.sdg.preservation.worker.pipeline.ingest.object.postprocessor;

import nu.xom.Builder;
import nu.xom.ParsingException;
import nu.xom.Serializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class PhotoshopPropertiesFilterTest {

    private PhotoshopPropertiesFilter photoshopPropertiesFilter;
    private Path testFilePath;
    private String outputFilename;

    @BeforeEach
    public void setup() {
        photoshopPropertiesFilter = new PhotoshopPropertiesFilter();
        testFilePath = Path.of("src/test/resources/fits/postprocessor/");
        outputFilename = "test_output.fits.xml";
    }

    @AfterEach
    public void teardown() throws IOException {
        Files.deleteIfExists(testFilePath.resolve(outputFilename));
    }

    @Test
    public void stripsPhotoshopPropertiesFromTiffFits() throws IOException, ParsingException {
        photoshopPropertiesFilter.process(testFilePath, "has_ps_props.tif.fits.xml", outputFilename);

        var outputData = getFormattedData(outputFilename);
        var expectedData = getFormattedData("no_ps_props.tif.fits.xml");

        Assertions.assertEquals(expectedData, outputData);
    }

    @Test
    public void noOpIfNonTiff() {
        photoshopPropertiesFilter.process(testFilePath, "non_tiff.txt.fits.xml", outputFilename);
        Assertions.assertFalse(Files.exists(testFilePath.resolve(outputFilename)));
    }

    @Test
    public void noOpIfNoPSProperties() {
        photoshopPropertiesFilter.process(testFilePath, "no_ps_props.tif.fits.xml", outputFilename);
        Assertions.assertFalse(Files.exists(testFilePath.resolve(outputFilename)));
    }

    private String getFormattedData(String filename) throws IOException, ParsingException {
        var document = new Builder().build(testFilePath.resolve(filename).toFile());
        var baos = new ByteArrayOutputStream();
        var serializer = new Serializer(baos);
        serializer.write(document);
        return baos.toString();
    }
}
