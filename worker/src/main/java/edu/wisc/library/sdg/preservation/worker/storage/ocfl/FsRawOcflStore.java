package edu.wisc.library.sdg.preservation.worker.storage.ocfl;

import edu.wisc.library.sdg.preservation.common.exception.SafeRuntimeException;
import edu.wisc.library.sdg.preservation.common.util.ArgCheck;
import io.micrometer.core.annotation.Timed;
import io.ocfl.api.model.VersionNum;
import io.ocfl.core.ObjectPaths;
import io.ocfl.core.extension.storage.layout.OcflStorageLayoutExtension;
import io.ocfl.core.util.NamasteTypeFile;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Implementation for filesystem based OCFL repos
 */
public class FsRawOcflStore extends BaseRawOcflStore {

    private static final Logger LOG = LoggerFactory.getLogger(FsRawOcflStore.class);

    private static final String STORE = "Filesystem";

    private final Path ocflRoot;
    private final OcflStorageLayoutExtension ocflLayout;
    private final NamasteTypeFile namasteFile;

    public FsRawOcflStore(Path ocflRoot,
                          OcflStorageLayoutExtension ocflLayout) {
        this.ocflRoot = ocflRoot;
        this.ocflLayout = ocflLayout;
        this.namasteFile = new NamasteTypeFile("ocfl_object_1.1");
    }

    @Override
    @Timed(value = "ocfl.raw", extraTags = {"store", STORE})
    public void installVersion(String objectId, String versionNum, Path versionDir) {
        ArgCheck.notBlank(objectId, "objectId");
        ArgCheck.notBlank(versionNum, "versionNum");
        ArgCheck.notNull(versionDir, "versionDir");

        validateVersion(versionDir);

        var objectRoot = ocflRoot.resolve(ocflLayout.mapObjectId(objectId));

        LOG.info("Cleaning up any existing files for object <{}> version <{}>", objectId, versionNum);

        // cleanup anything that may remain of a version before installing the restored copy
        try {
            FileUtils.deleteDirectory(objectRoot.resolve(versionDir.getFileName()).toFile());
        } catch (IOException e) {
            throw new SafeRuntimeException(String.format(
                    "Version cleanup failed when restoring object %s version %s. This could result in a corrupt OCFL object",
                    objectId, versionNum), e);
        }

        LOG.info("Copying {} into object <{}> version <{}> at {}", versionDir, objectId, versionNum, objectRoot);

        try {
            FileUtils.copyDirectoryToDirectory(versionDir.toFile(), objectRoot.toFile());
        } catch (IOException e) {
            throw new SafeRuntimeException(String.format(
                    "Failed to copy %s to %s. This could result in a corrupt OCFL object.", versionDir, objectRoot), e);
        }
    }

    @Override
    @Timed(value = "ocfl.raw", extraTags = {"store", STORE})
    public void installRootInventory(String objectId) {
        ArgCheck.notBlank(objectId, "objectId");

        var objectRoot = ocflRoot.resolve(ocflLayout.mapObjectId(objectId));

        try (var files = Files.list(objectRoot)) {
            var head = files.filter(Files::isDirectory)
                    .filter(dir -> dir.getFileName().toString().startsWith("v"))
                    .map(dir -> VersionNum.fromString(dir.getFileName().toString()))
                    .max(VersionNum::compareTo)
                    .orElseThrow(() -> new SafeRuntimeException(String.format(
                            "No OCFL versions found for object %s at %s", objectId, objectRoot)));

            LOG.info("Installing the {} inventory into the root of object {}", head, objectId);

            var versionPath = objectRoot.resolve(head.toString());
            var inventoryPath = ObjectPaths.inventoryPath(versionPath);
            var sidecarPath = ObjectPaths.findInventorySidecarPath(versionPath);

            if (Files.notExists(inventoryPath) || Files.notExists(sidecarPath)) {
                throw new SafeRuntimeException(String.format(
                        "Cannot install the %s inventory into the root of object %s because it does not exist",
                        head, objectId));
            }

            Files.copy(inventoryPath, objectRoot.resolve(inventoryPath.getFileName()), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(sidecarPath, objectRoot.resolve(sidecarPath.getFileName()), StandardCopyOption.REPLACE_EXISTING);

            namasteFile.writeFile(objectRoot);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
