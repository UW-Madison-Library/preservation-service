package edu.wisc.library.sdg.preservation.worker.validation;

import edu.wisc.library.sdg.preservation.common.exception.ValidationException;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ObjectFile;

import java.util.List;

public interface GlacierVersionValidator {
    /**
     * Invokes a lambda function to validate an object version archive stored in Glacier. If the archive is invalid,
     * a ValidationException is thrown.
     *
     * @param objectId           the internal object id
     * @param persistenceVersion the OCFL version
     * @param key                the key for the archive in Glacier
     * @param sha256Digest       the expected digest of the archive
     * @param expectedFiles      the expected contents of the archive
     * @throws ValidationException if the archive is invalid
     */
    void validate(String objectId,
                  String persistenceVersion,
                  String key,
                  String sha256Digest,
                  List<ObjectFile> expectedFiles);
}
