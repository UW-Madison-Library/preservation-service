package edu.wisc.library.sdg.preservation.worker.event.listener;

import edu.wisc.library.sdg.preservation.common.exception.LockException;
import edu.wisc.library.sdg.preservation.common.exception.ValidationException;
import edu.wisc.library.sdg.preservation.common.metrics.Outcome;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.DeferJobRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.JobState;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.UpdateJobStateRequest;
import edu.wisc.library.sdg.preservation.worker.event.model.JobEvent;
import edu.wisc.library.sdg.preservation.worker.log.JobLogger;
import edu.wisc.library.sdg.preservation.worker.log.RemoteJobLogger;
import edu.wisc.library.sdg.preservation.worker.util.ManagerRetrier;
import io.micrometer.core.instrument.LongTaskTimer;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;
import java.util.concurrent.ThreadLocalRandom;

/**
 * JobEventListener that is able to defer events if they are not ready to be processed
 */
public abstract class JobEventListener<T extends JobEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(JobEventListener.class);

    private final ManagerInternalApi internalClient;
    private final String eventName;
    private final MeterRegistry registry;
    private final LongTaskTimer longTimer;

    public JobEventListener(ManagerInternalApi internalClient, String eventName, MeterRegistry registry) {
        this.internalClient = internalClient;
        this.eventName = eventName;
        this.registry = registry;

        this.longTimer = LongTaskTimer.builder("processEventLong")
                .tag("event", eventName)
                .register(registry);
    }

    public void receive(T event) {
        LOG.debug("Event: <{}>", event);

        var jobLogger = new RemoteJobLogger(internalClient, event.getJobId());

        var outcome = Outcome.SUCCESS;
        var timer = Timer.start();
        var longSample = longTimer.start();

        try {
            try {
                updateState(event.getJobId(), JobState.EXECUTING);
            } catch (RuntimeException e) {
                LOG.info("Failed to transition job <{}> to EXECUTING", event.getJobId(), e);
                return;
            }

            // TODO Currently, the logger is only used to log uncaught exceptions while processing a job. However,
            //      it could be passed to the processors for more detailed logging if that proves to be useful.
            var defer = processEvent(event, jobLogger);

            if (!defer) {
                var nextAttempt = OffsetDateTime.now().plusHours(4).plusMinutes(jitter());
                LOG.info("Deferring job <{}> till <{}>", event.getJobId(), nextAttempt);
                try {
                    internalClient.defer(new DeferJobRequest()
                            .jobId(event.getJobId())
                            .nextAttemptTimestamp(nextAttempt));
                } catch (RuntimeException e) {
                    LOG.error("Failed to defer job <{}>", event.getJobId(), e);
                }
            } else {
                updateState(event.getJobId(), JobState.COMPLETE);
            }
        } catch (LockException e) {
            LOG.info("Job <{}> failed to acquire a lock needed to execute", event.getJobId(), e);
            updateState(event.getJobId(), JobState.PENDING);
        } catch (ValidationException e) {
            outcome = Outcome.FAILURE;
            jobLogger.warn(e, "Job <%s> failed.", event.getJobId().toString());
            failJob(event.getJobId());
        } catch (RuntimeException e) {
            outcome = Outcome.FAILURE;
            jobLogger.error(e, "Job <%s> failed.", event.getJobId().toString());
            failJob(event.getJobId());
        } finally {
            jobLogger.report();
            timer.stop(Timer.builder("processEvent")
                    .tag("event", eventName)
                    .tag(Outcome.NAME, outcome.toString())
                    .publishPercentileHistogram()
                    .register(registry));
            longSample.stop();
        }
    }

    /**
     * Process the event. Return true if the event has been processed or false to defer the event for a later time
     *
     * @param event the event to process
     * @param jobLogger job logger that writes to both the application logger and the remote job log
     * @return true if processed; false to defer
     */
    protected abstract boolean processEvent(T event, JobLogger jobLogger);

    private void failJob(Long jobId) {
        try {
            updateState(jobId, JobState.FAILED);
        } catch (RuntimeException e) {
            LOG.error("Failed to update job state to FAILED", e);
        }
    }

    private void updateState(Long jobId, JobState state) {
        ManagerRetrier.retry(() -> {
            internalClient.updateJobState(new UpdateJobStateRequest()
                    .jobId(jobId).state(state));
        });
    }

    private long jitter() {
        return ThreadLocalRandom.current().nextLong(20);
    }

}
