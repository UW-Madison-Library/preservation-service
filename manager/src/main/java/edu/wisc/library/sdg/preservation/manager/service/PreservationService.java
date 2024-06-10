package edu.wisc.library.sdg.preservation.manager.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.wisc.library.sdg.preservation.common.PreservationConstants;
import edu.wisc.library.sdg.preservation.common.exception.ConcurrentUpdateException;
import edu.wisc.library.sdg.preservation.common.exception.IllegalOperationException;
import edu.wisc.library.sdg.preservation.common.exception.NotFoundException;
import edu.wisc.library.sdg.preservation.common.exception.ValidationException;
import edu.wisc.library.sdg.preservation.common.util.ArgCheck;
import edu.wisc.library.sdg.preservation.common.util.Time;
import edu.wisc.library.sdg.preservation.manager.common.OperationContext;
import edu.wisc.library.sdg.preservation.manager.db.model.DataStore;
import edu.wisc.library.sdg.preservation.manager.db.model.EventOutcome;
import edu.wisc.library.sdg.preservation.manager.db.model.EventType;
import edu.wisc.library.sdg.preservation.manager.db.model.IngestBatch;
import edu.wisc.library.sdg.preservation.manager.db.model.IngestBatchObject;
import edu.wisc.library.sdg.preservation.manager.db.model.IngestBatchObjectFile;
import edu.wisc.library.sdg.preservation.manager.db.model.LogLevel;
import edu.wisc.library.sdg.preservation.manager.db.model.MetaSource;
import edu.wisc.library.sdg.preservation.manager.db.model.PreservationEvent;
import edu.wisc.library.sdg.preservation.manager.db.model.PreservationEventLog;
import edu.wisc.library.sdg.preservation.manager.db.model.PreservationObject;
import edu.wisc.library.sdg.preservation.manager.db.model.PreservationObjectFile;
import edu.wisc.library.sdg.preservation.manager.db.model.PreservationObjectFileEncoding;
import edu.wisc.library.sdg.preservation.manager.db.model.PreservationObjectFileEncodingComposite;
import edu.wisc.library.sdg.preservation.manager.db.model.PreservationObjectFileFormat;
import edu.wisc.library.sdg.preservation.manager.db.model.PreservationObjectFileFormatComposite;
import edu.wisc.library.sdg.preservation.manager.db.model.PreservationObjectFileValidity;
import edu.wisc.library.sdg.preservation.manager.db.model.PreservationObjectFileValidityComposite;
import edu.wisc.library.sdg.preservation.manager.db.model.PreservationObjectState;
import edu.wisc.library.sdg.preservation.manager.db.model.PreservationObjectVersion;
import edu.wisc.library.sdg.preservation.manager.db.model.PreservationObjectVersionComposite;
import edu.wisc.library.sdg.preservation.manager.db.model.PreservationObjectVersionFile;
import edu.wisc.library.sdg.preservation.manager.db.model.PreservationObjectVersionFileComposite;
import edu.wisc.library.sdg.preservation.manager.db.model.PreservationObjectVersionLocation;
import edu.wisc.library.sdg.preservation.manager.db.model.RetrieveRequest;
import edu.wisc.library.sdg.preservation.manager.db.model.RetrieveRequestJob;
import edu.wisc.library.sdg.preservation.manager.db.model.RetrieveRequestJobComposite;
import edu.wisc.library.sdg.preservation.manager.db.model.SimplePreservationObjectVersionFileComposite;
import edu.wisc.library.sdg.preservation.manager.db.model.StorageProblem;
import edu.wisc.library.sdg.preservation.manager.db.model.StorageProblemType;
import edu.wisc.library.sdg.preservation.manager.db.repo.FileEncodingRepository;
import edu.wisc.library.sdg.preservation.manager.db.repo.FileFormatRepository;
import edu.wisc.library.sdg.preservation.manager.db.repo.MetaSourceRepository;
import edu.wisc.library.sdg.preservation.manager.db.repo.PreservationEventRepository;
import edu.wisc.library.sdg.preservation.manager.db.repo.PreservationObjectFileEncodingRepository;
import edu.wisc.library.sdg.preservation.manager.db.repo.PreservationObjectFileFormatRepository;
import edu.wisc.library.sdg.preservation.manager.db.repo.PreservationObjectFileRepository;
import edu.wisc.library.sdg.preservation.manager.db.repo.PreservationObjectFileValidityRepository;
import edu.wisc.library.sdg.preservation.manager.db.repo.PreservationObjectRepository;
import edu.wisc.library.sdg.preservation.manager.db.repo.PreservationObjectVersionFileRepository;
import edu.wisc.library.sdg.preservation.manager.db.repo.PreservationObjectVersionLocationRepository;
import edu.wisc.library.sdg.preservation.manager.db.repo.PreservationObjectVersionRepository;
import edu.wisc.library.sdg.preservation.manager.db.repo.RetrieveRequestJobRepository;
import edu.wisc.library.sdg.preservation.manager.db.repo.RetrieveRequestRepository;
import edu.wisc.library.sdg.preservation.manager.db.repo.StorageProblemRepository;
import edu.wisc.library.sdg.preservation.manager.premis.PremisWriter;
import edu.wisc.library.sdg.preservation.manager.util.Agent;
import edu.wisc.library.sdg.preservation.manager.vault.VaultStatsUpdater;
import io.micrometer.core.instrument.MeterRegistry;
import io.ocfl.api.model.VersionNum;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class PreservationService {

    private static final Logger LOG = LoggerFactory.getLogger(PreservationService.class);

    private final PreservationObjectRepository objectRepo;
    private final PreservationObjectVersionRepository versionRepo;
    private final PreservationObjectFileRepository fileRepo;
    private final PreservationObjectVersionFileRepository versionFileRepo;
    private final PreservationObjectFileFormatRepository objectFileFormatRepo;
    private final PreservationObjectFileEncodingRepository objectFileEncodingRepo;
    private final PreservationObjectFileValidityRepository objectFileValidityRepo;
    private final FileFormatRepository fileFormatRepo;
    private final FileEncodingRepository fileEncodingRepo;
    private final MetaSourceRepository metaSourceRepo;
    private final PreservationObjectVersionLocationRepository versionLocationRepo;
    private final StorageProblemRepository storageProblemRepo;
    private final PreservationEventRepository eventRepo;
    private final RetrieveRequestRepository retrieveRequestRepo;
    private final RetrieveRequestJobRepository retrieveRequestJobRepo;
    private final VaultStatsUpdater vaultStatsUpdater;
    private final ObjectMapper objectMapper;
    private final MeterRegistry meterRegistry;

    private JobService jobService;
    private IngestService ingestService;

    @Autowired
    public PreservationService(PreservationObjectRepository objectRepo,
                               PreservationObjectVersionRepository versionRepo,
                               PreservationObjectFileRepository fileRepo,
                               PreservationObjectVersionFileRepository versionFileRepo,
                               PreservationObjectFileFormatRepository objectFileFormatRepo,
                               PreservationObjectFileEncodingRepository objectFileEncodingRepo,
                               PreservationObjectFileValidityRepository objectFileValidityRepo,
                               FileFormatRepository fileFormatRepo,
                               FileEncodingRepository fileEncodingRepo,
                               MetaSourceRepository metaSourceRepo,
                               PreservationObjectVersionLocationRepository versionLocationRepo,
                               StorageProblemRepository storageProblemRepo,
                               PreservationEventRepository eventRepo,
                               RetrieveRequestRepository retrieveRequestRepo,
                               RetrieveRequestJobRepository retrieveRequestJobRepo,
                               VaultStatsUpdater vaultStatsUpdater,
                               ObjectMapper objectMapper,
                               MeterRegistry meterRegistry) {
        this.objectRepo = objectRepo;
        this.versionRepo = versionRepo;
        this.fileRepo = fileRepo;
        this.versionFileRepo = versionFileRepo;
        this.objectFileFormatRepo = objectFileFormatRepo;
        this.objectFileEncodingRepo = objectFileEncodingRepo;
        this.objectFileValidityRepo = objectFileValidityRepo;
        this.fileFormatRepo = fileFormatRepo;
        this.fileEncodingRepo = fileEncodingRepo;
        this.metaSourceRepo = metaSourceRepo;
        this.versionLocationRepo = versionLocationRepo;
        this.storageProblemRepo = storageProblemRepo;
        this.eventRepo = eventRepo;
        this.retrieveRequestRepo = retrieveRequestRepo;
        this.retrieveRequestJobRepo = retrieveRequestJobRepo;
        this.vaultStatsUpdater = vaultStatsUpdater;
        this.objectMapper = objectMapper;
        this.meterRegistry = meterRegistry;
    }

    // Setter injection to break circular dependency
    @Autowired
    public void setJobService(JobService jobService) {
        this.jobService = jobService;
    }

    // Setter injection to break circular dependency
    @Autowired
    public void setIngestService(IngestService ingestService) {
        this.ingestService = ingestService;
    }

    /**
     * Returns the PreservationObjectVersion that was the HEAD version of the object at the specified timestamp, or null
     * if the object did not exist at that time.
     */
    public PreservationObjectVersion getHeadObjectVersion(String vault,
                                                          String externalObjectId,
                                                          OffsetDateTime timestamp) {
        ArgCheck.notBlank(vault, "vault");
        ArgCheck.notBlank(externalObjectId, "externalObjectId");
        ArgCheck.notNull(timestamp, "timestamp");

        return versionRepo.findHeadVersionAtTimestamp(vault, externalObjectId,
                timestamp.atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime())
                .orElse(null);
    }

    public PreservationObjectVersion getHeadObjectVersion(UUID objectId) {
        ArgCheck.notNull(objectId, "objectId");

        return versionRepo.findHeadVersionForObjectId(objectId).orElseThrow(() ->
                new NotFoundException(String.format("Preservation object <%s> was not found.", objectId)));
    }

    public PreservationObject getObject(String vault, String externalObjectId) {
        ArgCheck.notBlank(vault, "vault");
        ArgCheck.notBlank(externalObjectId, "externalObjectId");

        return objectRepo.findByVaultAndExternalObjectId(vault, externalObjectId)
                .orElseThrow(() -> new NotFoundException(String.format("Preservation object <%s> was not found in vault <%s>.",
                        externalObjectId, vault)));
    }

    public PreservationObject getObject(UUID objectId) {
        ArgCheck.notNull(objectId, "objectId");

        return objectRepo.findById(objectId)
                .orElseThrow(() -> new NotFoundException(String.format("Preservation object <%s> was not found.",
                        objectId)));
    }

    public PreservationObjectVersion getObjectVersion(Long objectVersionId) {
        ArgCheck.notNull(objectVersionId, "objectVersionId");

        return versionRepo.findById(objectVersionId)
                .orElseThrow(() -> new NotFoundException(String.format("Preservation object version <%s> was not found.", objectVersionId)));
    }

    public PreservationObjectVersion getObjectVersion(UUID objectId, Integer version) {
        ArgCheck.notNull(objectId, "objectId");
        ArgCheck.notNull(version, "version");

        return versionRepo.findByObjectIdAndVersion(objectId, version)
                .orElseThrow(() -> new NotFoundException(String.format("Preservation object <%s> version <%s> was not found.", objectId, version)));
    }

    public PreservationObjectVersion getObjectVersionByIngestId(Long ingestId, String externalObjectId) {
        ArgCheck.notNull(ingestId, "ingestId");
        ArgCheck.notBlank(externalObjectId, "externalObjectId");

        return versionRepo.findByIngestIdAndExternalObjectId(ingestId, externalObjectId)
                .orElseThrow(() -> new NotFoundException(String.format("Preservation object <%s> in batch <%s> was not found.", externalObjectId, ingestId)));
    }

    /**
     * Returns a composite view of the object at the specified version, including all files.
     *
     * @param vault the vault the object is in
     * @param externalObjectId the external id of the object
     * @param version the version to retrieve, null for the head version
     * @return object version composite
     */
    public PreservationObjectVersionComposite getObjectVersionComposite(
            String vault, String externalObjectId, Integer version) {
        ArgCheck.notBlank(vault, "vault");
        ArgCheck.notBlank(externalObjectId, "externalObjectId");

        PreservationObjectVersion objectVersion;

        if (version == null) {
            objectVersion = versionRepo.findHeadByVaultAndExternalObjectId(vault, externalObjectId)
                    .orElseThrow(() -> new NotFoundException(String.format("Preservation object <%s;%s> was not found.",
                            vault, externalObjectId)));
        } else {
            objectVersion = versionRepo.findByVaultAndExternalObjectIdAndVersion(vault, externalObjectId, version)
                    .orElseThrow(() -> new NotFoundException(String.format("Preservation object <%s;%s> version <%s> was not found.",
                            vault, externalObjectId, version)));
        }

        var object = objectRepo.findById(objectVersion.getObjectId()).get();

        return getObjectVersionComposite(object, objectVersion);
    }

    /**
     * Returns a composite view of the object at the specified version, including all files.
     *
     * @param objectId the id of the object
     * @param version the version to retrieve, null for the head version
     * @return object version composite
     */
    public PreservationObjectVersionComposite getObjectVersionComposite(
            UUID objectId, Integer version) {
        ArgCheck.notNull(objectId, "objectId");

        PreservationObjectVersion objectVersion;

        if (version == null) {
            objectVersion = versionRepo.findHeadVersionForObjectId(objectId)
                    .orElseThrow(() -> new NotFoundException(String.format("Preservation object <%s> version was not found.",
                            objectId)));
        } else {
            objectVersion = versionRepo.findByObjectIdAndVersion(objectId, version)
                    .orElseThrow(() -> new NotFoundException(String.format("Preservation object <%s> version <%s> was not found.",
                            objectId, version)));
        }

        var object = objectRepo.findById(objectId).get();

        return getObjectVersionComposite(object, objectVersion);
    }

    /**
     * Returns a composite view of the object at the specified version, including all files.
     *
     * @param objectVersionId the id of the object version
     * @return object version composite
     */
    public PreservationObjectVersionComposite getObjectVersionComposite(
            Long objectVersionId) {
        ArgCheck.notNull(objectVersionId, "objectVersionId");

        var objectVersion = versionRepo.findById(objectVersionId)
                .orElseThrow(() -> new NotFoundException(String.format("Preservation object version %s was not found.", objectVersionId)));
        var object = objectRepo.findById(objectVersion.getObjectId()).get();

        return getObjectVersionComposite(object, objectVersion);
    }

    public PreservationObjectVersionLocation getObjectVersionLocation(Long objectVersionLocationId) {
        return versionLocationRepo.findById(objectVersionLocationId)
                .orElseThrow(() -> new NotFoundException(String.format(
                        "PreservationObjectVersionLocation for id <%s> could not be found", objectVersionLocationId)));
    }

    @Transactional
    public PreservationObjectVersion createObjectVersion(UUID objectId,
                                                         IngestBatch batch,
                                                         IngestBatchObject batchObject,
                                                         List<IngestBatchObjectFile> batchFiles) {
        ArgCheck.notNull(objectId, "objectId");
        ArgCheck.notNull(batch, "batch");
        ArgCheck.notNull(batchObject, "batchObject");
        ArgCheck.notNull(batchFiles, "batchFiles");

        var object = getOrCreateObjectIfNotExists(objectId, batch.getVault(), batchObject.getExternalObjectId());
        var objectVersion = createNewObjectVersion(object, batch.getIngestId());
        ingestFiles(objectVersion, batchFiles);

        var event = new PreservationEvent()
                .setObjectId(objectId)
                .setType(EventType.UPDATE_OBJ_METADATA)
                .setOutcome(EventOutcome.SUCCESS)
                .setAgent(Agent.PRESERVATION_SERVICE_VERSION)
                .setEventTimestamp(Time.now());

        recordEvent(event);
        ingestService.recordIngestEvent(event.toIngestEvent()
                .setIngestId(batch.getIngestId())
                .setExternalObjectId(object.getExternalObjectId()));

        return objectVersion;
    }

    @Transactional
    public void deleteObjectVersion(Long objectVersionId) {
        ArgCheck.notNull(objectVersionId, "objectVersionId");

        var objectVersionOptional = versionRepo.findById(objectVersionId);

        if (objectVersionOptional.isEmpty()) {
            return;
        }

        var objectVersion = objectVersionOptional.get();
        var object = getObject(objectVersion.getObjectId());

        if (object.getHeadObjectVersionId() != null) {
            var headVersion = getObjectVersion(object.getHeadObjectVersionId());

            if (objectVersion.getVersion() <= headVersion.getVersion()) {
                throw new IllegalOperationException(
                        String.format("Cannot delete object version %s because its version number, %s, is less than or equal to the current HEAD version, %s",
                                objectVersionId, objectVersion.getVersion(), headVersion.getVersion()));
            }
        }

        versionFileRepo.deleteAllByObjectVersionId(objectVersionId);
        fileRepo.deleteAllUnused(object.getObjectId());
        versionRepo.deleteById(objectVersionId);

        if (object.getHeadObjectVersionId() == null) {
            // Must cleanup any events associated with the object prior to purging
            objectRepo.deleteById(object.getObjectId());
        }
    }

    @Transactional
    public PreservationObjectVersion finalizeObjectVersion(Long objectVersionId, String persistenceVersion) {
        ArgCheck.notNull(objectVersionId, "objectVersionId");
        ArgCheck.notNull(persistenceVersion, "persistenceVersion");

        var now = Time.now();
        var objectVersion = getObjectVersion(objectVersionId);
        var object = getObject(objectVersion.getObjectId());

        var type = object.getHeadObjectVersionId() != null ? EventType.UPDATE_OBJ : EventType.CREATE_OBJ;
        var event = new PreservationEvent()
                .setObjectId(object.getObjectId())
                .setType(type)
                .setOutcome(EventOutcome.SUCCESS)
                .setAgent(Agent.PRESERVATION_SERVICE_VERSION)
                .setEventTimestamp(now);

        try {
            if (object.getHeadObjectVersionId() != null) {
                var headVersion = getObjectVersion(object.getHeadObjectVersionId());

                if (!objectVersion.getVersion().equals(headVersion.getVersion() + 1)) {
                    throw new IllegalOperationException(
                            String.format("Cannot set object version %s as the HEAD version of object %s, because its version," +
                                            " %s, is not one greater than the current HEAD version, %s",
                                    objectVersionId, object.getObjectId(), objectVersion.getVersion(), headVersion.getVersion()));
                }
            } else if (!objectVersion.getVersion().equals(1)) {
                throw new IllegalOperationException(
                        String.format("Cannot set object version %s as the HEAD version of object %s, because its version," +
                                        " %s, is not equal to 1",
                                objectVersionId, object.getObjectId(), objectVersion.getVersion()));
            }

            objectVersion.setInitialPersistenceVersion(persistenceVersion)
                    .setPersistenceVersion(persistenceVersion)
                    .setUpdatedTimestamp(now);

            versionRepo.save(objectVersion);

            object.setHeadObjectVersionId(objectVersionId)
                    .setUpdatedTimestamp(now);

            objectRepo.save(object);

            event.info(String.format("Created version %s", objectVersion.getVersion()), now);

            updateVaultStatsForObjectVersion(object.getVault(), object.getObjectId(), persistenceVersion);
        } catch (RuntimeException e) {
            event.setOutcome(EventOutcome.FAILURE);
            event.error(String.format("Failed to created version %s", objectVersion.getVersion()), now);
            throw e;
        } finally {
            try {
                recordEvent(event);
                ingestService.recordIngestEvent(event.toIngestEvent()
                        .setIngestId(objectVersion.getIngestId())
                        .setExternalObjectId(object.getExternalObjectId()));
            } catch (RuntimeException e) {
                LOG.error("Failed to record event: {}", event, e);
            }
        }

        return objectVersion;
    }

    @Transactional
    public void markReplicatedToDataStore(UUID objectId,
                                          String persistenceVersion,
                                          DataStore dataStore,
                                          String dataStoreKey,
                                          String sha256Digest) {
        ArgCheck.notNull(objectId, "objectId");
        ArgCheck.notBlank(persistenceVersion, "persistenceVersion");
        ArgCheck.notNull(dataStore, "dataStore");
        ArgCheck.notBlank(dataStoreKey, "dataStoreKey");
        ArgCheck.notBlank(sha256Digest, "sha256Digest");

        var location = versionLocationRepo.findByObjectIdAndPersistenceVersionAndDataStore(
                objectId, persistenceVersion, dataStore).orElseGet(PreservationObjectVersionLocation::new);

        versionLocationRepo.save(location
                .setObjectId(objectId)
                .setPersistenceVersion(persistenceVersion)
                .setDataStore(dataStore)
                .setDataStoreKey(dataStoreKey)
                .setSha256Digest(sha256Digest)
                .setWrittenTimestamp(Time.now())
                .setLastCheckTimestamp(null));
    }

    public List<PreservationObjectFileFormat> getAllUserProvidedFileFormats(String vault,
                                                                            String externalObjectId,
                                                                            String sha256Digest) {
        return getMetaSourceId(PreservationConstants.USER_SOURCE).map(source -> {
            return objectFileFormatRepo.findAllByVaultAndExternalObjectIdAndSha256Digest(
                    vault, externalObjectId, sha256Digest, source);
        }).orElse(Collections.emptyList());
    }

    public List<PreservationObjectFileEncoding> getAllUserProvidedFileEncodings(String vault,
                                                                                String externalObjectId,
                                                                                String sha256Digest) {
        return getMetaSourceId(PreservationConstants.USER_SOURCE).map(source -> {
            return objectFileEncodingRepo.findAllByVaultAndExternalObjectIdAndSha256Digest(
                    vault, externalObjectId, sha256Digest, source);
        }).orElse(Collections.emptyList());
    }

    public List<PreservationObjectFileValidity> getAllUserProvidedFileValidity(String vault,
                                                                               String externalObjectId,
                                                                               String sha256Digest) {
        return getMetaSourceId(PreservationConstants.USER_SOURCE).map(source -> {
            return objectFileValidityRepo.findAllByVaultAndExternalObjectIdAndSha256Digest(
                    vault, externalObjectId, sha256Digest, source);
        }).orElse(Collections.emptyList());
    }

    public UUID getInternalObjectId(String vault, String externalObjectId) {
        ArgCheck.notBlank(vault, "vault");
        ArgCheck.notBlank(externalObjectId, "externalObjectId");

        var object = versionRepo.findHeadByVaultAndExternalObjectId(vault, externalObjectId)
                .orElseThrow(() -> new NotFoundException(String.format("Preservation object <%s;%s> version was not found.",
                        vault, externalObjectId)));

        return object.getObjectId();
    }

    public Stream<PreservationObject> getAllObjectsInVault(String vault) {
        ArgCheck.notBlank(vault, "vault");
        return objectRepo.findAllByVault(vault);
    }

    public Long calculateObjectSize(Long objectVersionId) {
        ArgCheck.notNull(objectVersionId, "objectVersionId");

        return versionRepo.calculateObjectSize(objectVersionId);
    }

    public String getOrgNameByObjectVersionId(Long objectVersionId) {
        ArgCheck.notNull(objectVersionId, "objectVersionId");

        return versionRepo.lookupOrgNameByObjectVersionId(objectVersionId).orElseThrow(() ->
                new NotFoundException(String.format("Object version %s could not be found", objectVersionId)));
    }

    public String getOrgNameByObjectId(UUID objectId) {
        ArgCheck.notNull(objectId, "objectId");

        return objectRepo.lookupOrgNameByObjectId(objectId).orElseThrow(() -> {
            return new NotFoundException(String.format("Could not find the org name associated with object <%s>", objectId));
        });
    }

    public void createPremisXml(UUID objectId, List<Integer> versionNumbers, OutputStream output) {
        ArgCheck.notNull(objectId, "objectId");
        ArgCheck.notNull(versionNumbers, "versionNumbers");
        ArgCheck.notNull(output, "output");

        var object = getObject(objectId);

        versionNumbers.sort(Comparator.reverseOrder());

        try (var writer = new PremisWriter(output)) {
            writer.start();

            var preservationEvents = getObjectEvents(object.getVault(), object.getExternalObjectId());

            writer.addObject(object, versionNumbers, preservationEvents);

            versionNumbers.forEach(versionNumber -> {
                var composite = getObjectVersionComposite(object.getVault(),
                        object.getExternalObjectId(), versionNumber);

                var ingestEvents = ingestService.getAllIngestObjectEvents(
                        composite.getIngestId(), object.getExternalObjectId());

                writer.addVersion(composite, ingestEvents);
            });

            writer.finish();
        }
    }

    /**
     * Begins the process to restore the local copy of an object version from a remote source.
     * The actual restoration work will happen asynchronously.
     *
     * @param objectId the internal id of the object to restore
     * @param versions the persistence versions of the object to restore, an empty/null list means all versions
     * @param source the datastore to restore the object version from
     * @return job id
     */
    @Transactional
    public Long restoreObject(UUID objectId, List<String> versions, DataStore source) {
        ArgCheck.notNull(objectId, "objectId");
        ArgCheck.notNull(source, "source");

        var locations = resolveVersionLocations(objectId, versions, source);

        var jobIds = locations.stream().map(location -> {
            return jobService.createRestoreJob(location);
        }).toList();

        return jobService.createFinalizeRestoreJob(objectId, jobIds);
    }

    /**
     * Begins the process to validate remote copies of an object. The actual restoration work will happen asynchronously.
     *
     * @param objectId the internal id of the object to validate
     * @param versions the persistence versions of the object to validate, an empty/null list means all versions
     * @param dataStore the remote datastore the object is located in
     * @return validation job ids
     */
    @Transactional
    public List<Long> validateObjectRemote(UUID objectId, List<String> versions, DataStore dataStore) {
        ArgCheck.notNull(objectId, "objectId");
        ArgCheck.notNull(dataStore, "dataStore");

        var locations = resolveVersionLocations(objectId, versions, dataStore);

        var jobIds = locations.stream().map(location -> {
            return jobService.createValidateRemoteJob(location, false);
        }).toList();

        return jobIds;
    }

    /**
     * Begins the process to replicate an object to a remote. The actual restoration work will happen asynchronously.
     *
     * @param objectId the internal id of the object to replicate
     * @param versions the persistence versions of the object to replicate, an empty/null list means all versions
     * @param destination the remote datastore to replicate to
     * @return replicate job ids
     */
    @Transactional
    public List<Long> replicateObject(UUID objectId, List<String> versions, DataStore destination) {
        ArgCheck.notNull(objectId, "objectId");
        ArgCheck.notNull(destination, "destination");

        var head = getHeadObjectVersion(objectId);
        var headPersistenceVersion = VersionNum.fromString(head.getPersistenceVersion());
        List<String> resolvedVersions;

        if (CollectionUtils.isEmpty(versions)) {
            resolvedVersions = new ArrayList<>((int) headPersistenceVersion.getVersionNum());
            var current = VersionNum.fromInt(1);
            while (current.compareTo(headPersistenceVersion) <= 0) {
                resolvedVersions.add(current.toString());
                current = current.nextVersionNum();
            }
        } else {
            versions.forEach(version -> {
                var num = VersionNum.fromString(version);
                if (num.compareTo(headPersistenceVersion) > 0) {
                    throw new NotFoundException(String.format(
                            "Object %s does not contain persistence version %s.", objectId, version));
                }
            });
            resolvedVersions = versions;
        }

        var jobIds = resolvedVersions.stream().map(version -> {
            return jobService.createReplicateJob(objectId, version, destination);
        }).toList();

        return jobIds;
    }

    /**
     * Deletes all current problems associated with the object, including versions, for the specified datastore,
     * and adds a new problem. If the problem is NONE, then nothing is added.
     *
     * @param objectId the internal id of the object
     * @param dataStore the datastore where the problem is
     * @param problemType the type of problem
     */
    @Transactional
    public void setObjectStorageProblem(UUID objectId, DataStore dataStore, StorageProblemType problemType) {
        ArgCheck.notNull(objectId, "objectId");
        ArgCheck.notNull(dataStore, "dataStore");
        ArgCheck.notNull(problemType, "problemType");

        storageProblemRepo.deleteAllByObjectIdAndDatastore(objectId, dataStore);

        if (problemType != StorageProblemType.NONE) {
            LOG.info("Adding storage problem object <{}> datastore <{}> problem <{}>",
                    objectId, dataStore, problemType);

            storageProblemRepo.save(new StorageProblem()
                    .setObjectId(objectId)
                    .setDataStore(dataStore)
                    .setProblem(problemType)
                    .setReportedTimestamp(Time.now()));
        }
    }

    /**
     * Deletes all current problems associated with the object and version for the specified datastore,
     * and adds a new problem. If the problem is NONE, then nothing is added.
     *
     * @param objectId the internal id of the object
     * @param persistenceVersion the version the problem is with
     * @param dataStore the datastore where the problem is
     * @param problemType the type of problem
     */
    @Transactional
    public void setObjectVersionStorageProblem(UUID objectId,
                                               String persistenceVersion,
                                               DataStore dataStore,
                                               StorageProblemType problemType) {
        ArgCheck.notNull(objectId, "objectId");
        ArgCheck.notBlank(persistenceVersion, "persistenceVersion");
        ArgCheck.notNull(dataStore, "dataStore");
        ArgCheck.notNull(problemType, "problemType");

        storageProblemRepo.deleteAllByObjectIdAndPersistenceVersionAndDatastore(objectId, persistenceVersion, dataStore);

        if (problemType != StorageProblemType.NONE) {
            LOG.info("Adding storage problem object <{}> persistence version <{}> datastore <{}> problem <{}>",
                    objectId, persistenceVersion, dataStore, problemType);

            storageProblemRepo.save(new StorageProblem()
                    .setObjectId(objectId)
                    .setPersistenceVersion(persistenceVersion)
                    .setDataStore(dataStore)
                    .setProblem(problemType)
                    .setReportedTimestamp(Time.now()));
        }
    }

    /**
     * Returns a list of all of the storage problems associated to the specified object
     *
     * @param objectId the internal id of the object
     * @return the list of problems
     */
    public List<StorageProblem> getObjectStorageProblems(UUID objectId) {
        ArgCheck.notNull(objectId, "objectId");

        return storageProblemRepo.findAllByObjectId(objectId);
    }

    public List<PreservationEvent> getObjectEvents(String vault, String externalObjectId) {
        ArgCheck.notBlank(vault, "vault");
        ArgCheck.notBlank(externalObjectId, "externalObjectId");

        return eventRepo.findAllByExternalObjectId(vault, externalObjectId);
    }

    public void recordEvent(PreservationEvent event) {
        LOG.debug("Creating event: {}", event);

        incrementEventCounter(event);

        eventRepo.save(event);
    }

    public void recordEvents(List<PreservationEvent> events) {
        LOG.debug("Creating events: {}", events);

        events.forEach(this::incrementEventCounter);

        eventRepo.saveAll(events);
    }

    @Transactional
    public void recordPrepareDipEvents(Long jobId, List<Long> objectVersionIds) {
        ArgCheck.notNull(jobId, "jobId");
        ArgCheck.notNull(objectVersionIds, "objectVersionIds");

        var now = Time.now();
        var dipName = String.format(PreservationConstants.DIP_BAG_ZIP_TMPL, jobId);
        var objectMap = new HashMap<UUID, List<Integer>>();

        objectVersionIds.forEach(objectVersionId -> {
            var objectVersion = getObjectVersion(objectVersionId);
            var versions = objectMap.computeIfAbsent(objectVersion.getObjectId(), k -> new ArrayList<>());
            versions.add(objectVersion.getVersion());
        });

        var events = new ArrayList<PreservationEvent>(objectMap.size());

        objectMap.forEach((id, versions) -> {
            Collections.sort(versions);
            events.add(new PreservationEvent()
                    .setType(EventType.PREPARE_DIP)
                    .setObjectId(id)
                    .setOutcome(EventOutcome.SUCCESS)
                    .setAgent(Agent.PRESERVATION_SERVICE_VERSION)
                    .setEventTimestamp(now)
                    .setLogs(Set.of(new PreservationEventLog()
                            .setLevel(LogLevel.INFO)
                            .setCreatedTimestamp(now)
                            .setMessage(String.format("Created DIP %s containing versions %s", dipName, versions)))));
        });

        recordEvents(events);
    }

    /**
     * Returns all of the files that were introduced into the object in the specified persistence version. Introduced
     * means that it was the first time a file with the given hash was present in the object, and it does not include
     * files that were added to the object that share a hash with a file from a prior version.
     *
     * If the specified persistence version is not an initial version of a preservation version, then the version
     * is either invalid, in the case that it's later than the current head persistence version, or does not contain
     * any object content changes, in the case that preservation object metadata was updated without creating a new
     * preservation version.
     *
     * @param objectId the internal id of the object
     * @param persistenceVersion the OCFL version
     * @return list of all of the files that were introduced into the object at the specified version
     */
    public List<SimplePreservationObjectVersionFileComposite> getNewInVersionFiles(UUID objectId, String persistenceVersion) {
        ArgCheck.notNull(objectId, "objectId");
        ArgCheck.notBlank(persistenceVersion, "persistenceVersion");

        var requestedVersion = VersionNum.fromString(persistenceVersion);

        var files = versionFileRepo.findAllNewInVersion(objectId, persistenceVersion);

        if (!files.isEmpty()) {
            // Filter out files that were not introduced in the version
            for (var it = files.iterator(); it.hasNext(); ) {
                var file = it.next();
                var initialVersion = VersionNum.fromString(versionFileRepo.findEarliestPersistenceVersion(file.getObjectFileId()));
                if (!requestedVersion.equals(initialVersion)) {
                    it.remove();
                }
            }
        }

        return files;
    }

    /**
     * Returns a list of object ids for all of the objects that are due for a deep validation. That is objects with
     * last check timestamps that are before the specified timestamp.
     *
     * @param lastChecked the timestamp from which to select all objects with older last check timestamps
     * @return list of object ids
     */
    public List<UUID> getObjectsNeedingDeepValidation(LocalDateTime lastChecked) {
        return objectRepo.findAllNeedingDeepValidation(lastChecked);
    }

    /**
     * Returns a list of object ids for all of the objects that are due for a shallow validation. That is objects with
     * last check timestamps that are before the specified timestamp.
     *
     * @param lastChecked the timestamp from which to select all objects with older last check timestamps
     * @return list of object ids
     */
    public List<UUID> getObjectsNeedingShallowValidation(LocalDateTime lastChecked) {
        return objectRepo.findAllNeedingShallowValidation(lastChecked);
    }

    /**
     * Returns a list of locations in a given datastore that are due for validation. That is locations
     * with last check timestamps that are before the specified timestamp.
     *
     * @param dataStore the datastore to filter on
     * @param lastChecked the timestamp from which to select all locations with older last check timestamps
     * @return list of locations
     */
    public List<PreservationObjectVersionLocation> getRemoteLocationsNeedingValidation(DataStore dataStore,
                                                                                       LocalDateTime lastChecked) {
        ArgCheck.notNull(dataStore, "dataStore");
        ArgCheck.notNull(lastChecked, "lastChecked");
        return versionLocationRepo.findAllNeedingValidation(dataStore, lastChecked);
    }

    public void markObjectShallowValidation(UUID objectId) {
        var object = getObject(objectId);
        object.setLastShallowCheckTimestamp(Time.now());
        objectRepo.save(object);
    }

    public void markObjectDeepValidation(UUID objectId) {
        var object = getObject(objectId);
        object.setLastDeepCheckTimestamp(Time.now());
        objectRepo.save(object);
    }

    public void markRemoteVersionValidation(Long objectVersionLocationId) {
        var location = getStorageLocation(objectVersionLocationId);
        location.setLastCheckTimestamp(Time.now());
        versionLocationRepo.save(location);
    }

    public List<String> getExternalObjectIdsWithStorageProblems(String vault) {
        return objectRepo.findAllExternalIdsByVaultWithStorageProblems(vault);
    }

    public List<PreservationObjectVersionLocation> getStorageLocations(UUID objectId, String persistenceVersion) {
        ArgCheck.notNull(objectId, "objectId");
        ArgCheck.notBlank(persistenceVersion, "persistenceVersion");

        return versionLocationRepo.findAllByObjectIdAndPersistenceVersion(objectId, persistenceVersion);
    }

    public PreservationObjectVersionLocation getStorageLocation(UUID objectId,
                                                                String persistenceVersion,
                                                                DataStore dataStore) {
        ArgCheck.notNull(objectId, "objectId");
        ArgCheck.notBlank(persistenceVersion, "persistenceVersion");
        ArgCheck.notNull(dataStore, "dataStore");

        return versionLocationRepo.findByObjectIdAndPersistenceVersionAndDataStore(objectId, persistenceVersion, dataStore)
                .orElseThrow(() -> new NotFoundException(String.format(
                        "Object version storage location was not found for object %s version %s in %s",
                        objectId, persistenceVersion, dataStore)));
    }

    public boolean existsInDataStore(UUID objectId, String persistenceVersion, DataStore dataStore) {
        ArgCheck.notNull(objectId, "objectId");
        ArgCheck.notNull(dataStore, "dataStore");
        ArgCheck.notBlank(persistenceVersion, "persistenceVersion");

        return versionLocationRepo.findByObjectIdAndPersistenceVersionAndDataStore(objectId, persistenceVersion, dataStore).isPresent();
    }

    public PreservationObjectVersionLocation getStorageLocation(Long objectVersionLocationId) {
        ArgCheck.notNull(objectVersionLocationId, "objectVersionLocationId");

        return versionLocationRepo.findById(objectVersionLocationId)
                .orElseThrow(() -> new NotFoundException(String.format("Object version storage location %s was not found", objectVersionLocationId)));
    }

    /**
     * Creates a retrieve objects request and spawns jobs to fulfill the request.
     *
     * @param context user, vault, org details
     * @param externalObjectIds the object ids to retrieve, will retrieve ALL objects if empty
     * @param allVersions true if all object versions should be retrieved, and false for just the HEAD version
     * @return the id of the retrieve request
     */
    @Transactional
    public Long retrieveObjects(OperationContext context,
                                List<String> externalObjectIds,
                                boolean allVersions) {
        ArgCheck.notNull(context, "context");

        var request = new RetrieveRequest()
                .setUsername(context.getUsername())
                .setVault(context.getVault())
                .setCreatedTimestamp(Time.now())
                .setAllVersions(allVersions)
                .setDeleted(false);

        if (CollectionUtils.isNotEmpty(externalObjectIds)) {
            try {
                request.setExternalObjectIds(objectMapper.writeValueAsString(externalObjectIds));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        request = retrieveRequestRepo.save(request);
        var requestId = request.getRetrieveRequestId();

        var jobIds = jobService.createRetrieveJobs(context, externalObjectIds, allVersions);

        var requestJobs = jobIds.stream().map(jobId -> {
            return new RetrieveRequestJob()
                    .setRetrieveRequestId(requestId)
                    .setJobId(jobId);
        }).toList();

        retrieveRequestJobRepo.saveAll(requestJobs);

        return requestId;
    }

    public RetrieveRequest getRetrieveRequest(Long requestId) {
        ArgCheck.notNull(requestId, "requestId");

        return retrieveRequestRepo.findById(requestId).orElseThrow(() ->
                new NotFoundException(String.format("Retrieve request %s was not found", requestId)));
    }

    public List<RetrieveRequestJobComposite> getRetrieveRequestJobs(Long requestId) {
        ArgCheck.notNull(requestId, "requestId");

        return retrieveRequestJobRepo.findAllByRetrieveRequestId(requestId);
    }

    public RetrieveRequestJob getRetrieveRequestJob(Long jobId) {
        return retrieveRequestJobRepo.findByJobId(jobId).orElseThrow(() ->
                new NotFoundException(String.format("Retrieve request job for %s was not found", jobId)));
    }

    public void markRetrieveJobDownloaded(Long jobId) {
        ArgCheck.notNull(jobId, "jobId");

        var job = getRetrieveRequestJob(jobId);
        retrieveRequestJobRepo.save(job.setLastDownloadedTimestamp(Time.now()));
    }

    public void markRetrieveRequestDeleted(Long retrieveRequestId) {
        var request = getRetrieveRequest(retrieveRequestId);

        retrieveRequestRepo.save(request
                .setDeleted(true)
                .setDeletedTimestamp(Time.now()));
    }

    /**
     * Finds all of the retrieve request ids that have not been deleted and were created before the specified date
     */
    public List<Long> getAllExpiredButNotDeletedRetrieveRequests(LocalDateTime created) {
        ArgCheck.notNull(created, "created");
        return retrieveRequestRepo.findAllExpiredAndNotDeleted(created);
    }

    public String getOrgNameForRetrieveRequest(Long retrieveRequestId) {
        ArgCheck.notNull(retrieveRequestId, "retrieveRequestId");
        return retrieveRequestRepo.findOrgNameForRequest(retrieveRequestId);
    }

    @Transactional
    public void markObjectAsDeleted(String vault, String externalObjectId, String reason, String username) {
        ArgCheck.notBlank(vault, "vault");
        ArgCheck.notBlank(externalObjectId, "externalObjectId");
        ArgCheck.notBlank(reason, "reason");
        ArgCheck.notBlank(username, "username");

        var object = getObject(vault, externalObjectId);

        if (object.getState() != PreservationObjectState.ACTIVE) {
            throw new ValidationException(String.format(
                    "Cannot mark object %s as deleted because it is not in an active state", externalObjectId));
        }

        var now = Time.now();

        objectRepo.save(object.setState(PreservationObjectState.DELETED)
                .setUpdatedTimestamp(now));

        recordEvent(new PreservationEvent()
                .setEventTimestamp(now)
                .setObjectId(object.getObjectId())
                .setOutcome(EventOutcome.SUCCESS)
                .setType(EventType.DELETE_OBJ)
                .setUsername(username)
                .setLogs(Set.of(new PreservationEventLog()
                        .setLevel(LogLevel.INFO)
                        .setCreatedTimestamp(now)
                        .setMessage(reason))));
    }

    /**
     * Returns true if the object exists and is marked as deleted, and false otherwise
     *
     * @param vault the vault the object is in
     * @param externalObjectId the object's external id
     * @return true if the object exists and is marked as deleted
     */
    public boolean isObjectDeleted(String vault, String externalObjectId) {
        ArgCheck.notBlank(vault, "vault");
        ArgCheck.notBlank(externalObjectId, "externalObjectId");

        return objectRepo.findByVaultAndExternalObjectId(vault, externalObjectId)
                .map(object -> object.getState() == PreservationObjectState.DELETED)
                .orElse(false);
    }

    private PreservationObject getOrCreateObjectIfNotExists(UUID objectId, String vault, String externalObjectId) {
        ArgCheck.notNull(objectId, "objectId");
        ArgCheck.notBlank(vault, "vault");
        ArgCheck.notBlank(externalObjectId, "externalObjectId");

        return objectRepo.findById(objectId).orElseGet(() -> {
            try {
                return objectRepo.save(new PreservationObject()
                        .setObjectId(objectId)
                        .setVault(vault)
                        .setExternalObjectId(externalObjectId)
                        .setState(PreservationObjectState.ACTIVE)
                        .setCreatedTimestamp(Time.now())
                        .setUpdatedTimestamp(Time.now()));
            } catch (DbActionExecutionException e) {
                if (e.getCause() instanceof DuplicateKeyException) {
                    // this means there was a concurrent update -- reject
                    throw new ConcurrentUpdateException(String.format(
                            "Cannot create a new version for object %s in vault %s due to a concurrent update.",
                            externalObjectId, vault));
                }
                throw e;
            }
        });
    }

    private PreservationObjectVersion createNewObjectVersion(PreservationObject object, Long ingestId) {
        int newVersion = 1;
        if (object.getHeadObjectVersionId() != null) {
            var currentHead = getObjectVersion(object.getHeadObjectVersionId());
            newVersion = currentHead.getVersion() + 1;
        }

        var objectVersion = new PreservationObjectVersion()
                .setVersion(newVersion)
                .setObjectId(object.getObjectId())
                .setIngestId(ingestId)
                .setCreatedTimestamp(Time.now())
                .setUpdatedTimestamp(Time.now());

        try {
            return versionRepo.save(objectVersion);
        } catch (DbActionExecutionException e) {
            if (e.getCause() instanceof DuplicateKeyException) {
                // this means there was a concurrent update -- reject
                throw new ConcurrentUpdateException(String.format(
                        "Cannot create a new version for object %s in vault %s due to a concurrent update.",
                        object.getExternalObjectId(), object.getVault()));
            }
            throw e;
        }
    }

    private void ingestFiles(PreservationObjectVersion objectVersion, List<IngestBatchObjectFile> batchFiles) {
        batchFiles.forEach(batchFile -> {
            var file = getOrCreateObjectFile(objectVersion.getObjectId(),
                    batchFile.getSha256Digest(), batchFile.getFileSize());
            var versionFile = createObjectVersionFile(objectVersion.getObjectVersionId(), file, batchFile);
            updateFileDetails(file.getObjectFileId(), batchFile);
        });
    }

    private PreservationObjectFile getOrCreateObjectFile(UUID objectId, String sha256Digest, long fileSize) {
        var existingFile = fileRepo.findOptionalByObjectIdAndSha256Digest(objectId, sha256Digest);

        return existingFile.orElseGet(() -> {
            return fileRepo.save(new PreservationObjectFile()
                    .setObjectId(objectId)
                    .setSha256Digest(sha256Digest.toLowerCase())
                    .setFileSize(fileSize)
                    .setCreatedTimestamp(Time.now()));
        });
    }

    private PreservationObjectVersionFile createObjectVersionFile(Long objectVersionId,
                                                                  PreservationObjectFile objectFile,
                                                                  IngestBatchObjectFile batchFile) {
        return versionFileRepo.save(new PreservationObjectVersionFile()
                .setObjectVersionId(objectVersionId)
                .setObjectFileId(objectFile.getObjectFileId())
                .setFilePath(batchFile.getFilePath())
                .setCreatedTimestamp(Time.now()));
    }

    /**
     * Copies over the format, encoding, and validity info from the ingest file
     *
     * @param objectFileId id of the preservation file
     * @param batchFile source batch file
     */
    private void updateFileDetails(Long objectFileId, IngestBatchObjectFile batchFile) {
        objectFileFormatRepo.deleteAllByObjectFileId(objectFileId);

        if (batchFile.getFormats() != null) {
            var formats = new ArrayList<PreservationObjectFileFormat>();
            batchFile.getFormats().forEach(format -> {
                formats.add(new PreservationObjectFileFormat()
                        .setObjectFileId(objectFileId)
                        .setFileFormatId(format.getFileFormatId())
                        .setMetaSourceId(format.getMetaSourceId()));
            });
            objectFileFormatRepo.saveAll(formats);
        }

        objectFileEncodingRepo.deleteAllByObjectFileId(objectFileId);

        if (batchFile.getEncoding() != null) {
            var encoding = new ArrayList<PreservationObjectFileEncoding>();
            batchFile.getEncoding().forEach(e -> {
                encoding.add(new PreservationObjectFileEncoding()
                        .setObjectFileId(objectFileId)
                        .setFileEncodingId(e.getFileEncodingId())
                        .setMetaSourceId(e.getMetaSourceId()));
            });
            objectFileEncodingRepo.saveAll(encoding);
        }

        objectFileValidityRepo.deleteAllByObjectFileId(objectFileId);

        if (batchFile.getValidity() != null) {
            var validity = new ArrayList<PreservationObjectFileValidity>();
            batchFile.getValidity().forEach(v -> {
                validity.add(new PreservationObjectFileValidity()
                        .setObjectFileId(objectFileId)
                        .setValid(v.getValid())
                        .setWellFormed(v.getWellFormed())
                        .setMetaSourceId(v.getMetaSourceId()));
            });
            objectFileValidityRepo.saveAll(validity);
        }
    }

    private PreservationObjectVersionComposite getObjectVersionComposite(
            PreservationObject object,
            PreservationObjectVersion objectVersion) {
        var composite = new PreservationObjectVersionComposite()
                .setObjectId(objectVersion.getObjectId())
                .setObjectVersionId(objectVersion.getObjectVersionId())
                .setState(object.getState())
                .setVault(object.getVault())
                .setExternalObjectId(object.getExternalObjectId())
                .setVersion(objectVersion.getVersion())
                .setPersistenceVersion(objectVersion.getPersistenceVersion())
                .setInitialPersistenceVersion(objectVersion.getInitialPersistenceVersion())
                .setIngestId(objectVersion.getIngestId())
                .setCreatedTimestamp(objectVersion.getCreatedTimestamp());

        var versionFiles = versionFileRepo.findAllByObjectVersionId(objectVersion.getObjectVersionId());

        versionFiles.forEach(versionFile -> {
            var file = fileRepo.findById(versionFile.getObjectFileId())
                    .orElseThrow(() -> new NotFoundException(String.format("File <%s> was not found.",
                            versionFile.getObjectFileId())));
            var formats = findAllFileFormatComposites(versionFile.getObjectFileId());
            var encoding = findAllFileEncodingComposites(versionFile.getObjectFileId());
            var validity = findAllFileValidityComposites(versionFile.getObjectFileId());

            composite.getFiles().add(new PreservationObjectVersionFileComposite()
                    .setObjectFileId(versionFile.getObjectFileId())
                    .setObjectVersionFileId(versionFile.getObjectVersionFileId())
                    .setSha256Digest(file.getSha256Digest())
                    .setFileSize(file.getFileSize())
                    .setFilePath(versionFile.getFilePath())
                    .setCreatedTimestamp(file.getCreatedTimestamp())
                    .setFormats(formats)
                    .setEncoding(encoding)
                    .setValidity(validity));
        });

        return composite;
    }

    private List<PreservationObjectFileFormatComposite> findAllFileFormatComposites(Long objectFileId) {
        var formats = objectFileFormatRepo.findAllByObjectFileId(objectFileId);
        return formats.stream().map(format -> {
            var fileFormat = fileFormatRepo.findById(format.getFileFormatId())
                    .orElseThrow(() -> new NotFoundException(String.format("File format %s was not found", format.getFileFormatId())));
            var source = getMetaSource(format.getMetaSourceId());
            return new PreservationObjectFileFormatComposite(format, fileFormat, source);
        }).toList();
    }

    private List<PreservationObjectFileEncodingComposite> findAllFileEncodingComposites(Long objectFileId) {
        var encodings = objectFileEncodingRepo.findAllByObjectFileId(objectFileId);
        return encodings.stream().map(encoding -> {
            var fileEncoding = fileEncodingRepo.findById(encoding.getFileEncodingId())
                    .orElseThrow(() -> new NotFoundException(String.format("File encoding %s was not found", encoding.getFileEncodingId())));
            var source = getMetaSource(encoding.getMetaSourceId());
            return new PreservationObjectFileEncodingComposite(encoding, fileEncoding, source);
        }).toList();
    }

    private List<PreservationObjectFileValidityComposite> findAllFileValidityComposites(Long objectFileId) {
        var validity = objectFileValidityRepo.findAllByObjectFileId(objectFileId);
        return validity.stream().map(v -> {
            return new PreservationObjectFileValidityComposite(v, getMetaSource(v.getMetaSourceId()));
        }).toList();
    }

    private MetaSource getMetaSource(Long metaSourceId) {
        return metaSourceRepo.findById(metaSourceId)
                .orElseThrow(() -> new NotFoundException(String.format("Meta source %s was not found", metaSourceId)));
    }

    private Optional<Long> getMetaSourceId(String source) {
        return metaSourceRepo.findBySource(source).map(MetaSource::getMetaSourceId);
    }

    private void updateVaultStatsForObjectVersion(String vault, UUID objectId, String persistenceVersion) {
        try {
            var files = getNewInVersionFiles(objectId, persistenceVersion);
            var bytes = files.stream()
                    .map(SimplePreservationObjectVersionFileComposite::getFileSize)
                    .filter(Objects::nonNull)
                    .reduce(0L, Long::sum);
            var addObjects = "v1".equals(persistenceVersion) ? 1 : 0;
            vaultStatsUpdater.update(vault, addObjects, bytes);
        } catch (Exception e) {
            LOG.warn("Failed to update vault stats for {} to include object {} version {}",
                    vault, objectId, persistenceVersion, e);
        }
    }

    private List<PreservationObjectVersionLocation> resolveVersionLocations(UUID objectId, List<String> versions, DataStore dataStore) {
        List<PreservationObjectVersionLocation> locations;

        if (CollectionUtils.isEmpty(versions)) {
            locations = versionLocationRepo.findAllByObjectIdAndDataStore(objectId, dataStore);
        } else {
            locations = versions.stream().map(version -> {
                return versionLocationRepo.findByObjectIdAndPersistenceVersionAndDataStore(
                                objectId, version, dataStore)
                        .orElseThrow(() -> new NotFoundException(
                                String.format("Preservation object <%s> persistence version %s was not found in %s.",
                                        objectId, version, dataStore)));


            }).toList();
        }

        return locations;
    }

    private void incrementEventCounter(PreservationEvent event) {
        meterRegistry.counter("preservationEvent",
                        "type", event.getType().name(),
                        "outcome", event.getOutcome().name())
                .increment();
    }

}
