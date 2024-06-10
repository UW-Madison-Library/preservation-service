package edu.wisc.library.sdg.preservation.worker.itest;

import edu.wisc.library.sdg.preservation.manager.client.internal.model.FileValidity;

public class TestValidity {

    String source;
    Boolean valid;
    Boolean wellFormed;

    public TestValidity(String source, Boolean valid, Boolean wellFormed) {
        this.source = source;
        this.valid = valid;
        this.wellFormed = wellFormed;
    }

    FileValidity asIngestFileValidity() {
        return new FileValidity()
                .source(source)
                .valid(valid)
                .wellFormed(wellFormed);
    }

}
