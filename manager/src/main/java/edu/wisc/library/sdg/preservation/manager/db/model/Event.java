package edu.wisc.library.sdg.preservation.manager.db.model;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public interface Event {

    UUID getExternalEventId();

    String getUsername();

    String getAgent();

    EventType getType();

    EventOutcome getOutcome();

    LocalDateTime getEventTimestamp();

    Set<? extends EventLog> getLogs();

}
