package edu.wisc.library.sdg.preservation.manager.db.repo;

import edu.wisc.library.sdg.preservation.manager.db.model.Vault;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VaultRepository extends CrudRepository<Vault, String> {

    @Query("SELECT name FROM vault")
    List<String> findAllNames();

    List<Vault> findAllByOrgName(String orgName);

    @Query("SELECT COUNT(*) FROM preservation_object WHERE vault = :vault AND state != 2")
    Optional<Long> countAllObjectsByVault(@Param("vault") String vault);

    @Query("""
           SELECT SUM(f.file_size / 1048576) FROM preservation_object o
             INNER JOIN preservation_object_file f ON o.object_id = f.object_id
             WHERE vault = :vault
             AND state != 2
           """)
    Optional<Long> sumObjectBytesInMbByVault(@Param("vault") String vault);

    /**
     * @param org the org to filter on
     * @return total number of objects in all vaults in org
     */
    @Query("SELECT SUM(objects) FROM vault WHERE org_name = :org")
    Optional<Long> countAllObjectsByOrg(@Param("org") String org);

    /**
     * @param org the org to filter on
     * @return total number of MBs across all vaults in org
     */
    @Query("SELECT SUM(storage_mb) FROM vault WHERE org_name = :org")
    Optional<Long> sumObjectBytesInMbByOrg(@Param("org") String org);

    /**
     * @return total number of objects in all vaults
     */
    @Query("SELECT SUM(objects) FROM vault")
    Optional<Long> countAllObjects();

    /**
     * @return total number of MBs across all vaults
     */
    @Query("SELECT SUM(storage_mb) FROM vault")
    Optional<Long> sumObjectBytesInMb();

}
