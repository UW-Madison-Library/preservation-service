package edu.wisc.library.sdg.preservation.worker.event.listener;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;
import edu.wisc.library.sdg.preservation.worker.event.model.DeleteDipEvent;
import edu.wisc.library.sdg.preservation.worker.event.model.EventBusManager;
import edu.wisc.library.sdg.preservation.worker.log.JobLogger;
import edu.wisc.library.sdg.preservation.worker.process.DeleteDipProcessor;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeleteDipEventListener extends JobEventListener<DeleteDipEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(DeleteDipEventListener.class);

    private static final String NAME = "DeleteDip";

    private final EventBusManager eventBusManager;
    private final DeleteDipProcessor processor;

    @Autowired
    public DeleteDipEventListener(EventBusManager eventBusManager,
                                  MeterRegistry registry,
                                  ManagerInternalApi internalClient,
                                  DeleteDipProcessor processor) {
        super(internalClient, NAME, registry);
        this.eventBusManager = eventBusManager;
        this.eventBusManager.register(this);
        this.processor = processor;
    }

    @Subscribe
    @AllowConcurrentEvents
    public void receiveEvent(DeleteDipEvent event) {
        eventBusManager.process(() -> {
            super.receive(event);
        });
    }

    @Override
    protected boolean processEvent(DeleteDipEvent event, JobLogger logger) {
        processor.deleteDip(event.getJobId(), event.getRetrieveRequestId(), event.getRetrieveJobIds());
        return true;
    }
    
}
