package edu.wisc.library.sdg.preservation.manager.db.model;

import java.util.Collections;
import java.util.Set;

public enum JobState implements EnumAsShort {

    PENDING(1) {
        @Override
        public Set<JobState> allowedTransitions() {
            return Set.of(EXECUTING, FAILED, CANCELLED);
        }
    },
    EXECUTING(2) {
        @Override
        public Set<JobState> allowedTransitions() {
            return Set.of(FAILED, COMPLETE, PENDING);
        }
    },
    FAILED(3) {
        @Override
        public Set<JobState> allowedTransitions() {
            // Allow for retrying
            return Set.of(PENDING);
        }
    },
    COMPLETE(4) {
        @Override
        public Set<JobState> allowedTransitions() {
            return Collections.emptySet();
        }
    },
    CANCELLED(5) {
        @Override
        public Set<JobState> allowedTransitions() {
            // Allow for retrying
            return Set.of(PENDING);
        }
    };

    private final short intValue;

    JobState(int intValue) {
        this.intValue = (short) intValue;
    }

    public static JobState fromShort(short v) {
        for (var value : values()) {
            if (v == value.intValue) {
                return value;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + v);
    }

    @Override
    public short asShort() {
        return intValue;
    }

    public boolean isTransitionAllowed(JobState newStatus) {
        return allowedTransitions().contains(newStatus);
    }

    public abstract Set<JobState> allowedTransitions();

}
