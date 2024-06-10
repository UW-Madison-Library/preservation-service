package edu.wisc.library.sdg.preservation.worker.itest;

import com.google.common.io.MoreFiles;
import com.google.common.io.RecursiveDeleteOption;
import edu.wisc.library.sdg.preservation.common.PreservationConstants;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.JobState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

public class DeleteDipITest extends ITestBase {

    private static final Logger LOG = LoggerFactory.getLogger(DeleteDipITest.class);

    @Value("${app.dissemination.dir}")
    private Path disseminationDirectory;

    private Long jobId;

    @BeforeEach
    public void setup() {
        jobId = randomSerialId();
    }

    @BeforeEach
    public void baseline() {
        try {
            if (!Files.exists(disseminationDirectory)) Files.createDirectories(disseminationDirectory);
        } catch (IOException e) {
            LOG.warn("Failed to create dissemination directory");
        }

        try (var files = Files.list(disseminationDirectory)) {
            files.forEach(file -> {
                try {
                    if (Files.isDirectory(file)) {
                        MoreFiles.deleteRecursively(file, RecursiveDeleteOption.ALLOW_INSECURE);
                    } else {
                        Files.deleteIfExists(file);
                    }
                } catch (IOException e) {
                    LOG.warn("Exception baselining tests", e);
                }
            });
        } catch (IOException e) {
            LOG.warn("Exception baselining tests", e);
        }
    }

    @Test
    public void deleteAllDipsAssociatedToRequest() {
        var id1 = randomSerialId();
        var id2 = randomSerialId();
        var id3 = randomSerialId();
        var id4 = randomSerialId();
        var id5 = randomSerialId();

        var file1 = writeDip(id1);
        writeDip(id2);
        writeDip(id3);
        writeDip(id4);
        var file5 = writeDip(id5);

        deleteDip(jobId, randomSerialId(), List.of(id2, id3, id4));

        verifySetJobState(jobId, JobState.EXECUTING);
        verifySetJobState(jobId, JobState.COMPLETE);
        assertDisseminationContents(file1, file5);
    }

    @Test
    public void doNothingWhenDipsDoNotExist() {
        var id1 = randomSerialId();
        var id2 = randomSerialId();
        var id3 = randomSerialId();

        var file1 = writeDip(id1);
        var file2 = writeDip(id2);
        var file3 = writeDip(id3);

        deleteDip(jobId, randomSerialId(), List.of(randomSerialId(), randomSerialId()));

        verifySetJobState(jobId, JobState.EXECUTING);
        verifySetJobState(jobId, JobState.COMPLETE);
        assertDisseminationContents(file1, file2, file3);
    }

    private void assertDisseminationContents(Path... expectedFiles) {
        try (var list = Files.list(disseminationDirectory)) {
            var files = list.toList();
            assertThat(files, containsInAnyOrder(expectedFiles));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private Path writeDip(Long jobId) {
        var path = disseminationDirectory.resolve(String.format(PreservationConstants.DIP_BAG_ZIP_TMPL, jobId));
        try {
            Files.writeString(path, "testing");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return path;
    }

}
