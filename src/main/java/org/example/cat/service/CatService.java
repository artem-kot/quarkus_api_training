package org.example.cat.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.example.cat.model.Cat;
import org.example.cat.model.FoodTransferOrder;

import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class CatService {
    private Map<String, Cat> cats = new HashMap<>();

    public CatService() {
        cats.put("Boos", new Cat("Boos"));
        cats.put("Liposchka", new Cat("Liposchka"));
    }

    public Map<String, Cat> getCats() {
        return cats;
    }

    public Cat getCat(String name) {
        return cats.get(name);
    }

    public void addFood(String catName, String type, int amount) {
        Cat cat = getCat(catName);
        if (cat != null) {
            cat.addFood(type, amount);
        }
    }

    public void removeFood(String catName, String type, int amount) {
        Cat cat = getCat(catName);
        if (cat != null) {
            cat.removeFood(type, amount);
        }
    }
}


