package edu.wisc.library.sdg.preservation.common.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.wisc.library.sdg.preservation.common.exception.HttpClientErrorResponseException;
import edu.wisc.library.sdg.preservation.common.exception.HttpClientException;
import edu.wisc.library.sdg.preservation.common.util.ArgCheck;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

public class ExceptionResponseErrorHandler implements ResponseErrorHandler {

    private final ObjectMapper objectMapper;

    public ExceptionResponseErrorHandler(ObjectMapper objectMapper) {
        this.objectMapper = ArgCheck.notNull(objectMapper, "objectMapper");
    }

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().isError();
    }
    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        var bodyBytes = response.getBody().readAllBytes();
        try {
            var errorResponse = objectMapper.readValue(bodyBytes, ErrorResponse.class);
            var httpError = new HttpClientErrorResponseException(errorResponse);
            throw errorResponse.getError().newException(errorResponse.getMessage(), httpError);
        } catch (IOException e) {
            throw new HttpClientException(response.getStatusCode().value(), new String(bodyBytes));
        }
    }

}
