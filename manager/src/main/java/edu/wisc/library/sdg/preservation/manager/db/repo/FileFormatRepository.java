package edu.wisc.library.sdg.preservation.manager.db.repo;

import edu.wisc.library.sdg.preservation.manager.db.model.FileFormat;
import edu.wisc.library.sdg.preservation.manager.db.model.FormatRegistry;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface FileFormatRepository extends CrudRepository<FileFormat, Long> {

    @Query("INSERT INTO file_format (registry, format) VALUES (:registry, :format) ON CONFLICT DO NOTHING")
    @Modifying
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void insertIgnoreDuplicate(FormatRegistry registry, String format);

    @Cacheable(value = "fileFormatByFormat", key = "#a0 + \":\" + #a1", unless = "#result == null")
    Optional<FileFormat> findByRegistryAndFormat(FormatRegistry registry, String format);

    @Override
    @Cacheable(value = "fileFormatById", unless = "#result == null")
    Optional<FileFormat> findById(Long fileFormatId);
}
