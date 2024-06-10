package edu.wisc.library.sdg.preservation.worker.lock;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * In memory lock manager -- does not work when there are multiple workers.
 */
public class DefaultLockManager extends AbstractLockManager {

    private final Cache<String, Lock> locks;

    public DefaultLockManager(long acquireWaitTime, TimeUnit acquireWaitUnit) {
        super(acquireWaitTime, acquireWaitUnit);
        locks = Caffeine.newBuilder().weakValues().build();
    }

    @Override
    public Lock getLock(String lockName) {
        return locks.get(lockName, k -> new ReentrantLock());
    }
}
