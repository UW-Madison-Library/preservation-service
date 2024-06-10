package edu.wisc.library.sdg.preservation.manager.db.mapper;

import edu.wisc.library.sdg.preservation.manager.db.model.SimplePreservationObjectVersionFileComposite;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SimplePreservationObjectVersionFileCompositeMapper implements RowMapper<SimplePreservationObjectVersionFileComposite> {

    @Override
    public SimplePreservationObjectVersionFileComposite mapRow(ResultSet rs, int rowNum) throws SQLException {
        int i = 1;
        return new SimplePreservationObjectVersionFileComposite()
                .setObjectFileId(rs.getLong(i++))
                .setObjectVersionFileId(rs.getLong(i++))
                .setSha256Digest(rs.getString(i++))
                .setFileSize(rs.getLong(i++))
                .setFilePath(rs.getString(i++));
    }
}
