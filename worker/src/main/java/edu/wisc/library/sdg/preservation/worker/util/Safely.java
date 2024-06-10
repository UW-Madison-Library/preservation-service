package edu.wisc.library.sdg.preservation.worker.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

public final class Safely {

    private static final Logger LOG = LoggerFactory.getLogger(Safely.class);

    private Safely() {

    }

    public static void exec(Runnable exec, Runnable onFailure, Supplier<String> messageSupplier) {
        try {
            exec.run();
        } catch (Exception e) {
            Safely.exec(onFailure, messageSupplier);

            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }

            throw new RuntimeException(e);
        }
    }

    public static void exec(Runnable exec, Supplier<String> messageSupplier) {
        try {
            exec.run();
        } catch (Exception e) {
            LOG.error(messageSupplier.get(), e);
        }
    }

    public interface Runnable {
        void run() throws Exception;
    }

}
