package com.example.kid_toy_store.formatter;

public class PromotionFormatter {

    public static String formatPromotion(double promotion) {
        // Kiểm tra nếu giá trị promotion hợp lệ
        if (promotion < 0 || promotion > 100) {
            return "Invalid%"; // Giá trị không hợp lệ
        }
        return String.format("%.0f%%", promotion); // Định dạng hiển thị ví dụ: 20%
    }
    public static String getPromotionText(double promotion) {
        if (promotion == 0) {
            return "No Promotion";
        }
        return formatPromotion(promotion);
    }
}
