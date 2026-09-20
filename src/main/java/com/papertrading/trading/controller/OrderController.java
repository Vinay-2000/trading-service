package com.papertrading.trading.controller;

import com.papertrading.trading.api.OrdersApi;
import com.papertrading.trading.entity.Order;
import com.papertrading.trading.model.*;
import com.papertrading.trading.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.openapitools.jackson.nullable.JsonNullable;

@RestController
public class OrderController implements OrdersApi {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public ResponseEntity<OrderResponse> createOrder(
            CreateOrderRequest request) {

        Order order = orderService.createOrder(
                request.getUserId(),
                request.getSymbol(),
                request.getSide().getValue(),
                request.getOrderType().getValue(),
                request.getQuantity(),
                request.getLimitPrice().orElse(null)
        );

        return ResponseEntity
                .status(201)
                .body(toResponse(order));
    }

    @Override
    public ResponseEntity<OrderResponse> getOrder(
            Long orderId) {

        Order order = orderService.getOrder(orderId);

        return ResponseEntity.ok(toResponse(order));
    }

    private OrderResponse toResponse(Order order) {

        OrderResponse response = new OrderResponse();

        response.setId(order.getId());
        response.setUserId(order.getUserId());
        response.setSymbol(order.getSymbol());

        response.setSide(
                OrderSide.fromValue(order.getSide())
        );

        response.setOrderType(
                OrderType.fromValue(order.getOrderType())
        );

        response.setQuantity(order.getQuantity());

        if (order.getLimitPrice() != null) {
            response.setLimitPrice(
                    JsonNullable.of(order.getLimitPrice())
            );
        } else {
            response.setLimitPrice(
                    JsonNullable.undefined()
            );
        }

        response.setStatus(
                OrderStatus.fromValue(order.getStatus())
        );

        response.setCreatedAt(order.getCreatedAt());
        response.setUpdatedAt(order.getUpdatedAt());

        return response;
    }
}