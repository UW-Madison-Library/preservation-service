package edu.wisc.library.sdg.preservation.manager.job;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import edu.wisc.library.sdg.preservation.manager.service.JobService;

import java.time.Duration;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * This class is only intended to be used for testing purposes so that the jobs behave in a more deterministic fashion
 */
public class SimpleJobBroker implements JobBroker {

    private static final int CAPACITY = 10000;

    private final JobService jobService;

    private final BlockingQueue<Long> jobQueue;
    // this ensures that a job is not retried immediately after it was dispatched
    private final Cache<Long, Long> jobCache;

    public SimpleJobBroker(JobService jobService) {
        this.jobService = jobService;
        this.jobQueue = new ArrayBlockingQueue<>(CAPACITY);
        this.jobCache = Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofSeconds(30))
                .build();
    }

    @Override
    public void checkPendingJobs() {
        var cacheMap = jobCache.asMap();
        jobQueue.clear();
        jobService.getPendingRegularJobIds().stream()
                .filter(id -> !cacheMap.containsKey(id))
                .forEach(jobQueue::add);
        jobService.getPendingBackgroundJobIds().stream()
                .filter(id -> !cacheMap.containsKey(id))
                .forEach(jobQueue::add);
    }

    @Override
    public Long pollJob(long timeout, TimeUnit unit) {
        var jobId = jobQueue.poll();

        if (jobId != null) {
            jobCache.put(jobId, jobId);
        }

        return jobId;
    }

    public void invalidateCache() {
        jobCache.invalidateAll();
    }

}
