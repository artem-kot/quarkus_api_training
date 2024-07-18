package org.example.cat.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public class Cat {
    @JsonProperty("name")
    private String name;

    @JsonProperty("foodStock")
    private Map<String, Integer> foodStock;

    public Cat() {
    }

    public Cat(String name) {
        this.name = name;
        this.foodStock = new HashMap<>();
        this.foodStock.put("dry", 0);
        this.foodStock.put("wet", 0);
    }

    public String getName() {
        return name;
    }

    public Map<String, Integer> getFoodStock() {
        return foodStock;
    }

    public void addFood(String type, int amount) {
        foodStock.put(type, foodStock.get(type) + amount);
    }

    public void removeFood(String type, int amount) {
        foodStock.put(type, foodStock.get(type) - amount);
    }

    public boolean isHungry() {
        return (foodStock.get("dry") + foodStock.get("wet")) < 115;
    }
}
