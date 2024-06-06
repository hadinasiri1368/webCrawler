package org.webCrawler.common;

import lombok.Getter;

@Getter
public enum LettersTypes {
    ANNUAL_SAHEHOLDER_MEETING(20L, "تصمیمات مجمع عمومی عادی سالیانه"),
    EXTRAORDINARY_ANNUAL_SAHEHOLDER_MEETING(21L, "تصمیمات مجمع عمومي عادي بطور فوق العاده"),
    SUMMARY_ANNUAL_SAHEHOLDER_MEETING(2020L, "خلاصه تصمیمات مجمع عمومی عادی سالیانه"),
    SUMMARY_EXTRAORDINARY_ANNUAL_SAHEHOLDER_MEETING(2121L, "خلاصه تصمیمات مجمع عمومي عادي بطور فوق العاده"),
    EXTRAASSEMBLY_SAHEHOLDER_MEETING(22L, "تصمیمات مجمع عمومی فوق العاده"),
    SUMMARY_EXTRAASSEMBLY_SAHEHOLDER_MEETING(2222L, "خلاصه تصمیمات مجمع عمومی فوق العاده"),
    CAPITAL_INCREASE_SAHEHOLDER_MEETING(24L, "تصمیمات هیئت مدیره در خصوص افزایش سرمایه"),
    PRIORITYTIME_SAHEHOLDER_MEETING(25L, "مهلت استفاده از حق تقدم خرید سهام"),
    POSTULATEDISCUSSION_SAHEHOLDER_MEETING(27L, "اعلامیه پذیره نویسی عمومی"),
    CAPITAL_INCREASE_REGISTRATION(28L, "آگهی ثبت افزایش سرمایه"),
    INFORMATION_AND_INTERIM_FINANCIAL_STATEMENTS(6L, "اطلاعات و صورتهای مالی میاندوره ای");


    private final Long lettersTypeValue;
    private final String lettersTypeName;

    LettersTypes(Long lettersTypeValue, String lettersTypeName) {
        this.lettersTypeValue = lettersTypeValue;
        this.lettersTypeName = lettersTypeName;
    }

}
