package edu.wisc.library.sdg.preservation.manager.scheduling;

import edu.wisc.library.sdg.preservation.common.util.Time;
import edu.wisc.library.sdg.preservation.manager.service.JobService;
import edu.wisc.library.sdg.preservation.manager.service.PreservationService;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * Looks for DIPs that have expired and schedules jobs to remove them
 */
@Component
public class CleanupScheduler {

    private static final Logger LOG = LoggerFactory.getLogger(CleanupScheduler.class);

    private final Duration dipRetentionPeriod;
    private final JobService jobService;
    private final PreservationService preservationService;

    @Autowired
    public CleanupScheduler(@Value("${app.schedule.dip.retention.period}") Duration dipRetentionPeriod,
                            JobService jobService,
                            PreservationService preservationService) {
        this.dipRetentionPeriod = dipRetentionPeriod;
        this.jobService = jobService;
        this.preservationService = preservationService;
    }

    @Scheduled(cron = "${app.schedule.dip.cleanup.check.cron}")
    @Timed(value = "runTask")
    public void scheduleDipCleanup() {
        LOG.info("Looking for DIPs that have expired and need to be removed");

        var created = Time.now().minus(dipRetentionPeriod).truncatedTo(ChronoUnit.HOURS);
        var requestIds = preservationService.getAllExpiredButNotDeletedRetrieveRequests(created);

        var success = true;

        for (var id : requestIds) {
            try {
                var orgName = preservationService.getOrgNameForRetrieveRequest(id);
                LOG.info("Scheduling cleanup for retrieve request {}", id);
                jobService.createDeleteDipJob(id, orgName);
            } catch (Exception e) {
                success = false;
                LOG.error("Failed to create job to delete DIPs created by request {}", id, e);
            }
        }

        if (!success) {
            throw new RuntimeException("Encountered failures while scheduling DIP cleanup");
        }
    }

    @Scheduled(cron = "${app.schedule.sip.cleanup.check.cron}")
    @Timed(value = "runTask")
    public void scheduleSipCleanup() {
        LOG.info("Scheduling SIP cleanup");
        jobService.createCleanupSipsJob();
    }

}
