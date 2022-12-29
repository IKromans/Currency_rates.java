package org.example;

import io.javalin.Javalin;
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
import java.util.Scanner;

public class CurrencyRatesApi {
    public static void main(String[] args) throws Exception {

        CurrencyRatesService currencyRatesService = new CurrencyRatesService();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Please specify a command line argument: 'activate' or 'fetch' or 'close'.");
            String command = scanner.nextLine();
            switch (command) {
                case "activate" -> {
                    System.out.println("Starting endpoints...");
                    Thread.sleep(2000);
                    Javalin server = Javalin.create().start(7000);
                    server.get("/exchange-rates/latest", ctx -> ctx.result(currencyRatesService.getTodaysRates().toString()));
                    server.get("/exchange-rates/{currency}", ctx -> {
                        String currency = ctx.pathParam("currency");
                        String rate = currencyRatesService.getSelectedCurrencyRate(currency);
                        ctx.result(rate);
                    });
                    System.out.println("All endpoints are active!\n");
                }
                case "fetch" -> {
                    System.out.println("Fetching and storing data...");
                    Thread.sleep(2000);
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
                            currencyRatesService.insertCurrencyRate(currency, rate, date);
                        }
                    }
                    System.out.println("Data stored successfully!\n");
                }
                case "close" -> System.exit(1);
                default -> System.out.println("Invalid command line argument. Please use 'activate' or 'fetch'.");
            }
        }
    }
}
