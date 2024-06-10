package edu.wisc.library.sdg.preservation.manager.db.repo;

import edu.wisc.library.sdg.preservation.manager.db.model.IngestBatchObjectFile;
import edu.wisc.library.sdg.preservation.manager.db.model.ObjectFile;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IngestBatchObjectFileRepository extends CrudRepository<IngestBatchObjectFile, Long> {

    @Query("""
            SELECT f.* FROM
             ingest_batch_object o INNER JOIN ingest_batch_object_file f ON o.ingest_object_id = f.ingest_object_id
             WHERE o.ingest_id = :ingestId""")
    List<ObjectFile> findAllByIngestId(@Param("ingestId") Long ingestId);

    @Query("""
            SELECT f.* FROM
             ingest_batch_object o INNER JOIN ingest_batch_object_file f ON o.ingest_object_id = f.ingest_object_id
             WHERE o.ingest_id = :ingestId AND o.external_object_id = :externalObjectId""")
    List<IngestBatchObjectFile> findAllByIngestIdAndExternalObjectId(
            @Param("ingestId") Long ingestId,
            @Param("externalObjectId") String externalObjectId);

    @Query("""
            SELECT f.* FROM
              ingest_batch_object o INNER JOIN ingest_batch_object_file f ON o.ingest_object_id = f.ingest_object_id
              WHERE o.ingest_id = :ingestId
              AND o.external_object_id = :externalObjectId
              AND f.file_path = :filePath""")
    Optional<IngestBatchObjectFile> findByIngestIdAndExternalObjectIdAndFilePath(
            @Param("ingestId") Long ingestId,
            @Param("externalObjectId") String externalObjectId,
            @Param("filePath") String filePath);

}
