package edu.wisc.library.sdg.preservation.worker.log;

import edu.wisc.library.sdg.preservation.common.util.ArgCheck;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.LogEntry;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.LogLevel;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RecordJobLogsRequest;
import edu.wisc.library.sdg.preservation.worker.util.ManagerRetrier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

/**
 * Logs to slf4j as was as the preservation manager
 */
public class RemoteJobLogger implements JobLogger {

    private static final Logger LOG = LoggerFactory.getLogger(RemoteJobLogger.class);

    private final ManagerInternalApi managerClient;
    private final Long jobId;
    private final List<LogEntry> logs;

    public RemoteJobLogger(ManagerInternalApi managerClient, Long jobId) {
        this.managerClient = ArgCheck.notNull(managerClient, "managerClient");
        this.jobId = ArgCheck.notNull(jobId, "jobId");
        this.logs = new ArrayList<>();
    }

    @Override
    public void info(String format, String... parts) {
        var message = String.format(format, parts);
        LOG.info(message);
        addLog(LogLevel.INFO, message);
    }

    @Override
    public void info(Exception cause, String format, String... parts) {
        var message = String.format(format, parts) + String.format(" Cause: %s", cause.getMessage());
        LOG.info(message, cause);
        addLog(LogLevel.INFO, message);
    }

    @Override
    public void warn(String format, String... parts) {
        var message = String.format(format, parts);
        LOG.warn(message);
        addLog(LogLevel.WARN, message);
    }

    @Override
    public void warn(Exception cause, String format, String... parts) {
        var message = String.format(format, parts) + String.format(" Cause: %s", cause.getMessage());
        LOG.warn(message, cause);
        addLog(LogLevel.WARN, message);
    }

    @Override
    public void error(String format, String... parts) {
        var message = String.format(format, parts);
        LOG.error(message);
        addLog(LogLevel.ERROR, message);
    }

    @Override
    public void error(Exception cause, String format, String... parts) {
        var message = String.format(format, parts) + String.format(" Cause: %s", cause.getMessage());
        LOG.error(message, cause);
        addLog(LogLevel.ERROR, message);
    }

    public void report() {
        try {
            ManagerRetrier.retry(() -> {
                managerClient.recordJobLogs(new RecordJobLogsRequest()
                        .jobId(jobId)
                        .logEntries(logs));
            });
        } catch (RuntimeException e) {
            LOG.error("Failed to report logs for job <{}>", jobId, e);
        }
    }

    private void addLog(LogLevel level, String message) {
        logs.add(new LogEntry().level(level)
                .createdTimestamp(OffsetDateTime.now(ZoneOffset.UTC))
                .message(message));
    }

}
