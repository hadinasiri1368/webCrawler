package org.webCrawler.common;

public enum MeetingStatuses {
    NOT_HAVE_TABLE(1l, "جدول ندارد"),
    ZERO_SHAREHOLDER_MEETING(1l, "سود مجمع صفر است"),
    TRUE_SHAREHOLDER_MEETING(3l, "صحیح")
    ;

    MeetingStatuses(Long value, String name) {
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
