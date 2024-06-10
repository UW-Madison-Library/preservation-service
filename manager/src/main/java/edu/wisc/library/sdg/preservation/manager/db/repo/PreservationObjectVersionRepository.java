package edu.wisc.library.sdg.preservation.manager.db.repo;

import edu.wisc.library.sdg.preservation.manager.db.model.PreservationObjectVersion;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface PreservationObjectVersionRepository extends CrudRepository<PreservationObjectVersion, Long> {

    Optional<PreservationObjectVersion> findByObjectIdAndVersion(UUID objectId, Integer version);

    @Query("SELECT ov.* FROM preservation_object_version ov" +
            " INNER JOIN preservation_object o ON ov.object_id = o.object_id" +
            " WHERE ov.ingest_id = :ingestId" +
            " AND o.external_object_id = :externalObjectId")
    Optional<PreservationObjectVersion> findByIngestIdAndExternalObjectId(
            @Param("ingestId") Long ingestId,
            @Param("externalObjectId") String externalObjectId);

    @Query("SELECT ov.* FROM preservation_object_version ov" +
            " INNER JOIN preservation_object o ON ov.object_id = o.object_id" +
            " WHERE o.vault = :vault" +
            " AND o.external_object_id = :externalObjectId" +
            " AND ov.created_timestamp =" +
            " (SELECT MAX(created_timestamp) FROM preservation_object_version" +
            " WHERE object_id = ov.object_id AND created_timestamp <= :timestamp)")
    Optional<PreservationObjectVersion> findHeadVersionAtTimestamp(
            @Param("vault") String vault,
            @Param("externalObjectId") String externalObjectId,
            @Param("timestamp") LocalDateTime timestamp);

    @Query("SELECT ov.* FROM preservation_object_version ov" +
            " INNER JOIN preservation_object o ON ov.object_version_id = o.head_object_version_id" +
            " WHERE o.object_id = :objectId")
    Optional<PreservationObjectVersion> findHeadVersionForObjectId(
            @Param("objectId") UUID objectId);

    @Query("SELECT ov.* FROM preservation_object_version ov" +
            " INNER JOIN preservation_object o ON ov.object_id = o.object_id" +
            " WHERE o.vault = :vault" +
            " AND o.external_object_id = :externalObjectId" +
            " AND ov.version = :version")
    Optional<PreservationObjectVersion> findByVaultAndExternalObjectIdAndVersion(
            @Param("vault") String vault,
            @Param("externalObjectId") String externalObjectId,
            @Param("version") Integer version);

    @Query("SELECT ov.* FROM preservation_object_version ov" +
            " INNER JOIN preservation_object o ON ov.object_id = o.object_id" +
            " WHERE o.vault = :vault" +
            " AND o.external_object_id = :externalObjectId" +
            " AND ov.object_version_id = o.head_object_version_id")
    Optional<PreservationObjectVersion> findHeadByVaultAndExternalObjectId(
            @Param("vault") String vault,
            @Param("externalObjectId") String externalObjectId);

    @Query("SELECT SUM(f.file_size) FROM preservation_object_file f" +
            " INNER JOIN preservation_object_version_file vf ON f.object_file_id = vf.object_file_id" +
            " WHERE vf.object_version_id = :objectVersionId")
    Long calculateObjectSize(@Param("objectVersionId") Long objectVersionId);

    @Query("SELECT v.org_name FROM vault v" +
            " INNER JOIN preservation_object o ON v.name = o.vault" +
            " INNER JOIN preservation_object_version ov ON o.object_id = ov.object_id" +
            " WHERE ov.object_version_id = :objectVersionId")
    Optional<String> lookupOrgNameByObjectVersionId(@Param("objectVersionId") Long objectVersionId);

}
