package org.webCrawler.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InstrumentInfo {
    private String bourseAccount;
    private String date;
    private String market;
    private String groupName;
    private Long stockCount;
    private Long floatingStock;
    private Long baseVolume;
    private Long profit;
    private Double value;
    private Double groupValue;
    private Long purchaseCount;
    private Long purchasePercent;
    private Long saleCount;
    private Long salePercent;
    private Long companyPurchaseCount;
    private Long companyPurchasePercent;
    private Long companySaleCount;
    private Long companySalePercent;
    private Long changeOwnership;
    private Long withdrawalMoney;
    private Double powerSellers;
    private Long purchasePerCapita;
    private Long salePerCapita;
}
