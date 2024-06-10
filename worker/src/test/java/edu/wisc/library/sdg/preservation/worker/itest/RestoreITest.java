package edu.wisc.library.sdg.preservation.worker.itest;

import edu.wisc.library.sdg.preservation.manager.client.internal.model.DataStore;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.JobState;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.StorageProblemType;
import io.findify.s3mock.S3Mock;
import io.ocfl.api.exception.NotFoundException;
import io.ocfl.api.exception.ValidationException;
import io.ocfl.api.model.ObjectVersionId;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.reset;

public class RestoreITest extends ITestBase {

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
    public void restoreVersionFromGlacierWhenExistsLocally() throws IOException {
        var bag1 = "single-valid.zip";
        var obj1 = testObject1(ingestId, "47c70a8a70b3b94b74e5f1670f3f4e0d");

        ingestObjects(bag1, obj1);
        mockNewInVersion(obj1, "v1");
        replicateObject(randomSerialId(), obj1);

        var digest = verifyReplication(obj1, glacierKey(obj1));

        var filePath = ocflRepository.describeVersion(
                ObjectVersionId.head(obj1.internalObjectId))
                .getFile("object/RELS-EXT").getStorageRelativePath();

        Files.writeString(Paths.get(ocflRepoRoot, filePath), "corrupt!");
        Files.writeString(Paths.get(ocflRepoRoot, filePath).getParent().resolve("bogus"), "corrupt!");

        restoreObject(jobId, obj1, digest);

        verifySetJobState(jobId, JobState.EXECUTING);
        verifySetJobState(jobId, JobState.COMPLETE);

        jobId = randomSerialId();

        finalizeRestore(jobId, obj1);
        verifySetJobState(jobId, JobState.EXECUTING);
        verifySetJobState(jobId, JobState.COMPLETE);

        validateOcfl(obj1);
    }

    @Test
    public void failRestoreWhenNotInRemote() {
        var bag1 = "single-valid.zip";
        var obj1 = testObject1(ingestId, "47c70a8a70b3b94b74e5f1670f3f4e0d");

        ingestObjects(bag1, obj1);

        restoreObject(jobId, obj1, "digest");

        verifySetJobState(jobId, JobState.EXECUTING);
        verifySetJobState(jobId, JobState.FAILED);
    }

    @Test
    public void restoreVersionFromGlacierWhenObjectDoesNotExistLocally() {
        var bag1 = "single-valid.zip";
        var obj1 = testObject1(ingestId, "47c70a8a70b3b94b74e5f1670f3f4e0d");

        ingestObjects(bag1, obj1);
        mockNewInVersion(obj1, "v1");
        replicateObject(randomSerialId(), obj1);

        var digest = verifyReplication(obj1, glacierKey(obj1));

        ocflRepository.purgeObject(obj1.internalObjectId);

        restoreObject(jobId, obj1, digest);

        verifySetJobState(jobId, JobState.EXECUTING);
        verifySetJobState(jobId, JobState.COMPLETE);

        jobId = randomSerialId();

        finalizeRestore(jobId, obj1);
        verifySetJobState(jobId, JobState.EXECUTING);
        verifySetJobState(jobId, JobState.COMPLETE);

        validateOcfl(obj1);
    }

    @Test
    public void failRestoreWhenRemoteHasVersionStorageProblems() {
        var bag1 = "single-valid.zip";
        var obj1 = testObject1(ingestId, "47c70a8a70b3b94b74e5f1670f3f4e0d");

        ingestObjects(bag1, obj1);
        mockNewInVersion(obj1, "v1");
        replicateObject(randomSerialId(), obj1);

        var digest = verifyReplication(obj1, glacierKey(obj1));

        mockStorageProblems(obj1, DataStore.GLACIER, StorageProblemType.NONE, Map.of(
                "v1", StorageProblemType.CORRUPT
        ));
        restoreObjectNoProblemMock(jobId, obj1, digest);

        verifySetJobState(jobId, JobState.EXECUTING);
        verifySetJobState(jobId, JobState.FAILED);
    }

    @Test
    public void restoreWhenRemoteHasVersionStorageProblemsOnDifferentVersion() {
        var bag1 = "single-valid.zip";
        var obj1 = testObject1(ingestId, "47c70a8a70b3b94b74e5f1670f3f4e0d");

        ingestObjects(bag1, obj1);
        mockNewInVersion(obj1, "v1");
        replicateObject(randomSerialId(), obj1);

        var digest = verifyReplication(obj1, glacierKey(obj1));

        mockStorageProblems(obj1, DataStore.GLACIER, StorageProblemType.NONE, Map.of(
                "v2", StorageProblemType.CORRUPT
        ));
        restoreObjectNoProblemMock(jobId, obj1, digest);

        verifySetJobState(jobId, JobState.EXECUTING);
        verifySetJobState(jobId, JobState.COMPLETE);
    }

