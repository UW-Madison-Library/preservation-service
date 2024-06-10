package edu.wisc.library.sdg.preservation.manager.db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("file_format")
public class FileFormat {

    @Id
    @Column("file_format_id")
    private Long fileFormatId;

    @Column("registry")
    private FormatRegistry registry;

    @Column("format")
    private String format;

    public FileFormat() {

    }

    @PersistenceCreator
    public FileFormat(Long fileFormatId, FormatRegistry registry, String format) {
        this.fileFormatId = fileFormatId;
        this.registry = registry;
        this.format = format;
    }

    public Long getFileFormatId() {
        return fileFormatId;
    }

    public FileFormat setFileFormatId(Long fileFormatId) {
        this.fileFormatId = fileFormatId;
        return this;
    }

    public FormatRegistry getRegistry() {
        return registry;
    }

    public FileFormat setRegistry(FormatRegistry registry) {
        this.registry = registry;
        return this;
    }

    public String getFormat() {
        return format;
    }

    public FileFormat setFormat(String format) {
        this.format = format;
        return this;
    }

    @Override
    public String toString() {
        return "FileFormat{" +
                "fileFormatId=" + fileFormatId +
                ", registry=" + registry +
                ", format=" + format +
                '}';
    }
}
