package edu.wisc.library.sdg.preservation.manager.db.repo;

import edu.wisc.library.sdg.preservation.manager.db.model.FileEncoding;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface FileEncodingRepository extends CrudRepository<FileEncoding, Long> {

    @Query("INSERT INTO file_encoding (encoding) VALUES (:encoding) ON CONFLICT DO NOTHING")
    @Modifying
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void insertIgnoreDuplicate(String encoding);

    @Cacheable(value = "fileEncodingByEncoding", unless = "#result == null")
    Optional<FileEncoding> findByEncoding(String encoding);

    @Override
    @Cacheable(value = "fileEncodingById", unless = "#result == null")
    Optional<FileEncoding> findById(Long fileEncodingId);
}
