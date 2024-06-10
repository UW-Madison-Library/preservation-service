package edu.wisc.library.sdg.preservation.manager.db.repo;

import edu.wisc.library.sdg.preservation.manager.db.model.Organization;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrganizationRepository extends CrudRepository<Organization, String> {

    @Query("SELECT o.* FROM organization o" +
            " INNER JOIN user_organization_map ou ON o.org_name = ou.org_name" +
            " WHERE ou.username = :username" +
            " AND ou.enabled")
    List<Organization> findAllByUser(String username);

}
