package edu.wisc.library.sdg.preservation.manager.db.model;

public enum StorageProblemType implements EnumAsShort {

    MISSING(1),
    CORRUPT(2),
    NONE(3);

    private final short intValue;

    StorageProblemType(int intValue) {
        this.intValue = (short) intValue;
    }

    @Override
    public short asShort() {
        return intValue;
    }

}
