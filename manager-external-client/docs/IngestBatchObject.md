

# IngestBatchObject

An object within an Ingest Batch

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**ingestId** | **Long** | The ID for the Ingest Batch | 
**vault** | **String** | The name of the vault | 
**externalObjectId** | **String** | The external ID of the object | 
**version** | **Integer** | The version of an object |  [optional]
**state** | **IngestObjectState** |  | 
**reviewedBy** | **String** | The username of the user who approved/rejected the batch object |  [optional]
**hasAnalysisErrors** | **Boolean** | Indicates if there were errors during analysis |  [optional]
**hasAnalysisWarnings** | **Boolean** | Indicates if there were warnings during analysis |  [optional]



