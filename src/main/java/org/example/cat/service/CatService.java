package org.example.cat.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.example.cat.model.Cat;

import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class CatService {
    private Map<String, Cat> catsUAT = new HashMap<>();
    private Map<String, Cat> catsPROD = new HashMap<>();

    public CatService() {
        catsUAT.put("Boos", new Cat("Boos"));
        catsUAT.put("Liposchka", new Cat("Liposchka"));

        catsPROD.put("Boos", new Cat("Boos"));
        catsPROD.put("Liposchka", new Cat("Liposchka"));
    }

    public Map<String, Cat> getCats(String environment) {
        return environment.equals("uat") ? catsUAT : catsPROD;
    }

    public Cat getCat(String environment, String name) {
        return environment.equals("uat") ? catsUAT.get(name) : catsPROD.get(name);
    }

    public void addFood(String environment, String name, String type, int amount) {
        Cat cat = getCat(environment, name);
        if (cat != null) {
            cat.addFood(type, amount);
        }
    }

    public void removeFood(String environment, String name, String type, int amount) {
        Cat cat = getCat(environment, name);
        if (cat != null && cat.getFoodStock().get(type) >= amount) {
            cat.removeFood(type, amount);
        }
    }
}