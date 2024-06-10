package edu.wisc.library.sdg.preservation.manager.itest;

import edu.wisc.library.sdg.preservation.manager.db.model.FormatRegistry;

public class TestFormat {

    FormatRegistry registry;
    String source;
    String format;

    public TestFormat(FormatRegistry registry, String source, String format) {
        this.registry = registry;
        this.source = source;
        this.format = format;
    }

}
