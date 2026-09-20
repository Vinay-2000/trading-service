package com.papertrading.trading.service;

import com.papertrading.trading.dao.OrderDao;
import com.papertrading.trading.dao.TradeDao;
import com.papertrading.trading.entity.Order;
import com.papertrading.trading.entity.Trade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;
    private final TradeDao tradeDao;
    private final MarketPriceService marketPriceService;

    public OrderServiceImpl(
            OrderDao orderDao,
            TradeDao tradeDao,
            MarketPriceService marketPriceService) {
        this.orderDao = orderDao;
        this.tradeDao = tradeDao;
        this.marketPriceService = marketPriceService;
    }

    @Override
    @Transactional
    public Order createOrder(
            Long userId,
            String symbol,
            String side,
            String orderType,
            BigDecimal quantity,
            BigDecimal limitPrice) {

        if ("MARKET".equals(orderType)) {
            return createMarketOrder(
                    userId,
                    symbol,
                    side,
                    quantity
            );
        }

        if ("LIMIT".equals(orderType)) {
            return createLimitOrder(
                    userId,
                    symbol,
                    side,
                    quantity,
                    limitPrice
            );
        }

        throw new IllegalArgumentException(
                "Unsupported order type: " + orderType
        );
    }

    private Order createMarketOrder(
            Long userId,
            String symbol,
            String side,
            BigDecimal quantity) {

        BigDecimal executionPrice =
                marketPriceService.getLatestPrice(symbol);
        System.out.println("Current price for executionPrice "+symbol+": "+executionPrice);

        Order order = new Order();
        order.setUserId(userId);
        order.setSymbol(symbol);
        order.setSide(side);
        order.setOrderType("MARKET");
        order.setQuantity(quantity);
        order.setStatus("FILLED");

        Order savedOrder = orderDao.save(order);

        Trade trade = new Trade();
        trade.setOrderId(savedOrder.getId());
        trade.setUserId(userId);
        trade.setSymbol(symbol);
        trade.setSide(side);
        trade.setQuantity(quantity);
        trade.setExecutionPrice(executionPrice);

        tradeDao.save(trade);

        return savedOrder;
    }

    private Order createLimitOrder(
            Long userId,
            String symbol,
            String side,
            BigDecimal quantity,
            BigDecimal limitPrice) {

        if (limitPrice == null) {
            throw new IllegalArgumentException(
                    "Limit price is required for LIMIT orders"
            );
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setSymbol(symbol);
        order.setSide(side);
        order.setOrderType("LIMIT");
        order.setQuantity(quantity);
        order.setLimitPrice(limitPrice);
        order.setStatus("NEW");

        return orderDao.save(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Order getOrder(Long orderId) {
        return orderDao.findById(orderId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Order not found: " + orderId
                        )
                );
    }
}