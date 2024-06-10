package edu.wisc.library.sdg.preservation.manager.db.model;

public enum Permission implements EnumAsShort {

    READ(1),
    WRITE(2);

    private final short intValue;

    Permission(int intValue) {
        this.intValue = (short) intValue;
    }

    @Override
    public short asShort() {
        return intValue;
    }

    public static Permission fromShort(short v) {
        for (var value : values()) {
            if (v == value.intValue) {
                return value;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + v);
    }

}
