package edu.wisc.library.sdg.preservation.worker.validation.lambda;

public class GlacierValidationResponse {

    private Validity validity;
    private String reason;

    public Validity getValidity() {
        return validity;
    }

    public GlacierValidationResponse setValidity(Validity validity) {
        this.validity = validity;
        return this;
    }

    public String getReason() {
        return reason;
    }

    public GlacierValidationResponse setReason(String reason) {
        this.reason = reason;
        return this;
    }

    @Override
    public String toString() {
        return "GlacierValidationResponse{" +
                "validity=" + validity +
                ", reason='" + reason + '\'' +
                '}';
    }
}
