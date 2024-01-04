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
public class ExtraAssemblyDto {
    private String link;
    private String bourseAccount;
    private String company;
    private String meetingDate;
    private List<PeoplePrsentInMeeting> peoplePrsentInMeetingList;
    private List<DecisionsMade> decisionsMades;
}
