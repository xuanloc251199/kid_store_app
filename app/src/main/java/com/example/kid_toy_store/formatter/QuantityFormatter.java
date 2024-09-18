package com.example.kid_toy_store.formatter;

public class QuantityFormatter {

    public static String formatQuantity(int quantity) {
        if (quantity < 1000) {
            return String.valueOf(quantity);
        } else {
            double result = quantity / 1000.0;
            // Kiểm tra nếu số sau dấu thập phân là 0 thì không hiển thị phần thập phân
            if (result == (int) result) {
                return String.format("%.0fk", result);
            } else {
                return String.format("%.1fk", result);
            }
        }
    }
}

