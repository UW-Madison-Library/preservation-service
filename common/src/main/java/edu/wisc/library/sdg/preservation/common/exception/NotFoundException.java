package edu.wisc.library.sdg.preservation.common.exception;

public class NotFoundException extends SafeRuntimeException {

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundException(String message) {
        super(message);
    }

}
