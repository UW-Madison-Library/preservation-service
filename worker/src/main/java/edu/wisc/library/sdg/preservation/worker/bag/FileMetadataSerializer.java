package edu.wisc.library.sdg.preservation.worker.bag;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class FileMetadataSerializer {

    private final ObjectReader reader;
    private final ObjectWriter writer;

    public FileMetadataSerializer() {
        var mapper = (CsvMapper) new CsvMapper()
                .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

        var schema = mapper.schemaFor(FileMetadata.class)
                .withArrayElementSeparator("|")
                .withHeader()
                .withStrictHeaders(true);

        reader = mapper.readerFor(FileMetadata.class).with(schema);
        writer = mapper.writerFor(FileMetadata.class).with(schema);
    }

    /**
     * Deserializes a csv of file metadata into a map of object ids to object metadata.
     *
     * @param metadataFile the file to parse
     * @return map of object ids to object meta
     */
    public Map<String, ObjectMetadata> deserialize(Path metadataFile) {
        try {
            var map = new HashMap<String, ObjectMetadata>();

            reader.<FileMetadata>readValues(metadataFile.toFile()).forEachRemaining(value -> {
                var objectMeta = map.computeIfAbsent(value.getObjectId(), ObjectMetadata::new);
                objectMeta.addFile(value);
            });

            return map;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void serialize(Collection<FileMetadata> fileMetadata, Path destination) {
        try (var w = writer.writeValues(destination.toFile())) {
            w.writeAll(fileMetadata);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
