package edu.wisc.library.sdg.preservation.manager.util;

import com.github.dozermapper.core.DozerConverter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * Dozer converter for translating between OffsetDateTime and LocalDateTime
 */
public class UtcTimestampConverter  extends DozerConverter<OffsetDateTime, LocalDateTime> {

    public UtcTimestampConverter() {
        super(OffsetDateTime.class, LocalDateTime.class);
    }

    @Override
    public LocalDateTime convertTo(OffsetDateTime source, LocalDateTime destination) {
        if (source == null) {
            return null;
        }
        return source.atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
    }

    @Override
    public OffsetDateTime convertFrom(LocalDateTime source, OffsetDateTime destination) {
        if (source == null) {
            return null;
        }
        return source.atOffset(ZoneOffset.UTC);
    }

}
