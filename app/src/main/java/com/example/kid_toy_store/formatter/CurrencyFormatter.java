package com.example.kid_toy_store.formatter;
import android.annotation.SuppressLint;

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyFormatter {

    private static final Locale localeVN = new Locale("vi", "VN");
    private static final NumberFormat currencyFormatter = NumberFormat.getInstance(localeVN);

    // Phương thức định dạng tiền tệ
    public static String formatCurrency(double amount) {
        return currencyFormatter.format(amount) + " đ";
    }

    // Phương thức làm tròn đến hàng nghìn (nếu cần)
    public static double roundToThousands(double amount) {
        return Math.round(amount / 1000) * 1000;
    }
}


