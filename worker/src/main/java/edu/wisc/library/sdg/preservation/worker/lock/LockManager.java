package edu.wisc.library.sdg.preservation.worker.lock;

import edu.wisc.library.sdg.preservation.common.exception.LockException;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.DataStore;

public interface LockManager {

    String BATCH_LOCK_PREFIX = "BATCH:";
    String REPLICATE_LOCK_PREFIX = "REPLICATE:";
    String LOCAL_OCFL_PREFIX = "LOCAL_OCFL:";

    /**
     * Executes the runnable after acquiring a lock on processing an ingest batch.
     *
     * @param id the id of the ingest batch
     * @param runnable the code to execute in the lock
     */
    void doInBatchLock(Long id, Runnable runnable);

    /**
     * Executes the runnable after acquiring a lock on replicating to a datastore.
     *
     * @param objectId the internal object id, OCFL object id
     * @param persistenceVersion the persistence version, OCFL version
     * @param destination the destination datastore
     */
    void doInReplicateLock(String objectId, String persistenceVersion, DataStore destination, Runnable runnable);

    /**
     * Executes the runnable after acquiring a lock on a local OCFL object
     *
     * @param objectId the internal object id, OCFL object id
     */
    void doInLocalOcflLock(String objectId, Runnable runnable);

    default String batchLockName(Long batchId) {
        return LockManager.BATCH_LOCK_PREFIX + batchId.toString();
    }

    default String ocflObjectLockName(String objectId) {
        return LockManager.LOCAL_OCFL_PREFIX + objectId;
    }

    default String replicateLockName(String objectId, String persistenceVersion, DataStore destination) {
        return String.format("%s%s:%s:%s", LockManager.REPLICATE_LOCK_PREFIX, objectId, persistenceVersion, destination);
    }

    default LockException lockException(String name) {
        return new LockException(String.format("Failed to acquire lock on <%s>.", name));
    }

}
