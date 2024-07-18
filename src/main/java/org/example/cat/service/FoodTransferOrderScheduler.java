package org.example.cat.service;

import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.example.cat.model.FoodTransferOrder;

import java.util.Map;
import java.util.Set;

@ApplicationScoped
public class FoodTransferOrderScheduler {

    @Inject
    FoodTransferService foodTransferService;

    @Scheduled(every = "20s")
    public void updateOrderStatus() {
        updateOrdersStatus("uat");
        updateOrdersStatus("prod");
    }

    private void updateOrdersStatus(String environment) {
        Set<Map.Entry<String, FoodTransferOrder>> orders = foodTransferService.getAllOrders(environment);
        for (Map.Entry<String, FoodTransferOrder> entry : orders) {
            FoodTransferOrder order = entry.getValue();
            if ("new".equals(order.getStatus())) {
                order.setStatus("in_progress");
            }
        }
    }
}
