package edu.wisc.library.sdg.preservation.manager.db.model;

public class PreservationObjectFileValidityComposite {

    private Long objectFileValidityId;
    private Long objectFileId;
    private Boolean valid;
    private Boolean wellFormed;
    private String source;

    public PreservationObjectFileValidityComposite() {

    }

    public PreservationObjectFileValidityComposite(PreservationObjectFileValidity validity,
                                                   MetaSource source) {
        this.objectFileValidityId = validity.getObjectFileValidityId();
        this.objectFileId = validity.getObjectFileId();
        this.valid = validity.getValid();
        this.wellFormed = validity.getWellFormed();
        this.source = source.getSource();
    }

    public Long getObjectFileValidityId() {
        return objectFileValidityId;
    }

    public PreservationObjectFileValidityComposite setObjectFileValidityId(Long objectFileValidityId) {
        this.objectFileValidityId = objectFileValidityId;
        return this;
    }

    public Long getObjectFileId() {
        return objectFileId;
    }

    public PreservationObjectFileValidityComposite setObjectFileId(Long objectFileId) {
        this.objectFileId = objectFileId;
        return this;
    }

    public Boolean getValid() {
        return valid;
    }

    public PreservationObjectFileValidityComposite setValid(Boolean valid) {
        this.valid = valid;
        return this;
    }

    public Boolean getWellFormed() {
        return wellFormed;
    }

    public PreservationObjectFileValidityComposite setWellFormed(Boolean wellFormed) {
        this.wellFormed = wellFormed;
        return this;
    }

    public String getSource() {
        return source;
    }

    public PreservationObjectFileValidityComposite setSource(String source) {
        this.source = source;
        return this;
    }

    @Override
    public String toString() {
        return "PreservationObjectFileValidityComposite{" +
                "objectFileValidityId=" + objectFileValidityId +
                ", objectFileId=" + objectFileId +
                ", valid=" + valid +
                ", wellFormed=" + wellFormed +
                ", source='" + source + '\'' +
                '}';
    }
}
