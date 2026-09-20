package com.papertrading.trading.service;

import java.math.BigDecimal;

public interface MarketPriceService {

    BigDecimal getLatestPrice(String symbol);
}