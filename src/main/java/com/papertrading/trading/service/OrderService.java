package com.papertrading.trading.service;

import com.papertrading.trading.entity.Order;

public interface OrderService {

    Order createOrder(
            Long userId,
            String symbol,
            String side,
            String orderType,
            java.math.BigDecimal quantity,
            java.math.BigDecimal limitPrice
    );

    Order getOrder(Long orderId);
}