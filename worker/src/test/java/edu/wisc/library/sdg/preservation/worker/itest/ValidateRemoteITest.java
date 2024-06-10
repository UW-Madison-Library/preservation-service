package edu.wisc.library.sdg.preservation.worker.itest;

import edu.wisc.library.sdg.preservation.common.exception.ValidationException;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.EventOutcome;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.EventType;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.JobState;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.LogEntry;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.LogLevel;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.StorageProblemType;
import io.findify.s3mock.S3Mock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;

public class ValidateRemoteITest extends ITestBase {

    private static S3Mock s3Mock;

    private Long jobId;

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
    public void validateObjectWhenValid() {
        var bag1 = "single-valid.zip";
        var obj1 = testObject1(ingestId, "47c70a8a70b3b94b74e5f1670f3f4e0d");

        ingestObjects(bag1, obj1);

        mockNewInVersion(obj1, "v1");
        replicateObject(jobId, obj1);

        jobId = randomSerialId();

        mockNewInVersion(obj1, "v1");
        validateRemote(jobId, obj1);

        verifySetJobState(jobId, JobState.EXECUTING);
        verifySetJobState(jobId, JobState.COMPLETE);

        verifySetStorageProblemsGlacier(obj1, StorageProblemType.NONE, null);
        verifyPreservationEvent(EventType.VALIDATE_OBJ_VERSION_REMOTE, EventOutcome.SUCCESS, obj1.internalObjectId,
                List.of(validateLog("v1")));
    }

    @Test
    public void reportMissingWhenDoesNotExist() {
        var bag1 = "single-valid.zip";
        var obj1 = testObject1(ingestId, "47c70a8a70b3b94b74e5f1670f3f4e0d");

        ingestObjects(bag1, obj1);

        mockNewInVersion(obj1, "v1");
        validateRemote(jobId, obj1);

        verifySetJobState(jobId, JobState.EXECUTING);
        verifySetJobState(jobId, JobState.COMPLETE);

        verifySetStorageProblemsGlacier(obj1, StorageProblemType.MISSING, null);
        verifyPreservationEvent(EventType.VALIDATE_OBJ_VERSION_REMOTE, EventOutcome.FAILURE, obj1.internalObjectId,
                List.of(validateLog("v1"), error("OCFL version v1 was not found in GLACIER")));
    }

    @Test
    public void reportCorruptWhenValidationFails() {
        var bag1 = "single-valid.zip";
        var obj1 = testObject1(ingestId, "47c70a8a70b3b94b74e5f1670f3f4e0d");

        ingestObjects(bag1, obj1);

        mockNewInVersion(obj1, "v1");
        replicateObject(jobId, obj1);

        jobId = randomSerialId();

        mockNewInVersion(obj1, "v1");
        doThrow(new ValidationException("Missing file")).when(glacierVersionValidator)
                .validate(eq(obj1.internalObjectId), eq("v" + obj1.version),
                        eq(glacierKey(obj1)), eq(glacierDigest(obj1)), anyList());

        validateRemote(jobId, obj1);

        verifySetJobState(jobId, JobState.EXECUTING);
        verifySetJobState(jobId, JobState.COMPLETE);

        verifySetStorageProblemsGlacier(obj1, StorageProblemType.CORRUPT, null);
        verifyPreservationEvent(EventType.VALIDATE_OBJ_VERSION_REMOTE, EventOutcome.FAILURE, obj1.internalObjectId,
                List.of(validateLog("v1"), error("Missing file")));
    }

    @Test
    public void reportFailureWhenValidationThrowsException() {
        var bag1 = "single-valid.zip";
        var obj1 = testObject1(ingestId, "47c70a8a70b3b94b74e5f1670f3f4e0d");

        ingestObjects(bag1, obj1);

        mockNewInVersion(obj1, "v1");
        replicateObject(jobId, obj1);

        jobId = randomSerialId();

        mockNewInVersion(obj1, "v1");
        doThrow(new RuntimeException("Broke!")).when(glacierVersionValidator)
                .validate(eq(obj1.internalObjectId), eq("v" + obj1.version),
                        eq(glacierKey(obj1)), eq(glacierDigest(obj1)), anyList());

        validateRemote(jobId, obj1);

        verifySetJobState(jobId, JobState.EXECUTING);
        verifySetJobState(jobId, JobState.FAILED);
    }

    private LogEntry validateLog(String version) {
        return info(String.format("Validate OCFL version %s in GLACIER", version));
    }

    private LogEntry error(String message) {
        return new LogEntry().level(LogLevel.ERROR).message(message);
    }

    private LogEntry info(String message) {
        return new LogEntry().level(LogLevel.INFO).message(message);
    }

}
