package edu.wisc.library.sdg.preservation.manager.db.model;

public enum FormatRegistry implements EnumAsShort {

    MIME(1),
    PRONOM(2);

    private final short intValue;

    FormatRegistry(int intValue) {
        this.intValue = (short) intValue;
    }

    @Override
    public short asShort() {
        return intValue;
    }

}
