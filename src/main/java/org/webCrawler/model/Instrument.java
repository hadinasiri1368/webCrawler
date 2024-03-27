package org.webCrawler.model;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "[instrument]")
@Entity(name = "instrument")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Instrument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "nvarchar(12)", name = "ins_max_lcode")
    private String insMaxLcode;
    @Column(columnDefinition = "nvarchar(5)", name = "ins_min_lcode")
    private String insMinLcode;
    @Column(columnDefinition = "NVARCHAR(50)", name = "latin_name")
    private String latinName;
    @Column(columnDefinition = "nvarchar(4)", name = "four_digit_company_code")
    private String fourDigitCompanyCode;
    @Column(columnDefinition = "NVARCHAR(100)", name = "company_name")
    private String companyName;
    @Column(columnDefinition = "NVARCHAR(50)", name = "instrument_name")
    private String instrumentName;
    @Column(columnDefinition = "nvarchar(30)", name = "instrument_name_thirty")
    private String instrumentNameThirty;
    @Column(columnDefinition = "NVARCHAR(50)", name = "isin_code")
    private String isinCode;
    @Column(columnDefinition = "nvarchar(50)", name = "market")
    private String market;
    @Column(columnDefinition = "tinyint", name = "board_code")
    private Long boardCode;
    @Column(columnDefinition = "decimal(18, 0)", name = "f_industry_id")
    private Long industryId;
    @Column(columnDefinition = "int", name = "industry_subgroup_code")
    private Long industrySubgroupCode;
    @Column(columnDefinition = "nvarchar(100)", name = "industry_subgroup_name")
    private String industrySubgroupName;
    @Column(columnDefinition = "nvarchar(100)", name = "tsetms_id")
    private String tsetmsId;
    @Column(columnDefinition = "BIT", name = "is_deleted")
    private Boolean isDeleted;

}
