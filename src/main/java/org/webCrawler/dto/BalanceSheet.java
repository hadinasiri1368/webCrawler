package org.webCrawler.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BalanceSheet {
    private String description;
    private Long actualPerformance;
    private Long endOfOldYear;
    private Long changePercent;
    private String description2;
    private Long actualPerformance2;
    private Long endOfOldYear2;
    private Long changePercent2;
}
