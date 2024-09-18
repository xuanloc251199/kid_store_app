package com.example.kid_toy_store.response;

import com.example.kid_toy_store.model.Order;

import java.util.List;

public class OrderResponse {
    private boolean success;
    private List<Order> orders;

    // Getters and Setters

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
