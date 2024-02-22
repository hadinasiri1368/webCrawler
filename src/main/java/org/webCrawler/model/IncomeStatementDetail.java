package org.webCrawler.model;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "[income_statement_detail]")
@Entity(name = "income_statement_detail")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IncomeStatementDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "decimal(18, 0)", name = "f_income_statement_id")
    private Long incomeStatementId;
    @Column(columnDefinition = "decimal(18, 0)" , name = "f_industry_column_id")
    private Long industryColumnId;
    @Column(columnDefinition = "decimal(18, 6)")
    private Long value;
}
