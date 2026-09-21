package com.papertrading.trading.dao;

import com.papertrading.trading.entity.Order;
import com.papertrading.trading.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDao extends JpaRepository<Order, Long> {

    List<Order> findByUserId(Long userId);

    List<Order> findByUserIdAndStatus(Long userId, String status);

    List<Order> findByUserIdAndStatusAndSymbol(
            Long userId,
            String status,
            String symbol
    );

    List<Order> findByUserIdAndSymbol(Long userId, String symbol);
}