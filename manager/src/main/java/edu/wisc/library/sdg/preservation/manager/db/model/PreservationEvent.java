package edu.wisc.library.sdg.preservation.manager.db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Table("preservation_event")
public class PreservationEvent implements Event {

    @Id
    @Column("event_id")
    private Long eventId;

    @Column("external_event_id")
    private UUID externalEventId;

    @Column("object_id")
    private UUID objectId;

    @Column("username")
    private String username;

    @Column("agent")
    private String agent;

    @Column("type")
    private EventType type;

    @Column("outcome")
    private EventOutcome outcome;

    @Column("event_timestamp")
    private LocalDateTime eventTimestamp;

    @MappedCollection(idColumn = "event_id", keyColumn = "event_id")
    private Set<PreservationEventLog> logs;

    public PreservationEvent() {
        logs = new HashSet<>();
        externalEventId = UUID.randomUUID();
    }

    @PersistenceCreator
    public PreservationEvent(Long eventId,
                             UUID externalEventId,
                             UUID objectId,
                             String username,
                             String agent,
                             EventType type,
                             EventOutcome outcome,
                             LocalDateTime eventTimestamp,
                             Set<PreservationEventLog> logs) {
        this.eventId = eventId;
        this.externalEventId = externalEventId;
        this.objectId = objectId;
        this.username = username;
        this.agent = agent;
        this.type = type;
        this.outcome = outcome;
        this.eventTimestamp = eventTimestamp;
        this.logs = logs == null ? new HashSet<>() : logs;
    }

    public Long getEventId() {
        return eventId;
    }

    public PreservationEvent setEventId(Long eventId) {
        this.eventId = eventId;
        return this;
    }

    @Override
    public UUID getExternalEventId() {
        return externalEventId;
    }

    public PreservationEvent setExternalEventId(UUID externalEventId) {
        this.externalEventId = externalEventId;
        return this;
    }

    public UUID getObjectId() {
        return objectId;
    }

    public PreservationEvent setObjectId(UUID objectId) {
        this.objectId = objectId;
        return this;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public PreservationEvent setUsername(String username) {
        this.username = username;
        return this;
    }

    @Override
    public String getAgent() {
        return agent;
    }

    public PreservationEvent setAgent(String agent) {
        this.agent = agent;
        return this;
    }

    @Override
    public EventType getType() {
        return type;
    }

    public PreservationEvent setType(EventType type) {
        this.type = type;
        return this;
    }

    @Override
    public EventOutcome getOutcome() {
        return outcome;
    }

    public PreservationEvent setOutcome(EventOutcome outcome) {
        this.outcome = outcome;
        return this;
    }

    @Override
    public LocalDateTime getEventTimestamp() {
        return eventTimestamp;
    }

    public PreservationEvent setEventTimestamp(LocalDateTime eventTimestamp) {
        this.eventTimestamp = eventTimestamp;
        return this;
    }

    @Override
    public Set<PreservationEventLog> getLogs() {
        return logs;
    }

    public PreservationEvent setLogs(Set<PreservationEventLog> logs) {
        this.logs = logs;
        return this;
    }

    public PreservationEvent info(String message, LocalDateTime timestamp) {
        this.logs.add(new PreservationEventLog()
                .setLevel(LogLevel.INFO)
                .setMessage(message)
                .setCreatedTimestamp(timestamp));
        return this;
    }

    public PreservationEvent warn(String message, LocalDateTime timestamp) {
        this.logs.add(new PreservationEventLog()
                .setLevel(LogLevel.WARN)
                .setMessage(message)
                .setCreatedTimestamp(timestamp));
        return this;
    }

    public PreservationEvent error(String message, LocalDateTime timestamp) {
        this.logs.add(new PreservationEventLog()
                .setLevel(LogLevel.ERROR)
                .setMessage(message)
                .setCreatedTimestamp(timestamp));
        return this;
    }

    public IngestEvent toIngestEvent() {
        return new IngestEvent()
                .setExternalEventId(this.getExternalEventId())
                .setUsername(this.getUsername())
                .setEventTimestamp(this.getEventTimestamp())
                .setOutcome(this.getOutcome())
                .setType(this.getType())
                .setAgent(this.getAgent())
                .setLogs(toIngestLogs(this.getLogs()));
    }

    private Set<IngestEventLog> toIngestLogs(Set<PreservationEventLog> logs) {
        if (logs == null) {
            return null;
        }

        return logs.stream().map(log -> {
            return new IngestEventLog()
                    .setLevel(log.getLevel())
                    .setMessage(log.getMessage())
                    .setCreatedTimestamp(log.getCreatedTimestamp());
        }).collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return "PreservationEvent{" +
                "eventId=" + eventId +
                ", externalEventId='" + externalEventId + '\'' +
                ", objectId='" + objectId + '\'' +
                ", username='" + username + '\'' +
                ", agent='" + agent + '\'' +
                ", type=" + type +
                ", outcome=" + outcome +
                ", eventTimestamp=" + eventTimestamp +
                ", logs=" + logs +
                '}';
    }

}
