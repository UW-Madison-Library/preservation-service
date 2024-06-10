package edu.wisc.library.sdg.preservation.manager.db.repo;

import edu.wisc.library.sdg.preservation.manager.db.model.PreservationObjectFileValidity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PreservationObjectFileValidityRepository extends CrudRepository<PreservationObjectFileValidity, Long> {

    @Query("DELETE FROM preservation_object_file_validity WHERE object_file_id = :objectFileId")
    @Modifying
    void deleteAllByObjectFileId(@Param("objectFileId") Long objectFileId);

    @Query("SELECT fv.* FROM preservation_object_file_validity fv" +
            " INNER JOIN preservation_object_file f ON fv.object_file_id = f.object_file_id" +
            " INNER JOIN preservation_object o ON f.object_id = o.object_id" +
            " WHERE o.vault = :vault" +
            " AND o.external_object_id = :externalObjectId" +
            " AND f.sha256_digest = :sha256Digest" +
            " AND fv.meta_source_id = :metaSourceId")
    List<PreservationObjectFileValidity> findAllByVaultAndExternalObjectIdAndSha256Digest(
            @Param("vault") String vault,
            @Param("externalObjectId") String externalObjectId,
            @Param("sha256Digest") String sha256Digest,
            @Param("metaSourceId") Long metaSourceId);

    List<PreservationObjectFileValidity> findAllByObjectFileId(Long objectFileId);

}
