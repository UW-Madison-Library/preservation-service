package edu.wisc.library.sdg.preservation.manager.db.mapper;

import edu.wisc.library.sdg.preservation.manager.db.model.OrgRole;
import edu.wisc.library.sdg.preservation.manager.db.model.Permission;
import edu.wisc.library.sdg.preservation.manager.db.model.PreservationUserType;
import edu.wisc.library.sdg.preservation.manager.db.model.VaultUserComposite;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VaultUserCompositeMapper implements RowMapper<VaultUserComposite> {

    @Override
    public VaultUserComposite mapRow(ResultSet rs, int rowNum) throws SQLException {
        int i = 1;
        return new VaultUserComposite()
                .setUsername(rs.getString(i++))
                .setExternalId(rs.getString(i++))
                .setDisplayName(rs.getString(i++))
                .setUserType(PreservationUserType.fromShort(rs.getShort(i++)))
                .setRole(OrgRole.fromShort(rs.getShort(i++)))
                .setEnabled(rs.getBoolean(i++))
                .setEnabledInOrg(rs.getBoolean(i++))
                .setPermission(Permission.fromShort(rs.getShort(i++)));
    }
}
