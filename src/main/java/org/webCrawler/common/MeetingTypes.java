package org.webCrawler.common;

public enum MeetingTypes {
    EXTRAASSEMBLY_SAHEHOLDER_MEETING(1l, "فوق العاده"),
    ANNUAL_SAHEHOLDER_MEETING(1l, "عادی")
    ;

    MeetingTypes(Long value, String name) {
        this.value = value;
        this.name = name;
    }

    private final Long value;
    private final String name;

    public Long getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
