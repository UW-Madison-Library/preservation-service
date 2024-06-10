package edu.wisc.library.sdg.preservation.worker.event.listener;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;
import edu.wisc.library.sdg.preservation.worker.event.model.EventBusManager;
import edu.wisc.library.sdg.preservation.worker.event.model.RestoreObjectVersionEvent;
import edu.wisc.library.sdg.preservation.worker.log.JobLogger;
import edu.wisc.library.sdg.preservation.worker.process.RestoreObjectVersionProcessor;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RestoreObjectVersionEventListener extends JobEventListener<RestoreObjectVersionEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(RestoreObjectVersionEventListener.class);

    private static final String NAME = "RestoreObject";

    private final EventBusManager eventBusManager;
    private final RestoreObjectVersionProcessor processor;

    @Autowired
    public RestoreObjectVersionEventListener(EventBusManager eventBusManager,
                                             MeterRegistry registry,
                                             ManagerInternalApi internalClient,
                                             RestoreObjectVersionProcessor processor) {
        super(internalClient, NAME, registry);
        this.eventBusManager = eventBusManager;
        this.processor = processor;
        this.eventBusManager.register(this);
    }

    @Subscribe
    @AllowConcurrentEvents
    public void receive(RestoreObjectVersionEvent event) {
        eventBusManager.process(() -> {
            super.receive(event);
        });
    }

    @Override
    protected boolean processEvent(RestoreObjectVersionEvent event, JobLogger logger) {
        return processor.restore(event.getJobId(),
                event.getObjectId(),
                event.getPersistenceVersion(),
                event.getSource(),
                event.getKey(),
                event.getSha256Digest());
    }

}
