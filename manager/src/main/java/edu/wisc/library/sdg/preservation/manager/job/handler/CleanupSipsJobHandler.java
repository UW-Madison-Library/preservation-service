package edu.wisc.library.sdg.preservation.manager.job.handler;

import edu.wisc.library.sdg.preservation.manager.client.internal.model.CleanupSipsJob;
import edu.wisc.library.sdg.preservation.manager.db.model.Job;
import edu.wisc.library.sdg.preservation.manager.service.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CleanupSipsJobHandler extends BaseJobHandler {

    private static final Logger LOG = LoggerFactory.getLogger(CleanupSipsJobHandler.class);

    public CleanupSipsJobHandler(JobService jobService) {
        super(jobService);
    }

    @Override
    public Object createWorkerJob(Job job) {
        return new CleanupSipsJob().jobId(job.getJobId());
    }

}
