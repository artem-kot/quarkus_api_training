package org.example.cat.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.example.cat.model.FoodTransferOrder;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class FoodTransferService {
    private List<FoodTransferOrder> orders = new ArrayList<>();

    public List<FoodTransferOrder> getOrders() {
        return orders;
    }

    public FoodTransferOrder getOrder(int id) {
        return orders.stream().filter(order -> order.getId() == id).findFirst().orElse(null);
    }

    public void addOrder(FoodTransferOrder order) {
        orders.add(order);
    }

    public void updateOrderStatus(int id, String status) {
        FoodTransferOrder order = getOrder(id);
        if (order != null) {
            order.setStatus(status);
        }
    }
}
