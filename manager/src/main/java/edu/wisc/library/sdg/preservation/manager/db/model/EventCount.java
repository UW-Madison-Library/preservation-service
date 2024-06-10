package edu.wisc.library.sdg.preservation.manager.db.model;

public class EventCount {

    private final EventType type;
    private final EventOutcome outcome;
    private final int count;

    public EventCount(EventType type, EventOutcome outcome, int count) {
        this.type = type;
        this.outcome = outcome;
        this.count = count;
    }

    public EventType getType() {
        return type;
    }

    public EventOutcome getOutcome() {
        return outcome;
    }

    public int getCount() {
        return count;
    }
}
