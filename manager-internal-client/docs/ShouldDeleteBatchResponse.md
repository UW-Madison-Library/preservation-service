

# ShouldDeleteBatchResponse


## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**ingestId** | **Long** | The ID for the Ingest Batch |  [optional]
**verdict** | [**VerdictEnum**](#VerdictEnum) | Indicates if the batch should be kept or deleted |  [optional]



## Enum: VerdictEnum

Name | Value
---- | -----
KEEP | &quot;KEEP&quot;
DELETE | &quot;DELETE&quot;
NOT_FOUND | &quot;NOT_FOUND&quot;



