package com.papertrading.trading.dao;

import com.papertrading.trading.entity.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TradeDao extends JpaRepository<Trade, Long> {

    List<Trade> findByOrderId(Long orderId);

    List<Trade> findByUserId(Long userId);
}