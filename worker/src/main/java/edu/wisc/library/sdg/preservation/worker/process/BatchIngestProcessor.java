package edu.wisc.library.sdg.preservation.worker.process;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.MoreFiles;
import edu.wisc.library.sdg.preservation.common.PreservationConstants;
import edu.wisc.library.sdg.preservation.common.exception.SafeRuntimeException;
import edu.wisc.library.sdg.preservation.common.model.PreservationMetadata;
import edu.wisc.library.sdg.preservation.common.util.ArgCheck;
import edu.wisc.library.sdg.preservation.common.util.UncheckedFiles;
import edu.wisc.library.sdg.preservation.common.util.UuidUtils;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.BatchCompleteIngestingRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.BatchObjectWithFiles;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.BatchStartIngestingRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.CreateObjectVersionRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.DeleteObjectVersionRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.EventOutcome;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.EventType;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.FinalizeObjectVersionRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.IngestBatch;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ObjectCompleteIngestingRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ObjectStartIngestingRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.Outcome;
import edu.wisc.library.sdg.preservation.worker.log.EventRecorder;
import edu.wisc.library.sdg.preservation.worker.util.ManagerRetrier;
import edu.wisc.library.sdg.preservation.worker.util.Safely;
import edu.wisc.library.sdg.preservation.worker.util.WorkDirectory;
import io.ocfl.api.OcflOption;
import io.ocfl.api.OcflRepository;
import io.ocfl.api.model.DigestAlgorithm;
import io.ocfl.api.model.ObjectVersionId;
import io.ocfl.api.model.VersionInfo;
import io.ocfl.api.model.VersionNum;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Component
public class BatchIngestProcessor implements BatchProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(BatchIngestProcessor.class);

    private final WorkDirectory workDir;
    private final ManagerInternalApi managerClient;
    private final OcflRepository localOcflRepo;
    private final EventRecorder eventRecorder;
    private final ObjectMapper objectMapper;
    private final int objectParallelism;

    @Autowired
    public BatchIngestProcessor(WorkDirectory workDir,
                                ManagerInternalApi managerClient,
                                @Qualifier("localOcflRepo") OcflRepository localOcflRepo,
                                EventRecorder eventRecorder,
                                ObjectMapper objectMapper,
                                @Value("${app.batch.object.parallelism}")
                                int objectParallelism) {
        this.workDir = workDir;
        this.managerClient = managerClient;
        this.localOcflRepo = localOcflRepo;
        this.eventRecorder = eventRecorder;
        this.objectMapper = objectMapper;
        this.objectParallelism = ArgCheck.condition(objectParallelism, objectParallelism > 0,
                "objectParallelism must be greater than 0");
    }

    @Override
    public void process(IngestBatch batch) {
        var ingestId = batch.getIngestId();

        LOG.info("Ingesting batch <{}>", ingestId);

        startBatchIngest(ingestId);

        var executor = Executors.newWorkStealingPool(objectParallelism);

        try {
            var objects = managerClient.retrieveBatchIngest(ingestId).getApprovedObjects();

            LOG.debug("Ingesting objects: {}", objects);

            var futures = new ArrayList<Future<Boolean>>(objects.size());

            for (var object : objects) {
                futures.add(executor.submit(() -> {
                    try {
                        return ingestObject(ingestId, batch.getVault(), object);
                    } catch (Exception e) {
                        LOG.error("Error ingesting object {} to {}",
                                object.getExternalObjectId(), batch.getVault(), e);
                        return true;
                    }
                }));
            }

            var hasFailures = futures.stream().map(future -> {
                try {
                    return future.get();
                } catch (Exception e) {
                    LOG.error("Failure getting result from ingest thread", e);
                    return true;
                }
            }).reduce(Boolean.FALSE, (l, r) -> l || r);

            executor.shutdown();

            if (hasFailures) {
                safeCompleteBatchIngest(ingestId, false);
            } else {
                safeCompleteBatchIngest(ingestId, true);
                cleanup(batch);
            }
        } catch (RuntimeException e) {
            safeCompleteBatchIngest(ingestId, false);
            throw new SafeRuntimeException(String.format("Failed to ingest batch <%s>", ingestId), e);
        }
    }

    private boolean ingestObject(Long ingestId, String vault, BatchObjectWithFiles object) {
        var hasFailures = false;
        startObjectIngest(object);

        var writeAttempted = new AtomicBoolean(false);
        var written = new AtomicBoolean(false);
        var rollbackAttempted = new AtomicBoolean(false);

        var internalObjectId = internalObjectId(object);

        if (hasNoChanges(internalObjectId, object)) {
            LOG.info("Object <{}> has no changes and will not be ingested", internalObjectId);
            markObjectNoChange(object);
            eventRecorder.recordEvent(EventType.WRITE_OBJ_LOCAL, ingestId, object.getExternalObjectId(), logger -> {
                logger.info("Object was not written because it contains no changes", internalObjectId);
                logger.outcome(EventOutcome.NOT_EXECUTED);
            });
            return hasFailures;
        }

        try {
            var response = ManagerRetrier.retry(() -> {
                return managerClient.createObjectVersion(new CreateObjectVersionRequest()
                        .ingestObjectId(object.getIngestObjectId())
                        .objectId(internalObjectId));
            });

            Safely.exec(() -> {
                writeAttempted.set(true);
                var ocflId = ingestObjectToOcfl(internalObjectId, object, vault, response.getVersion());
                written.set(true);

                Safely.exec(() -> {
                    ManagerRetrier.retry(() -> {
                        managerClient.finalizeObjectVersion(new FinalizeObjectVersionRequest()
                                .objectVersionId(response.getObjectVersionId())
                                .ingestObjectId(object.getIngestObjectId())
                                .persistenceVersion(ocflId.getVersionNum().toString()));
                    });
                }, () -> {
                    rollbackAttempted.set(true);
                    rollbackOcflVersion(ocflId);
                    written.set(false);
                }, () -> "Failed to rollback OCFL object");
            }, () -> {
                ManagerRetrier.retry(() -> {
                    managerClient.deleteObjectVersion(new DeleteObjectVersionRequest()
                            .objectVersionId(response.getObjectVersionId()));
                });
            }, () -> String.format("Failed to delete object version %s", response.getObjectVersionId()));

            // TODO should errors from these methods be added to the job logs?
            safeCompleteObjectIngest(object, true);
        } catch (RuntimeException e) {
            LOG.error("Failed to ingest object <{};{}>", ingestId, object.getExternalObjectId(), e);
            hasFailures = true;
            safeCompleteObjectIngest(object, false);
        }

        if (writeAttempted.get()) {
            if (written.get()) {
                eventRecorder.recordEvent(EventType.WRITE_OBJ_LOCAL, ingestId, object.getExternalObjectId(), logger -> {
                    logger.info("Written to OCFL object %s", internalObjectId);
                });
            } else if (!rollbackAttempted.get()) {
                eventRecorder.recordEvent(EventType.WRITE_OBJ_LOCAL, ingestId, object.getExternalObjectId(), logger -> {
                    logger.outcome(EventOutcome.FAILURE);
                });
            }
        }

        return hasFailures;
    }

    private ObjectVersionId ingestObjectToOcfl(String internalObjectId,
                                               BatchObjectWithFiles object,
                                               String vault,
                                               Integer preservationVersion) {
        var versionNum = object.getHeadPersistenceVersion() == null ? "v0" : object.getHeadPersistenceVersion();
        var ocflId = ObjectVersionId.version(internalObjectId, versionNum);

        var objectRoot = Paths.get(object.getObjectRootPath());
        var objectWip = workDir.batchObjectWipDirectory(object.getIngestId(), object.getExternalObjectId());

        writeSystemMeta(objectWip, vault, object.getExternalObjectId(), preservationVersion);
        writePremisFile(objectWip, internalObjectId, preservationVersion);

        var versionInfo = new VersionInfo()
                .setUser(object.getApproverName(), object.getApproverAddress())
                .setMessage(String.format("This version put in place by %s, through the preservation service, in batch %s",
                        object.getApproverName(), object.getIngestId()));

        return localOcflRepo.updateObject(ocflId, versionInfo, updater -> {
            updater.clearVersionState();

            object.getFiles().forEach(file -> {
                var filePath = file.getFilePath();
                var logicalPath = logicalPath(filePath);
                LOG.debug("Adding {} to OCFL object {} at path {}", filePath, ocflId.getObjectId(), logicalPath);
                // TODO copy vs move?
                updater.addPath(objectRoot.resolve(filePath), logicalPath, OcflOption.OVERWRITE);
                updater.addFileFixity(logicalPath, DigestAlgorithm.sha256, file.getSha256Digest());
            });

            try {
                // TODO is it possible that processing will generate files that we do not want to commit?
                var systemFiles = MoreFiles.listFiles(objectWip);
                if (systemFiles.size() > 0) {
                    LOG.debug("Adding files to system directory: {}", systemFiles);
                    updater.addPath(objectWip, PreservationConstants.OCFL_OBJECT_SYSTEM_DIR, OcflOption.OVERWRITE);
                }
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });
    }

    private void writeSystemMeta(Path wip, String vault, String externalObjectId, Integer version) {
        var metadataPath = wip.resolve(PreservationConstants.SYSTEM_METADATA_FILENAME);

        var meta = new PreservationMetadata()
                .setMetadataVersion(PreservationMetadata.Version.V1_0)
                .setVault(vault)
                .setExternalObjectId(externalObjectId)
                .setVersion(version);

        try {
            objectMapper.writeValue(metadataPath.toFile(), meta);
        } catch (IOException e) {
            throw new SafeRuntimeException(String.format("Failed to write metadata for object %s", externalObjectId), e);
        }
    }

    private void writePremisFile(Path wip, String internalObjectId, Integer version) {
        var versions = IntStream.range(1, version + 1).boxed().toList();
        var premisFile = ManagerRetrier.retry(() -> {
            return managerClient.getPremisDocument(internalObjectId, versions);
        });
        UncheckedFiles.move(premisFile.toPath(), wip.resolve(PreservationConstants.OBJECT_PREMIS_FILE), REPLACE_EXISTING);
    }

    private void rollbackOcflVersion(ObjectVersionId ocflId) {
        if (ocflId.getVersionNum().equals(VersionNum.V1)) {
            LOG.info("Purging OCFL object {}.", ocflId);
            localOcflRepo.purgeObject(ocflId.getObjectId());
        } else {
            var previousVersion = ObjectVersionId.version(ocflId.getObjectId(), ocflId.getVersionNum().previousVersionNum());
            LOG.info("Rolling OCFL object {} back to version {}", previousVersion.getObjectId(), previousVersion.getVersionNum());
            localOcflRepo.rollbackToVersion(ObjectVersionId.version(ocflId.getObjectId(), ocflId.getVersionNum().previousVersionNum()));
        }
    }

    private String internalObjectId(BatchObjectWithFiles object) {
        if (Strings.isBlank(object.getInternalObjectId())) {
            return UuidUtils.newWithPrefix();
        }
        return UuidUtils.withPrefix(object.getInternalObjectId());
    }

    private void startObjectIngest(BatchObjectWithFiles object) {
        ManagerRetrier.retry(() -> {
            managerClient.objectStartIngesting(new ObjectStartIngestingRequest()
                    .ingestId(object.getIngestId())
                    .externalObjectId(object.getExternalObjectId()));
        });
    }

    private void markObjectNoChange(BatchObjectWithFiles object) {
        ManagerRetrier.retry(() -> {
            managerClient.objectCompleteIngesting(new ObjectCompleteIngestingRequest()
                    .ingestId(object.getIngestId())
                    .externalObjectId(object.getExternalObjectId())
                    .outcome(ObjectCompleteIngestingRequest.OutcomeEnum.NO_CHANGE));
        });
    }

    private void completeObjectIngest(BatchObjectWithFiles object, boolean success) {
        ManagerRetrier.retry(() -> {
            managerClient.objectCompleteIngesting(new ObjectCompleteIngestingRequest()
                    .ingestId(object.getIngestId())
                    .externalObjectId(object.getExternalObjectId())
                    .outcome(success ? ObjectCompleteIngestingRequest.OutcomeEnum.SUCCESS
                            : ObjectCompleteIngestingRequest.OutcomeEnum.FAILURE));
        });
    }

    private void safeCompleteObjectIngest(BatchObjectWithFiles object, boolean success) {
        try {
            completeObjectIngest(object, success);
        } catch (RuntimeException e) {
            LOG.error("Failed to mark ingest complete for <{};{}>. Success: {}",
                    object.getIngestId(), object.getExternalObjectId(), success, e);
        }
    }

    private void startBatchIngest(Long ingestId) {
        managerClient.batchStartIngesting(new BatchStartIngestingRequest()
                .ingestId(ingestId));
    }

    private void completeBatchIngest(Long ingestId, boolean success) {
        ManagerRetrier.retry(() -> {
            managerClient.batchCompleteIngesting(new BatchCompleteIngestingRequest()
                    .ingestId(ingestId)
                    .outcome(success ? Outcome.SUCCESS : Outcome.FAILURE));
        });
    }

    private void safeCompleteBatchIngest(Long ingestId, boolean success) {
        try {
            completeBatchIngest(ingestId, success);
        } catch (RuntimeException e) {
            LOG.error("Failed to mark ingest complete for batch <{}>. Success: {}", ingestId, success, e);
        }
    }

    private void cleanup(IngestBatch batch) {
        try {
            eventRecorder.recordEvent(EventType.DELETE_BAG, batch.getIngestId(), null, eventLogger -> {
                if (Strings.isNotBlank(batch.getFilePath())) {
                    LOG.info("Deleting bag: {}", batch.getFilePath());
                    try {
                        Files.deleteIfExists(Paths.get(batch.getFilePath()));
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                }

                workDir.deleteBatchDirectory(batch.getIngestId());
            });
        } catch (Exception e) {
            LOG.error("Failed to cleanup batch <{}> files on disk. These files will need to be removed manually.",
                    batch.getIngestId(), e);
        }
    }

    private boolean hasNoChanges(String internalObjectId, BatchObjectWithFiles object) {
        if (!"v0".equals(object.getHeadPersistenceVersion()) && localOcflRepo.containsObject(internalObjectId)) {
            var existingFiles = localOcflRepo.describeVersion(ObjectVersionId.version(internalObjectId, object.getHeadPersistenceVersion()))
                    .getFileMap().entrySet().stream()
                    .filter(entry -> entry.getKey().startsWith(PreservationConstants.OCFL_OBJECT_OBJECT_DIR))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            for (var file : object.getFiles()) {
                var logicalPath = logicalPath(file.getFilePath());
                if (existingFiles.containsKey(logicalPath)) {
                    var existingDigest = existingFiles.remove(logicalPath).getFixity().get(DigestAlgorithm.sha256);
                    if (!file.getSha256Digest().equalsIgnoreCase(existingDigest)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }

            return existingFiles.isEmpty();
        } else {
            return false;
        }
    }

    private String logicalPath(String path) {
        return PreservationConstants.OCFL_OBJECT_OBJECT_DIR + "/" + path;
    }

}
