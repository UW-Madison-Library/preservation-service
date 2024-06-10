package edu.wisc.library.sdg.preservation.worker.tools.fits.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ToolStatus {

    DID_NOT_RUN("did not run"),
    FAILED("failed");

    private final String value;

    private ToolStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

}
