package com.example.kid_toy_store.request;

import com.example.kid_toy_store.model.Order;

public class PaymentRequest {
    private String card_number;
    private String expiry_date;
    private String cvv;
    private double amount;

    public PaymentRequest(String card_number, String expiry_date, String cvv, double amount) {
        this.card_number = card_number;
        this.expiry_date = expiry_date;
        this.cvv = cvv;
        this.amount = amount;
    }
}



