package edu.wisc.library.sdg.preservation.manager.client.api;

import edu.wisc.library.sdg.preservation.manager.client.ApiClient;

import edu.wisc.library.sdg.preservation.manager.client.model.ApproveIngestBatchRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.ApproveIngestObjectRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.BatchRetryIngestRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.BatchRetryReplicateRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.BatchSearchResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.CancelJobRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.CreateOrgRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.CreateVaultRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.DeleteObjectRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.DescribeJobResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.DescribeOrgResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.DescribePreservationObjectResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.DescribeRetrieveObjectsResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.DescribeUserResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.DescribeVaultResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.DiffBatchObjectResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.DisableJobTypesRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.EnableJobTypesRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.ErrorResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.EventOutcome;
import edu.wisc.library.sdg.preservation.manager.client.model.EventType;
import java.io.File;
import edu.wisc.library.sdg.preservation.manager.client.model.IngestBagResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.ListJobsResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.ListOrgUsersResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.ListUserOrgsResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.ListVaultPermissionsResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.ListVaultProblemsResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.ListVaultUsersResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.ListVaultsResponse;
import java.time.LocalDate;
import edu.wisc.library.sdg.preservation.manager.client.model.OrganizationContactResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.RejectIngestBatchRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.RejectIngestObjectRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.ReplicateObjectRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.ReplicateObjectResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.RestorePreservationObjectRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.RestorePreservationObjectResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.RetrieveBatchObjectFileResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.RetrieveBatchObjectFilesResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.RetrieveBatchObjectResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.RetrieveBatchObjectsResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.RetrieveBatchResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.RetrieveEventsResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.RetrieveLogsResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.RetrieveObjectStorageProblemsResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.RetrieveObjectsRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.RetrieveObjectsResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.SystemEventCountsResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.SystemStorageResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.UpdateUserVaultPermissionsRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.ValidateObjectRemoteRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.ValidateObjectRemoteResponse;
import edu.wisc.library.sdg.preservation.manager.client.model.ValidatePreservationObjectRequest;
import edu.wisc.library.sdg.preservation.manager.client.model.ValidatePreservationObjectResponse;

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
@Component("edu.wisc.library.sdg.preservation.manager.client.api.PreservationManagerApi")
public class PreservationManagerApi {
    private ApiClient apiClient;

    public PreservationManagerApi() {
        this(new ApiClient());
    }

