package edu.wisc.library.sdg.preservation.manager.db.mapper;

import edu.wisc.library.sdg.preservation.manager.db.model.JobState;
import edu.wisc.library.sdg.preservation.manager.db.model.RetrieveRequestJobComposite;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class RetrieveRequestJobCompositeMapper implements RowMapper<RetrieveRequestJobComposite> {

    @Override
    public RetrieveRequestJobComposite mapRow(ResultSet rs, int rowNum) throws SQLException {
        int i = 1;
        return new RetrieveRequestJobComposite()
                .setRetrieveRequestJobId(rs.getLong(i++))
                .setRetrieveRequestId(rs.getLong(i++))
                .setJobId(rs.getLong(i++))
                .setLastDownloadedTimestamp(convert(rs.getTimestamp(i++)))
                .setState(JobState.fromShort(rs.getShort(i++)));
    }

    private LocalDateTime convert(Timestamp original) {
        if (original == null) {
            return null;
        }
        return original.toLocalDateTime();
    }

}
