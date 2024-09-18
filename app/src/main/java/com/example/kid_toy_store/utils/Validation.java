package com.example.kid_toy_store.utils;

import java.util.Calendar;

public class Validation {
        // Kiểm tra số thẻ
        public static boolean isValidCardNumber(String cardNumber) {
            return cardNumber != null && cardNumber.length() == 16 && cardNumber.matches("\\d+");
        }

        // Kiểm tra ngày hết hạn
        public static boolean isValidExpiryDate(String expiryDate) {
            return expiryDate != null && expiryDate.matches("(0[1-9]|1[0-2])/\\d{2}");
        }

        // Kiểm tra CVV
        public static boolean isValidCvv(String cvv) {
            return cvv != null && cvv.length() == 3 && cvv.matches("\\d+");
        }
}
