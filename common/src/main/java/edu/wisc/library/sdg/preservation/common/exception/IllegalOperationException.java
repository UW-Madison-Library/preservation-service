package edu.wisc.library.sdg.preservation.common.exception;

public class IllegalOperationException extends SafeRuntimeException {

    public IllegalOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalOperationException(String message) {
        super(message);
    }

}
