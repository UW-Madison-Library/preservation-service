package edu.wisc.library.sdg.preservation.manager.vault;

import edu.wisc.library.sdg.preservation.common.exception.NotFoundException;
import edu.wisc.library.sdg.preservation.common.util.ArgCheck;
import edu.wisc.library.sdg.preservation.common.util.Time;
import edu.wisc.library.sdg.preservation.manager.db.model.Vault;
import edu.wisc.library.sdg.preservation.manager.db.repo.VaultRepository;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class is responsible for updating vault statistics
 */
@Component
@Profile({"!itest"})
public class CachedVaultStatsUpdater implements VaultStatsUpdater {

    private static final Logger LOG = LoggerFactory.getLogger(CachedVaultStatsUpdater.class);

    private final VaultRepository vaultRepo;
    private final Map<String, Stats> vaultStats;
    private final Lock updateLock;

    public CachedVaultStatsUpdater(VaultRepository vaultRepo) {
        this.vaultRepo = vaultRepo;
        this.vaultStats = new ConcurrentHashMap<>();
        this.updateLock = new ReentrantLock();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(String vault, int addObjects, long addStorageBytes) {
        ArgCheck.notNull(vault, "vault");

        vaultStats.computeIfAbsent(vault, k -> new Stats()).add(addObjects, addStorageBytes);
    }

    /**
     * Adds the cached updates to the database
     */
    @Scheduled(fixedDelayString = "${app.schedule.vault.cache.update.sec}", timeUnit = TimeUnit.SECONDS)
    @Timed(value = "runTask")
    public void updateVaultStats() {
        if (updateLock.tryLock()) {
            try {
                LOG.debug("Updating vault stats");
                var success = new AtomicBoolean(true);

                vaultStats.forEach((name, stats) -> {
                    if (stats.hasUpdates()) {
                        LOG.debug("Updating vault stats for {}", name);
                        try {
                            var vault = findVault(name);
                            stats.updateVault(vault);
                            vaultRepo.save(vault.setUpdatedTimestamp(Time.now()));
                        } catch (Exception e) {
                            success.set(false);
                            LOG.error("Failed to update stats for vault {}", name);
                        }
                    }
                });

                if (!success.get()) {
                    throw new RuntimeException("Encountered failures while updating vault stats");
                }
            } finally {
                updateLock.unlock();
            }
        }
    }

    /**
     * Recalculates all of the vault stats based on what's in the db
     */
    @Scheduled(cron = "${app.schedule.vault.cache.recalculate.cron}")
    @Timed(value = "runTask")
    public void recalculateVaultStates() throws InterruptedException {
        if (updateLock.tryLock(1, TimeUnit.MINUTES)) {
            try {
                LOG.info("Recalculating vault stats");
                var success = new AtomicBoolean(true);

                vaultRepo.findAllNames().forEach(name -> {
                    LOG.debug("Recalculating vault stats for {}", name);

                    try {
                        var storage = vaultRepo.sumObjectBytesInMbByVault(name);
                        var objects = vaultRepo.countAllObjectsByVault(name).orElse(0L);
                        var vault = findVault(name);
                        vault.setObjects(objects)
                                .setStorageMb(storage.orElse(0L))
                                .setUpdatedTimestamp(Time.now());
                        vaultRepo.save(vault);
                    } catch (Exception e) {
                        success.set(false);
                        LOG.error("Failed to update stats for vault {}", name);
                    }

                    // If objects were added between the time the stats were recalculated and the cached stats were cleared
                    // then the updates will be lost until the time the stats are recalculated next
                    var stats = vaultStats.get(name);
                    if (stats != null) {
                        stats.clear();
                    }
                });

                if (!success.get()) {
                    throw new RuntimeException("Encountered failures while recalculating vault stats");
                }
            } finally {
                updateLock.unlock();
            }
        }
    }

    private Vault findVault(String name) {
        return vaultRepo.findById(name)
                .orElseThrow(() -> new NotFoundException(String.format("Vault %s was not found", name)));
    }

    private static class Stats {
        private long addObjects;
        private long addStorageBytes;

        public Stats() {
            this.addObjects = 0L;
            this.addStorageBytes = 0L;
        }

        synchronized void add(long objects, long storageMbs) {
            this.addObjects += objects;
            this.addStorageBytes += storageMbs;
        }

        synchronized void clear() {
            addObjects = 0L;
            addStorageBytes = 0L;
        }

        synchronized boolean hasUpdates() {
            return addObjects > 0 || addStorageBytes > 0;
        }

        synchronized void updateVault(Vault vault) {
            vault.setObjects(vault.getObjects() + addObjects)
                    .setStorageMb(vault.getStorageMb() + (addStorageBytes / 1048576));
            clear();
        }
    }

}
