package edu.wisc.library.sdg.preservation.worker.event.listener;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;
import edu.wisc.library.sdg.preservation.worker.event.model.EventBusManager;
import edu.wisc.library.sdg.preservation.worker.event.model.RetrieveObjectsEvent;
import edu.wisc.library.sdg.preservation.worker.log.JobLogger;
import edu.wisc.library.sdg.preservation.worker.process.RetrieveObjectsProcessor;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RetrieveObjectsEventListener extends JobEventListener<RetrieveObjectsEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(RetrieveObjectsEventListener.class);

    private static final String NAME = "RetrieveObjects";

    private final EventBusManager eventBusManager;
    private final RetrieveObjectsProcessor processor;

    @Autowired
    public RetrieveObjectsEventListener(EventBusManager eventBusManager,
                                        MeterRegistry registry,
                                        ManagerInternalApi internalClient,
                                        RetrieveObjectsProcessor processor) {
        super(internalClient, NAME, registry);
        this.eventBusManager = eventBusManager;
        this.eventBusManager.register(this);
        this.processor = processor;
    }

    @Subscribe
    @AllowConcurrentEvents
    public void receiveEvent(RetrieveObjectsEvent event) {
        eventBusManager.process(() -> {
            super.receive(event);
        });
    }

    @Override
    protected boolean processEvent(RetrieveObjectsEvent event, JobLogger logger) {
        processor.retrieveObjects(event.getJobId(), event.getObjects());
        return true;
    }

}
