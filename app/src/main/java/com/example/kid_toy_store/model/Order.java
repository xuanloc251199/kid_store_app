package com.example.kid_toy_store.model;

public class Order {
    private int id;
    private double total_price;
    // Các trường khác của đơn hàng

    public Order(int id, double total_price) {
        this.id = id;
        this.total_price = total_price;
    }


    // Getter và setter cho các trường

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }
}
