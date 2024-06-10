package edu.wisc.library.sdg.preservation.manager.db.model;

public enum LogLevel implements EnumAsShort {

    INFO(1),
    WARN(2),
    ERROR(3);

    private final short intValue;

    LogLevel(int intValue) {
        this.intValue = (short) intValue;
    }

    @Override
    public short asShort() {
        return intValue;
    }

}
