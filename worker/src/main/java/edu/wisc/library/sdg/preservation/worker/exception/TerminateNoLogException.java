package edu.wisc.library.sdg.preservation.worker.exception;

/**
 * The point of this exception is to terminate the processing of a batch and indicate that this exception should NOT
 * be logged to the server. The normal reason for wanting to do this is if you already recorded errors to the log and
 * now want to terminate but don't want a redundant message.
 */
public class TerminateNoLogException extends RuntimeException {

    public TerminateNoLogException(String message) {
        super(message);
    }

    public TerminateNoLogException(String message, Throwable cause) {
        super(message, cause);
    }

}
