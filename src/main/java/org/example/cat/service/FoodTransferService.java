package org.example.cat.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.example.cat.model.FoodTransferOrder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class FoodTransferService {
    private Map<String, FoodTransferOrder> ordersUAT = new HashMap<>();
    private Map<String, FoodTransferOrder> ordersPROD = new HashMap<>();

    public void addOrder(String environment, FoodTransferOrder order) {
        if (environment.equals("uat")) {
            ordersUAT.put(order.getId(), order);
        } else {
            ordersPROD.put(order.getId(), order);
        }
    }

    public FoodTransferOrder getOrder(String environment, String orderId) {
        return environment.equals("uat") ? ordersUAT.get(orderId) : ordersPROD.get(orderId);
    }
}