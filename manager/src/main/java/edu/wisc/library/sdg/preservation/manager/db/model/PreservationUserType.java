package edu.wisc.library.sdg.preservation.manager.db.model;

public enum PreservationUserType implements EnumAsShort {

    /**
     * A non-human user, such as the user that moves objects from Fedora to the preservation system
     */
    PROGRAMMATIC_USER(1),
    /**
     * A programmatic user that has the ability to proxy requests as any other user
     */
    PROXYING_USER(2),
    /**
     * A regular, human user
     */
    GENERAL_USER(3),
    /**
     * A user with service admin privileges
     */
    SERVICE_ADMIN(4);

    private final short intValue;

    PreservationUserType(int intValue) {
        this.intValue = (short) intValue;
    }

    @Override
    public short asShort() {
        return intValue;
    }

    public static PreservationUserType fromShort(short v) {
        for (var value : values()) {
            if (v == value.intValue) {
                return value;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + v);
    }

}
