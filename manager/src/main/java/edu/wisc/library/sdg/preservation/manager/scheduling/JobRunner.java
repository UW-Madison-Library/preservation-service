package edu.wisc.library.sdg.preservation.manager.scheduling;

import edu.wisc.library.sdg.preservation.manager.job.JobBroker;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * This class periodically looks for jobs that need to be run and executes them.
 */
@Component
@Profile({"!itest"})
public class JobRunner {

    private static final Logger LOG = LoggerFactory.getLogger(JobRunner.class);

    private final JobBroker jobBroker;
    private final BatchReplicationMonitor batchReplicationMonitor;

    public JobRunner(JobBroker jobBroker,
                     BatchReplicationMonitor batchReplicationMonitor) {
        this.jobBroker = jobBroker;
        this.batchReplicationMonitor = batchReplicationMonitor;
    }

    @Scheduled(fixedDelayString = "${app.schedule.job.check.sec}", timeUnit = TimeUnit.SECONDS)
    @Timed(value = "runTask")
    public void runJobs() {
        try {
            jobBroker.checkPendingJobs();
        } catch (RuntimeException e) {
            LOG.error("Failed to find pending jobs", e);
            throw e;
        }
    }

    @Scheduled(fixedDelayString = "${app.schedule.job.check.sec}", timeUnit = TimeUnit.SECONDS)
    @Timed(value = "runTask")
    public void checkReplicationStatuses() {
        try {
            batchReplicationMonitor.checkReplicationStatuses();
        } catch (RuntimeException e) {
            LOG.error("Failed to check replication statuses", e);
            throw e;
        }
    }
}
