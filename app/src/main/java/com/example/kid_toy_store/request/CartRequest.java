package com.example.kid_toy_store.request;

public class CartRequest {
    private int item_id;
    private String type;
    private int quantity;

    public CartRequest(int item_id, String type, int quantity) {
        this.item_id = item_id;
        this.type = type;
        this.quantity = quantity;
    }

    // Getters and Setters
}

