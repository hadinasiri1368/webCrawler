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
public class DecisionDto {
    private String link;
    private String bourseAccount;
    private String meetingDate;
    private List<AssemblyDecisions> assemblyDecisions;
}
