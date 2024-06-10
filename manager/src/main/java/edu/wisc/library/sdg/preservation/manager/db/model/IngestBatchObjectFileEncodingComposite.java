package edu.wisc.library.sdg.preservation.manager.db.model;

/**
 * Exists to denormalize IngestBatchObjectFileEncoding
 */
public class IngestBatchObjectFileEncodingComposite {

    private Long ingestFileEncodingId;
    private Long ingestFileId;
    private String encoding;
    private String source;

    public IngestBatchObjectFileEncodingComposite() {

    }

    public IngestBatchObjectFileEncodingComposite(IngestBatchObjectFileEncoding ingestEncoding,
                                                  FileEncoding fileEncoding,
                                                  MetaSource source) {
        this.ingestFileEncodingId = ingestEncoding.getIngestFileEncodingId();
        this.ingestFileId = ingestEncoding.getIngestFileId();
        this.source = source.getSource();
        this.encoding = fileEncoding.getEncoding();
    }

    public Long getIngestFileEncodingId() {
        return ingestFileEncodingId;
    }

    public IngestBatchObjectFileEncodingComposite setIngestFileEncodingId(Long ingestFileEncodingId) {
        this.ingestFileEncodingId = ingestFileEncodingId;
        return this;
    }

    public Long getIngestFileId() {
        return ingestFileId;
    }

    public IngestBatchObjectFileEncodingComposite setIngestFileId(Long ingestFileId) {
        this.ingestFileId = ingestFileId;
        return this;
    }

    public String getEncoding() {
        return encoding;
    }

    public IngestBatchObjectFileEncodingComposite setEncoding(String encoding) {
        this.encoding = encoding;
        return this;
    }

    public String getSource() {
        return source;
    }

    public IngestBatchObjectFileEncodingComposite setSource(String source) {
        this.source = source;
        return this;
    }

    @Override
    public String toString() {
        return "IngestBatchObjectFileEncodingComposite{" +
                "ingestFileFormatId=" + ingestFileEncodingId +
                ", ingestFileId=" + ingestFileId +
                ", format='" + encoding + '\'' +
                ", source=" + source +
                '}';
    }

}
