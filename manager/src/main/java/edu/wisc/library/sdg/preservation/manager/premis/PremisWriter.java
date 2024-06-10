package edu.wisc.library.sdg.preservation.manager.premis;

import com.ctc.wstx.stax.WstxOutputFactory;
import edu.wisc.library.sdg.preservation.manager.db.model.Event;
import edu.wisc.library.sdg.preservation.manager.db.model.FormatRegistry;
import edu.wisc.library.sdg.preservation.manager.db.model.IngestEvent;
import edu.wisc.library.sdg.preservation.manager.db.model.PreservationEvent;
import edu.wisc.library.sdg.preservation.manager.db.model.PreservationObject;
import edu.wisc.library.sdg.preservation.manager.db.model.PreservationObjectFileFormatComposite;
import edu.wisc.library.sdg.preservation.manager.db.model.PreservationObjectVersionComposite;
import edu.wisc.library.sdg.preservation.manager.db.model.PreservationObjectVersionFileComposite;
import edu.wisc.library.sdg.preservation.manager.util.PrettyXMLStreamWriter;
import org.apache.commons.collections4.CollectionUtils;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PremisWriter implements AutoCloseable {

    private static final String PREMIS_NS = "http://www.loc.gov/premis/v3";
    private static final String XSI_NS = "http://www.w3.org/2001/XMLSchema-instance";
    private static final String SCHEMA_URL = "https://www.loc.gov/standards/premis/v3/premis-v3-0.xsd";

    private final OutputStream stream;
    private final XMLStreamWriter writer;
    private final Map<UUID, Event> events;

    public PremisWriter(OutputStream output) {
        this.stream = output;
        try {
            writer = new PrettyXMLStreamWriter(WstxOutputFactory.newFactory().createXMLStreamWriter(output));
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
        events = new HashMap<>();
    }

    public PremisWriter start() {
        write(writer::writeStartDocument);
        write(() -> writer.setDefaultNamespace(PREMIS_NS));
        write(() -> writer.writeStartElement(PREMIS_NS, "premis"));
        write(() -> writer.writeDefaultNamespace(PREMIS_NS));
        writeAttribute("version", "3.0");
        write(() -> writer.writeNamespace("xsi", XSI_NS));
        writeAttribute(XSI_NS, "schemaLocation", PREMIS_NS + " " + SCHEMA_URL);
        return this;
    }

    public PremisWriter finish() {
        events.values().stream()
                .sorted(Comparator.comparing(Event::getEventTimestamp))
                .forEach(this::writeEvent);

        write(writer::writeEndElement);
        write(writer::writeEndDocument);
        return this;
    }

    public PremisWriter addObject(PreservationObject object,
                                  List<Integer> versionNumbers,
                                  List<PreservationEvent> events) {
        writePremisElement("object", () -> {
            writeAttribute(XSI_NS, "type", "intellectualEntity");

            writeObjectIdentifier("external", object.getExternalObjectId());
            writeObjectIdentifier("vault", object.getVault());

            versionNumbers.forEach(this::writeVersionRelationship);

            addEvents(events);
        });

        return this;
    }

    public PremisWriter addVersion(PreservationObjectVersionComposite objectVersion, List<IngestEvent> events) {
        writeVersion(objectVersion, events);
        objectVersion.getFiles().stream()
                .sorted(Comparator.comparing(PreservationObjectVersionFileComposite::getFilePath))
                .forEach(this::writeFile);
        return this;
    }

    @Override
    public void close() {
        Exception exception = null;

        try {
            writer.close();
        } catch (XMLStreamException e) {
            exception = e;
        }
        try {
            stream.close();
        } catch (IOException e) {
            if (exception != null) {
                exception = e;
            }
        }

        if (exception != null) {
            throw new RuntimeException(exception);
        }
    }

    private void writeVersion(PreservationObjectVersionComposite version, List<IngestEvent> events) {
        writePremisElement("object", () -> {
            writeAttribute(XSI_NS, "type", "representation");

            writeObjectIdentifier("version", version.getVersion().toString());

            version.getFiles().stream()
                    .sorted(Comparator.comparing(PreservationObjectVersionFileComposite::getFilePath))
                    .forEach(file -> {
                        writeFileRelationship(file.getFilePath());
                    });

            addEvents(events);
        });
    }

    private void writeFile(PreservationObjectVersionFileComposite file) {
        writePremisElement("object", () -> {
            writeAttribute(XSI_NS, "type", "file");

            writeObjectIdentifier("local path", file.getFilePath());
            writePremisElement("objectCharacteristics", () -> {
                writePremisElement("fixity", () -> {
                    writePremisElementWithValue("messageDigestAlgorithm", "SHA-256");
                    writePremisElementWithValue("messageDigest", file.getSha256Digest());
                });
                writePremisElementWithValue("size", file.getFileSize().toString());

                writeFileFormats(file.getFormats());
            });
        });
    }

    private void writeFileFormats(List<PreservationObjectFileFormatComposite> formats) {
        if (CollectionUtils.isEmpty(formats)) {
            writeFormatDesignation("unknown");
        } else {
            formats.stream()
                    // sort for testing consistency
                    .sorted(Comparator.comparing(PreservationObjectFileFormatComposite::getFormat))
                    .forEach(format -> {
                if (format.getFormatRegistry() == FormatRegistry.MIME) {
                    writeFormatDesignation(format.getFormat());
                } else if (format.getFormatRegistry() == FormatRegistry.PRONOM) {
                    writePremisElement("format", () -> {
                        writePremisElement("formatRegistry", () -> {
                            writePremisElement("formatRegistryName", () -> {
                                writeAttribute("valueURI", "http://www.nationalarchives.gov.uk/pronom");
                                writeValue("PRONOM");
                            });
                            writePremisElementWithValue("formatRegistryKey", format.getFormat());
                        });
                    });
                }
            });
        }
    }

    private void writeFormatDesignation(String formatName) {
        writePremisElement("format", () -> {
            writePremisElement("formatDesignation", () -> {
                writePremisElementWithValue("formatName", formatName);
            });
        });
    }

    private void writeObjectIdentifier(String type, String id) {
        writePremisElement("objectIdentifier", () -> {
            writePremisElementWithValue("objectIdentifierType", type);
            writePremisElementWithValue("objectIdentifierValue", id);
        });
    }

    private void writeVersionRelationship(int versionNum) {
        writeRelationship("structural", "is Represented By", "version", String.valueOf(versionNum));
    }

    private void writeFileRelationship(String path) {
        writeRelationship("structural", "has Part", "local path", path);
    }

    private void writeLinkingAgentIdentifier(String type, String value, String roleUri, String role) {
        writePremisElement("linkingAgentIdentifier", () -> {
            writePremisElementWithValue("linkingAgentIdentifierType", type);
            writePremisElementWithValue("linkingAgentIdentifierValue", value);
            writePremisElement("linkingAgentRole", () -> {
                writeAttribute("valueURI", roleUri);
                writeValue(role);
            });
        });
    }

    private void writeRelationship(String type, String subType, String idType, String idValue) {
        writePremisElement("relationship", () -> {
            writePremisElementWithValue("relationshipType", type);
            writePremisElementWithValue("relationshipSubType", subType);

            writePremisElement("relatedObjectIdentifier", () -> {
                writePremisElementWithValue("relatedObjectIdentifierType", idType);
                writePremisElementWithValue("relatedObjectIdentifierValue", idValue);
            });
        });
    }

    private <T extends Event> void addEvents(List<T> events) {
        events.forEach(this::addEvent);
    }

    private void addEvent(Event event) {
        writePremisElement("linkingEventIdentifier", () -> {
            writePremisElementWithValue("linkingEventIdentifierType", "uuid");
            writePremisElementWithValue("linkingEventIdentifierValue", event.getExternalEventId().toString());
        });

        events.putIfAbsent(event.getExternalEventId(), event);
    }

    private void writeAttribute(String namespace, String name, String value) {
        write(() -> writer.writeAttribute(namespace, name, value));
    }

    private void writeAttribute(String name, String value) {
        write(() -> writer.writeAttribute(name, value));
    }

    private void writeValue(String value) {
        if (value != null) {
            write(() -> writer.writeCharacters(value));
        }
    }

    private void writePremisElementWithValue(String name, String value) {
        writeElementWithValue(PREMIS_NS, name, value);
    }

    private void writePremisElement(String name, RunnableWithException inner) {
        writeElement(PREMIS_NS, name, inner);
    }

    private void writeElementWithValue(String namespace, String name, String value) {
        writeElement(namespace, name, () -> writeValue(value));
    }

    private void writeElement(String namespace, String name, RunnableWithException inner) {
        write(() -> writer.writeStartElement(namespace, name));
        write(inner);
        write(writer::writeEndElement);
    }

    private void write(RunnableWithException runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void writeEvent(Event event) {
        writePremisElement("event", () -> {
            writePremisElement("eventIdentifier", () -> {
                writePremisElementWithValue("eventIdentifierType", "uuid");
                writePremisElementWithValue("eventIdentifierValue", event.getExternalEventId().toString());
            });

            writePremisElement("eventType", () -> {
                var premisType = event.getType().getPremisEventType();
                writeAttribute("valueURI", premisType.getUri());
                writeValue(premisType.getName());
            });

            writePremisElementWithValue("eventDateTime",
                    event.getEventTimestamp().atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));

            writePremisElement("eventOutcomeInformation", () -> {
                writePremisElementWithValue("eventOutcome", event.getOutcome().getPremisOutcome());

                if (CollectionUtils.isNotEmpty(event.getLogs())) {
                    writePremisElement("eventOutcomeDetail", () -> {
                        event.getLogs().forEach(log -> {
                            writePremisElementWithValue("eventOutcomeDetailNote", log.getMessage());
                        });
                    });
                }
            });

            if (event.getUsername() != null) {
                writeLinkingAgentIdentifier("username", event.getUsername(),
                        "http://id.loc.gov/vocabulary/preservation/eventRelatedAgentRole/aut", "authorizer");
            } else if (event.getAgent() != null) {
                writeLinkingAgentIdentifier("local", event.getAgent(),
                        "http://id.loc.gov/vocabulary/preservation/eventRelatedAgentRole/imp", "implementer");
            }
        });
    }

    private interface RunnableWithException {
        void run() throws Exception;
    }

}
