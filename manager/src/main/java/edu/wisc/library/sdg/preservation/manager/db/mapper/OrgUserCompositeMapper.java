package edu.wisc.library.sdg.preservation.manager.db.mapper;

import edu.wisc.library.sdg.preservation.manager.db.model.OrgRole;
import edu.wisc.library.sdg.preservation.manager.db.model.OrgUserComposite;
import edu.wisc.library.sdg.preservation.manager.db.model.PreservationUserType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrgUserCompositeMapper implements RowMapper<OrgUserComposite> {

    @Override
    public OrgUserComposite mapRow(ResultSet rs, int rowNum) throws SQLException {
        int i = 1;
        return new OrgUserComposite()
                .setUsername(rs.getString(i++))
                .setExternalId(rs.getString(i++))
                .setDisplayName(rs.getString(i++))
                .setUserType(PreservationUserType.fromShort(rs.getShort(i++)))
                .setRole(OrgRole.fromShort(rs.getShort(i++)))
                .setEnabled(rs.getBoolean(i++))
                .setEnabledInOrg(rs.getBoolean(i++));
    }

}
