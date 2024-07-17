package org.example.cat.model;

import java.util.HashMap;
import java.util.Map;

public class Cat {
    private String name;
    private Map<String, Integer> foodStock = new HashMap<>();
    private boolean isHungry;

    public Cat(String name) {
        this.name = name;
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
        this.foodStock.put(type, this.foodStock.getOrDefault(type, 0) + amount);
    }

    public void removeFood(String type, int amount) {
        this.foodStock.put(type, this.foodStock.getOrDefault(type, 0) - amount);
    }

    public boolean isHungry() {
        return isHungry;
    }

    public void setHungry(boolean hungry) {
        isHungry = hungry;
    }
}
