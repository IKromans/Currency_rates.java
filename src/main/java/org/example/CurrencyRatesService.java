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
import java.util.Date;

public class CurrencyRatesService {

    protected void fetchData() throws Exception {
        URL url = new URL("https://www.bank.lv/vk/ecb_rss.xml");
        InputStream inputStream = url.openStream();
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputStream);
        NodeList items = doc.getElementsByTagName("item");
        for (int i = 0; i < items.getLength(); i++) {
            Element item = (Element) items.item(i);
            String[] rates = item.getElementsByTagName("description").item(0).getTextContent().split(" ");
            String pubDateString = item.getElementsByTagName("pubDate").item(0).getTextContent();
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
            Date date = dateFormat.parse(pubDateString);
            for (int j = 0; j < rates.length - 1; j += 2) {
                String currency = rates[j];
                BigDecimal rate = new BigDecimal(rates[j + 1]);
                rate = rate.setScale(6, RoundingMode.HALF_UP);
                insertCurrencyRate(currency, rate, date);
            }
        }
    }

    protected void insertCurrencyRate(String currency, BigDecimal rate, Date date) throws SQLException {
        String url = "jdbc:mariadb://localhost:3306/mariadb";
        String username = "root";
        String password = "root";
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
