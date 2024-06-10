package edu.wisc.library.sdg.preservation.manager.controller;

import edu.wisc.library.sdg.preservation.common.exception.NotFoundException;
import edu.wisc.library.sdg.preservation.common.exception.ValidationException;
import edu.wisc.library.sdg.preservation.common.util.RequestFieldValidator;
import edu.wisc.library.sdg.preservation.common.util.Time;
import edu.wisc.library.sdg.preservation.common.util.UuidUtils;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.BatchCompleteAnalysisRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.BatchCompleteIngestingRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.BatchCompleteRejectingRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.BatchMarkDeletedRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.BatchObjectWithFiles;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.BatchStartAnalysisRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.BatchStartIngestingRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.BatchStartRejectingRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.CleanupSipsJob;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.CreateObjectVersionRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.CreateObjectVersionResponse;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.DeferJobRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.DeleteDipJob;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.DeleteObjectVersionRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.DescribeObjectVersionStatesResponse;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.Event;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.FinalizeObjectVersionRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.FinalizeRestoreJob;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.GetObjectStorageProblemsResponse;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.IngestBatch;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.JobPollResponse;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.JobType;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ObjectCompleteAnalysisRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ObjectCompleteIngestingRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ObjectFile;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ObjectStartIngestingRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ObjectStorageProblem;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ObjectVersionReplicatedRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ObjectVersionState;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ObjectVersionStateState;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.Outcome;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ProcessBatchJob;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RecordIngestEventRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RecordJobLogsRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RecordPreservationEventRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RegisterIngestObjectFileDetailsRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RegisterIngestObjectFileRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RegisterIngestObjectFileResponse;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RegisterIngestObjectRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RegisterIngestObjectResponse;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ReplicateJob;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RestoreJob;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RetrieveBatchIngestResponse;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RetrieveBatchResponse;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RetrieveJob;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RetrieveNewInVersionFilesResponse;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.SetObjectStorageProblemsRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ShouldDeleteBatchRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ShouldDeleteBatchResponse;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.UpdateJobStateRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ValidateLocalJob;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ValidateRemoteJob;
import edu.wisc.library.sdg.preservation.manager.db.model.DataStore;
import edu.wisc.library.sdg.preservation.manager.db.model.IngestBatchObjectFileEncodingComposite;
import edu.wisc.library.sdg.preservation.manager.db.model.IngestBatchObjectFileFormatComposite;
import edu.wisc.library.sdg.preservation.manager.db.model.IngestBatchObjectFileValidityComposite;
import edu.wisc.library.sdg.preservation.manager.db.model.IngestEvent;
import edu.wisc.library.sdg.preservation.manager.db.model.IngestObjectState;
import edu.wisc.library.sdg.preservation.manager.db.model.JobLog;
import edu.wisc.library.sdg.preservation.manager.db.model.JobState;
import edu.wisc.library.sdg.preservation.manager.db.model.PreservationEvent;
import edu.wisc.library.sdg.preservation.manager.db.model.PreservationObjectVersionComposite;
import edu.wisc.library.sdg.preservation.manager.db.model.StorageProblemType;
import edu.wisc.library.sdg.preservation.manager.job.JobBroker;
import edu.wisc.library.sdg.preservation.manager.job.JobManager;
import edu.wisc.library.sdg.preservation.manager.service.IngestService;
import edu.wisc.library.sdg.preservation.manager.service.JobService;
import edu.wisc.library.sdg.preservation.manager.service.PreservationService;
import edu.wisc.library.sdg.preservation.manager.service.UserService;
import edu.wisc.library.sdg.preservation.manager.util.FileDeletingInputStream;
import edu.wisc.library.sdg.preservation.manager.util.ModelMapper;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/internal/api")
@Timed(histogram = true)
public class InternalController {

    private static final Logger LOG = LoggerFactory.getLogger(InternalController.class);

    @Value("${app.temp.dir}")
    private Path tempDir;

    private final long pollSeconds;
    private final long deferredResultMillis;
    private final Duration sipRetentionPeriod;

    private final ModelMapper modelMapper;
    private final IngestService ingestService;
    private final PreservationService preservationService;
    private final JobService jobService;
    private final JobBroker jobBroker;
    private final JobManager jobManager;
    private final UserService userService;
    private final ExecutorService executorService;

