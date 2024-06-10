package edu.wisc.library.sdg.preservation.common.exception;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum ErrorCode {

    ACCESS_DENIED("AccessDenied", AccessDeniedException.class),
    CONCURRENT_UPDATE("ConcurrentUpdate", ConcurrentUpdateException.class),
    ILLEGAL_OPERATION("IllegalOperation", IllegalOperationException.class),
    INTERNAL_ERROR("InternalError", InternalErrorException.class),
    NOT_FOUND("NotFound", NotFoundException.class),
    INVALID_REQUEST("InvalidRequest", ValidationException.class);

    private static final Logger LOG = LoggerFactory.getLogger(ErrorCode.class);

    private final String code;
    private final Class<? extends RuntimeException> clazz;

    ErrorCode(String code, Class<? extends RuntimeException> clazz) {
        this.code = code;
        this.clazz = clazz;
    }

    @JsonCreator
    public static ErrorCode fromString(String code) {
        for (var value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        throw new IllegalArgumentException("Unknown error code: " + code);
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    public RuntimeException newException(String message, RuntimeException cause) {
        try {
            return clazz.getDeclaredConstructor(String.class, Throwable.class).newInstance(message, cause);
        } catch (Exception e) {
            LOG.error("Failed to map http exception", e);
            return cause;
        }
    }

}
