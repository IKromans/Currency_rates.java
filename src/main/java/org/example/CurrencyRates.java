package org.example;

import java.math.BigDecimal;
import java.util.Date;

public class CurrencyRates {
    private String currency;
    private BigDecimal rate;
    private Date date;

    public CurrencyRates(String currency, BigDecimal rate, Date date) {
        this.currency = currency;
        this.rate = rate;
        this.date = date;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return currency + ", " + rate + ", " + date + "\n";
    }
}
