package org.example;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CurrencyRatesRepository {
    private final String url = "jdbc:mariadb://localhost:3306/mariadb";
    private final String username = "root";
    private final String password = "root";

    protected List<CurrencyRate> getTodaysRates() throws SQLException {

        List<CurrencyRate> exchangeRates = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            String sql = "SELECT currency, rate, date FROM exchange_rates WHERE date = (SELECT MAX(date) FROM exchange_rates)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                ResultSet resultSet = pstmt.executeQuery();
                while (resultSet.next()) {
                    String currency = resultSet.getString("currency");
                    BigDecimal rate = resultSet.getBigDecimal("rate");
                    Date date = resultSet.getDate("date");
                    CurrencyRate exchangeRate = new CurrencyRate(currency, rate, date);
                    exchangeRates.add(exchangeRate);
                }
            }
        }
        return exchangeRates;
    }

    protected String getSelectedCurrencyRate(String currency) throws SQLException {

        StringBuilder sb = new StringBuilder();
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            String sql = "SELECT rate, date FROM exchange_rates WHERE currency = ? ORDER BY date DESC";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, currency);
                ResultSet resultSet = pstmt.executeQuery();
                while (resultSet.next()) {
                    BigDecimal rate = resultSet.getBigDecimal("rate");
                    Date date = resultSet.getDate("date");
                    sb.append(currency.toUpperCase()).append(" ").append(rate).append(" (").append(date).append(")\n");
                }
            }
        }
        return sb.toString();
    }
}
