package edu.wisc.library.sdg.preservation.worker.log;

import edu.wisc.library.sdg.preservation.common.exception.SafeRuntimeException;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.Event;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.EventOutcome;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.EventType;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RecordIngestEventRequest;
import edu.wisc.library.sdg.preservation.worker.util.Agent;
import edu.wisc.library.sdg.preservation.worker.util.ManagerRetrier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.function.Consumer;

@Component
public class EventRecorder {

    private static final Logger LOG = LoggerFactory.getLogger(EventRecorder.class);

    private final ManagerInternalApi managerClient;

    public EventRecorder(ManagerInternalApi managerClient) {
        this.managerClient = managerClient;
    }

    /**
     * Records an event and submits it to the preservation manager. If an exception is thrown, the event is marked as
     * failed. If the exception is a SafeRuntimeException, then its message is additionally logged.
     *
     * @param type the type of event to record
     * @param ingestId the id of the ingest batch
     * @param externalObjectId the id of the object in the batch, optional
     * @param block the code to execute
     * @return the event that was recorded
     */
    public Event recordEvent(EventType type,
                             Long ingestId,
                             String externalObjectId,
                             Consumer<EventLogger> block) {
        var event = new Event()
                .type(type)
                .outcome(EventOutcome.SUCCESS)
                .agent(Agent.PRESERVATION_SERVICE_VERSION);

        var logger = new DefaultEventLogger(event);

        try {
            block.accept(logger);
            return event;
        } catch (RuntimeException e) {
            event.setOutcome(EventOutcome.FAILURE);
            if (e instanceof SafeRuntimeException) {
                logger.error(e.getMessage());
            }
            throw e;
        } finally {
            event.eventTimestamp(OffsetDateTime.now(ZoneOffset.UTC));

            try {
                ManagerRetrier.retry(() -> {
                    managerClient.recordIngestEvent(new RecordIngestEventRequest()
                            .ingestId(ingestId)
                            .externalObjectId(externalObjectId)
                            .event(event));
                });
            } catch (RuntimeException e) {
                LOG.error("Failed to record event: {}", event, e);
            }
        }
    }

}
