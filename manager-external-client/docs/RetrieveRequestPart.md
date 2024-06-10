

# RetrieveRequestPart


## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**jobId** | **Long** | The id of a job |  [optional]
**state** | [**StateEnum**](#StateEnum) | Indicates the state of this request part |  [optional]
**lastDownloadedTimestamp** | **OffsetDateTime** | Timestamp this part was last downloaded |  [optional]



## Enum: StateEnum

Name | Value
---- | -----
READY | &quot;READY&quot;
NOT_READY | &quot;NOT_READY&quot;
FAILED | &quot;FAILED&quot;
DELETED | &quot;DELETED&quot;
CANCELLED | &quot;CANCELLED&quot;



