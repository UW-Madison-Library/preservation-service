package edu.wisc.library.sdg.preservation.manager.controller;

import edu.wisc.library.sdg.preservation.common.util.RequestFieldValidator;
import edu.wisc.library.sdg.preservation.manager.auth.PreservationAuth;
import edu.wisc.library.sdg.preservation.manager.client.model.CancelJobRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.DescribeJobResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.DisableJobTypesRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.EnableJobTypesRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.JobDetails;
import edu.wisc.library.sdg.preservation.manager.client.model.ListJobsResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.LogEntry;
import edu.wisc.library.sdg.preservation.manager.client.model.ReplicateObjectRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.ReplicateObjectResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.RestorePreservationObjectRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.RestorePreservationObjectResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.RetrieveLogsResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.ValidateObjectRemoteRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.ValidateObjectRemoteResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.ValidatePreservationObjectRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.ValidatePreservationObjectResponse;
import edu.wisc.library.sdg.preservation.manager.db.model.DataStore;
import edu.wisc.library.sdg.preservation.manager.db.model.JobState;
import edu.wisc.library.sdg.preservation.manager.db.model.JobType;
import edu.wisc.library.sdg.preservation.manager.job.JobBroker;
import edu.wisc.library.sdg.preservation.manager.job.JobManager;
import edu.wisc.library.sdg.preservation.manager.service.JobService;
import edu.wisc.library.sdg.preservation.manager.service.PreservationService;
import edu.wisc.library.sdg.preservation.manager.util.ModelMapper;
import io.micrometer.core.annotation.Timed;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
@Timed(histogram = true)
public class JobController {

    private static final Logger LOG = LoggerFactory.getLogger(JobController.class);

    private final PreservationAuth preservationAuth;
    private final ModelMapper modelMapper;
    private final JobService jobService;
    private final PreservationService preservationService;
    private final JobBroker jobBroker;
    private final JobManager jobManager;

    @Autowired
    public JobController(PreservationAuth preservationAuth,
                         ModelMapper modelMapper,
                         JobService jobService,
                         PreservationService preservationService,
                         JobBroker jobBroker,
                         JobManager jobManager) {
        this.preservationAuth = preservationAuth;
        this.modelMapper = modelMapper;
        this.jobService = jobService;
        this.preservationService = preservationService;
        this.jobBroker = jobBroker;
        this.jobManager = jobManager;
    }

    @GetMapping("/{orgName}/job")
    public ListJobsResponse listJobs(@PathVariable("orgName") String orgName) {
        return preservationAuth.applyIfServiceAdmin(() -> {
            var jobs = jobService.getJobs(orgName);
            return new ListJobsResponse().jobs(modelMapper.mapList(jobs, JobDetails.class));
        });
    }

    @GetMapping("/job/{jobId}")
    public DescribeJobResponse describeJob(@PathVariable("jobId") Long jobId) {
        var job = jobService.retrieveJob(jobId);

        return preservationAuth.applyIfServiceAdmin(() -> {
            return new DescribeJobResponse()
                    .job(modelMapper.map(job, JobDetails.class));
        });
    }

    @GetMapping("/job/executing")
    public ListJobsResponse listExecutingJobs() {
        return preservationAuth.applyIfServiceAdmin(() -> {
            var jobs = jobService.getExecutingJobs();
            return new ListJobsResponse().jobs(modelMapper.mapList(jobs, JobDetails.class));
        });
    }

    /**
     * Jobs may get stuck in a state of EXECUTING if a worker goes down while working on a job.
     * This allows service administrators to transition jobs to FAILED if needed.
     *
     * @param request - the jobId for the job that should be transitioned to FAILED
     */
    @PostMapping("/job/fail")
    public void failJob(@RequestBody CancelJobRequest request) {
        RequestFieldValidator.notNull(request.getJobId(), "jobId");

        preservationAuth.applyIfServiceAdmin(() -> {
            jobService.updateJobState(request.getJobId(), JobState.FAILED);
        });
    }


    @PostMapping("/job/validate")
    public ValidatePreservationObjectResponse validateObject(@RequestBody ValidatePreservationObjectRequest request) {
        RequestFieldValidator.notBlank(request.getVault(), "vault");
        RequestFieldValidator.notBlank(request.getExternalObjectId(), "externalObjectId");
        RequestFieldValidator.notNull(request.getContentFixityCheck(), "contentFixityCheck");

        var objectId = preservationService.getInternalObjectId(request.getVault(), request.getExternalObjectId());

        return preservationAuth.applyIfServiceAdmin(() -> {
            var jobId = jobService.createValidateLocalJob(objectId, request.getContentFixityCheck(), false);
            jobBroker.checkPendingJobs();
            return new ValidatePreservationObjectResponse().jobId(jobId);
        });
    }

