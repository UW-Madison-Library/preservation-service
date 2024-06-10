package edu.wisc.library.sdg.preservation.manager.itest;

public class TestValidity {

    String source;
    Boolean valid;
    Boolean wellFormed;

    public TestValidity(String source, Boolean valid, Boolean wellFormed) {
        this.source = source;
        this.valid = valid;
        this.wellFormed = wellFormed;
    }

}
