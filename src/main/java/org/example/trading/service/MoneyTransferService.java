package org.example.trading.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.example.trading.model.CashTransferOrder;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@ApplicationScoped
public class MoneyTransferService {
    private Map<String, CashTransferOrder> ordersUAT = new HashMap<>();
    private Map<String, CashTransferOrder> ordersPROD = new HashMap<>();

    public void addOrder(String environment, CashTransferOrder order) {
        if (environment.equals("uat")) {
            ordersUAT.put(order.getId(), order);
        } else {
            ordersPROD.put(order.getId(), order);
        }
    }

    public CashTransferOrder getOrder(String environment, String orderId) {
        return environment.equals("uat") ? ordersUAT.get(orderId) : ordersPROD.get(orderId);
    }

    public Set<Map.Entry<String, CashTransferOrder>> getAllOrders(String environment) {
        return environment.equals("uat") ? ordersUAT.entrySet() : ordersPROD.entrySet();
    }
}