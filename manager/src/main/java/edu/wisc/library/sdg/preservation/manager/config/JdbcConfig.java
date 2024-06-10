package edu.wisc.library.sdg.preservation.manager.config;

import edu.wisc.library.sdg.preservation.common.util.ArgCheck;
import edu.wisc.library.sdg.preservation.manager.db.model.DataStore;
import edu.wisc.library.sdg.preservation.manager.db.model.EnumAsShort;
import edu.wisc.library.sdg.preservation.manager.db.model.EventOutcome;
import edu.wisc.library.sdg.preservation.manager.db.model.EventType;
import edu.wisc.library.sdg.preservation.manager.db.model.FormatRegistry;
import edu.wisc.library.sdg.preservation.manager.db.model.IngestBatchState;
import edu.wisc.library.sdg.preservation.manager.db.model.IngestObjectState;
import edu.wisc.library.sdg.preservation.manager.db.model.JobState;
import edu.wisc.library.sdg.preservation.manager.db.model.JobType;
import edu.wisc.library.sdg.preservation.manager.db.model.LogLevel;
import edu.wisc.library.sdg.preservation.manager.db.model.OrgRole;
import edu.wisc.library.sdg.preservation.manager.db.model.Permission;
import edu.wisc.library.sdg.preservation.manager.db.model.PreservationObjectState;
import edu.wisc.library.sdg.preservation.manager.db.model.PreservationUserType;
import edu.wisc.library.sdg.preservation.manager.db.model.StorageProblemType;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.jdbc.core.mapping.JdbcValue;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;

import javax.annotation.Nullable;
import java.lang.reflect.Array;
import java.sql.Clob;
import java.sql.JDBCType;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@Configuration
public class JdbcConfig extends AbstractJdbcConfiguration {

    @Override
    protected List<?> userConverters() {
        return List.of(
                // Source: https://stackoverflow.com/questions/60041952/spring-data-jdbc-clob-column-to-string-property-in-pojo-not-working
                new Converter<Clob, String>() {
                    @Nullable
                    @Override
                    public String convert(Clob clob) {
                        try {
                            return Math.toIntExact(clob.length()) == 0 //
                                    ? "" //
                                    : clob.getSubString(1, Math.toIntExact(clob.length()));

                        } catch (SQLException e) {
                            throw new IllegalStateException("Failed to convert CLOB to String.", e);
                        }
                    }
                },
                EnumWritingConverter.INSTANCE,
                // All enums that are stored as ints must implement EnumAsShort and be declared here
                new ShortDataStoreConverter(),
                new IntDataStoreConverter(),
                new ShortEventOutcomeConverter(),
                new IntEventOutcomeConverter(),
                new ShortEventTypeConverter(),
                new IntEventTypeConverter(),
                new ShortDataStoreConverter(),
                new IntDataStoreConverter(),
                new ShortFormatRegistryConverter(),
                new IntFormatRegistryConverter(),
                new ShortIngestBatchStateConverter(),
                new IntIngestBatchStateConverter(),
                new ShortIngestObjectStateConverter(),
                new IntIngestObjectStateConverter(),
                new ShortJobStateConverter(),
                new IntJobStateConverter(),
                new ShortJobTypeConverter(),
                new IntJobTypeConverter(),
                new ShortLogLevelConverter(),
                new IntLogLevelConverter(),
                new ShortOrgRoleConverter(),
                new IntOrgRoleConverter(),
                new ShortPermissionConverter(),
                new IntPermissionConverter(),
                new ShortPreservationUserTypeConverter(),
                new IntPreservationUserTypeConverter(),
                new ShortStorageProblemTypeConverter(),
                new IntStorageProblemTypeConverter(),
                new ShortPreservationObjectStateConverter(),
                new IntPreservationObjectStateConverter()
        );
    }

    @WritingConverter
    public enum EnumWritingConverter implements Converter<EnumAsShort, JdbcValue> {
        INSTANCE;

        @Override
        public JdbcValue convert(EnumAsShort anEnum) {
            return JdbcValue.of(anEnum.asShort(), JDBCType.INTEGER);
        }
    }

    /**
     * Each enum must implement this class because the converters are mapped by type and the type is earased here.
     */
    @ReadingConverter
    public static abstract class EnumReadingConverter<N extends Number, E extends EnumAsShort> implements Converter<N, E> {

        private final E[] enums;

        public EnumReadingConverter(Class<E> type) {
            ArgCheck.notNull(type, "type");

            var enumValues = type.getEnumConstants();
            if (enumValues == null) {
                throw new IllegalArgumentException(type.getSimpleName() + " does not represent an enum type.");
            }

            short maxIndex = Arrays.stream(enumValues)
                    .map(EnumAsShort::asShort)
                    .max(Short::compareTo)
                    .orElse((short) 0);

            if (maxIndex < 1) {
                throw new IllegalArgumentException(type.getSimpleName() + " max enum index must be greater than 0");
            }

            enums = (E[]) Array.newInstance(type, maxIndex);

            for (E value : enumValues) {
                if (enums[value.asShort() - 1] != null) {
                    throw new IllegalArgumentException(type.getSimpleName()
                            + " has multiple variants with the same int value: " + value.asShort());
                }
                enums[value.asShort() - 1] = value;
            }
        }

        @Override
        public E convert(N i) {
            if (i.shortValue() > 0) {
                return enums[i.shortValue() - 1];
            }

            throw new IllegalArgumentException("Index must be greater than 0. Was: " + i);
        }
    }

    @ReadingConverter
    public static class ShortDataStoreConverter extends EnumReadingConverter<Short, DataStore> {
        public ShortDataStoreConverter() {
            super(DataStore.class);
        }
    }

