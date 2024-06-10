package edu.wisc.library.sdg.preservation.worker.lock;

import edu.wisc.library.sdg.preservation.common.util.ArgCheck;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.DataStore;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

public abstract class AbstractLockManager implements LockManager {

    private final long acquireWaitTime;
    private final TimeUnit acquireWaitUnit;

    public AbstractLockManager(long acquireWaitTime, TimeUnit acquireWaitUnit) {
        this.acquireWaitTime = ArgCheck.condition(acquireWaitTime, acquireWaitTime >= 0, "acquireWaitTime must be >= 0");
        this.acquireWaitUnit = ArgCheck.notNull(acquireWaitUnit, "acquireWaitUnit");
    }

    @Override
    public void doInBatchLock(Long id, Runnable runnable) {
        ArgCheck.notNull(id, "id");
        doInLock(batchLockName(id), runnable);
    }

    @Override
    public void doInReplicateLock(String objectId,
                                  String persistenceVersion,
                                  DataStore destination,
                                  Runnable runnable) {
        ArgCheck.notBlank(objectId, "objectId");
        ArgCheck.notBlank(persistenceVersion, "persistenceVersion");
        ArgCheck.notNull(destination, "destination");

        var lockName = replicateLockName(objectId, persistenceVersion, destination);
        doInLock(lockName, runnable);
    }

    @Override
    public void doInLocalOcflLock(String objectId, Runnable runnable) {
        ArgCheck.notBlank(objectId, "objectId");
        doInLock(ocflObjectLockName(objectId), runnable);
    }

    /**
     * Returns an unlocked lock instance
     */
    public abstract Lock getLock(String lockName);

    private void doInLock(String lockName, Runnable runnable) {
        var lock = getLock(lockName);

        try {
            if (lock.tryLock(acquireWaitTime, acquireWaitUnit)) {
                try {
                    runnable.run();
                } finally {
                    lock.unlock();
                }
            } else {
                throw lockException(lockName);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw lockException(lockName);
        }
    }

}
