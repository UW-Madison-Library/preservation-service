package edu.wisc.library.sdg.preservation.manager.itest;

import edu.wisc.library.sdg.preservation.common.util.UuidUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class TestObject {
    String externalId;
    UUID internalId;
    Long ingestObjectId;
    int version;
    String persistenceVersion;
    Long objectVersionId;
    List<TestFile> files;

    TestObject(String externalId) {
        this(externalId, new ArrayList<>());
    }

    TestObject(String externalId, List<TestFile> files) {
        this.externalId = externalId;
        this.internalId = UUID.randomUUID();
        version = 1;
        persistenceVersion = "v1";
        this.files = files;
    }

    TestObject addFile(String path, long size) {
        files.add(new TestFile(path, size));
        return this;
    }

    TestObject addFileWithFormat(String path,
                                 long size,
                                 TestFormat... formats) {
        var file = new TestFile(path, size);
        file.formats.addAll(Arrays.asList(formats));
        files.add(file);
        return this;
    }

    TestObject version(int version) {
        this.version = version;
        this.persistenceVersion = "v" + version;
        return this;
    }

    String prefixedInternalId() {
        return UuidUtils.withPrefix(internalId);
    }

    String unprefixedInternalId() {
        return internalId.toString();
    }

    @Override
    public String toString() {
        return "TestObject{" +
                "externalId='" + externalId + '\'' +
                ", internalId='" + internalId + '\'' +
                ", objectIngestId=" + ingestObjectId +
                ", version=" + version +
                ", persistenceVersion='" + persistenceVersion + '\'' +
                ", objectVersionId='" + objectVersionId + '\'' +
                ", files=" + files +
                '}';
    }
}
