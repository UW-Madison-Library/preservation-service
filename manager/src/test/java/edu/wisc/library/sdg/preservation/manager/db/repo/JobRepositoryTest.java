package edu.wisc.library.sdg.preservation.manager.db.repo;

import edu.wisc.library.sdg.preservation.common.util.Time;
import edu.wisc.library.sdg.preservation.manager.db.model.Job;
import edu.wisc.library.sdg.preservation.manager.db.model.JobState;
import edu.wisc.library.sdg.preservation.manager.db.model.JobType;
import edu.wisc.library.sdg.preservation.manager.itest.DbHelper;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "logging.level.edu.wisc.library.sdg.preservation=WARN"
        })
@ActiveProfiles({"default", "itest"})
public class JobRepositoryTest {

    private static final String TEST_ORG = "test-organization";

    @Autowired
    private Flyway flyway;
    private DbHelper dbHelper;

    @Autowired
    private JobRepository jobRepository;

    @BeforeEach
    public void setup() {
        dbHelper = new DbHelper(flyway);
        dbHelper.baseline();
    }

    @Test
    public void doNotSelectDeferredJobsThatAreNotReady() {
        var notReady = createJob(JobType.RESTORE, JobState.PENDING, job -> {
            job.setNextAttemptTimestamp(Time.now().plusMinutes(2));
        });

        var ready = createJob(JobType.RESTORE, JobState.PENDING, job -> {
            job.setNextAttemptTimestamp(Time.now().minusMinutes(2));
        });

        var jobs = List.of(
                createJob(JobType.REPLICATE, JobState.PENDING),
                createJob(JobType.RETRIEVE_OBJECTS, JobState.COMPLETE),
                createJob(JobType.RETRIEVE_OBJECTS, JobState.FAILED),
                createJob(JobType.RETRIEVE_OBJECTS, JobState.PENDING),
                ready,
                notReady
        );

        jobRepository.saveAll(jobs);

        var pending = jobRepository.findAllJobIdsPendingExecution(Time.now(), false);

        assertEquals(3, pending.size());

        var containsReady = false;

        for (var job : pending) {
            assertNotEquals(notReady.getJobId(), job);
            if (job.equals(ready.getJobId())) {
                containsReady = true;
            }
        }

        assertTrue(containsReady);
    }

    private Job createJob(JobType type, JobState state) {
        return createJob(type, state, null);
    }

    private Job createJob(JobType type, JobState state, Consumer<Job> customizer) {
        var job = new Job()
                .setType(type)
                .setState(state)
                .setOrgName(TEST_ORG)
                .setReceivedTimestamp(Time.now())
                .setUpdatedTimestamp(Time.now())
                .setBackground(false);

        if (customizer != null) {
            customizer.accept(job);
        }

        return job;
    }

}
