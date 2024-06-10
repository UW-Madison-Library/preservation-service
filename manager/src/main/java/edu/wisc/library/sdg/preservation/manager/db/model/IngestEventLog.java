package edu.wisc.library.sdg.preservation.manager.db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("ingest_event_log")
public class IngestEventLog implements EventLog {

    @Id
    @Column("ingest_event_log_id")
    private Long ingestEventLogId;

    @Column("ingest_event_id")
    private Long ingestEventId;

    @Column("level")
    private LogLevel level;

    @Column("message")
    private String message;

    @Column("created_timestamp")
    private LocalDateTime createdTimestamp;

    public IngestEventLog() {
    }

    @PersistenceCreator
    public IngestEventLog(Long ingestEventLogId,
                          Long ingestEventId,
                          LogLevel level,
                          String message,
                          LocalDateTime createdTimestamp) {
        this.ingestEventLogId = ingestEventLogId;
        this.ingestEventId = ingestEventId;
        this.level = level;
        this.message = message;
        this.createdTimestamp = createdTimestamp;
    }

    public Long getIngestEventLogId() {
        return ingestEventLogId;
    }

    public IngestEventLog setIngestEventLogId(Long ingestEventLogId) {
        this.ingestEventLogId = ingestEventLogId;
        return this;
    }

    public Long getIngestEventId() {
        return ingestEventId;
    }

    public IngestEventLog setIngestEventId(Long ingestEventId) {
        this.ingestEventId = ingestEventId;
        return this;
    }

    @Override
    public LogLevel getLevel() {
        return level;
    }

    public IngestEventLog setLevel(LogLevel level) {
        this.level = level;
        return this;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public IngestEventLog setMessage(String message) {
        this.message = message;
        return this;
    }

    @Override
    public LocalDateTime getCreatedTimestamp() {
        return createdTimestamp;
    }

    public IngestEventLog setCreatedTimestamp(LocalDateTime createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
        return this;
    }

    @Override
    public String toString() {
        return "IngestEventLog{" +
                "ingestEventLogId=" + ingestEventLogId +
                ", ingestEventId=" + ingestEventId +
                ", level=" + level +
                ", message='" + message + '\'' +
                ", createdTimestamp=" + createdTimestamp +
                '}';
    }

}
