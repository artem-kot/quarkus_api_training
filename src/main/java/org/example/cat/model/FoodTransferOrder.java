package org.example.cat.model;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

public class FoodTransferOrder {
    private static final AtomicInteger counter = new AtomicInteger(0);

    private String id;
    private String status;
    private String sender;
    private String recipient;
    private String type;
    private int amount;

    public FoodTransferOrder() {
        this.id = Instant.now().toEpochMilli() + "_" + counter.incrementAndGet();
        this.status = "new";
    }

    public FoodTransferOrder(String sender, String recipient, String type, int amount) {
        this();
        this.sender = sender;
        this.recipient = recipient;
        this.type = type;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSender() {
        return sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }
}