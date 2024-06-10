# ManagerInternalApi

All URIs are relative to *http://localhost:8484/internal/api*

Method | HTTP request | Description
------------- | ------------- | -------------
[**batchCompleteAnalysis**](ManagerInternalApi.md#batchCompleteAnalysis) | **POST** /batch/completeAnalysis | Transitions a batch out of an analysis state
[**batchCompleteIngesting**](ManagerInternalApi.md#batchCompleteIngesting) | **POST** /batch/completeIngesting | Transitions a batch out of an ingesting state
[**batchCompleteRejecting**](ManagerInternalApi.md#batchCompleteRejecting) | **POST** /batch/completeRejecting | Transitions a batch out of a rejecting state
[**batchMarkDeleted**](ManagerInternalApi.md#batchMarkDeleted) | **POST** /batch/markDeleted | Transitions a batch to a deleted state
[**batchStartAnalysis**](ManagerInternalApi.md#batchStartAnalysis) | **POST** /batch/startAnalysis | Transitions a batch into an analysis state
[**batchStartIngesting**](ManagerInternalApi.md#batchStartIngesting) | **POST** /batch/startIngesting | Transitions a batch into an ingesting state
[**batchStartRejecting**](ManagerInternalApi.md#batchStartRejecting) | **POST** /batch/startRejecting | Transitions a batch into a rejecting state
[**createObjectVersion**](ManagerInternalApi.md#createObjectVersion) | **PUT** /object/version | Creates a new version of a preservation object
[**defer**](ManagerInternalApi.md#defer) | **POST** /job/defer | Defers a job if it is not ready to be processed
[**deleteObjectVersion**](ManagerInternalApi.md#deleteObjectVersion) | **DELETE** /object/version | Deletes a preservation object version
[**finalizeObjectVersion**](ManagerInternalApi.md#finalizeObjectVersion) | **POST** /object/version/finalize | Finalizes the creation of a new object version and sets it to HEAD
[**getObjectStorageProblems**](ManagerInternalApi.md#getObjectStorageProblems) | **GET** /object/problems/storage | Retrieves details about what storage problems are affecting an object
[**getObjectVersionStates**](ManagerInternalApi.md#getObjectVersionStates) | **GET** /object/{internalObjectId}/versionStates | Describes the version state of every version of an object
[**getPremisDocument**](ManagerInternalApi.md#getPremisDocument) | **GET** /object/premis | Generates a PREMIS document for the object
[**objectCompleteAnalysis**](ManagerInternalApi.md#objectCompleteAnalysis) | **POST** /batch/object/completeAnalysis | Transitions a batch out of an analysis state
[**objectCompleteIngesting**](ManagerInternalApi.md#objectCompleteIngesting) | **POST** /batch/object/completeIngesting | Transitions a batch out of an ingesting state
[**objectStartIngesting**](ManagerInternalApi.md#objectStartIngesting) | **POST** /batch/object/startIngesting | Transitions a batch into an ingesting state
[**objectVersionReplicated**](ManagerInternalApi.md#objectVersionReplicated) | **POST** /object/version/replicated | Marks an object version as being replicated to a data store
[**pollForJob**](ManagerInternalApi.md#pollForJob) | **GET** /job/poll | Long poll for a job to process
[**recordIngestEvent**](ManagerInternalApi.md#recordIngestEvent) | **POST** /batch/event | Record ingest event
[**recordJobLogs**](ManagerInternalApi.md#recordJobLogs) | **POST** /job/logs | Adds logs to a job
[**recordPreservationEvent**](ManagerInternalApi.md#recordPreservationEvent) | **POST** /object/event | Record preservation event
[**registerIngestObject**](ManagerInternalApi.md#registerIngestObject) | **POST** /batch/object | Registers an object to a batch
[**registerIngestObjectFile**](ManagerInternalApi.md#registerIngestObjectFile) | **POST** /batch/object/file | Registers a file to an object in a batch
[**registerIngestObjectFileDetails**](ManagerInternalApi.md#registerIngestObjectFileDetails) | **POST** /batch/object/file/details | Adds details to an ingest object file
[**retrieveBatch**](ManagerInternalApi.md#retrieveBatch) | **GET** /batch/{ingestId} | Retrieves Ingest Batch details
[**retrieveBatchIngest**](ManagerInternalApi.md#retrieveBatchIngest) | **GET** /batch/{ingestId}/ingest | Retrieves all of the details about batch objects that are ready for ingest
[**retrieveNewInVersionFiles**](ManagerInternalApi.md#retrieveNewInVersionFiles) | **GET** /object/{internalObjectId}/persistenceVersion/{persistenceVersion}/filesNewInVersion | Retrieves a list of all of the files that were introduced to the object in the specified persistence version
[**setObjectStorageProblems**](ManagerInternalApi.md#setObjectStorageProblems) | **PUT** /object/problems/storage | Sets an object&#39;s storage problems
[**shouldDeleteBatch**](ManagerInternalApi.md#shouldDeleteBatch) | **POST** /batch/shouldDelete | Check if the batch is old and should be deleted
[**updateJobState**](ManagerInternalApi.md#updateJobState) | **POST** /job/state | Updates the state of a job



## batchCompleteAnalysis

> batchCompleteAnalysis(batchCompleteAnalysisRequest)

Transitions a batch out of an analysis state

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.internal.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.internal.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.models.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/internal/api");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        ManagerInternalApi apiInstance = new ManagerInternalApi(defaultClient);
        BatchCompleteAnalysisRequest batchCompleteAnalysisRequest = new BatchCompleteAnalysisRequest(); // BatchCompleteAnalysisRequest | 
        try {
            apiInstance.batchCompleteAnalysis(batchCompleteAnalysisRequest);
        } catch (ApiException e) {
            System.err.println("Exception when calling ManagerInternalApi#batchCompleteAnalysis");
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
 **batchCompleteAnalysisRequest** | [**BatchCompleteAnalysisRequest**](BatchCompleteAnalysisRequest.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Success |  -  |
| **400** | Response returned when an operation fails. |  -  |


## batchCompleteIngesting

> batchCompleteIngesting(batchCompleteIngestingRequest)

Transitions a batch out of an ingesting state

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.internal.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.internal.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.models.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/internal/api");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        ManagerInternalApi apiInstance = new ManagerInternalApi(defaultClient);
        BatchCompleteIngestingRequest batchCompleteIngestingRequest = new BatchCompleteIngestingRequest(); // BatchCompleteIngestingRequest | 
        try {
            apiInstance.batchCompleteIngesting(batchCompleteIngestingRequest);
        } catch (ApiException e) {
            System.err.println("Exception when calling ManagerInternalApi#batchCompleteIngesting");
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
 **batchCompleteIngestingRequest** | [**BatchCompleteIngestingRequest**](BatchCompleteIngestingRequest.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Success |  -  |
| **400** | Response returned when an operation fails. |  -  |


## batchCompleteRejecting

> batchCompleteRejecting(batchCompleteRejectingRequest)

Transitions a batch out of a rejecting state

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.internal.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.internal.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.models.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/internal/api");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        ManagerInternalApi apiInstance = new ManagerInternalApi(defaultClient);
        BatchCompleteRejectingRequest batchCompleteRejectingRequest = new BatchCompleteRejectingRequest(); // BatchCompleteRejectingRequest | 
        try {
            apiInstance.batchCompleteRejecting(batchCompleteRejectingRequest);
        } catch (ApiException e) {
            System.err.println("Exception when calling ManagerInternalApi#batchCompleteRejecting");
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
 **batchCompleteRejectingRequest** | [**BatchCompleteRejectingRequest**](BatchCompleteRejectingRequest.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Success |  -  |
| **400** | Response returned when an operation fails. |  -  |


## batchMarkDeleted

> batchMarkDeleted(batchMarkDeletedRequest)

Transitions a batch to a deleted state

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.internal.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.internal.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.models.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/internal/api");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        ManagerInternalApi apiInstance = new ManagerInternalApi(defaultClient);
        BatchMarkDeletedRequest batchMarkDeletedRequest = new BatchMarkDeletedRequest(); // BatchMarkDeletedRequest | 
        try {
            apiInstance.batchMarkDeleted(batchMarkDeletedRequest);
        } catch (ApiException e) {
            System.err.println("Exception when calling ManagerInternalApi#batchMarkDeleted");
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
 **batchMarkDeletedRequest** | [**BatchMarkDeletedRequest**](BatchMarkDeletedRequest.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Success |  -  |
| **400** | Response returned when an operation fails. |  -  |


## batchStartAnalysis

> batchStartAnalysis(batchStartAnalysisRequest)

Transitions a batch into an analysis state

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.internal.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.internal.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.models.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/internal/api");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        ManagerInternalApi apiInstance = new ManagerInternalApi(defaultClient);
        BatchStartAnalysisRequest batchStartAnalysisRequest = new BatchStartAnalysisRequest(); // BatchStartAnalysisRequest | 
        try {
            apiInstance.batchStartAnalysis(batchStartAnalysisRequest);
        } catch (ApiException e) {
            System.err.println("Exception when calling ManagerInternalApi#batchStartAnalysis");
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
 **batchStartAnalysisRequest** | [**BatchStartAnalysisRequest**](BatchStartAnalysisRequest.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Success |  -  |
| **400** | Response returned when an operation fails. |  -  |


## batchStartIngesting

> batchStartIngesting(batchStartIngestingRequest)

Transitions a batch into an ingesting state

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.internal.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.internal.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.models.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/internal/api");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        ManagerInternalApi apiInstance = new ManagerInternalApi(defaultClient);
        BatchStartIngestingRequest batchStartIngestingRequest = new BatchStartIngestingRequest(); // BatchStartIngestingRequest | 
        try {
            apiInstance.batchStartIngesting(batchStartIngestingRequest);
        } catch (ApiException e) {
            System.err.println("Exception when calling ManagerInternalApi#batchStartIngesting");
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
 **batchStartIngestingRequest** | [**BatchStartIngestingRequest**](BatchStartIngestingRequest.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Success |  -  |
| **400** | Response returned when an operation fails. |  -  |


## batchStartRejecting

> batchStartRejecting(batchStartRejectingRequest)

Transitions a batch into a rejecting state

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.internal.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.internal.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.models.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/internal/api");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        ManagerInternalApi apiInstance = new ManagerInternalApi(defaultClient);
        BatchStartRejectingRequest batchStartRejectingRequest = new BatchStartRejectingRequest(); // BatchStartRejectingRequest | 
        try {
            apiInstance.batchStartRejecting(batchStartRejectingRequest);
        } catch (ApiException e) {
            System.err.println("Exception when calling ManagerInternalApi#batchStartRejecting");
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
 **batchStartRejectingRequest** | [**BatchStartRejectingRequest**](BatchStartRejectingRequest.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Success |  -  |
| **400** | Response returned when an operation fails. |  -  |


## createObjectVersion

> CreateObjectVersionResponse createObjectVersion(createObjectVersionRequest)

Creates a new version of a preservation object

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.internal.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.internal.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.models.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/internal/api");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        ManagerInternalApi apiInstance = new ManagerInternalApi(defaultClient);
        CreateObjectVersionRequest createObjectVersionRequest = new CreateObjectVersionRequest(); // CreateObjectVersionRequest | 
        try {
            CreateObjectVersionResponse result = apiInstance.createObjectVersion(createObjectVersionRequest);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling ManagerInternalApi#createObjectVersion");
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
 **createObjectVersionRequest** | [**CreateObjectVersionRequest**](CreateObjectVersionRequest.md)|  | [optional]

### Return type

[**CreateObjectVersionResponse**](CreateObjectVersionResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Details about the new version |  -  |
| **400** | Response returned when an operation fails. |  -  |


## defer

> defer(deferJobRequest)

Defers a job if it is not ready to be processed

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.internal.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.internal.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.models.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/internal/api");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        ManagerInternalApi apiInstance = new ManagerInternalApi(defaultClient);
        DeferJobRequest deferJobRequest = new DeferJobRequest(); // DeferJobRequest | 
        try {
            apiInstance.defer(deferJobRequest);
        } catch (ApiException e) {
            System.err.println("Exception when calling ManagerInternalApi#defer");
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
 **deferJobRequest** | [**DeferJobRequest**](DeferJobRequest.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Success |  -  |
| **400** | Response returned when an operation fails. |  -  |


## deleteObjectVersion

> deleteObjectVersion(deleteObjectVersionRequest)

Deletes a preservation object version

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.internal.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.internal.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.models.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/internal/api");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        ManagerInternalApi apiInstance = new ManagerInternalApi(defaultClient);
        DeleteObjectVersionRequest deleteObjectVersionRequest = new DeleteObjectVersionRequest(); // DeleteObjectVersionRequest | 
        try {
            apiInstance.deleteObjectVersion(deleteObjectVersionRequest);
        } catch (ApiException e) {
            System.err.println("Exception when calling ManagerInternalApi#deleteObjectVersion");
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
 **deleteObjectVersionRequest** | [**DeleteObjectVersionRequest**](DeleteObjectVersionRequest.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Success |  -  |
| **400** | Response returned when an operation fails. |  -  |


## finalizeObjectVersion

> finalizeObjectVersion(finalizeObjectVersionRequest)

Finalizes the creation of a new object version and sets it to HEAD

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.internal.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.internal.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.models.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/internal/api");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        ManagerInternalApi apiInstance = new ManagerInternalApi(defaultClient);
        FinalizeObjectVersionRequest finalizeObjectVersionRequest = new FinalizeObjectVersionRequest(); // FinalizeObjectVersionRequest | 
        try {
            apiInstance.finalizeObjectVersion(finalizeObjectVersionRequest);
        } catch (ApiException e) {
            System.err.println("Exception when calling ManagerInternalApi#finalizeObjectVersion");
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
 **finalizeObjectVersionRequest** | [**FinalizeObjectVersionRequest**](FinalizeObjectVersionRequest.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Success |  -  |
| **400** | Response returned when an operation fails. |  -  |


## getObjectStorageProblems

> GetObjectStorageProblemsResponse getObjectStorageProblems(internalObjectId)

Retrieves details about what storage problems are affecting an object

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.internal.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.internal.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.models.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/internal/api");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        ManagerInternalApi apiInstance = new ManagerInternalApi(defaultClient);
        String internalObjectId = "urn:uuid:3c658673-38aa-4425-b931-2971c45256d3"; // String | The internal ID of the object
        try {
            GetObjectStorageProblemsResponse result = apiInstance.getObjectStorageProblems(internalObjectId);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling ManagerInternalApi#getObjectStorageProblems");
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
 **internalObjectId** | **String**| The internal ID of the object |

### Return type

[**GetObjectStorageProblemsResponse**](GetObjectStorageProblemsResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The object&#39;s storage problems, if any |  -  |
| **400** | Response returned when an operation fails. |  -  |


## getObjectVersionStates

> DescribeObjectVersionStatesResponse getObjectVersionStates(internalObjectId)

Describes the version state of every version of an object

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.internal.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.internal.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.models.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/internal/api");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        ManagerInternalApi apiInstance = new ManagerInternalApi(defaultClient);
        String internalObjectId = "urn:uuid:3c658673-38aa-4425-b931-2971c45256d3"; // String | The internal ID of the object
        try {
            DescribeObjectVersionStatesResponse result = apiInstance.getObjectVersionStates(internalObjectId);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling ManagerInternalApi#getObjectVersionStates");
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
 **internalObjectId** | **String**| The internal ID of the object |

### Return type

[**DescribeObjectVersionStatesResponse**](DescribeObjectVersionStatesResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Details about the state of every version of an object |  -  |
| **400** | Response returned when an operation fails. |  -  |


## getPremisDocument

> File getPremisDocument(internalObjectId, versionNumbers)

Generates a PREMIS document for the object

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.internal.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.internal.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.models.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/internal/api");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        ManagerInternalApi apiInstance = new ManagerInternalApi(defaultClient);
        String internalObjectId = "urn:uuid:3c658673-38aa-4425-b931-2971c45256d3"; // String | The internal ID of the object
        List<Integer> versionNumbers = Arrays.asList(); // List<Integer> | List of versions to include in PREMIS
        try {
            File result = apiInstance.getPremisDocument(internalObjectId, versionNumbers);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling ManagerInternalApi#getPremisDocument");
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
 **internalObjectId** | **String**| The internal ID of the object |
 **versionNumbers** | [**List&lt;Integer&gt;**](Integer.md)| List of versions to include in PREMIS |

### Return type

[**File**](File.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/xml


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Success |  -  |


## objectCompleteAnalysis

> objectCompleteAnalysis(objectCompleteAnalysisRequest)

Transitions a batch out of an analysis state

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.internal.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.internal.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.models.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/internal/api");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        ManagerInternalApi apiInstance = new ManagerInternalApi(defaultClient);
        ObjectCompleteAnalysisRequest objectCompleteAnalysisRequest = new ObjectCompleteAnalysisRequest(); // ObjectCompleteAnalysisRequest | 
        try {
            apiInstance.objectCompleteAnalysis(objectCompleteAnalysisRequest);
        } catch (ApiException e) {
            System.err.println("Exception when calling ManagerInternalApi#objectCompleteAnalysis");
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
 **objectCompleteAnalysisRequest** | [**ObjectCompleteAnalysisRequest**](ObjectCompleteAnalysisRequest.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Success |  -  |
| **400** | Response returned when an operation fails. |  -  |


## objectCompleteIngesting

> objectCompleteIngesting(objectCompleteIngestingRequest)

Transitions a batch out of an ingesting state

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.internal.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.internal.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.models.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/internal/api");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        ManagerInternalApi apiInstance = new ManagerInternalApi(defaultClient);
        ObjectCompleteIngestingRequest objectCompleteIngestingRequest = new ObjectCompleteIngestingRequest(); // ObjectCompleteIngestingRequest | 
        try {
            apiInstance.objectCompleteIngesting(objectCompleteIngestingRequest);
        } catch (ApiException e) {
            System.err.println("Exception when calling ManagerInternalApi#objectCompleteIngesting");
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
 **objectCompleteIngestingRequest** | [**ObjectCompleteIngestingRequest**](ObjectCompleteIngestingRequest.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Success |  -  |
| **400** | Response returned when an operation fails. |  -  |


## objectStartIngesting

> objectStartIngesting(objectStartIngestingRequest)

Transitions a batch into an ingesting state

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.internal.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.internal.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.models.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/internal/api");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        ManagerInternalApi apiInstance = new ManagerInternalApi(defaultClient);
        ObjectStartIngestingRequest objectStartIngestingRequest = new ObjectStartIngestingRequest(); // ObjectStartIngestingRequest | 
        try {
            apiInstance.objectStartIngesting(objectStartIngestingRequest);
        } catch (ApiException e) {
            System.err.println("Exception when calling ManagerInternalApi#objectStartIngesting");
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
 **objectStartIngestingRequest** | [**ObjectStartIngestingRequest**](ObjectStartIngestingRequest.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Success |  -  |
| **400** | Response returned when an operation fails. |  -  |


## objectVersionReplicated

> objectVersionReplicated(objectVersionReplicatedRequest)

Marks an object version as being replicated to a data store

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.internal.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.internal.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.models.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/internal/api");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        ManagerInternalApi apiInstance = new ManagerInternalApi(defaultClient);
        ObjectVersionReplicatedRequest objectVersionReplicatedRequest = new ObjectVersionReplicatedRequest(); // ObjectVersionReplicatedRequest | 
        try {
            apiInstance.objectVersionReplicated(objectVersionReplicatedRequest);
        } catch (ApiException e) {
            System.err.println("Exception when calling ManagerInternalApi#objectVersionReplicated");
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
 **objectVersionReplicatedRequest** | [**ObjectVersionReplicatedRequest**](ObjectVersionReplicatedRequest.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Success |  -  |
| **400** | Response returned when an operation fails. |  -  |


## pollForJob

> JobPollResponse pollForJob()

Long poll for a job to process

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.internal.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.internal.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.models.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/internal/api");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        ManagerInternalApi apiInstance = new ManagerInternalApi(defaultClient);
        try {
            JobPollResponse result = apiInstance.pollForJob();
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling ManagerInternalApi#pollForJob");
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

[**JobPollResponse**](JobPollResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A job to process |  -  |
| **400** | Response returned when an operation fails. |  -  |


## recordIngestEvent

> recordIngestEvent(recordIngestEventRequest)

Record ingest event

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.internal.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.internal.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.models.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/internal/api");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        ManagerInternalApi apiInstance = new ManagerInternalApi(defaultClient);
        RecordIngestEventRequest recordIngestEventRequest = new RecordIngestEventRequest(); // RecordIngestEventRequest | 
        try {
            apiInstance.recordIngestEvent(recordIngestEventRequest);
        } catch (ApiException e) {
            System.err.println("Exception when calling ManagerInternalApi#recordIngestEvent");
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
 **recordIngestEventRequest** | [**RecordIngestEventRequest**](RecordIngestEventRequest.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Success |  -  |
| **400** | Response returned when an operation fails. |  -  |


## recordJobLogs

> recordJobLogs(recordJobLogsRequest)

Adds logs to a job

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.internal.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.internal.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.models.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/internal/api");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        ManagerInternalApi apiInstance = new ManagerInternalApi(defaultClient);
        RecordJobLogsRequest recordJobLogsRequest = new RecordJobLogsRequest(); // RecordJobLogsRequest | 
        try {
            apiInstance.recordJobLogs(recordJobLogsRequest);
        } catch (ApiException e) {
            System.err.println("Exception when calling ManagerInternalApi#recordJobLogs");
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
 **recordJobLogsRequest** | [**RecordJobLogsRequest**](RecordJobLogsRequest.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Success |  -  |
| **400** | Response returned when an operation fails. |  -  |


## recordPreservationEvent

> recordPreservationEvent(recordPreservationEventRequest)

Record preservation event

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.internal.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.internal.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.models.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/internal/api");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        ManagerInternalApi apiInstance = new ManagerInternalApi(defaultClient);
        RecordPreservationEventRequest recordPreservationEventRequest = new RecordPreservationEventRequest(); // RecordPreservationEventRequest | 
        try {
            apiInstance.recordPreservationEvent(recordPreservationEventRequest);
        } catch (ApiException e) {
            System.err.println("Exception when calling ManagerInternalApi#recordPreservationEvent");
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
 **recordPreservationEventRequest** | [**RecordPreservationEventRequest**](RecordPreservationEventRequest.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Success |  -  |
| **400** | Response returned when an operation fails. |  -  |


## registerIngestObject

> RegisterIngestObjectResponse registerIngestObject(registerIngestObjectRequest)

Registers an object to a batch

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.internal.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.internal.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.models.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/internal/api");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        ManagerInternalApi apiInstance = new ManagerInternalApi(defaultClient);
        RegisterIngestObjectRequest registerIngestObjectRequest = new RegisterIngestObjectRequest(); // RegisterIngestObjectRequest | 
        try {
            RegisterIngestObjectResponse result = apiInstance.registerIngestObject(registerIngestObjectRequest);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling ManagerInternalApi#registerIngestObject");
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
 **registerIngestObjectRequest** | [**RegisterIngestObjectRequest**](RegisterIngestObjectRequest.md)|  | [optional]

### Return type

[**RegisterIngestObjectResponse**](RegisterIngestObjectResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Register ingest object response |  -  |
| **400** | Response returned when an operation fails. |  -  |


## registerIngestObjectFile

> RegisterIngestObjectFileResponse registerIngestObjectFile(registerIngestObjectFileRequest)

Registers a file to an object in a batch

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.internal.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.internal.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.models.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/internal/api");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        ManagerInternalApi apiInstance = new ManagerInternalApi(defaultClient);
        RegisterIngestObjectFileRequest registerIngestObjectFileRequest = new RegisterIngestObjectFileRequest(); // RegisterIngestObjectFileRequest | 
        try {
            RegisterIngestObjectFileResponse result = apiInstance.registerIngestObjectFile(registerIngestObjectFileRequest);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling ManagerInternalApi#registerIngestObjectFile");
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
 **registerIngestObjectFileRequest** | [**RegisterIngestObjectFileRequest**](RegisterIngestObjectFileRequest.md)|  | [optional]

### Return type

[**RegisterIngestObjectFileResponse**](RegisterIngestObjectFileResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Register ingest object file response |  -  |
| **400** | Response returned when an operation fails. |  -  |


## registerIngestObjectFileDetails

> registerIngestObjectFileDetails(registerIngestObjectFileDetailsRequest)

Adds details to an ingest object file

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.internal.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.internal.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.models.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/internal/api");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        ManagerInternalApi apiInstance = new ManagerInternalApi(defaultClient);
        RegisterIngestObjectFileDetailsRequest registerIngestObjectFileDetailsRequest = new RegisterIngestObjectFileDetailsRequest(); // RegisterIngestObjectFileDetailsRequest | 
        try {
            apiInstance.registerIngestObjectFileDetails(registerIngestObjectFileDetailsRequest);
        } catch (ApiException e) {
            System.err.println("Exception when calling ManagerInternalApi#registerIngestObjectFileDetails");
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
 **registerIngestObjectFileDetailsRequest** | [**RegisterIngestObjectFileDetailsRequest**](RegisterIngestObjectFileDetailsRequest.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Success |  -  |
| **400** | Response returned when an operation fails. |  -  |


## retrieveBatch

> RetrieveBatchResponse retrieveBatch(ingestId)

Retrieves Ingest Batch details

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.internal.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.internal.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.models.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/internal/api");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        ManagerInternalApi apiInstance = new ManagerInternalApi(defaultClient);
        Long ingestId = 10293L; // Long | The ID for the Ingest Batch
        try {
            RetrieveBatchResponse result = apiInstance.retrieveBatch(ingestId);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling ManagerInternalApi#retrieveBatch");
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

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Ingest Batch details |  -  |
| **400** | Response returned when an operation fails. |  -  |


## retrieveBatchIngest

> RetrieveBatchIngestResponse retrieveBatchIngest(ingestId)

Retrieves all of the details about batch objects that are ready for ingest

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.internal.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.internal.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.models.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/internal/api");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        ManagerInternalApi apiInstance = new ManagerInternalApi(defaultClient);
        Long ingestId = 10293L; // Long | The ID for the Ingest Batch
        try {
            RetrieveBatchIngestResponse result = apiInstance.retrieveBatchIngest(ingestId);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling ManagerInternalApi#retrieveBatchIngest");
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

[**RetrieveBatchIngestResponse**](RetrieveBatchIngestResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Ingest Batch details |  -  |
| **400** | Response returned when an operation fails. |  -  |


## retrieveNewInVersionFiles

> RetrieveNewInVersionFilesResponse retrieveNewInVersionFiles(internalObjectId, persistenceVersion)

Retrieves a list of all of the files that were introduced to the object in the specified persistence version

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.internal.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.internal.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.models.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/internal/api");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        ManagerInternalApi apiInstance = new ManagerInternalApi(defaultClient);
        String internalObjectId = "urn:uuid:3c658673-38aa-4425-b931-2971c45256d3"; // String | The internal ID of the object
        String persistenceVersion = "v2"; // String | The version identifier of the object version
        try {
            RetrieveNewInVersionFilesResponse result = apiInstance.retrieveNewInVersionFiles(internalObjectId, persistenceVersion);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling ManagerInternalApi#retrieveNewInVersionFiles");
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
 **internalObjectId** | **String**| The internal ID of the object |
 **persistenceVersion** | **String**| The version identifier of the object version |

### Return type

[**RetrieveNewInVersionFilesResponse**](RetrieveNewInVersionFilesResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Files new to the object in the specified version |  -  |
| **400** | Response returned when an operation fails. |  -  |


## setObjectStorageProblems

> setObjectStorageProblems(setObjectStorageProblemsRequest)

Sets an object&#39;s storage problems

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.internal.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.internal.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.models.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/internal/api");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        ManagerInternalApi apiInstance = new ManagerInternalApi(defaultClient);
        SetObjectStorageProblemsRequest setObjectStorageProblemsRequest = new SetObjectStorageProblemsRequest(); // SetObjectStorageProblemsRequest | 
        try {
            apiInstance.setObjectStorageProblems(setObjectStorageProblemsRequest);
        } catch (ApiException e) {
            System.err.println("Exception when calling ManagerInternalApi#setObjectStorageProblems");
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
 **setObjectStorageProblemsRequest** | [**SetObjectStorageProblemsRequest**](SetObjectStorageProblemsRequest.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Success |  -  |
| **400** | Response returned when an operation fails. |  -  |


## shouldDeleteBatch

> ShouldDeleteBatchResponse shouldDeleteBatch(shouldDeleteBatchRequest)

Check if the batch is old and should be deleted

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.internal.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.internal.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.models.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/internal/api");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        ManagerInternalApi apiInstance = new ManagerInternalApi(defaultClient);
        ShouldDeleteBatchRequest shouldDeleteBatchRequest = new ShouldDeleteBatchRequest(); // ShouldDeleteBatchRequest | 
        try {
            ShouldDeleteBatchResponse result = apiInstance.shouldDeleteBatch(shouldDeleteBatchRequest);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling ManagerInternalApi#shouldDeleteBatch");
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
 **shouldDeleteBatchRequest** | [**ShouldDeleteBatchRequest**](ShouldDeleteBatchRequest.md)|  | [optional]

### Return type

[**ShouldDeleteBatchResponse**](ShouldDeleteBatchResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Indicates if the batch should be deleted |  -  |
| **400** | Response returned when an operation fails. |  -  |


## updateJobState

> updateJobState(updateJobStateRequest)

Updates the state of a job

### Example

```java
// Import classes:
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiClient;
import edu.wisc.library.sdg.preservation.manager.client.internal.ApiException;
import edu.wisc.library.sdg.preservation.manager.client.internal.Configuration;
import edu.wisc.library.sdg.preservation.manager.client.internal.auth.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.models.*;
import edu.wisc.library.sdg.preservation.manager.client.internal.api.ManagerInternalApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8484/internal/api");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        ManagerInternalApi apiInstance = new ManagerInternalApi(defaultClient);
        UpdateJobStateRequest updateJobStateRequest = new UpdateJobStateRequest(); // UpdateJobStateRequest | 
        try {
            apiInstance.updateJobState(updateJobStateRequest);
        } catch (ApiException e) {
            System.err.println("Exception when calling ManagerInternalApi#updateJobState");
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
 **updateJobStateRequest** | [**UpdateJobStateRequest**](UpdateJobStateRequest.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Success |  -  |
| **400** | Response returned when an operation fails. |  -  |

