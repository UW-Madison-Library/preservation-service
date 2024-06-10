package edu.wisc.library.sdg.preservation.worker.job;

import com.google.common.annotations.VisibleForTesting;
import edu.wisc.library.sdg.preservation.common.util.ArgCheck;
import edu.wisc.library.sdg.preservation.common.util.RequestFieldValidator;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.CleanupSipsJob;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.DeleteDipJob;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.FinalizeRestoreJob;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.JobPollResponse;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ProcessBatchJob;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ReplicateJob;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RestoreJob;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RetrieveJob;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ValidateLocalJob;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ValidateRemoteJob;
import edu.wisc.library.sdg.preservation.worker.event.model.CleanupSipsEvent;
import edu.wisc.library.sdg.preservation.worker.event.model.DeleteDipEvent;
import edu.wisc.library.sdg.preservation.worker.event.model.EventBusManager;
import edu.wisc.library.sdg.preservation.worker.event.model.FinalizeRestoreEvent;
import edu.wisc.library.sdg.preservation.worker.event.model.ProcessBatchEvent;
import edu.wisc.library.sdg.preservation.worker.event.model.ReplicateObjectVersionEvent;
import edu.wisc.library.sdg.preservation.worker.event.model.RestoreObjectVersionEvent;
import edu.wisc.library.sdg.preservation.worker.event.model.RetrieveObjectsEvent;
import edu.wisc.library.sdg.preservation.worker.event.model.ValidateObjectLocalEvent;
import edu.wisc.library.sdg.preservation.worker.event.model.ValidateObjectRemoteEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

import java.util.concurrent.TimeUnit;

/**
 * Polls the manager for jobs to do. Jobs are converted to events and posted to the internal event bus to be processed
 * asynchronously.
 */
@Component
public class JobPoller {

    private static final Logger LOG = LoggerFactory.getLogger(JobPoller.class);

    private final ManagerInternalApi managerClient;
    private final EventBusManager eventBusManager;
    private final Poller poller;
    private final boolean pollingEnabled;
    private final int maxJobs;

    private Thread pollingThread;

    @Autowired
    public JobPoller(ManagerInternalApi managerClient,
                     EventBusManager eventBusManager,
                     @Value("${app.job.polling.enabled}") boolean pollingEnabled,
                     @Value("${app.job.max}") int maxJobs) {
        this.managerClient = managerClient;
        this.eventBusManager = eventBusManager;
        this.pollingEnabled = pollingEnabled;
        this.maxJobs = ArgCheck.condition(maxJobs, maxJobs > 0, "maxJobs must be greater than 0");
        this.poller = new Poller();
    }

    /**
     * Starts the polling. This is called by Spring via JobPollerLifecycle
     */
    public void start() {
        if (pollingEnabled) {
            pollingThread = new Thread(poller, "JobPoller");
            pollingThread.start();
        }
    }

    /**
     * Stops the polling. This is called by Spring via JobPollerLifecycle
     *
     * @param callback the callback to execute after the polling has stopped
     */
    public void stop(Runnable callback) {
        if (pollingEnabled) {
            LOG.debug("Stopping job polling...");

            poller.stop();
            pollingThread.interrupt();

            try {
                pollingThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LOG.warn("Interrupted while waiting for job poller to stop", e);
            }
        }

        callback.run();
    }

    private class Poller implements Runnable {
        private boolean running = true;

