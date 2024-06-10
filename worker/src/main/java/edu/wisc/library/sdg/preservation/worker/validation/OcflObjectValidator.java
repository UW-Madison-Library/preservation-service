package edu.wisc.library.sdg.preservation.worker.validation;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.wisc.library.sdg.preservation.common.PreservationConstants;
import edu.wisc.library.sdg.preservation.common.model.PreservationMetadata;
import edu.wisc.library.sdg.preservation.common.util.ArgCheck;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ObjectVersionState;
import io.ocfl.api.OcflRepository;
import io.ocfl.api.exception.NotFoundException;
import io.ocfl.api.model.DigestAlgorithm;
import io.ocfl.api.model.FileDetails;
import io.ocfl.api.model.ValidationIssue;
import io.ocfl.api.model.VersionDetails;
import io.ocfl.api.model.VersionNum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class OcflObjectValidator {

    private static final Logger LOG = LoggerFactory.getLogger(OcflObjectValidator.class);

    private static final String SYSTEM_PREFIX = PreservationConstants.OCFL_OBJECT_SYSTEM_DIR + "/";

    private final OcflRepository ocflRepo;
    private final ObjectMapper objectMapper;

    public OcflObjectValidator(@Qualifier("localOcflRepo") OcflRepository ocflRepo,
                               ObjectMapper objectMapper) {
        this.ocflRepo = ocflRepo;
        this.objectMapper = objectMapper;
    }

    /**
     * Validates the ocfl object. If the object is valid. Then it validates that the object matches the state that's
     * recorded in the preservation db.
     *
     * @param objectId ocfl object id
     * @param vault vault the object is in
     * @param externalObjectId the object's external id
     * @param versionStates the object states as recorded in the db
     * @param contentFixityCheck true if the digests of all files in the ocfl object should be validated
     * @return validation errors
     */
    public ObjectValidationResult validateObject(String objectId,
                               String vault,
                               String externalObjectId,
                               List<ObjectVersionState> versionStates,
                               boolean contentFixityCheck) {
        ArgCheck.notBlank(objectId, "objectId");
        ArgCheck.notBlank(vault, "vault");
        ArgCheck.notBlank(externalObjectId, "externalObjectId");
        ArgCheck.notNull(versionStates, "versionStates");

        var objectResult = new ObjectValidationResult();

        // Must invalidate cache prior to validation to ensure the actual state on disk is read
        ocflRepo.invalidateCache(objectId);

        try {
            // TODO figure out how to get version specific results here?
            var results = ocflRepo.validateObject(objectId, contentFixityCheck);
            results.getErrors().stream()
                    .map(ValidationIssue::toString)
                    .forEach(objectResult::addProblem);

            if (results.hasErrors()) {
                // The OCFL validation doesn't provide a good way to pinpoint version specific issues
                objectResult.setStatus(ValidationProblem.CORRUPT);
                // short-circuit if object is invalid
                return objectResult;
            }

            var ocflObject = ocflRepo.describeObject(objectId);
            VersionNum headVersionNum = null;

            for (var expectedVersion : versionStates) {
                var versionResult = new VersionValidationResult(
                        expectedVersion.getVersion(), expectedVersion.getPersistenceVersion());
                var versionStr = expectedVersion.getPersistenceVersion();
                var versionNum = VersionNum.fromString(versionStr);

                if (headVersionNum == null
                        || headVersionNum.compareTo(versionNum) < 0) {
                    headVersionNum = versionNum;
                }

                var ocflVersion = ocflObject.getVersion(versionNum);

                if (ocflVersion == null) {
                    versionResult.setStatus(ValidationProblem.MISSING);
                    versionResult.addProblem("Expected OCFL object %s version %s to exist, but was not found",
                            objectId, versionStr);
                    objectResult.addVersionResult(versionResult);
                    continue;
                }

                var unseenFiles = ocflVersion.getFiles().stream()
                        .map(FileDetails::getPath)
                        // System files aren't modeled in the db
                        .filter(path -> !path.startsWith(SYSTEM_PREFIX))
                        .collect(Collectors.toSet());

                expectedVersion.getState().forEach((path, details) -> {
                    var prefixedPath = String.format("%s/%s", PreservationConstants.OCFL_OBJECT_OBJECT_DIR, path);
                    if (unseenFiles.remove(prefixedPath)) {
                        var ocflFile = ocflVersion.getFile(prefixedPath);
                        var ocflDigest = ocflFile.getFixity().get(DigestAlgorithm.sha256);

                        if (!ocflDigest.equalsIgnoreCase(details.getSha256Digest())) {
                            versionResult.addProblem("File %s in OCFL object %s version %s does not match expected digest." +
                                            " Expected: %s; Actual: %s",
                                    prefixedPath, objectId, versionStr, details.getSha256Digest(), ocflDigest);
                        }
                    } else {
                        versionResult.addProblem("File %s was not found in OCFL object %s version %s",
                                prefixedPath, objectId, versionStr);
                    }
                });

                unseenFiles.forEach(file -> {
                    versionResult.addProblem("Unexpected file %s found in OCFL object %s version %s",
                            file, objectId, versionStr);
                });

                validateMetadata(objectId, versionStr, ocflVersion, vault,
                        externalObjectId, expectedVersion.getVersion(), versionResult);

                if (versionResult.hasProblems()) {
                    // TODO make more specific?
                    versionResult.setStatus(ValidationProblem.CORRUPT);
                    objectResult.addVersionResult(versionResult);
                }
            }

            if (!Objects.equals(ocflObject.getHeadVersionNum(), headVersionNum)) {
                // TODO this status is not ideal... but it's hard to know what's missing
                objectResult.setStatus(ValidationProblem.CORRUPT);
                objectResult.addProblem("Expected HEAD version of OCFL object %s to be %s, but was %s",
                        objectId, headVersionNum, ocflObject.getHeadVersionNum());
            }
        } catch (NotFoundException e) {
            objectResult.setStatus(ValidationProblem.MISSING);
            objectResult.addProblem("OCFL object %s does not exist", objectId);
        }

        return objectResult;
    }

    private void validateMetadata(String objectId,
                                  String versionStr,
                                  VersionDetails ocflVersion,
                                  String vault,
                                  String externalObjectId,
                                  Integer expectedVersion,
                                  VersionValidationResult versionResult) {
        var metadataFile = ocflVersion.getFile(PreservationConstants.SYSTEM_METADATA_PATH);

        if (metadataFile == null) {
            versionResult.addProblem("File %s was not found in OCFL object %s version %s",
                    PreservationConstants.SYSTEM_METADATA_PATH, objectId, versionStr);
        } else {
            try (var stream = ocflRepo.getObject(ocflVersion.getObjectVersionId())
                    .getFile(PreservationConstants.SYSTEM_METADATA_PATH).getStream()) {
                var metadata = objectMapper.readValue(stream, PreservationMetadata.class);

                if (!Objects.equals(externalObjectId, metadata.getExternalObjectId())) {
                    versionResult.addProblem("In OCFL object %s version %s, expected external object ID to be %s; found %s",
                            objectId, versionStr, externalObjectId, metadata.getExternalObjectId());
                }
                if (!Objects.equals(vault, metadata.getVault())) {
                    versionResult.addProblem("In OCFL object %s version %s, expected vault to be %s; found %s",
                            objectId, versionStr, vault, metadata.getVault());
                }
                if (!Objects.equals(expectedVersion, metadata.getVersion())) {
                    versionResult.addProblem("In OCFL object %s version %s, expected version to be %s; found %s",
                            objectId, versionStr, expectedVersion, metadata.getVersion());
                }
            } catch (Exception e) {
                versionResult.addProblem("Failed to parse preservation metadata in OCFL object %s version %s",
                        objectId, versionStr);
            }
        }
    }

    public static class ObjectValidationResult {

        private ValidationProblem status;
        private List<String> problems;
        private List<VersionValidationResult> versionResults;

        public ObjectValidationResult() {
            this.status = ValidationProblem.NONE;
            this.problems = new ArrayList<>();
            this.versionResults = new ArrayList<>();
        }

        public void addProblem(String pattern, Object... args) {
            problems.add(String.format(pattern, args));
        }

        public void setStatus(ValidationProblem status) {
            this.status = status;
        }

        public void addVersionResult(VersionValidationResult versionResult) {
            if (versionResult.status != ValidationProblem.NONE) {
                versionResults.add(versionResult);
            }
        }

        public boolean hasObjectProblems() {
            return !problems.isEmpty();
        }

        public boolean hasVersionProblems() {
            return !versionResults.isEmpty();
        }

        public List<String> getProblems() {
            return problems;
        }

        public List<VersionValidationResult> getVersionResults() {
            return versionResults;
        }

        public ValidationProblem getStatus() {
            return status;
        }

        @Override
        public String toString() {
            return "ObjectValidationResult{" +
                    "status=" + status +
                    ", problems=" + problems +
                    ", versionResults=" + versionResults +
                    '}';
        }
    }

    public static class VersionValidationResult {

        private Integer version;
        private String persistenceVersion;
        private ValidationProblem status;
        private List<String> problems;

        public VersionValidationResult(Integer version, String persistenceVersion) {
            this.version = version;
            this.persistenceVersion = persistenceVersion;
            this.status = ValidationProblem.NONE;
            this.problems = new ArrayList<>();
        }

        public void addProblem(String pattern, Object... args) {
            problems.add(String.format(pattern, args));
        }

        public void setStatus(ValidationProblem status) {
            this.status = status;
        }

        public boolean hasProblems() {
            return !problems.isEmpty();
        }

        public List<String> getProblems() {
            return problems;
        }

        public ValidationProblem getStatus() {
            return status;
        }

        public Integer getVersion() {
            return version;
        }

        public String getPersistenceVersion() {
            return persistenceVersion;
        }

        @Override
        public String toString() {
            return "VersionValidationResult{" +
                    "version=" + version +
                    ", persistenceVersion='" + persistenceVersion + '\'' +
                    ", status=" + status +
                    ", problems=" + problems +
                    '}';
        }
    }

    public enum ValidationProblem {
        NONE,
        MISSING,
        CORRUPT
    }

}
