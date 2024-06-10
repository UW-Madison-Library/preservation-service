# PreservationManagerApi

All URIs are relative to *http://localhost:8484/api*

Method | HTTP request | Description
------------- | ------------- | -------------
[**approveIngestBatch**](PreservationManagerApi.md#approveIngestBatch) | **POST** /batch/approve | Approves a batch for ingestion
[**approveIngestObject**](PreservationManagerApi.md#approveIngestObject) | **POST** /batch/object/approve | Approves an object for ingestion
[**cancelJob**](PreservationManagerApi.md#cancelJob) | **POST** /job/cancel | Cancels a pending job
[**createOrg**](PreservationManagerApi.md#createOrg) | **POST** /org/{orgName} | Creates a new organization
[**createVault**](PreservationManagerApi.md#createVault) | **POST** /vault | Creates a new vault for an organization
[**deleteObject**](PreservationManagerApi.md#deleteObject) | **POST** /object/delete | Marks the object as deleted in the database, but does not actually delete any files
[**describeJob**](PreservationManagerApi.md#describeJob) | **GET** /job/{jobId} | Gets job details
[**describeOrg**](PreservationManagerApi.md#describeOrg) | **GET** /org/{orgName} | Describes the specified organization and returns the current user&#39;s permissions
[**describeOrgContact**](PreservationManagerApi.md#describeOrgContact) | **GET** /org/{orgName}/contact | Returns the primary contact for the organization.
[**describePreservationObject**](PreservationManagerApi.md#describePreservationObject) | **GET** /object | Retrieves details about an object version
[**describeRetrieveRequest**](PreservationManagerApi.md#describeRetrieveRequest) | **GET** /vault/retrieve/{requestId} | Describes a retrieve objects request
[**describeUser**](PreservationManagerApi.md#describeUser) | **GET** /user | Retrieves details about the current user
[**describeVault**](PreservationManagerApi.md#describeVault) | **GET** /vault/{vault}/describe | Returns details about the vault and the current user&#39;s permissions in the vault
[**diffBatchObject**](PreservationManagerApi.md#diffBatchObject) | **GET** /batch/object/diff | Diffs the batch object against the current state of the object in the preservation system
[**disableJobTypes**](PreservationManagerApi.md#disableJobTypes) | **POST** /job/disable | Disables job types from being processed
[**downloadJob**](PreservationManagerApi.md#downloadJob) | **GET** /vault/retrieve/download/{jobId} | Downloads the output of a retrieve job
[**enableJobTypes**](PreservationManagerApi.md#enableJobTypes) | **POST** /job/enable | Enables job types from being processed
[**failJob**](PreservationManagerApi.md#failJob) | **POST** /job/fail | Marks an executing job as failed. This action can only be completed by a service administrator, and should only be used when a job has a state of executing but is not being executed by a worker.
[**getIngestEventCounts**](PreservationManagerApi.md#getIngestEventCounts) | **GET** /system/ingestEventCounts | Counts of all ingest events in the system or organization if specified.
[**getPreservationEventCounts**](PreservationManagerApi.md#getPreservationEventCounts) | **GET** /system/preservationEventCounts | Counts of all preservation events in the system or organization if specified.
[**getSystemStorage**](PreservationManagerApi.md#getSystemStorage) | **GET** /system/storage | Retrieves system wide storage details, including a list of all organizations in the system and their storage details
[**ingestBag**](PreservationManagerApi.md#ingestBag) | **POST** /ingest/bag | Ingests the objects in a BagIt bag
[**listExecutingJobs**](PreservationManagerApi.md#listExecutingJobs) | **GET** /job/executing | Lists jobs that have a state of EXECUTING
[**listJobs**](PreservationManagerApi.md#listJobs) | **GET** /{orgName}/job | Lists jobs
[**listOrgUserVaultPermissions**](PreservationManagerApi.md#listOrgUserVaultPermissions) | **GET** /vault/{orgName}/permission | List all of the vaults in an org along with the user&#39;s permissions
[**listOrgUsers**](PreservationManagerApi.md#listOrgUsers) | **GET** /org/{orgName}/user | Retrieves a list of all of the members of an organization
[**listUserOrgs**](PreservationManagerApi.md#listUserOrgs) | **GET** /user/org | Retrieves a list of all organizations the authenticated user is an active member of
[**listUsersInVault**](PreservationManagerApi.md#listUsersInVault) | **GET** /vault/{vault}/user | Retrieves a list of users with read or write permissions for the given vault
[**listVaultProblems**](PreservationManagerApi.md#listVaultProblems) | **GET** /vault/{vault}/problems | Returns a list of objects within the vault for which storage problems have been identified
[**listVaults**](PreservationManagerApi.md#listVaults) | **GET** /vault/{orgName} | Lists all vaults for an organization
[**rejectIngestBatch**](PreservationManagerApi.md#rejectIngestBatch) | **POST** /batch/reject | Rejects a batch for ingestion
[**rejectIngestObject**](PreservationManagerApi.md#rejectIngestObject) | **POST** /batch/object/reject | Rejects an object for ingestion
[**replicateObject**](PreservationManagerApi.md#replicateObject) | **POST** /job/replicate | Replicates an object from local storage to a remote
[**restoreObject**](PreservationManagerApi.md#restoreObject) | **POST** /job/restore | Restores the local copy of an object from a remote source
[**retrieveBatch**](PreservationManagerApi.md#retrieveBatch) | **GET** /batch/{ingestId} | Retrieves Ingest Batch details
[**retrieveBatchEvents**](PreservationManagerApi.md#retrieveBatchEvents) | **GET** /batch/{ingestId}/event | Retrieves all of the events associated to the batch. This does not include events associated to objects in the batch
[**retrieveBatchObject**](PreservationManagerApi.md#retrieveBatchObject) | **GET** /batch/object | Retrieves details about an Ingest Batch Object
[**retrieveBatchObjectEvents**](PreservationManagerApi.md#retrieveBatchObjectEvents) | **GET** /batch/object/event | Retrieves all of the events associated to the object in the batch
[**retrieveBatchObjectFile**](PreservationManagerApi.md#retrieveBatchObjectFile) | **GET** /batch/object/file | Retrieves a specific file associated to an Ingest Batch Object
[**retrieveBatchObjectFiles**](PreservationManagerApi.md#retrieveBatchObjectFiles) | **GET** /batch/object/files | Retrieves the files associated to an Ingest Batch Object
[**retrieveBatchObjects**](PreservationManagerApi.md#retrieveBatchObjects) | **GET** /batch/{ingestId}/objects | Retrieves Ingest Batch Objects
[**retrieveJobLogs**](PreservationManagerApi.md#retrieveJobLogs) | **GET** /job/{jobId}/logs | Retrieves all of the logs associated with a job
[**retrieveObjectEvents**](PreservationManagerApi.md#retrieveObjectEvents) | **GET** /object/event | Retrieves all of the events associated with an object
[**retrieveObjectStorageProblems**](PreservationManagerApi.md#retrieveObjectStorageProblems) | **GET** /object/problems/storage | Retrieves details about what storage problems are affecting an object
[**retrieveObjects**](PreservationManagerApi.md#retrieveObjects) | **POST** /vault/retrieve | Requests the retrieval of preserved objects
[**retryBatchIngest**](PreservationManagerApi.md#retryBatchIngest) | **POST** /batch/retryIngest | Retries a batch ingest if it has failed
[**retryBatchReplicate**](PreservationManagerApi.md#retryBatchReplicate) | **POST** /batch/retryReplicate | Retries a batch replicate if it has failed
[**retryRetrieveJob**](PreservationManagerApi.md#retryRetrieveJob) | **POST** /vault/retrieve/{requestId}/retry/{jobId} | Retries a failed retrieve job
[**searchBatches**](PreservationManagerApi.md#searchBatches) | **GET** /batch/search | Search for batches
[**updateUserVaultPermissions**](PreservationManagerApi.md#updateUserVaultPermissions) | **POST** /user/vault | Updates a user&#39;s permissions on a vault. Any existing permissions are overwritten. An empty permission list removes all permissions for the vault.
[**validateObject**](PreservationManagerApi.md#validateObject) | **POST** /job/validate | Validates the local copy of an object
[**validateObjectRemote**](PreservationManagerApi.md#validateObjectRemote) | **POST** /job/validateRemote | Validates a remote copy of an object



## approveIngestBatch

> approveIngestBatch(approveIngestBatchRequest)

Approves a batch for ingestion

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.models.*;
import edu.wisc.library.sdg.preservation.manager.client.api.PreservationManagerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/api");
        
        // Configure API key authorization: ApiKeyAuth
        ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ApiKeyAuth.setApiKeyPrefix("Token");

        PreservationManagerApi apiInstance = new PreservationManagerApi(defaultClient);
        ApproveIngestBatchRequest approveIngestBatchRequest = new ApproveIngestBatchRequest(); // ApproveIngestBatchRequest | 
        try {
            apiInstance.approveIngestBatch(approveIngestBatchRequest);
        } catch (ApiException e) {
            System.err.println("Exception when calling PreservationManagerApi#approveIngestBatch");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **approveIngestBatchRequest** | [**ApproveIngestBatchRequest**](ApproveIngestBatchRequest.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Success |  -  |
| **400** | Response returned when an operation fails. |  -  |


## approveIngestObject

> approveIngestObject(approveIngestObjectRequest)

Approves an object for ingestion

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.models.*;
import edu.wisc.library.sdg.preservation.manager.client.api.PreservationManagerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/api");
        
        // Configure API key authorization: ApiKeyAuth
        ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ApiKeyAuth.setApiKeyPrefix("Token");

        PreservationManagerApi apiInstance = new PreservationManagerApi(defaultClient);
        ApproveIngestObjectRequest approveIngestObjectRequest = new ApproveIngestObjectRequest(); // ApproveIngestObjectRequest | 
        try {
            apiInstance.approveIngestObject(approveIngestObjectRequest);
        } catch (ApiException e) {
            System.err.println("Exception when calling PreservationManagerApi#approveIngestObject");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **approveIngestObjectRequest** | [**ApproveIngestObjectRequest**](ApproveIngestObjectRequest.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Success |  -  |
| **400** | Response returned when an operation fails. |  -  |


## cancelJob

> cancelJob(cancelJobRequest)

Cancels a pending job

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.models.*;
import edu.wisc.library.sdg.preservation.manager.client.api.PreservationManagerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/api");
        
        // Configure API key authorization: ApiKeyAuth
        ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ApiKeyAuth.setApiKeyPrefix("Token");

        PreservationManagerApi apiInstance = new PreservationManagerApi(defaultClient);
        CancelJobRequest cancelJobRequest = new CancelJobRequest(); // CancelJobRequest | 
        try {
            apiInstance.cancelJob(cancelJobRequest);
        } catch (ApiException e) {
            System.err.println("Exception when calling PreservationManagerApi#cancelJob");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **cancelJobRequest** | [**CancelJobRequest**](CancelJobRequest.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Success |  -  |
| **400** | Response returned when an operation fails. |  -  |


## createOrg

> createOrg(orgName, createOrgRequest)

Creates a new organization

Service adminstrator access is required to create an organization.

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.models.*;
import edu.wisc.library.sdg.preservation.manager.client.api.PreservationManagerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/api");
        
        // Configure API key authorization: ApiKeyAuth
        ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ApiKeyAuth.setApiKeyPrefix("Token");

        PreservationManagerApi apiInstance = new PreservationManagerApi(defaultClient);
        String orgName = "organization-id"; // String | The ID of the organization
        CreateOrgRequest createOrgRequest = new CreateOrgRequest(); // CreateOrgRequest | 
        try {
            apiInstance.createOrg(orgName, createOrgRequest);
        } catch (ApiException e) {
            System.err.println("Exception when calling PreservationManagerApi#createOrg");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **orgName** | **String**| The ID of the organization |
 **createOrgRequest** | [**CreateOrgRequest**](CreateOrgRequest.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Success |  -  |
| **400** | Response returned when an operation fails. |  -  |


## createVault

> createVault(createVaultRequest)

Creates a new vault for an organization

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.models.*;
import edu.wisc.library.sdg.preservation.manager.client.api.PreservationManagerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/api");
        
        // Configure API key authorization: ApiKeyAuth
        ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ApiKeyAuth.setApiKeyPrefix("Token");

        PreservationManagerApi apiInstance = new PreservationManagerApi(defaultClient);
        CreateVaultRequest createVaultRequest = new CreateVaultRequest(); // CreateVaultRequest | 
        try {
            apiInstance.createVault(createVaultRequest);
        } catch (ApiException e) {
            System.err.println("Exception when calling PreservationManagerApi#createVault");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **createVaultRequest** | [**CreateVaultRequest**](CreateVaultRequest.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Success |  -  |
| **400** | Response returned when an operation fails. |  -  |


## deleteObject

> deleteObject(deleteObjectRequest)

Marks the object as deleted in the database, but does not actually delete any files

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.models.*;
import edu.wisc.library.sdg.preservation.manager.client.api.PreservationManagerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/api");
        
        // Configure API key authorization: ApiKeyAuth
        ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ApiKeyAuth.setApiKeyPrefix("Token");

        PreservationManagerApi apiInstance = new PreservationManagerApi(defaultClient);
        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(); // DeleteObjectRequest | 
        try {
            apiInstance.deleteObject(deleteObjectRequest);
        } catch (ApiException e) {
            System.err.println("Exception when calling PreservationManagerApi#deleteObject");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **deleteObjectRequest** | [**DeleteObjectRequest**](DeleteObjectRequest.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Success |  -  |
| **400** | Response returned when an operation fails. |  -  |


## describeJob

> DescribeJobResponse describeJob(jobId)

Gets job details

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.models.*;
import edu.wisc.library.sdg.preservation.manager.client.api.PreservationManagerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/api");
        
        // Configure API key authorization: ApiKeyAuth
        ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ApiKeyAuth.setApiKeyPrefix("Token");

        PreservationManagerApi apiInstance = new PreservationManagerApi(defaultClient);
        Long jobId = 10293L; // Long | The ID of the job
        try {
            DescribeJobResponse result = apiInstance.describeJob(jobId);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling PreservationManagerApi#describeJob");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **jobId** | **Long**| The ID of the job |

### Return type

[**DescribeJobResponse**](DescribeJobResponse.md)

### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Job details |  -  |
| **400** | Response returned when an operation fails. |  -  |


## describeOrg

> DescribeOrgResponse describeOrg(orgName)

Describes the specified organization and returns the current user&#39;s permissions

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.models.*;
import edu.wisc.library.sdg.preservation.manager.client.api.PreservationManagerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/api");
        
        // Configure API key authorization: ApiKeyAuth
        ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ApiKeyAuth.setApiKeyPrefix("Token");

        PreservationManagerApi apiInstance = new PreservationManagerApi(defaultClient);
        String orgName = "organization-id"; // String | The ID of the organization
        try {
            DescribeOrgResponse result = apiInstance.describeOrg(orgName);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling PreservationManagerApi#describeOrg");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **orgName** | **String**| The ID of the organization |

### Return type

[**DescribeOrgResponse**](DescribeOrgResponse.md)

### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Details about the specified organization and returns the current user&#39;s permissions |  -  |
| **400** | Response returned when an operation fails. |  -  |


## describeOrgContact

> OrganizationContactResponse describeOrgContact(orgName)

Returns the primary contact for the organization.

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.models.*;
import edu.wisc.library.sdg.preservation.manager.client.api.PreservationManagerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/api");
        
        // Configure API key authorization: ApiKeyAuth
        ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ApiKeyAuth.setApiKeyPrefix("Token");

        PreservationManagerApi apiInstance = new PreservationManagerApi(defaultClient);
        String orgName = "organization-id"; // String | The ID of the organization
        try {
            OrganizationContactResponse result = apiInstance.describeOrgContact(orgName);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling PreservationManagerApi#describeOrgContact");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **orgName** | **String**| The ID of the organization |

### Return type

[**OrganizationContactResponse**](OrganizationContactResponse.md)

### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Details about the primary contact for the organization |  -  |
| **400** | Response returned when an operation fails. |  -  |


## describePreservationObject

> DescribePreservationObjectResponse describePreservationObject(vault, externalObjectId, version)

Retrieves details about an object version

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.models.*;
import edu.wisc.library.sdg.preservation.manager.client.api.PreservationManagerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/api");
        
        // Configure API key authorization: ApiKeyAuth
        ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ApiKeyAuth.setApiKeyPrefix("Token");

        PreservationManagerApi apiInstance = new PreservationManagerApi(defaultClient);
        String vault = "vault-name"; // String | The vault an object is in
        String externalObjectId = "1711.dl/ZQ5WPGU2GKBFT8Q"; // String | The external ID of the object
        Integer version = 1; // Integer | The version of an object
        try {
            DescribePreservationObjectResponse result = apiInstance.describePreservationObject(vault, externalObjectId, version);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling PreservationManagerApi#describePreservationObject");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **vault** | **String**| The vault an object is in |
 **externalObjectId** | **String**| The external ID of the object |
 **version** | **Integer**| The version of an object | [optional]

### Return type

[**DescribePreservationObjectResponse**](DescribePreservationObjectResponse.md)

### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Details about a preservation object |  -  |
| **400** | Response returned when an operation fails. |  -  |


## describeRetrieveRequest

> DescribeRetrieveObjectsResponse describeRetrieveRequest(requestId)

Describes a retrieve objects request

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.models.*;
import edu.wisc.library.sdg.preservation.manager.client.api.PreservationManagerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/api");
        
        // Configure API key authorization: ApiKeyAuth
        ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ApiKeyAuth.setApiKeyPrefix("Token");

        PreservationManagerApi apiInstance = new PreservationManagerApi(defaultClient);
        Long requestId = 10293L; // Long | The ID of the request
        try {
            DescribeRetrieveObjectsResponse result = apiInstance.describeRetrieveRequest(requestId);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling PreservationManagerApi#describeRetrieveRequest");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **requestId** | **Long**| The ID of the request |

### Return type

[**DescribeRetrieveObjectsResponse**](DescribeRetrieveObjectsResponse.md)

### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Details about a retrieve objects request |  -  |
| **400** | Response returned when an operation fails. |  -  |


## describeUser

> DescribeUserResponse describeUser()

Retrieves details about the current user

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.models.*;
import edu.wisc.library.sdg.preservation.manager.client.api.PreservationManagerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/api");
        
        // Configure API key authorization: ApiKeyAuth
        ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ApiKeyAuth.setApiKeyPrefix("Token");

        PreservationManagerApi apiInstance = new PreservationManagerApi(defaultClient);
        try {
            DescribeUserResponse result = apiInstance.describeUser();
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling PreservationManagerApi#describeUser");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters

This endpoint does not need any parameter.

### Return type

[**DescribeUserResponse**](DescribeUserResponse.md)

### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | User details |  -  |
| **400** | Response returned when an operation fails. |  -  |


## describeVault

> DescribeVaultResponse describeVault(vault)

Returns details about the vault and the current user&#39;s permissions in the vault

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.models.*;
import edu.wisc.library.sdg.preservation.manager.client.api.PreservationManagerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/api");
        
        // Configure API key authorization: ApiKeyAuth
        ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ApiKeyAuth.setApiKeyPrefix("Token");

        PreservationManagerApi apiInstance = new PreservationManagerApi(defaultClient);
        String vault = "vault-name"; // String | The name of the vault
        try {
            DescribeVaultResponse result = apiInstance.describeVault(vault);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling PreservationManagerApi#describeVault");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **vault** | **String**| The name of the vault |

### Return type

[**DescribeVaultResponse**](DescribeVaultResponse.md)

### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Details about the vault and the current user&#39;s permissions in the vault |  -  |
| **400** | Response returned when an operation fails. |  -  |


## diffBatchObject

> DiffBatchObjectResponse diffBatchObject(ingestId, externalObjectId)

Diffs the batch object against the current state of the object in the preservation system

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.models.*;
import edu.wisc.library.sdg.preservation.manager.client.api.PreservationManagerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/api");
        
        // Configure API key authorization: ApiKeyAuth
        ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ApiKeyAuth.setApiKeyPrefix("Token");

        PreservationManagerApi apiInstance = new PreservationManagerApi(defaultClient);
        Long ingestId = 10293L; // Long | The ID for the Ingest Batch
        String externalObjectId = "1711.dl/ZQ5WPGU2GKBFT8Q"; // String | The external ID of the object
        try {
            DiffBatchObjectResponse result = apiInstance.diffBatchObject(ingestId, externalObjectId);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling PreservationManagerApi#diffBatchObject");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **ingestId** | **Long**| The ID for the Ingest Batch |
 **externalObjectId** | **String**| The external ID of the object |

### Return type

[**DiffBatchObjectResponse**](DiffBatchObjectResponse.md)

### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Batch object diff |  -  |
| **400** | Response returned when an operation fails. |  -  |


## disableJobTypes

> disableJobTypes(disableJobTypesRequest)

Disables job types from being processed

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.models.*;
import edu.wisc.library.sdg.preservation.manager.client.api.PreservationManagerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/api");
        
        // Configure API key authorization: ApiKeyAuth
        ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ApiKeyAuth.setApiKeyPrefix("Token");

        PreservationManagerApi apiInstance = new PreservationManagerApi(defaultClient);
        DisableJobTypesRequest disableJobTypesRequest = new DisableJobTypesRequest(); // DisableJobTypesRequest | 
        try {
            apiInstance.disableJobTypes(disableJobTypesRequest);
        } catch (ApiException e) {
            System.err.println("Exception when calling PreservationManagerApi#disableJobTypes");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **disableJobTypesRequest** | [**DisableJobTypesRequest**](DisableJobTypesRequest.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Success |  -  |
| **400** | Response returned when an operation fails. |  -  |


## downloadJob

> File downloadJob(jobId)

Downloads the output of a retrieve job

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.models.*;
import edu.wisc.library.sdg.preservation.manager.client.api.PreservationManagerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/api");
        
        // Configure API key authorization: ApiKeyAuth
        ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ApiKeyAuth.setApiKeyPrefix("Token");

        PreservationManagerApi apiInstance = new PreservationManagerApi(defaultClient);
        Long jobId = 10293L; // Long | The ID of the job
        try {
            File result = apiInstance.downloadJob(jobId);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling PreservationManagerApi#downloadJob");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **jobId** | **Long**| The ID of the job |

### Return type

[**File**](File.md)

### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/zip


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |


## enableJobTypes

> enableJobTypes(enableJobTypesRequest)

Enables job types from being processed

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.models.*;
import edu.wisc.library.sdg.preservation.manager.client.api.PreservationManagerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/api");
        
        // Configure API key authorization: ApiKeyAuth
        ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ApiKeyAuth.setApiKeyPrefix("Token");

        PreservationManagerApi apiInstance = new PreservationManagerApi(defaultClient);
        EnableJobTypesRequest enableJobTypesRequest = new EnableJobTypesRequest(); // EnableJobTypesRequest | 
        try {
            apiInstance.enableJobTypes(enableJobTypesRequest);
        } catch (ApiException e) {
            System.err.println("Exception when calling PreservationManagerApi#enableJobTypes");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **enableJobTypesRequest** | [**EnableJobTypesRequest**](EnableJobTypesRequest.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Success |  -  |
| **400** | Response returned when an operation fails. |  -  |


## failJob

> failJob(cancelJobRequest)

Marks an executing job as failed. This action can only be completed by a service administrator, and should only be used when a job has a state of executing but is not being executed by a worker.

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.models.*;
import edu.wisc.library.sdg.preservation.manager.client.api.PreservationManagerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/api");
        
        // Configure API key authorization: ApiKeyAuth
        ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ApiKeyAuth.setApiKeyPrefix("Token");

        PreservationManagerApi apiInstance = new PreservationManagerApi(defaultClient);
        CancelJobRequest cancelJobRequest = new CancelJobRequest(); // CancelJobRequest | 
        try {
            apiInstance.failJob(cancelJobRequest);
        } catch (ApiException e) {
            System.err.println("Exception when calling PreservationManagerApi#failJob");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **cancelJobRequest** | [**CancelJobRequest**](CancelJobRequest.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Success |  -  |
| **400** | Response returned when an operation fails. |  -  |


## getIngestEventCounts

> SystemEventCountsResponse getIngestEventCounts(startDate, endDate, eventType, eventOutcome, organizationName)

Counts of all ingest events in the system or organization if specified.

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.models.*;
import edu.wisc.library.sdg.preservation.manager.client.api.PreservationManagerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/api");
        
        // Configure API key authorization: ApiKeyAuth
        ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ApiKeyAuth.setApiKeyPrefix("Token");

        PreservationManagerApi apiInstance = new PreservationManagerApi(defaultClient);
        LocalDate startDate = LocalDate.parse("2017-07-21"); // LocalDate | The starting date for the event counts.
        LocalDate endDate = LocalDate.parse("2017-07-21"); // LocalDate | The ending date for the event counts.
        List<EventType> eventType = Arrays.asList(); // List<EventType> | Array of event types.
        List<EventOutcome> eventOutcome = Arrays.asList(); // List<EventOutcome> | Array of event outcomes.
        String organizationName = "organization-id"; // String | The ID of the organization.
        try {
            SystemEventCountsResponse result = apiInstance.getIngestEventCounts(startDate, endDate, eventType, eventOutcome, organizationName);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling PreservationManagerApi#getIngestEventCounts");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **startDate** | **LocalDate**| The starting date for the event counts. |
 **endDate** | **LocalDate**| The ending date for the event counts. |
 **eventType** | [**List&lt;EventType&gt;**](EventType.md)| Array of event types. | [optional]
 **eventOutcome** | [**List&lt;EventOutcome&gt;**](EventOutcome.md)| Array of event outcomes. | [optional]
 **organizationName** | **String**| The ID of the organization. | [optional]

### Return type

[**SystemEventCountsResponse**](SystemEventCountsResponse.md)

### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | System wide event counts, grouped by event outcome. |  -  |
| **400** | Response returned when an operation fails. |  -  |


## getPreservationEventCounts

> SystemEventCountsResponse getPreservationEventCounts(startDate, endDate, eventType, eventOutcome, organizationName)

Counts of all preservation events in the system or organization if specified.

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.models.*;
import edu.wisc.library.sdg.preservation.manager.client.api.PreservationManagerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/api");
        
        // Configure API key authorization: ApiKeyAuth
        ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ApiKeyAuth.setApiKeyPrefix("Token");

        PreservationManagerApi apiInstance = new PreservationManagerApi(defaultClient);
        LocalDate startDate = LocalDate.parse("2017-07-21"); // LocalDate | The starting date for the event counts.
        LocalDate endDate = LocalDate.parse("2017-07-21"); // LocalDate | The ending date for the event counts.
        List<EventType> eventType = Arrays.asList(); // List<EventType> | Array of event types.
        List<EventOutcome> eventOutcome = Arrays.asList(); // List<EventOutcome> | Array of event outcomes.
        String organizationName = "organization-id"; // String | The ID of the organization.
        try {
            SystemEventCountsResponse result = apiInstance.getPreservationEventCounts(startDate, endDate, eventType, eventOutcome, organizationName);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling PreservationManagerApi#getPreservationEventCounts");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **startDate** | **LocalDate**| The starting date for the event counts. |
 **endDate** | **LocalDate**| The ending date for the event counts. |
 **eventType** | [**List&lt;EventType&gt;**](EventType.md)| Array of event types. | [optional]
 **eventOutcome** | [**List&lt;EventOutcome&gt;**](EventOutcome.md)| Array of event outcomes. | [optional]
 **organizationName** | **String**| The ID of the organization. | [optional]

### Return type

[**SystemEventCountsResponse**](SystemEventCountsResponse.md)

### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | System wide event counts, grouped by event outcome. |  -  |
| **400** | Response returned when an operation fails. |  -  |


## getSystemStorage

> SystemStorageResponse getSystemStorage()

Retrieves system wide storage details, including a list of all organizations in the system and their storage details

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.models.*;
import edu.wisc.library.sdg.preservation.manager.client.api.PreservationManagerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/api");
        
        // Configure API key authorization: ApiKeyAuth
        ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ApiKeyAuth.setApiKeyPrefix("Token");

        PreservationManagerApi apiInstance = new PreservationManagerApi(defaultClient);
        try {
            SystemStorageResponse result = apiInstance.getSystemStorage();
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling PreservationManagerApi#getSystemStorage");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters

This endpoint does not need any parameter.

### Return type

[**SystemStorageResponse**](SystemStorageResponse.md)

### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Retrieves system wide storage details, including a list of all organizations in the system and their storage details |  -  |
| **400** | Response returned when an operation fails. |  -  |


## ingestBag

> IngestBagResponse ingestBag(vault, _file)

Ingests the objects in a BagIt bag

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.models.*;
import edu.wisc.library.sdg.preservation.manager.client.api.PreservationManagerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/api");
        
        // Configure API key authorization: ApiKeyAuth
        ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ApiKeyAuth.setApiKeyPrefix("Token");

        PreservationManagerApi apiInstance = new PreservationManagerApi(defaultClient);
        String vault = "vault_example"; // String | The name of the vault
        File _file = new File("/path/to/file"); // File | BagIt bag containing the objects to ingest. Must be a zip file.
        try {
            IngestBagResponse result = apiInstance.ingestBag(vault, _file);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling PreservationManagerApi#ingestBag");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **vault** | **String**| The name of the vault |
 **_file** | **File**| BagIt bag containing the objects to ingest. Must be a zip file. |

### Return type

[**IngestBagResponse**](IngestBagResponse.md)

### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth)

### HTTP request headers

- **Content-Type**: multipart/form-data
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Ingest Batch was successfully received, but not processed yet. |  -  |
| **400** | Response returned when an operation fails. |  -  |


## listExecutingJobs

> ListJobsResponse listExecutingJobs()

Lists jobs that have a state of EXECUTING

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.models.*;
import edu.wisc.library.sdg.preservation.manager.client.api.PreservationManagerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/api");
        
        // Configure API key authorization: ApiKeyAuth
        ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ApiKeyAuth.setApiKeyPrefix("Token");

        PreservationManagerApi apiInstance = new PreservationManagerApi(defaultClient);
        try {
            ListJobsResponse result = apiInstance.listExecutingJobs();
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling PreservationManagerApi#listExecutingJobs");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters

This endpoint does not need any parameter.

### Return type

[**ListJobsResponse**](ListJobsResponse.md)

### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | List of jobs |  -  |
| **400** | Response returned when an operation fails. |  -  |


## listJobs

> ListJobsResponse listJobs(orgName)

Lists jobs

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.models.*;
import edu.wisc.library.sdg.preservation.manager.client.api.PreservationManagerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/api");
        
        // Configure API key authorization: ApiKeyAuth
        ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ApiKeyAuth.setApiKeyPrefix("Token");

        PreservationManagerApi apiInstance = new PreservationManagerApi(defaultClient);
        String orgName = "organization-id"; // String | The ID of the organization
        try {
            ListJobsResponse result = apiInstance.listJobs(orgName);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling PreservationManagerApi#listJobs");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **orgName** | **String**| The ID of the organization |

### Return type

[**ListJobsResponse**](ListJobsResponse.md)

### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | List of jobs |  -  |
| **400** | Response returned when an operation fails. |  -  |


## listOrgUserVaultPermissions

> ListVaultPermissionsResponse listOrgUserVaultPermissions(orgName, username)

List all of the vaults in an org along with the user&#39;s permissions

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.models.*;
import edu.wisc.library.sdg.preservation.manager.client.api.PreservationManagerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/api");
        
        // Configure API key authorization: ApiKeyAuth
        ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ApiKeyAuth.setApiKeyPrefix("Token");

        PreservationManagerApi apiInstance = new PreservationManagerApi(defaultClient);
        String orgName = "organization-id"; // String | The ID of the organization
        String username = "username_example"; // String | A user's username in the preservation system
        try {
            ListVaultPermissionsResponse result = apiInstance.listOrgUserVaultPermissions(orgName, username);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling PreservationManagerApi#listOrgUserVaultPermissions");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **orgName** | **String**| The ID of the organization |
 **username** | **String**| A user&#39;s username in the preservation system | [optional]

### Return type

[**ListVaultPermissionsResponse**](ListVaultPermissionsResponse.md)

### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | List of all vaults in an org along with the user&#39;s permissions |  -  |
| **400** | Response returned when an operation fails. |  -  |


## listOrgUsers

> ListOrgUsersResponse listOrgUsers(orgName)

Retrieves a list of all of the members of an organization

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.models.*;
import edu.wisc.library.sdg.preservation.manager.client.api.PreservationManagerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/api");
        
        // Configure API key authorization: ApiKeyAuth
        ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ApiKeyAuth.setApiKeyPrefix("Token");

        PreservationManagerApi apiInstance = new PreservationManagerApi(defaultClient);
        String orgName = "organization-id"; // String | The ID of the organization
        try {
            ListOrgUsersResponse result = apiInstance.listOrgUsers(orgName);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling PreservationManagerApi#listOrgUsers");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **orgName** | **String**| The ID of the organization |

### Return type

[**ListOrgUsersResponse**](ListOrgUsersResponse.md)

### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | List of users in an organization |  -  |
| **400** | Response returned when an operation fails. |  -  |


## listUserOrgs

> ListUserOrgsResponse listUserOrgs()

Retrieves a list of all organizations the authenticated user is an active member of

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.models.*;
import edu.wisc.library.sdg.preservation.manager.client.api.PreservationManagerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/api");
        
        // Configure API key authorization: ApiKeyAuth
        ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ApiKeyAuth.setApiKeyPrefix("Token");

        PreservationManagerApi apiInstance = new PreservationManagerApi(defaultClient);
        try {
            ListUserOrgsResponse result = apiInstance.listUserOrgs();
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling PreservationManagerApi#listUserOrgs");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters

This endpoint does not need any parameter.

### Return type

[**ListUserOrgsResponse**](ListUserOrgsResponse.md)

### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | List of organizations |  -  |
| **400** | Response returned when an operation fails. |  -  |


## listUsersInVault

> ListVaultUsersResponse listUsersInVault(vault)

Retrieves a list of users with read or write permissions for the given vault

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.models.*;
import edu.wisc.library.sdg.preservation.manager.client.api.PreservationManagerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/api");
        
        // Configure API key authorization: ApiKeyAuth
        ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ApiKeyAuth.setApiKeyPrefix("Token");

        PreservationManagerApi apiInstance = new PreservationManagerApi(defaultClient);
        String vault = "vault-name"; // String | The name of the vault
        try {
            ListVaultUsersResponse result = apiInstance.listUsersInVault(vault);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling PreservationManagerApi#listUsersInVault");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **vault** | **String**| The name of the vault |

### Return type

[**ListVaultUsersResponse**](ListVaultUsersResponse.md)

### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | List of all of the users who have access to the given vault along with their permissions |  -  |
| **400** | Response returned when an operation fails. |  -  |


## listVaultProblems

> ListVaultProblemsResponse listVaultProblems(vault)

Returns a list of objects within the vault for which storage problems have been identified

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.models.*;
import edu.wisc.library.sdg.preservation.manager.client.api.PreservationManagerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/api");
        
        // Configure API key authorization: ApiKeyAuth
        ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ApiKeyAuth.setApiKeyPrefix("Token");

        PreservationManagerApi apiInstance = new PreservationManagerApi(defaultClient);
        String vault = "vault-name"; // String | The name of the vault
        try {
            ListVaultProblemsResponse result = apiInstance.listVaultProblems(vault);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling PreservationManagerApi#listVaultProblems");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **vault** | **String**| The name of the vault |

### Return type

[**ListVaultProblemsResponse**](ListVaultProblemsResponse.md)

### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | List of objects within the vault for which storage problems have been identified |  -  |
| **400** | Response returned when an operation fails. |  -  |


## listVaults

> ListVaultsResponse listVaults(orgName)

Lists all vaults for an organization

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.models.*;
import edu.wisc.library.sdg.preservation.manager.client.api.PreservationManagerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/api");
        
        // Configure API key authorization: ApiKeyAuth
        ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ApiKeyAuth.setApiKeyPrefix("Token");

        PreservationManagerApi apiInstance = new PreservationManagerApi(defaultClient);
        String orgName = "organization-id"; // String | The ID of the organization
        try {
            ListVaultsResponse result = apiInstance.listVaults(orgName);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling PreservationManagerApi#listVaults");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **orgName** | **String**| The ID of the organization |

### Return type

[**ListVaultsResponse**](ListVaultsResponse.md)

### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | List of vaults |  -  |
| **400** | Response returned when an operation fails. |  -  |


## rejectIngestBatch

> rejectIngestBatch(rejectIngestBatchRequest)

Rejects a batch for ingestion

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.models.*;
import edu.wisc.library.sdg.preservation.manager.client.api.PreservationManagerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/api");
        
        // Configure API key authorization: ApiKeyAuth
        ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ApiKeyAuth.setApiKeyPrefix("Token");

        PreservationManagerApi apiInstance = new PreservationManagerApi(defaultClient);
        RejectIngestBatchRequest rejectIngestBatchRequest = new RejectIngestBatchRequest(); // RejectIngestBatchRequest | 
        try {
            apiInstance.rejectIngestBatch(rejectIngestBatchRequest);
        } catch (ApiException e) {
            System.err.println("Exception when calling PreservationManagerApi#rejectIngestBatch");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **rejectIngestBatchRequest** | [**RejectIngestBatchRequest**](RejectIngestBatchRequest.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Success |  -  |
| **400** | Response returned when an operation fails. |  -  |


## rejectIngestObject

> rejectIngestObject(rejectIngestObjectRequest)

Rejects an object for ingestion

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.models.*;
import edu.wisc.library.sdg.preservation.manager.client.api.PreservationManagerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/api");
        
        // Configure API key authorization: ApiKeyAuth
        ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ApiKeyAuth.setApiKeyPrefix("Token");

        PreservationManagerApi apiInstance = new PreservationManagerApi(defaultClient);
        RejectIngestObjectRequest rejectIngestObjectRequest = new RejectIngestObjectRequest(); // RejectIngestObjectRequest | 
        try {
            apiInstance.rejectIngestObject(rejectIngestObjectRequest);
        } catch (ApiException e) {
            System.err.println("Exception when calling PreservationManagerApi#rejectIngestObject");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **rejectIngestObjectRequest** | [**RejectIngestObjectRequest**](RejectIngestObjectRequest.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Success |  -  |
| **400** | Response returned when an operation fails. |  -  |


## replicateObject

> ReplicateObjectResponse replicateObject(replicateObjectRequest)

Replicates an object from local storage to a remote

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.models.*;
import edu.wisc.library.sdg.preservation.manager.client.api.PreservationManagerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/api");
        
        // Configure API key authorization: ApiKeyAuth
        ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ApiKeyAuth.setApiKeyPrefix("Token");

        PreservationManagerApi apiInstance = new PreservationManagerApi(defaultClient);
        ReplicateObjectRequest replicateObjectRequest = new ReplicateObjectRequest(); // ReplicateObjectRequest | 
        try {
            ReplicateObjectResponse result = apiInstance.replicateObject(replicateObjectRequest);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling PreservationManagerApi#replicateObject");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **replicateObjectRequest** | [**ReplicateObjectRequest**](ReplicateObjectRequest.md)|  | [optional]

### Return type

[**ReplicateObjectResponse**](ReplicateObjectResponse.md)

### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Replicate object response |  -  |
| **400** | Response returned when an operation fails. |  -  |


## restoreObject

> RestorePreservationObjectResponse restoreObject(restorePreservationObjectRequest)

Restores the local copy of an object from a remote source

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.models.*;
import edu.wisc.library.sdg.preservation.manager.client.api.PreservationManagerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/api");
        
        // Configure API key authorization: ApiKeyAuth
        ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ApiKeyAuth.setApiKeyPrefix("Token");

        PreservationManagerApi apiInstance = new PreservationManagerApi(defaultClient);
        RestorePreservationObjectRequest restorePreservationObjectRequest = new RestorePreservationObjectRequest(); // RestorePreservationObjectRequest | 
        try {
            RestorePreservationObjectResponse result = apiInstance.restoreObject(restorePreservationObjectRequest);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling PreservationManagerApi#restoreObject");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **restorePreservationObjectRequest** | [**RestorePreservationObjectRequest**](RestorePreservationObjectRequest.md)|  | [optional]

### Return type

[**RestorePreservationObjectResponse**](RestorePreservationObjectResponse.md)

### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Restore object response |  -  |
| **400** | Response returned when an operation fails. |  -  |


## retrieveBatch

> RetrieveBatchResponse retrieveBatch(ingestId)

Retrieves Ingest Batch details

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.models.*;
import edu.wisc.library.sdg.preservation.manager.client.api.PreservationManagerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/api");
        
        // Configure API key authorization: ApiKeyAuth
        ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ApiKeyAuth.setApiKeyPrefix("Token");

        PreservationManagerApi apiInstance = new PreservationManagerApi(defaultClient);
        Long ingestId = 10293L; // Long | The ID for the Ingest Batch
        try {
            RetrieveBatchResponse result = apiInstance.retrieveBatch(ingestId);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling PreservationManagerApi#retrieveBatch");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **ingestId** | **Long**| The ID for the Ingest Batch |

### Return type

[**RetrieveBatchResponse**](RetrieveBatchResponse.md)

### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Ingest Batch details |  -  |
| **400** | Response returned when an operation fails. |  -  |


## retrieveBatchEvents

> RetrieveEventsResponse retrieveBatchEvents(ingestId)

Retrieves all of the events associated to the batch. This does not include events associated to objects in the batch

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.models.*;
import edu.wisc.library.sdg.preservation.manager.client.api.PreservationManagerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/api");
        
        // Configure API key authorization: ApiKeyAuth
        ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ApiKeyAuth.setApiKeyPrefix("Token");

        PreservationManagerApi apiInstance = new PreservationManagerApi(defaultClient);
        Long ingestId = 10293L; // Long | The ID for the Ingest Batch
        try {
            RetrieveEventsResponse result = apiInstance.retrieveBatchEvents(ingestId);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling PreservationManagerApi#retrieveBatchEvents");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **ingestId** | **Long**| The ID for the Ingest Batch |

### Return type

[**RetrieveEventsResponse**](RetrieveEventsResponse.md)

### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Batch events |  -  |
| **400** | Response returned when an operation fails. |  -  |


## retrieveBatchObject

> RetrieveBatchObjectResponse retrieveBatchObject(ingestId, externalObjectId)

Retrieves details about an Ingest Batch Object

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.models.*;
import edu.wisc.library.sdg.preservation.manager.client.api.PreservationManagerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/api");
        
        // Configure API key authorization: ApiKeyAuth
        ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ApiKeyAuth.setApiKeyPrefix("Token");

        PreservationManagerApi apiInstance = new PreservationManagerApi(defaultClient);
        Long ingestId = 10293L; // Long | The ID for the Ingest Batch
        String externalObjectId = "1711.dl/ZQ5WPGU2GKBFT8Q"; // String | The external ID of the object
        try {
            RetrieveBatchObjectResponse result = apiInstance.retrieveBatchObject(ingestId, externalObjectId);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling PreservationManagerApi#retrieveBatchObject");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **ingestId** | **Long**| The ID for the Ingest Batch |
 **externalObjectId** | **String**| The external ID of the object |

### Return type

[**RetrieveBatchObjectResponse**](RetrieveBatchObjectResponse.md)

### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Ingest Batch Object details |  -  |
| **400** | Response returned when an operation fails. |  -  |


## retrieveBatchObjectEvents

> RetrieveEventsResponse retrieveBatchObjectEvents(ingestId, externalObjectId)

Retrieves all of the events associated to the object in the batch

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.models.*;
import edu.wisc.library.sdg.preservation.manager.client.api.PreservationManagerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/api");
        
        // Configure API key authorization: ApiKeyAuth
        ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ApiKeyAuth.setApiKeyPrefix("Token");

        PreservationManagerApi apiInstance = new PreservationManagerApi(defaultClient);
        Long ingestId = 10293L; // Long | The ID for the Ingest Batch
        String externalObjectId = "1711.dl/ZQ5WPGU2GKBFT8Q"; // String | The external ID of the object
        try {
            RetrieveEventsResponse result = apiInstance.retrieveBatchObjectEvents(ingestId, externalObjectId);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling PreservationManagerApi#retrieveBatchObjectEvents");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **ingestId** | **Long**| The ID for the Ingest Batch |
 **externalObjectId** | **String**| The external ID of the object |

### Return type

[**RetrieveEventsResponse**](RetrieveEventsResponse.md)

### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Object events |  -  |
| **400** | Response returned when an operation fails. |  -  |


## retrieveBatchObjectFile

> RetrieveBatchObjectFileResponse retrieveBatchObjectFile(ingestId, externalObjectId, filePath)

Retrieves a specific file associated to an Ingest Batch Object

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.models.*;
import edu.wisc.library.sdg.preservation.manager.client.api.PreservationManagerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/api");
        
        // Configure API key authorization: ApiKeyAuth
        ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ApiKeyAuth.setApiKeyPrefix("Token");

        PreservationManagerApi apiInstance = new PreservationManagerApi(defaultClient);
        Long ingestId = 10293L; // Long | The ID for the Ingest Batch
        String externalObjectId = "1711.dl/ZQ5WPGU2GKBFT8Q"; // String | The external ID of the object
        String filePath = "path/to/file.txt"; // String | The object relative path of the file
        try {
            RetrieveBatchObjectFileResponse result = apiInstance.retrieveBatchObjectFile(ingestId, externalObjectId, filePath);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling PreservationManagerApi#retrieveBatchObjectFile");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **ingestId** | **Long**| The ID for the Ingest Batch |
 **externalObjectId** | **String**| The external ID of the object |
 **filePath** | **String**| The object relative path of the file |

### Return type

[**RetrieveBatchObjectFileResponse**](RetrieveBatchObjectFileResponse.md)

### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Ingest Batch Object file |  -  |
| **400** | Response returned when an operation fails. |  -  |


## retrieveBatchObjectFiles

> RetrieveBatchObjectFilesResponse retrieveBatchObjectFiles(ingestId, externalObjectId)

Retrieves the files associated to an Ingest Batch Object

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.models.*;
import edu.wisc.library.sdg.preservation.manager.client.api.PreservationManagerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/api");
        
        // Configure API key authorization: ApiKeyAuth
        ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ApiKeyAuth.setApiKeyPrefix("Token");

        PreservationManagerApi apiInstance = new PreservationManagerApi(defaultClient);
        Long ingestId = 10293L; // Long | The ID for the Ingest Batch
        String externalObjectId = "1711.dl/ZQ5WPGU2GKBFT8Q"; // String | The external ID of the object
        try {
            RetrieveBatchObjectFilesResponse result = apiInstance.retrieveBatchObjectFiles(ingestId, externalObjectId);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling PreservationManagerApi#retrieveBatchObjectFiles");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **ingestId** | **Long**| The ID for the Ingest Batch |
 **externalObjectId** | **String**| The external ID of the object |

### Return type

[**RetrieveBatchObjectFilesResponse**](RetrieveBatchObjectFilesResponse.md)

### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Ingest Batch Object files |  -  |
| **400** | Response returned when an operation fails. |  -  |


## retrieveBatchObjects

> RetrieveBatchObjectsResponse retrieveBatchObjects(ingestId, pageSize, page, hasProblems)

Retrieves Ingest Batch Objects

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.models.*;
import edu.wisc.library.sdg.preservation.manager.client.api.PreservationManagerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/api");
        
        // Configure API key authorization: ApiKeyAuth
        ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ApiKeyAuth.setApiKeyPrefix("Token");

        PreservationManagerApi apiInstance = new PreservationManagerApi(defaultClient);
        Long ingestId = 10293L; // Long | The ID for the Ingest Batch
        Integer pageSize = 20; // Integer | The number of results to include per page (default 20)
        Integer page = 0; // Integer | The result page to retrieve, 0 indexed
        String hasProblems = "NONE"; // String | Filter objects by the existence of errors or warnings. WARNINGS returns objects that only have warnings; ERRORS returns any object that has errors.
        try {
            RetrieveBatchObjectsResponse result = apiInstance.retrieveBatchObjects(ingestId, pageSize, page, hasProblems);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling PreservationManagerApi#retrieveBatchObjects");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **ingestId** | **Long**| The ID for the Ingest Batch |
 **pageSize** | **Integer**| The number of results to include per page (default 20) | [optional]
 **page** | **Integer**| The result page to retrieve, 0 indexed | [optional]
 **hasProblems** | **String**| Filter objects by the existence of errors or warnings. WARNINGS returns objects that only have warnings; ERRORS returns any object that has errors. | [optional] [enum: NONE, WARNINGS, ERRORS]

### Return type

[**RetrieveBatchObjectsResponse**](RetrieveBatchObjectsResponse.md)

### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Ingest Batch Object details |  -  |
| **400** | Response returned when an operation fails. |  -  |


## retrieveJobLogs

> RetrieveLogsResponse retrieveJobLogs(jobId)

Retrieves all of the logs associated with a job

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.models.*;
import edu.wisc.library.sdg.preservation.manager.client.api.PreservationManagerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/api");
        
        // Configure API key authorization: ApiKeyAuth
        ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ApiKeyAuth.setApiKeyPrefix("Token");

        PreservationManagerApi apiInstance = new PreservationManagerApi(defaultClient);
        Long jobId = 10293L; // Long | The ID of the job
        try {
            RetrieveLogsResponse result = apiInstance.retrieveJobLogs(jobId);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling PreservationManagerApi#retrieveJobLogs");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **jobId** | **Long**| The ID of the job |

### Return type

[**RetrieveLogsResponse**](RetrieveLogsResponse.md)

### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Ingest logs |  -  |
| **400** | Response returned when an operation fails. |  -  |


## retrieveObjectEvents

> RetrieveEventsResponse retrieveObjectEvents(vault, externalObjectId)

Retrieves all of the events associated with an object

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.models.*;
import edu.wisc.library.sdg.preservation.manager.client.api.PreservationManagerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/api");
        
        // Configure API key authorization: ApiKeyAuth
        ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ApiKeyAuth.setApiKeyPrefix("Token");

        PreservationManagerApi apiInstance = new PreservationManagerApi(defaultClient);
        String vault = "vault-name"; // String | The vault an object is in
        String externalObjectId = "1711.dl/ZQ5WPGU2GKBFT8Q"; // String | The external ID of the object
        try {
            RetrieveEventsResponse result = apiInstance.retrieveObjectEvents(vault, externalObjectId);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling PreservationManagerApi#retrieveObjectEvents");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **vault** | **String**| The vault an object is in |
 **externalObjectId** | **String**| The external ID of the object |

### Return type

[**RetrieveEventsResponse**](RetrieveEventsResponse.md)

### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Object event logs |  -  |
| **400** | Response returned when an operation fails. |  -  |


## retrieveObjectStorageProblems

> RetrieveObjectStorageProblemsResponse retrieveObjectStorageProblems(vault, externalObjectId)

Retrieves details about what storage problems are affecting an object

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.models.*;
import edu.wisc.library.sdg.preservation.manager.client.api.PreservationManagerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/api");
        
        // Configure API key authorization: ApiKeyAuth
        ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ApiKeyAuth.setApiKeyPrefix("Token");

        PreservationManagerApi apiInstance = new PreservationManagerApi(defaultClient);
        String vault = "vault-name"; // String | The vault an object is in
        String externalObjectId = "1711.dl/ZQ5WPGU2GKBFT8Q"; // String | The external ID of the object
        try {
            RetrieveObjectStorageProblemsResponse result = apiInstance.retrieveObjectStorageProblems(vault, externalObjectId);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling PreservationManagerApi#retrieveObjectStorageProblems");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **vault** | **String**| The vault an object is in |
 **externalObjectId** | **String**| The external ID of the object |

### Return type

[**RetrieveObjectStorageProblemsResponse**](RetrieveObjectStorageProblemsResponse.md)

### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The object&#39;s storage problems, if any |  -  |
| **400** | Response returned when an operation fails. |  -  |


## retrieveObjects

> RetrieveObjectsResponse retrieveObjects(retrieveObjectsRequest)

Requests the retrieval of preserved objects

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.models.*;
import edu.wisc.library.sdg.preservation.manager.client.api.PreservationManagerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/api");
        
        // Configure API key authorization: ApiKeyAuth
        ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ApiKeyAuth.setApiKeyPrefix("Token");

        PreservationManagerApi apiInstance = new PreservationManagerApi(defaultClient);
        RetrieveObjectsRequest retrieveObjectsRequest = new RetrieveObjectsRequest(); // RetrieveObjectsRequest | 
        try {
            RetrieveObjectsResponse result = apiInstance.retrieveObjects(retrieveObjectsRequest);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling PreservationManagerApi#retrieveObjects");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **retrieveObjectsRequest** | [**RetrieveObjectsRequest**](RetrieveObjectsRequest.md)|  | [optional]

### Return type

[**RetrieveObjectsResponse**](RetrieveObjectsResponse.md)

### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The result of the retrieve request operation |  -  |
| **400** | Response returned when an operation fails. |  -  |


## retryBatchIngest

> retryBatchIngest(batchRetryIngestRequest)

Retries a batch ingest if it has failed

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.models.*;
import edu.wisc.library.sdg.preservation.manager.client.api.PreservationManagerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/api");
        
        // Configure API key authorization: ApiKeyAuth
        ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ApiKeyAuth.setApiKeyPrefix("Token");

        PreservationManagerApi apiInstance = new PreservationManagerApi(defaultClient);
        BatchRetryIngestRequest batchRetryIngestRequest = new BatchRetryIngestRequest(); // BatchRetryIngestRequest | 
        try {
            apiInstance.retryBatchIngest(batchRetryIngestRequest);
        } catch (ApiException e) {
            System.err.println("Exception when calling PreservationManagerApi#retryBatchIngest");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **batchRetryIngestRequest** | [**BatchRetryIngestRequest**](BatchRetryIngestRequest.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Success |  -  |
| **400** | Response returned when an operation fails. |  -  |


## retryBatchReplicate

> retryBatchReplicate(batchRetryReplicateRequest)

Retries a batch replicate if it has failed

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.models.*;
import edu.wisc.library.sdg.preservation.manager.client.api.PreservationManagerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/api");
        
        // Configure API key authorization: ApiKeyAuth
        ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ApiKeyAuth.setApiKeyPrefix("Token");

        PreservationManagerApi apiInstance = new PreservationManagerApi(defaultClient);
        BatchRetryReplicateRequest batchRetryReplicateRequest = new BatchRetryReplicateRequest(); // BatchRetryReplicateRequest | 
        try {
            apiInstance.retryBatchReplicate(batchRetryReplicateRequest);
        } catch (ApiException e) {
            System.err.println("Exception when calling PreservationManagerApi#retryBatchReplicate");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **batchRetryReplicateRequest** | [**BatchRetryReplicateRequest**](BatchRetryReplicateRequest.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Success |  -  |
| **400** | Response returned when an operation fails. |  -  |


## retryRetrieveJob

> retryRetrieveJob(requestId, jobId)

Retries a failed retrieve job

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.models.*;
import edu.wisc.library.sdg.preservation.manager.client.api.PreservationManagerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/api");
        
        // Configure API key authorization: ApiKeyAuth
        ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ApiKeyAuth.setApiKeyPrefix("Token");

        PreservationManagerApi apiInstance = new PreservationManagerApi(defaultClient);
        Long requestId = 10293L; // Long | The ID of the request
        Long jobId = 10293L; // Long | The ID of the job
        try {
            apiInstance.retryRetrieveJob(requestId, jobId);
        } catch (ApiException e) {
            System.err.println("Exception when calling PreservationManagerApi#retryRetrieveJob");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **requestId** | **Long**| The ID of the request |
 **jobId** | **Long**| The ID of the job |

### Return type

null (empty response body)

### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Success |  -  |
| **400** | Response returned when an operation fails. |  -  |


## searchBatches

> BatchSearchResponse searchBatches(orgName, vault, state, pageSize, page)

Search for batches

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.models.*;
import edu.wisc.library.sdg.preservation.manager.client.api.PreservationManagerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/api");
        
        // Configure API key authorization: ApiKeyAuth
        ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ApiKeyAuth.setApiKeyPrefix("Token");

        PreservationManagerApi apiInstance = new PreservationManagerApi(defaultClient);
        String orgName = "organization-id"; // String | The ID of the organization
        List<String> vault = Arrays.asList(); // List<String> | The vault an object is in
        List<String> state = Arrays.asList(); // List<String> | The state the batch is in
        Integer pageSize = 20; // Integer | The number of results to include per page (default 20)
        Integer page = 0; // Integer | The result page to retrieve, 0 indexed
        try {
            BatchSearchResponse result = apiInstance.searchBatches(orgName, vault, state, pageSize, page);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling PreservationManagerApi#searchBatches");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **orgName** | **String**| The ID of the organization | [optional]
 **vault** | [**List&lt;String&gt;**](String.md)| The vault an object is in | [optional]
 **state** | [**List&lt;String&gt;**](String.md)| The state the batch is in | [optional] [enum: RECEIVED, ANALYZING, ANALYSIS_FAILED, PENDING_REVIEW, PENDING_INGESTION, PENDING_REJECTION, INGESTING, INGEST_FAILED, REPLICATING, REPLICATION_FAILED, COMPLETE, REJECTING, REJECTED, DELETED]
 **pageSize** | **Integer**| The number of results to include per page (default 20) | [optional]
 **page** | **Integer**| The result page to retrieve, 0 indexed | [optional]

### Return type

[**BatchSearchResponse**](BatchSearchResponse.md)

### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Batch search results sorted by the batch updated timestamp in reverse chronological order |  -  |
| **400** | Response returned when an operation fails. |  -  |


## updateUserVaultPermissions

> updateUserVaultPermissions(updateUserVaultPermissionsRequest)

Updates a user&#39;s permissions on a vault. Any existing permissions are overwritten. An empty permission list removes all permissions for the vault.

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.models.*;
import edu.wisc.library.sdg.preservation.manager.client.api.PreservationManagerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/api");
        
        // Configure API key authorization: ApiKeyAuth
        ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ApiKeyAuth.setApiKeyPrefix("Token");

        PreservationManagerApi apiInstance = new PreservationManagerApi(defaultClient);
        UpdateUserVaultPermissionsRequest updateUserVaultPermissionsRequest = new UpdateUserVaultPermissionsRequest(); // UpdateUserVaultPermissionsRequest | 
        try {
            apiInstance.updateUserVaultPermissions(updateUserVaultPermissionsRequest);
        } catch (ApiException e) {
            System.err.println("Exception when calling PreservationManagerApi#updateUserVaultPermissions");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **updateUserVaultPermissionsRequest** | [**UpdateUserVaultPermissionsRequest**](UpdateUserVaultPermissionsRequest.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Success |  -  |
| **400** | Response returned when an operation fails. |  -  |


## validateObject

> ValidatePreservationObjectResponse validateObject(validatePreservationObjectRequest)

Validates the local copy of an object

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.models.*;
import edu.wisc.library.sdg.preservation.manager.client.api.PreservationManagerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/api");
        
        // Configure API key authorization: ApiKeyAuth
        ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ApiKeyAuth.setApiKeyPrefix("Token");

        PreservationManagerApi apiInstance = new PreservationManagerApi(defaultClient);
        ValidatePreservationObjectRequest validatePreservationObjectRequest = new ValidatePreservationObjectRequest(); // ValidatePreservationObjectRequest | 
        try {
            ValidatePreservationObjectResponse result = apiInstance.validateObject(validatePreservationObjectRequest);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling PreservationManagerApi#validateObject");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **validatePreservationObjectRequest** | [**ValidatePreservationObjectRequest**](ValidatePreservationObjectRequest.md)|  | [optional]

### Return type

[**ValidatePreservationObjectResponse**](ValidatePreservationObjectResponse.md)

### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Validate object response |  -  |
| **400** | Response returned when an operation fails. |  -  |


## validateObjectRemote

> ValidateObjectRemoteResponse validateObjectRemote(validateObjectRemoteRequest)

Validates a remote copy of an object

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.models.*;
import edu.wisc.library.sdg.preservation.manager.client.api.PreservationManagerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/api");
        
        // Configure API key authorization: ApiKeyAuth
        ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ApiKeyAuth.setApiKeyPrefix("Token");

        PreservationManagerApi apiInstance = new PreservationManagerApi(defaultClient);
        ValidateObjectRemoteRequest validateObjectRemoteRequest = new ValidateObjectRemoteRequest(); // ValidateObjectRemoteRequest | 
        try {
            ValidateObjectRemoteResponse result = apiInstance.validateObjectRemote(validateObjectRemoteRequest);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling PreservationManagerApi#validateObjectRemote");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **validateObjectRemoteRequest** | [**ValidateObjectRemoteRequest**](ValidateObjectRemoteRequest.md)|  | [optional]

### Return type

[**ValidateObjectRemoteResponse**](ValidateObjectRemoteResponse.md)

### Authorization

[ApiKeyAuth](../README.md#ApiKeyAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Validate object response |  -  |
| **400** | Response returned when an operation fails. |  -  |

