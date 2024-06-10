package edu.wisc.library.sdg.preservation.common.exception;

public class InternalErrorException extends SafeRuntimeException {

    public InternalErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public InternalErrorException(String message) {
        super(message);
    }

}
