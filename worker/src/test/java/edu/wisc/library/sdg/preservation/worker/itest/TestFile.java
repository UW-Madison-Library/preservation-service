package edu.wisc.library.sdg.preservation.worker.itest;

import edu.wisc.library.sdg.preservation.manager.client.internal.model.FormatRegistry;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.ObjectFile;

import java.util.ArrayList;
import java.util.List;

public class TestFile {

    Long ingestFileId;
    String path;
    String digest;
    Long size;
    List<TestFormat> formats;
    List<TestEncoding> encodings;
    List<TestValidity> validity;

    TestFile(String path, String digest, Long size) {
        this.path = path;
        this.digest = digest;
        this.size = size;
        this.formats = new ArrayList<>();
        this.encodings = new ArrayList<>();
        this.validity = new ArrayList<>();
    }

    TestFile withFormat(FormatRegistry registry, String source, String format) {
        formats.add(new TestFormat(registry, source, format));
        return this;
    }

    TestFile withEncoding(String source, String encoding) {
        encodings.add(new TestEncoding(source, encoding));
        return this;
    }

    TestFile withValidity(String source, Boolean valid, Boolean wellFormed) {
        validity.add(new TestValidity(source, valid, wellFormed));
        return this;
    }

    ObjectFile asBatchObjectFile() {
        return new ObjectFile()
                .filePath(path)
                .sha256Digest(digest)
                .fileSize(size);
    }



}
