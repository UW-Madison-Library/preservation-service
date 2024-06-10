package edu.wisc.library.sdg.preservation.manager.db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("job")
public class Job {

    @Id
    @Column("job_id")
    private Long jobId;

    @Column("org_name")
    private String orgName;

    @Column("type")
    private JobType type;

    @Column("state")
    private JobState state;

    @Column("received_timestamp")
    private LocalDateTime receivedTimestamp;

    @Column("updated_timestamp")
    private LocalDateTime updatedTimestamp;

    @Column("next_attempt_timestamp")
    private LocalDateTime nextAttemptTimestamp;

    @Column("background")
    private Boolean background;

    @Version
    @Column("record_version")
    private Integer recordVersion;

    public Job() {
    }

    @PersistenceCreator
    public Job(Long jobId,
               String orgName,
               JobType type,
               JobState state,
               LocalDateTime receivedTimestamp,
               LocalDateTime updatedTimestamp,
               LocalDateTime nextAttemptTimestamp,
               Boolean background,
               Integer recordVersion) {
        this.jobId = jobId;
        this.orgName = orgName;
        this.type = type;
        this.state = state;
        this.receivedTimestamp = receivedTimestamp;
        this.updatedTimestamp = updatedTimestamp;
        this.nextAttemptTimestamp = nextAttemptTimestamp;
        this.background = background;
        this.recordVersion = recordVersion;
    }

    public Long getJobId() {
        return jobId;
    }

    public Job setJobId(Long jobId) {
        this.jobId = jobId;
        return this;
    }

    public String getOrgName() {
        return orgName;
    }

    public Job setOrgName(String orgName) {
        this.orgName = orgName;
        return this;
    }

    public JobType getType() {
        return type;
    }

    public Job setType(JobType type) {
        this.type = type;
        return this;
    }

    public JobState getState() {
        return state;
    }

    public Job setState(JobState state) {
        this.state = state;
        return this;
    }

    public LocalDateTime getReceivedTimestamp() {
        return receivedTimestamp;
    }

    public Job setReceivedTimestamp(LocalDateTime receivedTimestamp) {
        this.receivedTimestamp = receivedTimestamp;
        return this;
    }

    public LocalDateTime getUpdatedTimestamp() {
        return updatedTimestamp;
    }

    public Job setUpdatedTimestamp(LocalDateTime updatedTimestamp) {
        this.updatedTimestamp = updatedTimestamp;
        return this;
    }

    public LocalDateTime getNextAttemptTimestamp() {
        return nextAttemptTimestamp;
    }

    public Job setNextAttemptTimestamp(LocalDateTime nextAttemptTimestamp) {
        this.nextAttemptTimestamp = nextAttemptTimestamp;
        return this;
    }

    public Boolean getBackground() {
        return background;
    }

    public Job setBackground(Boolean background) {
        this.background = background;
        return this;
    }

    public Integer getRecordVersion() {
        return recordVersion;
    }

    public Job setRecordVersion(Integer recordVersion) {
        this.recordVersion = recordVersion;
        return this;
    }

    @Override
    public String toString() {
        return "Job{" +
                "jobId=" + jobId +
                ", orgName='" + orgName + '\'' +
                ", type=" + type +
                ", state=" + state +
                ", receivedTimestamp=" + receivedTimestamp +
                ", updatedTimestamp=" + updatedTimestamp +
                ", nextAttemptTimestamp=" + nextAttemptTimestamp +
                ", background=" + background +
                ", recordVersion=" + recordVersion +
                '}';
    }

}
