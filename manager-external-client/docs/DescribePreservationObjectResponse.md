

# DescribePreservationObjectResponse


## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**vault** | **String** | The name of the vault |  [optional]
**externalObjectId** | **String** | The external ID of the object |  [optional]
**version** | **Integer** | The version of an object |  [optional]
**latestVersion** | **Integer** | The version of an object |  [optional]
**state** | **PreservationObjectState** |  |  [optional]
**ingestId** | **Long** | The ID for the Ingest Batch |  [optional]
**objectCreatedTimestamp** | **OffsetDateTime** | Timestamp the first version of the object was created |  [optional]
**versionCreatedTimestamp** | **OffsetDateTime** | Timestamp this object version was created |  [optional]
**lastShallowCheck** | **OffsetDateTime** | Timestamp the object last had a shallow validation check |  [optional]
**lastDeepCheck** | **OffsetDateTime** | Timestamp the object last had a deep validation check |  [optional]
**remoteVersionCheck** | [**List&lt;RemoteVersionCheck&gt;**](RemoteVersionCheck.md) | When this object version was last checked in remote storage locations |  [optional]
**files** | [**List&lt;ObjectFile&gt;**](ObjectFile.md) | The files contained in this version of the object |  [optional]



