package edu.wisc.library.sdg.preservation.manager.db.model;

/**
 * Exists to denormalize PreservationObjectFileEncoding
 */
public class PreservationObjectFileEncodingComposite {

    private Long objectFileEncodingId;
    private Long objectFileId;
    private String encoding;
    private String source;

    public PreservationObjectFileEncodingComposite() {
    }

    public PreservationObjectFileEncodingComposite(PreservationObjectFileEncoding objectFileEncoding,
                                                   FileEncoding fileEncoding,
                                                   MetaSource source) {
        this.objectFileEncodingId = objectFileEncoding.getObjectFileEncodingId();
        this.objectFileId = objectFileEncoding.getObjectFileId();
        this.source = source.getSource();
        this.encoding = fileEncoding.getEncoding();
    }

    public Long getObjectFileEncodingId() {
        return objectFileEncodingId;
    }

    public PreservationObjectFileEncodingComposite setObjectFileEncodingId(Long objectFileEncodingId) {
        this.objectFileEncodingId = objectFileEncodingId;
        return this;
    }

    public Long getObjectFileId() {
        return objectFileId;
    }

    public PreservationObjectFileEncodingComposite setObjectFileId(Long objectFileId) {
        this.objectFileId = objectFileId;
        return this;
    }

    public String getEncoding() {
        return encoding;
    }

    public PreservationObjectFileEncodingComposite setEncoding(String encoding) {
        this.encoding = encoding;
        return this;
    }

    public String getSource() {
        return source;
    }

    public PreservationObjectFileEncodingComposite setSource(String source) {
        this.source = source;
        return this;
    }

    @Override
    public String toString() {
        return "PreservationObjectFileEncodingComposite{" +
                "objectFileEncodingId=" + objectFileEncodingId +
                ", objectFileId=" + objectFileId +
                ", encoding='" + encoding + '\'' +
                ", source=" + source +
                '}';
    }

}
