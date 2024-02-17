package org.webCrawler.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Table(name = "[financial_statements_sheet]")
@Entity(name = "financial_statements_sheet")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FinancialStatementsSheet {
    @Id
    private Long id;
    @Column(columnDefinition = "nvarchar(100)")
    private String name;
    @Column(columnDefinition = "nvarchar(100)")
    private String name_farsi;
}
