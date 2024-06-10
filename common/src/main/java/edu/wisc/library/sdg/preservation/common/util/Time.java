package edu.wisc.library.sdg.preservation.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public final class Time {

    private Time() {

    }

    /**
     * @return current UTC time
     */
    public static LocalDateTime now() {
        return LocalDateTime.now(ZoneOffset.UTC);
    }

    public static OffsetDateTime utc(LocalDateTime time) {
        if (time == null) {
            return null;
        }
        return time.atOffset(ZoneOffset.UTC);
    }

    /**
     * Translates the incoming local date into a utc-adjusted end of day for interacting with the database.
     * For example, if the host's timezone offset is -05:00, this would return '3/14/2024' as '3/15/2024 05:00'.
     *
     * @param date
     * @return utc-adjusted end of day for the current time zone
     */
    public static LocalDateTime endOf(LocalDate date) {
        if (date == null) {
            return null;
        }

        var time = date.plusDays(1).atStartOfDay();
        return time.minusSeconds(ZoneOffset.systemDefault().getRules().getOffset(time).getTotalSeconds());
    }

    /**
     * Translates the incoming local date into a utc-adjusted start of day for interacting with the database.
     * For example, if the host's timezone offset is -05:00, this would return '3/14/2024' as '3/14/2024 05:00'
     *
     * @param date
     * @return utc-adjusted end of day for the current time zone
     */
    public static LocalDateTime startOf(LocalDate date) {
        if (date == null) {
            return null;
        }

        var time = date.atStartOfDay();
        return time.minusSeconds(ZoneOffset.systemDefault().getRules().getOffset(time).getTotalSeconds());
    }
}
