

# BatchObjectWithFiles

An object within an Ingest Batch

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**ingestObjectId** | **Long** | The ID of the object within the batch |  [optional]
**ingestId** | **Long** | The ID for the Ingest Batch |  [optional]
**externalObjectId** | **String** | The external ID of the object |  [optional]
**objectRootPath** | **String** | The path to the object root on disk |  [optional]
**state** | **IngestObjectState** |  |  [optional]
**internalObjectId** | **String** | The internal ID of the object |  [optional]
**headPersistenceVersion** | **String** | The version identifier of the object version |  [optional]
**approverName** | **String** | The display name of the user who approved the object |  [optional]
**approverAddress** | **String** | The address or id of the user who approved the object, should be a URI |  [optional]
**files** | [**List&lt;ObjectFile&gt;**](ObjectFile.md) | The files associated to the object |  [optional]



