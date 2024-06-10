package edu.wisc.library.sdg.preservation.manager.itest;

import edu.wisc.library.sdg.preservation.manager.client.internal.model.EventOutcome;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.EventType;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.JobState;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.JobType;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.LogLevel;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RetrieveJob;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

public class RetrieveBagITest extends ITestBase {

    private static final long KB = 1024;
    private static final long MB = 1024 * KB;

    @Test
    public void retrieveBagOfSingleObjectSingleVersion() {
        setupBaseline();

        var jobId = retrieveObjects(false, defaultObject1).get(0);

        assertJobState(jobId, JobState.PENDING.getValue());

        updateJobState(jobId, JobState.EXECUTING);
        updateJobState(jobId, JobState.COMPLETE);

        assertJobState(jobId, JobState.COMPLETE.getValue());

        List<RetrieveJob> jobs = getJobsOfType(JobType.RETRIEVE_OBJECTS);
        assertEquals(1, jobs.size());

        assertRetrieveObjectsRequest(jobs.get(0), jobId, defaultObject1v2);
    }

    @Test
    public void retrieveBagOfSingleObjectAllVersions() {
        setupBaseline();

        var jobId = retrieveObjects(true, defaultObject1).get(0);

        assertJobState(jobId, JobState.PENDING.getValue());

        updateJobState(jobId, JobState.EXECUTING);
        updateJobState(jobId, JobState.COMPLETE);

        assertJobState(jobId, JobState.COMPLETE.getValue());

        List<RetrieveJob> jobs = getJobsOfType(JobType.RETRIEVE_OBJECTS);
        assertEquals(1, jobs.size());

        assertRetrieveObjectsRequest(jobs.get(0), jobId, defaultObject1, defaultObject1v2);
    }

    @Test
    public void retrieveBagOfAllObjectsSingleVersion() {
        setupBaseline();

        var jobId = retrieveObjects(false).get(0);

        assertJobState(jobId, JobState.PENDING.getValue());

        updateJobState(jobId, JobState.EXECUTING);
        updateJobState(jobId, JobState.COMPLETE);

        assertJobState(jobId, JobState.COMPLETE.getValue());

        List<RetrieveJob> jobs = getJobsOfType(JobType.RETRIEVE_OBJECTS);
        assertEquals(1, jobs.size());

        assertRetrieveObjectsRequest(jobs.get(0), jobId, defaultObject1v2, defaultObject2);
    }

    @Test
    public void retrieveBagOfAllObjectsAllVersions() {
        setupBaseline();

        var jobId = retrieveObjects(true).get(0);

        assertJobState(jobId, JobState.PENDING.getValue());

        updateJobState(jobId, JobState.EXECUTING);
        updateJobState(jobId, JobState.COMPLETE);

        assertJobState(jobId, JobState.COMPLETE.getValue());

        List<RetrieveJob> jobs = getJobsOfType(JobType.RETRIEVE_OBJECTS);
        assertEquals(1, jobs.size());

        assertRetrieveObjectsRequest(jobs.get(0), jobId, defaultObject1, defaultObject1v2, defaultObject2);

        assertPreservationEventsSubset(defaultObject1, List.of(
                event(EventType.PREPARE_DIP, EventOutcome.SUCCESS, null,
                        List.of(logEntry(LogLevel.INFO, String.format("Created DIP dip-%s.zip containing versions [1, 2]", jobId))))
        ));

        assertPreservationEventsSubset(defaultObject2, List.of(
                event(EventType.PREPARE_DIP, EventOutcome.SUCCESS, null,
                        List.of(logEntry(LogLevel.INFO, String.format("Created DIP dip-%s.zip containing versions [1]", jobId))))
        ));
    }

    @Test
    public void retrieveBagOfAllObjectsAllVersionsWhenObjectsWillNotFitInSameBag() {
        defaultObject1 = new TestObject("o1")
                .addFile("MASTER0", 900 * MB)
                .addFile("BIB0", 50L);
        defaultObject1v2 = new TestObject("o1")
                .version(2)
                .addFile("MASTER0", 901 * MB)
                .addFile("BIB0", 50L);
        defaultObject1v2.internalId = defaultObject1.internalId;
        defaultObject2 = new TestObject("o2")
                .addFile("ENCTEXT", 300 * MB)
                .addFile("TECH0", 20L);

        setupBaseline();

        var jobs = retrieveObjects(true);

        assertEquals(2, jobs.size());

        jobs.forEach(jobId -> {
            assertJobState(jobId, JobState.PENDING.getValue());

            updateJobState(jobId, JobState.EXECUTING);
            updateJobState(jobId, JobState.COMPLETE);

            assertJobState(jobId, JobState.COMPLETE.getValue());
        });

        List<RetrieveJob> actualJobs = getJobsOfType(JobType.RETRIEVE_OBJECTS);
        assertEquals(2, actualJobs.size());

        var request1 = identifyRequest(defaultObject1, actualJobs);
        var request2 = identifyRequest(defaultObject2, actualJobs);

        assertRetrieveObjectsRequest(request1, request1.getJobId(), defaultObject1, defaultObject1v2);
        assertRetrieveObjectsRequest(request2, request2.getJobId(), defaultObject2);
    }

