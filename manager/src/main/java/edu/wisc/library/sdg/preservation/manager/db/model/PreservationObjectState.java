package edu.wisc.library.sdg.preservation.manager.db.model;

public enum PreservationObjectState implements EnumAsShort {

    ACTIVE(1),
    DELETED(2);

    private final short intValue;

    PreservationObjectState(int intValue) {
        this.intValue = (short) intValue;
    }

    @Override
    public short asShort() {
        return intValue;
    }

}
