package edu.wisc.library.sdg.preservation.manager.db.model;

import edu.wisc.library.sdg.preservation.manager.premis.PremisEventType;

public enum EventType implements EnumAsShort {

    RECEIVE_BAG(1, PremisEventType.TRANSFER),
    VIRUS_SCAN_BAG(2, PremisEventType.VIRUS_CHECK),
    VALIDATE_BAG(3, PremisEventType.VALIDATION),
    IDENTIFY_OBJ(4, PremisEventType.UNPACKING),
    IDENTIFY_FILE_FORMAT(5, PremisEventType.FORMAT_IDENTIFICATION),
    REVIEW_OBJ(6, PremisEventType.APPRAISAL),
    REVIEW_BATCH(7, PremisEventType.APPRAISAL),
    WRITE_OBJ_LOCAL(8, PremisEventType.ACCESSION),
    CREATE_OBJ(9, PremisEventType.CREATION),
    UPDATE_OBJ(10, PremisEventType.MODIFICATION),
    UPDATE_OBJ_METADATA(11, PremisEventType.METADATA_MODIFICATION),
    REPLICATE_OBJ_VERSION(12, PremisEventType.REPLICATION),
    COMPLETE_OBJ_INGEST(13, PremisEventType.INGESTION_END),
    COMPLETE_BATCH_INGEST(14, PremisEventType.INGESTION_END),
    DELETE_BAG(15, PremisEventType.INGESTION_END),

    VALIDATE_OBJ_LOCAL(16, PremisEventType.VALIDATION),
    VALIDATE_OBJ_VERSION_LOCAL(17, PremisEventType.VALIDATION),
    VALIDATE_OBJ_VERSION_REMOTE(18, PremisEventType.VALIDATION),
    RESTORE_OBJ_VERSION(19, PremisEventType.RECOVERY),
    PREPARE_DIP(20, PremisEventType.INFORMATION_PACKAGE_CREATION),
    DELETE_OBJ(21, PremisEventType.DELETION);

    private final PremisEventType premisEvent;
    private final short intValue;

    EventType(int intValue, PremisEventType premisEvent) {
        this.intValue = (short) intValue;
        this.premisEvent = premisEvent;
    }

    @Override
    public short asShort() {
        return intValue;
    }

    public PremisEventType getPremisEventType() {
        return premisEvent;
    }

}
