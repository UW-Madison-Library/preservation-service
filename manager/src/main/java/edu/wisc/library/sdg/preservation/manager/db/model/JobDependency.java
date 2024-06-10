package edu.wisc.library.sdg.preservation.manager.db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("job_dependency")
public class JobDependency {

    @Id
    @Column("job_dependency_id")
    private Long jobDependencyId;

    @Column("job_id")
    private Long jobId;

    @Column("depends_on_job_id")
    private Long dependsOnJobId;

    public JobDependency() {
    }

    @PersistenceCreator
    public JobDependency(Long jobDependencyId,
                         Long jobId,
                         Long dependsOnJobId) {
        this.jobDependencyId = jobDependencyId;
        this.jobId = jobId;
        this.dependsOnJobId = dependsOnJobId;
    }

    public Long getJobDependencyId() {
        return jobDependencyId;
    }

    public JobDependency setJobDependencyId(Long jobDependencyId) {
        this.jobDependencyId = jobDependencyId;
        return this;
    }

    public Long getJobId() {
        return jobId;
    }

    public JobDependency setJobId(Long jobId) {
        this.jobId = jobId;
        return this;
    }

    public Long getDependsOnJobId() {
        return dependsOnJobId;
    }

    public JobDependency setDependsOnJobId(Long dependsOnJobId) {
        this.dependsOnJobId = dependsOnJobId;
        return this;
    }

    @Override
    public String toString() {
        return "JobDependency{" +
                "jobDependencyId=" + jobDependencyId +
                ", jobId='" + jobId + '\'' +
                ", dependsOnJobId='" + dependsOnJobId + '\'' +
                '}';
    }

}
