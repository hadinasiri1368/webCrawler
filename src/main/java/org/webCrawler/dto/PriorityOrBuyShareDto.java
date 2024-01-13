package org.webCrawler.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PriorityOrBuyShareDto {
    private String link;
    private String licenseNumber;
    private String advertisementDate;
    private String meetingDate;
    private String proceedingsDate;
    private Long fromAmount;
    private Long toAmount;
    private String type;
    private String startDate;
    private String endDate;
    private String confirmDate;
}
