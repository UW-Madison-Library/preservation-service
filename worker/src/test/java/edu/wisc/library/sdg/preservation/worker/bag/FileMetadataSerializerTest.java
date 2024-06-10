package edu.wisc.library.sdg.preservation.worker.bag;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

public class FileMetadataSerializerTest {

    @TempDir
    public Path temp;

    private FileMetadataSerializer serializer;

    @BeforeEach
    public void setup() {
        serializer = new FileMetadataSerializer();
    }

    @Test
    public void roundTrip() {
        var file = temp.resolve("file-meta.csv");

        var obj1Meta1 = new FileMetadata("obj1", "file1.txt",
                List.of("mime1", "mime2"),
                List.of("pronom1", "pronom2"),
                List.of("encoding1", "encoding2"));

        var obj2Meta1 = new FileMetadata("obj2", "file1.txt",
                List.of("mime1"),
                List.of("pronom1"),
                List.of("encoding1"));
        var obj2Meta2 = new FileMetadata("obj2", "file2.txt",
                List.of("mime2"),
                List.of("pronom2"),
                List.of("encoding2"));

        var metadata = new ArrayList<FileMetadata>();
        metadata.add(obj2Meta1);
        metadata.add(obj1Meta1);
        metadata.add(obj2Meta2);

        serializer.serialize(metadata, file);
        var objectMeta = serializer.deserialize(file);

        assertThat(objectMeta.keySet(), containsInAnyOrder("obj1", "obj2"));
        assertThat(objectMeta.get("obj1").getAllFileMeta(), containsInAnyOrder(obj1Meta1));
        assertThat(objectMeta.get("obj2").getAllFileMeta(), containsInAnyOrder(obj2Meta1, obj2Meta2));
    }

    @Test
    public void readFileWithHeader() throws IOException {
        var file = temp.resolve("file-meta.csv");

        Files.writeString(file,
                "objectId,filePath,mimeTypes,pronomIds,encodings\n" +
                     "obj1,file1.txt,mime1|mime2,pronom1|pronom2,encoding1|encoding2\n" +
                     "obj2,file1.txt,mime1,pronom1,encoding1\n" +
                     "obj2,file2.txt,,,encoding2");

        var obj1Meta1 = new FileMetadata("obj1", "file1.txt",
                List.of("mime1", "mime2"),
                List.of("pronom1", "pronom2"),
                List.of("encoding1", "encoding2"));

        var obj2Meta1 = new FileMetadata("obj2", "file1.txt",
                List.of("mime1"),
                List.of("pronom1"),
                List.of("encoding1"));
        var obj2Meta2 = new FileMetadata("obj2", "file2.txt",
                null,
                null,
                List.of("encoding2"));

        var objectMeta = serializer.deserialize(file);

        assertThat(objectMeta.keySet(), containsInAnyOrder("obj1", "obj2"));
        assertThat(objectMeta.get("obj1").getAllFileMeta(), containsInAnyOrder(obj1Meta1));
        assertThat(objectMeta.get("obj2").getAllFileMeta(), containsInAnyOrder(obj2Meta1, obj2Meta2));
    }

}
