package edu.wisc.library.sdg.preservation.manager.db.model;

public class IngestBatchObjectFileValidityComposite {

    private Long ingestFileValidityId;
    private Long ingestFileId;
    private Boolean valid;
    private Boolean wellFormed;
    private String source;

    public IngestBatchObjectFileValidityComposite() {

    }

    public IngestBatchObjectFileValidityComposite(IngestBatchObjectFileValidity validity,
                                                  MetaSource source) {
        this.ingestFileValidityId = validity.getIngestFileValidityId();
        this.ingestFileId = validity.getIngestFileId();
        this.valid = validity.getValid();
        this.wellFormed = validity.getWellFormed();
        this.source = source.getSource();
    }

    public Long getIngestFileValidityId() {
        return ingestFileValidityId;
    }

    public IngestBatchObjectFileValidityComposite setIngestFileValidityId(Long ingestFileValidityId) {
        this.ingestFileValidityId = ingestFileValidityId;
        return this;
    }

    public Long getIngestFileId() {
        return ingestFileId;
    }

    public IngestBatchObjectFileValidityComposite setIngestFileId(Long ingestFileId) {
        this.ingestFileId = ingestFileId;
        return this;
    }

    public Boolean getValid() {
        return valid;
    }

    public IngestBatchObjectFileValidityComposite setValid(Boolean valid) {
        this.valid = valid;
        return this;
    }

    public Boolean getWellFormed() {
        return wellFormed;
    }

    public IngestBatchObjectFileValidityComposite setWellFormed(Boolean wellFormed) {
        this.wellFormed = wellFormed;
        return this;
    }

    public String getSource() {
        return source;
    }

    public IngestBatchObjectFileValidityComposite setSource(String source) {
        this.source = source;
        return this;
    }

    @Override
    public String toString() {
        return "IngestBatchObjectFileValidityComposite{" +
                "ingestFileValidityId=" + ingestFileValidityId +
                ", ingestFileId=" + ingestFileId +
                ", valid=" + valid +
                ", wellFormed=" + wellFormed +
                ", source='" + source + '\'' +
                '}';
    }
}
