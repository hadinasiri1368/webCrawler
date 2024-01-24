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
public class CapitalIncreaseDto {
    private String link;
    private String bourseAccount;
    private String company;
    private String meetingDate;
    private List<DecisionsBoard> decisionsBoards;
}
