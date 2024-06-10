package edu.wisc.library.sdg.preservation.manager.db.repo;

import edu.wisc.library.sdg.preservation.manager.db.mapper.OrgUserCompositeMapper;
import edu.wisc.library.sdg.preservation.manager.db.model.OrgUserComposite;
import edu.wisc.library.sdg.preservation.manager.db.model.UserOrgMap;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserOrgMapRepository extends CrudRepository<UserOrgMap, Long> {

    Optional<UserOrgMap> findByUsernameAndOrgName(String username, String orgName);

    /**
     * Returns a list of all of the globally enabled users by org. This includes users that are disabled in the org.
     *
     * @param orgName name of the org to filter on
     * @return list of users
     */
    @Query(value = "SELECT" +
            " m.username," +
            " u.external_id," +
            " u.display_name," +
            " u.user_type," +
            " m.role," +
            " u.enabled," +
            " m.enabled" +
            " FROM user_organization_map m JOIN preservation_user u ON m.username = u.username" +
            " WHERE m.org_name = :orgName" +
            " AND u.enabled IS TRUE", rowMapperClass = OrgUserCompositeMapper.class)
    List<OrgUserComposite> findAllGloballyEnabledByOrgName(String orgName);

    @Query(value = "SELECT" +
            " m.username," +
            " u.external_id," +
            " u.display_name," +
            " u.user_type," +
            " m.role," +
            " u.enabled," +
            " m.enabled" +
            " FROM user_organization_map m JOIN preservation_user u ON m.username = u.username" +
            " WHERE m.org_name = :orgName" +
            " AND m.username = :username" +
            " AND u.enabled IS TRUE", rowMapperClass = OrgUserCompositeMapper.class)
    OrgUserComposite findByOrgNameAndUsername(String orgName, String username);
}
