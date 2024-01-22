package org.webCrawler.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "[codal_shareholder_meeting]")
@Entity(name = "codalShareholderMeeting")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CodalShareholderMeeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "f_letter_type_id")
    private LetterType letterType;
    @ManyToOne
    @JoinColumn(name = "f_meeting_type_id")
    private MeetingType meetingType;
    @ManyToOne
    @JoinColumn(name = "f_meeting_status_id")
    private MeetingStatus meetingStatus;
    @Column(columnDefinition = "NVARCHAR(50)", name = "bourse_account")
    private String bourseAccount;
    @Column(columnDefinition = "NVARCHAR(10)", name = "meeting_date")
    private String meetingDate;
    @Column(columnDefinition = "BIGINT", name = "share_profit")
    private Long shareProfit;
    @Column(columnDefinition = "BIGINT", name = "last_asset_amount")
    private Long lastAssetAmount;
    @Column(columnDefinition = "BIGINT", name = "ipo_recap_amount")
    private Long ipoRecapAmount;
    @Column(columnDefinition = "BIGINT", name = "gift_recap_amount")
    private Long giftRecapAmount;
    @Column(columnDefinition = "BIGINT", name = "saved_recap")
    private Long savedRecap;
    @Column(columnDefinition = "BIGINT", name = "extra_re_erval_recap")
    private Long extraReErvalRecap;
    @Column(columnDefinition = "BIGINT", name = "stock_difference_recap")
    private Long stockDifferenceRecap;
    @Column(columnDefinition = "BIGINT", name = "deprivation_right")
    private Long deprivationRight;
    @Column(columnDefinition = "DECIMAL(18, 6)", name = "gift_recap_percent")
    private Double giftRecapPercent;
    @Column(columnDefinition = "DECIMAL(18, 6)", name = "ipo_recap_percent")
    private Double ipoRecapPercent;
    @Column(columnDefinition = "NVARCHAR(10)", name = "ipo_date")
    private String ipoDate;
    @Column(columnDefinition = "NVARCHAR(10)", name = "renewed_ipo_date")
    private String renewedIpoDate;
    @Column(columnDefinition = "NVARCHAR(10)", name = "issue_date")
    private String issueDate;
    @Column(columnDefinition = "NVARCHAR(10)", name = "confirmDate")
    private String confirmDate;
    @Column(columnDefinition = "NVARCHAR(500)")
    private String url;
}