    @PostMapping("/job/validateRemote")
    public ValidateObjectRemoteResponse validateObjectRemote(@RequestBody ValidateObjectRemoteRequest request) {
        RequestFieldValidator.notBlank(request.getVault(), "vault");
        RequestFieldValidator.notBlank(request.getExternalObjectId(), "externalObjectId");
        RequestFieldValidator.notNull(request.getDataStore(), "dataStore");

        var objectId = preservationService.getInternalObjectId(request.getVault(), request.getExternalObjectId());

        return preservationAuth.applyIfServiceAdmin(() -> {
            var jobIds = preservationService.validateObjectRemote(objectId,
                    request.getVersions(),
                    modelMapper.map(request.getDataStore(), DataStore.class));
            jobBroker.checkPendingJobs();
            return new ValidateObjectRemoteResponse().jobIds(jobIds);
        });
    }

    @PostMapping("/job/replicate")
    public ReplicateObjectResponse replicateObject(@RequestBody ReplicateObjectRequest request) {
        RequestFieldValidator.notBlank(request.getVault(), "vault");
        RequestFieldValidator.notBlank(request.getExternalObjectId(), "externalObjectId");
        RequestFieldValidator.notNull(request.getDestination(), "destination");

        var objectId = preservationService.getInternalObjectId(request.getVault(), request.getExternalObjectId());

        return preservationAuth.applyIfServiceAdmin(() -> {
            var jobIds = preservationService.replicateObject(objectId,
                    request.getVersions(),
                    modelMapper.map(request.getDestination(), DataStore.class));
            jobBroker.checkPendingJobs();
            return new ReplicateObjectResponse().jobIds(jobIds);
        });
    }

    @PostMapping("/job/restore")
    public RestorePreservationObjectResponse restoreObject(@RequestBody RestorePreservationObjectRequest request) {
        RequestFieldValidator.notBlank(request.getVault(), "vault");
        RequestFieldValidator.notBlank(request.getExternalObjectId(), "externalObjectId");

        var objectId = preservationService.getInternalObjectId(request.getVault(), request.getExternalObjectId());

        return preservationAuth.applyIfServiceAdmin(() -> {
            var jobId = preservationService.restoreObject(objectId,
                    request.getVersions(),
                    DataStore.GLACIER);
            jobBroker.checkPendingJobs();
            return new RestorePreservationObjectResponse().jobId(jobId);
        });
    }

    @PostMapping("/job/cancel")
    public void cancelJob(@RequestBody CancelJobRequest request) {
        RequestFieldValidator.notNull(request.getJobId(), "jobId");

        preservationAuth.applyIfServiceAdmin(() -> {
            jobService.updateJobState(request.getJobId(), JobState.CANCELLED);
        });
    }

    @GetMapping("/job/{jobId}/logs")
    public RetrieveLogsResponse getJobLogs(@PathVariable("jobId") Long jobId) {
        var job = jobService.retrieveJob(jobId);

        return preservationAuth.applyIfServiceAdmin(() -> {
            var logs = jobService.retrieveJobLogs(jobId);

            return new RetrieveLogsResponse().logEntries(modelMapper.mapList(logs, LogEntry.class));
        });
    }

    @PostMapping("/job/disable")
    public void disableJobTypes(@RequestBody DisableJobTypesRequest request) {
        preservationAuth.applyIfServiceAdmin(() -> {
            List<JobType> types;

            if (CollectionUtils.isEmpty(request.getTypes())) {
                types = Arrays.stream(JobType.values()).toList();
            } else {
                types = request.getTypes().stream().map(type -> JobType.valueOf(type.name())).toList();
            }

            jobManager.disableJobsOfType(types);
        });
    }

    @PostMapping("/job/enable")
    public void enableJobTypes(@RequestBody EnableJobTypesRequest request) {
        preservationAuth.applyIfServiceAdmin(() -> {
            List<JobType> types;

            if (CollectionUtils.isEmpty(request.getTypes())) {
                types = Arrays.stream(JobType.values()).toList();
            } else {
                types = request.getTypes().stream().map(type -> JobType.valueOf(type.name())).toList();
            }

            jobManager.enableJobsOfType(types);
        });
    }

}