    @Autowired
    public PreservationManagerApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Approves a batch for ingestion
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param approveIngestBatchRequest  (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void approveIngestBatch(ApproveIngestBatchRequest approveIngestBatchRequest) throws RestClientException {
        approveIngestBatchWithHttpInfo(approveIngestBatchRequest);
    }

    /**
     * Approves a batch for ingestion
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param approveIngestBatchRequest  (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> approveIngestBatchWithHttpInfo(ApproveIngestBatchRequest approveIngestBatchRequest) throws RestClientException {
        Object postBody = approveIngestBatchRequest;
        

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

        String[] authNames = new String[] { "ApiKeyAuth" };

        ParameterizedTypeReference<Void> returnType = new ParameterizedTypeReference<Void>() {};
        return apiClient.invokeAPI("/batch/approve", HttpMethod.POST, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Approves an object for ingestion
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param approveIngestObjectRequest  (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void approveIngestObject(ApproveIngestObjectRequest approveIngestObjectRequest) throws RestClientException {
        approveIngestObjectWithHttpInfo(approveIngestObjectRequest);
    }

    /**
     * Approves an object for ingestion
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param approveIngestObjectRequest  (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> approveIngestObjectWithHttpInfo(ApproveIngestObjectRequest approveIngestObjectRequest) throws RestClientException {
        Object postBody = approveIngestObjectRequest;
        

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

        String[] authNames = new String[] { "ApiKeyAuth" };

        ParameterizedTypeReference<Void> returnType = new ParameterizedTypeReference<Void>() {};
        return apiClient.invokeAPI("/batch/object/approve", HttpMethod.POST, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Cancels a pending job
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param cancelJobRequest  (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void cancelJob(CancelJobRequest cancelJobRequest) throws RestClientException {
        cancelJobWithHttpInfo(cancelJobRequest);
    }

    /**
     * Cancels a pending job
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param cancelJobRequest  (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> cancelJobWithHttpInfo(CancelJobRequest cancelJobRequest) throws RestClientException {
        Object postBody = cancelJobRequest;
        

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

        String[] authNames = new String[] { "ApiKeyAuth" };

        ParameterizedTypeReference<Void> returnType = new ParameterizedTypeReference<Void>() {};
        return apiClient.invokeAPI("/job/cancel", HttpMethod.POST, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Creates a new organization
     * Service adminstrator access is required to create an organization.
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param orgName The ID of the organization (required)
     * @param createOrgRequest  (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void createOrg(String orgName, CreateOrgRequest createOrgRequest) throws RestClientException {
        createOrgWithHttpInfo(orgName, createOrgRequest);
    }

    /**
     * Creates a new organization
     * Service adminstrator access is required to create an organization.
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param orgName The ID of the organization (required)
     * @param createOrgRequest  (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> createOrgWithHttpInfo(String orgName, CreateOrgRequest createOrgRequest) throws RestClientException {
        Object postBody = createOrgRequest;
        
        // verify the required parameter 'orgName' is set
        if (orgName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'orgName' when calling createOrg");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("orgName", orgName);

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

        String[] authNames = new String[] { "ApiKeyAuth" };

        ParameterizedTypeReference<Void> returnType = new ParameterizedTypeReference<Void>() {};
        return apiClient.invokeAPI("/org/{orgName}", HttpMethod.POST, uriVariables, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Creates a new vault for an organization
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param createVaultRequest  (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void createVault(CreateVaultRequest createVaultRequest) throws RestClientException {
        createVaultWithHttpInfo(createVaultRequest);
    }

    /**
     * Creates a new vault for an organization
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param createVaultRequest  (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> createVaultWithHttpInfo(CreateVaultRequest createVaultRequest) throws RestClientException {
        Object postBody = createVaultRequest;
        

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

        String[] authNames = new String[] { "ApiKeyAuth" };

        ParameterizedTypeReference<Void> returnType = new ParameterizedTypeReference<Void>() {};
        return apiClient.invokeAPI("/vault", HttpMethod.POST, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Marks the object as deleted in the database, but does not actually delete any files
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param deleteObjectRequest  (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void deleteObject(DeleteObjectRequest deleteObjectRequest) throws RestClientException {
        deleteObjectWithHttpInfo(deleteObjectRequest);
    }

    /**
     * Marks the object as deleted in the database, but does not actually delete any files
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param deleteObjectRequest  (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> deleteObjectWithHttpInfo(DeleteObjectRequest deleteObjectRequest) throws RestClientException {
        Object postBody = deleteObjectRequest;
        

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

        String[] authNames = new String[] { "ApiKeyAuth" };

        ParameterizedTypeReference<Void> returnType = new ParameterizedTypeReference<Void>() {};
        return apiClient.invokeAPI("/object/delete", HttpMethod.POST, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Gets job details
     * 
     * <p><b>200</b> - Job details
     * <p><b>400</b> - Response returned when an operation fails.
     * @param jobId The ID of the job (required)
     * @return DescribeJobResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public DescribeJobResponse describeJob(Long jobId) throws RestClientException {
        return describeJobWithHttpInfo(jobId).getBody();
    }

    /**
     * Gets job details
     * 
     * <p><b>200</b> - Job details
     * <p><b>400</b> - Response returned when an operation fails.
     * @param jobId The ID of the job (required)
     * @return ResponseEntity&lt;DescribeJobResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<DescribeJobResponse> describeJobWithHttpInfo(Long jobId) throws RestClientException {
        Object postBody = null;
        
        // verify the required parameter 'jobId' is set
        if (jobId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'jobId' when calling describeJob");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("jobId", jobId);

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

        String[] authNames = new String[] { "ApiKeyAuth" };

        ParameterizedTypeReference<DescribeJobResponse> returnType = new ParameterizedTypeReference<DescribeJobResponse>() {};
        return apiClient.invokeAPI("/job/{jobId}", HttpMethod.GET, uriVariables, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Describes the specified organization and returns the current user&#39;s permissions
     * 
     * <p><b>200</b> - Details about the specified organization and returns the current user&#39;s permissions
     * <p><b>400</b> - Response returned when an operation fails.
     * @param orgName The ID of the organization (required)
     * @return DescribeOrgResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public DescribeOrgResponse describeOrg(String orgName) throws RestClientException {
        return describeOrgWithHttpInfo(orgName).getBody();
    }

    /**
     * Describes the specified organization and returns the current user&#39;s permissions
     * 
     * <p><b>200</b> - Details about the specified organization and returns the current user&#39;s permissions
     * <p><b>400</b> - Response returned when an operation fails.
     * @param orgName The ID of the organization (required)
     * @return ResponseEntity&lt;DescribeOrgResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<DescribeOrgResponse> describeOrgWithHttpInfo(String orgName) throws RestClientException {
        Object postBody = null;
        
        // verify the required parameter 'orgName' is set
        if (orgName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'orgName' when calling describeOrg");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("orgName", orgName);

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

        String[] authNames = new String[] { "ApiKeyAuth" };

        ParameterizedTypeReference<DescribeOrgResponse> returnType = new ParameterizedTypeReference<DescribeOrgResponse>() {};
        return apiClient.invokeAPI("/org/{orgName}", HttpMethod.GET, uriVariables, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Returns the primary contact for the organization.
     * 
     * <p><b>200</b> - Details about the primary contact for the organization
     * <p><b>400</b> - Response returned when an operation fails.
     * @param orgName The ID of the organization (required)
     * @return OrganizationContactResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public OrganizationContactResponse describeOrgContact(String orgName) throws RestClientException {
        return describeOrgContactWithHttpInfo(orgName).getBody();
    }

    /**
     * Returns the primary contact for the organization.
     * 
     * <p><b>200</b> - Details about the primary contact for the organization
     * <p><b>400</b> - Response returned when an operation fails.
     * @param orgName The ID of the organization (required)
     * @return ResponseEntity&lt;OrganizationContactResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<OrganizationContactResponse> describeOrgContactWithHttpInfo(String orgName) throws RestClientException {
        Object postBody = null;
        
        // verify the required parameter 'orgName' is set
        if (orgName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'orgName' when calling describeOrgContact");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("orgName", orgName);

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

        String[] authNames = new String[] { "ApiKeyAuth" };

        ParameterizedTypeReference<OrganizationContactResponse> returnType = new ParameterizedTypeReference<OrganizationContactResponse>() {};
        return apiClient.invokeAPI("/org/{orgName}/contact", HttpMethod.GET, uriVariables, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Retrieves details about an object version
     * 
     * <p><b>200</b> - Details about a preservation object
     * <p><b>400</b> - Response returned when an operation fails.
     * @param vault The vault an object is in (required)
     * @param externalObjectId The external ID of the object (required)
     * @param version The version of an object (optional)
     * @return DescribePreservationObjectResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public DescribePreservationObjectResponse describePreservationObject(String vault, String externalObjectId, Integer version) throws RestClientException {
        return describePreservationObjectWithHttpInfo(vault, externalObjectId, version).getBody();
    }

    /**
     * Retrieves details about an object version
     * 
     * <p><b>200</b> - Details about a preservation object
     * <p><b>400</b> - Response returned when an operation fails.
     * @param vault The vault an object is in (required)
     * @param externalObjectId The external ID of the object (required)
     * @param version The version of an object (optional)
     * @return ResponseEntity&lt;DescribePreservationObjectResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<DescribePreservationObjectResponse> describePreservationObjectWithHttpInfo(String vault, String externalObjectId, Integer version) throws RestClientException {
        Object postBody = null;
        
        // verify the required parameter 'vault' is set
        if (vault == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'vault' when calling describePreservationObject");
        }
        
        // verify the required parameter 'externalObjectId' is set
        if (externalObjectId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'externalObjectId' when calling describePreservationObject");
        }
        

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "vault", vault));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "externalObjectId", externalObjectId));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "version", version));

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] contentTypes = {  };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "ApiKeyAuth" };

        ParameterizedTypeReference<DescribePreservationObjectResponse> returnType = new ParameterizedTypeReference<DescribePreservationObjectResponse>() {};
        return apiClient.invokeAPI("/object", HttpMethod.GET, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Describes a retrieve objects request
     * 
     * <p><b>200</b> - Details about a retrieve objects request
     * <p><b>400</b> - Response returned when an operation fails.
     * @param requestId The ID of the request (required)
     * @return DescribeRetrieveObjectsResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public DescribeRetrieveObjectsResponse describeRetrieveRequest(Long requestId) throws RestClientException {
        return describeRetrieveRequestWithHttpInfo(requestId).getBody();
    }

    /**
     * Describes a retrieve objects request
     * 
     * <p><b>200</b> - Details about a retrieve objects request
     * <p><b>400</b> - Response returned when an operation fails.
     * @param requestId The ID of the request (required)
     * @return ResponseEntity&lt;DescribeRetrieveObjectsResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<DescribeRetrieveObjectsResponse> describeRetrieveRequestWithHttpInfo(Long requestId) throws RestClientException {
        Object postBody = null;
        
        // verify the required parameter 'requestId' is set
        if (requestId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'requestId' when calling describeRetrieveRequest");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("requestId", requestId);

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

        String[] authNames = new String[] { "ApiKeyAuth" };

        ParameterizedTypeReference<DescribeRetrieveObjectsResponse> returnType = new ParameterizedTypeReference<DescribeRetrieveObjectsResponse>() {};
        return apiClient.invokeAPI("/vault/retrieve/{requestId}", HttpMethod.GET, uriVariables, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Retrieves details about the current user
     * 
     * <p><b>200</b> - User details
     * <p><b>400</b> - Response returned when an operation fails.
     * @return DescribeUserResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public DescribeUserResponse describeUser() throws RestClientException {
        return describeUserWithHttpInfo().getBody();
    }

    /**
     * Retrieves details about the current user
     * 
     * <p><b>200</b> - User details
     * <p><b>400</b> - Response returned when an operation fails.
     * @return ResponseEntity&lt;DescribeUserResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<DescribeUserResponse> describeUserWithHttpInfo() throws RestClientException {
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

        String[] authNames = new String[] { "ApiKeyAuth" };

        ParameterizedTypeReference<DescribeUserResponse> returnType = new ParameterizedTypeReference<DescribeUserResponse>() {};
        return apiClient.invokeAPI("/user", HttpMethod.GET, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Returns details about the vault and the current user&#39;s permissions in the vault
     * 
     * <p><b>200</b> - Details about the vault and the current user&#39;s permissions in the vault
     * <p><b>400</b> - Response returned when an operation fails.
     * @param vault The name of the vault (required)
     * @return DescribeVaultResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public DescribeVaultResponse describeVault(String vault) throws RestClientException {
        return describeVaultWithHttpInfo(vault).getBody();
    }

    /**
     * Returns details about the vault and the current user&#39;s permissions in the vault
     * 
     * <p><b>200</b> - Details about the vault and the current user&#39;s permissions in the vault
     * <p><b>400</b> - Response returned when an operation fails.
     * @param vault The name of the vault (required)
     * @return ResponseEntity&lt;DescribeVaultResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<DescribeVaultResponse> describeVaultWithHttpInfo(String vault) throws RestClientException {
        Object postBody = null;
        
        // verify the required parameter 'vault' is set
        if (vault == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'vault' when calling describeVault");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("vault", vault);

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

        String[] authNames = new String[] { "ApiKeyAuth" };

        ParameterizedTypeReference<DescribeVaultResponse> returnType = new ParameterizedTypeReference<DescribeVaultResponse>() {};
        return apiClient.invokeAPI("/vault/{vault}/describe", HttpMethod.GET, uriVariables, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Diffs the batch object against the current state of the object in the preservation system
     * 
     * <p><b>200</b> - Batch object diff
     * <p><b>400</b> - Response returned when an operation fails.
     * @param ingestId The ID for the Ingest Batch (required)
     * @param externalObjectId The external ID of the object (required)
     * @return DiffBatchObjectResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public DiffBatchObjectResponse diffBatchObject(Long ingestId, String externalObjectId) throws RestClientException {
        return diffBatchObjectWithHttpInfo(ingestId, externalObjectId).getBody();
    }

    /**
     * Diffs the batch object against the current state of the object in the preservation system
     * 
     * <p><b>200</b> - Batch object diff
     * <p><b>400</b> - Response returned when an operation fails.
     * @param ingestId The ID for the Ingest Batch (required)
     * @param externalObjectId The external ID of the object (required)
     * @return ResponseEntity&lt;DiffBatchObjectResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<DiffBatchObjectResponse> diffBatchObjectWithHttpInfo(Long ingestId, String externalObjectId) throws RestClientException {
        Object postBody = null;
        
        // verify the required parameter 'ingestId' is set
        if (ingestId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'ingestId' when calling diffBatchObject");
        }
        
        // verify the required parameter 'externalObjectId' is set
        if (externalObjectId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'externalObjectId' when calling diffBatchObject");
        }
        

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "ingestId", ingestId));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "externalObjectId", externalObjectId));

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] contentTypes = {  };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "ApiKeyAuth" };

        ParameterizedTypeReference<DiffBatchObjectResponse> returnType = new ParameterizedTypeReference<DiffBatchObjectResponse>() {};
        return apiClient.invokeAPI("/batch/object/diff", HttpMethod.GET, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Disables job types from being processed
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param disableJobTypesRequest  (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void disableJobTypes(DisableJobTypesRequest disableJobTypesRequest) throws RestClientException {
        disableJobTypesWithHttpInfo(disableJobTypesRequest);
    }

    /**
     * Disables job types from being processed
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param disableJobTypesRequest  (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> disableJobTypesWithHttpInfo(DisableJobTypesRequest disableJobTypesRequest) throws RestClientException {
        Object postBody = disableJobTypesRequest;
        

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

        String[] authNames = new String[] { "ApiKeyAuth" };

        ParameterizedTypeReference<Void> returnType = new ParameterizedTypeReference<Void>() {};
        return apiClient.invokeAPI("/job/disable", HttpMethod.POST, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Downloads the output of a retrieve job
     * 
     * <p><b>200</b> - OK
     * @param jobId The ID of the job (required)
     * @return File
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public File downloadJob(Long jobId) throws RestClientException {
        return downloadJobWithHttpInfo(jobId).getBody();
    }

    /**
     * Downloads the output of a retrieve job
     * 
     * <p><b>200</b> - OK
     * @param jobId The ID of the job (required)
     * @return ResponseEntity&lt;File&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<File> downloadJobWithHttpInfo(Long jobId) throws RestClientException {
        Object postBody = null;
        
        // verify the required parameter 'jobId' is set
        if (jobId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'jobId' when calling downloadJob");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("jobId", jobId);

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { 
            "application/zip"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] contentTypes = {  };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "ApiKeyAuth" };

        ParameterizedTypeReference<File> returnType = new ParameterizedTypeReference<File>() {};
        return apiClient.invokeAPI("/vault/retrieve/download/{jobId}", HttpMethod.GET, uriVariables, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Enables job types from being processed
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param enableJobTypesRequest  (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void enableJobTypes(EnableJobTypesRequest enableJobTypesRequest) throws RestClientException {
        enableJobTypesWithHttpInfo(enableJobTypesRequest);
    }

    /**
     * Enables job types from being processed
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param enableJobTypesRequest  (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> enableJobTypesWithHttpInfo(EnableJobTypesRequest enableJobTypesRequest) throws RestClientException {
        Object postBody = enableJobTypesRequest;
        

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

        String[] authNames = new String[] { "ApiKeyAuth" };

        ParameterizedTypeReference<Void> returnType = new ParameterizedTypeReference<Void>() {};
        return apiClient.invokeAPI("/job/enable", HttpMethod.POST, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Marks an executing job as failed. This action can only be completed by a service administrator, and should only be used when a job has a state of executing but is not being executed by a worker.
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param cancelJobRequest  (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void failJob(CancelJobRequest cancelJobRequest) throws RestClientException {
        failJobWithHttpInfo(cancelJobRequest);
    }

    /**
     * Marks an executing job as failed. This action can only be completed by a service administrator, and should only be used when a job has a state of executing but is not being executed by a worker.
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param cancelJobRequest  (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> failJobWithHttpInfo(CancelJobRequest cancelJobRequest) throws RestClientException {
        Object postBody = cancelJobRequest;
        

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

        String[] authNames = new String[] { "ApiKeyAuth" };

        ParameterizedTypeReference<Void> returnType = new ParameterizedTypeReference<Void>() {};
        return apiClient.invokeAPI("/job/fail", HttpMethod.POST, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Counts of all ingest events in the system or organization if specified.
     * 
     * <p><b>200</b> - System wide event counts, grouped by event outcome.
     * <p><b>400</b> - Response returned when an operation fails.
     * @param startDate The starting date for the event counts. (required)
     * @param endDate The ending date for the event counts. (required)
     * @param eventType Array of event types. (optional)
     * @param eventOutcome Array of event outcomes. (optional)
     * @param organizationName The ID of the organization. (optional)
     * @return SystemEventCountsResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public SystemEventCountsResponse getIngestEventCounts(LocalDate startDate, LocalDate endDate, List<EventType> eventType, List<EventOutcome> eventOutcome, String organizationName) throws RestClientException {
        return getIngestEventCountsWithHttpInfo(startDate, endDate, eventType, eventOutcome, organizationName).getBody();
    }

    /**
     * Counts of all ingest events in the system or organization if specified.
     * 
     * <p><b>200</b> - System wide event counts, grouped by event outcome.
     * <p><b>400</b> - Response returned when an operation fails.
     * @param startDate The starting date for the event counts. (required)
     * @param endDate The ending date for the event counts. (required)
     * @param eventType Array of event types. (optional)
     * @param eventOutcome Array of event outcomes. (optional)
     * @param organizationName The ID of the organization. (optional)
     * @return ResponseEntity&lt;SystemEventCountsResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<SystemEventCountsResponse> getIngestEventCountsWithHttpInfo(LocalDate startDate, LocalDate endDate, List<EventType> eventType, List<EventOutcome> eventOutcome, String organizationName) throws RestClientException {
        Object postBody = null;
        
        // verify the required parameter 'startDate' is set
        if (startDate == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'startDate' when calling getIngestEventCounts");
        }
        
        // verify the required parameter 'endDate' is set
        if (endDate == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'endDate' when calling getIngestEventCounts");
        }
        

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(ApiClient.CollectionFormat.valueOf("csv".toUpperCase(Locale.ROOT)), "eventType", eventType));
        queryParams.putAll(apiClient.parameterToMultiValueMap(ApiClient.CollectionFormat.valueOf("csv".toUpperCase(Locale.ROOT)), "eventOutcome", eventOutcome));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "startDate", startDate));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "endDate", endDate));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "organizationName", organizationName));

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] contentTypes = {  };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "ApiKeyAuth" };

        ParameterizedTypeReference<SystemEventCountsResponse> returnType = new ParameterizedTypeReference<SystemEventCountsResponse>() {};
        return apiClient.invokeAPI("/system/ingestEventCounts", HttpMethod.GET, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Counts of all preservation events in the system or organization if specified.
     * 
     * <p><b>200</b> - System wide event counts, grouped by event outcome.
     * <p><b>400</b> - Response returned when an operation fails.
     * @param startDate The starting date for the event counts. (required)
     * @param endDate The ending date for the event counts. (required)
     * @param eventType Array of event types. (optional)
     * @param eventOutcome Array of event outcomes. (optional)
     * @param organizationName The ID of the organization. (optional)
     * @return SystemEventCountsResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public SystemEventCountsResponse getPreservationEventCounts(LocalDate startDate, LocalDate endDate, List<EventType> eventType, List<EventOutcome> eventOutcome, String organizationName) throws RestClientException {
        return getPreservationEventCountsWithHttpInfo(startDate, endDate, eventType, eventOutcome, organizationName).getBody();
    }

    /**
     * Counts of all preservation events in the system or organization if specified.
     * 
     * <p><b>200</b> - System wide event counts, grouped by event outcome.
     * <p><b>400</b> - Response returned when an operation fails.
     * @param startDate The starting date for the event counts. (required)
     * @param endDate The ending date for the event counts. (required)
     * @param eventType Array of event types. (optional)
     * @param eventOutcome Array of event outcomes. (optional)
     * @param organizationName The ID of the organization. (optional)
     * @return ResponseEntity&lt;SystemEventCountsResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<SystemEventCountsResponse> getPreservationEventCountsWithHttpInfo(LocalDate startDate, LocalDate endDate, List<EventType> eventType, List<EventOutcome> eventOutcome, String organizationName) throws RestClientException {
        Object postBody = null;
        
        // verify the required parameter 'startDate' is set
        if (startDate == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'startDate' when calling getPreservationEventCounts");
        }
        
        // verify the required parameter 'endDate' is set
        if (endDate == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'endDate' when calling getPreservationEventCounts");
        }
        

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(ApiClient.CollectionFormat.valueOf("csv".toUpperCase(Locale.ROOT)), "eventType", eventType));
        queryParams.putAll(apiClient.parameterToMultiValueMap(ApiClient.CollectionFormat.valueOf("csv".toUpperCase(Locale.ROOT)), "eventOutcome", eventOutcome));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "startDate", startDate));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "endDate", endDate));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "organizationName", organizationName));

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] contentTypes = {  };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "ApiKeyAuth" };

        ParameterizedTypeReference<SystemEventCountsResponse> returnType = new ParameterizedTypeReference<SystemEventCountsResponse>() {};
        return apiClient.invokeAPI("/system/preservationEventCounts", HttpMethod.GET, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Retrieves system wide storage details, including a list of all organizations in the system and their storage details
     * 
     * <p><b>200</b> - Retrieves system wide storage details, including a list of all organizations in the system and their storage details
     * <p><b>400</b> - Response returned when an operation fails.
     * @return SystemStorageResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public SystemStorageResponse getSystemStorage() throws RestClientException {
        return getSystemStorageWithHttpInfo().getBody();
    }

    /**
     * Retrieves system wide storage details, including a list of all organizations in the system and their storage details
     * 
     * <p><b>200</b> - Retrieves system wide storage details, including a list of all organizations in the system and their storage details
     * <p><b>400</b> - Response returned when an operation fails.
     * @return ResponseEntity&lt;SystemStorageResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<SystemStorageResponse> getSystemStorageWithHttpInfo() throws RestClientException {
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

        String[] authNames = new String[] { "ApiKeyAuth" };

        ParameterizedTypeReference<SystemStorageResponse> returnType = new ParameterizedTypeReference<SystemStorageResponse>() {};
        return apiClient.invokeAPI("/system/storage", HttpMethod.GET, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Ingests the objects in a BagIt bag
     * 
     * <p><b>200</b> - Ingest Batch was successfully received, but not processed yet.
     * <p><b>400</b> - Response returned when an operation fails.
     * @param vault The name of the vault (required)
     * @param _file BagIt bag containing the objects to ingest. Must be a zip file. (required)
     * @return IngestBagResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public IngestBagResponse ingestBag(String vault, File _file) throws RestClientException {
        return ingestBagWithHttpInfo(vault, _file).getBody();
    }

    /**
     * Ingests the objects in a BagIt bag
     * 
     * <p><b>200</b> - Ingest Batch was successfully received, but not processed yet.
     * <p><b>400</b> - Response returned when an operation fails.
     * @param vault The name of the vault (required)
     * @param _file BagIt bag containing the objects to ingest. Must be a zip file. (required)
     * @return ResponseEntity&lt;IngestBagResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<IngestBagResponse> ingestBagWithHttpInfo(String vault, File _file) throws RestClientException {
        Object postBody = null;
        
        // verify the required parameter 'vault' is set
        if (vault == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'vault' when calling ingestBag");
        }
        
        // verify the required parameter '_file' is set
        if (_file == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter '_file' when calling ingestBag");
        }
        

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        if (vault != null)
            formParams.add("vault", vault);
        if (_file != null)
            formParams.add("file", new FileSystemResource(_file));

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] contentTypes = { 
            "multipart/form-data"
         };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "ApiKeyAuth" };

        ParameterizedTypeReference<IngestBagResponse> returnType = new ParameterizedTypeReference<IngestBagResponse>() {};
        return apiClient.invokeAPI("/ingest/bag", HttpMethod.POST, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Lists jobs that have a state of EXECUTING
     * 
     * <p><b>200</b> - List of jobs
     * <p><b>400</b> - Response returned when an operation fails.
     * @return ListJobsResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ListJobsResponse listExecutingJobs() throws RestClientException {
        return listExecutingJobsWithHttpInfo().getBody();
    }

    /**
     * Lists jobs that have a state of EXECUTING
     * 
     * <p><b>200</b> - List of jobs
     * <p><b>400</b> - Response returned when an operation fails.
     * @return ResponseEntity&lt;ListJobsResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<ListJobsResponse> listExecutingJobsWithHttpInfo() throws RestClientException {
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

        String[] authNames = new String[] { "ApiKeyAuth" };

        ParameterizedTypeReference<ListJobsResponse> returnType = new ParameterizedTypeReference<ListJobsResponse>() {};
        return apiClient.invokeAPI("/job/executing", HttpMethod.GET, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Lists jobs
     * 
     * <p><b>200</b> - List of jobs
     * <p><b>400</b> - Response returned when an operation fails.
     * @param orgName The ID of the organization (required)
     * @return ListJobsResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ListJobsResponse listJobs(String orgName) throws RestClientException {
        return listJobsWithHttpInfo(orgName).getBody();
    }

    /**
     * Lists jobs
     * 
     * <p><b>200</b> - List of jobs
     * <p><b>400</b> - Response returned when an operation fails.
     * @param orgName The ID of the organization (required)
     * @return ResponseEntity&lt;ListJobsResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<ListJobsResponse> listJobsWithHttpInfo(String orgName) throws RestClientException {
        Object postBody = null;
        
        // verify the required parameter 'orgName' is set
        if (orgName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'orgName' when calling listJobs");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("orgName", orgName);

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

        String[] authNames = new String[] { "ApiKeyAuth" };

        ParameterizedTypeReference<ListJobsResponse> returnType = new ParameterizedTypeReference<ListJobsResponse>() {};
        return apiClient.invokeAPI("/{orgName}/job", HttpMethod.GET, uriVariables, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * List all of the vaults in an org along with the user&#39;s permissions
     * 
     * <p><b>200</b> - List of all vaults in an org along with the user&#39;s permissions
     * <p><b>400</b> - Response returned when an operation fails.
     * @param orgName The ID of the organization (required)
     * @param username A user&#39;s username in the preservation system (optional)
     * @return ListVaultPermissionsResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ListVaultPermissionsResponse listOrgUserVaultPermissions(String orgName, String username) throws RestClientException {
        return listOrgUserVaultPermissionsWithHttpInfo(orgName, username).getBody();
    }

    /**
     * List all of the vaults in an org along with the user&#39;s permissions
     * 
     * <p><b>200</b> - List of all vaults in an org along with the user&#39;s permissions
     * <p><b>400</b> - Response returned when an operation fails.
     * @param orgName The ID of the organization (required)
     * @param username A user&#39;s username in the preservation system (optional)
     * @return ResponseEntity&lt;ListVaultPermissionsResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<ListVaultPermissionsResponse> listOrgUserVaultPermissionsWithHttpInfo(String orgName, String username) throws RestClientException {
        Object postBody = null;
        
        // verify the required parameter 'orgName' is set
        if (orgName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'orgName' when calling listOrgUserVaultPermissions");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("orgName", orgName);

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "username", username));

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] contentTypes = {  };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "ApiKeyAuth" };

        ParameterizedTypeReference<ListVaultPermissionsResponse> returnType = new ParameterizedTypeReference<ListVaultPermissionsResponse>() {};
        return apiClient.invokeAPI("/vault/{orgName}/permission", HttpMethod.GET, uriVariables, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Retrieves a list of all of the members of an organization
     * 
     * <p><b>200</b> - List of users in an organization
     * <p><b>400</b> - Response returned when an operation fails.
     * @param orgName The ID of the organization (required)
     * @return ListOrgUsersResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ListOrgUsersResponse listOrgUsers(String orgName) throws RestClientException {
        return listOrgUsersWithHttpInfo(orgName).getBody();
    }

    /**
     * Retrieves a list of all of the members of an organization
     * 
     * <p><b>200</b> - List of users in an organization
     * <p><b>400</b> - Response returned when an operation fails.
     * @param orgName The ID of the organization (required)
     * @return ResponseEntity&lt;ListOrgUsersResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<ListOrgUsersResponse> listOrgUsersWithHttpInfo(String orgName) throws RestClientException {
        Object postBody = null;
        
        // verify the required parameter 'orgName' is set
        if (orgName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'orgName' when calling listOrgUsers");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("orgName", orgName);

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

        String[] authNames = new String[] { "ApiKeyAuth" };

        ParameterizedTypeReference<ListOrgUsersResponse> returnType = new ParameterizedTypeReference<ListOrgUsersResponse>() {};
        return apiClient.invokeAPI("/org/{orgName}/user", HttpMethod.GET, uriVariables, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Retrieves a list of all organizations the authenticated user is an active member of
     * 
     * <p><b>200</b> - List of organizations
     * <p><b>400</b> - Response returned when an operation fails.
     * @return ListUserOrgsResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ListUserOrgsResponse listUserOrgs() throws RestClientException {
        return listUserOrgsWithHttpInfo().getBody();
    }

    /**
     * Retrieves a list of all organizations the authenticated user is an active member of
     * 
     * <p><b>200</b> - List of organizations
     * <p><b>400</b> - Response returned when an operation fails.
     * @return ResponseEntity&lt;ListUserOrgsResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<ListUserOrgsResponse> listUserOrgsWithHttpInfo() throws RestClientException {
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

        String[] authNames = new String[] { "ApiKeyAuth" };

        ParameterizedTypeReference<ListUserOrgsResponse> returnType = new ParameterizedTypeReference<ListUserOrgsResponse>() {};
        return apiClient.invokeAPI("/user/org", HttpMethod.GET, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Retrieves a list of users with read or write permissions for the given vault
     * 
     * <p><b>200</b> - List of all of the users who have access to the given vault along with their permissions
     * <p><b>400</b> - Response returned when an operation fails.
     * @param vault The name of the vault (required)
     * @return ListVaultUsersResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ListVaultUsersResponse listUsersInVault(String vault) throws RestClientException {
        return listUsersInVaultWithHttpInfo(vault).getBody();
    }

    /**
     * Retrieves a list of users with read or write permissions for the given vault
     * 
     * <p><b>200</b> - List of all of the users who have access to the given vault along with their permissions
     * <p><b>400</b> - Response returned when an operation fails.
     * @param vault The name of the vault (required)
     * @return ResponseEntity&lt;ListVaultUsersResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<ListVaultUsersResponse> listUsersInVaultWithHttpInfo(String vault) throws RestClientException {
        Object postBody = null;
        
        // verify the required parameter 'vault' is set
        if (vault == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'vault' when calling listUsersInVault");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("vault", vault);

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

        String[] authNames = new String[] { "ApiKeyAuth" };

        ParameterizedTypeReference<ListVaultUsersResponse> returnType = new ParameterizedTypeReference<ListVaultUsersResponse>() {};
        return apiClient.invokeAPI("/vault/{vault}/user", HttpMethod.GET, uriVariables, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Returns a list of objects within the vault for which storage problems have been identified
     * 
     * <p><b>200</b> - List of objects within the vault for which storage problems have been identified
     * <p><b>400</b> - Response returned when an operation fails.
     * @param vault The name of the vault (required)
     * @return ListVaultProblemsResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ListVaultProblemsResponse listVaultProblems(String vault) throws RestClientException {
        return listVaultProblemsWithHttpInfo(vault).getBody();
    }

    /**
     * Returns a list of objects within the vault for which storage problems have been identified
     * 
     * <p><b>200</b> - List of objects within the vault for which storage problems have been identified
     * <p><b>400</b> - Response returned when an operation fails.
     * @param vault The name of the vault (required)
     * @return ResponseEntity&lt;ListVaultProblemsResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<ListVaultProblemsResponse> listVaultProblemsWithHttpInfo(String vault) throws RestClientException {
        Object postBody = null;
        
        // verify the required parameter 'vault' is set
        if (vault == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'vault' when calling listVaultProblems");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("vault", vault);

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

        String[] authNames = new String[] { "ApiKeyAuth" };

        ParameterizedTypeReference<ListVaultProblemsResponse> returnType = new ParameterizedTypeReference<ListVaultProblemsResponse>() {};
        return apiClient.invokeAPI("/vault/{vault}/problems", HttpMethod.GET, uriVariables, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Lists all vaults for an organization
     * 
     * <p><b>200</b> - List of vaults
     * <p><b>400</b> - Response returned when an operation fails.
     * @param orgName The ID of the organization (required)
     * @return ListVaultsResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ListVaultsResponse listVaults(String orgName) throws RestClientException {
        return listVaultsWithHttpInfo(orgName).getBody();
    }

    /**
     * Lists all vaults for an organization
     * 
     * <p><b>200</b> - List of vaults
     * <p><b>400</b> - Response returned when an operation fails.
     * @param orgName The ID of the organization (required)
     * @return ResponseEntity&lt;ListVaultsResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<ListVaultsResponse> listVaultsWithHttpInfo(String orgName) throws RestClientException {
        Object postBody = null;
        
        // verify the required parameter 'orgName' is set
        if (orgName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'orgName' when calling listVaults");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("orgName", orgName);

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

        String[] authNames = new String[] { "ApiKeyAuth" };

        ParameterizedTypeReference<ListVaultsResponse> returnType = new ParameterizedTypeReference<ListVaultsResponse>() {};
        return apiClient.invokeAPI("/vault/{orgName}", HttpMethod.GET, uriVariables, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Rejects a batch for ingestion
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param rejectIngestBatchRequest  (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void rejectIngestBatch(RejectIngestBatchRequest rejectIngestBatchRequest) throws RestClientException {
        rejectIngestBatchWithHttpInfo(rejectIngestBatchRequest);
    }

    /**
     * Rejects a batch for ingestion
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param rejectIngestBatchRequest  (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> rejectIngestBatchWithHttpInfo(RejectIngestBatchRequest rejectIngestBatchRequest) throws RestClientException {
        Object postBody = rejectIngestBatchRequest;
        

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

        String[] authNames = new String[] { "ApiKeyAuth" };

        ParameterizedTypeReference<Void> returnType = new ParameterizedTypeReference<Void>() {};
        return apiClient.invokeAPI("/batch/reject", HttpMethod.POST, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Rejects an object for ingestion
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param rejectIngestObjectRequest  (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void rejectIngestObject(RejectIngestObjectRequest rejectIngestObjectRequest) throws RestClientException {
        rejectIngestObjectWithHttpInfo(rejectIngestObjectRequest);
    }

    /**
     * Rejects an object for ingestion
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param rejectIngestObjectRequest  (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> rejectIngestObjectWithHttpInfo(RejectIngestObjectRequest rejectIngestObjectRequest) throws RestClientException {
        Object postBody = rejectIngestObjectRequest;
        

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

        String[] authNames = new String[] { "ApiKeyAuth" };

        ParameterizedTypeReference<Void> returnType = new ParameterizedTypeReference<Void>() {};
        return apiClient.invokeAPI("/batch/object/reject", HttpMethod.POST, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Replicates an object from local storage to a remote
     * 
     * <p><b>200</b> - Replicate object response
     * <p><b>400</b> - Response returned when an operation fails.
     * @param replicateObjectRequest  (optional)
     * @return ReplicateObjectResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ReplicateObjectResponse replicateObject(ReplicateObjectRequest replicateObjectRequest) throws RestClientException {
        return replicateObjectWithHttpInfo(replicateObjectRequest).getBody();
    }

    /**
     * Replicates an object from local storage to a remote
     * 
     * <p><b>200</b> - Replicate object response
     * <p><b>400</b> - Response returned when an operation fails.
     * @param replicateObjectRequest  (optional)
     * @return ResponseEntity&lt;ReplicateObjectResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<ReplicateObjectResponse> replicateObjectWithHttpInfo(ReplicateObjectRequest replicateObjectRequest) throws RestClientException {
        Object postBody = replicateObjectRequest;
        

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

        String[] authNames = new String[] { "ApiKeyAuth" };

        ParameterizedTypeReference<ReplicateObjectResponse> returnType = new ParameterizedTypeReference<ReplicateObjectResponse>() {};
        return apiClient.invokeAPI("/job/replicate", HttpMethod.POST, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Restores the local copy of an object from a remote source
     * 
     * <p><b>200</b> - Restore object response
     * <p><b>400</b> - Response returned when an operation fails.
     * @param restorePreservationObjectRequest  (optional)
     * @return RestorePreservationObjectResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public RestorePreservationObjectResponse restoreObject(RestorePreservationObjectRequest restorePreservationObjectRequest) throws RestClientException {
        return restoreObjectWithHttpInfo(restorePreservationObjectRequest).getBody();
    }

    /**
     * Restores the local copy of an object from a remote source
     * 
     * <p><b>200</b> - Restore object response
     * <p><b>400</b> - Response returned when an operation fails.
     * @param restorePreservationObjectRequest  (optional)
     * @return ResponseEntity&lt;RestorePreservationObjectResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<RestorePreservationObjectResponse> restoreObjectWithHttpInfo(RestorePreservationObjectRequest restorePreservationObjectRequest) throws RestClientException {
        Object postBody = restorePreservationObjectRequest;
        

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

        String[] authNames = new String[] { "ApiKeyAuth" };

        ParameterizedTypeReference<RestorePreservationObjectResponse> returnType = new ParameterizedTypeReference<RestorePreservationObjectResponse>() {};
        return apiClient.invokeAPI("/job/restore", HttpMethod.POST, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
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

        String[] authNames = new String[] { "ApiKeyAuth" };

        ParameterizedTypeReference<RetrieveBatchResponse> returnType = new ParameterizedTypeReference<RetrieveBatchResponse>() {};
        return apiClient.invokeAPI("/batch/{ingestId}", HttpMethod.GET, uriVariables, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Retrieves all of the events associated to the batch. This does not include events associated to objects in the batch
     * 
     * <p><b>200</b> - Batch events
     * <p><b>400</b> - Response returned when an operation fails.
     * @param ingestId The ID for the Ingest Batch (required)
     * @return RetrieveEventsResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public RetrieveEventsResponse retrieveBatchEvents(Long ingestId) throws RestClientException {
        return retrieveBatchEventsWithHttpInfo(ingestId).getBody();
    }

    /**
     * Retrieves all of the events associated to the batch. This does not include events associated to objects in the batch
     * 
     * <p><b>200</b> - Batch events
     * <p><b>400</b> - Response returned when an operation fails.
     * @param ingestId The ID for the Ingest Batch (required)
     * @return ResponseEntity&lt;RetrieveEventsResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<RetrieveEventsResponse> retrieveBatchEventsWithHttpInfo(Long ingestId) throws RestClientException {
        Object postBody = null;
        
        // verify the required parameter 'ingestId' is set
        if (ingestId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'ingestId' when calling retrieveBatchEvents");
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

        String[] authNames = new String[] { "ApiKeyAuth" };

        ParameterizedTypeReference<RetrieveEventsResponse> returnType = new ParameterizedTypeReference<RetrieveEventsResponse>() {};
        return apiClient.invokeAPI("/batch/{ingestId}/event", HttpMethod.GET, uriVariables, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Retrieves details about an Ingest Batch Object
     * 
     * <p><b>200</b> - Ingest Batch Object details
     * <p><b>400</b> - Response returned when an operation fails.
     * @param ingestId The ID for the Ingest Batch (required)
     * @param externalObjectId The external ID of the object (required)
     * @return RetrieveBatchObjectResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public RetrieveBatchObjectResponse retrieveBatchObject(Long ingestId, String externalObjectId) throws RestClientException {
        return retrieveBatchObjectWithHttpInfo(ingestId, externalObjectId).getBody();
    }

    /**
     * Retrieves details about an Ingest Batch Object
     * 
     * <p><b>200</b> - Ingest Batch Object details
     * <p><b>400</b> - Response returned when an operation fails.
     * @param ingestId The ID for the Ingest Batch (required)
     * @param externalObjectId The external ID of the object (required)
     * @return ResponseEntity&lt;RetrieveBatchObjectResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<RetrieveBatchObjectResponse> retrieveBatchObjectWithHttpInfo(Long ingestId, String externalObjectId) throws RestClientException {
        Object postBody = null;
        
        // verify the required parameter 'ingestId' is set
        if (ingestId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'ingestId' when calling retrieveBatchObject");
        }
        
        // verify the required parameter 'externalObjectId' is set
        if (externalObjectId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'externalObjectId' when calling retrieveBatchObject");
        }
        

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "ingestId", ingestId));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "externalObjectId", externalObjectId));

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] contentTypes = {  };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "ApiKeyAuth" };

        ParameterizedTypeReference<RetrieveBatchObjectResponse> returnType = new ParameterizedTypeReference<RetrieveBatchObjectResponse>() {};
        return apiClient.invokeAPI("/batch/object", HttpMethod.GET, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Retrieves all of the events associated to the object in the batch
     * 
     * <p><b>200</b> - Object events
     * <p><b>400</b> - Response returned when an operation fails.
     * @param ingestId The ID for the Ingest Batch (required)
     * @param externalObjectId The external ID of the object (required)
     * @return RetrieveEventsResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public RetrieveEventsResponse retrieveBatchObjectEvents(Long ingestId, String externalObjectId) throws RestClientException {
        return retrieveBatchObjectEventsWithHttpInfo(ingestId, externalObjectId).getBody();
    }

    /**
     * Retrieves all of the events associated to the object in the batch
     * 
     * <p><b>200</b> - Object events
     * <p><b>400</b> - Response returned when an operation fails.
     * @param ingestId The ID for the Ingest Batch (required)
     * @param externalObjectId The external ID of the object (required)
     * @return ResponseEntity&lt;RetrieveEventsResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<RetrieveEventsResponse> retrieveBatchObjectEventsWithHttpInfo(Long ingestId, String externalObjectId) throws RestClientException {
        Object postBody = null;
        
        // verify the required parameter 'ingestId' is set
        if (ingestId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'ingestId' when calling retrieveBatchObjectEvents");
        }
        
        // verify the required parameter 'externalObjectId' is set
        if (externalObjectId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'externalObjectId' when calling retrieveBatchObjectEvents");
        }
        

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "ingestId", ingestId));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "externalObjectId", externalObjectId));

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] contentTypes = {  };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "ApiKeyAuth" };

        ParameterizedTypeReference<RetrieveEventsResponse> returnType = new ParameterizedTypeReference<RetrieveEventsResponse>() {};
        return apiClient.invokeAPI("/batch/object/event", HttpMethod.GET, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Retrieves a specific file associated to an Ingest Batch Object
     * 
     * <p><b>200</b> - Ingest Batch Object file
     * <p><b>400</b> - Response returned when an operation fails.
     * @param ingestId The ID for the Ingest Batch (required)
     * @param externalObjectId The external ID of the object (required)
     * @param filePath The object relative path of the file (required)
     * @return RetrieveBatchObjectFileResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public RetrieveBatchObjectFileResponse retrieveBatchObjectFile(Long ingestId, String externalObjectId, String filePath) throws RestClientException {
        return retrieveBatchObjectFileWithHttpInfo(ingestId, externalObjectId, filePath).getBody();
    }

    /**
     * Retrieves a specific file associated to an Ingest Batch Object
     * 
     * <p><b>200</b> - Ingest Batch Object file
     * <p><b>400</b> - Response returned when an operation fails.
     * @param ingestId The ID for the Ingest Batch (required)
     * @param externalObjectId The external ID of the object (required)
     * @param filePath The object relative path of the file (required)
     * @return ResponseEntity&lt;RetrieveBatchObjectFileResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<RetrieveBatchObjectFileResponse> retrieveBatchObjectFileWithHttpInfo(Long ingestId, String externalObjectId, String filePath) throws RestClientException {
        Object postBody = null;
        
        // verify the required parameter 'ingestId' is set
        if (ingestId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'ingestId' when calling retrieveBatchObjectFile");
        }
        
        // verify the required parameter 'externalObjectId' is set
        if (externalObjectId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'externalObjectId' when calling retrieveBatchObjectFile");
        }
        
        // verify the required parameter 'filePath' is set
        if (filePath == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'filePath' when calling retrieveBatchObjectFile");
        }
        

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "ingestId", ingestId));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "externalObjectId", externalObjectId));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "filePath", filePath));

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] contentTypes = {  };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "ApiKeyAuth" };

        ParameterizedTypeReference<RetrieveBatchObjectFileResponse> returnType = new ParameterizedTypeReference<RetrieveBatchObjectFileResponse>() {};
        return apiClient.invokeAPI("/batch/object/file", HttpMethod.GET, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Retrieves the files associated to an Ingest Batch Object
     * 
     * <p><b>200</b> - Ingest Batch Object files
     * <p><b>400</b> - Response returned when an operation fails.
     * @param ingestId The ID for the Ingest Batch (required)
     * @param externalObjectId The external ID of the object (required)
     * @return RetrieveBatchObjectFilesResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public RetrieveBatchObjectFilesResponse retrieveBatchObjectFiles(Long ingestId, String externalObjectId) throws RestClientException {
        return retrieveBatchObjectFilesWithHttpInfo(ingestId, externalObjectId).getBody();
    }

    /**
     * Retrieves the files associated to an Ingest Batch Object
     * 
     * <p><b>200</b> - Ingest Batch Object files
     * <p><b>400</b> - Response returned when an operation fails.
     * @param ingestId The ID for the Ingest Batch (required)
     * @param externalObjectId The external ID of the object (required)
     * @return ResponseEntity&lt;RetrieveBatchObjectFilesResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<RetrieveBatchObjectFilesResponse> retrieveBatchObjectFilesWithHttpInfo(Long ingestId, String externalObjectId) throws RestClientException {
        Object postBody = null;
        
        // verify the required parameter 'ingestId' is set
        if (ingestId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'ingestId' when calling retrieveBatchObjectFiles");
        }
        
        // verify the required parameter 'externalObjectId' is set
        if (externalObjectId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'externalObjectId' when calling retrieveBatchObjectFiles");
        }
        

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "ingestId", ingestId));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "externalObjectId", externalObjectId));

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] contentTypes = {  };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "ApiKeyAuth" };

        ParameterizedTypeReference<RetrieveBatchObjectFilesResponse> returnType = new ParameterizedTypeReference<RetrieveBatchObjectFilesResponse>() {};
        return apiClient.invokeAPI("/batch/object/files", HttpMethod.GET, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Retrieves Ingest Batch Objects
     * 
     * <p><b>200</b> - Ingest Batch Object details
     * <p><b>400</b> - Response returned when an operation fails.
     * @param ingestId The ID for the Ingest Batch (required)
     * @param pageSize The number of results to include per page (default 20) (optional)
     * @param page The result page to retrieve, 0 indexed (optional)
     * @param hasProblems Filter objects by the existence of errors or warnings. WARNINGS returns objects that only have warnings; ERRORS returns any object that has errors. (optional)
     * @return RetrieveBatchObjectsResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public RetrieveBatchObjectsResponse retrieveBatchObjects(Long ingestId, Integer pageSize, Integer page, String hasProblems) throws RestClientException {
        return retrieveBatchObjectsWithHttpInfo(ingestId, pageSize, page, hasProblems).getBody();
    }

    /**
     * Retrieves Ingest Batch Objects
     * 
     * <p><b>200</b> - Ingest Batch Object details
     * <p><b>400</b> - Response returned when an operation fails.
     * @param ingestId The ID for the Ingest Batch (required)
     * @param pageSize The number of results to include per page (default 20) (optional)
     * @param page The result page to retrieve, 0 indexed (optional)
     * @param hasProblems Filter objects by the existence of errors or warnings. WARNINGS returns objects that only have warnings; ERRORS returns any object that has errors. (optional)
     * @return ResponseEntity&lt;RetrieveBatchObjectsResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<RetrieveBatchObjectsResponse> retrieveBatchObjectsWithHttpInfo(Long ingestId, Integer pageSize, Integer page, String hasProblems) throws RestClientException {
        Object postBody = null;
        
        // verify the required parameter 'ingestId' is set
        if (ingestId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'ingestId' when calling retrieveBatchObjects");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("ingestId", ingestId);

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "pageSize", pageSize));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "page", page));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "hasProblems", hasProblems));

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] contentTypes = {  };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "ApiKeyAuth" };

        ParameterizedTypeReference<RetrieveBatchObjectsResponse> returnType = new ParameterizedTypeReference<RetrieveBatchObjectsResponse>() {};
        return apiClient.invokeAPI("/batch/{ingestId}/objects", HttpMethod.GET, uriVariables, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Retrieves all of the logs associated with a job
     * 
     * <p><b>200</b> - Ingest logs
     * <p><b>400</b> - Response returned when an operation fails.
     * @param jobId The ID of the job (required)
     * @return RetrieveLogsResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public RetrieveLogsResponse retrieveJobLogs(Long jobId) throws RestClientException {
        return retrieveJobLogsWithHttpInfo(jobId).getBody();
    }

    /**
     * Retrieves all of the logs associated with a job
     * 
     * <p><b>200</b> - Ingest logs
     * <p><b>400</b> - Response returned when an operation fails.
     * @param jobId The ID of the job (required)
     * @return ResponseEntity&lt;RetrieveLogsResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<RetrieveLogsResponse> retrieveJobLogsWithHttpInfo(Long jobId) throws RestClientException {
        Object postBody = null;
        
        // verify the required parameter 'jobId' is set
        if (jobId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'jobId' when calling retrieveJobLogs");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("jobId", jobId);

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

        String[] authNames = new String[] { "ApiKeyAuth" };

        ParameterizedTypeReference<RetrieveLogsResponse> returnType = new ParameterizedTypeReference<RetrieveLogsResponse>() {};
        return apiClient.invokeAPI("/job/{jobId}/logs", HttpMethod.GET, uriVariables, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Retrieves all of the events associated with an object
     * 
     * <p><b>200</b> - Object event logs
     * <p><b>400</b> - Response returned when an operation fails.
     * @param vault The vault an object is in (required)
     * @param externalObjectId The external ID of the object (required)
     * @return RetrieveEventsResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public RetrieveEventsResponse retrieveObjectEvents(String vault, String externalObjectId) throws RestClientException {
        return retrieveObjectEventsWithHttpInfo(vault, externalObjectId).getBody();
    }

    /**
     * Retrieves all of the events associated with an object
     * 
     * <p><b>200</b> - Object event logs
     * <p><b>400</b> - Response returned when an operation fails.
     * @param vault The vault an object is in (required)
     * @param externalObjectId The external ID of the object (required)
     * @return ResponseEntity&lt;RetrieveEventsResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<RetrieveEventsResponse> retrieveObjectEventsWithHttpInfo(String vault, String externalObjectId) throws RestClientException {
        Object postBody = null;
        
        // verify the required parameter 'vault' is set
        if (vault == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'vault' when calling retrieveObjectEvents");
        }
        
        // verify the required parameter 'externalObjectId' is set
        if (externalObjectId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'externalObjectId' when calling retrieveObjectEvents");
        }
        

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "vault", vault));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "externalObjectId", externalObjectId));

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] contentTypes = {  };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "ApiKeyAuth" };

        ParameterizedTypeReference<RetrieveEventsResponse> returnType = new ParameterizedTypeReference<RetrieveEventsResponse>() {};
        return apiClient.invokeAPI("/object/event", HttpMethod.GET, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Retrieves details about what storage problems are affecting an object
     * 
     * <p><b>200</b> - The object&#39;s storage problems, if any
     * <p><b>400</b> - Response returned when an operation fails.
     * @param vault The vault an object is in (required)
     * @param externalObjectId The external ID of the object (required)
     * @return RetrieveObjectStorageProblemsResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public RetrieveObjectStorageProblemsResponse retrieveObjectStorageProblems(String vault, String externalObjectId) throws RestClientException {
        return retrieveObjectStorageProblemsWithHttpInfo(vault, externalObjectId).getBody();
    }

    /**
     * Retrieves details about what storage problems are affecting an object
     * 
     * <p><b>200</b> - The object&#39;s storage problems, if any
     * <p><b>400</b> - Response returned when an operation fails.
     * @param vault The vault an object is in (required)
     * @param externalObjectId The external ID of the object (required)
     * @return ResponseEntity&lt;RetrieveObjectStorageProblemsResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<RetrieveObjectStorageProblemsResponse> retrieveObjectStorageProblemsWithHttpInfo(String vault, String externalObjectId) throws RestClientException {
        Object postBody = null;
        
        // verify the required parameter 'vault' is set
        if (vault == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'vault' when calling retrieveObjectStorageProblems");
        }
        
        // verify the required parameter 'externalObjectId' is set
        if (externalObjectId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'externalObjectId' when calling retrieveObjectStorageProblems");
        }
        

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "vault", vault));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "externalObjectId", externalObjectId));

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] contentTypes = {  };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "ApiKeyAuth" };

        ParameterizedTypeReference<RetrieveObjectStorageProblemsResponse> returnType = new ParameterizedTypeReference<RetrieveObjectStorageProblemsResponse>() {};
        return apiClient.invokeAPI("/object/problems/storage", HttpMethod.GET, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Requests the retrieval of preserved objects
     * 
     * <p><b>200</b> - The result of the retrieve request operation
     * <p><b>400</b> - Response returned when an operation fails.
     * @param retrieveObjectsRequest  (optional)
     * @return RetrieveObjectsResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public RetrieveObjectsResponse retrieveObjects(RetrieveObjectsRequest retrieveObjectsRequest) throws RestClientException {
        return retrieveObjectsWithHttpInfo(retrieveObjectsRequest).getBody();
    }

    /**
     * Requests the retrieval of preserved objects
     * 
     * <p><b>200</b> - The result of the retrieve request operation
     * <p><b>400</b> - Response returned when an operation fails.
     * @param retrieveObjectsRequest  (optional)
     * @return ResponseEntity&lt;RetrieveObjectsResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<RetrieveObjectsResponse> retrieveObjectsWithHttpInfo(RetrieveObjectsRequest retrieveObjectsRequest) throws RestClientException {
        Object postBody = retrieveObjectsRequest;
        

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

        String[] authNames = new String[] { "ApiKeyAuth" };

        ParameterizedTypeReference<RetrieveObjectsResponse> returnType = new ParameterizedTypeReference<RetrieveObjectsResponse>() {};
        return apiClient.invokeAPI("/vault/retrieve", HttpMethod.POST, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Retries a batch ingest if it has failed
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param batchRetryIngestRequest  (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void retryBatchIngest(BatchRetryIngestRequest batchRetryIngestRequest) throws RestClientException {
        retryBatchIngestWithHttpInfo(batchRetryIngestRequest);
    }

    /**
     * Retries a batch ingest if it has failed
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param batchRetryIngestRequest  (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> retryBatchIngestWithHttpInfo(BatchRetryIngestRequest batchRetryIngestRequest) throws RestClientException {
        Object postBody = batchRetryIngestRequest;
        

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

        String[] authNames = new String[] { "ApiKeyAuth" };

        ParameterizedTypeReference<Void> returnType = new ParameterizedTypeReference<Void>() {};
        return apiClient.invokeAPI("/batch/retryIngest", HttpMethod.POST, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Retries a batch replicate if it has failed
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param batchRetryReplicateRequest  (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void retryBatchReplicate(BatchRetryReplicateRequest batchRetryReplicateRequest) throws RestClientException {
        retryBatchReplicateWithHttpInfo(batchRetryReplicateRequest);
    }

    /**
     * Retries a batch replicate if it has failed
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param batchRetryReplicateRequest  (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> retryBatchReplicateWithHttpInfo(BatchRetryReplicateRequest batchRetryReplicateRequest) throws RestClientException {
        Object postBody = batchRetryReplicateRequest;
        

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

        String[] authNames = new String[] { "ApiKeyAuth" };

        ParameterizedTypeReference<Void> returnType = new ParameterizedTypeReference<Void>() {};
        return apiClient.invokeAPI("/batch/retryReplicate", HttpMethod.POST, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Retries a failed retrieve job
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param requestId The ID of the request (required)
     * @param jobId The ID of the job (required)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void retryRetrieveJob(Long requestId, Long jobId) throws RestClientException {
        retryRetrieveJobWithHttpInfo(requestId, jobId);
    }

    /**
     * Retries a failed retrieve job
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param requestId The ID of the request (required)
     * @param jobId The ID of the job (required)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> retryRetrieveJobWithHttpInfo(Long requestId, Long jobId) throws RestClientException {
        Object postBody = null;
        
        // verify the required parameter 'requestId' is set
        if (requestId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'requestId' when calling retryRetrieveJob");
        }
        
        // verify the required parameter 'jobId' is set
        if (jobId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'jobId' when calling retryRetrieveJob");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("requestId", requestId);
        uriVariables.put("jobId", jobId);

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

        String[] authNames = new String[] { "ApiKeyAuth" };

        ParameterizedTypeReference<Void> returnType = new ParameterizedTypeReference<Void>() {};
        return apiClient.invokeAPI("/vault/retrieve/{requestId}/retry/{jobId}", HttpMethod.POST, uriVariables, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Search for batches
     * 
     * <p><b>200</b> - Batch search results sorted by the batch updated timestamp in reverse chronological order
     * <p><b>400</b> - Response returned when an operation fails.
     * @param orgName The ID of the organization (optional)
     * @param vault The vault an object is in (optional)
     * @param state The state the batch is in (optional)
     * @param pageSize The number of results to include per page (default 20) (optional)
     * @param page The result page to retrieve, 0 indexed (optional)
     * @return BatchSearchResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public BatchSearchResponse searchBatches(String orgName, List<String> vault, List<String> state, Integer pageSize, Integer page) throws RestClientException {
        return searchBatchesWithHttpInfo(orgName, vault, state, pageSize, page).getBody();
    }

    /**
     * Search for batches
     * 
     * <p><b>200</b> - Batch search results sorted by the batch updated timestamp in reverse chronological order
     * <p><b>400</b> - Response returned when an operation fails.
     * @param orgName The ID of the organization (optional)
     * @param vault The vault an object is in (optional)
     * @param state The state the batch is in (optional)
     * @param pageSize The number of results to include per page (default 20) (optional)
     * @param page The result page to retrieve, 0 indexed (optional)
     * @return ResponseEntity&lt;BatchSearchResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<BatchSearchResponse> searchBatchesWithHttpInfo(String orgName, List<String> vault, List<String> state, Integer pageSize, Integer page) throws RestClientException {
        Object postBody = null;
        

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "orgName", orgName));
        queryParams.putAll(apiClient.parameterToMultiValueMap(ApiClient.CollectionFormat.valueOf("csv".toUpperCase(Locale.ROOT)), "vault", vault));
        queryParams.putAll(apiClient.parameterToMultiValueMap(ApiClient.CollectionFormat.valueOf("csv".toUpperCase(Locale.ROOT)), "state", state));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "pageSize", pageSize));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "page", page));

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] contentTypes = {  };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "ApiKeyAuth" };

        ParameterizedTypeReference<BatchSearchResponse> returnType = new ParameterizedTypeReference<BatchSearchResponse>() {};
        return apiClient.invokeAPI("/batch/search", HttpMethod.GET, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Updates a user&#39;s permissions on a vault. Any existing permissions are overwritten. An empty permission list removes all permissions for the vault.
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param updateUserVaultPermissionsRequest  (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void updateUserVaultPermissions(UpdateUserVaultPermissionsRequest updateUserVaultPermissionsRequest) throws RestClientException {
        updateUserVaultPermissionsWithHttpInfo(updateUserVaultPermissionsRequest);
    }

    /**
     * Updates a user&#39;s permissions on a vault. Any existing permissions are overwritten. An empty permission list removes all permissions for the vault.
     * 
     * <p><b>200</b> - Success
     * <p><b>400</b> - Response returned when an operation fails.
     * @param updateUserVaultPermissionsRequest  (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> updateUserVaultPermissionsWithHttpInfo(UpdateUserVaultPermissionsRequest updateUserVaultPermissionsRequest) throws RestClientException {
        Object postBody = updateUserVaultPermissionsRequest;
        

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

        String[] authNames = new String[] { "ApiKeyAuth" };

        ParameterizedTypeReference<Void> returnType = new ParameterizedTypeReference<Void>() {};
        return apiClient.invokeAPI("/user/vault", HttpMethod.POST, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Validates the local copy of an object
     * 
     * <p><b>200</b> - Validate object response
     * <p><b>400</b> - Response returned when an operation fails.
     * @param validatePreservationObjectRequest  (optional)
     * @return ValidatePreservationObjectResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ValidatePreservationObjectResponse validateObject(ValidatePreservationObjectRequest validatePreservationObjectRequest) throws RestClientException {
        return validateObjectWithHttpInfo(validatePreservationObjectRequest).getBody();
    }

    /**
     * Validates the local copy of an object
     * 
     * <p><b>200</b> - Validate object response
     * <p><b>400</b> - Response returned when an operation fails.
     * @param validatePreservationObjectRequest  (optional)
     * @return ResponseEntity&lt;ValidatePreservationObjectResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<ValidatePreservationObjectResponse> validateObjectWithHttpInfo(ValidatePreservationObjectRequest validatePreservationObjectRequest) throws RestClientException {
        Object postBody = validatePreservationObjectRequest;
        

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

        String[] authNames = new String[] { "ApiKeyAuth" };

        ParameterizedTypeReference<ValidatePreservationObjectResponse> returnType = new ParameterizedTypeReference<ValidatePreservationObjectResponse>() {};
        return apiClient.invokeAPI("/job/validate", HttpMethod.POST, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
    /**
     * Validates a remote copy of an object
     * 
     * <p><b>200</b> - Validate object response
     * <p><b>400</b> - Response returned when an operation fails.
     * @param validateObjectRemoteRequest  (optional)
     * @return ValidateObjectRemoteResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ValidateObjectRemoteResponse validateObjectRemote(ValidateObjectRemoteRequest validateObjectRemoteRequest) throws RestClientException {
        return validateObjectRemoteWithHttpInfo(validateObjectRemoteRequest).getBody();
    }

    /**
     * Validates a remote copy of an object
     * 
     * <p><b>200</b> - Validate object response
     * <p><b>400</b> - Response returned when an operation fails.
     * @param validateObjectRemoteRequest  (optional)
     * @return ResponseEntity&lt;ValidateObjectRemoteResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<ValidateObjectRemoteResponse> validateObjectRemoteWithHttpInfo(ValidateObjectRemoteRequest validateObjectRemoteRequest) throws RestClientException {
        Object postBody = validateObjectRemoteRequest;
        

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

        String[] authNames = new String[] { "ApiKeyAuth" };

        ParameterizedTypeReference<ValidateObjectRemoteResponse> returnType = new ParameterizedTypeReference<ValidateObjectRemoteResponse>() {};
        return apiClient.invokeAPI("/job/validateRemote", HttpMethod.POST, Collections.<String, Object>emptyMap(), queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, authNames, returnType);
    }
}
