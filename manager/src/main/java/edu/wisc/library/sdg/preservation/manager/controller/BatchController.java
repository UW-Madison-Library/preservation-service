package edu.wisc.library.sdg.preservation.manager.controller;

import edu.wisc.library.sdg.preservation.common.exception.NotFoundException;
import edu.wisc.library.sdg.preservation.common.util.RequestFieldValidator;
import edu.wisc.library.sdg.preservation.manager.auth.PreservationAuth;
import edu.wisc.library.sdg.preservation.manager.client.model.ApproveIngestBatchRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.ApproveIngestObjectRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.BatchRetryIngestRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.BatchRetryReplicateRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.BatchSearchResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.DiffBatchObjectResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.Event;
import edu.wisc.library.sdg.preservation.manager.client.model.IngestBatchObject;
import edu.wisc.library.sdg.preservation.manager.client.model.IngestBatchSummary;
import edu.wisc.library.sdg.preservation.manager.client.model.ObjectFile;
import edu.wisc.library.sdg.preservation.manager.client.model.RejectIngestBatchRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.RejectIngestObjectRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.RetrieveBatchObjectFileResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.RetrieveBatchObjectFilesResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.RetrieveBatchObjectResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.RetrieveBatchObjectsResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.RetrieveBatchResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.RetrieveEventsResponse;
import edu.wisc.library.sdg.preservation.manager.controller.model.HasProblems;
import edu.wisc.library.sdg.preservation.manager.db.model.IngestBatchState;
import edu.wisc.library.sdg.preservation.manager.db.model.OrgAuthority;
import edu.wisc.library.sdg.preservation.manager.db.model.Permission;
import edu.wisc.library.sdg.preservation.manager.job.JobBroker;
import edu.wisc.library.sdg.preservation.manager.service.IngestService;
import edu.wisc.library.sdg.preservation.manager.service.PreservationService;
import edu.wisc.library.sdg.preservation.manager.util.ModelMapper;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@RestController
@RequestMapping("/api/batch")
@Timed(histogram = true)
public class BatchController {

    private static final Logger LOG = LoggerFactory.getLogger(BatchController.class);

    private final PreservationAuth preservationAuth;
    private final ModelMapper modelMapper;
    private final IngestService ingestService;
    private final PreservationService preservationService;
    private final JobBroker jobBroker;

    @Autowired
    public BatchController(PreservationAuth preservationAuth,
                           ModelMapper modelMapper,
                           IngestService ingestService,
                           PreservationService preservationService,
                           JobBroker jobBroker) {
        this.preservationAuth = preservationAuth;
        this.modelMapper = modelMapper;
        this.ingestService = ingestService;
        this.preservationService = preservationService;
        this.jobBroker = jobBroker;
    }

    @GetMapping("/search")
    public BatchSearchResponse searchBatches(
            @RequestParam(value = "orgName", required = false) String orgName,
            @RequestParam(value = "vault", required = false) List<String> vaults,
            @RequestParam(value = "state", required = false) List<IngestBatchState> states,
            @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page
    ) {
        // For now orgName is actually required. It may not be required for a sysadmin in the future
        RequestFieldValidator.notBlank(orgName, "orgName");

        return preservationAuth.apply(orgName, OrgAuthority.BASIC_OPS, () -> {
            var pageResult = ingestService.searchBatches(orgName, vaults, states, pageSize, page);
            return new BatchSearchResponse()
                    .page(pageResult.getNumber())
                    .totalPages(pageResult.getTotalPages())
                    .totalResults((int) pageResult.getTotalElements())
                    .batches(modelMapper.mapList(pageResult.getContent(), IngestBatchSummary.class));
        });
    }

    @GetMapping("/{ingestId}")
    public RetrieveBatchResponse retrieveBatch(@PathVariable("ingestId") Long ingestId) {
        var batch = ingestService.getIngestBatch(ingestId);

        return preservationAuth.apply(batch.getVault(), Permission.READ, () -> {
            return new RetrieveBatchResponse()
                    .ingestBatch(modelMapper.map(batch, IngestBatchSummary.class));
        });
    }

