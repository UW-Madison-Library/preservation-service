package edu.wisc.library.sdg.preservation.worker.process;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.UrlEscapers;
import edu.wisc.library.sdg.preservation.common.PreservationConstants;
import edu.wisc.library.sdg.preservation.common.model.PreservationMetadata;
import edu.wisc.library.sdg.preservation.common.util.Archiver;
import edu.wisc.library.sdg.preservation.common.util.Pair;
import edu.wisc.library.sdg.preservation.common.util.UncheckedFiles;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ObjectInfo;
import edu.wisc.library.sdg.preservation.worker.util.Safely;
import gov.loc.repository.bagit.creator.BagCreator;
import gov.loc.repository.bagit.domain.Metadata;
import gov.loc.repository.bagit.hash.StandardSupportedAlgorithms;
import io.ocfl.api.OcflRepository;
import io.ocfl.api.model.ObjectVersionId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Component
public class RetrieveObjectsProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(RetrieveObjectsProcessor.class);

    private final Path disseminationDirectory;
    private final OcflRepository ocflRepo;
    private final ManagerInternalApi managerClient;
    private final ObjectMapper objectMapper;

    public RetrieveObjectsProcessor(@Value("${app.dissemination.dir}") Path disseminationDirectory,
                                    @Qualifier("localOcflRepo") OcflRepository ocflRepo,
                                    ManagerInternalApi managerClient,
                                    ObjectMapper objectMapper) {
        this.disseminationDirectory = disseminationDirectory;
        this.ocflRepo = ocflRepo;
        this.managerClient = managerClient;
        this.objectMapper = objectMapper;
    }

    public void retrieveObjects(Long jobId, List<ObjectInfo> objects) {
        var bagDir = baselineJobDir(jobId);
        var archive = archivePath(jobId);

        Safely.exec(() -> {
            var objectVersions = new HashMap<Pair<String>, List<Integer>>();
            String vault = null;

            for (var object : objects) {
                var objectDir = resolveObjectDir(bagDir, object.getExternalId());
                var objectVersionDir = objectDir.resolve(object.getVersion().toString());
                UncheckedFiles.createDirectories(objectDir);

                var pair = Pair.of(object.getInternalId(), object.getExternalId());
                objectVersions.computeIfAbsent(pair, key -> new ArrayList<>()).add(object.getVersion());

                var temp = objectDir.resolve(UUID.randomUUID().toString());
                try {
                    LOG.debug("Writing object {} to {}", object.getInternalId(), objectVersionDir);
                    ocflRepo.getObject(ObjectVersionId.version(object.getInternalId(), object.getPersistenceVersion()), temp);

                    if (vault == null) {
                        vault = extractVaultName(temp);
                    }

                    removeSystemMetadata(temp);
                    Files.move(temp.resolve(PreservationConstants.OCFL_OBJECT_OBJECT_DIR), objectVersionDir);
                } finally {
                    UncheckedFiles.delete(temp);
                }
            }

            objectVersions.forEach((id, versions) -> {
                var objectDir = resolveObjectDir(bagDir, id.getRight());
                var premisFile = managerClient.getPremisDocument(id.getLeft(), versions);
                UncheckedFiles.move(premisFile.toPath(), objectDir.resolve(PreservationConstants.OBJECT_PREMIS_FILE));
            });


            try (var dipReadmeStream = getClass().getResourceAsStream("/README-DIP.md")) {
                if (dipReadmeStream != null) Files.copy(dipReadmeStream, bagDir.resolve("README-DIP.md"));
                else LOG.error("Failed to add readme to DIP in {}, it does not exist", bagDir);
            } catch (IOException e) {
                LOG.warn("Failed to add readme to DIP in {}", bagDir);
            }

            createBag(bagDir, vault);
            Archiver.archive(bagDir, archive, true);

            Safely.exec(() -> {
                UncheckedFiles.deleteDirectory(bagDir);
            }, () -> String.format("Failed to delete bag directory %s", bagDir));
        }, () -> {
            UncheckedFiles.deleteDirectory(bagDir);
            Files.deleteIfExists(archive);
        }, () -> "Cleanup failed");
    }

    private Path baselineJobDir(Long jobId) {
        var jobDir = disseminationDirectory.resolve(String.format(PreservationConstants.DIP_BAG_NAME_TMPL, jobId));
        UncheckedFiles.deleteDirectory(jobDir);
        return UncheckedFiles.createDirectories(jobDir);
    }

    private Path archivePath(Long jobId) {
        return disseminationDirectory.resolve(String.format(PreservationConstants.DIP_BAG_ZIP_TMPL, jobId));
    }

    private void createBag(Path bagDir, String vault) {
        Metadata metadata = new Metadata();
        if (vault != null) {
            metadata.add(PreservationConstants.DIP_BAGIT_VAULT_TAG_NAME, vault);
        }

        try {
            BagCreator.bagInPlace(
                    bagDir,
                    Arrays.asList(StandardSupportedAlgorithms.SHA256),
                    false,
                    metadata
            );
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private void removeSystemMetadata(Path objectDir) {
        var systemPath = objectDir.resolve(PreservationConstants.OCFL_OBJECT_SYSTEM_DIR);
        UncheckedFiles.deleteDirectory(systemPath);
    }

    private String extractVaultName(Path objectDir) {
        var metadataPath = objectDir.resolve(PreservationConstants.SYSTEM_METADATA_PATH);
        try {
            var metadata = objectMapper.readValue(metadataPath.toFile(), PreservationMetadata.class);
            return metadata.getVault();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private Path resolveObjectDir(Path root, String externalId) {
        var escapedId = UrlEscapers.urlFormParameterEscaper().escape(externalId);
        return root.resolve(escapedId);
    }

}
