package edu.wisc.library.sdg.preservation.worker.pipeline.ingest.object;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.net.UrlEscapers;
import edu.wisc.library.sdg.preservation.common.PreservationConstants;
import edu.wisc.library.sdg.preservation.common.util.ArgCheck;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.EventType;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.FormatRegistry;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.IngestBatch;
import edu.wisc.library.sdg.preservation.worker.bag.ObjectMetadata;
import edu.wisc.library.sdg.preservation.worker.log.EventLogger;
import edu.wisc.library.sdg.preservation.worker.log.EventRecorder;
import edu.wisc.library.sdg.preservation.worker.pipeline.ingest.IngestContext;
import edu.wisc.library.sdg.preservation.worker.pipeline.ingest.bag.IngestBagContext;

import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Context object containing information related to the processing of a specific object.
 */
public class IngestObjectContext extends IngestContext {

    // Set at context creation
    private final IngestBatch ingestBatch;
    private final String externalId;
    private final Path objectSourcePath;
    private Path objectWorkPath;
    private final ObjectMetadata objectMetadata;

    // Set in pipeline
    private Long ingestObjectId;

    private Map<String, FileDetails> fileMap;

    public static IngestObjectContext fromBagContext(String externalId,
                                                     Path objectSourcePath,
                                                     IngestBagContext bagContext,
                                                     EventRecorder eventRecorder) {
        var encodedId = UrlEscapers.urlFormParameterEscaper().escape(externalId);
        return new IngestObjectContext(bagContext.getIngestBatch(),
                externalId,
                objectSourcePath,
                bagContext.getBatchWorkPath().resolve(encodedId),
                bagContext.getObjectMetadata(externalId),
                eventRecorder);
    }

    @VisibleForTesting
    IngestObjectContext(IngestBatch ingestBatch,
                        String externalId,
                        Path objectSourcePath,
                        Path objectWorkPath,
                        ObjectMetadata objectMetadata,
                        EventRecorder eventRecorder) {
        super(eventRecorder);
        this.ingestBatch = ArgCheck.notNull(ingestBatch, "ingestBatch");
        this.externalId = ArgCheck.notBlank(externalId, "externalId");
        this.objectSourcePath = ArgCheck.notNull(objectSourcePath, "objectSourcePath");
        this.objectWorkPath = ArgCheck.notNull(objectWorkPath, "objectWorkPath");
        this.objectMetadata = objectMetadata;
        this.fileMap = new HashMap<>();
    }

    /**
     * Records an event and submits it to the preservation manager. If an exception is thrown, the event is marked as
     * failed. If the exception is a SafeRuntimeException, then its message is additionally logged.
     *
     * @param type the type of event to record
     * @param block the code to execute
     */
    public void recordEvent(EventType type, Consumer<EventLogger> block) {
        recordEvent(type, getIngestId(), externalId, block);
    }

    public void setIngestObjectId(Long ingestObjectId) {
        this.ingestObjectId = ingestObjectId;
    }

    /**
     * The object's vault
     */
    public String getVault() {
        return ingestBatch.getVault();
    }

    /**
     * The object's external id
     */
    public String getExternalId() {
        return externalId;
    }

    /**
     * @return id of object within batch
     */
    public Long getIngestObjectId() {
        return ingestObjectId;
    }

    /**
     * The path to the object's root directory in the exploded SIP
     */
    public Path getObjectSourcePath() {
        return objectSourcePath;
    }

    /**
     * The object's work directory
     */
    public Path getObjectWorkPath() {
        return objectWorkPath;
    }

    @VisibleForTesting
    public void setObjectWorkPath(Path objectWorkPath) {
        this.objectWorkPath = objectWorkPath;
    }

    /**
     * The ingest batch the object is part of.
     */
    public IngestBatch getIngestBatch() {
        return ingestBatch;
    }

    /**
     * The id of the ingest batch
     */
    public Long getIngestId() {
        return ingestBatch.getIngestId();
    }

    /**
     * Registers a file as part of the object
     *
     * @param filePath object root relative path to the file
     * @param ingestFileId id of the file in the ingest batch
     */
    public void addFile(String filePath, Long ingestFileId) {
        var fileDetails = new FileDetails(filePath, ingestFileId);

        if (objectMetadata != null) {
            var fileMeta = objectMetadata.getFileMeta(filePath);
            if (fileMeta != null) {
                fileMeta.getMimeTypes().forEach(mime -> {
                    fileDetails.addFileFormat(FormatRegistry.MIME, PreservationConstants.USER_SOURCE, mime);
                });
                fileMeta.getPronomIds().forEach(pronom -> {
                    fileDetails.addFileFormat(FormatRegistry.PRONOM, PreservationConstants.USER_SOURCE, pronom);
                });
                fileMeta.getEncodings().forEach(encoding -> {
                    fileDetails.addFileEncoding(encoding, PreservationConstants.USER_SOURCE);
                });
            }
        }

        fileMap.put(filePath, fileDetails);
    }

    /**
     * Adds a format to a file in the object
     *
     * @param filePath object root relative path to the file
     * @param registry the format registry
     * @param source the tool that identified the format
     * @param format the format
     */
    public void addFileFormat(String filePath, FormatRegistry registry, String source, String format) {
        fileMap.get(filePath).addFileFormat(registry, source, format);
    }

    /**
     * Adds a encoding to a file in the object
     *
     * @param filePath object root relative path to the file
     * @param encoding the encoding
     * @param source the tool that identified the encoding
     */
    public void addFileEncoding(String filePath, String encoding, String source) {
        fileMap.get(filePath).addFileEncoding(encoding, source);
    }

    /**
     * Adds a format to a file in the object
     *
     * @param filePath object root relative path to the file
     * @param valid
     * @param wellFormed
     * @param source the tool that identified the validity
     */
    public void addFileValidity(String filePath, Boolean valid, Boolean wellFormed, String source) {
        fileMap.get(filePath).addFileValidity(valid, wellFormed, source);
    }

    public void addFileDetails(String filePath, String sha256Digest, Long fileSize) {
        var details = fileMap.get(filePath);
        details.setSha256Digest(sha256Digest);
        details.setFileSize(fileSize);
    }

    /**
     * @return all of the object root relative paths to all of the files within an object
     */
    public Set<String> getFilePaths() {
        return fileMap.keySet();
    }

    public Collection<FileDetails> getFileDetails() {
        return fileMap.values();
    }

}
