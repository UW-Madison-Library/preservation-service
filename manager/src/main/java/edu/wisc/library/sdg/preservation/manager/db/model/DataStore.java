package edu.wisc.library.sdg.preservation.manager.db.model;

import java.util.Set;

public enum DataStore implements EnumAsShort {

    IBM_COS(1),
    GLACIER(2);

    /**
     * The local data store
     */
    public static final DataStore LOCAL = IBM_COS;

    /**
     * Set of all of the remote data stores
     */
    public static final Set<DataStore> REMOTES = Set.of(GLACIER);

    private final short intValue;

    DataStore(int intValue) {
        this.intValue = (short) intValue;
    }

    @Override
    public short asShort() {
        return intValue;
    }
}
