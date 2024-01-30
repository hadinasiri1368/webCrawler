package org.webCrawler.common;

public enum LettersTypes {
    ANNUAL_SAHEHOLDER_MEETING(20l, "تصمیمات مجمع عمومی عادی سالیانه"),
    EXTRAORDINARY_ANNUAL_SAHEHOLDER_MEETING(21l, "تصمیمات مجمع عمومي عادي بطور فوق العاده"),
    SUMMARY_ANNUAL_SAHEHOLDER_MEETING(2020l, "خلاصه تصمیمات مجمع عمومی عادی سالیانه"),
    SUMMARY_EXTRAORDINARY_ANNUAL_SAHEHOLDER_MEETING(2121l, "خلاصه تصمیمات مجمع عمومي عادي بطور فوق العاده"),
    EXTRAASSEMBLY_SAHEHOLDER_MEETING(22l, "تصمیمات مجمع عمومی فوق العاده"),
    SUMMARY_EXTRAASSEMBLY_SAHEHOLDER_MEETING(2222l, "خلاصه تصمیمات مجمع عمومی فوق العاده"),
    CAPITAL_INCREASE_SAHEHOLDER_MEETING(24l, "تصمیمات هیئت مدیره در خصوص افزایش سرمایه"),
    PRIORITYTIME_SAHEHOLDER_MEETING(25l, "مهلت استفاده از حق تقدم خرید سهام"),
    POSTULATEDISCUSSION_SAHEHOLDER_MEETING(27l, "اعلامیه پذیره نویسی عمومی"),
    CAPITAL_INCREASE_REGISTRATION(28L, "آگهی ثبت افزایش سرمایه");


    private final Long lettersTypeValue;
    private final String lettersTypeName;

    LettersTypes(Long lettersTypeValue, String lettersTypeName) {
        this.lettersTypeValue = lettersTypeValue;
        this.lettersTypeName = lettersTypeName;
    }

    public Long getLettersTypeValue() {
        return lettersTypeValue;
    }

    public String getLettersTypeName() {
        return lettersTypeName;
    }
}
