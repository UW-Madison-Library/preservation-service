package edu.wisc.library.sdg.preservation.common.exception;

/**
 * RuntimeException with a message that's safe to return to the user.
 */
public class SafeRuntimeException extends RuntimeException {

    public SafeRuntimeException(String message) {
        super(message);
    }

    public SafeRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

}
