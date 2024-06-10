package edu.wisc.library.sdg.preservation.worker.event.listener;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;
import edu.wisc.library.sdg.preservation.worker.event.model.EventBusManager;
import edu.wisc.library.sdg.preservation.worker.event.model.ReplicateObjectVersionEvent;
import edu.wisc.library.sdg.preservation.worker.lock.LockManager;
import edu.wisc.library.sdg.preservation.worker.log.JobLogger;
import edu.wisc.library.sdg.preservation.worker.process.ReplicateObjectVersionProcessor;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReplicateObjectVersionEventListener extends JobEventListener<ReplicateObjectVersionEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(ReplicateObjectVersionEventListener.class);

    private static final String NAME = "ReplicateObject";

    private final EventBusManager eventBusManager;
    private final LockManager lockManager;
    private final ReplicateObjectVersionProcessor processor;

    @Autowired
    public ReplicateObjectVersionEventListener(EventBusManager eventBusManager,
                                               MeterRegistry registry,
                                               LockManager lockManager,
                                               ManagerInternalApi internalClient,
                                               ReplicateObjectVersionProcessor processor) {
        super(internalClient, NAME, registry);
        this.eventBusManager = eventBusManager;
        this.lockManager = lockManager;
        this.eventBusManager.register(this);
        this.processor = processor;
    }

    @Subscribe
    @AllowConcurrentEvents
    public void receiveEvent(ReplicateObjectVersionEvent event) {
        eventBusManager.process(() -> {
            super.receive(event);
        });
    }

    @Override
    protected boolean processEvent(ReplicateObjectVersionEvent event, JobLogger logger) {
        lockManager.doInReplicateLock(event.getObjectId(), event.getPersistenceVersion(), event.getDestination(), () -> {
            processor.replicateObjectVersion(event.getJobId(),
                    event.getObjectId(),
                    event.getVault(),
                    event.getExternalObjectId(),
                    event.getPersistenceVersion(),
                    event.getSource(),
                    event.getDestination());
        });
        return true;
    }
    
}
