package org.example;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
public class CurrencyRate {
    private String currency;
    private BigDecimal rate;
    private Date date;

    @Override
    public String toString() {
        return currency + ", " + rate + ", " + date + "\n";
    }
}
