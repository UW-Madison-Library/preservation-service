package edu.wisc.library.sdg.preservation.worker.itest;

import com.google.common.io.MoreFiles;
import com.google.common.io.RecursiveDeleteOption;
import edu.wisc.library.sdg.preservation.common.PreservationConstants;
import edu.wisc.library.sdg.preservation.common.util.Archiver;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.JobState;
import gov.loc.repository.bagit.domain.Bag;
import gov.loc.repository.bagit.reader.BagReader;
import gov.loc.repository.bagit.verify.BagVerifier;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.reset;

public class RetrieveITest extends ITestBase {

    @Value("${app.dissemination.dir}")
    private Path disseminationDirectory;

    private Long jobId;

    @BeforeEach
    public void setup() {
        jobId = randomSerialId();
    }

    @AfterEach
    public void after() throws IOException {
        try (var files = Files.list(disseminationDirectory)) {
            files.forEach(file -> {
                try {
                    if (Files.isDirectory(file)) {
                        MoreFiles.deleteRecursively(file, RecursiveDeleteOption.ALLOW_INSECURE);
                    } else {
                        Files.deleteIfExists(file);
                    }
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
        }
    }

    @Test
    public void retrieveSingleObjectHeadVersion() {
        var bag1 = "single-valid.zip";
        var obj1 = testObject1(ingestId, "47c70a8a70b3b94b74e5f1670f3f4e0d");

        ingestObjects(bag1, obj1);

        reset(managerApi);
        ingestId = randomSerialId();

        var bag2 = "single-update.zip";
        var obj2 = testObject1V2(ingestId, "47c70a8a70b3b94b74e5f1670f3f4e0d");
        obj2.internalObjectId = obj1.internalObjectId;
        obj2.headPersistenceVersion = "v1";

        ingestObjects(bag2, obj2);

        mockPremisFile(obj2);

        retrieveObjects(jobId, obj2);

        verifySetJobState(jobId, JobState.EXECUTING);
        verifySetJobState(jobId, JobState.COMPLETE);

        verifyBag(jobId, obj2);
    }

    @Test
    public void retrieveSingleObjectAllVersions() {
        var bag1 = "single-valid.zip";
        var obj1 = testObject1(ingestId, "47c70a8a70b3b94b74e5f1670f3f4e0d");

        ingestObjects(bag1, obj1);

        reset(managerApi);
        ingestId = randomSerialId();

        var bag2 = "single-update.zip";
        var obj2 = new TestObject(ingestId, "47c70a8a70b3b94b74e5f1670f3f4e0d", 2, List.of(
                new TestFile("DC", "c895a23cfa06e7c79ae235434f0a7ac989c1cfe8448ae15d426752780a5ddde6", 417L),
                new TestFile("RELS-EXT", "9d603228bc05e508744dbe525da9bfd50693d58123c51e3089ae67d596012de8", 355L)
        ));
        obj2.internalObjectId = obj1.internalObjectId;
        obj2.headPersistenceVersion = "v1";

        ingestObjects(bag2, obj2);

        mockPremisFile(obj1, obj2);

        retrieveObjects(jobId, obj1, obj2);

        verifySetJobState(jobId, JobState.EXECUTING);
        verifySetJobState(jobId, JobState.COMPLETE);

        verifyBag(jobId, obj1, obj2);
    }

    @Test
    public void retrieveMultipleObjectsHeadVersions() {
        var bag1 = "multiple-valid.zip";

        var obj1 = testObject1(ingestId, "obj1");
        var obj2 = testObject2(ingestId, "obj2");

        ingestObjects(bag1, obj1, obj2);

        reset(managerApi);
        ingestId = randomSerialId();

        var bag2 = "multiple-update.zip";

        var obj1v2 = new TestObject(ingestId, "obj1", 2, List.of(
                new TestFile("DC", "8e0a08d4b5e2d05019859082c60b7a589ffc12f95a55b17b521bca2ad6bf9926", 407L),
                new TestFile("METHODMAP", "23beae1f8f82b83f6be9c40445fa535cd4c73f14b8433640ecda6a76911af558", 399L)
        ));
        obj1v2.internalObjectId = obj1.internalObjectId;
        obj1v2.headPersistenceVersion = "v1";
        var obj2v2 = new TestObject(ingestId, "obj2", 2, List.of(
                new TestFile("file1.txt", "649b8b471e7d7bc175eec758a7006ac693c434c8297c07db15286788c837154a", 21L),
                new TestFile("sub/file2.txt", "54e2a0fdb7dd042137a115e5944adfab10f3bafa9fab4450cb876330b2a889c5", 28L),
                new TestFile("sub/foo/bar.xml", "3b52f32512790caf962f1bcff885d45781eecfa8b23c3483b08ec826e0569af2", 35L)
        ));
        obj2v2.internalObjectId = obj2.internalObjectId;
        obj2v2.headPersistenceVersion = "v1";

        ingestObjects(bag2, obj1v2, obj2v2);

        mockPremisFile(obj1);
        mockPremisFile(obj2v2);

        retrieveObjects(jobId, obj1, obj2v2);

        verifySetJobState(jobId, JobState.EXECUTING);
        verifySetJobState(jobId, JobState.COMPLETE);

        verifyBag(jobId, obj1, obj2v2);
    }

    @Test
    public void retrieveMultipleObjectsAllVersions() {
        var bag1 = "multiple-valid.zip";

        var obj1 = testObject1(ingestId, "obj1");
        var obj2 = testObject2(ingestId, "obj2");

        ingestObjects(bag1, obj1, obj2);

        reset(managerApi);
        ingestId = randomSerialId();

        var bag2 = "multiple-update.zip";

        var obj1v2 = new TestObject(ingestId, "obj1", 2, List.of(
                new TestFile("DC", "8e0a08d4b5e2d05019859082c60b7a589ffc12f95a55b17b521bca2ad6bf9926", 407L),
                new TestFile("METHODMAP", "23beae1f8f82b83f6be9c40445fa535cd4c73f14b8433640ecda6a76911af558", 399L)
        ));
        obj1v2.internalObjectId = obj1.internalObjectId;
        obj1v2.headPersistenceVersion = "v1";
        var obj2v2 = new TestObject(ingestId, "obj2", 2, List.of(
                new TestFile("file1.txt", "649b8b471e7d7bc175eec758a7006ac693c434c8297c07db15286788c837154a", 21L),
                new TestFile("sub/file2.txt", "54e2a0fdb7dd042137a115e5944adfab10f3bafa9fab4450cb876330b2a889c5", 28L),
                new TestFile("sub/foo/bar.xml", "3b52f32512790caf962f1bcff885d45781eecfa8b23c3483b08ec826e0569af2", 35L)
        ));
        obj2v2.internalObjectId = obj2.internalObjectId;
        obj2v2.headPersistenceVersion = "v1";

        ingestObjects(bag2, obj1v2, obj2v2);

        mockPremisFile(obj1);
        mockPremisFile(obj2, obj2v2);

        retrieveObjects(jobId, obj1, obj2, obj2v2);

        verifySetJobState(jobId, JobState.EXECUTING);
        verifySetJobState(jobId, JobState.COMPLETE);

        verifyBag(jobId, obj1, obj2, obj2v2);
    }

    @Test
    public void shouldHandleEncodedIds() {
        var bag = "url-encoded.zip";

        var obj1 = testObject1(ingestId, "info:fedora/ag/obj1");

        ingestObjects(bag, obj1);

        mockPremisFile(obj1);

        retrieveObjects(jobId, obj1);

        verifySetJobState(jobId, JobState.EXECUTING);
        verifySetJobState(jobId, JobState.COMPLETE);

        verifyBag(jobId, obj1);
    }

    @Test
    public void markJobFailedOnException() {
        var obj1 = testObject1(ingestId, "obj1");
        obj1.internalObjectId = "bogus";
        
        retrieveObjects(jobId, obj1);

        verifySetJobState(jobId, JobState.EXECUTING);
        verifySetJobState(jobId, JobState.FAILED);
    }

    private Path bagPath(Long jobId) {
        return disseminationDirectory.resolve(String.format(PreservationConstants.DIP_BAG_ZIP_TMPL, jobId));
    }

    private Path explodeBag(Long jobId, Path bag) {
        var dst = tempDir.resolve(String.format(PreservationConstants.DIP_BAG_NAME_TMPL, jobId));
        Archiver.extract(bag, dst);
        return dst.resolve(String.format(PreservationConstants.DIP_BAG_NAME_TMPL, jobId));
    }

    private void verifyBag(Long jobId, TestObject... objects) {
        var bagPath = bagPath(jobId);
        var exploded = explodeBag(jobId, bagPath);

        try {
            BagReader reader = new BagReader();
            Bag bag = reader.read(exploded);

            BagVerifier verifier = new BagVerifier();
            verifier.isValid(bag, false);

            assertEquals(DEFAULT_VAULT, bag.getMetadata().get(PreservationConstants.DIP_BAGIT_VAULT_TAG_NAME).get(0));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        var dataDir = exploded.resolve("data");

        var map = new HashMap<String, Map<String, TestObject>>();

        for (var object : objects) {
            var objectMap = map.computeIfAbsent(object.externalId, key -> new HashMap<>());
            objectMap.put(String.valueOf(object.version), object);
        }

        try (var objectDirs = Files.list(dataDir).filter(Files::isDirectory)) {
            var seenObjects = new HashSet<String>();

            objectDirs.forEach(objectDir -> {
                var decoded = URLDecoder.decode(objectDir.getFileName().toString(), StandardCharsets.UTF_8);
                var objectMap = map.get(decoded);
                assertNotNull(objectMap, String.format("Expected object with id %s to exist", decoded));
                seenObjects.add(decoded);

                try {
                    var premisContent = new String(Files.readAllBytes(objectDir.resolve(PreservationConstants.OBJECT_PREMIS_FILE)));
                    assertEquals(decoded, premisContent);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }

                var seenVersions = new HashSet<String>();

                try (var versionDirs = Files.list(objectDir)) {
                    versionDirs.filter(Files::isDirectory).forEach(versionDir -> {
                        var version = versionDir.getFileName().toString();

                        var object = objectMap.get(version);
                        assertNotNull(object, String.format("Expected object with id %s and version %s to exist", decoded, version));
                        seenVersions.add(version);

                        var fileMap = object.files.stream()
                                .collect(Collectors.toMap(file -> file.path, Function.identity()));

                        var bagFiles = new HashSet<String>();

                        try (var walk = Files.walk(versionDir)) {
                            walk.filter(Files::isRegularFile).forEach(file -> {
                                var relative = versionDir.relativize(file);
                                bagFiles.add(relative.toString());
                            });
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }

                        assertEquals(fileMap.keySet(), bagFiles);

                        fileMap.forEach((pathStr, expected) -> {
                            try {
                                var path = versionDir.resolve(pathStr);
                                var digest = DigestUtils.sha256Hex(Files.newInputStream(path));
                                assertEquals(expected.digest, digest,
                                        String.format("Digest for %s did not match expectation", path));
                            } catch (IOException e) {
                                throw new UncheckedIOException(e);
                            }
                        });
                    });
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }

                assertEquals(objectMap.keySet(), seenVersions);
            });

            assertEquals(map.keySet(), seenObjects);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
