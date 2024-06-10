package edu.wisc.library.sdg.preservation.worker.process;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.wisc.library.sdg.preservation.common.PreservationConstants;
import edu.wisc.library.sdg.preservation.common.model.PreservationMetadata;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ObjectVersionState;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ObjectVersionStateState;
import edu.wisc.library.sdg.preservation.worker.validation.OcflObjectValidator;
import io.ocfl.api.OcflConfig;
import io.ocfl.api.OcflOption;
import io.ocfl.api.OcflRepository;
import io.ocfl.api.model.DigestAlgorithm;
import io.ocfl.api.model.ObjectVersionId;
import io.ocfl.core.OcflRepositoryBuilder;
import io.ocfl.core.extension.storage.layout.config.HashedNTupleLayoutConfig;
import io.ocfl.core.path.constraint.ContentPathConstraints;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class OcflObjectValidatorTest {

    private static final String OBJECT_ID = "object-1";
    private static final String VAULT = "uwdc";
    private static final String EXTERNAL_ID = "ext-object-id";

    @TempDir
    public Path tempDir;

    private OcflRepository ocflRepo;
    private ObjectMapper objectMapper;

    private OcflObjectValidator validator;

    @BeforeEach
    public void setup() throws IOException {
        var ocflRepoRoot = Files.createDirectories(tempDir.resolve("ocfl-root"));
        var ocflWorkDir = Files.createDirectories(tempDir.resolve("ocfl-work"));

        ocflRepo = new OcflRepositoryBuilder()
                .contentPathConstraints(ContentPathConstraints.cloud())
                .defaultLayoutConfig(new HashedNTupleLayoutConfig())
                .storage(storage -> storage.fileSystem(ocflRepoRoot))
                .workDir(ocflWorkDir)
                .ocflConfig(new OcflConfig().setDefaultDigestAlgorithm(DigestAlgorithm.sha256))
                .build();

        objectMapper = new ObjectMapper();

        validator = new OcflObjectValidator(ocflRepo, objectMapper);
    }

    @Test
    public void failWhenExpectedVersionDoesNotExist() {
        writeObject(Map.of(
                PreservationConstants.SYSTEM_METADATA_PATH, metadataString(1),
                "object/example.txt", "test"
        ));

        var results = validate(
                versionState(1, "v1", Map.of(
                        "example.txt", "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08"
                )),
                versionState(2, "v2", Map.of(
                        "example.txt", "60303ae22b998861bce3b28f33eec1be758a213c86c93c076dbe9f558c11c752"
                ))
        );

        assertObjectProblem(results, OcflObjectValidator.ValidationProblem.CORRUPT,
                "Expected HEAD version of OCFL object object-1 to be v2, but was v1");
        assertVersionProblemSize(results, 1);
        assertVersionProblem(results, 2, "v2",
                OcflObjectValidator.ValidationProblem.MISSING, "Expected OCFL object object-1 version v2 to exist, but was not found");
    }

    @Test
    public void failWhenUnexpectedVersionExists() {
        writeObject(Map.of(
                PreservationConstants.SYSTEM_METADATA_PATH, metadataString(1),
                "object/example.txt", "test"
        ));
        writeObject(Map.of(
                PreservationConstants.SYSTEM_METADATA_PATH, metadataString(2),
                "object/example.txt", "test2"
        ));

        var results = validate(
                versionState(1, "v1", Map.of(
                        "example.txt", "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08"
                ))
        );

        assertObjectProblem(results, OcflObjectValidator.ValidationProblem.CORRUPT,
                "Expected HEAD version of OCFL object object-1 to be v1, but was v2");
        assertVersionProblemSize(results, 0);
    }

    @Test
    public void failWhenMissingFile() {
        writeObject(Map.of(
                PreservationConstants.SYSTEM_METADATA_PATH, metadataString(1),
                "object/example.txt", "test"
        ));

        var results = validate(
                versionState(1, "v1", Map.of(
                        "example.txt", "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08",
                        "example-2.txt", "60303ae22b998861bce3b28f33eec1be758a213c86c93c076dbe9f558c11c752"
                ))
        );

        assertObjectProblem(results, OcflObjectValidator.ValidationProblem.NONE);
        assertVersionProblemSize(results, 1);
        assertVersionProblem(results, 1, "v1",
                OcflObjectValidator.ValidationProblem.CORRUPT, "File object/example-2.txt was not found in OCFL object object-1 version v1");
    }

    @Test
    public void failWhenUnexpectedFile() {
        writeObject(Map.of(
                PreservationConstants.SYSTEM_METADATA_PATH, metadataString(1),
                "object/example.txt", "test",
                "object/example-2.txt", "test-2"
        ));

        var results = validate(
                versionState(1, "v1", Map.of(
                        "example.txt", "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08"
                ))
        );

        assertObjectProblem(results, OcflObjectValidator.ValidationProblem.NONE);
        assertVersionProblemSize(results, 1);
        assertVersionProblem(results, 1, "v1",
                OcflObjectValidator.ValidationProblem.CORRUPT, "Unexpected file object/example-2.txt found in OCFL object object-1 version v1");
    }

    @Test
    public void failWhenMultipleVersionProblems() {
        writeObject(Map.of(
                PreservationConstants.SYSTEM_METADATA_PATH, metadataString(1),
                "object/example.txt", "test",
                "object/example-2.txt", "test-2"
        ));
        writeObject(Map.of(
                PreservationConstants.SYSTEM_METADATA_PATH, metadataString(2),
                "object/example.txt", "test",
                "object/example-2.txt", "test-2"
        ));

        var results = validate(
                versionState(1, "v1", Map.of(
                        "example.txt", "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08"
                )),
                versionState(2, "v2", Map.of(
                        "example.txt", "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08",
                        "example-2.txt", "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08"
                ))
        );

        assertObjectProblem(results, OcflObjectValidator.ValidationProblem.NONE);
        assertVersionProblemSize(results, 2);
        assertVersionProblem(results, 1, "v1",
                OcflObjectValidator.ValidationProblem.CORRUPT, "Unexpected file object/example-2.txt found in OCFL object object-1 version v1");
        assertVersionProblem(results, 2, "v2",
                OcflObjectValidator.ValidationProblem.CORRUPT,
                "File object/example-2.txt in OCFL object object-1 version v2 does not match expected digest." +
                        " Expected: 9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08; Actual: e063cdf36f817a24e97839b0799c023644dd1c31c668bda6481869027035a655");
    }

    @Test
    public void failWhenMissingMetadataFile() {
        writeObject(Map.of(
                "object/example.txt", "test"
        ));

        var results = validate(
                versionState(1, "v1", Map.of(
                        "example.txt", "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08"
                ))
        );

        assertObjectProblem(results, OcflObjectValidator.ValidationProblem.NONE);
        assertVersionProblemSize(results, 1);
        assertVersionProblem(results, 1, "v1",
                OcflObjectValidator.ValidationProblem.CORRUPT, "File system/metadata.json was not found in OCFL object object-1 version v1");
    }

    @Test
    public void failWhenMetadataFileWrong() {
        writeObject(Map.of(
                PreservationConstants.SYSTEM_METADATA_PATH, metadataString("ns", "id", 2),
                "object/example.txt", "test"
        ));

        var results = validate(
                versionState(1, "v1", Map.of(
                        "example.txt", "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08"
                ))
        );

        assertObjectProblem(results, OcflObjectValidator.ValidationProblem.NONE);
        assertVersionProblemSize(results, 1);
        assertVersionProblem(results, 1, "v1",
                OcflObjectValidator.ValidationProblem.CORRUPT,
                "In OCFL object object-1 version v1, expected external object ID to be ext-object-id; found id",
                "In OCFL object object-1 version v1, expected vault to be uwdc; found ns",
                "In OCFL object object-1 version v1, expected version to be 1; found 2");
    }

    @Test
    public void failWhenMetadataFileCorrupt() {
        writeObject(Map.of(
                PreservationConstants.SYSTEM_METADATA_PATH, "bogus",
                "object/example.txt", "test"
        ));

        var results = validate(
                versionState(1, "v1", Map.of(
                        "example.txt", "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08"
                ))
        );

        assertObjectProblem(results, OcflObjectValidator.ValidationProblem.NONE);
        assertVersionProblemSize(results, 1);
        assertVersionProblem(results, 1, "v1",
                OcflObjectValidator.ValidationProblem.CORRUPT,
                "Failed to parse preservation metadata in OCFL object object-1 version v1");
    }

    private void assertObjectProblem(OcflObjectValidator.ObjectValidationResult result,
                                     OcflObjectValidator.ValidationProblem type,
                                     String... problems) {
        assertEquals(type, result.getStatus());
        if (problems == null || problems.length == 0) {
            assertEquals(0, result.getProblems().size());
        } else {
            assertThat(result.getProblems(), containsInAnyOrder(problems));
        }
    }

    private void assertVersionProblemSize(OcflObjectValidator.ObjectValidationResult result, int size) {
        assertEquals(size, result.getVersionResults().size(), () -> String.format("Actual: %s", result));
    }

    private void assertVersionProblem(OcflObjectValidator.ObjectValidationResult result,
                                      int version,
                                      String persistenceVersion,
                                      OcflObjectValidator.ValidationProblem type,
                                      String... problems) {
        for (var versionResult : result.getVersionResults()) {
            if (Objects.equals(versionResult.getPersistenceVersion(), persistenceVersion)) {
                assertEquals(version, versionResult.getVersion());
                assertEquals(type, versionResult.getStatus());
                if (problems == null || problems.length == 0) {
                    assertEquals(0, versionResult.getProblems().size());
                } else {
                    assertThat(versionResult.getProblems(), containsInAnyOrder(problems));
                }
                return;
            }
        }
        fail(String.format("Expected problem for %s to exist. Found: %s", persistenceVersion, result));
    }

    private OcflObjectValidator.ObjectValidationResult validate(ObjectVersionState... states) {
        return validator.validateObject(OBJECT_ID, VAULT, EXTERNAL_ID, Arrays.asList(states), false);
    }

    private void writeObject(Map<String, String> files) {
        ocflRepo.updateObject(ObjectVersionId.head(OBJECT_ID), null, updater -> {
            files.forEach((path, content) -> {
                updater.writeFile(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)), path, OcflOption.OVERWRITE);
            });
        });
    }

    private ObjectVersionState versionState(int version, String persistenceVersion, Map<String, String> state) {
        var versionState = new ObjectVersionState()
                .version(version)
                .persistenceVersion(persistenceVersion);

        state.forEach((path, digest) -> {
            versionState.putStateItem(path, new ObjectVersionStateState().sha256Digest(digest));
        });

        return versionState;
    }

    private String metadataString(int version) {
        return metadataString(VAULT, EXTERNAL_ID, version);
    }

    private String metadataString(String vault, String externalId, int version) {
        var meta = new PreservationMetadata()
                .setMetadataVersion(PreservationMetadata.Version.V1_0)
                .setVault(vault)
                .setExternalObjectId(externalId)
                .setVersion(version);
        try {
            return objectMapper.writeValueAsString(meta);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
