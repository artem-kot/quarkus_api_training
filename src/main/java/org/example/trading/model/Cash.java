package org.example.trading.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Cash {
    public Cash() {
    }

    public Cash(Ccy ccy, int amount) {
        this.ccy = ccy;
        this.amount = amount;
    }

    @JsonProperty("currency")
    private Ccy ccy;

    @JsonProperty("amount")
    private int amount;

    public Ccy getCcy() {
        return ccy;
    }

    public void setCcy(Ccy ccy) {
        this.ccy = ccy;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}