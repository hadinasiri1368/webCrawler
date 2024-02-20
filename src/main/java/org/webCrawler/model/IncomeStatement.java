package org.webCrawler.model;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "[income_statement]")
@Entity(name = "income_statement")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IncomeStatement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "decimal(18, 0)", name="f_instrumen_id")
    private Long instrumenID;
    @Column(columnDefinition = "NVARCHAR(50)" , name = "fiscal_year")
    private String fiscalYear;
    @Column(columnDefinition = "varchar(10)",name = "f_financial_statements_period_id")
    private Long financialStatementsPeriodId;
    @Column(columnDefinition = "decimal(18, 0)", name = "f_industry_id")
    private Long industryId;
    @Column(columnDefinition = "bit",name ="is_audited")
    private Boolean isAudited;
    @Column(columnDefinition = "varchar(10)", name="end_to")
    private String endTo;

}
