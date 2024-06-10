package edu.wisc.library.sdg.preservation.manager.db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("file_encoding")
public class FileEncoding {

    @Id
    @Column("file_encoding_id")
    private Long fileEncodingId;

    @Column("encoding")
    private String encoding;

    public FileEncoding() {

    }

    @PersistenceCreator
    public FileEncoding(Long fileEncodingId, String encoding) {
        this.fileEncodingId = fileEncodingId;
        this.encoding = encoding;
    }

    public Long getFileEncodingId() {
        return fileEncodingId;
    }

    public FileEncoding setFileEncodingId(Long fileEncodingId) {
        this.fileEncodingId = fileEncodingId;
        return this;
    }

    public String getEncoding() {
        return encoding;
    }

    public FileEncoding setEncoding(String encoding) {
        this.encoding = encoding;
        return this;
    }

    @Override
    public String toString() {
        return "FileEncoding{" +
                "fileEncodingId=" + fileEncodingId +
                ", encoding=" + encoding +
                '}';
    }
}
