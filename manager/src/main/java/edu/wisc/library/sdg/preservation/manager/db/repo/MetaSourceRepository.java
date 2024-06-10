package edu.wisc.library.sdg.preservation.manager.db.repo;

import edu.wisc.library.sdg.preservation.manager.db.model.MetaSource;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface MetaSourceRepository extends CrudRepository<MetaSource, Long> {

    @Query("INSERT INTO meta_source (source) VALUES (:source) ON CONFLICT DO NOTHING")
    @Modifying
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void insertIgnoreDuplicate(String source);

    @Cacheable(value = "metaSourceBySource", unless = "#result == null")
    Optional<MetaSource> findBySource(String source);

    @Override
    @Cacheable(value = "metaSourceById", unless = "#result == null")
    Optional<MetaSource> findById(Long metaSourceId);
}
