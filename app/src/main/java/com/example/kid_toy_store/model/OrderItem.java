package com.example.kid_toy_store.model;

public class OrderItem {
    private int id;
    private Products product;  // Sản phẩm
    private Tickets ticket;    // Vé sự kiện (ticket)
    private int quantity;
    private double price;

    // Getters và Setters
    public Products getProduct() {
        return product;
    }

    public void setProduct(Products product) {
        this.product = product;
    }

    public Tickets getTicket() {
        return ticket;
    }

    public void setTicket(Tickets ticket) {
        this.ticket = ticket;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
