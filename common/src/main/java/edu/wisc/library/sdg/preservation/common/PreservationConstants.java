package edu.wisc.library.sdg.preservation.common;

public final class PreservationConstants {

    private PreservationConstants() {

    }

    /**
     * The directory within an OCFL object where Preservation system files are stored
     */
    public static final String OCFL_OBJECT_SYSTEM_DIR = "system";

    /**
     * The name of the directory within an OCFL object where system technical metadata files are stored
     */
    public static final String OCFL_OBJECT_TECHMD_DIR_NAME = "techmd";

    /**
     * The directory path within an OCFL object where Preservation system technical metadata files are stored
     */
    public static final String OCFL_OBJECT_TECHMD_PATH = OCFL_OBJECT_SYSTEM_DIR + "/" + OCFL_OBJECT_TECHMD_DIR_NAME;

    /**
     * The name of the directory within the OCFL object where FITS technical metadata files are stored
     */
    public static final String OCFL_OBJECT_FITS_DIR_NAME = "fits";

    /**
     * The directory path within an OCFL object where Preservation FITS technical metadata files are stored
     */
    public static final String OCFL_OBJECT_FITS_PATH = OCFL_OBJECT_TECHMD_PATH + "/" + OCFL_OBJECT_FITS_DIR_NAME;

    /**
     * The directory within an OCFL object where object content files are stored
     */
    public static final String OCFL_OBJECT_OBJECT_DIR = "object";

    /**
     * The file within OCFL_OBJECT_SYSTEM_DIR that contains Preservation system metadata about the object
     */
    public static final String SYSTEM_METADATA_FILENAME = "metadata.json";

    /**
     * Full path to the system metadata file within the OCFL object
     */
    public static final String SYSTEM_METADATA_PATH = OCFL_OBJECT_SYSTEM_DIR + "/" + SYSTEM_METADATA_FILENAME;

    /**
     * The name of an object's PREMIS file
     */
    public static final String OBJECT_PREMIS_FILE = "premis.xml";

    /**
     * Full path to the object PREMIS file within the OCFL object's system directory
     */
    public static final String SYSTEM_METADATA_PREMIS_PATH = OCFL_OBJECT_SYSTEM_DIR + "/" + OBJECT_PREMIS_FILE;

    /**
     * The file within an OCFL version archive that describes the contents of the archive
     */
    public static final String ARCHIVE_OCFL_CONTENT_FILENAME = "ocfl-content.txt";

    /**
     * The name of the optional file in the root of a bag's data directory that contains file metadata
     */
    public static final String BAG_FILE_META = "file-meta.csv";

    /**
     * String that indicates that a user, rather than an automated tool, was the source of the metadata
     */
    public static final String USER_SOURCE = "USER";

    /**
     * The name of the tag in the DIP BagIt metadata that indicates the vault the objects in the bag are from
     */
    public static final String DIP_BAGIT_VAULT_TAG_NAME = "preservation-system-vault";

    /**
     * The string format template used to construct a DIP bag name. It expects a single argument, the job id.
     */
    public static final String DIP_BAG_NAME_TMPL = "dip-%s";

    /**
     * The string format template used to construct a DIP bag zip. It expects a single argument, the job id.
     */
    public static final String DIP_BAG_ZIP_TMPL = DIP_BAG_NAME_TMPL + ".zip";

}
