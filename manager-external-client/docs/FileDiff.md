

# FileDiff


## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**path** | **String** | The object relative path of the file |  [optional]
**newSha256Digest** | **String** | The sha256 digest of the file in the batch |  [optional]
**oldSha256Digest** | **String** | The sha256 digest of the file in the preserved object |  [optional]
**diff** | [**DiffEnum**](#DiffEnum) | The relationship between the batch object file and the preserved object file |  [optional]
**formatDiff** | [**FormatDiff**](FormatDiff.md) |  |  [optional]
**encodingDiff** | [**EncodingDiff**](EncodingDiff.md) |  |  [optional]
**validityDiff** | [**ValidityDiff**](ValidityDiff.md) |  |  [optional]



## Enum: DiffEnum

Name | Value
---- | -----
ADDED | &quot;ADDED&quot;
REMOVED | &quot;REMOVED&quot;
MODIFIED | &quot;MODIFIED&quot;
UNCHANGED | &quot;UNCHANGED&quot;



