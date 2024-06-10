package edu.wisc.library.sdg.preservation.manager.db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("preservation_object_file_validity")
public class PreservationObjectFileValidity {

    @Id
    @Column("object_file_validity_id")
    private Long objectFileValidityId;

    @Column("object_file_id")
    private Long objectFileId;

    @Column("valid")
    private Boolean valid;

    @Column("well_formed")
    private Boolean wellFormed;

    @Column("meta_source_id")
    private Long metaSourceId;

    public PreservationObjectFileValidity() {

    }

    @PersistenceCreator
    public PreservationObjectFileValidity(Long objectFileValidityId,
                                          Long objectFileId,
                                          Boolean valid,
                                          Boolean wellFormed,
                                          Long metaSourceId) {
        this.objectFileValidityId = objectFileValidityId;
        this.objectFileId = objectFileId;
        this.valid = valid;
        this.wellFormed = wellFormed;
        this.metaSourceId = metaSourceId;
    }

    public Long getObjectFileValidityId() {
        return objectFileValidityId;
    }

    public PreservationObjectFileValidity setObjectFileValidityId(Long objectFileValidityId) {
        this.objectFileValidityId = objectFileValidityId;
        return this;
    }

    public Long getObjectFileId() {
        return objectFileId;
    }

    public PreservationObjectFileValidity setObjectFileId(Long objectFileId) {
        this.objectFileId = objectFileId;
        return this;
    }

    public Boolean getValid() {
        return valid;
    }

    public PreservationObjectFileValidity setValid(Boolean valid) {
        this.valid = valid;
        return this;
    }

    public Boolean getWellFormed() {
        return wellFormed;
    }

    public PreservationObjectFileValidity setWellFormed(Boolean wellFormed) {
        this.wellFormed = wellFormed;
        return this;
    }

    public Long getMetaSourceId() {
        return metaSourceId;
    }

    public PreservationObjectFileValidity setMetaSourceId(Long metaSourceId) {
        this.metaSourceId = metaSourceId;
        return this;
    }

    @Override
    public String toString() {
        return "PreservationObjectFileValidity{" +
                "objectFileValidityId=" + objectFileValidityId +
                ", objectFileId=" + objectFileId +
                ", valid=" + valid +
                ", wellFormed=" + wellFormed +
                ", metaSourceId=" + metaSourceId +
                '}';
    }

}
