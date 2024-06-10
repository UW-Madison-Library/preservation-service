package edu.wisc.library.sdg.preservation.manager.db.repo;

import edu.wisc.library.sdg.preservation.manager.db.model.DataStore;
import edu.wisc.library.sdg.preservation.manager.db.model.StorageProblem;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface StorageProblemRepository extends CrudRepository<StorageProblem, Long> {

    List<StorageProblem> findAllByObjectId(UUID objectId);

    @Query("DELETE FROM storage_problem WHERE object_id = :objectId AND datastore = :datastore")
    @Modifying
    void deleteAllByObjectIdAndDatastore(@Param("objectId") UUID objectId,
                                         @Param("datastore") DataStore datastore);

    @Query("DELETE FROM storage_problem WHERE object_id = :objectId AND persistence_version = :persistenceVersion AND datastore = :datastore")
    @Modifying
    void deleteAllByObjectIdAndPersistenceVersionAndDatastore(@Param("objectId") UUID objectId,
                                                              @Param("persistenceVersion") String persistenceVersion,
                                                              @Param("datastore") DataStore datastore);

}
