package edu.wisc.library.sdg.preservation.manager.db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("meta_source")
public class MetaSource {

    @Id
    @Column("meta_source_id")
    private Long metaSourceId;

    @Column("source")
    private String source;

    public MetaSource() {

    }

    @PersistenceCreator
    public MetaSource(Long metaSourceId, String source) {
        this.metaSourceId = metaSourceId;
        this.source = source;
    }

    public Long getMetaSourceId() {
        return metaSourceId;
    }

    public MetaSource setMetaSourceId(Long metaSourceId) {
        this.metaSourceId = metaSourceId;
        return this;
    }

    public String getSource() {
        return source;
    }

    public MetaSource setSource(String source) {
        this.source = source;
        return this;
    }

    @Override
    public String toString() {
        return "MetaSource{" +
                "metaSourceId=" + metaSourceId +
                ", source='" + source + '\'' +
                '}';
    }
}
