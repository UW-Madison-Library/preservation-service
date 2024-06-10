package edu.wisc.library.sdg.preservation.manager.job;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import edu.wisc.library.sdg.preservation.common.util.ArgCheck;
import edu.wisc.library.sdg.preservation.manager.service.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class DefaultJobBroker implements JobBroker {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultJobBroker.class);

    private final JobService jobService;

    private final BlockingQueue<Long> jobQueue;
    private final JobMonitor jobMonitor;
    // this ensures that a job is not retried immediately after it was dispatched
    private final Cache<Long, Long> jobCache;

    private final int capacity;
    private final int maxBackgroundWhenSharing;
    private final int backgroundFrequency;

    public DefaultJobBroker(JobService jobService, int capacity, double backgroundPercent) {
        this.capacity = ArgCheck.condition(capacity, capacity > 0, "capacity must be greater than 0");
        ArgCheck.condition(backgroundPercent, backgroundPercent < 1 && backgroundPercent > 0,
                "backgroundPercent must be less than 1 and greater than 0");

        maxBackgroundWhenSharing = (int) Math.round(capacity * backgroundPercent);
        ArgCheck.condition(maxBackgroundWhenSharing, maxBackgroundWhenSharing > 0, "maxBackgroundWhenSharing must be greater than 0");
        backgroundFrequency = (capacity / maxBackgroundWhenSharing) + 1;

        this.jobService = jobService;
        this.jobQueue = new ArrayBlockingQueue<>(capacity);
        this.jobMonitor = new JobMonitor();
        this.jobCache = Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofSeconds(30))
                .build();
    }

    /**
     * Starts the job monitor. This is called by Spring via the DefaultJobBrokerLifecycle
     */
    public void start() {
        new Thread(jobMonitor, "JobMonitor").start();
    }

    /**
     * Stops the job monitor. This is called by Spring via the DefaultJobBrokerLifecycle
     *
     * @param callback callback to call after the monitor has stopped
     */
    public void stop(Runnable callback) {
        jobMonitor.stop();
        callback.run();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void checkPendingJobs() {
        synchronized (jobMonitor) {
            jobMonitor.notify();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long pollJob(long timeout, TimeUnit unit) {
        try {
            var jobId = jobQueue.poll(timeout, unit);

            if (jobId != null) {
                jobCache.put(jobId, jobId);
                checkPendingJobs();
            }

            return jobId;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Poll interrupted", e);
        }
    }

    private class JobMonitor implements Runnable {

        private boolean running = true;

        @Override
        public void run() {
            LOG.info("Starting job monitor");

            while (running) {
                synchronized (this) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        LOG.info("Job monitor interrupted. Stopping...");
                        break;
                    }
                }

                if (!running) {
                    break;
                }

                // This does mean that there will be a short pause in processing when the queue has been drained
                if (jobQueue.isEmpty()) {
                    LOG.debug("Looking for pending jobs");
                    var cacheMap = jobCache.asMap();

                    var backgroundJobs = jobService.getPendingBackgroundJobIds();
                    var jobs = jobService.getPendingRegularJobIds();

                    LOG.debug("Pending jobs: regular={}; background={}", jobs.size(), backgroundJobs.size());

                    // The following is an attempt at "fairly" scheduling jobs. Regular jobs have precedence over
                    // background jobs. Background jobs are assigned a percentage of the total capacity that they can
                    // use when there is more work to do than capacity. This ensures that the system is not dominated
                    // by a job type. However, if there is not enough of a job type to fill its quota, then the other
                    // type is allowed to use the extra capacity. Finally, background jobs are regularly interleaved
                    // with the regular jobs. For example, if capacity is 100 and background jobs are allotted 10%,
                    // then a background job will be scheduled every 10 jobs.

                    var pickBackground = Math.min(backgroundJobs.size(), maxBackgroundWhenSharing);
                    var pickRegular = Math.min(jobs.size(), capacity - pickBackground);

                    var totalPicked = pickBackground + pickRegular;

                    LOG.debug("Picked: total={}; regular={}; background={}", totalPicked, pickRegular, pickBackground);

                    var jobsIt = jobs.iterator();
                    var backgroundJobsIt = backgroundJobs.iterator();

                    var i = 0;

                    while (i < totalPicked) {
                        Long id;

                        if (!backgroundJobsIt.hasNext()) {
                            id = jobsIt.next();
                        } else if (!jobsIt.hasNext()) {
                            id = backgroundJobsIt.next();
                        } else if ((i + 1) % backgroundFrequency == 0) {
                            id = backgroundJobsIt.next();
                        } else {
                            id = jobsIt.next();
                        }

                        if (!cacheMap.containsKey(id)) {
                            jobQueue.add(id);
                            i++;
                        } else {
                            LOG.trace("Skipping job {} because it is in the job cache", id);
                        }

                        if (!jobsIt.hasNext() && !backgroundJobsIt.hasNext()) {
                            // Nothing left to select
                            break;
                        }
                    }
                }
            }

            LOG.info("Stopping job monitor");
        }

        public void stop() {
            running = false;
            synchronized (this) {
                notify();
            }
        }
    }

}
