package org.example.trading.model;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

public class CashTransferOrder {
    private static final AtomicInteger counter = new AtomicInteger(0);

    private String id;
    private String status;
    private String sender;
    private String recipient;
    private Ccy ccy;
    private int amount;

    public CashTransferOrder() {
        this.id = Instant.now().toEpochMilli() + "_" + counter.incrementAndGet();
        this.status = "new";
    }

    public CashTransferOrder(String sender, String recipient, Ccy currency, int amount) {
        this();
        this.sender = sender;
        this.recipient = recipient;
        this.ccy = currency;
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

    public Ccy getCcy() {
        return ccy;
    }

    public int getAmount() {
        return amount;
    }
}