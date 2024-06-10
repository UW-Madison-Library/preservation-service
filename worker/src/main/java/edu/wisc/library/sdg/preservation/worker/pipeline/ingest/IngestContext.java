package edu.wisc.library.sdg.preservation.worker.pipeline.ingest;

import edu.wisc.library.sdg.preservation.common.util.ArgCheck;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.EventOutcome;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.EventType;
import edu.wisc.library.sdg.preservation.worker.log.EventLogger;
import edu.wisc.library.sdg.preservation.worker.log.EventRecorder;
import edu.wisc.library.sdg.preservation.worker.pipeline.PipelineContext;

import java.util.function.Consumer;

/**
 * Abstract context class that provides methods for logging problems that occur during processing.
 */
public abstract class IngestContext implements PipelineContext {

    protected final EventRecorder eventRecorder;

    private boolean failed = false;
    private boolean problems = false;

    protected IngestContext(EventRecorder eventRecorder) {
        this.eventRecorder = ArgCheck.notNull(eventRecorder, "eventRecorder");
    }

    /**
     * Records an event and submits it to the preservation manager. If an exception is thrown, the event is marked as
     * failed. If the exception is a SafeRuntimeException, then its message is additionally logged.
     *
     * @param type the type of event to record
     * @param ingestId the id of the ingest batch
     * @param externalObjectId the id of the object in the batch, optional
     * @param block the code to execute
     */
    public void recordEvent(EventType type, Long ingestId, String externalObjectId, Consumer<EventLogger> block) {
        try {
            var event = eventRecorder.recordEvent(type, ingestId, externalObjectId, block);
            if (event.getOutcome() == EventOutcome.FAILURE) {
                failed = true;
                problems = true;
            } else if (event.getOutcome() == EventOutcome.SUCCESS_WITH_WARNINGS) {
                problems = true;
            }
        } catch (RuntimeException e) {
            failed = true;
            problems = true;
            throw e;
        }
    }

    /**
     * Whether or not the batch has any recorded problems
     */
    public boolean hasProblems() {
        return problems;
    }

    /**
     * Whether or not the batch has encountered a failure.
     */
    public boolean failed() {
        return failed;
    }

}