        @Override
        public void run() {
            while (running) {
                try {
                    eventBusManager.waitForMaxEvents(maxJobs);
                    if (running) {
                        LOG.debug("Polling for job...");
                        try {
                            var response = managerClient.pollForJob();
                            if (running) {
                                processJob(response);
                            }
                        } catch (ResourceAccessException e) {
                            // This can happen if the manager is done. Sleep briefly to give it a chance to recover
                            LOG.info("Failed to connect to manager: {}", e.getMessage());
                            TimeUnit.SECONDS.sleep(1);
                        } catch (RuntimeException e) {
                            LOG.error("Failed to poll for job", e);
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    LOG.debug("Interrupted while waiting for event completion");
                }
            }

            LOG.info("Stopped job polling");
        }

        private void stop() {
            running = false;
        }
    }

    @VisibleForTesting
    public void processJob(JobPollResponse response) {
        if (response.getJobType() == null) {
            LOG.debug("No jobs found");
        } else {
            LOG.debug("Received {} job", response.getJobType());
            eventBusManager.post(mapToEvent(response));
        }
    }

    private Object mapToEvent(JobPollResponse response) {
        switch (response.getJobType()) {
            case RETRIEVE_OBJECTS -> {
                return mapRetrieve(response.getRetrieveJob());
            }
            case REPLICATE -> {
                return mapReplicate(response.getReplicateJob());
            }
            case RESTORE -> {
                return mapRestore(response.getRestoreJob());
            }
            case FINALIZE_RESTORE -> {
                return mapFinalizeRestore(response.getFinalizeRestoreJob());
            }
            case VALIDATE_LOCAL -> {
                return mapValidateLocal(response.getValidateLocalJob());
            }
            case VALIDATE_REMOTE -> {
                return mapValidateRemote(response.getValidateRemoteJob());
            }
            case PROCESS_BATCH -> {
                return mapProcessBatch(response.getProcessBatchJob());
            }
            case DELETE_DIP -> {
                return mapDeleteDip(response.getDeleteDipJob());
            }
            case CLEANUP_SIPS -> {
                return mapCleanupSips(response.getCleanupSipsJob());
            }
            default -> {
                throw new IllegalStateException("Unknown job type: " + response.getJobType());
            }
        }
    }

    private RetrieveObjectsEvent mapRetrieve(RetrieveJob job) {
        RequestFieldValidator.notNull(job, "retrieveJob");

        var objects = job.getObjects();

        RequestFieldValidator.notNull(job.getJobId(), "jobId");
        RequestFieldValidator.notNull(objects, "objects");

        objects.forEach(object -> {
            RequestFieldValidator.notBlank(object.getExternalId(), "externalId");
            RequestFieldValidator.notBlank(object.getInternalId(), "internalId");
            RequestFieldValidator.notBlank(object.getPersistenceVersion(), "persistenceVersion");
            RequestFieldValidator.notNull(object.getVersion(), "version");
        });

        return new RetrieveObjectsEvent(job.getJobId(), objects);
    }

    private ReplicateObjectVersionEvent mapReplicate(ReplicateJob job) {
        RequestFieldValidator.notNull(job, "replicateJob");
        RequestFieldValidator.notNull(job.getJobId(), "jobId");
        RequestFieldValidator.notBlank(job.getInternalObjectId(), "internalObjectId");
        RequestFieldValidator.notBlank(job.getVault(), "vault");
        RequestFieldValidator.notBlank(job.getExternalObjectId(), "externalObjectId");
        RequestFieldValidator.notBlank(job.getPersistenceVersion(), "persistenceVersion");
        RequestFieldValidator.notNull(job.getSource(), "source");
        RequestFieldValidator.notNull(job.getDestination(), "destination");

        return new ReplicateObjectVersionEvent(job.getJobId(),
                job.getInternalObjectId(),
                job.getVault(),
                job.getExternalObjectId(),
                job.getPersistenceVersion(),
                job.getSource(),
                job.getDestination());
    }

    private RestoreObjectVersionEvent mapRestore(RestoreJob job) {
        RequestFieldValidator.notNull(job, "restoreJob");
        RequestFieldValidator.notNull(job.getJobId(), "jobId");
        RequestFieldValidator.notBlank(job.getInternalObjectId(), "internalObjectId");
        RequestFieldValidator.notBlank(job.getPersistenceVersion(), "persistenceVersion");
        RequestFieldValidator.notNull(job.getSource(), "source");
        RequestFieldValidator.notBlank(job.getKey(), "key");
        RequestFieldValidator.notBlank(job.getSha256Digest(), "sha256Digest");

        return new RestoreObjectVersionEvent(job.getJobId(),
                job.getInternalObjectId(),
                job.getPersistenceVersion(),
                job.getSource(),
                job.getKey(),
                job.getSha256Digest());
    }

    private FinalizeRestoreEvent mapFinalizeRestore(FinalizeRestoreJob job) {
        RequestFieldValidator.notNull(job, "finalizeRestoreJob");
        RequestFieldValidator.notNull(job.getJobId(), "jobId");
        RequestFieldValidator.notBlank(job.getInternalObjectId(), "internalObjectId");

        return new FinalizeRestoreEvent(
                job.getJobId(),
                job.getInternalObjectId());
    }

    private ValidateObjectLocalEvent mapValidateLocal(ValidateLocalJob job) {
        RequestFieldValidator.notNull(job, "validateLocalJob");
        RequestFieldValidator.notNull(job.getJobId(), "jobId");
        RequestFieldValidator.notBlank(job.getInternalObjectId(), "internalObjectId");
        RequestFieldValidator.notNull(job.getContentFixityCheck(), "contentFixityCheck");

        return new ValidateObjectLocalEvent(
                job.getJobId(),
                job.getInternalObjectId(),
                job.getContentFixityCheck()
        );
    }

    private ValidateObjectRemoteEvent mapValidateRemote(ValidateRemoteJob job) {
        RequestFieldValidator.notNull(job, "validateLocalJob");
        RequestFieldValidator.notNull(job.getJobId(), "jobId");
        RequestFieldValidator.notBlank(job.getInternalObjectId(), "internalObjectId");
        RequestFieldValidator.notBlank(job.getPersistenceVersion(), "persistenceVersion");
        RequestFieldValidator.notNull(job.getDataStore(), "dataStore");
        RequestFieldValidator.notBlank(job.getDataStoreKey(), "dataStoreKey");
        RequestFieldValidator.notBlank(job.getSha256Digest(), "sha256Digest");

        return new ValidateObjectRemoteEvent(
                job.getJobId(),
                job.getInternalObjectId(),
                job.getPersistenceVersion(),
                job.getDataStore(),
                job.getDataStoreKey(),
                job.getSha256Digest()
        );
    }

    private ProcessBatchEvent mapProcessBatch(ProcessBatchJob job) {
        RequestFieldValidator.notNull(job, "processBatchJob");
        RequestFieldValidator.notNull(job.getJobId(), "jobId");
        RequestFieldValidator.notNull(job.getIngestId(), "ingestId");

        return new ProcessBatchEvent(job.getJobId(), job.getIngestId());
    }

    private DeleteDipEvent mapDeleteDip(DeleteDipJob job) {
        RequestFieldValidator.notNull(job, "deleteDipJob");
        RequestFieldValidator.notNull(job.getJobId(), "jobId");
        RequestFieldValidator.notNull(job.getRetrieveRequestId(), "retrieveRequestId");
        RequestFieldValidator.notNull(job.getRetrieveJobIds(), "retrieveJobIds");

        return new DeleteDipEvent(job.getJobId(), job.getRetrieveRequestId(), job.getRetrieveJobIds());
    }

    private CleanupSipsEvent mapCleanupSips(CleanupSipsJob job) {
        RequestFieldValidator.notNull(job, "cleanupSipsJob");
        RequestFieldValidator.notNull(job.getJobId(), "jobId");

        return new CleanupSipsEvent(job.getJobId());
    }

}
