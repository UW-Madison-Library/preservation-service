package edu.wisc.library.sdg.preservation.manager.db.repo;

import edu.wisc.library.sdg.preservation.manager.db.mapper.VaultUserCompositeMapper;
import edu.wisc.library.sdg.preservation.manager.db.model.Permission;
import edu.wisc.library.sdg.preservation.manager.db.model.VaultPermission;
import edu.wisc.library.sdg.preservation.manager.db.model.VaultUserComposite;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VaultPermissionRepository extends CrudRepository<VaultPermission, Long> {

    Optional<VaultPermission> findByVaultAndUsernameAndPermission(
            String vault,
            String username,
            Permission permission);

    @Query("SELECT vp.* FROM vault_permission vp" +
            " WHERE vp.username = :username" +
            " AND vp.vault = :vault")
    List<VaultPermission> findByUsernameAndVault(@Param("username") String username,
                                                   @Param("vault") String vault);

    @Query("SELECT vp.* FROM vault_permission vp" +
            " INNER JOIN vault v ON vp.vault = v.name" +
            " WHERE vp.username = :username" +
            " AND v.org_name = :orgName")
    List<VaultPermission> findByUsernameAndOrgName(@Param("username") String username,
                                                   @Param("orgName") String orgName);

    /**
     * Returns a list of all of the globally enabled users by org. This includes users that are disabled in the org.
     *
     * If a user has multiple permissions, then they will have a result per permission.
     *
     * @param vault name of the vault to filter on
     * @return list of users
     */
    @Query(value = "SELECT" +
            " m.username," +
            " u.external_id," +
            " u.display_name," +
            " u.user_type," +
            " m.role," +
            " u.enabled," +
            " m.enabled," +
            " v.permission" +
            " FROM vault_permission v JOIN preservation_user u ON v.username = u.username" +
            " JOIN user_organization_map m ON v.username = m.username" +
            " WHERE v.vault = :vault" +
            " AND u.enabled IS TRUE", rowMapperClass = VaultUserCompositeMapper.class)
    List<VaultUserComposite> findAllGloballyEnabledUsersWithAccessToVault(String vault);

    @Query("DELETE FROM vault_permission WHERE username = :username AND vault = :vault")
    @Modifying
    void deleteAllByUsernameAndVault(@Param("username") String username,
                                     @Param("vault") String vault);

}
