package edu.wisc.library.sdg.preservation.worker.itest;

import edu.wisc.library.sdg.preservation.common.PreservationConstants;
import edu.wisc.library.sdg.preservation.common.util.Archiver;
import edu.wisc.library.sdg.preservation.common.util.UncheckedFiles;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.DataStore;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.JobState;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.StorageProblemType;
import gov.loc.repository.bagit.creator.BagCreator;
import gov.loc.repository.bagit.hash.StandardSupportedAlgorithms;
import io.findify.s3mock.S3Mock;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.io.FileMatchers.anExistingFile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.reset;

public class ReplicateITest extends ITestBase {

    private Long jobId;

    private static S3Mock s3Mock;

    @BeforeAll
    public static void beforeAll() {
        s3Mock = S3Mock.create(7878);
        s3Mock.start();
    }

    @AfterAll
    public static void afterAll() {
        s3Mock.shutdown();
    }

    @BeforeEach
    public void setup() {
        jobId = randomSerialId();
        s3Client.createBucket(request -> {
            request.bucket(S3_BUCKET);
        });
    }

    @AfterEach
    public void after() {
        s3Client.deleteBucket(request -> {
            request.bucket(S3_BUCKET);
        });
    }

    @Test
    public void replicateVersionV1ToS3() throws IOException {
        var bag1 = "single-valid.zip";
        var obj1 = testObject1(ingestId, "47c70a8a70b3b94b74e5f1670f3f4e0d");

        ingestObjects(bag1, obj1);

        mockNewInVersion(obj1, "v1");
        replicateObject(jobId, obj1);

        verifySetJobState(jobId, JobState.EXECUTING);
        verifySetJobState(jobId, JobState.COMPLETE);

        var key = glacierKey(obj1);

        verifyReplication(obj1, key);
        verifyArchive(obj1, key);
    }

    @Test
    public void replicateVersionV2ToS3() throws IOException {
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

        mockNewInVersion(obj2, "v2", "DC");
        replicateObject(jobId, obj2);

        verifySetJobState(jobId, JobState.EXECUTING);
        verifySetJobState(jobId, JobState.COMPLETE);

        var key = glacierKey(obj2);

        verifyReplication(obj2, key);
        verifyArchive(obj2, key, List.of(obj2.files.get(0)));
    }

    @Test
    public void replicateVersionWithMultipartRequestToS3() throws IOException {
        var bag1 = "large-bag.zip";
        var bagPath = tempDir.resolve(bag1);

        var digest = createBagWithLargeFile("large-file-test", "large-file", bagPath);

        var obj1 = new TestObject(ingestId, "large-file-test", 1, List.of(
                new TestFile("large-file", digest, 108_003_328L)
        ));

        ingestObjects(bagPath, obj1);

        mockNewInVersion(obj1, "v1");
        replicateObject(jobId, obj1);

        verifySetJobState(jobId, JobState.EXECUTING);
        verifySetJobState(jobId, JobState.COMPLETE);

        var key = glacierKey(obj1);

        verifyReplication(obj1, key);
        verifyArchive(obj1, key);
    }

    @Test
    public void failWhenReplicatingObjectVersionThatDoesNotExist() throws IOException {
        var bag1 = "single-valid.zip";
        var obj1 = testObject1(ingestId, "47c70a8a70b3b94b74e5f1670f3f4e0d");

        ingestObjects(bag1, obj1);

        obj1.version = 2;

        replicateObject(jobId, obj1);

        verifySetJobState(jobId, JobState.EXECUTING);
        verifySetJobState(jobId, JobState.FAILED);
    }

    @Test
    public void failWhenValidationFails() throws IOException {
        var bag1 = "single-valid.zip";
        var obj1 = testObject1(ingestId, "47c70a8a70b3b94b74e5f1670f3f4e0d");

        ingestObjects(bag1, obj1);

        mockNewInVersion(obj1, "v1", "BOGUS");
        replicateObject(jobId, obj1);

        verifySetJobState(jobId, JobState.EXECUTING);
        verifySetJobState(jobId, JobState.FAILED);
    }

    @Test
    public void failWhenSourceHasVersionLevelProblems() throws IOException {
        var bag1 = "single-valid.zip";
        var obj1 = testObject1(ingestId, "47c70a8a70b3b94b74e5f1670f3f4e0d");

        ingestObjects(bag1, obj1);

        mockNewInVersion(obj1, "v1");
        mockStorageProblems(obj1, DataStore.IBM_COS, StorageProblemType.NONE, Map.of(
                "v1", StorageProblemType.CORRUPT
        ));
        replicateObjectNoProblemMock(jobId, obj1);

        verifySetJobState(jobId, JobState.EXECUTING);
        verifySetJobState(jobId, JobState.FAILED);
    }

