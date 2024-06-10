package edu.wisc.library.sdg.preservation.common.exception;

import edu.wisc.library.sdg.preservation.common.client.ErrorResponse;

public class HttpClientErrorResponseException extends RuntimeException {

    private final ErrorResponse errorResponse;

    public HttpClientErrorResponseException(ErrorResponse errorResponse) {
        super(String.format("HTTP Error: %s", errorResponse));
        this.errorResponse = errorResponse;
    }

    public int getHttpStatus() {
        return errorResponse.getStatus();
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }

    @Override
    public String toString() {
        return "HttpClientErrorResponseException{" +
                "errorResponse=" + errorResponse +
                '}';
    }

}
