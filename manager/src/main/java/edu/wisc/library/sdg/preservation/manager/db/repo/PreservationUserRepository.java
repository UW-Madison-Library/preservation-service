package edu.wisc.library.sdg.preservation.manager.db.repo;

import edu.wisc.library.sdg.preservation.manager.db.model.PreservationUser;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PreservationUserRepository extends CrudRepository<PreservationUser, String> {

    @Query("SELECT u.* FROM preservation_user u" +
            " INNER JOIN api_key k ON u.username = k.username" +
            " WHERE k.key_hash = :keyHash")
    PreservationUser findByApiKeyHash(@Param("keyHash") String keyHash);

    PreservationUser findByExternalId(String externalId);

    List<PreservationUser> findAll();
}
