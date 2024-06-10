package edu.wisc.library.sdg.preservation.manager.itest;

import com.google.common.hash.Hashing;
import edu.wisc.library.sdg.preservation.manager.db.model.FormatRegistry;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TestFile {

    Long ingestFileId;
    String path;
    String digest;
    long size;
    List<TestFormat> formats;
    List<TestEncoding> encodings;
    List<TestValidity> validity;

    TestFile(String path, long size) {
        this.path = path;
        this.size = size;
        this.digest = Hashing.sha256().hashString(path+size, StandardCharsets.UTF_8).toString();
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

    @Override
    public String toString() {
        return "TestFile{" +
                "path='" + path + '\'' +
                ", digest='" + digest + '\'' +
                ", size=" + size +
                '}';
    }
}
