package org.webCrawler.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InstrumentDto {
    private String bourseAccount;
    private String name;
    private String groupName;
    private String instrumentLink;
    private String isDeleted;
}
