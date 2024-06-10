package edu.wisc.library.sdg.preservation.worker.event.listener;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;
import edu.wisc.library.sdg.preservation.worker.event.model.EventBusManager;
import edu.wisc.library.sdg.preservation.worker.event.model.ValidateObjectLocalEvent;
import edu.wisc.library.sdg.preservation.worker.lock.LockManager;
import edu.wisc.library.sdg.preservation.worker.log.JobLogger;
import edu.wisc.library.sdg.preservation.worker.process.ValidateObjectLocalProcessor;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidateObjectLocalEventListener extends JobEventListener<ValidateObjectLocalEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(ValidateObjectLocalEventListener.class);

    private static final String NAME = "ValidateObjectLocal";

    private final EventBusManager eventBusManager;
    private final LockManager lockManager;
    private final ValidateObjectLocalProcessor processor;

    @Autowired
    public ValidateObjectLocalEventListener(EventBusManager eventBusManager,
                                            MeterRegistry registry,
                                            ManagerInternalApi internalClient,
                                            LockManager lockManager,
                                            ValidateObjectLocalProcessor processor) {
        super(internalClient, NAME, registry);
        this.eventBusManager = eventBusManager;
        this.eventBusManager.register(this);
        this.lockManager = lockManager;
        this.processor = processor;
    }

    @Subscribe
    @AllowConcurrentEvents
    public void receiveEvent(ValidateObjectLocalEvent event) {
        eventBusManager.process(() -> {
            super.receive(event);
        });
    }

    @Override
    protected boolean processEvent(ValidateObjectLocalEvent event, JobLogger logger) {
        lockManager.doInLocalOcflLock(event.getObjectId(), () -> {
            processor.validateObject(event.getObjectId(), event.isContentFixityCheck());
        });
        return true;
    }
    
}
