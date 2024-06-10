package edu.wisc.library.sdg.preservation.manager.db.model;

/**
 * Used to map enums to ints when they're written/read from the db. The enums MUST also be declared in JdbcConfig in
 * order for them to be mapped correctly. This class requires that the values are 1 based, but the values do not need
 * to be consecutive.
 */
public interface EnumAsShort {

    /**
     * @return the int value of the enum
     */
    short asShort();

}
