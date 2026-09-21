package com.papertrading.trading.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public class PortfolioBalanceDao {

    private final JdbcTemplate jdbcTemplate;

    public PortfolioBalanceDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public BigDecimal getCashBalance(Long userId) {
        return jdbcTemplate.queryForObject(
                """
                SELECT CASH_BALANCE
                FROM PORTFOLIO
                WHERE USER_ID = ?
                """,
                BigDecimal.class,
                userId
        );
    }
}