package edu.wisc.library.sdg.preservation.common.exception;

public class HttpClientException extends RuntimeException {

    private final int httpStatus;
    private final String responseBody;

    public HttpClientException(int httpStatus, String responseBody) {
        super(String.format("HTTP Error: status<%s>; responseBody<%s>", httpStatus, responseBody));
        this.httpStatus = httpStatus;
        this.responseBody = responseBody;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public String getResponseBody() {
        return responseBody;
    }

    @Override
    public String toString() {
        return "HttpClientException{" +
                "httpStatus=" + httpStatus +
                ", responseBody='" + responseBody + '\'' +
                '}';
    }

}
