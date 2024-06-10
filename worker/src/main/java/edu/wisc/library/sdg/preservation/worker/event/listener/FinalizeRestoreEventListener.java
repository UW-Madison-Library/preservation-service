package edu.wisc.library.sdg.preservation.worker.event.listener;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;
import edu.wisc.library.sdg.preservation.worker.event.model.EventBusManager;
import edu.wisc.library.sdg.preservation.worker.event.model.FinalizeRestoreEvent;
import edu.wisc.library.sdg.preservation.worker.lock.LockManager;
import edu.wisc.library.sdg.preservation.worker.log.JobLogger;
import edu.wisc.library.sdg.preservation.worker.process.FinalizeRestoreProcessor;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FinalizeRestoreEventListener extends JobEventListener<FinalizeRestoreEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(FinalizeRestoreEventListener.class);

    private static final String NAME = "FinalizeRestore";

    private final EventBusManager eventBusManager;
    private final LockManager lockManager;
    private final FinalizeRestoreProcessor processor;

    @Autowired
    public FinalizeRestoreEventListener(EventBusManager eventBusManager,
                                        MeterRegistry registry,
                                        ManagerInternalApi internalClient,
                                        LockManager lockManager,
                                        FinalizeRestoreProcessor processor) {
        super(internalClient, NAME, registry);
        this.eventBusManager = eventBusManager;
        this.eventBusManager.register(this);
        this.lockManager = lockManager;
        this.processor = processor;
    }

    @Subscribe
    @AllowConcurrentEvents
    public void receiveEvent(FinalizeRestoreEvent event) {
        eventBusManager.process(() -> {
            super.receive(event);
        });
    }

    @Override
    protected boolean processEvent(FinalizeRestoreEvent event, JobLogger logger) {
        lockManager.doInLocalOcflLock(event.getObjectId(), () -> {
            processor.finalizeRestore(event.getObjectId());
        });
        return true;
    }
    
}
