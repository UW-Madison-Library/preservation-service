package edu.wisc.library.sdg.preservation.common.exception;

public class AccessDeniedException extends SafeRuntimeException {

    public AccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccessDeniedException(String message) {
        super(message);
    }

}
