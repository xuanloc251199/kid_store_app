package com.example.kid_toy_store.request;

public class DirectPaymentRequest {
    double total_price;
    String address;

    public DirectPaymentRequest(double totalPrice, String address) {
        this.total_price = totalPrice;
        this.address = address;
    }

    public double getTotalPrice() {
        return total_price;
    }

    public String getAddress() {
        return address;
    }
}

