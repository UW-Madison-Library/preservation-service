package edu.wisc.library.sdg.preservation.manager.itest;

import edu.wisc.library.sdg.preservation.manager.client.internal.model.DataStore;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.IngestBatchState;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.IngestObjectState;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.StorageProblemType;

public class InternalAlias {

    public static final IngestBatchState INTERNAL_ANALYZING = IngestBatchState.ANALYZING;
    public static final IngestBatchState INTERNAL_PENDING_REVIEW = IngestBatchState.PENDING_REVIEW;
    public static final IngestBatchState INTERNAL_INGESTING = IngestBatchState.INGESTING;
    public static final IngestBatchState INTERNAL_INGEST_FAILED = IngestBatchState.INGEST_FAILED;
    public static final IngestBatchState INTERNAL_REJECTING = IngestBatchState.REJECTING;
    public static final IngestBatchState INTERNAL_REJECTED = IngestBatchState.REJECTED;
    public static final IngestBatchState INTERNAL_REPLICATING = IngestBatchState.REPLICATING;
    public static final IngestBatchState INTERNAL_REPLICATION_FAILED = IngestBatchState.REPLICATION_FAILED;
    public static final IngestBatchState INTERNAL_ANALYSIS_FAILED = IngestBatchState.ANALYSIS_FAILED;

    public static final IngestObjectState INTERNAL_OBJ_ANALYSIS_FAILED = IngestObjectState.ANALYSIS_FAILED;
    public static final IngestObjectState INTERNAL_OBJ_PENDING_REVIEW = IngestObjectState.PENDING_REVIEW;
    public static final IngestObjectState INTERNAL_OBJ_INGESTING = IngestObjectState.INGESTING;
    public static final IngestObjectState INTERNAL_OBJ_INGESTED = IngestObjectState.INGESTED;
    public static final IngestObjectState INTERNAL_OBJ_INGEST_FAILED = IngestObjectState.INGEST_FAILED;
    public static final IngestObjectState INTERNAL_OBJ_REPLICATING = IngestObjectState.REPLICATING;
    public static final IngestObjectState INTERNAL_OBJ_REPLICATION_FAILED = IngestObjectState.REPLICATION_FAILED;

    public static final StorageProblemType PROBLEM_MISSING = StorageProblemType.MISSING;
    public static final StorageProblemType PROBLEM_CORRUPT = StorageProblemType.CORRUPT;
    public static final StorageProblemType PROBLEM_NONE = StorageProblemType.NONE;

    public static final DataStore GLACIER = DataStore.GLACIER;
}
