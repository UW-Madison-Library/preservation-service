package edu.wisc.library.sdg.preservation.manager.db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("ingest_file_validity")
public class IngestBatchObjectFileValidity {

    @Id
    @Column("ingest_file_validity_id")
    private Long ingestFileValidityId;

    @Column("ingest_file_id")
    private Long ingestFileId;

    @Column("valid")
    private Boolean valid;

    @Column("well_formed")
    private Boolean wellFormed;

    @Column("meta_source_id")
    private Long metaSourceId;

    public IngestBatchObjectFileValidity() {

    }

    @PersistenceCreator
    public IngestBatchObjectFileValidity(Long ingestFileValidityId,
                                         Long ingestFileId,
                                         Boolean valid,
                                         Boolean wellFormed,
                                         Long metaSourceId) {
        this.ingestFileValidityId = ingestFileValidityId;
        this.ingestFileId = ingestFileId;
        this.valid = valid;
        this.wellFormed = wellFormed;
        this.metaSourceId = metaSourceId;
    }

    public Long getIngestFileValidityId() {
        return ingestFileValidityId;
    }

    public IngestBatchObjectFileValidity setIngestFileValidityId(Long ingestFileValidityId) {
        this.ingestFileValidityId = ingestFileValidityId;
        return this;
    }

    public Long getIngestFileId() {
        return ingestFileId;
    }

    public IngestBatchObjectFileValidity setIngestFileId(Long ingestFileId) {
        this.ingestFileId = ingestFileId;
        return this;
    }

    public Boolean getValid() {
        return valid;
    }

    public IngestBatchObjectFileValidity setValid(Boolean valid) {
        this.valid = valid;
        return this;
    }

    public Boolean getWellFormed() {
        return wellFormed;
    }

    public IngestBatchObjectFileValidity setWellFormed(Boolean wellFormed) {
        this.wellFormed = wellFormed;
        return this;
    }

    public Long getMetaSourceId() {
        return metaSourceId;
    }

    public IngestBatchObjectFileValidity setMetaSourceId(Long metaSourceId) {
        this.metaSourceId = metaSourceId;
        return this;
    }

    @Override
    public String toString() {
        return "IngestBatchObjectFileValidity{" +
                "ingestFileEncodingId=" + ingestFileValidityId +
                ", ingestFileId=" + ingestFileId +
                ", valid=" + valid +
                ", wellFormed=" + wellFormed +
                ", metaSourceId=" + metaSourceId +
                '}';
    }

}
