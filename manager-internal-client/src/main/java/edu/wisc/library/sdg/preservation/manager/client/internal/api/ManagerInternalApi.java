package edu.wisc.library.sdg.preservation.manager.client.internal.api;

import edu.wisc.library.sdg.preservation.manager.client.internal.ApiClient;

import edu.wisc.library.sdg.preservation.manager.client.internal.model.BatchCompleteAnalysisRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.BatchCompleteIngestingRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.BatchCompleteRejectingRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.BatchMarkDeletedRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.BatchStartAnalysisRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.BatchStartIngestingRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.BatchStartRejectingRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.CreateObjectVersionRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.CreateObjectVersionResponse;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.DeferJobRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.DeleteObjectVersionRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.DescribeObjectVersionStatesResponse;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ErrorResponse;
import java.io.File;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.FinalizeObjectVersionRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.GetObjectStorageProblemsResponse;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.JobPollResponse;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ObjectCompleteAnalysisRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ObjectCompleteIngestingRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ObjectStartIngestingRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ObjectVersionReplicatedRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RecordIngestEventRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RecordJobLogsRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RecordPreservationEventRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RegisterIngestObjectFileDetailsRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RegisterIngestObjectFileRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RegisterIngestObjectFileResponse;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RegisterIngestObjectRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RegisterIngestObjectResponse;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RetrieveBatchIngestResponse;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RetrieveBatchResponse;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.RetrieveNewInVersionFilesResponse;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.SetObjectStorageProblemsRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ShouldDeleteBatchRequest;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ShouldDeleteBatchResponse;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.UpdateJobStateRequest;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
@Component("edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi")
public class ManagerInternalApi {
    private ApiClient apiClient;

    public ManagerInternalApi() {
        this(new ApiClient());
    }

