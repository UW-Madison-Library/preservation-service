package edu.wisc.library.sdg.preservation.worker.event.model;

import com.google.common.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Wrapper around the event bus to facilitate graceful shutdowns. Once stopped, it will not allow any additional
 * events to be processed, and will wait for all current jobs to complete.
 *
 * In order for this to work, all listeners MUST do all of their processing with in the process() method defined
 * here so that the manager can keep track of what work is being processed.
 */
@Component
public class EventBusManager {

    private static final Logger LOG = LoggerFactory.getLogger(EventBusManager.class);

    private final EventBus eventBus;

    private boolean running;
    private final Phaser phaser;
    private final AtomicInteger count;
    private final Lock lock;
    private final Condition eventComplete;

    @Autowired
    public EventBusManager(EventBus eventBus) {
        this.eventBus = eventBus;
        running = true;
        phaser = new Phaser();
        count = new AtomicInteger(0);
        lock = new ReentrantLock();
        eventComplete = lock.newCondition();
    }

    /**
     * Pass through to post an event on the event bus. Will through an IllegalStateException if stop has been called.
     *
     * @param event the event to process
     */
    public void post(Object event) {
        if (!running) {
            throw new IllegalStateException("Cannot process jobs right now -- server is shutting down.");
        }

        count.incrementAndGet();
        eventBus.post(event);
    }

    /**
     * Pass through to register an event listener
     *
     * @param object the listener to register
     */
    public void register(Object object) {
        eventBus.register(object);
    }

    /**
     * Listeners must use this method to execute their logic so that the manager can track in-flight events
     *
     * @param runnable the listener code to execute
     */
    public void process(Runnable runnable) {
        phaser.register();
        try {
            runnable.run();
        } finally {
            lock.lock();
            try {
                count.decrementAndGet();
                eventComplete.signal();
            } finally {
                lock.unlock();
            }
            phaser.arriveAndDeregister();
        }
    }

    /**
     * Instructs the manager to no longer accept new events and waits for all in-flight events to complete. The callback
     * is called on completion.
     *
     * @param callback the callback to call when all work is done
     */
    public void stop(Runnable callback) {
        running = false;

        LOG.info("Stopping event bus and waiting for {} jobs to finish...", phaser.getUnarrivedParties());

        phaser.register();
        phaser.arriveAndAwaitAdvance();

        LOG.info("All jobs complete");

        callback.run();
    }

    /**
     * Waits indefinitely until there are fewer inflight events than the specified number
     *
     * @throws InterruptedException
     */
    public void waitForMaxEvents(int max) throws InterruptedException {
        lock.lockInterruptibly();
        try {
            if (count.get() >= max) {
                LOG.debug("Waiting for events to complete...");
                eventComplete.await();
            }
        } finally {
            lock.unlock();
        }
    }

}
