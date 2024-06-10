package edu.wisc.library.sdg.preservation.worker.validation;

import com.google.common.hash.Funnels;
import com.google.common.hash.Hashing;
import com.google.common.io.ByteStreams;
import edu.wisc.library.sdg.preservation.common.exception.ValidationException;
import edu.wisc.library.sdg.preservation.common.util.PreservationPaths;
import edu.wisc.library.sdg.preservation.common.util.UncheckedFiles;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ObjectFile;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class VersionArchiveValidator {

    private static final Logger LOG = LoggerFactory.getLogger(VersionArchiveValidator.class);

    private final ManagerInternalApi managerClient;
    private final Path tempDir;

    @Autowired
    public VersionArchiveValidator(ManagerInternalApi managerClient,
                                   @Value("#{appConfig.tempDir}") Path tempDir) {
        this.managerClient = managerClient;
        this.tempDir = tempDir;
    }

    /**
     * Extracts the archived object version and validates that it contains the expected object content files.
     * The validation short-circuits on the first identified issue, throwing a ValidationException
     *
     * @param objectId the id of the object in the archive
     * @param persistenceVersion the persistence version contained in the archive
     * @param archive the archive
     * @throws ValidationException if unexpected content is discovered
     */
    public void validate(String objectId, String persistenceVersion, Path archive) {
        LOG.debug("Validating object <{}> version <{}> archive {}", objectId, persistenceVersion, archive);

        var temp = UncheckedFiles.createDirectories(tempDir.resolve(UUID.randomUUID().toString()));

        try {
            var prefix = PreservationPaths.archiveObjectContent(objectId, persistenceVersion);
            var digestMap = extractFileDigests(archive, prefix);
            validateContents(objectId, persistenceVersion, digestMap);
        } finally {
            UncheckedFiles.deleteDirectory(temp);
        }
    }

    private void validateContents(String objectId, String persistenceVersion, Map<String, String> digestMap) {
        var response = managerClient.retrieveNewInVersionFiles(objectId, persistenceVersion);

        LOG.debug("Expected files: {}", response);

        var expectedFiles = response.getFiles().stream()
                .collect(Collectors.toMap(ObjectFile::getFilePath, Function.identity()));

        var seenDigests = new HashSet<String>();

        if (expectedFiles.isEmpty()) {
            if (!digestMap.isEmpty()) {
                throw new ValidationException(String.format("Expected object <%s> version <%s> to contain no object content files; found: %s",
                        objectId, persistenceVersion, digestMap.keySet()));
            }
        } else {
            digestMap.forEach((file, digest) -> {
                var expected = expectedFiles.remove(file);

                if (expected == null) {
                    throw new ValidationException(String.format("Object <%s> version <%s> contains an unexpected file: %s",
                            objectId, persistenceVersion, file));
                } else {
                    if (!expected.getSha256Digest().equalsIgnoreCase(digest)) {
                        throw new ValidationException(String.format(
                                "File %s in object <%s> version <%s> does not match expected digest. Found: %s; Expected: %s",
                                file, objectId, persistenceVersion, digest, expected.getSha256Digest()));
                    } else {
                        seenDigests.add(digest);
                    }
                }
            });

            // This accounts for deduped files within the same version
            expectedFiles.entrySet()
                    .removeIf(entry -> seenDigests.contains(entry.getValue().getSha256Digest()));

            if (!expectedFiles.isEmpty()) {
                throw new ValidationException(String.format(
                        "Object <%s> version <%s> is missing the following files: %s",
                        objectId, persistenceVersion, expectedFiles.keySet()));
            }
        }
    }

    private Map<String, String> extractFileDigests(Path archive, String prefix) {
        var digestMap = new HashMap<String, String>();

        try (var zipFile = new ZipFile(archive.toFile())) {

            for (var it = zipFile.getEntries().asIterator(); it.hasNext();) {
                var entry = it.next();

                if (!entry.isDirectory() && entry.getName().startsWith(prefix)) {
                    try (var stream = zipFile.getInputStream(entry)) {
                        var hasher = Hashing.sha256().newHasher();
                        ByteStreams.copy(stream, Funnels.asOutputStream(hasher));
                        var digest = hasher.hash().toString();
                        digestMap.put(entry.getName().substring(prefix.length()), digest);
                    }
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return digestMap;
    }

}
