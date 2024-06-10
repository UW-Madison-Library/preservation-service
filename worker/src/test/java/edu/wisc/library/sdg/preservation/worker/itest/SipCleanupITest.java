package edu.wisc.library.sdg.preservation.worker.itest;

import com.google.common.io.MoreFiles;
import com.google.common.io.RecursiveDeleteOption;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.JobState;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ShouldDeleteBatchResponse;
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
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;

public class SipCleanupITest extends ITestBase {

    private static final Logger LOG = LoggerFactory.getLogger(SipCleanupITest.class);

    @Value("${app.upload.dir}")
    private Path uploadDir;

    private Long jobId;

    @BeforeEach
    public void setup() {
        jobId = randomSerialId();
    }

    @BeforeEach
    public void baseline() {
        try {
            LOG.warn("Creating directory {}", uploadDir);
            if (!Files.exists(uploadDir)) Files.createDirectories(uploadDir);
        } catch (IOException e) {
            LOG.warn("Failed to create upload directory");
        }

        try (var files = Files.list(uploadDir)) {
            files.forEach(file -> {
                LOG.warn("Attempting to delete {}", file);
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

        try {
            LOG.warn("Deleting batches...");
            workDirectory.listIngestIds().forEach(id -> {
                LOG.warn("Deleting batch {}", id);
                workDirectory.deleteBatchDirectory(id);
            });
        } catch (Exception e) {
            LOG.warn("Exception baselining tests", e);
        }
    }

    @Test
    public void removesWorkDirectory() {
        var sip = writeSip(UUID.randomUUID());
        var batch = randomSerialId();
        var workDir = writeSipWork(batch);

        assertThat(Files.exists(workDir), is(true));

        mockShouldDeleteBatch(sip, batch, ShouldDeleteBatchResponse.VerdictEnum.DELETE);
        mockShouldDeleteBatch(batch, ShouldDeleteBatchResponse.VerdictEnum.DELETE);

        cleanupSips(jobId);

        assertThat(Files.exists(workDir), is(false));
    }

    @Test
    public void deleteExpiredSipsWhenInBothDirs() {
        var sip1 = writeSip(UUID.randomUUID());
        var sip2 = writeSip(UUID.randomUUID());
        var sip3 = writeSip(UUID.randomUUID());

        var batch1 = randomSerialId();
        var batch2 = randomSerialId();
        var batch3 = randomSerialId();

        var work1 = writeSipWork(batch1);
        writeSipWork(batch2);
        var work3 = writeSipWork(batch3);

        mockShouldDeleteBatch(sip1, batch1, ShouldDeleteBatchResponse.VerdictEnum.KEEP);
        mockShouldDeleteBatch(sip2, batch2, ShouldDeleteBatchResponse.VerdictEnum.DELETE);
        mockShouldDeleteBatch(sip3, batch3, ShouldDeleteBatchResponse.VerdictEnum.KEEP);

        mockShouldDeleteBatch(batch1, ShouldDeleteBatchResponse.VerdictEnum.KEEP);
        mockShouldDeleteBatch(batch2, ShouldDeleteBatchResponse.VerdictEnum.DELETE);
        mockShouldDeleteBatch(batch3, ShouldDeleteBatchResponse.VerdictEnum.KEEP);

        cleanupSips(jobId);

        verifySetJobState(jobId, JobState.EXECUTING);
        verifySetJobState(jobId, JobState.COMPLETE);

        assertUploadContents(sip1, sip3);
        assertWorkContents(work1, work3);
    }

    @Test
    public void deleteExpiredSipsWhenNotInBothLocations() {
        var sip1 = writeSip(UUID.randomUUID());
        var sip2 = writeSip(UUID.randomUUID());

        var batch1 = randomSerialId();
        var batch2 = randomSerialId();
        var batch3 = randomSerialId();

        var work2 = writeSipWork(batch2);
        writeSipWork(batch3);

        mockShouldDeleteBatch(sip1, batch1, ShouldDeleteBatchResponse.VerdictEnum.DELETE);
        mockShouldDeleteBatch(sip2, batch2, ShouldDeleteBatchResponse.VerdictEnum.KEEP);

        mockShouldDeleteBatch(batch2, ShouldDeleteBatchResponse.VerdictEnum.KEEP);
        mockShouldDeleteBatch(batch3, ShouldDeleteBatchResponse.VerdictEnum.DELETE);

        cleanupSips(jobId);

        verifySetJobState(jobId, JobState.EXECUTING);
        verifySetJobState(jobId, JobState.COMPLETE);

        assertUploadContents(sip2);
        assertWorkContents(work2);
    }

    @Test
    public void deleteExpiredSipsWhenDoNotMapToBatches() {
        var sip1 = writeSip(UUID.randomUUID());
        var sip2 = writeSip(UUID.randomUUID());
        var sip3 = writeSip(UUID.randomUUID());

        var batch1 = randomSerialId();
        var batch2 = randomSerialId();
        var batch3 = randomSerialId();

        writeSipWork(batch1);
        var work2 = writeSipWork(batch2);
        writeSipWork(batch3);

        mockShouldDeleteBatch(sip1, batch1, ShouldDeleteBatchResponse.VerdictEnum.NOT_FOUND);
        mockShouldDeleteBatch(sip2, batch2, ShouldDeleteBatchResponse.VerdictEnum.KEEP);
        mockShouldDeleteBatch(sip3, batch3, ShouldDeleteBatchResponse.VerdictEnum.NOT_FOUND);

        mockShouldDeleteBatch(batch1, ShouldDeleteBatchResponse.VerdictEnum.NOT_FOUND);
        mockShouldDeleteBatch(batch2, ShouldDeleteBatchResponse.VerdictEnum.KEEP);
        mockShouldDeleteBatch(batch3, ShouldDeleteBatchResponse.VerdictEnum.NOT_FOUND);

        cleanupSips(jobId);

        verifySetJobState(jobId, JobState.EXECUTING);
        verifySetJobState(jobId, JobState.COMPLETE);

        assertUploadContents(sip2);
        assertWorkContents(work2);
    }

    private void assertUploadContents(Path... expectedFiles) {
        assertDirContents(uploadDir, expectedFiles);
    }

    private void assertWorkContents(Path... expectedFiles) {
        assertDirContents(workDirectory.batchDirectory(randomSerialId()).getParent(), expectedFiles);
    }

    private void assertDirContents(Path dir, Path... expectedFiles) {
        try (var list = Files.list(dir)) {
            var files = list.toList();
            assertThat(files, containsInAnyOrder(expectedFiles));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private Path writeSip(UUID uuid) {
        var path = uploadDir.resolve(String.format("%s-bag.zip", uuid));
        try {
            Files.writeString(path, "testing");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return path;
    }

    private Path writeSipWork(Long id) {
        var path = workDirectory.batchDirectory(id);
        try {
            Files.createDirectories(path);
            return path;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
