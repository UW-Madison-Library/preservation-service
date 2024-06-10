package edu.wisc.library.sdg.preservation.manager.db.repo;

import edu.wisc.library.sdg.preservation.manager.db.model.IngestBatchObject;
import edu.wisc.library.sdg.preservation.manager.db.model.IngestObjectState;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IngestBatchObjectRepository extends CrudRepository<IngestBatchObject, Long> {

    Optional<IngestBatchObject> findOptionalByIngestIdAndExternalObjectId(Long ingestId, String externalObjectId);

    List<IngestBatchObject> findAllByIngestId(Long ingestId);

    List<IngestBatchObject> findAllByIngestIdAndState(Long ingestId, IngestObjectState state);

    long countAllByIngestIdAndState(Long ingestId, IngestObjectState state);

    /**
     * Searches for batch objects that match the supplied criteria. Results are sorted by external object id.
     *
     * @param ingestId the id of the batch
     * @param hasAnalysisErrors if the object has errors, null to ignore
     * @param hasAnalysisWarnings if the object has warnings, null to ignore
     * @param limit the number of results to return
     * @param offset the result offset to start at
     * @return matched objects
     */
    @Query("SELECT bo.* FROM ingest_batch_object bo" +
            " WHERE bo.ingest_id = :ingestId" +
            " AND (:hasAnalysisErrors IS NULL OR bo.has_analysis_errors = :hasAnalysisErrors)" +
            " AND (:hasAnalysisWarnings IS NULL OR bo.has_analysis_warnings = :hasAnalysisWarnings)" +
            " ORDER BY bo.external_object_id ASC" +
            " LIMIT :limit OFFSET :offset")
    List<IngestBatchObject> search(
            @Param("ingestId") Long ingestId,
            @Param("hasAnalysisErrors") Boolean hasAnalysisErrors,
            @Param("hasAnalysisWarnings") Boolean hasAnalysisWarnings,
            @Param("limit") int limit,
            @Param("offset") int offset
    );

    /**
     * Counts the number of batch objects that match the supplied criteria.
     *
     *  @param ingestId the id of the batch
     * @param hasAnalysisErrors if the object has errors, null to ignore
     * @param hasAnalysisWarnings if the object has warnings, null to ignore
     * @return matched objects count
     */
    @Query("SELECT COUNT(bo.*) FROM ingest_batch_object bo" +
            " WHERE bo.ingest_id = :ingestId" +
            " AND (:hasAnalysisErrors IS NULL OR bo.has_analysis_errors = :hasAnalysisErrors)" +
            " AND (:hasAnalysisWarnings IS NULL OR bo.has_analysis_warnings = :hasAnalysisWarnings)")
    long searchCount(
            @Param("ingestId") Long ingestId,
            @Param("hasAnalysisErrors") Boolean hasAnalysisErrors,
            @Param("hasAnalysisWarnings") Boolean hasAnalysisWarnings
    );

}
