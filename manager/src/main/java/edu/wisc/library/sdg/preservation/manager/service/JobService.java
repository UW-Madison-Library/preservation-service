package edu.wisc.library.sdg.preservation.manager.service;

import edu.wisc.library.sdg.preservation.common.exception.NotFoundException;
import edu.wisc.library.sdg.preservation.common.exception.ValidationException;
import edu.wisc.library.sdg.preservation.common.util.ArgCheck;
import edu.wisc.library.sdg.preservation.common.util.Time;
import edu.wisc.library.sdg.preservation.manager.common.OperationContext;
import edu.wisc.library.sdg.preservation.manager.db.model.DataStore;
import edu.wisc.library.sdg.preservation.manager.db.model.DeleteDipJobDetails;
import edu.wisc.library.sdg.preservation.manager.db.model.FinalizeRestoreJobDetails;
import edu.wisc.library.sdg.preservation.manager.db.model.IngestBatch;
import edu.wisc.library.sdg.preservation.manager.db.model.Job;
import edu.wisc.library.sdg.preservation.manager.db.model.JobDependency;
import edu.wisc.library.sdg.preservation.manager.db.model.JobLog;
import edu.wisc.library.sdg.preservation.manager.db.model.JobState;
import edu.wisc.library.sdg.preservation.manager.db.model.JobType;
import edu.wisc.library.sdg.preservation.manager.db.model.PreservationObject;
import edu.wisc.library.sdg.preservation.manager.db.model.PreservationObjectState;
import edu.wisc.library.sdg.preservation.manager.db.model.PreservationObjectVersion;
import edu.wisc.library.sdg.preservation.manager.db.model.PreservationObjectVersionLocation;
import edu.wisc.library.sdg.preservation.manager.db.model.ProcessBatchJobDetails;
import edu.wisc.library.sdg.preservation.manager.db.model.ReplicateJobDetails;
import edu.wisc.library.sdg.preservation.manager.db.model.RestoreJobDetails;
import edu.wisc.library.sdg.preservation.manager.db.model.RetrieveJobDetails;
import edu.wisc.library.sdg.preservation.manager.db.model.ValidateLocalJobDetails;
import edu.wisc.library.sdg.preservation.manager.db.model.ValidateRemoteJobDetails;
import edu.wisc.library.sdg.preservation.manager.db.repo.DeleteDipJobDetailsRepository;
import edu.wisc.library.sdg.preservation.manager.db.repo.FinalizeRestoreJobDetailsRepository;
import edu.wisc.library.sdg.preservation.manager.db.repo.JobDependencyRepository;
import edu.wisc.library.sdg.preservation.manager.db.repo.JobLogRepository;
import edu.wisc.library.sdg.preservation.manager.db.repo.JobRepository;
import edu.wisc.library.sdg.preservation.manager.db.repo.ProcessBatchJobDetailsRepository;
import edu.wisc.library.sdg.preservation.manager.db.repo.ReplicateJobDetailsRepository;
import edu.wisc.library.sdg.preservation.manager.db.repo.RestoreJobDetailsRepository;
import edu.wisc.library.sdg.preservation.manager.db.repo.RetrieveJobDetailsRepository;
import edu.wisc.library.sdg.preservation.manager.db.repo.ValidateLocalJobDetailsRepository;
import edu.wisc.library.sdg.preservation.manager.db.repo.ValidateRemoteJobDetailsRepository;
import edu.wisc.library.sdg.preservation.manager.service.job.BagContents;
import edu.wisc.library.sdg.preservation.manager.service.job.BagPacker;
import edu.wisc.library.sdg.preservation.manager.service.job.ObjectDetails;
import edu.wisc.library.sdg.preservation.manager.service.job.ObjectVersionDetails;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class JobService {

    private static final Logger LOG = LoggerFactory.getLogger(JobService.class);

    private final long maxBagSizeBytes;
    private final Duration deepValidationPeriod;
    private final Duration shallowValidationPeriod;
    private final Duration glacierValidationPeriod;

    private final JobRepository jobRepo;
    private final JobLogRepository jobLogRepo;
    private final RetrieveJobDetailsRepository retrieveJobRepo;
    private final ReplicateJobDetailsRepository replicateJobRepo;
    private final RestoreJobDetailsRepository restoreJobRepo;
    private final FinalizeRestoreJobDetailsRepository finalizeRestoreJobRepo;
    private final ValidateLocalJobDetailsRepository validateLocalJobRepo;
    private final ValidateRemoteJobDetailsRepository validateRemoteJobRepo;
    private final ProcessBatchJobDetailsRepository processBatchJobDetailsRepo;
    private final DeleteDipJobDetailsRepository deleteDipJobDetailsRepo;
    private final JobDependencyRepository jobDependencyRepo;
    private PreservationService preservationService;

    @Autowired
    public JobService(@Value("${app.bag.max-size-bytes}") long maxBagSizeBytes,
                      @Value("${app.schedule.validation.local.deep.period}") Duration deepValidationPeriod,
                      @Value("${app.schedule.validation.local.shallow.period}") Duration shallowValidationPeriod,
                      @Value("${app.schedule.validation.glacier.period}") Duration glacierValidationPeriod,
                      JobRepository jobRepo,
                      JobLogRepository jobLogRepo,
                      RetrieveJobDetailsRepository retrieveJobRepo,
                      ReplicateJobDetailsRepository replicateJobRepo,
                      RestoreJobDetailsRepository restoreJobRepo,
                      FinalizeRestoreJobDetailsRepository finalizeRestoreJobRepo,
                      ValidateLocalJobDetailsRepository validateLocalJobRepo,
                      ValidateRemoteJobDetailsRepository validateRemoteJobRepo,
                      ProcessBatchJobDetailsRepository processBatchJobDetailsRepo,
                      DeleteDipJobDetailsRepository deleteDipJobDetailsRepo,
                      JobDependencyRepository jobDependencyRepo) {
        this.maxBagSizeBytes = maxBagSizeBytes;
        this.deepValidationPeriod = ArgCheck.condition(deepValidationPeriod, !deepValidationPeriod.isNegative(),
                "deepValidationPeriod must be greater than 0");
        this.shallowValidationPeriod = ArgCheck.condition(shallowValidationPeriod, !shallowValidationPeriod.isNegative(),
                "shallowValidationPeriod must be greater than 0");
        this.glacierValidationPeriod = ArgCheck.condition(glacierValidationPeriod, !glacierValidationPeriod.isNegative(),
                "glacierValidationPeriod must be greater than 0");

        this.jobRepo = jobRepo;
        this.jobLogRepo = jobLogRepo;
        this.retrieveJobRepo = retrieveJobRepo;
        this.replicateJobRepo = replicateJobRepo;
        this.restoreJobRepo = restoreJobRepo;
        this.finalizeRestoreJobRepo = finalizeRestoreJobRepo;
        this.validateLocalJobRepo = validateLocalJobRepo;
        this.validateRemoteJobRepo = validateRemoteJobRepo;
        this.processBatchJobDetailsRepo = processBatchJobDetailsRepo;
        this.deleteDipJobDetailsRepo = deleteDipJobDetailsRepo;
        this.jobDependencyRepo = jobDependencyRepo;
    }

    // Setter injection to break circular dependency
    @Autowired
    public void setPreservationService(PreservationService preservationService) {
        this.preservationService = preservationService;
    }

    @Transactional
    public List<Long> createRetrieveJobs(OperationContext context,
                                         List<String> externalObjectIds,
                                         boolean allVersions) {
        ArgCheck.notNull(context, "context");

        var jobIds = new ArrayList<Long>();

        var bagPacker = new BagPacker(maxBagSizeBytes);
        var objects = loadObjectDetails(context.getVault(), externalObjectIds, allVersions);

        for (var it = objects.iterator(); it.hasNext();) {
            var object = it.next();
            if (object.getState() != PreservationObjectState.ACTIVE) {
                LOG.warn("Skipping retrieve of object {} because it is in state {}",
                        object.getObjectId(), object.getState());
                continue;
            }

            var fullBags = bagPacker.addObject(object);

            if (!fullBags.isEmpty()) {
                jobIds.addAll(createRetrieveJobs(context, fullBags));
            }
        }

        var bags = bagPacker.getBags();

        jobIds.addAll(createRetrieveJobs(context, bags));

        return jobIds;
    }

    /**
     * Queues an object version to be replicated to all remote stores after it was ingested. This method should ONLY
     * be used for replication after ingestion. It will filter out data stores that have already been replicated to.
     *
     * @param objectVersion the object version to replicate
     * @return the ids of the replication jobs, one per remote
     */
    @Transactional
    public List<Long> createReplicateAfterIngestJobs(PreservationObjectVersion objectVersion) {
        ArgCheck.notNull(objectVersion, "objectVersion");

        var object = preservationService.getObject(objectVersion.getObjectId());
        var orgName = preservationService.getOrgNameByObjectVersionId(objectVersion.getObjectVersionId());

        var stored = preservationService.getStorageLocations(object.getObjectId(), objectVersion.getPersistenceVersion())
                .stream().map(PreservationObjectVersionLocation::getDataStore).collect(Collectors.toSet());

        return DataStore.REMOTES.stream()
                // Filter out data stores that have already been replicated to
                .filter(dataStore -> !stored.contains(dataStore))
                .map(dataStore -> {
                    return createReplicateJob(objectVersion.getObjectId(),
                            objectVersion.getPersistenceVersion(),
                            orgName,
                            DataStore.LOCAL,
                            dataStore,
                            objectVersion.getIngestId(),
                            object.getExternalObjectId());
        }).map(Job::getJobId).collect(Collectors.toList());
    }

    /**
     * Queues an object version to be replicated to a remote store.
     *
     * @param objectId the internal id of the object to replicate
     * @param persistenceVersion the persistence version  of the object version to replicate
     * @param destination the datastore to replicate the object version to
     * @return the ids of the replication jobs, one per remote
     */
    @Transactional
    public Long createReplicateJob(UUID objectId, String persistenceVersion, DataStore destination) {
        ArgCheck.notNull(objectId, "objectId");
        ArgCheck.notNull(destination, "destination");

        var object = preservationService.getObject(objectId);

        validateObjectIsActive(object);

        var orgName = preservationService.getOrgNameByObjectId(objectId);

        return createReplicateJob(objectId,
                persistenceVersion,
                orgName,
                DataStore.LOCAL,
                destination,
                null,
                object.getExternalObjectId()).getJobId();
    }

    @Transactional
    public Long createRestoreJob(PreservationObjectVersionLocation objectVersionLocation) {
        ArgCheck.notNull(objectVersionLocation, "objectVersionLocation");

        validateObjectIsActive(objectVersionLocation.getObjectId());
        var orgName = preservationService.getOrgNameByObjectId(objectVersionLocation.getObjectId());

        var job = createJob(JobType.RESTORE, orgName);

        var jobDetails = new RestoreJobDetails()
                .setJobId(job.getJobId())
                .setObjectVersionLocationId(objectVersionLocation.getObjectVersionLocationId());

        LOG.debug("Creating job details: {}", jobDetails);
        restoreJobRepo.save(jobDetails);

        return job.getJobId();
    }

    @Transactional
    public Long createFinalizeRestoreJob(UUID objectId, List<Long> restoreJobIds) {
        ArgCheck.notNull(objectId, "objectId");
        ArgCheck.notNull(restoreJobIds, "restoreJobIds");

        var orgName = preservationService.getOrgNameByObjectId(objectId);

        var job = createJob(JobType.FINALIZE_RESTORE, orgName);

        var details = new FinalizeRestoreJobDetails()
                .setJobId(job.getJobId())
                .setObjectId(objectId);

        LOG.debug("Creating job details: {}", details);
        finalizeRestoreJobRepo.save(details);

        var deps = restoreJobIds.stream().map(jobId -> {
            return new JobDependency()
                    .setJobId(job.getJobId())
                    .setDependsOnJobId(jobId);
        }).collect(Collectors.toList());

        jobDependencyRepo.saveAll(deps);

        return job.getJobId();
    }

    @Transactional
    public Long createValidateLocalJob(UUID objectId, boolean contentFixityCheck, boolean background) {
        ArgCheck.notNull(objectId, "objectId");

        validateObjectIsActive(objectId);
        var orgName = preservationService.getOrgNameByObjectId(objectId);

        var job = createJob(JobType.VALIDATE_LOCAL, orgName, builder -> {
            builder.setBackground(background);
        });

        var details = new ValidateLocalJobDetails()
                .setJobId(job.getJobId())
                .setObjectId(objectId)
                .setContentFixityCheck(contentFixityCheck);

        LOG.debug("Creating job details: {}", details);
        validateLocalJobRepo.save(details);

        return job.getJobId();
    }

    @Transactional
    public Long createValidateRemoteJob(PreservationObjectVersionLocation location,
                                        boolean background) {
        ArgCheck.notNull(location, "location");

        validateObjectIsActive(location.getObjectId());
        var orgName = preservationService.getOrgNameByObjectId(location.getObjectId());

        var job = createJob(JobType.VALIDATE_REMOTE, orgName, builder -> {
            builder.setBackground(background);
        });

        var details = new ValidateRemoteJobDetails()
                .setJobId(job.getJobId())
                .setObjectVersionLocationId(location.getObjectVersionLocationId());

        LOG.debug("Creating job details: {}", details);
        validateRemoteJobRepo.save(details);

        return job.getJobId();
    }

    @Transactional
    public Long createDeleteDipJob(Long retrieveRequestId, String orgName) {
        ArgCheck.notNull(retrieveRequestId, "retrieveRequestId");
        ArgCheck.notBlank(orgName, "orgName");

        var job = createJob(JobType.DELETE_DIP, orgName);

        var details = new DeleteDipJobDetails()
                .setJobId(job.getJobId())
                .setRetrieveRequestId(retrieveRequestId);

        LOG.debug("Creating job details: {}", details);
        deleteDipJobDetailsRepo.save(details);

        return job.getJobId();
    }

    @Transactional
    public Long createCleanupSipsJob() {
        var job = createJob(JobType.CLEANUP_SIPS, null);
        return job.getJobId();
    }

    @Transactional
    public Long createProcessBatchJob(IngestBatch batch) {
        ArgCheck.notNull(batch, "batch");

        var job = createJob(JobType.PROCESS_BATCH, batch.getOrgName());

        var details = new ProcessBatchJobDetails()
                .setJobId(job.getJobId())
                .setIngestId(batch.getIngestId());

        LOG.debug("Creating job details: {}", details);
        processBatchJobDetailsRepo.save(details);

        return job.getJobId();
    }

    @Transactional
    public void updateJobState(Long jobId, JobState state) {
        ArgCheck.notNull(jobId, "jobId");
        ArgCheck.notNull(state, "state");

        var job = retrieveJob(jobId);
        var originalState = job.getState();

        validateStateTransition(originalState, state);
        jobRepo.save(job.setState(state).setUpdatedTimestamp(Time.now()));
    }

    public void deferJob(Long jobId, OffsetDateTime nextAttemptTimestamp) {
        ArgCheck.notNull(jobId, "jobId");
        ArgCheck.notNull(nextAttemptTimestamp, "nextAttemptTimestamp");

        var job = retrieveJob(jobId);
        var originalState = job.getState();

        if (originalState != JobState.PENDING) {
            validateStateTransition(originalState, JobState.PENDING);
        }

        job.setState(JobState.PENDING)
                .setNextAttemptTimestamp(nextAttemptTimestamp.toLocalDateTime())
                .setUpdatedTimestamp(Time.now());
        jobRepo.save(job);
    }

    public Job retrieveJob(Long jobId) {
        ArgCheck.notNull(jobId, "jobId");
        return jobRepo.findById(jobId)
                .orElseThrow(() -> new NotFoundException(String.format("Job <%s> was not found.", jobId)));
    }

    public List<Job> getJobs(String orgName) {
        ArgCheck.notBlank(orgName, "orgName");
        return jobRepo.findAllByOrgName(orgName);
    }

    public List<Job> getExecutingJobs() {
        return jobRepo.findAllByState(JobState.EXECUTING);
    }

    public List<Long> getPendingBackgroundJobIds() {
        return jobRepo.findAllJobIdsPendingExecution(Time.now(), true);
    }

    public List<Long> getPendingRegularJobIds() {
        return jobRepo.findAllJobIdsPendingExecution(Time.now(), false);
    }

    public Set<JobState> getDependencyStates(Long jobId) {
        return new HashSet<>(jobRepo.findAllDependencyStatesForJob(jobId));
    }

    public List<RetrieveJobDetails> getRetrieveJobDetails(Long jobId) {
        return retrieveJobRepo.findAllByJobId(jobId);
    }

    public ReplicateJobDetails getReplicateJobDetails(Long jobId) {
        return replicateJobRepo.findByJobId(jobId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Replicate job details for job %s could not be found", jobId)));
    }

    public RestoreJobDetails getRestoreJobDetails(Long jobId) {
        return restoreJobRepo.findByJobId(jobId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Restore job details for job %s could not be found", jobId)));
    }

    public FinalizeRestoreJobDetails getFinalizeJobDetails(Long jobId) {
        return finalizeRestoreJobRepo.findByJobId(jobId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Finalize restore job details for job %s could not be found", jobId)));
    }

    public ValidateLocalJobDetails getValidateLocalJobDetails(Long jobId) {
        return validateLocalJobRepo.findByJobId(jobId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Validate local job details for job %s could not be found", jobId)));
    }

    public ValidateRemoteJobDetails getValidateRemoteJobDetails(Long jobId) {
        return validateRemoteJobRepo.findByJobId(jobId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Validate remote job details for job %s could not be found", jobId)));
    }

    public ProcessBatchJobDetails getProcessBatchJobDetails(Long jobId) {
        return processBatchJobDetailsRepo.findByJobId(jobId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Process batch details for job %s could not be found", jobId)));
    }

    public DeleteDipJobDetails getDeleteDipJobDetails(Long jobId) {
        return deleteDipJobDetailsRepo.findByJobId(jobId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Delete DIP details for job %s could not be found", jobId)));
    }

    @Transactional
    public void recordJobLogs(List<JobLog> logs) {
        ArgCheck.notNull(logs, "logs");
        jobLogRepo.saveAll(logs);
    }

    public List<JobLog> retrieveJobLogs(Long jobId) {
        ArgCheck.notNull(jobId, "jobId");
        return jobLogRepo.findAllByJobId(jobId);
    }

    /**
     * Identifies all of the objects that are due for local validation and creates jobs to validate them.
     */
    // It isn't great that this transactional at this level, but it's difficult to move the transaction to the job level
    // because transactions are only visible when they cross class bounds
    @Transactional
    public void scheduleLocalObjectValidations() {
        var now = Time.now();
        var lastDeepCheck = now.minus(deepValidationPeriod).truncatedTo(ChronoUnit.HOURS);
        var lastShallowCheck = now.minus(shallowValidationPeriod).truncatedTo(ChronoUnit.HOURS);

        var activeDeepIds = validateLocalJobRepo.findAllObjectIdsForActiveJobs(true);
        var activeShallowIds = validateLocalJobRepo.findAllObjectIdsForActiveJobs(false);

        // There's a race condition here, but it should not happen in practice because we only look for new jobs once an hour

        var deepIds = preservationService.getObjectsNeedingDeepValidation(lastDeepCheck)
                .stream().filter(id -> !activeDeepIds.contains(id)).toList();
        var shallowIds = preservationService.getObjectsNeedingShallowValidation(lastShallowCheck)
                .stream().filter(id -> !deepIds.contains(id)
                        && !activeDeepIds.contains(id)
                        && !activeShallowIds.contains(id))
                .toList();

        // It would be more efficient to a bulk save here -- revisit if it becomes a performance problem.
        deepIds.forEach(id -> createValidateLocalJob(id, true, true));
        shallowIds.forEach(id -> createValidateLocalJob(id, false, true));
    }

    /**
     * Identifies all of the objects that are due for remote validation and creates jobs to validate them.
     */
    // It isn't great that this transactional at this level, but it's difficult to move the transaction to the job level
    // because transactions are only visible when they cross class bounds
    @Transactional
    public void scheduleRemoteObjectValidations() {
        // TODO note this only supports glacier currently

        var lastCheck = Time.now().minus(glacierValidationPeriod).truncatedTo(ChronoUnit.HOURS);

        var activeIds = validateRemoteJobRepo.findAllLocationIdsForActiveJobs();

        // There's a race condition here, but it should not happen in practice because we only look for new jobs once an hour

        var glacierLocations = preservationService.getRemoteLocationsNeedingValidation(DataStore.GLACIER, lastCheck)
                .stream().filter(location -> !activeIds.contains(location.getObjectVersionLocationId())).toList();

        // It would be more efficient to a bulk save here -- revisit if it becomes a performance problem.
        glacierLocations.forEach(location -> createValidateRemoteJob(location, true));
    }

    /**
     * Returns the number of replication jobs associated with the batch that have not been completed.
     *
     * @param ingestId the id of the batch
     * @return number of incomplete replication jobs
     */
    public long countIncompleteIngestReplicationJobs(Long ingestId) {
        ArgCheck.notNull(ingestId, "ingestId");
        return jobRepo.countIncompleteIngestReplicationJobs(ingestId);
    }

    private Stream<ObjectDetails> loadObjectDetails(String vault,
                                                    List<String> externalObjectIds,
                                                    boolean allVersions) {
        if (CollectionUtils.isEmpty(externalObjectIds)) {
            return preservationService.getAllObjectsInVault(vault).map(object -> {
                return loadObjectDetails(object, allVersions);
            });
        } else {
            return externalObjectIds.stream().map(externalId -> {
                var object = preservationService.getObject(vault, externalId);
                return loadObjectDetails(object, allVersions);
            });
        }
    }

    private ObjectDetails loadObjectDetails(PreservationObject object, boolean allVersions) {
        var objectDetails = new ObjectDetails(object.getObjectId(), object.getState());

        var headVersion = preservationService.getObjectVersion(object.getHeadObjectVersionId());
        var headSize = preservationService.calculateObjectSize(headVersion.getObjectVersionId());

        objectDetails.addVersion(new ObjectVersionDetails(headVersion.getObjectVersionId(), headSize, headVersion.getVersion()));

        if (allVersions) {
            for (int i = headVersion.getVersion() - 1; i > 0; i--) {
                var version = preservationService.getObjectVersion(object.getObjectId(), i);
                var size = preservationService.calculateObjectSize(version.getObjectVersionId());
                objectDetails.addVersion(new ObjectVersionDetails(version.getObjectVersionId(), size, version.getVersion()));
            }
        }

        objectDetails.sort();

        return objectDetails;
    }

    private void validateStateTransition(JobState original, JobState updated) {
        if (!original.isTransitionAllowed(updated)) {
            throw new ValidationException(String.format("Cannot transition from state %s to state %s.", original, updated));
        }
    }

    private List<Long> createRetrieveJobs(OperationContext context, Collection<BagContents> bags) {
        var jobIds = new ArrayList<Long>();
        for (var bag : bags) {
            var job = createJob(JobType.RETRIEVE_OBJECTS, context.getOrgName());

            var jobDetails = bag.getContents().stream().map(details -> {
                return new RetrieveJobDetails()
                        .setJobId(job.getJobId())
                        .setObjectVersionId(details.getObjectVersionId());
            }).collect(Collectors.toList());

            LOG.debug("Creating retrieve job details: {}", jobDetails);
            retrieveJobRepo.saveAll(jobDetails);

            jobIds.add(job.getJobId());
        }
        return jobIds;
    }

    private Job createJob(JobType type, String orgName) {
        return createJob(type, orgName, null);
    }

    private Job createJob(JobType type, String orgName, Consumer<Job> customizer) {
        var job =  new Job()
                .setType(type)
                .setState(JobState.PENDING)
                .setOrgName(orgName)
                .setReceivedTimestamp(Time.now())
                .setUpdatedTimestamp(Time.now())
                .setBackground(false);

        if (customizer != null) {
            customizer.accept(job);
        }

        LOG.debug("Creating job: {}", job);
        return jobRepo.save(job);
    }

    private Job createReplicateJob(UUID objectId,
                                   String persistenceVersion,
                                   String orgName,
                                   DataStore source,
                                   DataStore destination,
                                   Long ingestId,
                                   String externalObjectId) {
        var job = createJob(JobType.REPLICATE, orgName);

        var details = new ReplicateJobDetails()
                .setJobId(job.getJobId())
                .setObjectId(objectId)
                .setPersistenceVersion(persistenceVersion)
                .setSource(source)
                .setDestination(destination)
                .setIngestId(ingestId)
                .setExternalObjectId(externalObjectId);
        LOG.debug("Creating job details: {}", details);
        replicateJobRepo.save(details);

        return job;
    }

    private void validateObjectIsActive(UUID objectId) {
        validateObjectIsActive(preservationService.getObject(objectId));
    }

    private void validateObjectIsActive(PreservationObject object) {
        if (object.getState() != PreservationObjectState.ACTIVE) {
            var exceptionMessage = String.format(
                    "Cannot perform actions on object <%s> in vault <%s> because it is not in an active state",
                    object.getExternalObjectId(),
                    object.getVault()
            );
            throw new ValidationException(exceptionMessage);
        }
    }

}
