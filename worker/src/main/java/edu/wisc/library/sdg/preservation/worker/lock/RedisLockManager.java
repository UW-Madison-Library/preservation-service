package edu.wisc.library.sdg.preservation.worker.lock;

import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * Distributed lock manager backed by redis
 */
public class RedisLockManager extends AbstractLockManager {

    private final RedissonClient client;

    public RedisLockManager(RedissonClient client, long acquireWaitTime, TimeUnit acquireWaitUnit) {
        super(acquireWaitTime, acquireWaitUnit);
        this.client = client;
    }

    @Override
    public Lock getLock(String lockName) {
        return client.getLock(lockName);
    }
}
