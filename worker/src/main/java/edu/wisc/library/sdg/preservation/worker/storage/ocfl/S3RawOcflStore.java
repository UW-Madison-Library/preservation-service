package edu.wisc.library.sdg.preservation.worker.storage.ocfl;

import edu.wisc.library.sdg.preservation.common.exception.SafeRuntimeException;
import edu.wisc.library.sdg.preservation.common.util.ArgCheck;
import io.micrometer.core.annotation.Timed;
import io.ocfl.api.model.VersionNum;
import io.ocfl.core.ObjectPaths;
import io.ocfl.core.extension.storage.layout.OcflStorageLayoutExtension;
import io.ocfl.core.storage.cloud.CloudClient;
import io.ocfl.core.util.FileUtil;
import io.ocfl.core.util.NamasteTypeFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Implementation for S3 based OCFL repos
 */
public class S3RawOcflStore extends BaseRawOcflStore {

    private static final Logger LOG = LoggerFactory.getLogger(S3RawOcflStore.class);

    private static final String STORE = "IbmCos";

    // technically speaking this is only accurate so long as we use sha256
    private static final String SIDECAR_SUFFIX = ".sha256";

    private final CloudClient cloudClient;
    private final OcflStorageLayoutExtension ocflLayout;
    private final NamasteTypeFile namasteFile;

    public S3RawOcflStore(CloudClient cloudClient,
                          OcflStorageLayoutExtension ocflLayout) {
        this.cloudClient = cloudClient;
        this.ocflLayout = ocflLayout;
        // This will cause all restored objects to have a NAMASTE file of "0=ocfl_object_1.1".
        // This will need to be updated everytime a new OCFL specification is used.
        this.namasteFile = new NamasteTypeFile("ocfl_object_1.1");
    }

    @Override
    @Timed(value = "ocfl.raw", extraTags = {"store", STORE})
    public void installVersion(String objectId, String versionNum, Path versionDir) {
        ArgCheck.notBlank(objectId, "objectId");
        ArgCheck.notBlank(versionNum, "versionNum");
        ArgCheck.notNull(versionDir, "versionDir");

        validateVersion(versionDir);
        var objectRoot = ocflLayout.mapObjectId(objectId);
        var destinationPrefix = FileUtil.pathJoinFailEmpty(objectRoot, versionNum);

        LOG.info("Cleaning up any existing files for object <{}> version <{}>", objectId, versionNum);

        // cleanup anything that may remain of a version before installing the restored copy
        try {
            cloudClient.deletePath(destinationPrefix);
        } catch (RuntimeException e) {
            throw new SafeRuntimeException(String.format(
                    "Version cleanup failed when restoring object %s version %s. This could result in a corrupt OCFL object",
                    objectId, versionNum), e);
        }

        LOG.info("Copying {} into object <{}> version <{}> at {}", versionDir, objectId, versionNum, destinationPrefix);

        try (var files = Files.walk(versionDir)) {
            files.filter(Files::isRegularFile).forEach(file -> {
                var relative = versionDir.relativize(file);
                var destination = FileUtil.pathJoinFailEmpty(destinationPrefix, relative.toString());
                LOG.debug("Copying {} to {}", file, destination);
                cloudClient.uploadFile(file, destination);
            });
        } catch (IOException e) {
            throw new SafeRuntimeException(String.format(
                    "Failed to copy %s to %s. This could result in a corrupt OCFL object.", versionDir, destinationPrefix), e);
        }
    }

    @Override
    @Timed(value = "ocfl.raw", extraTags = {"store", STORE})
    public void installRootInventory(String objectId) {
        ArgCheck.notBlank(objectId, "objectId");

        var objectRoot = ocflLayout.mapObjectId(objectId);

        var result = cloudClient.listDirectory(objectRoot);

        var head = result.getDirectories().stream()
                .filter(dir -> dir.getName().startsWith("v"))
                .map(dir -> VersionNum.fromString(dir.getName()))
                .max(VersionNum::compareTo)
                .orElseThrow(() -> new SafeRuntimeException(String.format(
                        "No OCFL versions found for object %s at %s", objectId, objectRoot)));

        LOG.info("Installing the {} inventory into the root of object {}", head, objectId);

        var versionPath = FileUtil.pathJoinFailEmpty(objectRoot, head.toString());
        var srcInventoryPath = ObjectPaths.inventoryPath(versionPath);
        var srcSidecarPath = srcInventoryPath + SIDECAR_SUFFIX;

        var dstInventoryPath = ObjectPaths.inventoryPath(objectRoot);
        var dstSidecarPath = dstInventoryPath + SIDECAR_SUFFIX;

        cloudClient.copyObject(srcInventoryPath, dstInventoryPath);
        cloudClient.copyObject(srcSidecarPath, dstSidecarPath);
        cloudClient.uploadBytes(FileUtil.pathJoinFailEmpty(objectRoot, namasteFile.fileName()),
                namasteFile.fileContent().getBytes(StandardCharsets.UTF_8), "text/plain");
    }
}
