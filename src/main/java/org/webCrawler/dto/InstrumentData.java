package org.webCrawler.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InstrumentData {
    String bourseAccount;
    String date;
    Long unit;
    Double volume;
    Double value;
    Long yesterdayPrice;
    Long firstPrice;
    Long lastPrice;
    Long finalPrice;
    Long minPrice;
    Long maxPrice;
}
