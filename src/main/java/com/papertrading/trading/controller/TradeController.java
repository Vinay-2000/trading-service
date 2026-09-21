package com.papertrading.trading.controller;

import com.papertrading.trading.api.TradesApi;
import com.papertrading.trading.entity.Trade;
import com.papertrading.trading.model.TradeResponse;
import com.papertrading.trading.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TradeController implements TradesApi {

    private final OrderService orderService;

    public TradeController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public ResponseEntity<List<TradeResponse>> getOrderTrades(
            Long orderId) {

        List<TradeResponse> responses = orderService
                .getTradesByOrder(orderId)
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(responses);
    }

    private TradeResponse toResponse(Trade trade) {

        TradeResponse response = new TradeResponse();

        response.setId(trade.getId());
        response.setOrderId(trade.getOrderId());
        response.setUserId(trade.getUserId());
        response.setSymbol(trade.getSymbol());

        response.setSide(
                com.papertrading.trading.model.OrderSide
                        .fromValue(trade.getSide())
        );

        response.setQuantity(trade.getQuantity());
        response.setExecutionPrice(trade.getExecutionPrice());
        response.setExecutedAt(trade.getExecutedAt());

        return response;
    }
}