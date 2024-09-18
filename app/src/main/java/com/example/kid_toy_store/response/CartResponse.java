package com.example.kid_toy_store.response;

import com.example.kid_toy_store.model.CartItem;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CartResponse {
    @SerializedName("total_quantity")
    private int totalQuantity;

    @SerializedName("total_price")
    private double totalPrice; // Thêm thuộc tính này để ánh xạ với "total_price" từ JSON

    @SerializedName("cart_items")
    private List<CartItem> cartItems;

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }
}
