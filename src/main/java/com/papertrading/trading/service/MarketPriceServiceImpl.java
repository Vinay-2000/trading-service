package com.papertrading.trading.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class MarketPriceServiceImpl implements MarketPriceService {

    private final StringRedisTemplate redisTemplate;

    public MarketPriceServiceImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public BigDecimal getLatestPrice(String symbol) {

        String price = redisTemplate.opsForValue().get(
                "market-price:" + symbol
        );

        if (price == null) {
            throw new IllegalStateException(
                    "Latest market price not available for " + symbol
            );
        }

        return new BigDecimal(price);
    }
}