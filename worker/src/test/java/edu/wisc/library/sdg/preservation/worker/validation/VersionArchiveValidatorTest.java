package edu.wisc.library.sdg.preservation.worker.validation;

import edu.wisc.library.sdg.preservation.common.exception.ValidationException;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ObjectFile;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RetrieveNewInVersionFilesResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class VersionArchiveValidatorTest {

    private VersionArchiveValidator validator;

    @Mock
    ManagerInternalApi internalApi;

    @TempDir
    Path temp;

    private final String objId = "obj1";
    private final String version = "v1";
    private final File file1 = new File("file.txt", "12a61f4e173fb3a11c05d6471f74728f76231b4a5fcd9667cef3af87a3ae4dc2");
    private final File file2 = new File("sub/file2.txt", "3a4f3ff4905c62daf02799ed7233b2e556fccf66b28c91cc6ce9adfea54050d7");

    @BeforeEach
    public void setup() {
        validator = new VersionArchiveValidator(internalApi, temp);
    }

    @Test
    public void validateWhenAllFilesPresent() {
        mockNewInVersion(file1, file2);
        validate();
    }

    @Test
    public void validateWhenDuplicateFile() {
        mockNewInVersion(file1, file2, new File("file3.txt", "12a61f4e173fb3a11c05d6471f74728f76231b4a5fcd9667cef3af87a3ae4dc2"));
        validate();
    }

    @Test
    public void failWhenMissingFile() {
        mockNewInVersion(file1, file2, new File("file3.txt", "4a4f3ff4905c62daf02799ed7233b2e556fccf66b28c91cc6ce9adfea54050d7"));
        assertThat(assertThrows(ValidationException.class, this::validate).getMessage(),
                containsString("missing the following files"));
    }

    @Test
    public void failWhenUnexpectedFile() {
        mockNewInVersion(file1);
        assertThat(assertThrows(ValidationException.class, this::validate).getMessage(),
                containsString("contains an unexpected file"));
    }

    @Test
    public void failWhenDigestMismatch() {
        mockNewInVersion(new File("file.txt", "3a4f3ff4905c62daf02799ed7233b2e556fccf66b28c91cc6ce9adfea54050d7"), file2);
        assertThat(assertThrows(ValidationException.class, this::validate).getMessage(),
                containsString("does not match expected digest"));
    }

    private void mockNewInVersion(File... files) {
        var response = new RetrieveNewInVersionFilesResponse().files(
                        Arrays.stream(files)
                        .map(f -> new ObjectFile().filePath(f.path).sha256Digest(f.digest))
                        .toList());

        doReturn(response).when(internalApi)
                .retrieveNewInVersionFiles(objId, version);
    }

    private void validate() {
        validator.validate(objId, version, Paths.get("src/test/resources/version-archives/obj1-v1.zip"));
    }

    private record File (String path, String digest) { }

}
