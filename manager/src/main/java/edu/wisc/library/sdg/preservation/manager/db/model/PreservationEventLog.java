package edu.wisc.library.sdg.preservation.manager.db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("preservation_event_log")
public class PreservationEventLog implements EventLog {

    @Id
    @Column("event_log_id")
    private Long eventLogId;

    @Column("event_id")
    private Long eventId;

    @Column("level")
    private LogLevel level;

    @Column("message")
    private String message;

    @Column("created_timestamp")
    private LocalDateTime createdTimestamp;

    public PreservationEventLog() {
    }

    @PersistenceCreator
    public PreservationEventLog(Long eventLogId,
                                Long eventId,
                                LogLevel level,
                                String message,
                                LocalDateTime createdTimestamp) {
        this.eventLogId = eventLogId;
        this.eventId = eventId;
        this.level = level;
        this.message = message;
        this.createdTimestamp = createdTimestamp;
    }

    public Long getEventLogId() {
        return eventLogId;
    }

    public PreservationEventLog setEventLogId(Long eventLogId) {
        this.eventLogId = eventLogId;
        return this;
    }

    public Long getEventId() {
        return eventId;
    }

    public PreservationEventLog setEventId(Long eventId) {
        this.eventId = eventId;
        return this;
    }

    @Override
    public LogLevel getLevel() {
        return level;
    }

    public PreservationEventLog setLevel(LogLevel level) {
        this.level = level;
        return this;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public PreservationEventLog setMessage(String message) {
        this.message = message;
        return this;
    }

    @Override
    public LocalDateTime getCreatedTimestamp() {
        return createdTimestamp;
    }

    public PreservationEventLog setCreatedTimestamp(LocalDateTime createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
        return this;
    }

    @Override
    public String toString() {
        return "PreservationEventLog{" +
                "eventLogId=" + eventLogId +
                ", eventId=" + eventId +
                ", level=" + level +
                ", message='" + message + '\'' +
                ", createdTimestamp=" + createdTimestamp +
                '}';
    }
}
