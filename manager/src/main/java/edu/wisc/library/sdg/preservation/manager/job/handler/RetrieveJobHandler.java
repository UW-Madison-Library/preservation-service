package edu.wisc.library.sdg.preservation.manager.job.handler;

import edu.wisc.library.sdg.preservation.common.util.UuidUtils;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ObjectInfo;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RetrieveJob;
import edu.wisc.library.sdg.preservation.manager.db.model.Job;
import edu.wisc.library.sdg.preservation.manager.db.model.RetrieveJobDetails;
import edu.wisc.library.sdg.preservation.manager.service.JobService;
import edu.wisc.library.sdg.preservation.manager.service.PreservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;

public class RetrieveJobHandler extends BaseJobHandler {

    private static final Logger LOG = LoggerFactory.getLogger(RetrieveJobHandler.class);

    private final PreservationService preservationService;

    public RetrieveJobHandler(JobService jobService,
                             PreservationService preservationService) {
        super(jobService);
        this.preservationService = preservationService;
    }

    @Override
    public Object createWorkerJob(Job job) {
        var details = jobService.getRetrieveJobDetails(job.getJobId());

        return new RetrieveJob()
                .jobId(job.getJobId())
                .objects(details.stream().map(detail -> {
                    var objectVersion = preservationService.getObjectVersion(detail.getObjectVersionId());
                    var object = preservationService.getObject(objectVersion.getObjectId());
                    return new ObjectInfo()
                            .externalId(object.getExternalObjectId())
                            .internalId(UuidUtils.withPrefix(objectVersion.getObjectId()))
                            .version(objectVersion.getVersion())
                            .persistenceVersion(objectVersion.getPersistenceVersion());
                }).collect(Collectors.toList()));
    }

    @Override
    public void completeJob(Job job) {
        super.completeJob(job);

        try {
            var details = jobService.getRetrieveJobDetails(job.getJobId());
            var objectVersionIds = details.stream().map(RetrieveJobDetails::getObjectVersionId).toList();
            preservationService.recordPrepareDipEvents(job.getJobId(), objectVersionIds);
        } catch (RuntimeException e) {
            LOG.error("Failed to record DIP events for job {}", job.getJobId(), e);
        }
    }
}