    @Autowired
    public ManagerInternalApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Transitions a batch out of an analysis state
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param batchCompleteAnalysisRequest  (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void batchCompleteAnalysis(BatchCompleteAnalysisRequest batchCompleteAnalysisRequest) throws RestClientException {
        batchCompleteAnalysisWithHttpInfo(batchCompleteAnalysisRequest);
    }

    /**
     * Transitions a batch out of an analysis state
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param batchCompleteAnalysisRequest  (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> batchCompleteAnalysisWithHttpInfo(BatchCompleteAnalysisRequest batchCompleteAnalysisRequest) throws RestClientException {
        Object postBody = batchCompleteAnalysisRequest;
        

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] contentTypes = { 
            "application/json"
         };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<Void> returnType = new ParameterizedTypeReference<Void>() {};
        return apiClient.invokeAPI("/batch/completeAnalysis", HttpMethod.POST, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Transitions a batch out of an ingesting state
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param batchCompleteIngestingRequest  (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void batchCompleteIngesting(BatchCompleteIngestingRequest batchCompleteIngestingRequest) throws RestClientException {
        batchCompleteIngestingWithHttpInfo(batchCompleteIngestingRequest);
    }

    /**
     * Transitions a batch out of an ingesting state
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param batchCompleteIngestingRequest  (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> batchCompleteIngestingWithHttpInfo(BatchCompleteIngestingRequest batchCompleteIngestingRequest) throws RestClientException {
        Object postBody = batchCompleteIngestingRequest;
        

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] contentTypes = { 
            "application/json"
         };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<Void> returnType = new ParameterizedTypeReference<Void>() {};
        return apiClient.invokeAPI("/batch/completeIngesting", HttpMethod.POST, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Transitions a batch out of a rejecting state
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param batchCompleteRejectingRequest  (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void batchCompleteRejecting(BatchCompleteRejectingRequest batchCompleteRejectingRequest) throws RestClientException {
        batchCompleteRejectingWithHttpInfo(batchCompleteRejectingRequest);
    }

    /**
     * Transitions a batch out of a rejecting state
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param batchCompleteRejectingRequest  (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> batchCompleteRejectingWithHttpInfo(BatchCompleteRejectingRequest batchCompleteRejectingRequest) throws RestClientException {
        Object postBody = batchCompleteRejectingRequest;
        

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] contentTypes = { 
            "application/json"
         };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<Void> returnType = new ParameterizedTypeReference<Void>() {};
        return apiClient.invokeAPI("/batch/completeRejecting", HttpMethod.POST, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Transitions a batch to a deleted state
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param batchMarkDeletedRequest  (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void batchMarkDeleted(BatchMarkDeletedRequest batchMarkDeletedRequest) throws RestClientException {
        batchMarkDeletedWithHttpInfo(batchMarkDeletedRequest);
    }

    /**
     * Transitions a batch to a deleted state
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param batchMarkDeletedRequest  (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> batchMarkDeletedWithHttpInfo(BatchMarkDeletedRequest batchMarkDeletedRequest) throws RestClientException {
        Object postBody = batchMarkDeletedRequest;
        

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] contentTypes = { 
            "application/json"
         };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<Void> returnType = new ParameterizedTypeReference<Void>() {};
        return apiClient.invokeAPI("/batch/markDeleted", HttpMethod.POST, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Transitions a batch into an analysis state
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param batchStartAnalysisRequest  (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void batchStartAnalysis(BatchStartAnalysisRequest batchStartAnalysisRequest) throws RestClientException {
        batchStartAnalysisWithHttpInfo(batchStartAnalysisRequest);
    }

    /**
     * Transitions a batch into an analysis state
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param batchStartAnalysisRequest  (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> batchStartAnalysisWithHttpInfo(BatchStartAnalysisRequest batchStartAnalysisRequest) throws RestClientException {
        Object postBody = batchStartAnalysisRequest;
        

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] contentTypes = { 
            "application/json"
         };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<Void> returnType = new ParameterizedTypeReference<Void>() {};
        return apiClient.invokeAPI("/batch/startAnalysis", HttpMethod.POST, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Transitions a batch into an ingesting state
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param batchStartIngestingRequest  (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void batchStartIngesting(BatchStartIngestingRequest batchStartIngestingRequest) throws RestClientException {
        batchStartIngestingWithHttpInfo(batchStartIngestingRequest);
    }

    /**
     * Transitions a batch into an ingesting state
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param batchStartIngestingRequest  (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> batchStartIngestingWithHttpInfo(BatchStartIngestingRequest batchStartIngestingRequest) throws RestClientException {
        Object postBody = batchStartIngestingRequest;
        

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] contentTypes = { 
            "application/json"
         };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<Void> returnType = new ParameterizedTypeReference<Void>() {};
        return apiClient.invokeAPI("/batch/startIngesting", HttpMethod.POST, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Transitions a batch into a rejecting state
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param batchStartRejectingRequest  (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void batchStartRejecting(BatchStartRejectingRequest batchStartRejectingRequest) throws RestClientException {
        batchStartRejectingWithHttpInfo(batchStartRejectingRequest);
    }

    /**
     * Transitions a batch into a rejecting state
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param batchStartRejectingRequest  (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> batchStartRejectingWithHttpInfo(BatchStartRejectingRequest batchStartRejectingRequest) throws RestClientException {
        Object postBody = batchStartRejectingRequest;
        

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] contentTypes = { 
            "application/json"
         };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<Void> returnType = new ParameterizedTypeReference<Void>() {};
        return apiClient.invokeAPI("/batch/startRejecting", HttpMethod.POST, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Creates a new version of a preservation object
     * 
     * <p><b>200</b> - Details about the new version
     * <p><b>400</b> - Response returned when an operation fails.
     * @param createObjectVersionRequest  (optional)
     * @return CreateObjectVersionResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public CreateObjectVersionResponse createObjectVersion(CreateObjectVersionRequest createObjectVersionRequest) throws RestClientException {
        return createObjectVersionWithHttpInfo(createObjectVersionRequest).getBody();
    }

    /**
     * Creates a new version of a preservation object
     * 
     * <p><b>200</b> - Details about the new version
     * <p><b>400</b> - Response returned when an operation fails.
     * @param createObjectVersionRequest  (optional)
     * @return ResponseEntity&lt;CreateObjectVersionResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<CreateObjectVersionResponse> createObjectVersionWithHttpInfo(CreateObjectVersionRequest createObjectVersionRequest) throws RestClientException {
        Object postBody = createObjectVersionRequest;
        

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] contentTypes = { 
            "application/json"
         };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<CreateObjectVersionResponse> returnType = new ParameterizedTypeReference<CreateObjectVersionResponse>() {};
        return apiClient.invokeAPI("/object/version", HttpMethod.PUT, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Defers a job if it is not ready to be processed
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param deferJobRequest  (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void defer(DeferJobRequest deferJobRequest) throws RestClientException {
        deferWithHttpInfo(deferJobRequest);
    }

    /**
     * Defers a job if it is not ready to be processed
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param deferJobRequest  (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> deferWithHttpInfo(DeferJobRequest deferJobRequest) throws RestClientException {
        Object postBody = deferJobRequest;
        

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] contentTypes = { 
            "application/json"
         };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<Void> returnType = new ParameterizedTypeReference<Void>() {};
        return apiClient.invokeAPI("/job/defer", HttpMethod.POST, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Deletes a preservation object version
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param deleteObjectVersionRequest  (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void deleteObjectVersion(DeleteObjectVersionRequest deleteObjectVersionRequest) throws RestClientException {
        deleteObjectVersionWithHttpInfo(deleteObjectVersionRequest);
    }

    /**
     * Deletes a preservation object version
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param deleteObjectVersionRequest  (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> deleteObjectVersionWithHttpInfo(DeleteObjectVersionRequest deleteObjectVersionRequest) throws RestClientException {
        Object postBody = deleteObjectVersionRequest;
        

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] contentTypes = { 
            "application/json"
         };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<Void> returnType = new ParameterizedTypeReference<Void>() {};
        return apiClient.invokeAPI("/object/version", HttpMethod.DELETE, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Finalizes the creation of a new object version and sets it to HEAD
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param finalizeObjectVersionRequest  (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void finalizeObjectVersion(FinalizeObjectVersionRequest finalizeObjectVersionRequest) throws RestClientException {
        finalizeObjectVersionWithHttpInfo(finalizeObjectVersionRequest);
    }

    /**
     * Finalizes the creation of a new object version and sets it to HEAD
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param finalizeObjectVersionRequest  (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> finalizeObjectVersionWithHttpInfo(FinalizeObjectVersionRequest finalizeObjectVersionRequest) throws RestClientException {
        Object postBody = finalizeObjectVersionRequest;
        

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] contentTypes = { 
            "application/json"
         };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<Void> returnType = new ParameterizedTypeReference<Void>() {};
        return apiClient.invokeAPI("/object/version/finalize", HttpMethod.POST, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Retrieves details about what storage problems are affecting an object
     * 
     * <p><b>200</b> - The object&#39;s storage problems, if any
     * <p><b>400</b> - Response returned when an operation fails.
     * @param internalObjectId The internal ID of the object (required)
     * @return GetObjectStorageProblemsResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public GetObjectStorageProblemsResponse getObjectStorageProblems(String internalObjectId) throws RestClientException {
        return getObjectStorageProblemsWithHttpInfo(internalObjectId).getBody();
    }

    /**
     * Retrieves details about what storage problems are affecting an object
     * 
     * <p><b>200</b> - The object&#39;s storage problems, if any
     * <p><b>400</b> - Response returned when an operation fails.
     * @param internalObjectId The internal ID of the object (required)
     * @return ResponseEntity&lt;GetObjectStorageProblemsResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<GetObjectStorageProblemsResponse> getObjectStorageProblemsWithHttpInfo(String internalObjectId) throws RestClientException {
        Object postBody = null;
        
        // verify the required parameter 'internalObjectId' is set
        if (internalObjectId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'internalObjectId' when calling getObjectStorageProblems");
        }
        

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "internalObjectId", internalObjectId));

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] contentTypes = {  };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<GetObjectStorageProblemsResponse> returnType = new ParameterizedTypeReference<GetObjectStorageProblemsResponse>() {};
        return apiClient.invokeAPI("/object/problems/storage", HttpMethod.GET, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Describes the version state of every version of an object
     * 
     * <p><b>200</b> - Details about the state of every version of an object
     * <p><b>400</b> - Response returned when an operation fails.
     * @param internalObjectId The internal ID of the object (required)
     * @return DescribeObjectVersionStatesResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public DescribeObjectVersionStatesResponse getObjectVersionStates(String internalObjectId) throws RestClientException {
        return getObjectVersionStatesWithHttpInfo(internalObjectId).getBody();
    }

    /**
     * Describes the version state of every version of an object
     * 
     * <p><b>200</b> - Details about the state of every version of an object
     * <p><b>400</b> - Response returned when an operation fails.
     * @param internalObjectId The internal ID of the object (required)
     * @return ResponseEntity&lt;DescribeObjectVersionStatesResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<DescribeObjectVersionStatesResponse> getObjectVersionStatesWithHttpInfo(String internalObjectId) throws RestClientException {
        Object postBody = null;
        
        // verify the required parameter 'internalObjectId' is set
        if (internalObjectId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'internalObjectId' when calling getObjectVersionStates");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("internalObjectId", internalObjectId);

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] contentTypes = {  };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<DescribeObjectVersionStatesResponse> returnType = new ParameterizedTypeReference<DescribeObjectVersionStatesResponse>() {};
        return apiClient.invokeAPI("/object/{internalObjectId}/versionStates", HttpMethod.GET, uriVariables, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Generates a PREMIS document for the object
     * 
     * <p><b>200</b> - Success
     * @param internalObjectId The internal ID of the object (required)
     * @param versionNumbers List of versions to include in PREMIS (required)
     * @return File
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public File getPremisDocument(String internalObjectId, List<Integer> versionNumbers) throws RestClientException {
        return getPremisDocumentWithHttpInfo(internalObjectId, versionNumbers).getBody();
    }

    /**
     * Generates a PREMIS document for the object
     * 
     * <p><b>200</b> - Success
     * @param internalObjectId The internal ID of the object (required)
     * @param versionNumbers List of versions to include in PREMIS (required)
     * @return ResponseEntity&lt;File&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<File> getPremisDocumentWithHttpInfo(String internalObjectId, List<Integer> versionNumbers) throws RestClientException {
        Object postBody = null;
        
        // verify the required parameter 'internalObjectId' is set
        if (internalObjectId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'internalObjectId' when calling getPremisDocument");
        }
        
        // verify the required parameter 'versionNumbers' is set
        if (versionNumbers == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'versionNumbers' when calling getPremisDocument");
        }
        

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "internalObjectId", internalObjectId));
        queryParams.putAll(apiClient.parameterToMultiValueMap(ApiClient.CollectionFormat.valueOf("multi".toUpperCase(Locale.ROOT)), "versionNumbers", versionNumbers));

        final String[] localVarAccepts = { 
            "application/xml"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] contentTypes = {  };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<File> returnType = new ParameterizedTypeReference<File>() {};
        return apiClient.invokeAPI("/object/premis", HttpMethod.GET, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Transitions a batch out of an analysis state
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param objectCompleteAnalysisRequest  (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void objectCompleteAnalysis(ObjectCompleteAnalysisRequest objectCompleteAnalysisRequest) throws RestClientException {
        objectCompleteAnalysisWithHttpInfo(objectCompleteAnalysisRequest);
    }

    /**
     * Transitions a batch out of an analysis state
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param objectCompleteAnalysisRequest  (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> objectCompleteAnalysisWithHttpInfo(ObjectCompleteAnalysisRequest objectCompleteAnalysisRequest) throws RestClientException {
        Object postBody = objectCompleteAnalysisRequest;
        

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] contentTypes = { 
            "application/json"
         };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<Void> returnType = new ParameterizedTypeReference<Void>() {};
        return apiClient.invokeAPI("/batch/object/completeAnalysis", HttpMethod.POST, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Transitions a batch out of an ingesting state
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param objectCompleteIngestingRequest  (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void objectCompleteIngesting(ObjectCompleteIngestingRequest objectCompleteIngestingRequest) throws RestClientException {
        objectCompleteIngestingWithHttpInfo(objectCompleteIngestingRequest);
    }

    /**
     * Transitions a batch out of an ingesting state
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param objectCompleteIngestingRequest  (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> objectCompleteIngestingWithHttpInfo(ObjectCompleteIngestingRequest objectCompleteIngestingRequest) throws RestClientException {
        Object postBody = objectCompleteIngestingRequest;
        

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] contentTypes = { 
            "application/json"
         };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<Void> returnType = new ParameterizedTypeReference<Void>() {};
        return apiClient.invokeAPI("/batch/object/completeIngesting", HttpMethod.POST, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Transitions a batch into an ingesting state
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param objectStartIngestingRequest  (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void objectStartIngesting(ObjectStartIngestingRequest objectStartIngestingRequest) throws RestClientException {
        objectStartIngestingWithHttpInfo(objectStartIngestingRequest);
    }

    /**
     * Transitions a batch into an ingesting state
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param objectStartIngestingRequest  (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> objectStartIngestingWithHttpInfo(ObjectStartIngestingRequest objectStartIngestingRequest) throws RestClientException {
        Object postBody = objectStartIngestingRequest;
        

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] contentTypes = { 
            "application/json"
         };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<Void> returnType = new ParameterizedTypeReference<Void>() {};
        return apiClient.invokeAPI("/batch/object/startIngesting", HttpMethod.POST, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Marks an object version as being replicated to a data store
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param objectVersionReplicatedRequest  (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void objectVersionReplicated(ObjectVersionReplicatedRequest objectVersionReplicatedRequest) throws RestClientException {
        objectVersionReplicatedWithHttpInfo(objectVersionReplicatedRequest);
    }

    /**
     * Marks an object version as being replicated to a data store
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param objectVersionReplicatedRequest  (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> objectVersionReplicatedWithHttpInfo(ObjectVersionReplicatedRequest objectVersionReplicatedRequest) throws RestClientException {
        Object postBody = objectVersionReplicatedRequest;
        

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] contentTypes = { 
            "application/json"
         };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<Void> returnType = new ParameterizedTypeReference<Void>() {};
        return apiClient.invokeAPI("/object/version/replicated", HttpMethod.POST, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Long poll for a job to process
     * 
     * <p><b>200</b> - A job to process
     * <p><b>400</b> - Response returned when an operation fails.
     * @return JobPollResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public JobPollResponse pollForJob() throws RestClientException {
        return pollForJobWithHttpInfo().getBody();
    }

    /**
     * Long poll for a job to process
     * 
     * <p><b>200</b> - A job to process
     * <p><b>400</b> - Response returned when an operation fails.
     * @return ResponseEntity&lt;JobPollResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<JobPollResponse> pollForJobWithHttpInfo() throws RestClientException {
        Object postBody = null;
        

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] contentTypes = {  };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<JobPollResponse> returnType = new ParameterizedTypeReference<JobPollResponse>() {};
        return apiClient.invokeAPI("/job/poll", HttpMethod.GET, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Record ingest event
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param recordIngestEventRequest  (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void recordIngestEvent(RecordIngestEventRequest recordIngestEventRequest) throws RestClientException {
        recordIngestEventWithHttpInfo(recordIngestEventRequest);
    }

    /**
     * Record ingest event
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param recordIngestEventRequest  (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> recordIngestEventWithHttpInfo(RecordIngestEventRequest recordIngestEventRequest) throws RestClientException {
        Object postBody = recordIngestEventRequest;
        

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] contentTypes = { 
            "application/json"
         };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<Void> returnType = new ParameterizedTypeReference<Void>() {};
        return apiClient.invokeAPI("/batch/event", HttpMethod.POST, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Adds logs to a job
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param recordJobLogsRequest  (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void recordJobLogs(RecordJobLogsRequest recordJobLogsRequest) throws RestClientException {
        recordJobLogsWithHttpInfo(recordJobLogsRequest);
    }

    /**
     * Adds logs to a job
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param recordJobLogsRequest  (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> recordJobLogsWithHttpInfo(RecordJobLogsRequest recordJobLogsRequest) throws RestClientException {
        Object postBody = recordJobLogsRequest;
        

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] contentTypes = { 
            "application/json"
         };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<Void> returnType = new ParameterizedTypeReference<Void>() {};
        return apiClient.invokeAPI("/job/logs", HttpMethod.POST, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Record preservation event
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param recordPreservationEventRequest  (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void recordPreservationEvent(RecordPreservationEventRequest recordPreservationEventRequest) throws RestClientException {
        recordPreservationEventWithHttpInfo(recordPreservationEventRequest);
    }

    /**
     * Record preservation event
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param recordPreservationEventRequest  (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> recordPreservationEventWithHttpInfo(RecordPreservationEventRequest recordPreservationEventRequest) throws RestClientException {
        Object postBody = recordPreservationEventRequest;
        

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] contentTypes = { 
            "application/json"
         };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<Void> returnType = new ParameterizedTypeReference<Void>() {};
        return apiClient.invokeAPI("/object/event", HttpMethod.POST, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Registers an object to a batch
     * 
     * <p><b>200</b> - Register ingest object response
     * <p><b>400</b> - Response returned when an operation fails.
     * @param registerIngestObjectRequest  (optional)
     * @return RegisterIngestObjectResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public RegisterIngestObjectResponse registerIngestObject(RegisterIngestObjectRequest registerIngestObjectRequest) throws RestClientException {
        return registerIngestObjectWithHttpInfo(registerIngestObjectRequest).getBody();
    }

    /**
     * Registers an object to a batch
     * 
     * <p><b>200</b> - Register ingest object response
     * <p><b>400</b> - Response returned when an operation fails.
     * @param registerIngestObjectRequest  (optional)
     * @return ResponseEntity&lt;RegisterIngestObjectResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<RegisterIngestObjectResponse> registerIngestObjectWithHttpInfo(RegisterIngestObjectRequest registerIngestObjectRequest) throws RestClientException {
        Object postBody = registerIngestObjectRequest;
        

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] contentTypes = { 
            "application/json"
         };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<RegisterIngestObjectResponse> returnType = new ParameterizedTypeReference<RegisterIngestObjectResponse>() {};
        return apiClient.invokeAPI("/batch/object", HttpMethod.POST, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Registers a file to an object in a batch
     * 
     * <p><b>200</b> - Register ingest object file response
     * <p><b>400</b> - Response returned when an operation fails.
     * @param registerIngestObjectFileRequest  (optional)
     * @return RegisterIngestObjectFileResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public RegisterIngestObjectFileResponse registerIngestObjectFile(RegisterIngestObjectFileRequest registerIngestObjectFileRequest) throws RestClientException {
        return registerIngestObjectFileWithHttpInfo(registerIngestObjectFileRequest).getBody();
    }

    /**
     * Registers a file to an object in a batch
     * 
     * <p><b>200</b> - Register ingest object file response
     * <p><b>400</b> - Response returned when an operation fails.
     * @param registerIngestObjectFileRequest  (optional)
     * @return ResponseEntity&lt;RegisterIngestObjectFileResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<RegisterIngestObjectFileResponse> registerIngestObjectFileWithHttpInfo(RegisterIngestObjectFileRequest registerIngestObjectFileRequest) throws RestClientException {
        Object postBody = registerIngestObjectFileRequest;
        

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] contentTypes = { 
            "application/json"
         };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<RegisterIngestObjectFileResponse> returnType = new ParameterizedTypeReference<RegisterIngestObjectFileResponse>() {};
        return apiClient.invokeAPI("/batch/object/file", HttpMethod.POST, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Adds details to an ingest object file
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param registerIngestObjectFileDetailsRequest  (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void registerIngestObjectFileDetails(RegisterIngestObjectFileDetailsRequest registerIngestObjectFileDetailsRequest) throws RestClientException {
        registerIngestObjectFileDetailsWithHttpInfo(registerIngestObjectFileDetailsRequest);
    }

    /**
     * Adds details to an ingest object file
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param registerIngestObjectFileDetailsRequest  (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> registerIngestObjectFileDetailsWithHttpInfo(RegisterIngestObjectFileDetailsRequest registerIngestObjectFileDetailsRequest) throws RestClientException {
        Object postBody = registerIngestObjectFileDetailsRequest;
        

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] contentTypes = { 
            "application/json"
         };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<Void> returnType = new ParameterizedTypeReference<Void>() {};
        return apiClient.invokeAPI("/batch/object/file/details", HttpMethod.POST, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Retrieves Ingest Batch details
     * 
     * <p><b>200</b> - Ingest Batch details
     * <p><b>400</b> - Response returned when an operation fails.
     * @param ingestId The ID for the Ingest Batch (required)
     * @return RetrieveBatchResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public RetrieveBatchResponse retrieveBatch(Long ingestId) throws RestClientException {
        return retrieveBatchWithHttpInfo(ingestId).getBody();
    }

    /**
     * Retrieves Ingest Batch details
     * 
     * <p><b>200</b> - Ingest Batch details
     * <p><b>400</b> - Response returned when an operation fails.
     * @param ingestId The ID for the Ingest Batch (required)
     * @return ResponseEntity&lt;RetrieveBatchResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<RetrieveBatchResponse> retrieveBatchWithHttpInfo(Long ingestId) throws RestClientException {
        Object postBody = null;
        
        // verify the required parameter 'ingestId' is set
        if (ingestId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'ingestId' when calling retrieveBatch");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("ingestId", ingestId);

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] contentTypes = {  };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<RetrieveBatchResponse> returnType = new ParameterizedTypeReference<RetrieveBatchResponse>() {};
        return apiClient.invokeAPI("/batch/{ingestId}", HttpMethod.GET, uriVariables, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Retrieves all of the details about batch objects that are ready for ingest
     * 
     * <p><b>200</b> - Ingest Batch details
     * <p><b>400</b> - Response returned when an operation fails.
     * @param ingestId The ID for the Ingest Batch (required)
     * @return RetrieveBatchIngestResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public RetrieveBatchIngestResponse retrieveBatchIngest(Long ingestId) throws RestClientException {
        return retrieveBatchIngestWithHttpInfo(ingestId).getBody();
    }

    /**
     * Retrieves all of the details about batch objects that are ready for ingest
     * 
     * <p><b>200</b> - Ingest Batch details
     * <p><b>400</b> - Response returned when an operation fails.
     * @param ingestId The ID for the Ingest Batch (required)
     * @return ResponseEntity&lt;RetrieveBatchIngestResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<RetrieveBatchIngestResponse> retrieveBatchIngestWithHttpInfo(Long ingestId) throws RestClientException {
        Object postBody = null;
        
        // verify the required parameter 'ingestId' is set
        if (ingestId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'ingestId' when calling retrieveBatchIngest");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("ingestId", ingestId);

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] contentTypes = {  };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<RetrieveBatchIngestResponse> returnType = new ParameterizedTypeReference<RetrieveBatchIngestResponse>() {};
        return apiClient.invokeAPI("/batch/{ingestId}/ingest", HttpMethod.GET, uriVariables, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Retrieves a list of all of the files that were introduced to the object in the specified persistence version
     * 
     * <p><b>200</b> - Files new to the object in the specified version
     * <p><b>400</b> - Response returned when an operation fails.
     * @param internalObjectId The internal ID of the object (required)
     * @param persistenceVersion The version identifier of the object version (required)
     * @return RetrieveNewInVersionFilesResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public RetrieveNewInVersionFilesResponse retrieveNewInVersionFiles(String internalObjectId, String persistenceVersion) throws RestClientException {
        return retrieveNewInVersionFilesWithHttpInfo(internalObjectId, persistenceVersion).getBody();
    }

    /**
     * Retrieves a list of all of the files that were introduced to the object in the specified persistence version
     * 
     * <p><b>200</b> - Files new to the object in the specified version
     * <p><b>400</b> - Response returned when an operation fails.
     * @param internalObjectId The internal ID of the object (required)
     * @param persistenceVersion The version identifier of the object version (required)
     * @return ResponseEntity&lt;RetrieveNewInVersionFilesResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<RetrieveNewInVersionFilesResponse> retrieveNewInVersionFilesWithHttpInfo(String internalObjectId, String persistenceVersion) throws RestClientException {
        Object postBody = null;
        
        // verify the required parameter 'internalObjectId' is set
        if (internalObjectId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'internalObjectId' when calling retrieveNewInVersionFiles");
        }
        
        // verify the required parameter 'persistenceVersion' is set
        if (persistenceVersion == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'persistenceVersion' when calling retrieveNewInVersionFiles");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("internalObjectId", internalObjectId);
        uriVariables.put("persistenceVersion", persistenceVersion);

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] contentTypes = {  };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<RetrieveNewInVersionFilesResponse> returnType = new ParameterizedTypeReference<RetrieveNewInVersionFilesResponse>() {};
        return apiClient.invokeAPI("/object/{internalObjectId}/persistenceVersion/{persistenceVersion}/filesNewInVersion", HttpMethod.GET, uriVariables, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Sets an object&#39;s storage problems
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param setObjectStorageProblemsRequest  (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void setObjectStorageProblems(SetObjectStorageProblemsRequest setObjectStorageProblemsRequest) throws RestClientException {
        setObjectStorageProblemsWithHttpInfo(setObjectStorageProblemsRequest);
    }

    /**
     * Sets an object&#39;s storage problems
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param setObjectStorageProblemsRequest  (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> setObjectStorageProblemsWithHttpInfo(SetObjectStorageProblemsRequest setObjectStorageProblemsRequest) throws RestClientException {
        Object postBody = setObjectStorageProblemsRequest;
        

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] contentTypes = { 
            "application/json"
         };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<Void> returnType = new ParameterizedTypeReference<Void>() {};
        return apiClient.invokeAPI("/object/problems/storage", HttpMethod.PUT, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Check if the batch is old and should be deleted
     * 
     * <p><b>200</b> - Indicates if the batch should be deleted
     * <p><b>400</b> - Response returned when an operation fails.
     * @param shouldDeleteBatchRequest  (optional)
     * @return ShouldDeleteBatchResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ShouldDeleteBatchResponse shouldDeleteBatch(ShouldDeleteBatchRequest shouldDeleteBatchRequest) throws RestClientException {
        return shouldDeleteBatchWithHttpInfo(shouldDeleteBatchRequest).getBody();
    }

    /**
     * Check if the batch is old and should be deleted
     * 
     * <p><b>200</b> - Indicates if the batch should be deleted
     * <p><b>400</b> - Response returned when an operation fails.
     * @param shouldDeleteBatchRequest  (optional)
     * @return ResponseEntity&lt;ShouldDeleteBatchResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<ShouldDeleteBatchResponse> shouldDeleteBatchWithHttpInfo(ShouldDeleteBatchRequest shouldDeleteBatchRequest) throws RestClientException {
        Object postBody = shouldDeleteBatchRequest;
        

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] contentTypes = { 
            "application/json"
         };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<ShouldDeleteBatchResponse> returnType = new ParameterizedTypeReference<ShouldDeleteBatchResponse>() {};
        return apiClient.invokeAPI("/batch/shouldDelete", HttpMethod.POST, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Updates the state of a job
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param updateJobStateRequest  (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void updateJobState(UpdateJobStateRequest updateJobStateRequest) throws RestClientException {
        updateJobStateWithHttpInfo(updateJobStateRequest);
    }

    /**
     * Updates the state of a job
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param updateJobStateRequest  (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> updateJobStateWithHttpInfo(UpdateJobStateRequest updateJobStateRequest) throws RestClientException {
        Object postBody = updateJobStateRequest;
        

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] contentTypes = { 
            "application/json"
         };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<Void> returnType = new ParameterizedTypeReference<Void>() {};
        return apiClient.invokeAPI("/job/state", HttpMethod.POST, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
}
