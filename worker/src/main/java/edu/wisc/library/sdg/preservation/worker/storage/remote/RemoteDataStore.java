package edu.wisc.library.sdg.preservation.worker.storage.remote;

import java.nio.file.Path;

/**
 * Interface around a remote datastore
 */
public interface RemoteDataStore {

    /**
     * Writes an object version archive to a remote datastore
     *
     * @param objectId the internal object id
     * @param persistenceVersion the version number
     * @param archive the archive to write
     * @return the key of the object version in the remote store
     */
    String writeObjectVersion(String objectId,
                              String persistenceVersion,
                              Path archive);

    /**
     * Downloads the file at the specified key to the destination. If the file is not ready to be downloaded,
     * a restoration request is executed, and false is returned.
     *
     * @param key the remote key
     * @param destination the location to write the file
     * @return true if the object was downloaded; false if the object was not ready
     */
    boolean download(String key, Path destination);

    /**
     * Indicates if the datastore contains the specified object version. DOES NOT validate contents.
     *
     * @param objectId the internal object id
     * @param persistenceVersion the version number
     * @return true if the object version exists
     */
    boolean containsObjectVersion(String objectId, String persistenceVersion);

    /**
     * Starts the process of restoring an object version from a remote. For example, with Glacier the object
     * version must be first moved back to S3 before the data can be accessed and this process may take hours.
     *
     * <p>If this method is called when the object is already restored, or is in the process of being restored,
     * then nothing happens, and the state is returned.
     *
     * @param key the identifier of the object version in the remote
     * @return true if the object is available and false if it's currently unavailable
     */
    boolean initiateObjectVersionRestoration(String key);

    /**
     * Returns the file size of the object
     *
     * @param key the identifier of the object
     * @return the object's size in bytes
     */
    long fileSize(String key);

}
