package edu.wisc.library.sdg.preservation.worker.util;

import dev.failsafe.Failsafe;
import dev.failsafe.RetryPolicy;
import dev.failsafe.function.CheckedRunnable;
import dev.failsafe.function.CheckedSupplier;
import edu.wisc.library.sdg.preservation.common.exception.HttpClientException;
import edu.wisc.library.sdg.preservation.common.exception.InternalErrorException;

import java.time.Duration;

/**
 * Util class for retrying server errors when making preservation manager requests
 */
public final class ManagerRetrier {

    // This will retry for about 25 minutes. The intent is to be able to tolerate a brief manager outage without
    // losing state. This may need to be adjusted.
    private static final RetryPolicy<Object> retryPolicy = RetryPolicy.builder()
            .handle(InternalErrorException.class, HttpClientException.class)
            .withBackoff(Duration.ofMillis(100), Duration.ofMinutes(1))
            .withMaxRetries(30)
            .build();

    private ManagerRetrier() {

    }

    /**
     * Retry the preservation manager request until successful or exhausted retries
     */
    public static void retry(CheckedRunnable action) {
        Failsafe.with(retryPolicy).run(action);
    }

    /**
     * Retry the preservation manager request until successful or exhausted retries
     */
    public static <T> T retry(CheckedSupplier<T> action) {
        return Failsafe.with(retryPolicy).get(action);
    }

}
