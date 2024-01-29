package org.webCrawler.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.naming.Name;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InstrumentDto {
    private String bourseAccount;
    private String name;
    private String groupName;
}
