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
import java.text.SimpleDateFormat;
import java.util.Date;

public class CurrencyRatesService {

    CurrencyRatesRepository currencyRatesRepository = new CurrencyRatesRepository();

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
                currencyRatesRepository.insertCurrencyRate(currency, rate, date);
            }
        }
    }
}
