package edu.wisc.library.sdg.preservation.manager.premis;

public enum PremisEventType {

    TRANSFER("http://id.loc.gov/vocabulary/preservation/eventType/tra", "transfer"),
    VIRUS_CHECK("http://id.loc.gov/vocabulary/preservation/eventType/vir", "virus check"),
    VALIDATION("http://id.loc.gov/vocabulary/preservation/eventType/val", "validation"),
    UNPACKING("http://id.loc.gov/vocabulary/preservation/eventType/unp", "unpacking"),
    FORMAT_IDENTIFICATION("http://id.loc.gov/vocabulary/preservation/eventType/for", "format identification"),
    APPRAISAL("http://id.loc.gov/vocabulary/preservation/eventType/app", "appraisal"),
    ACCESSION("http://id.loc.gov/vocabulary/preservation/eventType/acc", "accession"),
    CREATION("http://id.loc.gov/vocabulary/preservation/eventType/cre", "creation"),
    MODIFICATION("http://id.loc.gov/vocabulary/preservation/eventType/mod", "modification"),
    METADATA_MODIFICATION("http://id.loc.gov/vocabulary/preservation/eventType/mem", "metadata modification"),
    REPLICATION("http://id.loc.gov/vocabulary/preservation/eventType/rep", "replication"),
    INGESTION_END("http://id.loc.gov/vocabulary/preservation/eventType/ine", "ingestion end"),
    RECOVERY("http://id.loc.gov/vocabulary/preservation/eventType/rec", "recovery"),
    INFORMATION_PACKAGE_CREATION("http://id.loc.gov/vocabulary/preservation/eventType/ipc", "information package creation"),
    DISSEMINATION("http://id.loc.gov/vocabulary/preservation/eventType/dis", "dissemination"),
    DELETION("http://id.loc.gov/vocabulary/preservation/eventType/del", "deletion");

    private final String uri;
    private final String name;

    PremisEventType(String uri, String name) {
        this.uri = uri;
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public String getName() {
        return name;
    }
}