    @Autowired
    public InternalController(ModelMapper modelMapper,
                              IngestService ingestService,
                              PreservationService preservationService,
                              JobService jobService,
                              UserService userService,
                              JobBroker jobBroker,
                              JobManager jobManager,
                              ExecutorService executorService,
                              @Value("${app.request.long-poll-seconds}") long pollSeconds,
                              @Value("${app.schedule.sip.retention.period}") Duration sipRetentionPeriod) {
        this.modelMapper = modelMapper;
        this.ingestService = ingestService;
        this.preservationService = preservationService;
        this.jobService = jobService;
        this.userService = userService;
        this.jobBroker = jobBroker;
        this.jobManager = jobManager;
        this.executorService = executorService;
        this.pollSeconds = pollSeconds;
        this.deferredResultMillis = (pollSeconds + 10) * 1000;
        this.sipRetentionPeriod = sipRetentionPeriod;
    }

    @GetMapping("/batch/{ingestId}")
    public RetrieveBatchResponse retrieveBatch(@PathVariable("ingestId") Long ingestId) {
        var batch = ingestService.getIngestBatch(ingestId);

        return new RetrieveBatchResponse()
                .ingestBatch(modelMapper.map(batch, IngestBatch.class));
    }

    @GetMapping("/batch/{ingestId}/ingest")
    public RetrieveBatchIngestResponse retrieveBatchIngest(@PathVariable("ingestId") Long ingestId) {
        var objects = ingestService.getAllObjects(ingestId);
        var userCache = userService.getAll();
        var filesCache = ingestService.getAllObjectFilesByIngestId(ingestId);

        var responseObjects = objects.stream().filter(o -> {
            return o.getState() == IngestObjectState.PENDING_INGESTION || o.getState() == IngestObjectState.INGESTING;
        }).map(o -> {
            var mapped = modelMapper.map(o, BatchObjectWithFiles.class);

            var user = userCache.stream()
                    .filter(u -> u.getUsername().equals(o.getReviewedBy()))
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException(String.format("User %s was not found", o.getReviewedBy())));
            mapped.approverName(user.getDisplayName()).approverAddress(user.getExternalId());

            var files = filesCache.stream()
                    .filter(f -> f.getIngestObjectId().equals(o.getIngestObjectId()))
                    .toList();
            mapped.setFiles(modelMapper.mapList(files, ObjectFile.class));

            if (o.getHeadObjectVersionId() != null) {
                var version = preservationService.getObjectVersion(o.getHeadObjectVersionId());
                mapped.internalObjectId(UuidUtils.withPrefix(version.getObjectId()))
                        .headPersistenceVersion(version.getPersistenceVersion());
            }

            return mapped;
        }).toList();

