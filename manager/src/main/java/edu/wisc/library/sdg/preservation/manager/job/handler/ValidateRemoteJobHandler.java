package edu.wisc.library.sdg.preservation.manager.job.handler;

import edu.wisc.library.sdg.preservation.common.util.UuidUtils;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.DataStore;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ValidateRemoteJob;
import edu.wisc.library.sdg.preservation.manager.db.model.Job;
import edu.wisc.library.sdg.preservation.manager.service.JobService;
import edu.wisc.library.sdg.preservation.manager.service.PreservationService;

public class ValidateRemoteJobHandler extends BaseJobHandler {

    private final PreservationService preservationService;

    public ValidateRemoteJobHandler(JobService jobService,
                                    PreservationService preservationService) {
        super(jobService);
        this.preservationService = preservationService;
    }

    @Override
    public Object createWorkerJob(Job job) {
        var details = jobService.getValidateRemoteJobDetails(job.getJobId());
        var location = preservationService.getStorageLocation(details.getObjectVersionLocationId());

        return new ValidateRemoteJob()
                .jobId(job.getJobId())
                .internalObjectId(UuidUtils.withPrefix(location.getObjectId()))
                .persistenceVersion(location.getPersistenceVersion())
                .dataStore(DataStore.fromValue(location.getDataStore().toString()))
                .dataStoreKey(location.getDataStoreKey())
                .sha256Digest(location.getSha256Digest());
    }

    @Override
    public void completeJob(Job job) {
        super.completeJob(job);

        var details = jobService.getValidateRemoteJobDetails(job.getJobId());
        preservationService.markRemoteVersionValidation(details.getObjectVersionLocationId());
    }
}