    @Test
    public void failRestoreWhenRemoteHasObjectStorageProblems() {
        var bag1 = "single-valid.zip";
        var obj1 = testObject1(ingestId, "47c70a8a70b3b94b74e5f1670f3f4e0d");

        ingestObjects(bag1, obj1);
        mockNewInVersion(obj1, "v1");
        replicateObject(randomSerialId(), obj1);

        var digest = verifyReplication(obj1, glacierKey(obj1));

        mockStorageProblems(obj1, DataStore.GLACIER, StorageProblemType.CORRUPT, Map.of());
        restoreObjectNoProblemMock(jobId, obj1, digest);

        verifySetJobState(jobId, JobState.EXECUTING);
        verifySetJobState(jobId, JobState.FAILED);
    }

    @Test
    public void restoreVersionFromGlacierWhenManyVersionAndOneMissing() {
        var obj1 = testObject1(ingestId, "47c70a8a70b3b94b74e5f1670f3f4e0d");

        ingestObjects("single-valid.zip", obj1);
        mockNewInVersion(obj1, "v1");
        replicateObject(randomSerialId(), obj1);
        verifyReplication(obj1, glacierKey(obj1));

        reset(managerApi);
        ingestId = randomSerialId();

        var obj2 = testObject1V2(ingestId, "47c70a8a70b3b94b74e5f1670f3f4e0d");
        obj2.internalObjectId = obj1.internalObjectId;
        obj2.headPersistenceVersion = "v1";

        ingestObjects("single-update.zip", obj2);
        mockNewInVersion(obj2, "v2", "DC");
        replicateObject(randomSerialId(), obj2);
        var digestV2= verifyReplication(obj2, glacierKey(obj2));

        reset(managerApi);
        ingestId = randomSerialId();

        var obj3 = new TestObject(ingestId, "47c70a8a70b3b94b74e5f1670f3f4e0d", 3, List.of(
                new TestFile("DC", "c895a23cfa06e7c79ae235434f0a7ac989c1cfe8448ae15d426752780a5ddde6", 417L),
                new TestFile("NEW", "0f15384d18789b1ebf3043dc7b6bc27273c8576373fbeb6f3e15854b588141c0", 9L),
                new TestFile("RELS-EXT", "9d603228bc05e508744dbe525da9bfd50693d58123c51e3089ae67d596012de8", 355L)
        ));
        obj3.internalObjectId = obj1.internalObjectId;
        obj3.headPersistenceVersion = "v2";

        ingestObjects("single-update-2.zip", obj3);
        mockNewInVersion(obj3, "v3", "NEW");
        replicateObject(randomSerialId(), obj3);
        verifyReplication(obj3, glacierKey(obj3));

        ocflRepository.rollbackToVersion(
                ObjectVersionId.version(obj1.internalObjectId, 1));

        restoreObject(jobId, obj2, digestV2);

        verifySetJobState(jobId, JobState.EXECUTING);
        verifySetJobState(jobId, JobState.COMPLETE);

        jobId = randomSerialId();

        finalizeRestore(jobId, obj2);
        verifySetJobState(jobId, JobState.EXECUTING);
        verifySetJobState(jobId, JobState.COMPLETE);

        validateOcfl(obj1);
        validateOcfl(obj2);

        assertThrows(NotFoundException.class, () -> {
            ocflRepository.describeVersion(
                    ObjectVersionId.version(obj1.internalObjectId, 3));
        });
    }

    private void validateOcfl(TestObject object) {
        assertTrue(ocflRepository.containsObject(object.internalObjectId));
        for (var file : object.files) {
            var ocflFile = ocflRepository.getObject(
                    ObjectVersionId.version(object.internalObjectId, object.version))
                    .getFile("object/" + file.path);
            assertNotNull(ocflFile, "missing " + file.path);
            try (var stream = ocflFile.getStream()) {
                stream.readAllBytes();
                stream.checkFixity();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
        var results = ocflRepository.validateObject(object.internalObjectId, false);
        if (results.hasErrors()) {
            throw new ValidationException("validation failed for " + object.internalObjectId, results);
        }
    }

}
