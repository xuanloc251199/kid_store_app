package com.example.kid_toy_store.request;

import com.example.kid_toy_store.model.CartItem;

import java.util.List;

public class CheckoutRequest {
    // Ví dụ về các trường cần có trong yêu cầu checkout
    private List<CartItem> cartItems;
    private String address;
    private String paymentMethod;

    public CheckoutRequest(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    // Getter và setter cho các trường
    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
