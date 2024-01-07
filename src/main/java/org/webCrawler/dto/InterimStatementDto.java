package org.webCrawler.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InterimStatementDto {
    private String link;
    private String company;
    private String bourseAccount;
    private String ISICCode;
    private String date;
    private Long RegisteredCapital;
    private Long notRegisteredCapital;
    private String publisherStatus;
    private String financialStatementStatus;
    private String period;
    private List<BalanceSheet> balanceSheets;
    private List<BalanceSheet> profitAndStatement;
    private List<BalanceSheet> cashFlow;
}
