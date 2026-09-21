package com.papertrading.trading.service;

import com.papertrading.trading.entity.Order;
import com.papertrading.trading.entity.Trade;
import com.papertrading.trading.model.OrderStatus;

import java.util.List;

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

    List<Order> getOrdersByUser(
            Long userId,
            OrderStatus status,
            String symbol
    );

    List<Trade> getTradesByOrder(Long orderId);
}