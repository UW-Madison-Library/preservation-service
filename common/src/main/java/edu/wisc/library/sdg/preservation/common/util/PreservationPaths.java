package edu.wisc.library.sdg.preservation.common.util;

import edu.wisc.library.sdg.preservation.common.PreservationConstants;

public final class PreservationPaths {

    private PreservationPaths() {

    }

    /**
     * Returns the path to the object content directory within an OCFL version export.
     *
     * @param objectId the OCFL object id
     * @param persistenceVersion the OCFL persistence version
     * @return path to object content
     */
    public static String archiveObjectContent(String objectId, String persistenceVersion) {
        ArgCheck.notBlank(objectId, "objectId");
        ArgCheck.notBlank(persistenceVersion, "persistenceVersion");
        return String.format("%s/%s/content/%s/",
                UuidUtils.withoutPrefix(objectId), persistenceVersion, PreservationConstants.OCFL_OBJECT_OBJECT_DIR);
    }

}
