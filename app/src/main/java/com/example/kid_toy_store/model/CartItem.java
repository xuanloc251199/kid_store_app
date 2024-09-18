package com.example.kid_toy_store.model;

import com.google.gson.annotations.SerializedName;

public class CartItem {
    private int id;
    @SerializedName("user_id")
    private int userId;
    @SerializedName("product_id")
    private int productId;
    private int quantity;
    private Products product;

    public CartItem(int productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public Products getProduct() {
        return product;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setProduct(Products product) {
        this.product = product;
    }
}