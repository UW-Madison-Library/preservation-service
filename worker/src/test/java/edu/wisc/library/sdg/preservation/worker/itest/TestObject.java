package edu.wisc.library.sdg.preservation.worker.itest;

import com.google.common.net.UrlEscapers;
import edu.wisc.library.sdg.preservation.common.util.UuidUtils;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.BatchObjectWithFiles;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.IngestObjectState;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ObjectInfo;

import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class TestObject {

    private static final AtomicLong SERIAL = new AtomicLong(1L);

    Long ingestId;
    String externalId;
    String internalObjectId;
    String headPersistenceVersion;
    int version;
    Long ingestObjectId;
    Long objectVersionId;
    List<TestFile> files;
    String approverName;
    String approverAddress;

    TestObject(Long ingestId, String externalId, int version, List<TestFile> files) {
        this.ingestId = ingestId;
        this.externalId = externalId;
        this.version = version;
        ingestObjectId = SERIAL.getAndIncrement();
        objectVersionId = SERIAL.getAndIncrement();
        this.files = files;
    }

    BatchObjectWithFiles asBatchObject() {
        return new BatchObjectWithFiles()
                .ingestId(ingestId)
                .ingestObjectId(ingestObjectId)
                .externalObjectId(externalId)
                .internalObjectId(internalObjectId)
                .headPersistenceVersion(headPersistenceVersion)
                .state(IngestObjectState.PENDING_INGESTION)
                .approverName(approverName)
                .approverAddress(approverAddress)
                .objectRootPath(objectRootPath(ingestId, externalId))
                .files(files.stream().map(TestFile::asBatchObjectFile).collect(Collectors.toList()));
    }

    private String objectRootPath(Long ingestId, String objectId) {
        return Paths.get("target/work", "batch-" + ingestId, "source/bag/data",
                UrlEscapers.urlFormParameterEscaper().escape(objectId)).toAbsolutePath().toString();
    }

    public ObjectInfo asObjectInfo() {
        return new ObjectInfo()
                .internalId(UuidUtils.withPrefix(internalObjectId))
                .externalId(externalId)
                .version(version)
                .persistenceVersion("v" + version);
    }

    public String unprefixedInternalObjectId() {
        return UuidUtils.withoutPrefix(internalObjectId);
    }

    public TestFile file(String path) {
        for (var file : files) {
            if (Objects.equals(path , file.path)) {
                return file;
            }
        }
        throw new RuntimeException("File not found: " + path);
    }

}
