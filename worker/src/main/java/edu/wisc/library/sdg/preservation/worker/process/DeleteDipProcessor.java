package edu.wisc.library.sdg.preservation.worker.process;

import edu.wisc.library.sdg.preservation.common.PreservationConstants;
import edu.wisc.library.sdg.preservation.common.exception.SafeRuntimeException;
import edu.wisc.library.sdg.preservation.common.util.ArgCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Component
public class DeleteDipProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(DeleteDipProcessor.class);

    private final Path disseminationDir;

    @Autowired
    public DeleteDipProcessor(@Value("${app.dissemination.dir}") Path disseminationDir) {
        this.disseminationDir = disseminationDir;
    }

    /**
     * Deletes all of the DIPs created as part of a retrieve request.
     *
     * @param jobId the id of the delete job
     * @param retrieveRequestId the id of the retrieve request
     * @param jobIds the ids of the jobs that are part of the retrieve request
     */
    public void deleteDip(Long jobId, Long retrieveRequestId, List<Long> jobIds) {
        ArgCheck.notNull(jobId, "jobId");
        ArgCheck.notNull(retrieveRequestId, "retrieveRequestId");
        ArgCheck.notNull(jobIds, "jobIds");

        var hasFailures = false;

        for (var id : jobIds) {
            var dip = archivePath(id);
            LOG.info("Deleting DIP at {}", dip);
            try {
                Files.deleteIfExists(dip);
            } catch (Exception e) {
                hasFailures = true;
                LOG.error("Failed to delete DIP at {}", dip, e);
            }
        }

        if (hasFailures) {
            throw new SafeRuntimeException(String.format(
                    "Failed to delete all DIPs associated to retrieve request %s", retrieveRequestId));
        }
    }

    private Path archivePath(Long jobId) {
        return disseminationDir.resolve(String.format(PreservationConstants.DIP_BAG_ZIP_TMPL, jobId));
    }

}
