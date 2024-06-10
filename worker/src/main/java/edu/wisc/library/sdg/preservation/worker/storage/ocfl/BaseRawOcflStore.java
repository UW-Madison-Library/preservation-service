package edu.wisc.library.sdg.preservation.worker.storage.ocfl;

import io.ocfl.api.model.DigestAlgorithm;
import io.ocfl.core.ObjectPaths;
import io.ocfl.core.inventory.InventoryMapper;
import io.ocfl.core.model.Inventory;
import io.ocfl.core.validation.InventoryValidator;

import java.nio.file.Path;

public abstract class BaseRawOcflStore implements RawOcflStore {

    private final InventoryMapper inventoryMapper;

    public BaseRawOcflStore() {
        this.inventoryMapper = InventoryMapper.defaultMapper();
    }

    protected void validateVersion(Path versionDir) {
        var inventory = parseVersionInventory(versionDir);
        InventoryValidator.validateShallow(inventory);
    }

    private Inventory parseVersionInventory(Path versionDir) {
        return inventoryMapper.read(versionDir.toString(), DigestAlgorithm.sha256, ObjectPaths.inventoryPath(versionDir));
    }

}