    @Test
    public void replicateWhenSourceHasVersionLevelProblemsOnDifferentVersion() throws IOException {
        var bag1 = "single-valid.zip";
        var obj1 = testObject1(ingestId, "47c70a8a70b3b94b74e5f1670f3f4e0d");

        ingestObjects(bag1, obj1);

        mockNewInVersion(obj1, "v1");
        mockStorageProblems(obj1, DataStore.IBM_COS, StorageProblemType.NONE, Map.of(
                "v2", StorageProblemType.CORRUPT
        ));
        replicateObjectNoProblemMock(jobId, obj1);

        verifySetJobState(jobId, JobState.EXECUTING);
        verifySetJobState(jobId, JobState.COMPLETE);
    }

    @Test
    public void failWhenSourceHasObjectLevelProblems() throws IOException {
        var bag1 = "single-valid.zip";
        var obj1 = testObject1(ingestId, "47c70a8a70b3b94b74e5f1670f3f4e0d");

        ingestObjects(bag1, obj1);

        mockNewInVersion(obj1, "v1");
        mockStorageProblems(obj1, DataStore.IBM_COS, StorageProblemType.MISSING, Map.of());
        replicateObjectNoProblemMock(jobId, obj1);

        verifySetJobState(jobId, JobState.EXECUTING);
        verifySetJobState(jobId, JobState.FAILED);
    }

    private void verifyArchive(TestObject object, String key) throws IOException {
        verifyArchive(object, key, object.files);
    }

    private void verifyArchive(TestObject object, String key, List<TestFile> files) throws IOException {
        var archive = tempDir.resolve(key);
        var unzipped = archive.getParent();
        UncheckedFiles.createDirectories(unzipped);

        s3Client.getObject(request -> {
            request.bucket(S3_BUCKET).key(key);
        }, archive);

        Archiver.extract(archive, unzipped);

        var root = unzipped.resolve(object.unprefixedInternalObjectId());

        var descFile = root.resolve(PreservationConstants.ARCHIVE_OCFL_CONTENT_FILENAME);
        assertThat(descFile.toFile(), anExistingFile());

        assertEquals(descFileContents(object), Files.readString(descFile));

        var ocflContent = root.resolve("v" + object.version).resolve("content");
        var systemContent = ocflContent.resolve(PreservationConstants.OCFL_OBJECT_SYSTEM_DIR);
        var objectContent = ocflContent.resolve(PreservationConstants.OCFL_OBJECT_OBJECT_DIR);

        assertThat(systemContent.resolve(PreservationConstants.SYSTEM_METADATA_FILENAME).toFile(), anExistingFile());

        for (var file : files) {
            var path = objectContent.resolve(file.path);
            assertEquals(file.digest, DigestUtils.sha256Hex(Files.newInputStream(path)));
        }

        try (var walk = Files.walk(objectContent)) {
            var fileCount = walk.filter(Files::isRegularFile).count();
            assertEquals(files.size(), fileCount, "Found more files in archive than expected");
        }
    }

    private String descFileContents(TestObject object) {
        return String.format("# This directory contains a single version of an OCFL object.\n" +
                "object-id: %s\n" +
                "vault: %s\n" +
                "external-object-id: %s\n" +
                "version: %s\n" +
                "ocfl-version: 1.1", object.internalObjectId, DEFAULT_VAULT, object.externalId, "v" + object.version);
    }

    Random RANDOM = new SecureRandom();

    private String createBagWithLargeFile(String externalObjectId, String fileName, Path destination) throws IOException {
        var bagDir = tempDir.resolve(externalObjectId + "-" + Instant.now().toEpochMilli()).resolve("bag");
        var file = bagDir.resolve(externalObjectId).resolve(fileName);
        UncheckedFiles.createDirectories(file.getParent());

        var buffer = new byte[1024]; // 1KB

        try (var out = new BufferedOutputStream(new FileOutputStream(file.toFile()))) {
            // 103MB
            for (int i = 0; i < 105_472; i++) {
                RANDOM.nextBytes(buffer);
                out.write(buffer);
            }
        }

        var digest = DigestUtils.sha256Hex(Files.newInputStream(file));

        createBag(bagDir);

        Archiver.archive(bagDir, destination, true);

        return digest;
    }

    private void createBag(Path bagDir) {
        try {
            BagCreator.bagInPlace(bagDir, Arrays.asList(StandardSupportedAlgorithms.SHA256), false);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
