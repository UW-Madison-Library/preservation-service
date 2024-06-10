package edu.wisc.library.sdg.preservation.manager.itest;

import org.junit.jupiter.api.Test;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PremisITest extends ITestBase {

    @Test
    public void getPremisDocumentSingleVersion() {
        setupBaseline();

        var premisFile = getPremisDocument(defaultObject1);

        validateSchema(premisFile);

        var expected = toString(expectedFile("premis-single-version.xml"));

        var actual = toString(premisFile.toPath());
        actual = replaceEventIds(actual);
        actual = removeTimestamps(actual);

        assertEquals(expected, actual);
    }

    @Test
    public void getPremisDocumentMultipleVersion() {
        setupBaseline();

        var premisFile = getPremisDocument(defaultObject1, defaultObject1v2);

        validateSchema(premisFile);

        var expected = toString(expectedFile("premis-multiple-versions.xml"));

        var actual = toString(premisFile.toPath());
        actual = replaceEventIds(actual);
        actual = removeTimestamps(actual);

        assertEquals(expected, actual);
    }

    private Path expectedFile(String name) {
        return Paths.get("src/test/resources/premis", name);
    }

    private String toString(Path file) {
        try {
            return new String(Files.readAllBytes(file));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void validateSchema(File file) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(Paths.get("src/test/resources/premis/premis-3.0.xsd").toFile());
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(file));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String removeTimestamps(String xml) {
        return xml.replaceAll("<eventDateTime>[^<]+", "<eventDateTime>");
    }

    private String replaceEventIds(String xml) {
        var ids = extractEventIds(xml);
        var i = 1;

        var modified = xml;

        for (var id : ids) {
            modified = modified.replaceAll(id, Integer.toString(i));
            i++;
        }

        return modified;
    }

    private List<String> extractEventIds(String xml) {
        var pattern = Pattern.compile("<eventIdentifierValue>([^<]+)</eventIdentifierValue>");
        var matcher = pattern.matcher(xml);

        var ids = new ArrayList<String>();

        while (matcher.find()) {
            ids.add(matcher.group(1));
        }

        return ids;
    }

}
