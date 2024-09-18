package com.example.kid_toy_store.request;

// Tạo lớp yêu cầu cập nhật
public class UpdateCartItemRequest {
    private int quantity;

    public UpdateCartItemRequest(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }
}
