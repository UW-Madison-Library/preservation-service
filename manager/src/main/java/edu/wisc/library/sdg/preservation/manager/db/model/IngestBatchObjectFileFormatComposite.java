package edu.wisc.library.sdg.preservation.manager.db.model;

/**
 * Exists to denormalize IngestBatchObjectFileFormat
 */
public class IngestBatchObjectFileFormatComposite {

    private Long ingestFileFormatId;
    private Long ingestFileId;
    private FormatRegistry formatRegistry;
    private String format;
    private String source;

    public IngestBatchObjectFileFormatComposite() {

    }

    public IngestBatchObjectFileFormatComposite(IngestBatchObjectFileFormat ingestFormat,
                                                FileFormat fileFormat,
                                                MetaSource source) {
        this.ingestFileFormatId = ingestFormat.getIngestFileFormatId();
        this.ingestFileId = ingestFormat.getIngestFileId();
        this.source = source.getSource();
        this.formatRegistry = fileFormat.getRegistry();
        this.format = fileFormat.getFormat();
    }

    public Long getIngestFileFormatId() {
        return ingestFileFormatId;
    }

    public IngestBatchObjectFileFormatComposite setIngestFileFormatId(Long ingestFileFormatId) {
        this.ingestFileFormatId = ingestFileFormatId;
        return this;
    }

    public Long getIngestFileId() {
        return ingestFileId;
    }

    public IngestBatchObjectFileFormatComposite setIngestFileId(Long ingestFileId) {
        this.ingestFileId = ingestFileId;
        return this;
    }

    public FormatRegistry getFormatRegistry() {
        return formatRegistry;
    }

    public IngestBatchObjectFileFormatComposite setFormatRegistry(FormatRegistry formatRegistry) {
        this.formatRegistry = formatRegistry;
        return this;
    }

    public String getFormat() {
        return format;
    }

    public IngestBatchObjectFileFormatComposite setFormat(String format) {
        this.format = format;
        return this;
    }

    public String getSource() {
        return source;
    }

    public IngestBatchObjectFileFormatComposite setSource(String source) {
        this.source = source;
        return this;
    }

    @Override
    public String toString() {
        return "IngestBatchObjectFileFormatComposite{" +
                "ingestFileFormatId=" + ingestFileFormatId +
                ", ingestFileId=" + ingestFileId +
                ", formatRegistry='" + formatRegistry + '\'' +
                ", format='" + format + '\'' +
                ", source=" + source +
                '}';
    }

}
