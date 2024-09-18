package com.example.kid_toy_store.response;

import com.example.kid_toy_store.model.Order;

public class PaymentResponse {
    private String message;
    private Order order;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
