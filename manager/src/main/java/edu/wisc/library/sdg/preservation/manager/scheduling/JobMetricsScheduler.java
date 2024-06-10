package edu.wisc.library.sdg.preservation.manager.scheduling;

import edu.wisc.library.sdg.preservation.manager.db.model.JobCount;
import edu.wisc.library.sdg.preservation.manager.db.model.JobState;
import edu.wisc.library.sdg.preservation.manager.db.model.JobType;
import edu.wisc.library.sdg.preservation.manager.db.repo.JobRepository;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class JobMetricsScheduler {

    private static final Logger LOG = LoggerFactory.getLogger(JobMetricsScheduler.class);
    // how long a job can run before it is considered long-running
    private Duration LONG_RUN_PERIOD = Duration.ofHours(4);

    private final JobRepository jobRepository;

    private final Map<JobType, AtomicInteger> pendingCounts;
    private final Map<JobType, AtomicInteger> executingCounts;
    private final Map<JobType, AtomicInteger> longRunningCounts;

    @Autowired
    public JobMetricsScheduler(JobRepository jobRepository, MeterRegistry meterRegistry) {
        this.jobRepository = jobRepository;
        this.pendingCounts = new HashMap<>(JobType.values().length);
        this.executingCounts = new HashMap<>(JobType.values().length);
        this.longRunningCounts = new HashMap<>(JobType.values().length);

        for (var type : JobType.values()) {
            pendingCounts.put(type, meterRegistry.gauge("pendingJobCount",
                    List.of(Tag.of("jobType", type.toString())),
                    new AtomicInteger(0)));
            executingCounts.put(type, meterRegistry.gauge("executingJobCount",
                    List.of(Tag.of("jobType", type.toString())),
                    new AtomicInteger(0)));
            longRunningCounts.put(type, meterRegistry.gauge("longRunningJobCount",
                    List.of(Tag.of("jobType", type.toString())),
                    new AtomicInteger(0)));
        }
    }

    @Scheduled(fixedDelayString = "${app.schedule.job.metrics.sec}", timeUnit = TimeUnit.SECONDS)
    @Timed(value = "runTask")
    public void calculateJobMetrics() {
        LOG.debug("calculating job metrics");

        var pendingJobCounts = jobRepository.countJobsInStateByType(JobState.PENDING);
        var executingJobCounts = jobRepository.countJobsInStateByType(JobState.EXECUTING);
        var longRunningJobCounts = jobRepository.countJobsInStateAndUpdatedTimestampBeforeByType(
                JobState.EXECUTING,
                OffsetDateTime.now().minus(LONG_RUN_PERIOD)
        );

        updateCounts(pendingJobCounts, pendingCounts);
        updateCounts(executingJobCounts, executingCounts);
        updateCounts(longRunningJobCounts, longRunningCounts);

        LOG.debug("pendingCounts: {}", pendingCounts);
        LOG.debug("executingCounts: {}", executingCounts);
        LOG.debug("longRunningCounts: {}", longRunningCounts);
    }

    private void updateCounts(List<JobCount> jobCounts, Map<JobType, AtomicInteger> metricCounts) {
        metricCounts.forEach((type, count) -> {
            jobCounts.stream().filter(jobCount -> jobCount.getJobType() == type)
                    .findFirst()
                    .ifPresentOrElse(updatedCount -> count.set((int) updatedCount.getCount()),
                            () -> count.set(0));
        });
    }
}
