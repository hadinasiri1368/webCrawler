package org.webCrawler.model;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "[industry_column]")
@Entity(name = "industry_column")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IndustryColumn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "decimal(18, 0)" , name="f_industry_id")
    private Long industryId;
    @Column(columnDefinition = "decimal(18, 0)" , name="f_column_id")
    private Long columnId;
    @Column(columnDefinition = "decimal(18, 0)" , name = "f_financial_statements_sheet_id")
    private Long financialStatementsSheetId;
}
