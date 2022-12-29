package org.example;

import io.javalin.Javalin;

import java.util.Scanner;

public class CurrencyRatesMain {
    public static void main(String[] args) throws Exception {

        CurrencyRatesRepository currencyRatesRepository = new CurrencyRatesRepository();
        CurrencyRatesService currencyRatesService = new CurrencyRatesService();
        Scanner scanner = new Scanner(System.in);

        System.out.println("""
                Please choose your input:
                 to activate endpoints - 'activate'
                 to get and save data to database - 'fetch'.""");
        String command = scanner.nextLine();
        switch (command) {
            case "activate" -> {
                System.out.println("Starting endpoints...");
                Javalin server = Javalin.create().start(7000);
                server.get("/exchange-rates/latest", ctx -> ctx.result(currencyRatesRepository.getTodayRates().toString()));
                server.get("/exchange-rates/{currency}", ctx -> {
                    String currency = ctx.pathParam("currency");
                    String rate = currencyRatesRepository.getSelectedCurrencyRate(currency);
                    ctx.result(rate);
                });
                System.out.println("Endpoints are active!\n");
            }
            case "fetch" -> {
                System.out.println("Fetching and storing data...");
                currencyRatesService.fetchData();
                System.out.println("Data stored successfully!\n");
            }
            default -> System.out.println("Invalid input.");
        }
    }
}
