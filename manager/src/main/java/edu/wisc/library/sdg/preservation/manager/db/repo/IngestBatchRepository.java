package edu.wisc.library.sdg.preservation.manager.db.repo;

import edu.wisc.library.sdg.preservation.manager.db.model.IngestBatch;
import edu.wisc.library.sdg.preservation.manager.db.model.IngestBatchState;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IngestBatchRepository extends CrudRepository<IngestBatch, Long> {

    @Query("SELECT ingest_id FROM ingest_batch" +
            " WHERE state = :state")
    List<Long> findAllBatchIdsInState(@Param("state") IngestBatchState state);

    List<IngestBatch> findAllByOrgName(String orgName);

    /**
     * Searches for batches that match the supplied criteria. Results are returned in reverse chronological order.
     *
     * The query is a little janky to make it work in this context. Vaults and states must always be lists that
     * contain at least one item, even if you do not want to filter by state of vault. This is so that the generated
     * SQL is syntactically valid. If you do not want to filter by vault or state, then set hasVaults or hasStates
     * to false.
     *
     * @param orgName the org name to filter on, may be null
     * @param hasVaults true, if there are vaults to filter on
     * @param vaults the vaults to filter on, MUST contain one or more items
     * @param hasStates true, if there are states to filter on
     * @param states the states to filter on, MUST contain one or more items
     * @param limit the number of results to return
     * @param offset the result offset to start at
     * @return matched batches
     */
    @Query("SELECT * FROM ingest_batch" +
            " WHERE (:orgName IS NULL OR org_name = :orgName)" +
            " AND (:hasVaults IS FALSE OR vault IN (:vaults))" +
            " AND (:hasStates IS FALSE OR state IN (:states))" +
            " ORDER BY updated_timestamp DESC" +
            " LIMIT :limit OFFSET :offset")
    List<IngestBatch> search(
            @Param("orgName") String orgName,
            @Param("hasVaults") boolean hasVaults,
            @Param("vaults") List<String> vaults,
            @Param("hasStates") boolean hasStates,
            @Param("states") List<Short> states,
            @Param("limit") int limit,
            @Param("offset") int offset
    );

    /**
     * Counts the number of batches that match the supplied criteria.
     *
     * The query is a little janky to make it work in this context. Vaults and states must always be lists that
     * contain at least one item, even if you do not want to filter by state of vault. This is so that the generated
     * SQL is syntactically valid. If you do not want to filter by vault or state, then set hasVaults or hasStates
     * to false.
     *
     * @param orgName the org name to filter on, may be null
     * @param hasVaults true, if there are vaults to filter on
     * @param vaults the vaults to filter on, MUST contain one or more items
     * @param hasStates true, if there are states to filter on
     * @param states the states to filter on, MUST contain one or more items
     * @return matched batches
     */
    @Query("SELECT COUNT(*) FROM ingest_batch" +
            " WHERE (:orgName IS NULL OR org_name = :orgName)" +
            " AND (:hasVaults IS FALSE OR vault IN (:vaults))" +
            " AND (:hasStates IS FALSE OR state IN (:states))")
    long searchCount(
            @Param("orgName") String orgName,
            @Param("hasVaults") boolean hasVaults,
            @Param("vaults") List<String> vaults,
            @Param("hasStates") boolean hasStates,
            @Param("states") List<Short> states
    );

    Optional<IngestBatch> findByFilePath(String filePath);

}
