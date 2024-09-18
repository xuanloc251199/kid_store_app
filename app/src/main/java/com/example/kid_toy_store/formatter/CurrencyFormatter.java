package com.example.kid_toy_store.formatter;
import android.annotation.SuppressLint;

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyFormatter {

    private static final Locale localeVN = new Locale("vi", "VN");
    private static final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(localeVN);

    // Phương thức định dạng tiền tệ
    @SuppressLint("DefaultLocale")
    public static String formatCurrency(double amount) {
        if (amount == Math.floor(amount)) {
            // Nếu là số nguyên, hiển thị giá dưới dạng số nguyên
            return String.format("%.0f đ", amount);
        } else {
            // Nếu có số lẻ, hiển thị đầy đủ giá trị thập phân
            return String.format("%.2f đ", amount);
        }
    }

    // Phương thức làm tròn đến hàng nghìn
    private static double roundToThousands(double amount) {
        return Math.round(amount / 1000) * 1000;
    }
}


