package org.example.trading.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

import static org.example.trading.model.Ccy.*;

public class TradingUnit {
    @JsonProperty("location")
    private String location;

    @JsonProperty("stock")
    private Map<Ccy, Integer> stock;

    public TradingUnit() {
    }

    public TradingUnit(String location) {
        this.location = location;
        this.stock = new HashMap<>();
        this.stock.put(USD, 0);
        this.stock.put(EUR, 0);
//        this.stock.put(CAD, 0);
//        this.stock.put(AUD, 0);
//        this.stock.put(GBP, 0);
//        this.stock.put(JPY, 0);
    }

    public String getLocation() {
        return location;
    }

    public Map<Ccy, Integer> getStock() {
        return stock;
    }

    public void addCash(Ccy ccy, int amount) {
        stock.put(ccy, stock.get(ccy) + amount);
    }

    public void removeCash(Ccy ccy, int amount) {
        stock.put(ccy, stock.get(ccy) - amount);
    }

    public boolean isTradingReady() {
        return (stock.get(USD) + stock.get(EUR)) < 1000;
    }
}
