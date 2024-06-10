package edu.wisc.library.sdg.preservation.worker.pipeline.ingest.bag;

import edu.wisc.library.sdg.preservation.common.util.ArgCheck;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.EventType;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.IngestBatch;
import edu.wisc.library.sdg.preservation.worker.bag.ObjectMetadata;
import edu.wisc.library.sdg.preservation.worker.log.EventLogger;
import edu.wisc.library.sdg.preservation.worker.log.EventRecorder;
import edu.wisc.library.sdg.preservation.worker.pipeline.ingest.IngestContext;
import edu.wisc.library.sdg.preservation.worker.pipeline.ingest.object.IngestObjectContext;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Contains information related to processing an ingest batch.
 */
public class IngestBagContext extends IngestContext {

    // Set at context creation

    private final IngestBatch ingestBatch;
    private final Long ingestId;
    private final Path bagArchivedPath;

    // Set in pipeline
    private Path bagExplodedPath;
    private Path bagRootPath;
    private Path bagDataPath;
    private Path batchWorkPath;
    private Map<String, ObjectMetadata> objectMetadataMap;

    private boolean failed = false;
    private boolean problems = false;

    public static IngestBagContext fromIngestBatch(IngestBatch ingestBatch, EventRecorder eventRecorder) {
        return new IngestBagContext(ingestBatch, eventRecorder);
    }

    private IngestBagContext(IngestBatch ingestBatch, EventRecorder eventRecorder) {
        super(eventRecorder);
        this.ingestBatch = ArgCheck.notNull(ingestBatch, "ingestBatch");
        this.ingestId = ArgCheck.notNull(ingestBatch.getIngestId(), "ingestId");
        this.bagArchivedPath = Paths.get(ArgCheck.notBlank(ingestBatch.getFilePath(), "filePath"));
    }

    /**
     * Creates a context object for processing an object within a batch.
     */
    public IngestObjectContext createIngestObjectContext(String externalId, Path sourcePath) {
        return IngestObjectContext.fromBagContext(externalId, sourcePath, this, eventRecorder);
    }

    /**
     * Records an event and submits it to the preservation manager. If an exception is thrown, the event is marked as
     * failed. If the exception is a SafeRuntimeException, then its message is additionally logged.
     *
     * @param type the type of event to record
     * @param block the code to execute
     */
    public void recordEvent(EventType type, Consumer<EventLogger> block) {
        recordEvent(type, ingestId, null, block);
    }

    /**
     * The ingest batch being processed
     */
    public IngestBatch getIngestBatch() {
        return ingestBatch;
    }

    /**
     * The id associated to the batch being processed
     */
    public Long getIngestId() {
        return ingestId;
    }

    /**
     * The path to the compressed archive to process
     */
    public Path getBagArchivedPath() {
        return bagArchivedPath;
    }

    /**
     * The path to the directory the bag was extracted to
     */
    public Path getBagExplodedPath() {
        return bagExplodedPath;
    }

    public void setBagExplodedPath(Path bagExplodedPath) {
        this.bagExplodedPath = ArgCheck.notNull(bagExplodedPath, "bagExplodedPath");
    }

    public void setBagRootPath(Path bagRootPath) {
        this.bagRootPath = ArgCheck.notNull(bagRootPath, "bagRootPath");
        this.bagDataPath = bagRootPath.resolve("data");
    }

    /**
     * Path to the root of the exploded bag
     */
    public Path getBagRootPath() {
        return bagRootPath;
    }

    /**
     * Path to the data dir in the exploded bag
     */
    public Path getBagDataPath() {
        return bagDataPath;
    }

    public void setBatchWorkPath(Path batchWorkPath) {
        this.batchWorkPath = ArgCheck.notNull(batchWorkPath, "batchWorkPath");
    }

    /**
     * Path to the work directory where files generated during batch processing are stored.
     */
    public Path getBatchWorkPath() {
        return batchWorkPath;
    }

    public void setObjectMetadataMap(Map<String, ObjectMetadata> objectMetadataMap) {
        this.objectMetadataMap = objectMetadataMap;
    }

    /**
     * Returns the object metadata associated with the object, or null if there isn't any
     *
     * @param externalObjectId the id of the object
     * @return the associated metadata or null
     */
    public ObjectMetadata getObjectMetadata(String externalObjectId) {
        if (objectMetadataMap != null) {
            return objectMetadataMap.get(externalObjectId);
        }
        return null;
    }

}
