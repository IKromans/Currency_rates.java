package org.example;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CurrencyRatesRepository {
    private static final String url = "jdbc:mariadb://localhost:3306/mariadb";
    private static final String username = "root";
    private static final String password = "root";

    protected static List<CurrencyRate> getTodayRates() throws SQLException {

        List<CurrencyRate> exchangeRates = new ArrayList<>();
        String sql = "SELECT currency, rate, date FROM exchange_rates WHERE date = (SELECT MAX(date) FROM exchange_rates)";
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                String currency = resultSet.getString("currency");
                BigDecimal rate = resultSet.getBigDecimal("rate");
                Date date = resultSet.getDate("date");
                CurrencyRate exchangeRate = new CurrencyRate(currency, rate, date);
                exchangeRates.add(exchangeRate);
            }
        }
        return exchangeRates;
    }

    protected static String getSelectedCurrencyRate(String currency) throws SQLException {

        StringBuilder sb = new StringBuilder();
        String sql = "SELECT rate, date FROM exchange_rates WHERE currency = ? ORDER BY date DESC";
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, currency);
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                BigDecimal rate = resultSet.getBigDecimal("rate");
                Date date = resultSet.getDate("date");
                sb.append(currency.toUpperCase()).append(" ").append(rate).append(" (").append(date).append(")\n");
            }
        }
        return sb.toString();
    }

    protected static void insertCurrencyRate(String currency, BigDecimal rate, Date date) throws SQLException {

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            DatabaseMetaData meta = conn.getMetaData();
            try (ResultSet rs = meta.getTables(null, null, "exchange_rates", null)) {
                if (!rs.next()) {
                    String createTableSql = "CREATE TABLE exchange_rates (currency VARCHAR(255), rate DECIMAL(10,5), date DATE)";
                    try (Statement stmt = conn.createStatement()) {
                        stmt.executeUpdate(createTableSql);
                    }
                }
            }
            String sql = "INSERT INTO exchange_rates (currency, rate, date) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, currency);
                pstmt.setBigDecimal(2, rate);
                pstmt.setDate(3, new java.sql.Date(date.getTime()));
                pstmt.executeUpdate();
            }
        }
    }
}
