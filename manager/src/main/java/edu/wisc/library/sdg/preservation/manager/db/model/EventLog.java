package edu.wisc.library.sdg.preservation.manager.db.model;

import java.time.LocalDateTime;

public interface EventLog {

    LogLevel getLevel();

    String getMessage();

    LocalDateTime getCreatedTimestamp();

}
