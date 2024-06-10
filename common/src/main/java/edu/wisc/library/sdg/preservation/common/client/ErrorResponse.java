package edu.wisc.library.sdg.preservation.common.client;

import edu.wisc.library.sdg.preservation.common.exception.ErrorCode;

import java.time.OffsetDateTime;

public class ErrorResponse {

    private OffsetDateTime timestamp;
    private String requestId;
    private Integer status;
    private ErrorCode error;
    private String message;
    private String path;

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public ErrorResponse setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String getRequestId() {
        return requestId;
    }

    public ErrorResponse setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public ErrorResponse setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public ErrorCode getError() {
        return error;
    }

    public ErrorResponse setError(ErrorCode error) {
        this.error = error;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ErrorResponse setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getPath() {
        return path;
    }

    public ErrorResponse setPath(String path) {
        this.path = path;
        return this;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "timestamp=" + timestamp +
                ", requestId='" + requestId + '\'' +
                ", status=" + status +
                ", error=" + error +
                ", message='" + message + '\'' +
                ", path='" + path + '\'' +
                '}';
    }

}
