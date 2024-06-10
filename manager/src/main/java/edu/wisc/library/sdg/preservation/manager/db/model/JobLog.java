package edu.wisc.library.sdg.preservation.manager.db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("job_log")
public class JobLog {

    @Id
    @Column("job_log_id")
    private Long jobLogId;

    @Column("job_id")
    private Long jobId;

    @Column("level")
    private LogLevel level;

    @Column("message")
    private String message;

    @Column("created_timestamp")
    private LocalDateTime createdTimestamp;

    public JobLog() {
    }

    @PersistenceCreator
    public JobLog(Long jobLogId, Long jobId, LogLevel level, String message, LocalDateTime createdTimestamp) {
        this.jobLogId = jobLogId;
        this.jobId = jobId;
        this.level = level;
        this.message = message;
        this.createdTimestamp = createdTimestamp;
    }

    public Long getJobLogId() {
        return jobLogId;
    }

    public JobLog setJobLogId(Long jobLogId) {
        this.jobLogId = jobLogId;
        return this;
    }

    public Long getJobId() {
        return jobId;
    }

    public JobLog setJobId(Long jobId) {
        this.jobId = jobId;
        return this;
    }

    public LogLevel getLevel() {
        return level;
    }

    public JobLog setLevel(LogLevel level) {
        this.level = level;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public JobLog setMessage(String message) {
        this.message = message;
        return this;
    }

    public LocalDateTime getCreatedTimestamp() {
        return createdTimestamp;
    }

    public JobLog setCreatedTimestamp(LocalDateTime createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
        return this;
    }

    @Override
    public String toString() {
        return "JobLog{" +
                "jobLogId=" + jobLogId +
                ", jobId=" + jobId +
                ", level=" + level +
                ", message='" + message + '\'' +
                ", createdTimestamp=" + createdTimestamp +
                '}';
    }

}
