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
public class MarketStatusDto {
    private String date;
    private String time;
    private List<MarketStatusDetailDto> enteredMoney;
    private List<MarketStatusDetailDto> outMoney;
    private List<MarketStatusDetailDto> companyEnteredMoney;
    private List<MarketStatusDetailDto> companyOutMoney;
}