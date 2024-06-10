package edu.wisc.library.sdg.preservation.manager.db.model;

public enum EventOutcome implements EnumAsShort {

    SUCCESS(1, "success"),
    FAILURE(2, "fail"),
    SUCCESS_WITH_WARNINGS(3, "warning"),
    APPROVED(4, "approved"),
    REJECTED(5, "rejected"),
    NOT_EXECUTED(6, "not executed");

    private final String premisOutcome;
    private final short intValue;

    EventOutcome(int intValue, String premisOutcome) {
        this.intValue = (short) intValue;
        this.premisOutcome = premisOutcome;
    }

    @Override
    public short asShort() {
        return intValue;
    }

    public String getPremisOutcome() {
        return premisOutcome;
    }
}
