package org.webCrawler.common;

public enum FinancialStatementsSheet {
    INCOME_STATEMENT(1L, "صورت سود و زیان"),
    FINANCIAL_STATEMENTS(2L, "صورت وضعیت مالی"),
    COMPREHENSIVE_INCOME_STATEMENT(3L, "صورت سود و زيان جامع"),
    STATEMENT_OF_CASH_FLOWS(4L, "جریان وجوه نقد"),
    BALANCE_SHEET(5L,"ترازنامه");


    private final Long financialStatementsSheetValue;
    private final String financialStatementsSheetName;

    FinancialStatementsSheet(Long financialStatementsSheetValue, String financialStatementsSheetName) {
        this.financialStatementsSheetValue = financialStatementsSheetValue;
        this.financialStatementsSheetName = financialStatementsSheetName;
    }

    public Long getLettersTypeValue() {
        return financialStatementsSheetValue;
    }

    public String getLettersTypeName() {
        return financialStatementsSheetName;
    }
}
