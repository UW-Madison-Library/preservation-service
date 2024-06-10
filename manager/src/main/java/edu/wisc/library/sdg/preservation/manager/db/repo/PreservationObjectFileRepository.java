package edu.wisc.library.sdg.preservation.manager.db.repo;

import edu.wisc.library.sdg.preservation.manager.db.model.PreservationObjectFile;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface PreservationObjectFileRepository extends CrudRepository<PreservationObjectFile, Long> {

    Optional<PreservationObjectFile> findOptionalByObjectIdAndSha256Digest(UUID objectId,
                                                                           String sha256Digest);

    @Query("DELETE FROM preservation_object_file f WHERE f.object_id = :objectId AND NOT EXISTS" +
            " (SELECT 1 FROM preservation_object_version_file vf WHERE vf.object_file_id = f.object_file_id)")
    @Modifying
    void deleteAllUnused(@Param("objectId") UUID objectId);

}
