package edu.wisc.library.sdg.preservation.manager.vault;

public interface VaultStatsUpdater {

    /**
     * Updates vault object stats
     *
     * @param vault the name of the vault to update
     * @param addObjects the number of objects to add
     * @param addStorageBytes the number of bytes to add
     */
    void update(String vault, int addObjects, long addStorageBytes);

}
