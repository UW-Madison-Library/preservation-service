package edu.wisc.library.sdg.preservation.manager.job.handler;

import edu.wisc.library.sdg.preservation.manager.client.internal.model.DeleteDipJob;
import edu.wisc.library.sdg.preservation.manager.db.model.Job;
import edu.wisc.library.sdg.preservation.manager.db.model.RetrieveRequestJobComposite;
import edu.wisc.library.sdg.preservation.manager.service.JobService;
import edu.wisc.library.sdg.preservation.manager.service.PreservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeleteDipJobHandler extends BaseJobHandler {

    private static final Logger LOG = LoggerFactory.getLogger(DeleteDipJobHandler.class);

    private final PreservationService preservationService;

    public DeleteDipJobHandler(JobService jobService,
                               PreservationService preservationService) {
        super(jobService);
        this.preservationService = preservationService;
    }

    @Override
    public Object createWorkerJob(Job job) {
        var details = jobService.getDeleteDipJobDetails(job.getJobId());
        var jobs = preservationService.getRetrieveRequestJobs(details.getRetrieveRequestId());
        var ids = jobs.stream().map(RetrieveRequestJobComposite::getJobId).toList();

        return new DeleteDipJob()
                .jobId(job.getJobId())
                .retrieveRequestId(details.getRetrieveRequestId())
                .retrieveJobIds(ids);
    }

    @Override
    public void completeJob(Job job) {
        super.completeJob(job);
        var details = jobService.getDeleteDipJobDetails(job.getJobId());
        preservationService.markRetrieveRequestDeleted(details.getRetrieveRequestId());
    }
}
