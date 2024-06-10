package edu.wisc.library.sdg.preservation.common.util;

import java.util.UUID;

public final class UuidUtils {

    private static final String PREFIX = "urn:uuid:";

    private UuidUtils() {

    }

    public static UUID fromString(String uuid) {
        ArgCheck.notBlank(uuid, "uuid");
        if (uuid.startsWith(PREFIX)) {
            return UUID.fromString(uuid.substring(PREFIX.length()));
        } else {
            return UUID.fromString(uuid);
        }
    }

    public static String newWithPrefix() {
        return PREFIX + UUID.randomUUID();
    }

    public static String withPrefix(String uuid) {
        ArgCheck.notBlank(uuid, "uuid");
        if (uuid.startsWith(PREFIX)) {
            return uuid;
        } else {
            return PREFIX + uuid;
        }
    }

    public static String withPrefix(UUID uuid) {
        ArgCheck.notNull(uuid, "uuid");
        return PREFIX + uuid;
    }

    public static String withoutPrefix(String uuid) {
        ArgCheck.notBlank(uuid, "uuid");
        if (uuid.startsWith(PREFIX)) {
            return uuid.substring(PREFIX.length());
        } else {
            return uuid;
        }
    }

}
