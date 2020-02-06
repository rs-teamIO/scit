package com.scit.xml.model.paper;

public enum PaperStatus {

    SUBMITTED("submitted"),
    IN_REVIEW("in_review"),
    REVIEWED("reviewed"),
    REVOKED("revoked"),
    REVISION("revision"),
    REJECTED("rejected"),
    PUBLISHED("published");

    private String name;

    PaperStatus(String name) { this.name = name; }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return getName().toUpperCase();
    }
}
