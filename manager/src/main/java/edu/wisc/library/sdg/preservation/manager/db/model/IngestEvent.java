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

@Table("ingest_event")
public class IngestEvent implements Event {

    @Id
    @Column("ingest_event_id")
    private Long ingestEventId;

    @Column("external_event_id")
    private UUID externalEventId;

    @Column("ingest_id")
    private Long ingestId;

    @Column("external_object_id")
    private String externalObjectId;

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

    @MappedCollection(idColumn = "ingest_event_id", keyColumn = "ingest_event_id")
    private Set<IngestEventLog> logs;

    public IngestEvent() {
        logs = new HashSet<>();
        externalEventId = UUID.randomUUID();
    }

    @PersistenceCreator
    public IngestEvent(Long ingestEventId,
                       UUID externalEventId,
                       Long ingestId,
                       String externalObjectId,
                       String username,
                       String agent,
                       EventType type,
                       EventOutcome outcome,
                       LocalDateTime eventTimestamp,
                       Set<IngestEventLog> logs) {
        this.ingestEventId = ingestEventId;
        this.externalEventId = externalEventId;
        this.ingestId = ingestId;
        this.externalObjectId = externalObjectId;
        this.username = username;
        this.agent = agent;
        this.type = type;
        this.outcome = outcome;
        this.eventTimestamp = eventTimestamp;
        this.logs = logs == null ? new HashSet<>() : logs;
    }

    public Long getIngestEventId() {
        return ingestEventId;
    }

    public IngestEvent setIngestEventId(Long ingestEventId) {
        this.ingestEventId = ingestEventId;
        return this;
    }

    @Override
    public UUID getExternalEventId() {
        return externalEventId;
    }

    public IngestEvent setExternalEventId(UUID externalEventId) {
        this.externalEventId = externalEventId;
        return this;
    }

    public Long getIngestId() {
        return ingestId;
    }

    public IngestEvent setIngestId(Long ingestId) {
        this.ingestId = ingestId;
        return this;
    }

    public String getExternalObjectId() {
        return externalObjectId;
    }

    public IngestEvent setExternalObjectId(String externalObjectId) {
        this.externalObjectId = externalObjectId;
        return this;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public IngestEvent setUsername(String username) {
        this.username = username;
        return this;
    }

    @Override
    public String getAgent() {
        return agent;
    }

    public IngestEvent setAgent(String agent) {
        this.agent = agent;
        return this;
    }

    @Override
    public EventType getType() {
        return type;
    }

    public IngestEvent setType(EventType type) {
        this.type = type;
        return this;
    }

    @Override
    public EventOutcome getOutcome() {
        return outcome;
    }

    public IngestEvent setOutcome(EventOutcome outcome) {
        this.outcome = outcome;
        return this;
    }

    @Override
    public LocalDateTime getEventTimestamp() {
        return eventTimestamp;
    }

    public IngestEvent setEventTimestamp(LocalDateTime eventTimestamp) {
        this.eventTimestamp = eventTimestamp;
        return this;
    }

    @Override
    public Set<IngestEventLog> getLogs() {
        return logs;
    }

    public IngestEvent setLogs(Set<IngestEventLog> logs) {
        this.logs = logs;
        return this;
    }

    public IngestEvent info(String message, LocalDateTime timestamp) {
        this.logs.add(new IngestEventLog()
                .setLevel(LogLevel.INFO)
                .setMessage(message)
                .setCreatedTimestamp(timestamp));
        return this;
    }

    public IngestEvent warn(String message, LocalDateTime timestamp) {
        this.logs.add(new IngestEventLog()
                .setLevel(LogLevel.WARN)
                .setMessage(message)
                .setCreatedTimestamp(timestamp));
        return this;
    }

    public IngestEvent error(String message, LocalDateTime timestamp) {
        this.logs.add(new IngestEventLog()
                .setLevel(LogLevel.ERROR)
                .setMessage(message)
                .setCreatedTimestamp(timestamp));
        return this;
    }

    public PreservationEvent toPreservationEvent() {
        return new PreservationEvent()
                .setExternalEventId(this.getExternalEventId())
                .setUsername(this.getUsername())
                .setEventTimestamp(this.getEventTimestamp())
                .setOutcome(this.getOutcome())
                .setType(this.getType())
                .setAgent(this.getAgent())
                .setLogs(toPreservationLogs(this.getLogs()));
    }

    private Set<PreservationEventLog> toPreservationLogs(Set<IngestEventLog> logs) {
        if (logs == null) {
            return null;
        }

        return logs.stream().map(log -> {
            return new PreservationEventLog()
                    .setLevel(log.getLevel())
                    .setMessage(log.getMessage())
                    .setCreatedTimestamp(log.getCreatedTimestamp());
        }).collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return "IngestEvent{" +
                "ingestEventId=" + ingestEventId +
                ", externalEventId='" + externalEventId + '\'' +
                ", ingestId=" + ingestId +
                ", externalObjectId=" + externalObjectId +
                ", username='" + username + '\'' +
                ", agent='" + agent + '\'' +
                ", type=" + type +
                ", outcome=" + outcome +
                ", eventTimestamp=" + eventTimestamp +
                ", logs=" + logs +
                '}';
    }

}