    @ReadingConverter
    public static class IntDataStoreConverter extends EnumReadingConverter<Integer, DataStore> {
        public IntDataStoreConverter() {
            super(DataStore.class);
        }
    }

    @ReadingConverter
    public static class ShortEventOutcomeConverter extends EnumReadingConverter<Short, EventOutcome> {
        public ShortEventOutcomeConverter() {
            super(EventOutcome.class);
        }
    }

    @ReadingConverter
    public static class IntEventOutcomeConverter extends EnumReadingConverter<Integer, EventOutcome> {
        public IntEventOutcomeConverter() {
            super(EventOutcome.class);
        }
    }

    @ReadingConverter
    public static class ShortEventTypeConverter extends EnumReadingConverter<Short, EventType> {
        public ShortEventTypeConverter() {
            super(EventType.class);
        }
    }

    @ReadingConverter
    public static class IntEventTypeConverter extends EnumReadingConverter<Integer, EventType> {
        public IntEventTypeConverter() {
            super(EventType.class);
        }
    }

    @ReadingConverter
    public static class ShortFormatRegistryConverter extends EnumReadingConverter<Short, FormatRegistry> {
        public ShortFormatRegistryConverter() {
            super(FormatRegistry.class);
        }
    }

    @ReadingConverter
    public static class IntFormatRegistryConverter extends EnumReadingConverter<Integer, FormatRegistry> {
        public IntFormatRegistryConverter() {
            super(FormatRegistry.class);
        }
    }

    @ReadingConverter
    public static class ShortIngestBatchStateConverter extends EnumReadingConverter<Short, IngestBatchState> {
        public ShortIngestBatchStateConverter() {
            super(IngestBatchState.class);
        }
    }

    @ReadingConverter
    public static class IntIngestBatchStateConverter extends EnumReadingConverter<Integer, IngestBatchState> {
        public IntIngestBatchStateConverter() {
            super(IngestBatchState.class);
        }
    }

    @ReadingConverter
    public static class ShortIngestObjectStateConverter extends EnumReadingConverter<Short, IngestObjectState> {
        public ShortIngestObjectStateConverter() {
            super(IngestObjectState.class);
        }
    }

    @ReadingConverter
    public static class IntIngestObjectStateConverter extends EnumReadingConverter<Integer, IngestObjectState> {
        public IntIngestObjectStateConverter() {
            super(IngestObjectState.class);
        }
    }

    @ReadingConverter
    public static class ShortJobStateConverter extends EnumReadingConverter<Short, JobState> {
        public ShortJobStateConverter() {
            super(JobState.class);
        }
    }

    @ReadingConverter
    public static class IntJobStateConverter extends EnumReadingConverter<Integer, JobState> {
        public IntJobStateConverter() {
            super(JobState.class);
        }
    }

    @ReadingConverter
    public static class ShortJobTypeConverter extends EnumReadingConverter<Short, JobType> {
        public ShortJobTypeConverter() {
            super(JobType.class);
        }
    }

    @ReadingConverter
    public static class IntJobTypeConverter extends EnumReadingConverter<Integer, JobType> {
        public IntJobTypeConverter() {
            super(JobType.class);
        }
    }

    @ReadingConverter
    public static class ShortLogLevelConverter extends EnumReadingConverter<Short, LogLevel> {
        public ShortLogLevelConverter() {
            super(LogLevel.class);
        }
    }

    @ReadingConverter
    public static class IntLogLevelConverter extends EnumReadingConverter<Integer, LogLevel> {
        public IntLogLevelConverter() {
            super(LogLevel.class);
        }
    }

    @ReadingConverter
    public static class ShortOrgRoleConverter extends EnumReadingConverter<Short, OrgRole> {
        public ShortOrgRoleConverter() {
            super(OrgRole.class);
        }
    }

    @ReadingConverter
    public static class IntOrgRoleConverter extends EnumReadingConverter<Integer, OrgRole> {
        public IntOrgRoleConverter() {
            super(OrgRole.class);
        }
    }

    @ReadingConverter
    public static class ShortPermissionConverter extends EnumReadingConverter<Short, Permission> {
        public ShortPermissionConverter() {
            super(Permission.class);
        }
    }

    @ReadingConverter
    public static class IntPermissionConverter extends EnumReadingConverter<Integer, Permission> {
        public IntPermissionConverter() {
            super(Permission.class);
        }
    }

    @ReadingConverter
    public static class ShortPreservationUserTypeConverter extends EnumReadingConverter<Short, PreservationUserType> {
        public ShortPreservationUserTypeConverter() {
            super(PreservationUserType.class);
        }
    }

    @ReadingConverter
    public static class IntPreservationUserTypeConverter extends EnumReadingConverter<Integer, PreservationUserType> {
        public IntPreservationUserTypeConverter() {
            super(PreservationUserType.class);
        }
    }

    @ReadingConverter
    public static class ShortStorageProblemTypeConverter extends EnumReadingConverter<Short, StorageProblemType> {
        public ShortStorageProblemTypeConverter() {
            super(StorageProblemType.class);
        }
    }

    @ReadingConverter
    public static class IntStorageProblemTypeConverter extends EnumReadingConverter<Integer, StorageProblemType> {
        public IntStorageProblemTypeConverter() {
            super(StorageProblemType.class);
        }
    }

    @ReadingConverter
    public static class ShortPreservationObjectStateConverter extends EnumReadingConverter<Short, PreservationObjectState> {
        public ShortPreservationObjectStateConverter() {
            super(PreservationObjectState.class);
        }
    }

    @ReadingConverter
    public static class IntPreservationObjectStateConverter extends EnumReadingConverter<Integer, PreservationObjectState> {
        public IntPreservationObjectStateConverter() {
            super(PreservationObjectState.class);
        }
    }
    
}
