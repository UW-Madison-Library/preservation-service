package edu.wisc.library.sdg.preservation.worker.tools.clamav;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class shells out to run clamdscan on a given directory. In order to use this class, clamdscan must be
 * installed on the system, and clamd must already be running.
 */
public class ClamAV {

    private static final Logger LOG = LoggerFactory.getLogger(ClamAV.class);

    private static final String CLAMDSCAN_PATH = "/usr/bin/clamdscan";

    /**
     * Scans all of the files in the specified directory and returns a list of paths to files that are suspected
     * to contain viruses.
     *
     * @param directory the directory to scan
     * @return set of paths of files that may contain viruses
     */
    public Set<Path> scan(Path directory) {
        try {
            var cmd = new ProcessBuilder().command(List.of(
                    "clamdscan", "--infected", "--stdout", "--no-summary", "--fdpass",
                    directory.toAbsolutePath().toString()))
                    .redirectErrorStream(true);

            if (LOG.isDebugEnabled()) {
                LOG.debug("Executing: {}", String.join(" ", cmd.command()));
            }

            var process = cmd.start();
            var exitCode = process.waitFor();

            switch (exitCode) {
                // Run and found no viruses
                case 0:
                    return Collections.emptySet();
                // Run and found viruses
                case 1:
                    return parseOutput(process);
                // Failed to run
                default:
                    var output = readCmdOutput(process);
                    throw new RuntimeException(String.format("clamdscan failed with exit code %s:\n%s", exitCode, output));
            }

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Indicates if ClamAV can be found.
     *
     * @return true if ClamAV is installed
     */
    public boolean exists() {
        // FIXME technically using which would be more robust, but it is unlikely to matter
        var fitsPath = Paths.get(CLAMDSCAN_PATH);
        return Files.exists(fitsPath) && Files.isRegularFile(fitsPath) && Files.isExecutable(fitsPath);
    }

    private Set<Path> parseOutput(Process process) {
        var paths = new HashSet<Path>();

        try (var outputReader = new BufferedReader(
                new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
            while (outputReader.ready()) {
                // Output lines are formatted like: /scandir/test.txt: Eicar-Signature FOUND
                var line = outputReader.readLine();
                if (StringUtils.isNotBlank(line)) {
                    LOG.warn("Virus detected: {}", line);
                    var end = line.lastIndexOf(':');
                    paths.add(Paths.get(line.substring(0, end)));
                }
            }

            return paths;
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse clamdscan output", e);
        }
    }

    private String readCmdOutput(Process process) {
        var outputText = "na";
        try (var output = new BufferedInputStream(process.getInputStream())) {
            outputText = IOUtils.toString(output, StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOG.warn("Failed to read clamdscan output", e);
        }
        return outputText;
    }

}
