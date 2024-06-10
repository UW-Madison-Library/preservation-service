package edu.wisc.library.sdg.preservation.manager.db.model;

import java.util.Collections;
import java.util.Set;

public enum IngestBatchState implements EnumAsShort {

    RECEIVED(1) {
        @Override
        public Set<IngestBatchState> allowedTransitions() {
            return Set.of(ANALYZING, DELETED);
        }
    },

    ANALYZING(2) {
        @Override
        public Set<IngestBatchState> allowedTransitions() {
            return Set.of(ANALYSIS_FAILED, PENDING_REVIEW, DELETED);
        }
    },

    ANALYSIS_FAILED(3),
    PENDING_REVIEW(4) {
        @Override
        public Set<IngestBatchState> allowedTransitions() {
            return Set.of(PENDING_INGESTION, PENDING_REJECTION, DELETED);
        }
    },

    PENDING_INGESTION(5) {
        @Override
        public Set<IngestBatchState> allowedTransitions() {
            return Set.of(INGESTING, PENDING_REVIEW, DELETED);
        }
    },
    PENDING_REJECTION(6) {
        @Override
        public Set<IngestBatchState> allowedTransitions() {
            return Set.of(REJECTING, PENDING_REVIEW, DELETED);
        }
    },

    INGESTING(7) {
        @Override
        public Set<IngestBatchState> allowedTransitions() {
            return Set.of(REPLICATING, INGEST_FAILED, DELETED);
        }
    },
    REPLICATING(8) {
        @Override
        public Set<IngestBatchState> allowedTransitions() {
            return Set.of(COMPLETE, REPLICATION_FAILED, DELETED);
        }
    },
    INGEST_FAILED(9) {
        @Override
        public Set<IngestBatchState> allowedTransitions() {
            return Set.of(PENDING_INGESTION, DELETED);
        }
    },
    REPLICATION_FAILED(10) {
        @Override
        public Set<IngestBatchState> allowedTransitions() {
            return Set.of(REPLICATING, DELETED);
        }
    },
    COMPLETE(11),

    REJECTING(12) {
        @Override
        public Set<IngestBatchState> allowedTransitions() {
            return Set.of(REJECTED, DELETED);
        }
    },
    REJECTED(13),
    DELETED(14);

    private final short intValue;

    IngestBatchState(int intValue) {
        this.intValue = (short) intValue;
    }

    @Override
    public short asShort() {
        return intValue;
    }

    public boolean isTransitionAllowed(IngestBatchState newStatus) {
        return allowedTransitions().contains(newStatus);
    }

    public Set<IngestBatchState> allowedTransitions() {
        return Collections.emptySet();
    }

    public IngestObjectState toObjectState() {
        return IngestObjectState.valueOf(this.name());
    }

}
