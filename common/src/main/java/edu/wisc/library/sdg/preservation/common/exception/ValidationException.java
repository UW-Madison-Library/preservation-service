package edu.wisc.library.sdg.preservation.common.exception;

public class ValidationException extends SafeRuntimeException {

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationException(String message) {
        super(message);
    }

}
