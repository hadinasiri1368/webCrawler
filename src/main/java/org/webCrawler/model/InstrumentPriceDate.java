package org.webCrawler.model;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "[instrument_price_date]")
@Entity(name = "instrumentPriceDate")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InstrumentPriceDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "f_instrument_id")
    private Instrument instrument;
    @Column(columnDefinition = "NVARCHAR(10)", name = "price_date")
    private String priceDate;
    @Column(columnDefinition = "BIGINT", name = "first_price")
    private Long firstPrice;
    @Column(columnDefinition = "BIGINT", name = "last_trade_price")
    private Long lastTradePrice;
    @Column(columnDefinition = "BIGINT", name = "lowest_price")
    private Long lowestPrice;
    @Column(columnDefinition = "BIGINT", name = "highest_price")
    private Long highestPrice;
    @Column(columnDefinition = "BIGINT", name = "last_day_price")
    private Long lastDayPrice;
    @Column(columnDefinition = "BIGINT", name = "value")
    private Double value;
    @Column(columnDefinition = "BIGINT", name = "value_price")
    private Double valuePrice;
    @Column(columnDefinition = "BIGINT", name = "number_of_transactions")
    private Long numberOfTransactions;

}
