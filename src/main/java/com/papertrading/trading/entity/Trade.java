package com.papertrading.trading.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "TRADES")
@Getter
@Setter
@NoArgsConstructor
public class Trade {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "trade_seq"
    )
    @SequenceGenerator(
            name = "trade_seq",
            sequenceName = "SEQ_TRADES",
            allocationSize = 1
    )
    private Long id;

    @Column(name = "ORDER_ID", nullable = false)
    private Long orderId;

    @Column(name = "USER_ID", nullable = false)
    private Long userId;

    @Column(name = "SYMBOL", nullable = false, length = 20)
    private String symbol;

    @Column(name = "SIDE", nullable = false, length = 10)
    private String side;

    @Column(name = "QUANTITY", nullable = false, precision = 19, scale = 8)
    private BigDecimal quantity;

    @Column(name = "EXECUTION_PRICE", nullable = false, precision = 19, scale = 8)
    private BigDecimal executionPrice;

    @Column(name = "EXECUTED_AT", nullable = false)
    private OffsetDateTime executedAt;

    @PrePersist
    protected void onCreate() {
        executedAt = OffsetDateTime.now();
    }
}