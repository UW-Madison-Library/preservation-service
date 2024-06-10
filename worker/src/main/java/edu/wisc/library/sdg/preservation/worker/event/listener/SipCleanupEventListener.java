package edu.wisc.library.sdg.preservation.worker.event.listener;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;
import edu.wisc.library.sdg.preservation.worker.event.model.CleanupSipsEvent;
import edu.wisc.library.sdg.preservation.worker.event.model.EventBusManager;
import edu.wisc.library.sdg.preservation.worker.log.JobLogger;
import edu.wisc.library.sdg.preservation.worker.process.SipCleanupProcessor;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SipCleanupEventListener extends JobEventListener<CleanupSipsEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(SipCleanupEventListener.class);

    private static final String NAME = "SipCleanup";

    private final EventBusManager eventBusManager;
    private final SipCleanupProcessor processor;

    @Autowired
    public SipCleanupEventListener(EventBusManager eventBusManager,
                                   MeterRegistry registry,
                                   ManagerInternalApi internalClient,
                                   SipCleanupProcessor processor) {
        super(internalClient, NAME, registry);
        this.eventBusManager = eventBusManager;
        this.eventBusManager.register(this);
        this.processor = processor;
    }

    @Subscribe
    @AllowConcurrentEvents
    public void receiveEvent(CleanupSipsEvent event) {
        eventBusManager.process(() -> {
            super.receive(event);
        });
    }

    @Override
    protected boolean processEvent(CleanupSipsEvent event, JobLogger logger) {
        processor.cleanupSips();
        return true;
    }
    
}
