package edu.wisc.library.sdg.preservation.worker.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

@Component
public class JobPollerLifecycle implements SmartLifecycle {

    private final JobPoller jobPoller;

    private boolean running;

    @Autowired
    public JobPollerLifecycle(JobPoller jobPoller) {
        this.jobPoller = jobPoller;
    }

    @Override
    public void start() {
        running = true;
        jobPoller.start();
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
        jobPoller.stop(callback);
    }

    @Override
    public int getPhase() {
        return SmartLifecycle.DEFAULT_PHASE;
    }
}
