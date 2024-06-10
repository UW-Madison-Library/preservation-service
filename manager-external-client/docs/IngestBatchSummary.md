

# IngestBatchSummary

Ingest Batch summary

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**ingestId** | **Long** | The ID for the Ingest Batch | 
**orgName** | **String** | The ID of the organization | 
**vault** | **String** | The name of the vault | 
**createdBy** | **String** | The username of the user who submitted the batch | 
**reviewedBy** | **String** | The username of the user who approved/rejected the batch object |  [optional]
**state** | **IngestBatchState** |  | 
**originalFilename** | **String** | The name of the file as uploaded |  [optional]
**fileSize** | **Long** | Bag size in bytes |  [optional]
**hasAnalysisErrors** | **Boolean** | Indicates if there were errors during analysis |  [optional]
**hasAnalysisWarnings** | **Boolean** | Indicates if there were warnings during analysis |  [optional]
**receivedTimestamp** | **OffsetDateTime** | UTC timestamp when the batch was received | 
**updatedTimestamp** | **OffsetDateTime** | UTC timestamp when the batch was last updated |  [optional]



