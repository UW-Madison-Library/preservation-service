package edu.wisc.library.sdg.preservation.common.util;

import edu.wisc.library.sdg.preservation.common.exception.ValidationException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RequestFieldValidator {

    private RequestFieldValidator() {

    }

    public static <T> T notNull(T object, String name) {
        if (object == null) {
            throw new ValidationException(String.format("Field '%s' cannot be null", name));
        }
        return object;
    }

    public static String notBlank(String object, String name) {
        if (object == null || object.isBlank()) {
            throw new ValidationException(String.format("Field '%s' cannot be blank", name));
        }
        return object;
    }

    public static String matchPattern(String object, Pattern pattern, String name, String message) {
        var matcher = pattern.matcher(object);
        var match = matcher.matches();
        if (!match) {
            throw new ValidationException(String.format("Field '%s' must %s", name, message));
        }
        return object;
    }

}
