package edu.wisc.library.sdg.preservation.manager.db.model;

import java.util.Set;

public enum OrgRole implements EnumAsShort {

    STANDARD(1, Set.of(
            OrgAuthority.BASIC_OPS
    )),
    ADMIN(2, Set.of(
            OrgAuthority.BASIC_OPS,
            OrgAuthority.ADMIN_OPS
    ));

    private final Set<OrgAuthority> authorities;
    private final short intValue;

    OrgRole(int intValue, Set<OrgAuthority> authorities) {
        this.intValue = (short) intValue;
        this.authorities = authorities;
    }

    @Override
    public short asShort() {
        return intValue;
    }

    public Set<OrgAuthority> getAuthorities() {
        return authorities;
    }

    public boolean hasAuthority(OrgAuthority authority) {
        return authorities.contains(authority);
    }

    public static OrgRole fromShort(short v) {
        for (var value : values()) {
            if (v == value.intValue) {
                return value;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + v);
    }

}