    @GetMapping("/{ingestId}/objects")
    public RetrieveBatchObjectsResponse retrieveBatchObjects(@PathVariable("ingestId") Long ingestId,
                                                             @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize,
                                                             @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                                             @RequestParam(value = "hasProblems", required = false) HasProblems hasProblems) {
        var batch = ingestService.getIngestBatch(ingestId);

        return preservationAuth.apply(batch.getVault(), Permission.READ, () -> {
            Boolean hasErrors = null;
            Boolean hasWarnings = null;

            if (hasProblems != null) {
                switch (hasProblems) {
                    case NONE -> {
                        hasErrors = false;
                        hasWarnings = false;
                    }
                    case WARNINGS -> {
                        hasErrors = false;
                        hasWarnings = true;
                    }
                    case ERRORS -> hasErrors = true;
                    default -> {
                    }
                }
            }

            var objects = ingestService.searchObjects(ingestId, hasErrors, hasWarnings, pageSize, page);
            var responseObjects = modelMapper.mapList(objects.getContent(), IngestBatchObject.class);
            responseObjects.forEach(obj -> {
                obj.setVault(batch.getVault());
            });

            return new RetrieveBatchObjectsResponse()
                    .page(objects.getNumber())
                    .totalPages(objects.getTotalPages())
                    .totalResults((int) objects.getTotalElements())
                    .batchObjects(responseObjects);
        });
    }

    @GetMapping("/object")
    public RetrieveBatchObjectResponse retrieveBatchObject(
            @RequestParam("ingestId") Long ingestId,
            @RequestParam("externalObjectId") String externalObjectId) {
        RequestFieldValidator.notNull(ingestId, "ingestId");
        RequestFieldValidator.notBlank(externalObjectId, "externalObjectId");

        var batch = ingestService.getIngestBatch(ingestId);

        return preservationAuth.apply(batch.getVault(), Permission.READ, () -> {
            var object = ingestService.getIngestObject(ingestId, externalObjectId);

            Integer version = null;
            try {
                version = preservationService.getObjectVersionByIngestId(
                        object.getIngestId(), object.getExternalObjectId()).getVersion();
            } catch (NotFoundException e) {
                // ignore -- object has not been ingested yet
            }

            var responseObject = modelMapper.map(object, IngestBatchObject.class)
                    .vault(batch.getVault())
                    .version(version);

            return new RetrieveBatchObjectResponse()
                    .batchObject(responseObject);
        });
    }

    @GetMapping("/object/files")
    public RetrieveBatchObjectFilesResponse retrieveBatchObjectFiles(
            @RequestParam("ingestId") Long ingestId,
            @RequestParam("externalObjectId") String externalObjectId) {
        RequestFieldValidator.notNull(ingestId, "ingestId");
        RequestFieldValidator.notBlank(externalObjectId, "externalObjectId");

        var batch = ingestService.getIngestBatch(ingestId);

        return preservationAuth.apply(batch.getVault(), Permission.READ, () -> {
            var files = ingestService.getAllObjectFileComposites(ingestId, externalObjectId);

            return new RetrieveBatchObjectFilesResponse()
                    .files(modelMapper.mapList(files, ObjectFile.class));
        });
    }

    @GetMapping("/object/file")
    public RetrieveBatchObjectFileResponse retrieveBatchObjectFile(
            @RequestParam("ingestId") Long ingestId,
            @RequestParam("externalObjectId") String externalObjectId,
            @RequestParam("filePath") String filePath) {
        RequestFieldValidator.notNull(ingestId, "ingestId");
        RequestFieldValidator.notBlank(externalObjectId, "externalObjectId");
        RequestFieldValidator.notBlank(filePath, "filePath");

        var batch = ingestService.getIngestBatch(ingestId);

        return preservationAuth.apply(batch.getVault(), Permission.READ, () -> {
            var file = ingestService.getObjectFileComposite(ingestId, externalObjectId, filePath);

            return new RetrieveBatchObjectFileResponse()
                    ._file(modelMapper.map(file, ObjectFile.class));
        });
    }

    @GetMapping("/object/diff")
    public DiffBatchObjectResponse diffBatchObject(
            @RequestParam("ingestId") Long ingestId,
            @RequestParam("externalObjectId") String externalObjectId) {
        RequestFieldValidator.notNull(ingestId, "ingestId");
        RequestFieldValidator.notBlank(externalObjectId, "externalObjectId");

        var batch = ingestService.getIngestBatch(ingestId);

        return preservationAuth.apply(batch.getVault(), Permission.READ, () -> {
            var diff = ingestService.diffObjectFiles(ingestId, batch.getVault(), externalObjectId);
            var response = modelMapper.map(diff, DiffBatchObjectResponse.class);
            response.setHasChanges(diff.hasChanges());
            return response;
        });
    }

