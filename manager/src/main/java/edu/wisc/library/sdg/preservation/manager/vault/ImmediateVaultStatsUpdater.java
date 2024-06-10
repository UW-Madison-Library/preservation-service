package edu.wisc.library.sdg.preservation.manager.vault;

import edu.wisc.library.sdg.preservation.common.exception.NotFoundException;
import edu.wisc.library.sdg.preservation.common.util.Time;
import edu.wisc.library.sdg.preservation.manager.db.model.Vault;
import edu.wisc.library.sdg.preservation.manager.db.repo.VaultRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Vault stats updater that immediately updates the vault stats. This should only be used for TEST purposes.
 */
@Component
@Profile({"itest"})
public class ImmediateVaultStatsUpdater implements VaultStatsUpdater {

    private final VaultRepository vaultRepo;

    public ImmediateVaultStatsUpdater(VaultRepository vaultRepo) {
        this.vaultRepo = vaultRepo;
    }

    @Override
    public void update(String name, int addObjects, long addStorageBytes) {
        var vault = findVault(name);
        vault.setObjects(vault.getObjects() + addObjects)
                .setStorageMb(vault.getStorageMb() + (addStorageBytes / 1048576))
                .setUpdatedTimestamp(Time.now());
        vaultRepo.save(vault);
    }

    private Vault findVault(String name) {
        return vaultRepo.findById(name)
                .orElseThrow(() -> new NotFoundException(String.format("Vault %s was not found", name)));
    }

}
