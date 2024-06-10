package edu.wisc.library.sdg.preservation.worker.log;

import edu.wisc.library.sdg.preservation.manager.client.internal.model.EventOutcome;

public interface EventLogger {

    void info(String format, String... parts);

    void warn(String format, String... parts);

    void error(String format, String... parts);

    void outcome(EventOutcome outcome);

}
