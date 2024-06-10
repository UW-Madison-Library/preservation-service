package edu.wisc.library.sdg.preservation.common.exception;

public class ConcurrentUpdateException extends SafeRuntimeException {

    public ConcurrentUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConcurrentUpdateException(String message) {
        super(message);
    }

}
