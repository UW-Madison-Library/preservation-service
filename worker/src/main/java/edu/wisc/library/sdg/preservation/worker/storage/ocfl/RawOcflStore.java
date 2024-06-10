package edu.wisc.library.sdg.preservation.worker.storage.ocfl;

import java.nio.file.Path;

/**
 * This is a "raw" facade over an OCFL repo. It allows files in an OCFL repo to be directly manipulated without
 * going through an OCFL client
 */
public interface RawOcflStore {

    /**
     * Copies the specified version into an existing OCFL object. If there is an existing version, this version is
     * overlayed on top of it. The existing version is NOT deleted first.
     *
     * After installing the version, the object is NOT validated and the object's root inventory is NOT touched
     *
     * @param objectId the OCFL object ID
     * @param versionNum the OCFL version number of the version
     * @param versionDir a valid OCFL version
     */
    void installVersion(String objectId, String versionNum, Path versionDir);

    /**
     * Examines the specified OCFL object, and if the object's root inventory is NOT a copy of the most recent
     * inventory version, then the most recent inventory is copied into the object root.
     *
     * @param objectId the OCFL object ID
     */
    void installRootInventory(String objectId);

}
