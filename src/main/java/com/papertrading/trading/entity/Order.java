package com.papertrading.trading.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "ORDERS")
@Getter
@Setter
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "order_seq"
    )
    @SequenceGenerator(
            name = "order_seq",
            sequenceName = "SEQ_ORDERS",
            allocationSize = 1
    )
    private Long id;

    @Column(name = "USER_ID", nullable = false)
    private Long userId;

    @Column(name = "SYMBOL", nullable = false, length = 20)
    private String symbol;

    @Column(name = "SIDE", nullable = false, length = 10)
    private String side;

    @Column(name = "ORDER_TYPE", nullable = false, length = 10)
    private String orderType;

    @Column(name = "QUANTITY", nullable = false, precision = 19, scale = 8)
    private BigDecimal quantity;

    @Column(name = "LIMIT_PRICE", precision = 19, scale = 8)
    private BigDecimal limitPrice;

    @Column(name = "STATUS", nullable = false, length = 20)
    private String status;

    @Column(name = "CREATED_AT", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "UPDATED_AT", nullable = false)
    private OffsetDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        OffsetDateTime now = OffsetDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }
}