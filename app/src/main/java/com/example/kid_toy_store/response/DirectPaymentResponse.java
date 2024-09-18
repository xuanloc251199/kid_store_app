package com.example.kid_toy_store.response;

import com.example.kid_toy_store.model.Order;

public class DirectPaymentResponse {
    String message;
    Order order;
    String address;

    public String getMessage() {
        return message;
    }

    public Order getOrder() {
        return order;
    }

    public String getAddress() {
        return address;
    }
}
