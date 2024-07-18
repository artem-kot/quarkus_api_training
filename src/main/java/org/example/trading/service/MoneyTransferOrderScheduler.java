package org.example.trading.service;

import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.example.trading.model.CashTransferOrder;

import java.util.Map;
import java.util.Set;

@ApplicationScoped
public class MoneyTransferOrderScheduler {

    @Inject
    MoneyTransferService moneyTransferService;

    @Scheduled(every = "20s")
    public void updateOrderStatus() {
        updateOrdersStatus("uat");
        updateOrdersStatus("prod");
    }

    private void updateOrdersStatus(String environment) {
        Set<Map.Entry<String, CashTransferOrder>> orders = moneyTransferService.getAllOrders(environment);
        for (Map.Entry<String, CashTransferOrder> entry : orders) {
            CashTransferOrder order = entry.getValue();
            if ("new".equals(order.getStatus())) {
                order.setStatus("in_progress");
            }
        }
    }
}
