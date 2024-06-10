package edu.wisc.library.sdg.preservation.manager.db.repo;

import edu.wisc.library.sdg.preservation.manager.db.mapper.SimplePreservationObjectVersionFileCompositeMapper;
import edu.wisc.library.sdg.preservation.manager.db.model.PreservationObjectVersionFile;
import edu.wisc.library.sdg.preservation.manager.db.model.SimplePreservationObjectVersionFileComposite;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PreservationObjectVersionFileRepository extends CrudRepository<PreservationObjectVersionFile, Long> {

    List<PreservationObjectVersionFile> findAllByObjectVersionId(Long objectVersionId);

    /**
     * If the persistence version is the initial persistence version of a preservation version, then all of the files
     * in that version's state are returned. Otherwise, nothing is returned.
     *
     * When files are returned, it does NOT mean that those files were introduced in that version.
     *
     * @param objectId the internal id of the object
     * @param persistenceVersion the persistence version to filter on
     * @return list of files in the version
     */
    @Query(value = """
        SELECT
          f.object_file_id,
          vf.object_version_file_id,
          f.sha256_digest,
          f.file_size,
          vf.file_path
          FROM preservation_object_version v
          JOIN preservation_object_version_file vf ON v.object_version_id = vf.object_version_id
          JOIN preservation_object_file f ON vf.object_file_id = f.object_file_id
          WHERE v.object_id = :objectId
          AND v.initial_persistence_version = :persistenceVersion""",
            rowMapperClass = SimplePreservationObjectVersionFileCompositeMapper.class)
    List<SimplePreservationObjectVersionFileComposite> findAllNewInVersion(
            @Param("objectId") UUID objectId,
            @Param("persistenceVersion") String persistenceVersion);

    /**
     * Selects the initial persistence version of the preservation version that first introduced the specified file
     * into the object.
     *
     * @param objectFileId the id of the file to lookup
     * @return the persistence version the file was introduced in
     */
    @Query(value = """
        SELECT
          t.initial_persistence_version
          FROM preservation_object_version t
          INNER JOIN
          (SELECT MIN(v.version) min_version, v.object_id FROM preservation_object_version v
            JOIN preservation_object_version_file vf ON v.object_version_id = vf.object_version_id
            JOIN preservation_object_file f ON vf.object_file_id = f.object_file_id
            WHERE f.object_file_id = :objectFileId
            GROUP BY v.object_id) min_v
          ON min_v.min_version = t.version AND min_v.object_id = t.object_id""")
    String findEarliestPersistenceVersion(@Param("objectFileId") Long objectFileId);

    @Query("DELETE FROM preservation_object_version_file WHERE object_version_id = :objectVersionId")
    @Modifying
    void deleteAllByObjectVersionId(@Param("objectVersionId") Long objectVersionId);

}
