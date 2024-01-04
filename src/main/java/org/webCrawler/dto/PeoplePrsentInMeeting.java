package org.webCrawler.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PeoplePrsentInMeeting {
    private String shareholderName;
    private Long unitCount;
    private Float sharePercent;
}
