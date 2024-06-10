package edu.wisc.library.sdg.preservation.worker.itest;

import edu.wisc.library.sdg.preservation.common.PreservationConstants;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.EventOutcome;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.EventType;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.JobState;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.LogEntry;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.LogLevel;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.StorageProblemType;
import io.ocfl.api.model.DigestAlgorithm;
import io.ocfl.core.util.DigestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.reset;

public class ValidateLocalITest extends ITestBase {

    private Long jobId;

    @BeforeEach
    public void setup() {
        jobId = randomSerialId();
    }

    @Test
    public void validateObjectWhenValid() {
        var bag1 = "single-valid.zip";
        var obj1 = testObject1(ingestId, "47c70a8a70b3b94b74e5f1670f3f4e0d");

        ingestObjects(bag1, obj1);

        reset(managerApi);

        mockGetVersionStates(obj1);

        validateObject(jobId, obj1);

        verifySetJobState(jobId, JobState.EXECUTING);
        verifySetJobState(jobId, JobState.COMPLETE);

        verifySetStorageProblemsLocal(obj1, StorageProblemType.NONE, null);
        verifyPreservationEvent(EventType.VALIDATE_OBJ_LOCAL, EventOutcome.SUCCESS, obj1.internalObjectId, null);
    }

    @Test
    public void recordValidateFailureWhenOcflValidationFails() throws IOException {
        var bag1 = "single-valid.zip";
        var obj1 = testObject1(ingestId, "47c70a8a70b3b94b74e5f1670f3f4e0d");

        ingestObjects(bag1, obj1);

        var storagePath = ocflRepository.describeObject(obj1.internalObjectId)
                .getHeadVersion().getFile(String.format("%s/%s", PreservationConstants.OCFL_OBJECT_OBJECT_DIR, "DC"))
                .getStorageRelativePath();

        var fullPath = Paths.get(ocflRepoRoot, storagePath);

        Files.delete(fullPath);

        reset(managerApi);

        mockGetVersionStates(obj1);

        validateObject(jobId, obj1);

        verifySetJobState(jobId, JobState.EXECUTING);
        verifySetJobState(jobId, JobState.COMPLETE);

        var logEntry = log(String.format("[E092] Inventory manifest in %s/inventory.json contains a content" +
                " path that does not exist: v1/content/object/DC", objectRootPath(obj1)));

        verifySetStorageProblemsLocal(obj1, StorageProblemType.CORRUPT, null);
        verifyPreservationEvent(EventType.VALIDATE_OBJ_LOCAL, EventOutcome.FAILURE, obj1.internalObjectId, List.of(logEntry));
    }

    @Test
    public void recordValidateFailureWhenMetaComparisonFails() throws IOException {
        var bag1 = "single-valid.zip";
        var obj1 = testObject1(ingestId, "47c70a8a70b3b94b74e5f1670f3f4e0d");

        ingestObjects(bag1, obj1);

        reset(managerApi);

        obj1.files.add(new TestFile("BOGUS", "9d603228bc05e508744dbe525da9bfd50693d58123c51e3089ae67d596012de8", 1L));

        mockGetVersionStates(obj1);

        validateObject(jobId, obj1);

        verifySetJobState(jobId, JobState.EXECUTING);
        verifySetJobState(jobId, JobState.COMPLETE);

        var logEntry = log(String.format("File object/BOGUS was not found in OCFL object %s version v1",
                obj1.internalObjectId));

        verifySetStorageProblemsLocal(obj1, StorageProblemType.NONE, Map.of(
                "v1", StorageProblemType.CORRUPT
        ));

        verifyPreservationEvent(EventType.VALIDATE_OBJ_LOCAL, EventOutcome.FAILURE, obj1.internalObjectId, List.of(logEntry));
    }

    private LogEntry log(String message) {
        return new LogEntry().level(LogLevel.ERROR).message(message);
    }

    private String objectRootPath(TestObject object) {
        var digest = DigestUtil.computeDigestHex(DigestAlgorithm.sha256, object.internalObjectId);
        return String.format("%s/%s/%s/%s", digest.substring(0, 3), digest.substring(3, 6), digest.substring(6, 9), digest);
    }

}
