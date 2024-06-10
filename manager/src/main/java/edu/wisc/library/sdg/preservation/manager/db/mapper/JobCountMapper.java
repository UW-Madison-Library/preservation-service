package edu.wisc.library.sdg.preservation.manager.db.mapper;

import edu.wisc.library.sdg.preservation.manager.db.model.JobCount;
import edu.wisc.library.sdg.preservation.manager.db.model.JobType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class JobCountMapper  implements RowMapper<JobCount> {
    @Override
    public JobCount mapRow(ResultSet rs, int rowNum) throws SQLException {
        int i = 1;
        return new JobCount().setJobType(JobType.fromShort(rs.getShort(i++)))
                .setCount(rs.getLong(i++));
    }
}
