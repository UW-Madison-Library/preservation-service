package edu.wisc.library.sdg.preservation.worker.itest;

import edu.wisc.library.sdg.preservation.manager.client.internal.model.FileEncoding;

public class TestEncoding {

    String source;
    String encoding;

    public TestEncoding(String source, String encoding) {
        this.source = source;
        this.encoding = encoding;
    }

    FileEncoding asIngestFileEncoding() {
        return new FileEncoding()
                .source(source)
                .encoding(encoding);
    }

}
