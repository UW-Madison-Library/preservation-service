package edu.wisc.library.sdg.preservation.worker.log;

import edu.wisc.library.sdg.preservation.manager.client.internal.model.Event;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.EventOutcome;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.LogEntry;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.LogLevel;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class DefaultEventLogger implements EventLogger {

    private final Event event;

    public DefaultEventLogger(Event event) {
        this.event = event.outcome(EventOutcome.SUCCESS);
    }

    @Override
    public void info(String format, String... parts) {
        event.addLogsItem(new LogEntry()
                .level(LogLevel.INFO)
                .createdTimestamp(OffsetDateTime.now(ZoneOffset.UTC))
                .message(formatMessage(format, parts)));
    }

    @Override
    public void warn(String format, String... parts) {
        if (event.getOutcome() == EventOutcome.SUCCESS) {
            event.setOutcome(EventOutcome.SUCCESS_WITH_WARNINGS);
        }

        event.addLogsItem(new LogEntry()
                .level(LogLevel.WARN)
                .createdTimestamp(OffsetDateTime.now(ZoneOffset.UTC))
                .message(formatMessage(format, parts)));
    }

    @Override
    public void error(String format, String... parts) {
        event.setOutcome(EventOutcome.FAILURE);
        event.addLogsItem(new LogEntry()
                .level(LogLevel.ERROR)
                .createdTimestamp(OffsetDateTime.now(ZoneOffset.UTC))
                .message(formatMessage(format, parts)));
    }

    @Override
    public void outcome(EventOutcome outcome) {
        event.setOutcome(outcome);
    }

    private String formatMessage(String format, String... parts) {
        if (parts == null || parts.length == 0) {
            return format;
        } else {
            return String.format(format, (Object[]) parts);
        }
    }

}
