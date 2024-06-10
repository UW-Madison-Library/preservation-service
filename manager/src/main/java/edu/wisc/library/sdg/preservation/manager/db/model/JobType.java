package edu.wisc.library.sdg.preservation.manager.db.model;

public enum JobType implements EnumAsShort {

    RETRIEVE_OBJECTS(1),
    REPLICATE(2),
    RESTORE(3),
    FINALIZE_RESTORE(4),
    VALIDATE_LOCAL(5),
    PROCESS_BATCH(6),
    VALIDATE_REMOTE(7),
    DELETE_DIP(8),
    CLEANUP_SIPS(9);

    private final short intValue;

    JobType(int intValue) {
        this.intValue = (short) intValue;
    }

    public static JobType fromShort(short v) {
        for (var value : values()) {
            if (v == value.intValue) {
                return value;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + v);
    }

    @Override
    public short asShort() {
        return intValue;
    }

}
