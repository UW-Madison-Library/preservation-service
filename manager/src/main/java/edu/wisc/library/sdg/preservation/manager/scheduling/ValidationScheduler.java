package edu.wisc.library.sdg.preservation.manager.scheduling;

import edu.wisc.library.sdg.preservation.manager.service.JobService;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Profile({"!itest"})
public class ValidationScheduler {

    private static final Logger LOG = LoggerFactory.getLogger(ValidationScheduler.class);

    private final JobService jobService;

    public ValidationScheduler(JobService jobService) {
        this.jobService = jobService;
    }

    @Scheduled(cron = "${app.schedule.validation.local.check.cron}")
    @Timed(value = "runTask")
    public void scheduleLocalObjectValidations() {
        LOG.info("Looking for local objects to validate");

        jobService.scheduleLocalObjectValidations();
    }

    @Scheduled(cron = "${app.schedule.validation.remote.check.cron}")
    @Timed(value = "runTask")
    public void scheduleRemoteVersionValidations() {
        LOG.info("Looking for remote versions to validate");

        jobService.scheduleRemoteObjectValidations();
    }

}
