package org.webCrawler.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DecisionsBoard {
    private Long unitCount;
    private Long nominalValue;
    private Long amount;
    private Long cash;
    private Long accumulatedProfit;
    private Long savedUp;
    private Long asset;
    private Long stock;
    private Long entryCash;
    private Long capitalIncrease;
    private Long percentIncrease;
    private Long unitCountNotRegistered;
    private Long nominalValueNotRegistered;
    private Long amountNotRegistered;
}
