package edu.wisc.library.sdg.preservation.manager.db.model;

import java.util.Collections;
import java.util.Set;

public enum IngestObjectState implements EnumAsShort {

    ANALYZING(1) {
        @Override
        public Set<IngestObjectState> allowedTransitions() {
            return Set.of(ANALYSIS_FAILED, PENDING_REVIEW, DELETED);
        }
    },

    ANALYSIS_FAILED(2),
    PENDING_REVIEW(3) {
        @Override
        public Set<IngestObjectState> allowedTransitions() {
            return Set.of(PENDING_INGESTION, PENDING_REJECTION, DELETED);
        }
    },

    PENDING_INGESTION(4) {
        @Override
        public Set<IngestObjectState> allowedTransitions() {
            return Set.of(INGESTING, PENDING_REVIEW, DELETED);
        }
    },
    PENDING_REJECTION(5) {
        @Override
        public Set<IngestObjectState> allowedTransitions() {
            return Set.of(REJECTING, PENDING_REVIEW, DELETED);
        }
    },

    INGESTING(6) {
        @Override
        public Set<IngestObjectState> allowedTransitions() {
            return Set.of(INGESTED, INGEST_FAILED, NO_CHANGE, DELETED);
        }
    },
    INGESTED(7) {
        @Override
        public Set<IngestObjectState> allowedTransitions() {
            return Set.of(REPLICATING);
        }
    },
    NO_CHANGE(8),
    INGEST_FAILED(9) {
        @Override
        public Set<IngestObjectState> allowedTransitions() {
            return Set.of(PENDING_INGESTION, DELETED);
        }
    },

    REPLICATING(10) {
        @Override
        public Set<IngestObjectState> allowedTransitions() {
            return Set.of(REPLICATED, REPLICATION_FAILED, DELETED);
        }
    },
    REPLICATED(11) {
        @Override
        public Set<IngestObjectState> allowedTransitions() {
            return Set.of(COMPLETE, REPLICATION_FAILED, DELETED);
        }
    },
    REPLICATION_FAILED(12) {
        @Override
        public Set<IngestObjectState> allowedTransitions() {
            return Set.of(REPLICATED, INGESTED, DELETED);
        }
    },

    COMPLETE(13) {
        @Override
        public Set<IngestObjectState> allowedTransitions() {
            return Set.of(REPLICATION_FAILED, DELETED);
        }
    },

    REJECTING(14) {
        @Override
        public Set<IngestObjectState> allowedTransitions() {
            return Set.of(REJECTED, DELETED);
        }
    },
    REJECTED(15),
    DELETED(16);

    private final short intValue;

    IngestObjectState(int intValue) {
        this.intValue = (short) intValue;
    }

    @Override
    public short asShort() {
        return intValue;
    }

    public boolean isTransitionAllowed(IngestObjectState newStatus) {
        return allowedTransitions().contains(newStatus);
    }

    public Set<IngestObjectState> allowedTransitions() {
        return Collections.emptySet();
    }

}
