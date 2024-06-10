package edu.wisc.library.sdg.preservation.common.spring;

import edu.wisc.library.sdg.preservation.common.client.ErrorResponse;
import edu.wisc.library.sdg.preservation.common.exception.ConcurrentUpdateException;
import edu.wisc.library.sdg.preservation.common.exception.ErrorCode;
import edu.wisc.library.sdg.preservation.common.exception.IllegalOperationException;
import edu.wisc.library.sdg.preservation.common.exception.NotFoundException;
import edu.wisc.library.sdg.preservation.common.exception.SafeRuntimeException;
import edu.wisc.library.sdg.preservation.common.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler
    public ResponseEntity<Object> globalFallback(Exception ex, WebRequest request) {
        LOG.error("Request failure", ex);
        return generalHandler(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_ERROR,
                "Internal server error", ex, request);
    }

    @ExceptionHandler(SafeRuntimeException.class)
    public ResponseEntity<Object> safeFallback(SafeRuntimeException ex, WebRequest request) {
        LOG.warn("Request failure", ex);
        return generalSafeHandler(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_ERROR, ex, request);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> validationException(ValidationException ex, WebRequest request) {
        LOG.warn("Request failure", ex);
        return generalSafeHandler(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_REQUEST, ex, request);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> notFoundException(NotFoundException ex, WebRequest request) {
        LOG.warn("Request failure", ex);
        return generalSafeHandler(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND, ex, request);
    }

    @ExceptionHandler(ConcurrentUpdateException.class)
    public ResponseEntity<Object> concurrentUpdateException(ConcurrentUpdateException ex, WebRequest request) {
        LOG.warn("Request failure", ex);
        return generalSafeHandler(HttpStatus.CONFLICT, ErrorCode.CONCURRENT_UPDATE, ex, request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> accessDeniedException(AccessDeniedException ex, WebRequest request) {
        LOG.warn("Request failure", ex);
        return generalHandler(HttpStatus.FORBIDDEN, ErrorCode.ACCESS_DENIED,
                "The request resource either does not exist or you do not have permission to access it.",
                ex, request);
    }

    @ExceptionHandler(IllegalOperationException.class)
    public ResponseEntity<Object> illegalOperationException(IllegalOperationException ex, WebRequest request) {
        LOG.warn("Request failure", ex);
        return generalSafeHandler(HttpStatus.BAD_REQUEST, ErrorCode.ILLEGAL_OPERATION, ex, request);
    }

    private ResponseEntity<Object> generalSafeHandler(HttpStatus status, ErrorCode error, SafeRuntimeException ex, WebRequest request) {
        return generalHandler(status, error, ex.getMessage(), ex, request);
    }

    private ResponseEntity<Object> generalHandler(HttpStatus status, ErrorCode error, String message, Exception ex, WebRequest request) {
        var errorResponse = createErrorResponse(request, status, error, message);
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return handleExceptionInternal(ex, errorResponse, headers, status, request);
    }

    private ErrorResponse createErrorResponse(WebRequest request, HttpStatus status, ErrorCode error, String message) {
        return new ErrorResponse()
                .setRequestId(requestId(request))
                .setTimestamp(OffsetDateTime.now(ZoneOffset.UTC))
                .setStatus(status.value())
                .setError(error)
                .setMessage(message)
                .setPath(path(request));
    }

    private String requestId(WebRequest request) {
        return (String) request.getAttribute(RequestIdFilter.REQUEST_ID, RequestAttributes.SCOPE_REQUEST);
    }

    private String path(WebRequest request) {
        return ((ServletWebRequest) request).getRequest().getRequestURI();
    }

}
