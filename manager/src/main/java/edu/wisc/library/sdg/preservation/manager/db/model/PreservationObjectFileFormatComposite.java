package edu.wisc.library.sdg.preservation.manager.db.model;

/**
 * Exists to denormalize PreservationObjectFileFormat
 */
public class PreservationObjectFileFormatComposite {

    private Long objectFileFormatId;
    private Long objectFileId;
    private FormatRegistry formatRegistry;
    private String format;
    private String source;

    public PreservationObjectFileFormatComposite() {
    }

    public PreservationObjectFileFormatComposite(PreservationObjectFileFormat objectFileFormat,
                                                 FileFormat fileFormat,
                                                 MetaSource source) {
        this.objectFileFormatId = objectFileFormat.getObjectFileFormatId();
        this.objectFileId = objectFileFormat.getObjectFileId();
        this.source = source.getSource();
        this.formatRegistry = fileFormat.getRegistry();
        this.format = fileFormat.getFormat();
    }

    public Long getObjectFileFormatId() {
        return objectFileFormatId;
    }

    public PreservationObjectFileFormatComposite setObjectFileFormatId(Long objectFileFormatId) {
        this.objectFileFormatId = objectFileFormatId;
        return this;
    }

    public Long getObjectFileId() {
        return objectFileId;
    }

    public PreservationObjectFileFormatComposite setObjectFileId(Long objectFileId) {
        this.objectFileId = objectFileId;
        return this;
    }

    public FormatRegistry getFormatRegistry() {
        return formatRegistry;
    }

    public PreservationObjectFileFormatComposite setFormatRegistry(FormatRegistry formatRegistry) {
        this.formatRegistry = formatRegistry;
        return this;
    }

    public String getFormat() {
        return format;
    }

    public PreservationObjectFileFormatComposite setFormat(String format) {
        this.format = format;
        return this;
    }

    public String getSource() {
        return source;
    }

    public PreservationObjectFileFormatComposite setSource(String source) {
        this.source = source;
        return this;
    }

    @Override
    public String toString() {
        return "PreservationObjectFileFormatComposite{" +
                "objectFileFormatId=" + objectFileFormatId +
                ", objectFileId=" + objectFileId +
                ", formatRegistry='" + formatRegistry + '\'' +
                ", format='" + format + '\'' +
                ", source=" + source +
                '}';
    }

}
