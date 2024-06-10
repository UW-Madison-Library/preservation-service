package edu.wisc.library.sdg.preservation.worker.log;

/**
 * Log interface for logging job information
 */
public interface JobLogger {

    void info(String format, String... parts);

    void info(Exception cause, String format, String... parts);

    void warn(String format, String... parts);

    void warn(Exception cause, String format, String... parts);

    void error(String format, String... parts);

    void error(Exception cause, String format, String... parts);

}
