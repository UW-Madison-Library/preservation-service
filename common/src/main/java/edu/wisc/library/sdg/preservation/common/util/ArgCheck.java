package edu.wisc.library.sdg.preservation.common.util;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

public final class ArgCheck {

    private ArgCheck() {

    }

    public static <T> T notNull(T object, String name) {
        return Preconditions.checkNotNull(object, "%s cannot be null", name);
    }

    public static String notBlank(String object, String name) {
        return Preconditions.checkNotNull(Strings.emptyToNull(object), "%s cannot be blank", name);
    }

    public static <T> T condition(T object, boolean condition, String message) {
        Preconditions.checkArgument(condition, message);
        return object;
    }

}
