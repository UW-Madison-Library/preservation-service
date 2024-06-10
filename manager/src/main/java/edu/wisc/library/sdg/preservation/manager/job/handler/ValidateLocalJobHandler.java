package edu.wisc.library.sdg.preservation.manager.job.handler;

import edu.wisc.library.sdg.preservation.common.util.UuidUtils;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ValidateLocalJob;
import edu.wisc.library.sdg.preservation.manager.db.model.Job;
import edu.wisc.library.sdg.preservation.manager.service.JobService;
import edu.wisc.library.sdg.preservation.manager.service.PreservationService;

public class ValidateLocalJobHandler extends BaseJobHandler {

    private final PreservationService preservationService;

    public ValidateLocalJobHandler(JobService jobService,
                                   PreservationService preservationService) {
        super(jobService);
        this.preservationService = preservationService;
    }

    @Override
    public Object createWorkerJob(Job job) {
        var details = jobService.getValidateLocalJobDetails(job.getJobId());

        return new ValidateLocalJob()
                .jobId(job.getJobId())
                .internalObjectId(UuidUtils.withPrefix(details.getObjectId()))
                .contentFixityCheck(details.getContentFixityCheck());
    }

    @Override
    public void completeJob(Job job) {
        super.completeJob(job);

        var details = jobService.getValidateLocalJobDetails(job.getJobId());
        if (details.getContentFixityCheck()) {
            preservationService.markObjectDeepValidation(details.getObjectId());
        } else {
            preservationService.markObjectShallowValidation(details.getObjectId());
        }
    }
}
