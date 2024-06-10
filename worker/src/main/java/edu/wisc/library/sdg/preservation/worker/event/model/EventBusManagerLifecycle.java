package edu.wisc.library.sdg.preservation.worker.event.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

@Component
public class EventBusManagerLifecycle implements SmartLifecycle {

    private final EventBusManager eventBusManager;

    private boolean running;

    @Autowired
    public EventBusManagerLifecycle(EventBusManager eventBusManager) {
        this.eventBusManager = eventBusManager;
    }

    @Override
    public void start() {
        running = true;
    }

    @Override
    public void stop() {
        throw new UnsupportedOperationException("Stop must not be invoked directly");
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public void stop(Runnable callback) {
        running = false;
        eventBusManager.stop(callback);
    }

    @Override
    public int getPhase() {
        return SmartLifecycle.DEFAULT_PHASE;
    }
}
