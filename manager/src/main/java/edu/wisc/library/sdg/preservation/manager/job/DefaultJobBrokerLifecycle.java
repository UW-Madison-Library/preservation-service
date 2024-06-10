package edu.wisc.library.sdg.preservation.manager.job;

import org.springframework.context.SmartLifecycle;

public class DefaultJobBrokerLifecycle implements SmartLifecycle {

    private final DefaultJobBroker jobBroker;

    private boolean running;

    public DefaultJobBrokerLifecycle(DefaultJobBroker jobBroker) {
        this.jobBroker = jobBroker;
    }

    @Override
    public void start() {
        running = true;
        jobBroker.start();
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
        jobBroker.stop(callback);
    }

    @Override
    public int getPhase() {
        return SmartLifecycle.DEFAULT_PHASE;
    }
}
