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
public class MarketStatusPerBourseAccountDto {
    private String date;
    private String time;
    private String groupName;
    private List<MarketStatusDetailDto> enteredMoney;
    private List<MarketStatusDetailDto> outMoney;
}
