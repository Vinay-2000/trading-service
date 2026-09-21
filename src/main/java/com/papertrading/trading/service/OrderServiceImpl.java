package com.papertrading.trading.service;

import com.papertrading.trading.dao.OrderDao;
import com.papertrading.trading.dao.PortfolioBalanceDao;
import com.papertrading.trading.dao.TradeDao;
import com.papertrading.trading.entity.Order;
import com.papertrading.trading.entity.Trade;
import com.papertrading.trading.exception.InsufficientBalanceException;
import com.papertrading.trading.model.OrderStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;
    private final TradeDao tradeDao;
    private final MarketPriceService marketPriceService;
    private final PortfolioBalanceDao portfolioBalanceDao;

    public OrderServiceImpl(
            OrderDao orderDao,
            TradeDao tradeDao,
            MarketPriceService marketPriceService,
            PortfolioBalanceDao portfolioBalanceDao) {
        this.orderDao = orderDao;
        this.tradeDao = tradeDao;
        this.marketPriceService = marketPriceService;
        this.portfolioBalanceDao = portfolioBalanceDao;
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

        BigDecimal tradeValue =
                executionPrice.multiply(quantity);

        BigDecimal cashBalance =
                portfolioBalanceDao.getCashBalance(userId);

        if (cashBalance.compareTo(tradeValue) < 0) {
            throw new InsufficientBalanceException("Insufficient cash balance");
        }

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
    @Override
    @Transactional(readOnly = true)
    public List<Order> getOrdersByUser(
            Long userId,
            OrderStatus status,
            String symbol) {

        if (status != null && symbol != null) {
            return orderDao.findByUserIdAndStatusAndSymbol(
                    userId,
                    status.toString(),
                    symbol
            );
        }

        if (status != null) {
            return orderDao.findByUserIdAndStatus(
                    userId,
                    status.toString()
            );
        }

        if (symbol != null) {
            return orderDao.findByUserIdAndSymbol(
                    userId,
                    symbol
            );
        }

        return orderDao.findByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trade> getTradesByOrder(Long orderId) {

        // Make sure the order exists
        orderDao.findById(orderId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Order not found: " + orderId
                        ));

        return tradeDao.findByOrderId(orderId);
    }

}