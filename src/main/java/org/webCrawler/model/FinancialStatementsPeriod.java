package org.webCrawler.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Table(name = "[financial_statements_period]")
@Entity(name = "financial_statements_period")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FinancialStatementsPeriod {
    @Id
    private Long id;
    @Column(columnDefinition = "NVARCHAR(50)")
    private String name;
}