        return new RetrieveBatchIngestResponse().approvedObjects(responseObjects);
    }

    @PostMapping("/batch/startAnalysis")
    public void batchStartAnalysis(@RequestBody BatchStartAnalysisRequest request) {
        RequestFieldValidator.notNull(request.getIngestId(), "ingestId");

        ingestService.updateBatchStartAnalysis(request.getIngestId());
    }

    @PostMapping("/batch/completeAnalysis")
    public void batchCompleteAnalysis(@RequestBody BatchCompleteAnalysisRequest request) {
        RequestFieldValidator.notNull(request.getIngestId(), "ingestId");
        RequestFieldValidator.notNull(request.getOutcome(), "outcome");

        if (request.getOutcome() == Outcome.SUCCESS) {
            ingestService.updateBatchCompleteAnalysis(request.getIngestId());
        } else {
            ingestService.updateBatchFailAnalysis(request.getIngestId());
        }
    }

    @PostMapping("/batch/startRejecting")
    public void batchStartRejecting(@RequestBody BatchStartRejectingRequest request) {
        RequestFieldValidator.notNull(request.getIngestId(), "ingestId");

        ingestService.updateBatchStartRejecting(request.getIngestId());
    }

    @PostMapping("/batch/completeRejecting")
    public void batchCompleteRejecting(@RequestBody BatchCompleteRejectingRequest request) {
        RequestFieldValidator.notNull(request.getIngestId(), "ingestId");

        ingestService.updateBatchCompleteRejecting(request.getIngestId());
    }

    @PostMapping("/batch/startIngesting")
    public void batchStartIngesting(@RequestBody BatchStartIngestingRequest request) {
        RequestFieldValidator.notNull(request.getIngestId(), "ingestId");

        ingestService.updateBatchStartIngesting(request.getIngestId());
    }

    @PostMapping("/batch/completeIngesting")
    public void batchCompleteIngesting(@RequestBody BatchCompleteIngestingRequest request) {
        RequestFieldValidator.notNull(request.getIngestId(), "ingestId");
        RequestFieldValidator.notNull(request.getOutcome(), "outcome");

        if (request.getOutcome() == Outcome.SUCCESS) {
            ingestService.updateBatchCompleteIngestion(request.getIngestId());
        } else {
            ingestService.updateBatchFailIngestion(request.getIngestId());
        }
    }

    @PostMapping("/batch/markDeleted")
    public void batchMarkDeleted(@RequestBody BatchMarkDeletedRequest request) {
        RequestFieldValidator.notNull(request.getIngestId(), "ingestId");

        ingestService.updateBatchMarkDeleted(request.getIngestId());
    }

    @PostMapping("/batch/object")
    public RegisterIngestObjectResponse registerObject(@RequestBody RegisterIngestObjectRequest request) {
        RequestFieldValidator.notNull(request.getIngestId(), "ingestId");
        RequestFieldValidator.notBlank(request.getExternalObjectId(), "externalObjectId");
        RequestFieldValidator.notBlank(request.getObjectRootPath(), "objectRootPath");

        var batchObject = ingestService.registerBatchObject(
                request.getIngestId(),
                request.getExternalObjectId(),
                request.getObjectRootPath());

        return new RegisterIngestObjectResponse().ingestObjectId(batchObject.getIngestObjectId());
    }

    @PostMapping("/batch/object/file")
    public RegisterIngestObjectFileResponse registerObjectFile(@RequestBody RegisterIngestObjectFileRequest request) {
        RequestFieldValidator.notNull(request.getIngestObjectId(), "ingestObjectId");
        RequestFieldValidator.notBlank(request.getFilePath(), "filePath");

        var fileId = ingestService.registerFile(request.getIngestObjectId(), request.getFilePath());

        return new RegisterIngestObjectFileResponse().ingestFileId(fileId);
    }

    @PostMapping("/batch/object/file/details")
    public void registerObjectFileDetails(@RequestBody RegisterIngestObjectFileDetailsRequest request) {
        RequestFieldValidator.notNull(request.getIngestFileId(), "ingestFileId");
        RequestFieldValidator.notBlank(request.getSha256Digest(), "sha256Digest");
        RequestFieldValidator.notNull(request.getFileSize(), "fileSize");

        var formats = request.getFormats();
        List<IngestBatchObjectFileFormatComposite> mappedFormats = new ArrayList<>();

        if (formats != null) {
            formats.forEach(format -> {
                RequestFieldValidator.notNull(format.getFormatRegistry(), "formatRegistry");
                RequestFieldValidator.notBlank(format.getSource(), "source");
                RequestFieldValidator.notBlank(format.getFormat(), "format");
            });
            mappedFormats = modelMapper.mapList(formats, IngestBatchObjectFileFormatComposite.class);
        }

        var encoding = request.getEncoding();
        List<IngestBatchObjectFileEncodingComposite> mappedEncoding = new ArrayList<>();

        if (encoding != null) {
            encoding.forEach(e -> {
                RequestFieldValidator.notBlank(e.getSource(), "source");
                RequestFieldValidator.notBlank(e.getEncoding(), "encoding");
            });
            mappedEncoding = modelMapper.mapList(encoding, IngestBatchObjectFileEncodingComposite.class);
        }

        var validity = request.getValidity();
        List<IngestBatchObjectFileValidityComposite> mappedValidity = new ArrayList<>();

        if (validity != null) {
            validity.forEach(v -> {
                RequestFieldValidator.notBlank(v.getSource(), "source");
                if (v.getValid() == null && v.getWellFormed() == null) {
                    throw new ValidationException("Field 'validity' must have at least one of 'valid' or 'wellFormed' set");
                }
            });
            mappedValidity = modelMapper.mapList(validity, IngestBatchObjectFileValidityComposite.class);
        }

        ingestService.setFileDetails(request.getIngestFileId(),
                request.getSha256Digest(),
                request.getFileSize(),
                new HashSet<>(mappedFormats),
                new HashSet<>(mappedEncoding),
                new HashSet<>(mappedValidity));
    }

    @PostMapping("/batch/object/completeAnalysis")
    public void objectCompleteAnalysis(@RequestBody ObjectCompleteAnalysisRequest request) {
        RequestFieldValidator.notNull(request.getIngestId(), "ingestId");
        RequestFieldValidator.notBlank(request.getExternalObjectId(), "externalObjectId");
        RequestFieldValidator.notNull(request.getOutcome(), "outcome");

        if (request.getOutcome() == Outcome.SUCCESS) {
            ingestService.updateObjectCompleteAnalysis(request.getIngestId(), request.getExternalObjectId());
        } else {
            ingestService.updateObjectFailAnalysis(request.getIngestId(), request.getExternalObjectId());
        }
    }

    @PostMapping("/batch/object/startIngesting")
    public void objectStartIngesting(@RequestBody ObjectStartIngestingRequest request) {
        RequestFieldValidator.notNull(request.getIngestId(), "ingestId");
        RequestFieldValidator.notBlank(request.getExternalObjectId(), "externalObjectId");

        ingestService.updateObjectStartIngest(request.getIngestId(), request.getExternalObjectId());
    }

    @PostMapping("/batch/object/completeIngesting")
    public void objectCompleteIngesting(@RequestBody ObjectCompleteIngestingRequest request) {
        RequestFieldValidator.notNull(request.getIngestId(), "ingestId");
        RequestFieldValidator.notBlank(request.getExternalObjectId(), "externalObjectId");
        RequestFieldValidator.notNull(request.getOutcome(), "outcome");

        if (request.getOutcome() == ObjectCompleteIngestingRequest.OutcomeEnum.SUCCESS) {
            ingestService.updateObjectCompleteIngest(request.getIngestId(), request.getExternalObjectId());
        } else if (request.getOutcome() == ObjectCompleteIngestingRequest.OutcomeEnum.NO_CHANGE) {
            ingestService.updateObjectIngestNoChange(request.getIngestId(), request.getExternalObjectId());
        } else {
            ingestService.updateObjectIngestFailed(request.getIngestId(), request.getExternalObjectId());
        }
        jobBroker.checkPendingJobs();
    }

    @PostMapping("/batch/event")
    public void recordIngestLogs(@RequestBody RecordIngestEventRequest request) {
        var ingestId = request.getIngestId();
        var externalObjectId = request.getExternalObjectId();
        var event = request.getEvent();

        RequestFieldValidator.notNull(ingestId, "ingestId");
        validateEventRequest(event);

        var ingestEvent = modelMapper.map(event, IngestEvent.class);
        ingestEvent.setIngestId(ingestId);
        ingestEvent.setExternalObjectId(externalObjectId);

        ingestService.recordIngestEvent(ingestEvent);
    }

    @PostMapping("/batch/shouldDelete")
    public ShouldDeleteBatchResponse shouldDeleteBatch(@RequestBody ShouldDeleteBatchRequest request) {
        if (request.getIngestId() == null && request.getSipPath() == null) {
            throw new ValidationException("Either ingestId or sipPath must be set");
        } else if (request.getIngestId() != null && request.getSipPath() != null) {
            throw new ValidationException("Both ingestId or sipPath cannot be set");
        }

        edu.wisc.library.sdg.preservation.manager.db.model.IngestBatch batch = null;
        var verdict = ShouldDeleteBatchResponse.VerdictEnum.KEEP;

        try {
            if (request.getIngestId() != null) {
                batch = ingestService.getIngestBatch(request.getIngestId());
            } else {
                batch = ingestService.getIngestBatchByFilePath(request.getSipPath());
            }

            var cutoff = Time.now().minus(sipRetentionPeriod).truncatedTo(ChronoUnit.HOURS);

            if (batch.getUpdatedTimestamp().isBefore(cutoff)) {
                verdict = ShouldDeleteBatchResponse.VerdictEnum.DELETE;
            }
        } catch (NotFoundException e) {
            verdict = ShouldDeleteBatchResponse.VerdictEnum.NOT_FOUND;
        }

        var id = batch != null ? batch.getIngestId() : null;

        return new ShouldDeleteBatchResponse().ingestId(id).verdict(verdict);
    }

    @PutMapping("/object/version")
    @Transactional
    public CreateObjectVersionResponse createObjectVersion(@RequestBody CreateObjectVersionRequest request) {
        RequestFieldValidator.notBlank(request.getObjectId(), "objectId");
        RequestFieldValidator.notNull(request.getIngestObjectId(), "ingestObjectId");

        var objectId = UuidUtils.fromString(request.getObjectId());
        var batchObject = ingestService.getIngestObject(request.getIngestObjectId());
        var batch = ingestService.getIngestBatch(batchObject.getIngestId());
        var files = ingestService.getAllObjectFiles(batch.getIngestId(), batchObject.getExternalObjectId());

        var objectVersion = preservationService.createObjectVersion(objectId,
                batch, batchObject, files);

        LOG.info("Created object version: {}", objectVersion);

        return new CreateObjectVersionResponse()
                .objectId(UuidUtils.withPrefix(objectVersion.getObjectId()))
                .version(objectVersion.getVersion())
                .objectVersionId(objectVersion.getObjectVersionId());
    }

    @DeleteMapping("/object/version")
    public void deleteObjectVersion(@RequestBody DeleteObjectVersionRequest request) {
        RequestFieldValidator.notNull(request.getObjectVersionId(), "objectVersionId");

        preservationService.deleteObjectVersion(request.getObjectVersionId());
    }

    @PostMapping("/object/version/finalize")
    @Transactional
    public void finalObjectVersion(@RequestBody FinalizeObjectVersionRequest request) {
        RequestFieldValidator.notNull(request.getIngestObjectId(), "ingestObjectId");
        RequestFieldValidator.notNull(request.getObjectVersionId(), "objectVersionId");
        RequestFieldValidator.notBlank(request.getPersistenceVersion(), "persistenceVersion");

        preservationService.finalizeObjectVersion(request.getObjectVersionId(), request.getPersistenceVersion());
    }

    @PostMapping("/object/version/replicated")
    @Transactional
    public void objectVersionReplicated(@RequestBody ObjectVersionReplicatedRequest request) {
        RequestFieldValidator.notBlank(request.getObjectId(), "objectId");
        RequestFieldValidator.notBlank(request.getPersistenceVersion(), "persistenceVersion");
        RequestFieldValidator.notNull(request.getDataStore(), "dataStore");
        RequestFieldValidator.notBlank(request.getDataStoreKey(), "dataStoreKey");
        RequestFieldValidator.notBlank(request.getSha256Digest(), "sha256Digest");

        preservationService.markReplicatedToDataStore(UuidUtils.fromString(request.getObjectId()),
                request.getPersistenceVersion(),
                modelMapper.map(request.getDataStore(), DataStore.class),
                request.getDataStoreKey(), request.getSha256Digest());
    }

    @GetMapping("/object/{internalObjectId}/versionStates")
    public DescribeObjectVersionStatesResponse getObjectVersionStates(
            @PathVariable("internalObjectId") String internalObjectId) {
        // TODO this is not terribly efficient

        var headVersion = preservationService.getObjectVersionComposite(
                UuidUtils.fromString(internalObjectId), null);

        var versionStates = new ArrayList<ObjectVersionState>();

        var response = new DescribeObjectVersionStatesResponse()
                .vault(headVersion.getVault())
                .externalObjectId(headVersion.getExternalObjectId())
                .versionStates(versionStates);

        versionStates.add(createVersionState(headVersion));

        for (int i = headVersion.getVersion() - 1; i > 0; i--) {
            var version = preservationService.getObjectVersionComposite(
                    UuidUtils.fromString(internalObjectId), i);
            versionStates.add(createVersionState(version));
        }

        return response;
    }

    @GetMapping(value = "/object/premis", produces = { "application/xml" })
    public ResponseEntity<InputStreamResource> getPremisDocument(
            @RequestParam("internalObjectId") String objectId,
            @RequestParam(value = "versionNumbers", required = false) List<Integer> versions) {
        RequestFieldValidator.notBlank(objectId, "internalObjectId");
        RequestFieldValidator.notNull(versions, "versionNumbers");

        var objectUuid = UuidUtils.fromString(objectId);
        var premisFile = tempPremisFile();

        try (var output = Files.newOutputStream(premisFile)) {
            preservationService.createPremisXml(objectUuid, versions, output);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        try {
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=premis-" + objectUuid + ".xml")
                    .contentType(new MediaType("application", "xml"))
                    .contentLength(Files.size(premisFile))
                    .body(new InputStreamResource(
                            new FileDeletingInputStream(premisFile, Files.newInputStream(premisFile))));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @PutMapping("/object/problems/storage")
    @Transactional
    public void setObjectStorageProblems(@RequestBody SetObjectStorageProblemsRequest request) {
        RequestFieldValidator.notBlank(request.getObjectId(), "objectId");
        RequestFieldValidator.notNull(request.getObjectProblem(), "objectProblem");
        RequestFieldValidator.notNull(request.getDataStore(), "dataStore");

        var objectId = UuidUtils.fromString(request.getObjectId());
        var datastore = modelMapper.map(request.getDataStore(), DataStore.class);

        if (request.getObjectProblem() != null) {
            preservationService.setObjectStorageProblem(
                    objectId,
                    datastore,
                    modelMapper.map(request.getObjectProblem(), StorageProblemType.class));
        }

        if (request.getVersionProblems() != null) {
            request.getVersionProblems().forEach(versionProblem -> {
                preservationService.setObjectVersionStorageProblem(
                        objectId,
                        versionProblem.getPersistenceVersion(),
                        datastore,
                        modelMapper.map(versionProblem.getProblem(), StorageProblemType.class));
            });
        }
    }

    @GetMapping("/object/problems/storage")
    public GetObjectStorageProblemsResponse getObjectStorageProblems(
            @RequestParam("internalObjectId") String internalObjectId) {
        var objectId = UuidUtils.fromString(internalObjectId);
        var problems = preservationService.getObjectStorageProblems(objectId);
        return new GetObjectStorageProblemsResponse()
                .problems(modelMapper.mapList(problems, ObjectStorageProblem.class));
    }

    @PostMapping("/object/event")
    public void recordPreservationEvent(@RequestBody RecordPreservationEventRequest request) {
        RequestFieldValidator.notBlank(request.getObjectId(), "objectId");

        var objectId = UuidUtils.fromString(request.getObjectId());
        var event = request.getEvent();

        validateEventRequest(event);

        var preservationEvent = modelMapper.map(event, PreservationEvent.class);
        preservationEvent.setObjectId(objectId);

        preservationService.recordEvent(preservationEvent);
    }

    @GetMapping("/object/{internalObjectId}/persistenceVersion/{persistenceVersion}/filesNewInVersion")
    public RetrieveNewInVersionFilesResponse retrieveNewInVersionFiles(
            @PathVariable("internalObjectId") String internalObjectId,
            @PathVariable("persistenceVersion") String persistenceVersion) {
        var files = preservationService.getNewInVersionFiles(
                UuidUtils.fromString(internalObjectId), persistenceVersion);

        return new RetrieveNewInVersionFilesResponse().files(modelMapper.mapList(files, ObjectFile.class));
    }

    @PostMapping("/job/state")
    public void updateJobState(@RequestBody UpdateJobStateRequest request) {
        RequestFieldValidator.notNull(request.getJobId(), "jobId");
        RequestFieldValidator.notNull(request.getState(), "state");

        var state = modelMapper.map(request.getState(), JobState.class);

        if (state == JobState.COMPLETE) {
            jobManager.completeJob(request.getJobId());
        } else if (state == JobState.FAILED) {
            jobManager.failJob(request.getJobId());
        } else {
            jobService.updateJobState(request.getJobId(), state);
        }
        jobBroker.checkPendingJobs();
    }

    @PostMapping("/job/defer")
    public void deferJob(@RequestBody DeferJobRequest request) {
        RequestFieldValidator.notNull(request.getJobId(), "jobId");

        jobService.deferJob(request.getJobId(), request.getNextAttemptTimestamp());
    }

    @PostMapping("/job/logs")
    public void recordJobLogs(@RequestBody RecordJobLogsRequest request) {
        var entries = request.getLogEntries();

        RequestFieldValidator.notNull(request.getJobId(), "jobId");
        RequestFieldValidator.notNull(entries, "logEntries");

        entries.forEach(entry -> {
            RequestFieldValidator.notNull(entry.getLevel(), "level");
            RequestFieldValidator.notBlank(entry.getMessage(), "message");
            RequestFieldValidator.notNull(entry.getCreatedTimestamp(), "createdTimestamp");
        });

        var logs = modelMapper.mapList(entries, JobLog.class);

        logs.forEach(log -> {
            log.setJobId(request.getJobId());
        });

        jobService.recordJobLogs(logs);
    }

    @GetMapping("/job/poll")
    public DeferredResult<JobPollResponse> pollForJob() {
        var deferred = new DeferredResult<JobPollResponse>(deferredResultMillis);

        executorService.execute(() -> {
            try {
                var response = new JobPollResponse();

                var remainingSeconds = pollSeconds;

                // This is a little janky but is currently necessary to deal with parent jobs that are pending
                // but cannot be processed because their children are not complete yet
                while (true) {
                    var start = System.currentTimeMillis();
                    var jobId = jobBroker.pollJob(remainingSeconds, TimeUnit.SECONDS);
                    remainingSeconds -= (System.currentTimeMillis() - start) / 1000;

                    if (jobId != null) {
                        var job = jobManager.createWorkerJobWhenReady(jobId);
                        if (job != null) {
                            if (job instanceof FinalizeRestoreJob j) {
                                response.jobType(JobType.FINALIZE_RESTORE).finalizeRestoreJob(j);
                            } else if (job instanceof ProcessBatchJob j) {
                                response.jobType(JobType.PROCESS_BATCH).processBatchJob(j);
                            } else if (job instanceof ReplicateJob j) {
                                response.jobType(JobType.REPLICATE).replicateJob(j);
                            } else if (job instanceof RestoreJob j) {
                                response.jobType(JobType.RESTORE).restoreJob(j);
                            } else if (job instanceof RetrieveJob j) {
                                response.jobType(JobType.RETRIEVE_OBJECTS).retrieveJob(j);
                            } else if (job instanceof ValidateLocalJob j) {
                                response.jobType(JobType.VALIDATE_LOCAL).validateLocalJob(j);
                            } else if (job instanceof ValidateRemoteJob j) {
                                response.jobType(JobType.VALIDATE_REMOTE).validateRemoteJob(j);
                            } else if (job instanceof DeleteDipJob j) {
                                response.jobType(JobType.DELETE_DIP).deleteDipJob(j);
                            } else if (job instanceof CleanupSipsJob j) {
                                response.jobType(JobType.CLEANUP_SIPS).cleanupSipsJob(j);
                            } else {
                                throw new IllegalStateException(String.format("Unknown job type: %s", job));
                            }

                            // job request created -- break
                            break;
                        }
                    } else {
                        // no pending id found -- break
                        break;
                    }
                }

                deferred.setResult(response);
            } catch (RuntimeException e) {
                deferred.setErrorResult(e);
            }
        });

        return deferred;
    }

    private ObjectVersionState createVersionState(PreservationObjectVersionComposite version) {
        var state = new HashMap<String, ObjectVersionStateState>();

        version.getFiles().forEach(file -> {
            state.put(file.getFilePath(), new ObjectVersionStateState()
                    .sha256Digest(file.getSha256Digest())
                    .fileSize(file.getFileSize()));
        });

        return new ObjectVersionState()
                .version(version.getVersion())
                .persistenceVersion(version.getPersistenceVersion())
                .state(state);
    }

    private Path tempPremisFile() {
        try {
            Files.createDirectories(tempDir);
            return Files.createTempFile(tempDir, "premis", ".xml");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void validateEventRequest(Event event) {
        RequestFieldValidator.notNull(event, "event");
        RequestFieldValidator.notNull(event.getType(), "type");
        RequestFieldValidator.notNull(event.getOutcome(), "outcome");
        RequestFieldValidator.notNull(event.getEventTimestamp(), "eventTimestamp");

        if (event.getLogs() != null) {
            event.getLogs().forEach(logEntry -> {
                RequestFieldValidator.notNull(logEntry.getLevel(), "level");
                RequestFieldValidator.notNull(logEntry.getMessage(), "message");
                RequestFieldValidator.notNull(logEntry.getCreatedTimestamp(), "createdTimestamp");
            });
        }
    }

}
