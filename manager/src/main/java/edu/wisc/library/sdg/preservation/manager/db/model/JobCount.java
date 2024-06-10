package edu.wisc.library.sdg.preservation.manager.db.model;

public class JobCount {
    private JobType jobType;
    private long count;

    public JobType getJobType() {
        return jobType;
    }

    public JobCount setJobType(JobType jobType) {
        this.jobType = jobType;
        return this;
    }

    public long getCount() {
        return count;
    }

    public JobCount setCount(long count) {
        this.count = count;
        return this;
    }

    @Override
    public String toString() {
        return "JobCount{" +
                "jobType=" + jobType +
                ", count=" + count +
                '}';
    }
}
