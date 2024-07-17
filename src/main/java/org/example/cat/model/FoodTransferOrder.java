package org.example.cat.model;

public class FoodTransferOrder {
    private static int idCounter = 0;
    private int id;
    private String sender;
    private String recipient;
    private String type;
    private int amount;
    private String status; // new, in_progress, completed

    public FoodTransferOrder(String sender, String recipient, String type, int amount) {
        this.id = idCounter++;
        this.sender = sender;
        this.recipient = recipient;
        this.type = type;
        this.amount = amount;
        this.status = "new";
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public static int getIdCounter() {
        return idCounter;
    }

    public static void setIdCounter(int idCounter) {
        FoodTransferOrder.idCounter = idCounter;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}

