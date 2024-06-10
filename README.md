# Preservation Service
This repository is archived and will not be updated.

This is a service for preserving objects. There are two primary components to the system,
the Preservation Manager and the Preservation Workers.

The Preservation Manager keeps track of all the objects preserved in the system, and provides an
API for interacting with the system. The Preservation Workers prepare objects for storage and replication.

Users preserve objects by submitting Bagit bags to the [Preservation Manager to ingest](manager/src/main/java/edu/wisc/library/sdg/preservation/manager/controller/IngestController.java).
Objects within a bag must be encapsulated in directories that are named using the external
object id. For example:
```
bag.zip
  +-- bagit.txt
  +-- manifest-sha256.txt
  +-- data/
        +-- external-object-id-01/
        |     +-- FILE1
        |     +-- FILE2
        |
        +-- external-object-id-02/
              +-- FILE1
              +-- FILE2
```

## Requirements

Maven 3+ and Java 17+ are required.

The preservation service has been developed and tested on Mac and Linux systems;  it will not build or run on Windows.

## Development

### Build

The Preservation Service is built using [Maven](https://maven.apache.org/).
To build, clone the repository, then first execute the following command in the top-level directory:

```shell
mvn validate
```
This only needs to be run once, the first time you build the package. This command copies the UW Madison 
[bagit-java library](https://github.com/UW-Madison-Library/bagit-java) jar into your local maven repository.

To build the preservation service jars and install them to your local maven repository:

```shell
mvn clean install
```
Run the following to build the entire project and install the jars to your local maven repository, without running tests:

```shell
mvn -DskipTests clean install
```

The tests can be run, without installing the jar to your repository, by executing:

```shell
mvn clean verify
```

## Run

Execute this command twice, once from within the `manager` directory, and once from within the
`worker` directory:

```shell
mvn clean spring-boot:run
```

This will start the two services. By default, they are configured to use an in-memory non-persistent H2 database and 
store objects on the local filesystem. *These default settings are provided for test and exploration purposes only.  
We strongly recommend setting up a separate database and dedicated storage platforms, and creating
your own API keys, for production use.*

These services run in the foreground.  You'll need to open a separate terminal session to run each of these two 
services, and a third terminal session to test the service out.

- **Endpoint**: `http://localhost:8484/api`
- **Org**: `test-organization`
- **Vault**: `test-vault`
- **User**: `fedora-object-preserver`
   - **API Key**: `9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08`
- **User**: `service-admin-1`
   - **API Key**: `55c7b019ea4b3d546425d4a8d24c3a8dcf00a449eb1da51f5f2d0f080e6cc883`

## Test

You can verify that the preservation service and workers are running correctly in the included sample repository by executing
the following `curl` commands.

For the purposes of exploring the repository functions, you can use the test zip files under `worker/src/test/resources/itest/bags/`.

You must specify an API key with each request to the service, by setting the `X-API-KEY` header.
Include this parameter in your `curl` command, for example: `-H "X-API-KEY: 55c7b019ea4b3d546425d4a8d24c3a8dcf00a449eb1da51f5f2d0f080e6cc883`.

### Preserve something

To submit a batch, view its details, and approve it, use the `fedora-object-preserver` API key.

#### Submit a bag and create a batch
```shell
curl -k -H "X-API-KEY: 9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08" \
  -F orgName=test-organization \
  -F vault=test-vault \
  -F file=@worker/src/test/resources/itest/bags/single-valid.zip \
  "http://localhost:8484/api/ingest/bag"
```
Output
```shell
{"ingestId":1}
```
The `ingestId` is the ID of the batch to examine and approve.

#### Show details about the batch
```shell
curl -k -H "X-API-KEY: 9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08" \
  "http://localhost:8484/api/batch/1"
```
The number at the end of the path is the batch ID, retrieved above.  
Output
```shell
{
  "ingestBatch":{
  "ingestId":1,
  "orgName":"test-organization",
  "vault":"test-vault",
  "createdBy":"fedora-object-preserver",
  "reviewedBy":null,
  "state":"PENDING_REVIEW",
  "originalFilename":"single-valid.zip",
  "fileSize":2853,
  "hasAnalysisErrors":null,
  "hasAnalysisWarnings":null,
  "receivedTimestamp":"2024-06-04T21:09:18.365857Z",
  "updatedTimestamp":"2024-06-04T21:09:18.774716Z"
  }
}
```
Note the status *PENDING_REVIEW*.  This indicates that the bag has been successfully received, unpacked, validated, and analyzed, and is now ready for approval.

#### Show the objects in the batch
```shell
curl -k -H "X-API-KEY: 9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08" \
  "http://localhost:8484/api/batch/1/objects"
```
Output
```shell
{ 
  "page":0,
  "totalPages":1,
  "totalResults":1,
  "batchObjects":[
    {
      "ingestId":1,
      "vault":"test-vault",
      "externalObjectId":"47c70a8a70b3b94b74e5f1670f3f4e0d",
      "version":null,
      "state":"PENDING_REVIEW",
      "reviewedBy":null,
      "hasAnalysisErrors":false,
      "hasAnalysisWarnings":false
    }
  ]
}
```
The `externalObjectId` is used to query the state of individual objects in the preservation repository. 
Once a batch is approved and replicated; it is no longer tracked; only objects are stored in the preservation repository.

#### Approve the batch
```shell
curl -k -H "X-API-KEY: 9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08" \
  -H "Content-Type: application/json" \
  -d '{"ingestId":"1"}' \
  "http://localhost:8484/api/batch/approve"
```
Output: none.  You will only see an error if the approval failed for any reason.

#### See the details of an ingested object
```shell
curl -k -H "X-API-KEY: 9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08" \
  -H "Content-Type: application/json" \
  "http://localhost:8484/api/object?vault=test-vault&externalObjectId=47c70a8a70b3b94b74e5f1670f3f4e0d"
```
The `externalObjectId` parameter value is the same as the one shown in the batch object listing above.  
Output
```shell
{
  "vault":"test-vault",
  "externalObjectId":"47c70a8a70b3b94b74e5f1670f3f4e0d",
  "version":1,
  "latestVersion":1,
  "state":"ACTIVE",
  "ingestId":1,
  "objectCreatedTimestamp":"2024-06-04T21:20:17.050714Z",
  "versionCreatedTimestamp":"2024-06-04T21:20:17.054986Z",
  "lastShallowCheck":null,
  "lastDeepCheck":null,
  "remoteVersionCheck":[],
  "files":[
    {
      "filePath":"RELS-EXT",
      "sha256Digest":"9d603228bc05e508744dbe525da9bfd50693d58123c51e3089ae67d596012de8",
      "fileSize":355,
      "formats":[],
      "encoding":[],
      "validity":[]
    },
    {
      "filePath":"METHODMAP",
      "sha256Digest":"23beae1f8f82b83f6be9c40445fa535cd4c73f14b8433640ecda6a76911af558",
      "fileSize":399,
      "formats":[],
      "encoding":[],
      "validity":[]
    },
    {
      "filePath":"DC",
      "sha256Digest":"8e0a08d4b5e2d05019859082c60b7a589ffc12f95a55b17b521bca2ad6bf9926",
      "fileSize":407,
      "formats":[],
      "encoding":[],
      "validity":[]
    }
  ]
}
```
### Retrieve something

Creating and downloading DIP files containing objects preserved in the repository is a three-step process:
1. Submit a DIP request.  The DIP files are then assembled asynchronously in the background.
2. Retrieve the state of the DIP request.  Multiple job IDs and current job states are returned; the job IDs will be used to 
   retrieve the assembled DIP zip files, when they are ready.
3. Retrieve the DIP files, once they have been assembled.

Only a service administrator can request and retrieve DIPs;  use the `service-admin-1` API key for the following requests.

#### Submit a DIP request
```shell
curl -H "X-API-KEY: 55c7b019ea4b3d546425d4a8d24c3a8dcf00a449eb1da51f5f2d0f080e6cc883" \
  -H "Content-Type: application/json" \
  -d '{"vault":"test-vault", "allVersions": false}' \
  "http://localhost:8484/api/vault/retrieve"
```
Output
```shell
{"requestId":1}
```
The `requestId` is used to the see the state of the request and its job IDs.

#### See the state of a DIP request
```shell
curl -H "X-API-KEY: 55c7b019ea4b3d546425d4a8d24c3a8dcf00a449eb1da51f5f2d0f080e6cc883" \
  "http://localhost:8484/api/vault/retrieve/1"
```
The `requestId` number is appended to the end of the retrieve path.  
Output
```shell
{
  "username":"service-admin-1",
  "vault":"test-vault",
  "allVersions":false,
  "externalObjectIds":null,
  "deleted":false,
  "createdTimestamp":"2024-06-04T21:39:17.32287Z",
  "deletedTimestamp":null,
  "parts":[
    {
      "jobId":4,
      "state":"READY",
      "lastDownloadedTimestamp":null
    }
  ]
}
```
The `jobId`s will be used to retrieve the DIP files (one zip file per job). The job's `state` will be `READY` when the
DIP file is ready to download.

#### Download a DIP file
```shell
curl -H "X-API-KEY: 55c7b019ea4b3d546425d4a8d24c3a8dcf00a449eb1da51f5f2d0f080e6cc883" \
  -OJ "http://localhost:8484/api/vault/retrieve/download/4"
```
The `jobId` shown in the state of the DIP request is used as the download number at the end of the path.  
Output
```shell
  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
100  6466  100  6466    0     0  1311k      0 --:--:-- --:--:-- --:--:-- 1578k
curl: Saved to filename 'dip-4.zip'
```

## AWS

The local version **will not** connect to AWS S3, and any replication attempts will fail. If you need to connect
to AWS S3, you must set the following properties in the worker [application-default.properties](worker/src/main/resources/config/application-default.properties).

```
app.aws.access-key=
app.aws.secret-key=
app.aws.s3.bucket=
```

They can be found at the bottom of the file, and changes to them **must not** be committed.
