package org.example.cat.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Food {
    public Food() {
    }

    public Food(String type, int amount) {
        this.type = type;
        this.amount = amount;
    }

    @JsonProperty("type")
    private String type;

    @JsonProperty("amount")
    private int amount;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}