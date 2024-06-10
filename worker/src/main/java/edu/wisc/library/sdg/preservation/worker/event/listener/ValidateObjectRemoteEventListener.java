package edu.wisc.library.sdg.preservation.worker.event.listener;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;
import edu.wisc.library.sdg.preservation.worker.event.model.EventBusManager;
import edu.wisc.library.sdg.preservation.worker.event.model.ValidateObjectRemoteEvent;
import edu.wisc.library.sdg.preservation.worker.log.JobLogger;
import edu.wisc.library.sdg.preservation.worker.process.ValidateObjectRemoteProcessor;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidateObjectRemoteEventListener extends JobEventListener<ValidateObjectRemoteEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(ValidateObjectRemoteEventListener.class);

    private static final String NAME = "ValidateObjectRemote";

    private final EventBusManager eventBusManager;
    private final ValidateObjectRemoteProcessor processor;

    @Autowired
    public ValidateObjectRemoteEventListener(EventBusManager eventBusManager,
                                             MeterRegistry registry,
                                             ManagerInternalApi internalClient,
                                             ValidateObjectRemoteProcessor processor) {
        super(internalClient, NAME, registry);
        this.eventBusManager = eventBusManager;
        this.processor = processor;
        this.eventBusManager.register(this);
    }

    @Subscribe
    @AllowConcurrentEvents
    public void receive(ValidateObjectRemoteEvent event) {
        eventBusManager.process(() -> {
            super.receive(event);
        });
    }

    @Override
    protected boolean processEvent(ValidateObjectRemoteEvent event, JobLogger logger) {
        return processor.validate(event.getJobId(),
                event.getObjectId(),
                event.getPersistenceVersion(),
                event.getDataStore(),
                event.getDataStoreKey(),
                event.getSha256Digest());
    }
    
}