    @PostMapping("/approve")
    public void approveBatch(
            Authentication authentication,
            @RequestBody ApproveIngestBatchRequest request) {

        // TODO this should probably take a message too

        RequestFieldValidator.notNull(request.getIngestId(), "ingestId");

        var timestamp = request.getReviewedTimestamp() != null ?
                request.getReviewedTimestamp() : OffsetDateTime.now(ZoneOffset.UTC);

        var batch = ingestService.getIngestBatch(request.getIngestId());

        preservationAuth.apply(batch.getVault(), Permission.WRITE, () -> {
            ingestService.approveBatch(authentication.getName(), request.getIngestId(), timestamp);
            jobBroker.checkPendingJobs();
        });
    }

    @PostMapping("/reject")
    public void rejectBatch(
            Authentication authentication,
            @RequestBody RejectIngestBatchRequest request) {

        // TODO this should probably take a message too

        RequestFieldValidator.notNull(request.getIngestId(), "ingestId");

        var batch = ingestService.getIngestBatch(request.getIngestId());

        preservationAuth.apply(batch.getVault(), Permission.WRITE, () -> {
            ingestService.rejectBatch(request.getIngestId(), authentication.getName());
            jobBroker.checkPendingJobs();
        });
    }

    @PostMapping("/retryIngest")
    public void retryBatch(@RequestBody BatchRetryIngestRequest request) {
        RequestFieldValidator.notNull(request.getIngestId(), "ingestId");

        preservationAuth.applyIfServiceAdmin(() -> {
            ingestService.retryBatchIngest(request.getIngestId());
            jobBroker.checkPendingJobs();
        });
    }

    @PostMapping("/retryReplicate")
    public void retryReplicate(@RequestBody BatchRetryReplicateRequest request) {
        RequestFieldValidator.notNull(request.getIngestId(), "ingestId");

        preservationAuth.applyIfServiceAdmin(() -> {
            ingestService.retryBatchReplicate(request.getIngestId());
            jobBroker.checkPendingJobs();
        });
    }

    @PostMapping("/object/approve")
    public void approveIngestObject(Authentication authentication,
                                    @RequestBody ApproveIngestObjectRequest request) {
        RequestFieldValidator.notNull(request.getIngestId(), "ingestId");
        RequestFieldValidator.notBlank(request.getExternalObjectId(), "externalObjectId");

        var batch = ingestService.getIngestBatch(request.getIngestId());

        preservationAuth.apply(batch.getVault(), Permission.WRITE, () -> {
            ingestService.approveObject(
                    request.getIngestId(),
                    request.getExternalObjectId(),
                    authentication.getName(),
                    request.getReviewedTimestamp());
        });

        jobBroker.checkPendingJobs();
    }

    @PostMapping("/object/reject")
    public void rejectIngestObject(Authentication authentication,
                                   @RequestBody RejectIngestObjectRequest request) {
        RequestFieldValidator.notNull(request.getIngestId(), "ingestId");
        RequestFieldValidator.notBlank(request.getExternalObjectId(), "externalObjectId");

        var batch = ingestService.getIngestBatch(request.getIngestId());

        preservationAuth.apply(batch.getVault(), Permission.WRITE, () -> {
            ingestService.rejectObject(
                    request.getIngestId(),
                    request.getExternalObjectId(),
                    authentication.getName());
        });

        jobBroker.checkPendingJobs();
    }

    @GetMapping("/object/event")
    public RetrieveEventsResponse retrieveIngestObjectEvents(
            @RequestParam("ingestId") Long ingestId,
            @RequestParam("externalObjectId") String externalObjectId) {
        var batch = ingestService.getIngestBatch(ingestId);

        return preservationAuth.apply(batch.getVault(), Permission.READ, () -> {
            var logs = ingestService.getAllIngestObjectEvents(ingestId, externalObjectId);

            return new RetrieveEventsResponse()
                    .events(modelMapper.mapList(logs, Event.class));
        });
    }

    @GetMapping("/{ingestId}/event")
    public RetrieveEventsResponse retrieveBatchEvents(@PathVariable("ingestId") Long ingestId) {
        var batch = ingestService.getIngestBatch(ingestId);

        return preservationAuth.apply(batch.getVault(), Permission.READ, () -> {
            var logs = ingestService.getAllBatchIngestEvents(ingestId);

            return new RetrieveEventsResponse()
                    .events(modelMapper.mapList(logs, Event.class));
        });
    }

}
