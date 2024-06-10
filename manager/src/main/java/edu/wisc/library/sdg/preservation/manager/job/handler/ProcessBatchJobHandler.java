package edu.wisc.library.sdg.preservation.manager.job.handler;

import edu.wisc.library.sdg.preservation.manager.client.internal.model.ProcessBatchJob;
import edu.wisc.library.sdg.preservation.manager.db.model.Job;
import edu.wisc.library.sdg.preservation.manager.service.JobService;

public class ProcessBatchJobHandler extends BaseJobHandler {

    public ProcessBatchJobHandler(JobService jobService) {
        super(jobService);
    }

    @Override
    public Object createWorkerJob(Job job) {
        var details = jobService.getProcessBatchJobDetails(job.getJobId());

        return new ProcessBatchJob()
                .jobId(job.getJobId())
                .ingestId(details.getIngestId());
    }

}
