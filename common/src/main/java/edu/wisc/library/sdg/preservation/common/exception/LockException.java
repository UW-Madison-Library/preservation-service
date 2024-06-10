package edu.wisc.library.sdg.preservation.common.exception;

public class LockException extends SafeRuntimeException {

    public LockException(String message) {
        super(message);
    }

    public LockException(String message, Throwable cause) {
        super(message, cause);
    }

}