    @Test
    public void retrieveBagOfAllObjectsAllVersionsWhenVersionsWillNotFitInSameBag() {
        defaultObject1 = new TestObject("o1")
                .addFile("MASTER0", 900 * MB)
                .addFile("BIB0", 50L);
        defaultObject1v2 = new TestObject("o1")
                .version(2)
                .addFile("MASTER0", 1500 * MB)
                .addFile("BIB0", 50L);
        defaultObject1v2.internalId = defaultObject1.internalId;
        defaultObject2 = new TestObject("o2")
                .addFile("ENCTEXT", 300 * MB)
                .addFile("TECH0", 20L);

        setupBaseline();

        var jobs = retrieveObjects(true);

        assertEquals(2, jobs.size());

        jobs.forEach(jobId -> {
            assertJobState(jobId, JobState.PENDING.getValue());

            updateJobState(jobId, JobState.EXECUTING);
            updateJobState(jobId, JobState.COMPLETE);

            assertJobState(jobId, JobState.COMPLETE.getValue());
        });

        List<RetrieveJob> actualJobs = getJobsOfType(JobType.RETRIEVE_OBJECTS);
        assertEquals(2, actualJobs.size());

        var request1 = identifyRequest(defaultObject1, actualJobs);
        var request2 = identifyRequest(defaultObject1v2, actualJobs);

        assertNotSame(request1, request2);

        // just check that it's in a request, which it's in is non-deterministic
        identifyRequest(defaultObject2, actualJobs);
    }

    @Test
    public void retrieveBagOfAllObjectsAllVersionsWhenBagFullBeforeAllObjectsProcessed() {
        defaultObject1 = new TestObject("o1")
                .addFile("MASTER0", 2000 * MB)
                .addFile("BIB0", 50L);
        defaultObject1v2 = new TestObject("o1")
                .version(2)
                .addFile("MASTER0", 200 * MB)
                .addFile("BIB0", 50L);
        defaultObject1v2.internalId = defaultObject1.internalId;
        defaultObject2 = new TestObject("o2")
                .addFile("ENCTEXT", 2050 * MB)
                .addFile("TECH0", 20L);

        setupBaseline();

        var jobs = retrieveObjects(true);

        assertEquals(3, jobs.size());

        jobs.forEach(jobId -> {
            assertJobState(jobId, JobState.PENDING.getValue());

            updateJobState(jobId, JobState.EXECUTING);
            updateJobState(jobId, JobState.COMPLETE);

            assertJobState(jobId, JobState.COMPLETE.getValue());
        });

        List<RetrieveJob> actualJobs = getJobsOfType(JobType.RETRIEVE_OBJECTS);
        assertEquals(3, actualJobs.size());

        var request1 = identifyRequest(defaultObject1, actualJobs);
        var request2 = identifyRequest(defaultObject1v2, actualJobs);
        var request3 = identifyRequest(defaultObject2, actualJobs);

        assertRetrieveObjectsRequest(request1, request1.getJobId(), defaultObject1);
        assertRetrieveObjectsRequest(request2, request2.getJobId(), defaultObject1v2);
        assertRetrieveObjectsRequest(request3, request3.getJobId(), defaultObject2);

        assertPreservationEventsSubset(defaultObject1, List.of(
                event(EventType.PREPARE_DIP, EventOutcome.SUCCESS, null,
                        List.of(logEntry(LogLevel.INFO, String.format("Created DIP dip-%s.zip containing versions [1]", jobs.get(0))))),
                event(EventType.PREPARE_DIP, EventOutcome.SUCCESS, null,
                        List.of(logEntry(LogLevel.INFO, String.format("Created DIP dip-%s.zip containing versions [2]", jobs.get(2)))))
        ));

        assertPreservationEventsSubset(defaultObject2, List.of(
                event(EventType.PREPARE_DIP, EventOutcome.SUCCESS, null,
                        List.of(logEntry(LogLevel.INFO, String.format("Created DIP dip-%s.zip containing versions [1]", jobs.get(1)))))
        ));
    }

    private RetrieveJob identifyRequest(TestObject object, List<RetrieveJob> requests) {
        for (var request : requests) {
            for (var requestObj : request.getObjects()) {
                if (requestObj.getInternalId().equals(object.prefixedInternalId())
                        && requestObj.getVersion().equals(object.version)) {
                    return request;
                }
            }
        }
        throw new RuntimeException("Object not found: " + object);
    }

}
