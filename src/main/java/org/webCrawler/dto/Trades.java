package org.webCrawler.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Trades {
    String bourseAccount;
    String date;
    String time;
    Long volume;
    Long price;
    Boolean isCancel;
}
