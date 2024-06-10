package edu.wisc.library.sdg.preservation.worker.itest;

import edu.wisc.library.sdg.preservation.manager.client.internal.model.FileFormat;
import edu.wisc.library.sdg.preservation.manager.client.internal.model.FormatRegistry;

public class TestFormat {

    FormatRegistry registry;
    String source;
    String format;

    public TestFormat(FormatRegistry registry, String source, String format) {
        this.registry = registry;
        this.source = source;
        this.format = format;
    }

    FileFormat asIngestFileFormat() {
        return new FileFormat()
                .formatRegistry(registry)
                .source(source)
                .format(format);
    }

}
