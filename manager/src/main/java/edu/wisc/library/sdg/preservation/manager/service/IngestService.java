package edu.wisc.library.sdg.preservation.manager.service;

import edu.wisc.library.sdg.preservation.common.exception.IllegalOperationException;
import edu.wisc.library.sdg.preservation.common.exception.NotFoundException;
import edu.wisc.library.sdg.preservation.common.exception.ValidationException;
import edu.wisc.library.sdg.preservation.common.util.ArgCheck;
import edu.wisc.library.sdg.preservation.common.util.Time;
import edu.wisc.library.sdg.preservation.manager.common.OperationContext;
import edu.wisc.library.sdg.preservation.manager.db.model.EventOutcome;
import edu.wisc.library.sdg.preservation.manager.db.model.EventType;
import edu.wisc.library.sdg.preservation.manager.db.model.IngestBatch;
import edu.wisc.library.sdg.preservation.manager.db.model.IngestBatchObject;
import edu.wisc.library.sdg.preservation.manager.db.model.IngestBatchObjectFile;
import edu.wisc.library.sdg.preservation.manager.db.model.IngestBatchObjectFileComposite;
import edu.wisc.library.sdg.preservation.manager.db.model.IngestBatchObjectFileEncoding;
import edu.wisc.library.sdg.preservation.manager.db.model.IngestBatchObjectFileEncodingComposite;
import edu.wisc.library.sdg.preservation.manager.db.model.IngestBatchObjectFileFormat;
import edu.wisc.library.sdg.preservation.manager.db.model.IngestBatchObjectFileFormatComposite;
import edu.wisc.library.sdg.preservation.manager.db.model.IngestBatchObjectFileValidity;
import edu.wisc.library.sdg.preservation.manager.db.model.IngestBatchObjectFileValidityComposite;
import edu.wisc.library.sdg.preservation.manager.db.model.IngestBatchState;
import edu.wisc.library.sdg.preservation.manager.db.model.IngestEvent;
import edu.wisc.library.sdg.preservation.manager.db.model.IngestObjectState;
import edu.wisc.library.sdg.preservation.manager.db.model.MetaSource;
import edu.wisc.library.sdg.preservation.manager.db.model.ObjectFile;
import edu.wisc.library.sdg.preservation.manager.db.model.PreservationObjectFileEncoding;
import edu.wisc.library.sdg.preservation.manager.db.model.PreservationObjectFileFormat;
import edu.wisc.library.sdg.preservation.manager.db.model.PreservationObjectFileValidity;
import edu.wisc.library.sdg.preservation.manager.db.model.PreservationObjectVersionFileComposite;
import edu.wisc.library.sdg.preservation.manager.db.repo.FileEncodingRepository;
import edu.wisc.library.sdg.preservation.manager.db.repo.FileFormatRepository;
import edu.wisc.library.sdg.preservation.manager.db.repo.IngestBatchObjectFileRepository;
import edu.wisc.library.sdg.preservation.manager.db.repo.IngestBatchObjectRepository;
import edu.wisc.library.sdg.preservation.manager.db.repo.IngestBatchRepository;
import edu.wisc.library.sdg.preservation.manager.db.repo.IngestEventRepository;
import edu.wisc.library.sdg.preservation.manager.db.repo.MetaSourceRepository;
import edu.wisc.library.sdg.preservation.manager.diff.ObjectDiff;
import edu.wisc.library.sdg.preservation.manager.diff.ObjectDiffer;
import edu.wisc.library.sdg.preservation.manager.util.Agent;
import io.micrometer.core.instrument.MeterRegistry;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class IngestService {

    private static final Logger LOG = LoggerFactory.getLogger(IngestService.class);

    private static final Set<String> ANALYSIS_STATES = Set.of(
            IngestBatchState.RECEIVED.toString(),
            IngestBatchState.ANALYZING.toString(),
            IngestBatchState.ANALYSIS_FAILED.toString()
    );

    private static final Set<IngestObjectState> REJECTED_STATES = Set.of(
            IngestObjectState.PENDING_REJECTION,
            IngestObjectState.REJECTING,
            IngestObjectState.REJECTED
    );

    private final IngestBatchRepository batchRepo;
    private final IngestBatchObjectRepository objectRepo;
    private final IngestBatchObjectFileRepository fileRepo;
    private final FileFormatRepository fileFormatRepo;
    private final FileEncodingRepository fileEncodingRepo;
    private final MetaSourceRepository metaSourceRepo;
    private final IngestEventRepository ingestEventRepo;

    private PreservationService preservationService;
    private JobService jobService;
    private final ObjectDiffer objectDiffer;
    private final MeterRegistry meterRegistry;

    @Autowired
    public IngestService(IngestBatchRepository batchRepo,
                         IngestBatchObjectRepository objectRepo,
                         IngestBatchObjectFileRepository fileRepo,
                         FileFormatRepository fileFormatRepo,
                         FileEncodingRepository fileEncodingRepo,
                         MetaSourceRepository metaSourceRepo,
                         IngestEventRepository ingestEventRepo,
                         ObjectDiffer objectDiffer,
                         MeterRegistry meterRegistry) {
        this.batchRepo = batchRepo;
        this.objectRepo = objectRepo;
        this.fileRepo = fileRepo;
        this.fileFormatRepo = fileFormatRepo;
        this.fileEncodingRepo = fileEncodingRepo;
        this.metaSourceRepo = metaSourceRepo;
        this.ingestEventRepo = ingestEventRepo;
        this.objectDiffer = objectDiffer;
        this.meterRegistry = meterRegistry;
    }

    // Setter injection to break circular dependency
    @Autowired
    public void setJobService(JobService jobService) {
        this.jobService = jobService;
    }

    // Setter injection to break circular dependency
    @Autowired
    public void setPreservationService(PreservationService preservationService) {
        this.preservationService = preservationService;
    }

    @Transactional
    public Long createIngestBatchForBag(OperationContext context, String bagPath, String originalFilename) {
        ArgCheck.notNull(context, "context");
        ArgCheck.notBlank(bagPath, "bagPath");

        var now = Time.now();

        var size = fileSize(bagPath);

        var ingestBatch = new IngestBatch()
                .setOrgName(context.getOrgName())
                .setVault(context.getVault())
                .setCreatedBy(context.getUsername())
                .setState(IngestBatchState.RECEIVED)
                .setOriginalFilename(originalFilename)
                .setFilePath(bagPath)
                .setFileSize(size)
                .setReceivedTimestamp(now)
                .setUpdatedTimestamp(now);

        LOG.debug("Creating new ingest batch: {}", ingestBatch);

        ingestBatch = batchRepo.save(ingestBatch);

        recordIngestEvent(new IngestEvent()
                .setIngestId(ingestBatch.getIngestId())
                .setType(EventType.RECEIVE_BAG)
                .setOutcome(EventOutcome.SUCCESS)
                .setUsername(ingestBatch.getCreatedBy())
                .setEventTimestamp(ingestBatch.getReceivedTimestamp())
                .info(String.format("Received SIP %s", ingestBatch.getOriginalFilename()), ingestBatch.getReceivedTimestamp()));

        jobService.createProcessBatchJob(ingestBatch);

        return ingestBatch.getIngestId();
    }

    public IngestBatch getIngestBatch(Long ingestId) {
        return batchRepo.findById(ingestId)
                .orElseThrow(() -> new NotFoundException(String.format("Ingest batch <%s> was not found.", ingestId)));
    }

    public IngestBatch getIngestBatchByFilePath(String filePath) {
        return batchRepo.findByFilePath(filePath)
                .orElseThrow(() -> new NotFoundException(String.format("Ingest batch with file path %s was not found.", filePath)));
    }

    /**
     * Search for batches that match the specified criteria.
     *
     * @param orgName the name of the organization to filter on
     * @param vaults the vaults to filter on
     * @param states the states to filter on
     * @param pageSize the max number of results to return
     * @param page the page to retrieve, 0 indexed
     * @return the batches that match the criteria
     */
    public Page<IngestBatch> searchBatches(String orgName,
                                           List<String> vaults,
                                           List<IngestBatchState> states,
                                           int pageSize,
                                           int page) {
        List<String> vaultsQuery = CollectionUtils.isEmpty(vaults) ? new ArrayList<>() : vaults;
        var hasVaults = !vaultsQuery.isEmpty();
        List<IngestBatchState> statesQuery = CollectionUtils.isEmpty(states) ? new ArrayList<>() : states;
        var hasStates = !statesQuery.isEmpty();

        var statesNum = statesQuery.stream()
                .map(IngestBatchState::asShort)
                .collect(Collectors.toList());

        // The sql requires that the list is not empty. These values are not actually used.
        // This is super hacky -- may want to just build the sql here and execute directly.
        if (!hasVaults) {
            vaultsQuery.add("true");
        }
        if (!hasStates) {
            statesNum.add((short) 0);
        }

        var pageable = Pageable.ofSize(pageSize).withPage(page);

        var results = batchRepo.search(
                orgName,
                hasVaults,
                vaultsQuery,
                hasStates,
                statesNum,
                pageSize,
                (int) pageable.getOffset());

        return PageableExecutionUtils.getPage(results, pageable, () -> {
            return batchRepo.searchCount(orgName, hasVaults, vaultsQuery, hasStates, statesNum);
        });
    }

    public IngestBatchObject getIngestObject(Long ingestId, String externalObjectId) {
        return objectRepo.findOptionalByIngestIdAndExternalObjectId(ingestId, externalObjectId)
                .orElseThrow(() -> new NotFoundException(String.format("Ingest batch object <%s;%s> was not found.",
                        ingestId, externalObjectId)));
    }

    public IngestBatchObject getIngestObject(Long ingestObjectId) {
        return objectRepo.findById(ingestObjectId)
                .orElseThrow(() -> new NotFoundException(String.format("Ingest batch object <%s> was not found.",
                        ingestObjectId)));
    }

    public IngestBatchObjectFile getIngestObjectFile(Long ingestFileId) {
        return fileRepo.findById(ingestFileId)
                .orElseThrow(() -> new NotFoundException(String.format("Ingest batch object file <%s> was not found.",
                        ingestFileId)));
    }

    public List<IngestBatchObject> getAllObjects(Long ingestId) {
        return objectRepo.findAllByIngestId(ingestId);
    }

    /**
     * Search for objects that match the specified criteria.
     *
     * @param ingestId the batch to filter on
     * @param hasAnalysisErrors filter on if the object has analysis errors
     * @param hasAnalysisWarnings filter on if the object has analysis warnings
     * @param pageSize the max number of results to return
     * @param page the page to retrieve, 0 indexed
     * @return the objects that match the criteria
     */
    public Page<IngestBatchObject> searchObjects(Long ingestId,
                                           Boolean hasAnalysisErrors,
                                           Boolean hasAnalysisWarnings,
                                           int pageSize,
                                           int page) {
        ArgCheck.notNull(ingestId, "ingestId");

        var pageable = Pageable.ofSize(pageSize).withPage(page);

        var results = objectRepo.search(
                ingestId,
                hasAnalysisErrors,
                hasAnalysisWarnings,
                pageSize,
                (int) pageable.getOffset());

        return PageableExecutionUtils.getPage(results, pageable, () -> {
            return objectRepo.searchCount(ingestId, hasAnalysisErrors, hasAnalysisWarnings);
        });
    }

    public List<IngestBatchObjectFile> getAllObjectFiles(Long ingestId, String externalObjectId) {
        return fileRepo.findAllByIngestIdAndExternalObjectId(ingestId, externalObjectId);
    }

    public IngestBatchObjectFile getObjectFile(Long ingestId, String externalObjectId, String filePath) {
        return fileRepo.findByIngestIdAndExternalObjectIdAndFilePath(ingestId, externalObjectId, filePath)
                .orElseThrow(() -> new NotFoundException(
                        String.format("File %s was not found on object %s in batch %s", filePath, externalObjectId, ingestId)));
    }

    public List<ObjectFile> getAllObjectFilesByIngestId(Long ingestId) {
        return fileRepo.findAllByIngestId(ingestId);
    }

    public List<IngestBatchObjectFileComposite> getAllObjectFileComposites(Long ingestId, String externalObjectId) {
        var files = getAllObjectFiles(ingestId, externalObjectId);
        return files.stream().map(this::createFileComposite).collect(Collectors.toList());
    }

    public IngestBatchObjectFileComposite getObjectFileComposite(Long ingestId,
                                                                 String externalObjectId,
                                                                 String filePath) {
        var file = getObjectFile(ingestId, externalObjectId, filePath);
        return createFileComposite(file);
    }

    @Transactional
    public void updateBatchStartAnalysis(Long ingestId) {
        updateBatchStateSimple(ingestId, IngestBatchState.ANALYZING);
    }

    @Transactional
    public void updateBatchFailAnalysis(Long ingestId) {
        updateBatchStateSimple(ingestId, IngestBatchState.ANALYSIS_FAILED);
    }

    @Transactional
    public void updateBatchCompleteAnalysis(Long ingestId) {
        updateBatchStateSimple(ingestId, IngestBatchState.PENDING_REVIEW);
    }

    @Transactional
    public void updateBatchStartRejecting(Long ingestId) {
        updateBatchStateAndAllObjects(ingestId, IngestBatchState.REJECTING);
    }

    @Transactional
    public void updateBatchCompleteRejecting(Long ingestId) {
        updateBatchStateAndAllObjects(ingestId, IngestBatchState.REJECTED);
    }

    @Transactional
    public void updateBatchStartIngesting(Long ingestId) {
        updateBatchStateSimple(ingestId, IngestBatchState.INGESTING);
    }

    @Transactional
    public void updateBatchFailIngestion(Long ingestId) {
        updateBatchStateSimple(ingestId, IngestBatchState.INGEST_FAILED);
    }

    @Transactional
    public void updateBatchCompleteIngestion(Long ingestId) {
        updateBatchStateSimple(ingestId, IngestBatchState.REPLICATING);
    }

    @Transactional
    public void updateBatchFailReplication(Long ingestId) {
        updateBatchStateSimple(ingestId, IngestBatchState.REPLICATION_FAILED);
    }

    @Transactional
    public void updateBatchMarkDeleted(Long ingestId) {
        try {
            updateBatchStateAndAllObjects(ingestId, IngestBatchState.DELETED);
        } catch (RuntimeException e) {
            // we care if this fails because the batch may not be in a terminal state
            LOG.error("Failed to transition batch {} to DELETED state.", ingestId);
        }
    }

    @Transactional
    public void updateBatchCompleteReplication(Long ingestId) {
        updateBatchStateSimple(ingestId, IngestBatchState.COMPLETE);

        recordEvent(new IngestEvent()
                .setIngestId(ingestId)
                .setType(EventType.COMPLETE_BATCH_INGEST)
                .setOutcome(EventOutcome.SUCCESS)
                .setAgent(Agent.PRESERVATION_SERVICE_VERSION)
                .setEventTimestamp(Time.now()));
    }

    /**
     * Rejects the batch and all of the objects that it contains. This includes previously approved objects.
     *
     * @param ingestId the id of the batch to reject
     * @param username the user who rejected it
     */
    @Transactional
    public void rejectBatch(Long ingestId, String username) {
        ArgCheck.notNull(ingestId, "ingestId");
        ArgCheck.notBlank(username, "username");

        var batch = getIngestBatch(ingestId);
        var originalState = batch.getState();
        var newState = IngestBatchState.PENDING_REJECTION;

        validateStateTransition(originalState, newState);

        var events = new ArrayList<IngestEvent>();
        var now = Time.now();

        batch.setReviewedBy(username);
        events.add(new IngestEvent()
                .setIngestId(ingestId)
                .setType(EventType.REVIEW_BATCH)
                .setOutcome(EventOutcome.REJECTED)
                .setUsername(username)
                .setEventTimestamp(now));

        // Also reject all objects in the batch
        var objects = objectRepo.findAllByIngestIdAndState(ingestId, originalState.toObjectState());

        objects.stream().filter(o -> o.getReviewedBy() == null)
                .forEach(o -> o.setReviewedBy(username));

        // When a batch is rejected, also reject every object that was previously approved
        var additional = objectRepo.findAllByIngestIdAndState(ingestId, IngestObjectState.PENDING_INGESTION);
        additional.forEach(o -> o.setReviewedBy(username));
        objects.addAll(additional);

        objects.forEach(object -> {
            events.add(new IngestEvent()
                    .setIngestId(ingestId)
                    .setExternalObjectId(object.getExternalObjectId())
                    .setType(EventType.REVIEW_OBJ)
                    .setOutcome(EventOutcome.REJECTED)
                    .setUsername(object.getReviewedBy())
                    .setEventTimestamp(now));
        });

        objects.forEach(object -> {
            object.setState(newState.toObjectState());
        });

        objectRepo.saveAll(objects);

        batchRepo.save(batch.setState(newState)
                .setUpdatedTimestamp(now));

        recordEvents(events);

        jobService.createProcessBatchJob(batch);
    }

    /**
     * Transitions a batch from PENDING_REVIEW to PENDING_INGESTION. All of the objects within
     * the batch that are PENDING_REVIEW are also transitioned to PENDING_INGESTION and the HEAD version of the
     * object at the time of approval is recorded. All of the object within the batch that were rejected are transitioned
     * from PENDING_REJECTION to REJECTED.
     *
     * @param username user who approved the transition
     * @param ingestId the id of the batch
     * @param reviewedTimestamp the timestamp the transition was approved
     */
    @Transactional
    public void approveBatch(String username, Long ingestId, OffsetDateTime reviewedTimestamp) {
        ArgCheck.notBlank(username, "username");
        ArgCheck.notNull(ingestId, "ingestId");
        ArgCheck.notNull(reviewedTimestamp, "reviewedTimestamp");

        var batch = getIngestBatch(ingestId);
        var state = batch.getState();

        validateStateTransition(state, IngestBatchState.PENDING_INGESTION);

        var approvedObjects = objectRepo.findAllByIngestIdAndState(ingestId, state.toObjectState());
        var rejectedObjects = objectRepo.findAllByIngestIdAndState(ingestId, IngestObjectState.PENDING_REJECTION);
        var events = new ArrayList<IngestEvent>();

        var now = Time.now();

        batchRepo.save(batch.setState(IngestBatchState.PENDING_INGESTION)
                .setReviewedBy(username)
                .setUpdatedTimestamp(now));

        events.add(new IngestEvent()
                .setIngestId(ingestId)
                .setType(EventType.REVIEW_BATCH)
                .setOutcome(EventOutcome.APPROVED)
                .setUsername(username)
                .setEventTimestamp(now));

        approvedObjects.forEach(object -> {
            validateObjectNotDeletedForIngest(batch.getVault(), object.getExternalObjectId());
            object.setState(IngestObjectState.PENDING_INGESTION)
                    .setReviewedBy(username);
            var headVersion = preservationService.getHeadObjectVersion(
                    batch.getVault(), object.getExternalObjectId(), reviewedTimestamp);
            LOG.debug("Found object <{};{}> HEAD at {}: {}",
                    batch.getVault(), object.getExternalObjectId(), reviewedTimestamp, headVersion);
            if (headVersion != null) {
                object.setHeadObjectVersionId(headVersion.getObjectVersionId());
            }

            events.add(new IngestEvent()
                    .setIngestId(ingestId)
                    .setExternalObjectId(object.getExternalObjectId())
                    .setType(EventType.REVIEW_OBJ)
                    .setOutcome(EventOutcome.APPROVED)
                    .setUsername(username)
                    .setEventTimestamp(now));
        });

        rejectedObjects.forEach(object -> {
            object.setState(IngestObjectState.REJECTED);

            events.add(new IngestEvent()
                    .setIngestId(ingestId)
                    .setExternalObjectId(object.getExternalObjectId())
                    .setType(EventType.REVIEW_OBJ)
                    .setOutcome(EventOutcome.REJECTED)
                    .setUsername(object.getReviewedBy())
                    .setEventTimestamp(now));
        });

        // Create events for objects were one-offed approved
        objectRepo.findAllByIngestIdAndState(ingestId, IngestObjectState.PENDING_INGESTION).forEach(object -> {
            validateObjectNotDeletedForIngest(batch.getVault(), object.getExternalObjectId());
            events.add(new IngestEvent()
                    .setIngestId(ingestId)
                    .setExternalObjectId(object.getExternalObjectId())
                    .setType(EventType.REVIEW_OBJ)
                    .setOutcome(EventOutcome.APPROVED)
                    .setUsername(object.getReviewedBy())
                    .setEventTimestamp(now));
        });

        objectRepo.saveAll(approvedObjects);
        objectRepo.saveAll(rejectedObjects);
        recordEvents(events);

        jobService.createProcessBatchJob(batch);
    }

    /**
     * Moves a batch from INGEST_FAILED back to PENDING_INGESTION. Any objects in the batch that are in an INGEST_FAILED
     * state are also transitioned back.
     *
     * @param ingestId the id of the batch to retry
     */
    @Transactional
    public void retryBatchIngest(Long ingestId) {
        retryBatch(ingestId, IngestBatchState.INGEST_FAILED, IngestBatchState.PENDING_INGESTION, IngestObjectState.PENDING_INGESTION);

        // Create the PROCESS_BATCH job in this method because retryBatch is also used by retryBatchReplicate
        // which is NOT a PROCESS_BATCH job.
        var batch = getIngestBatch(ingestId);
        jobService.createProcessBatchJob(batch);
    }

    /**
     * Moves a batch from REPLICATION_FAILED back to REPLICATING. Any objects in the batch that are in a REPLICATION_FAILED
     * state are also transitioned back.
     *
     * @param ingestId the id of the batch to retry
     */
    @Transactional
    public void retryBatchReplicate(Long ingestId) {
        retryBatch(ingestId, IngestBatchState.REPLICATION_FAILED, IngestBatchState.REPLICATING, IngestObjectState.INGESTED);
    }

    public IngestBatchObject registerBatchObject(Long ingestId, String externalObjectId, String objectRootPath) {
        ArgCheck.notNull(ingestId, "ingestId");
        ArgCheck.notBlank(externalObjectId, "externalObjectId");
        ArgCheck.notBlank(objectRootPath, "objectRootPath");

        var object = new IngestBatchObject()
                .setIngestId(ingestId)
                .setExternalObjectId(externalObjectId)
                .setObjectRootPath(objectRootPath)
                .setState(IngestObjectState.ANALYZING);

        return objectRepo.save(object);
    }

    /**
     * Registers a file to an ingest object. The id of the file is returned
     *
     * @param ingestId id of the ingest object
     * @param objectRelativePath path to the file to register
     * @return id of the newly registered file
     */
    @Transactional
    public Long registerFile(Long ingestId, String objectRelativePath) {
        ArgCheck.notNull(ingestId, "ingestId");
        ArgCheck.notBlank(objectRelativePath, "objectRelativePath");

        var file = fileRepo.save(new IngestBatchObjectFile()
                .setIngestObjectId(ingestId)
                .setFilePath(objectRelativePath));

        return file.getIngestFileId();
    }

    /**
     * Sets the file details for an ingest object file. Any existing details for the file are overwritten. If the object
     * file was previously preserved, and the preserved file has user attributed formats associated with it, then
     * the formats are applied to the ingest file.
     *
     * @param ingestFileId id of the ingest file
     * @param sha256Digest sha256 digest of the file
     * @param fileSize size of the file in bytes
     * @param formats formats associated with the file
     */
    @Transactional
    public void setFileDetails(Long ingestFileId,
                               String sha256Digest,
                               Long fileSize,
                               Set<IngestBatchObjectFileFormatComposite> formats,
                               Set<IngestBatchObjectFileEncodingComposite> encoding,
                               Set<IngestBatchObjectFileValidityComposite> validity) {
        ArgCheck.notNull(ingestFileId, "ingestFileId");
        ArgCheck.notBlank(sha256Digest, "sha256Digest");
        ArgCheck.notNull(fileSize, "fileSize");
        ArgCheck.notNull(formats, "formats");
        ArgCheck.notNull(encoding, "encoding");
        ArgCheck.notNull(validity, "validity");

        var file = getIngestObjectFile(ingestFileId);
        var object = getIngestObject(file.getIngestObjectId());
        var batch = getIngestBatch(object.getIngestId());

        file.setFileSize(fileSize);
        file.setSha256Digest(sha256Digest);

        file.setFormats(new HashSet<>());

        // copies any existing user associated types onto the file
        var userFormats = preservationService.getAllUserProvidedFileFormats(
                batch.getVault(), object.getExternalObjectId(), file.getSha256Digest());
        var allFormats = new HashSet<>(convertFileFormats(userFormats));

        var userEncodings = preservationService.getAllUserProvidedFileEncodings(
                batch.getVault(), object.getExternalObjectId(), file.getSha256Digest());
        var allEncodings = new HashSet<>(convertFileEncoding(userEncodings));

        var userValidity = preservationService.getAllUserProvidedFileValidity(
                batch.getVault(), object.getExternalObjectId(), file.getSha256Digest());
        var allValidities = new HashSet<>(convertFileValidity(userValidity));

        // Create the detected formats
        formats.forEach(format -> {
            var formatId = getOrCreateFileFormat(format);
            var sourceId = getOrCreateMetaSource(format.getSource());
            allFormats.add(new IngestBatchObjectFileFormat()
                    .setMetaSourceId(sourceId)
                    .setFileFormatId(formatId));
        });

        allFormats.forEach(format -> {
            format.setIngestFileId(file.getIngestFileId());
            format.setIngestFileFormatId(null);
        });

        // Create the detected encodings
        encoding.forEach(e -> {
            var encodingId = getOrCreateFileEncoding(e);
            var sourceId = getOrCreateMetaSource(e.getSource());
            allEncodings.add(new IngestBatchObjectFileEncoding()
                    .setMetaSourceId(sourceId)
                    .setFileEncodingId(encodingId));
        });

        allEncodings.forEach(e -> {
            e.setIngestFileId(file.getIngestFileId());
            e.setIngestFileEncodingId(null);
        });

        validity.forEach(v -> {
            var sourceId = getOrCreateMetaSource(v.getSource());
            allValidities.add(new IngestBatchObjectFileValidity()
                    .setWellFormed(v.getWellFormed())
                    .setValid(v.getValid())
                    .setMetaSourceId(sourceId));
        });

        allValidities.forEach(v -> {
            v.setIngestFileId(file.getIngestFileId());
            v.setIngestFileValidityId(null);
        });

        dedup(allFormats, entry -> entry.getMetaSourceId() + ":" + entry.getFileFormatId());
        dedup(allEncodings, entry -> entry.getMetaSourceId() + ":" + entry.getFileEncodingId());
        dedup(allValidities, entry -> entry.getMetaSourceId() + ":" + entry.getValid() + ":" + entry.getWellFormed());

        file.setFormats(allFormats);
        file.setEncoding(allEncodings);
        file.setValidity(allValidities);

        fileRepo.save(file);
    }

    @Transactional
    public void updateObjectStartIngest(Long ingestId, String externalObjectId) {
        updateObjectStateSimple(ingestId, externalObjectId, IngestObjectState.INGESTING);
    }

    /**
     * Successfully completes an object ingest, and immediately transitions to replicating. This is necessary because
     * replication is part of the ingest workflow, but the replication logic lies outside of it, which means that
     * a replication job itself will never interact with the ingest workflow directly.
     *
     * @param ingestId the id of the batch
     * @param externalObjectId the external id of the object in the batch
     */
    @Transactional
    public void updateObjectCompleteIngest(Long ingestId, String externalObjectId) {
        updateObjectState(ingestId, externalObjectId, IngestObjectState.INGESTED, object -> {
            // Transition immediately to replicating because there could be multiple replication jobs and we only care that one succeeds
            object.setState(IngestObjectState.REPLICATING);

            var objectVersion = preservationService.getObjectVersionByIngestId(object.getIngestId(), object.getExternalObjectId());
            var jobs = jobService.createReplicateAfterIngestJobs(objectVersion);

            if (jobs.isEmpty()) {
                // If no replication jobs were created, then it means that the version was replicated to all remotes outside
                // of the ingest workflow and there is nothing left to do here. However, this does mean that the ingest
                // replication even was probably not recorded
                object.setState(IngestObjectState.COMPLETE);
            }
        });
    }

    @Transactional
    public void updateObjectIngestNoChange(Long ingestId, String externalObjectId) {
        updateObjectStateSimple(ingestId, externalObjectId, IngestObjectState.NO_CHANGE);
    }

    @Transactional
    public void updateObjectIngestFailed(Long ingestId, String externalObjectId) {
        updateObjectStateSimple(ingestId, externalObjectId, IngestObjectState.INGEST_FAILED);
    }

    @Transactional
    public void updateObjectCompleteAnalysis(Long ingestId, String externalObjectId) {
        updateObjectStateSimple(ingestId, externalObjectId, IngestObjectState.PENDING_REVIEW);
    }

    @Transactional
    public void updateObjectFailAnalysis(Long ingestId, String externalObjectId) {
        updateObjectState(ingestId, externalObjectId, IngestObjectState.ANALYSIS_FAILED, object -> {
            object.setHasAnalysisErrors(true);
        });
    }

    @Transactional
    public void updateObjectCompleteReplication(Long ingestId, String externalObjectId) {
        updateObjectState(ingestId, externalObjectId, IngestObjectState.REPLICATED, object -> {
            // Replicated is currently the final state, so transition immediately to complete
            object.setState(IngestObjectState.COMPLETE);
        });
    }

    @Transactional
    public void updateObjectFailReplication(Long ingestId, String externalObjectId) {
        updateObjectStateSimple(ingestId, externalObjectId, IngestObjectState.REPLICATION_FAILED);
    }

    /**
     * Approves the object for ingestion
     *
     * @param ingestId the id of the batch the object is in
     * @param externalObjectId the object's external object id
     * @param username the user to associate the transition to, required for approving/rejecting
     * @param reviewedTimestamp the timestamp the object was reviewed, required for approving
     */
    @Transactional
    public void approveObject(Long ingestId,
                              String externalObjectId,
                              String username,
                              OffsetDateTime reviewedTimestamp) {
        updateObjectState(ingestId, externalObjectId, IngestObjectState.PENDING_INGESTION, object -> {
            object.setReviewedBy(username)
                    .setHeadObjectVersionId(null);
            var timestamp = reviewedTimestamp != null ? reviewedTimestamp : OffsetDateTime.now(ZoneOffset.UTC);

            var batch = getIngestBatch(ingestId);

            var headVersion = preservationService.getHeadObjectVersion(
                    batch.getVault(), object.getExternalObjectId(), timestamp);

            LOG.debug("Found object <{};{}> HEAD at {}: {}",
                    batch.getVault(), object.getExternalObjectId(), timestamp, headVersion);

            if (headVersion != null) {
                object.setHeadObjectVersionId(headVersion.getObjectVersionId());
            }
        });
    }

    /**
     * Rejects the object
     *
     * @param ingestId the id of the batch the object is in
     * @param externalObjectId the object's external object id
     * @param username the user to associate the transition to, required for approving/rejecting
     */
    @Transactional
    public void rejectObject(Long ingestId,
                             String externalObjectId,
                             String username) {
        updateObjectState(ingestId, externalObjectId, IngestObjectState.PENDING_REJECTION, object -> {
            object.setReviewedBy(username)
                    .setHeadObjectVersionId(null);
        });
    }

    @Transactional
    public void recordIngestEvent(IngestEvent event) {
        ArgCheck.notNull(event, "event");

        LOG.debug("Creating event: {}", event);

        recordEvent(event);

        IngestBatch batch = null;

        // writing to local storage is an ingest event and a preservation event, but should only be recorded as a
        // preservation event if the event was executed
        if (event.getType() == EventType.WRITE_OBJ_LOCAL
                && event.getOutcome() != EventOutcome.NOT_EXECUTED) {
            batch = getIngestBatch(event.getIngestId());

            try {
                var object = preservationService.getObject(batch.getVault(), event.getExternalObjectId());
                preservationService.recordEvent(event.toPreservationEvent()
                        .setObjectId(object.getObjectId()));
            } catch (NotFoundException e) {
                // Ignore. This just means that the object doesn't exist so we can't record an event on it
            }
        }

        // Update the flags to indicate analysis errors/warnings, but only when in an appropriate state
        if (event.getExternalObjectId() != null) {
            var object = getIngestObject(event.getIngestId(), event.getExternalObjectId());

            if (ANALYSIS_STATES.contains(object.getState().toString())) {
                if (event.getOutcome() == EventOutcome.FAILURE) {
                    object.setHasAnalysisErrors(true);
                } else if (event.getOutcome() == EventOutcome.SUCCESS_WITH_WARNINGS) {
                    object.setHasAnalysisWarnings(true);
                }
                objectRepo.save(object);
            }
        } else {
            if (batch == null) {
                batch = getIngestBatch(event.getIngestId());
            }

            if (ANALYSIS_STATES.contains(batch.getState().toString())) {
                if (event.getOutcome() == EventOutcome.FAILURE) {
                    batch.setHasAnalysisErrors(true);
                } else if (event.getOutcome() == EventOutcome.SUCCESS_WITH_WARNINGS) {
                    batch.setHasAnalysisWarnings(true);
                }
                batchRepo.save(batch.setUpdatedTimestamp(Time.now()));
            }
        }
    }

    public List<IngestEvent> getAllBatchIngestEvents(Long ingestId) {
        ArgCheck.notNull(ingestId, "ingestId");

        return ingestEventRepo.findAllByIngestIdAndExternalObjectIdIsNull(ingestId);
    }

    public List<IngestEvent> getAllIngestObjectEvents(Long ingestId, String externalObjectId) {
        ArgCheck.notNull(ingestId, "ingestId");
        ArgCheck.notNull(externalObjectId, "externalObjectId");

        return ingestEventRepo.findAllByIngestIdAndExternalObjectIdOrderByEventTimestamp(ingestId, externalObjectId);
    }

    public ObjectDiff diffObjectFiles(Long ingestId, String vault, String externalId) {
        ArgCheck.notNull(ingestId, "ingestId");
        ArgCheck.notBlank(vault, "vault");
        ArgCheck.notBlank(externalId, "externalId");

        var batchObject = getIngestObject(ingestId, externalId);

        if (ANALYSIS_STATES.contains(batchObject.getState().toString())) {
            throw new IllegalOperationException(String.format("Cannot diff object %s because it has not been analyzed.", externalId));
        }

        var newFiles = getAllObjectFileComposites(ingestId, externalId);
        List<PreservationObjectVersionFileComposite> currentFiles;

        if (batchObject.getState() == IngestObjectState.PENDING_REVIEW
                || REJECTED_STATES.contains(batchObject.getState())) {
            try {
                currentFiles = preservationService.getObjectVersionComposite(vault, externalId, null).getFiles();
            } catch (NotFoundException e) {
                currentFiles = new ArrayList<>();
            }
        } else if (batchObject.getHeadObjectVersionId() != null) {
            currentFiles = preservationService.getObjectVersionComposite(batchObject.getHeadObjectVersionId()).getFiles();
        } else {
            currentFiles = new ArrayList<>();
        }

        return objectDiffer.diffObject(newFiles, currentFiles);
    }

    /**
     * Counts the number of ingest batch objects in the specified state
     *
     * @param ingestId the id of the batch
     * @param state the state to look for
     * @return the number of objects in the state
     */
    public long countObjectsByIngestIdAndState(Long ingestId, IngestObjectState state) {
        return objectRepo.countAllByIngestIdAndState(ingestId, state);
    }

    /**
     * Returns a list of all of the ingest batch ids that are in the specified state.
     *
     * @param state the state to look for
     * @return list of ids in the specified state
     */
    public List<Long> getAllBatchesInState(IngestBatchState state) {
        return batchRepo.findAllBatchIdsInState(state);
    }

    private void recordEvent(IngestEvent event) {
        LOG.debug("Creating event: {}", event);

        incrementEventCounter(event);

        ingestEventRepo.save(event);
    }

    private void recordEvents(List<IngestEvent> events) {
        LOG.debug("Creating events: {}", events);

        events.forEach(this::incrementEventCounter);

        ingestEventRepo.saveAll(events);
    }

    /**
     * Transitions a batch to the specified state and executes no other logic. This should be used for simple state
     * transitions that have no other effects.
     *
     * @param ingestId the id of the batch
     * @param newState the new state
     */
    private void updateBatchStateSimple(Long ingestId, IngestBatchState newState) {
        ArgCheck.notNull(ingestId, "ingestId");

        var batch = getIngestBatch(ingestId);
        var originalState = batch.getState();

        validateStateTransition(originalState, newState);

        batchRepo.save(batch.setState(newState)
                .setUpdatedTimestamp(Time.now()));
    }

    /**
     * Transitions a batch to the specified state. Any object in the batch that was in the same original state as the
     * batch is also transitioned to the same new state.
     *
     * @param ingestId the id of the batch
     * @param newState the new state
     */
    private void updateBatchStateAndAllObjects(Long ingestId, IngestBatchState newState) {
        ArgCheck.notNull(ingestId, "ingestId");

        var batch = getIngestBatch(ingestId);
        var originalState = batch.getState();

        validateStateTransition(originalState, newState);

        var events = new ArrayList<IngestEvent>();
        var now = Time.now();

        var objects = objectRepo.findAllByIngestIdAndState(ingestId, originalState.toObjectState());

        objects.forEach(object -> {
            object.setState(newState.toObjectState());
        });

        objectRepo.saveAll(objects);

        batchRepo.save(batch.setState(newState)
                .setUpdatedTimestamp(now));

        recordEvents(events);
    }

    /**
     * Updates the state of the object to the specified state and does nothing else.
     *
     * @param ingestId the id of the batch
     * @param externalObjectId the external id of the object in the batch
     * @param newState the state to transition to
     */
    private void updateObjectStateSimple(Long ingestId,
                                         String externalObjectId,
                                         IngestObjectState newState) {
        updateObjectState(ingestId, externalObjectId, newState, null);
    }

    /**
     * Updates the state of the object to the specified state and does nothing else.
     *
     * @param ingestId the id of the batch
     * @param externalObjectId the external id of the object in the batch
     * @param modifier allows the object to be modified before save
     * @param newState the state to transition to
     */
    private void updateObjectState(Long ingestId,
                                   String externalObjectId,
                                   IngestObjectState newState,
                                   Consumer<IngestBatchObject> modifier) {
        ArgCheck.notNull(ingestId, "ingestId");
        ArgCheck.notBlank(externalObjectId, "externalObjectId");
        ArgCheck.notNull(newState, "newState");

        var object = getIngestObject(ingestId, externalObjectId);
        validateStateTransition(object.getState(), newState);

        object.setState(newState);

        if (modifier != null) {
            modifier.accept(object);
        }

        if (object.getState() == IngestObjectState.COMPLETE) {
            recordIngestEvent(new IngestEvent()
                    .setIngestId(ingestId)
                    .setExternalObjectId(object.getExternalObjectId())
                    .setType(EventType.COMPLETE_OBJ_INGEST)
                    .setOutcome(EventOutcome.SUCCESS)
                    .setAgent(Agent.PRESERVATION_SERVICE_VERSION)
                    .setEventTimestamp(Time.now()));
        }

        objectRepo.save(object);
    }

    private void retryBatch(Long ingestId,
                            IngestBatchState currentState,
                            IngestBatchState batchNewState,
                            IngestObjectState objectNewState) {
        ArgCheck.notNull(ingestId, "ingestId");
        ArgCheck.notNull(currentState, "currentState");
        ArgCheck.notNull(batchNewState, "batchNewState");
        ArgCheck.notNull(objectNewState, "objectNewState");

        var batch = getIngestBatch(ingestId);

        if (batch.getState() != currentState) {
            throw new ValidationException(String.format("Cannot transition from state %s to state %s.",
                    batch.getState(), batchNewState));
        }

        validateStateTransition(currentState, batchNewState);
        validateStateTransition(currentState.toObjectState(), objectNewState);

        var objects = objectRepo.findAllByIngestIdAndState(ingestId, currentState.toObjectState());

        objects.forEach(object -> {
            if (objectNewState == IngestObjectState.INGESTED) {
                // In this case we want the replication spawning logic to run again
                updateObjectCompleteIngest(ingestId, object.getExternalObjectId());
            } else {
                updateObjectStateSimple(ingestId, object.getExternalObjectId(), objectNewState);
            }
        });

        batchRepo.save(batch
                .setState(batchNewState)
                .setUpdatedTimestamp(Time.now()));
    }

    private List<IngestBatchObjectFileFormat> convertFileFormats(List<PreservationObjectFileFormat> formats) {
        return formats.stream().map(format -> {
            return new IngestBatchObjectFileFormat()
                    .setFileFormatId(format.getFileFormatId())
                    .setMetaSourceId(format.getMetaSourceId());
        }).collect(Collectors.toList());
    }

    private List<IngestBatchObjectFileEncoding> convertFileEncoding(List<PreservationObjectFileEncoding> encoding) {
        return encoding.stream().map(e -> {
            return new IngestBatchObjectFileEncoding()
                    .setFileEncodingId(e.getFileEncodingId())
                    .setMetaSourceId(e.getMetaSourceId());
        }).collect(Collectors.toList());
    }

    private List<IngestBatchObjectFileValidity> convertFileValidity(List<PreservationObjectFileValidity> validity) {
        return validity.stream().map(v -> {
            return new IngestBatchObjectFileValidity()
                    .setValid(v.getValid())
                    .setWellFormed(v.getWellFormed())
                    .setMetaSourceId(v.getMetaSourceId());
        }).collect(Collectors.toList());
    }

    private void validateStateTransition(IngestBatchState original, IngestBatchState updated) {
        if (!original.isTransitionAllowed(updated)) {
            throw new ValidationException(String.format("Cannot transition from state %s to state %s.", original, updated));
        }
    }

    private void validateStateTransition(IngestObjectState original, IngestObjectState updated) {
        if (!original.isTransitionAllowed(updated)) {
            throw new ValidationException(String.format("Cannot transition from state %s to state %s.", original, updated));
        }
    }

    private long fileSize(String path) {
        try {
            return Files.size(Paths.get(path));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private <T> void dedup(HashSet<T> list, Function<T, String> keyCreator) {
        var seen = new HashSet<String>();
        for (var iter = list.iterator(); iter.hasNext();) {
            var key = keyCreator.apply(iter.next());
            if (seen.contains(key)) {
                iter.remove();
            } else {
                seen.add(key);
            }
        }
    }

    private IngestBatchObjectFileComposite createFileComposite(IngestBatchObjectFile file) {
        var formatComposites = file.getFormats().stream().map(format -> {
            var fileFormat = fileFormatRepo.findById(format.getFileFormatId())
                    .orElseThrow(() -> new NotFoundException(String.format("File format %s was not found", format.getFileFormatId())));
            var source = getMetaSource(format.getMetaSourceId());
            return new IngestBatchObjectFileFormatComposite(format, fileFormat, source);
        }).collect(Collectors.toList());

        var encodingComposites = file.getEncoding().stream().map(ingestEncoding -> {
            var encoding = fileEncodingRepo.findById(ingestEncoding.getFileEncodingId())
                    .orElseThrow(() -> new NotFoundException(String.format("File encoding %s was not found", ingestEncoding.getFileEncodingId())));
            var source = getMetaSource(ingestEncoding.getMetaSourceId());
            return new IngestBatchObjectFileEncodingComposite(ingestEncoding, encoding, source);
        }).collect(Collectors.toList());

        var validityComposites = file.getValidity().stream().map(v -> {
            var source = getMetaSource(v.getMetaSourceId());
            return new IngestBatchObjectFileValidityComposite(v, source);
        }).collect(Collectors.toList());

        return new IngestBatchObjectFileComposite(file, formatComposites, encodingComposites, validityComposites);
    }

    private Long getOrCreateFileFormat(IngestBatchObjectFileFormatComposite ingestFormat) {
        var format = ingestFormat.getFormat().toLowerCase();

        return fileFormatRepo.findByRegistryAndFormat(ingestFormat.getFormatRegistry(), format).orElseGet(() -> {
            fileFormatRepo.insertIgnoreDuplicate(ingestFormat.getFormatRegistry(), format);
            return fileFormatRepo.findByRegistryAndFormat(ingestFormat.getFormatRegistry(), format)
                    .orElseThrow(() -> new NotFoundException(String.format("File format %s was not found", format)));
        }).getFileFormatId();
    }

    private Long getOrCreateFileEncoding(IngestBatchObjectFileEncodingComposite ingestEncoding) {
        var encoding = ingestEncoding.getEncoding().toUpperCase();

        return fileEncodingRepo.findByEncoding(encoding).orElseGet(() -> {
            fileEncodingRepo.insertIgnoreDuplicate(encoding);
            return fileEncodingRepo.findByEncoding(encoding)
                    .orElseThrow(() -> new NotFoundException(String.format("File encoding %s was not found", encoding)));
        }).getFileEncodingId();
    }

    private Long getOrCreateMetaSource(String source) {
        return metaSourceRepo.findBySource(source).orElseGet(() -> {
            metaSourceRepo.insertIgnoreDuplicate(source);
            return getMetaSource(source);
        }).getMetaSourceId();
    }

    private MetaSource getMetaSource(Long metaSourceId) {
        return metaSourceRepo.findById(metaSourceId)
                .orElseThrow(() -> new NotFoundException(String.format("Meta source %s was not found", metaSourceId)));
    }

    private MetaSource getMetaSource(String source) {
        return metaSourceRepo.findBySource(source)
                .orElseThrow(() -> new NotFoundException(String.format("Meta source %s was not found", source)));
    }

    private void validateObjectNotDeletedForIngest(String vault, String objectId) {
        if (preservationService.isObjectDeleted(vault, objectId)) {
            throw new ValidationException(String.format(
                    "Cannot ingest object %s because it is marked as deleted.", objectId));
        }
    }

    private void incrementEventCounter(IngestEvent event) {
        meterRegistry.counter("ingestEvent",
                        "type", event.getType().name(),
                        "outcome", event.getOutcome().name())
                .increment();
    }

}
