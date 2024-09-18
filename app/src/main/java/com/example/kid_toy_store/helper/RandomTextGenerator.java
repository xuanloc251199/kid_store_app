package com.example.kid_toy_store.helper;

import java.util.Random;

public class RandomTextGenerator {

    public static String generateRandomText(int maxLength) {
        // Xác định các ký tự có thể sử dụng trong chuỗi
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder randomText = new StringBuilder();
        Random random = new Random();

        // Sinh chuỗi với độ dài ngẫu nhiên trong khoảng từ 1 đến maxLength
        int length = random.nextInt(maxLength) + 1; // Đảm bảo độ dài luôn lớn hơn 0

        for (int i = 0; i < length; i++) {
            // Chọn ký tự ngẫu nhiên từ chuỗi characters và thêm vào randomText
            char randomChar = characters.charAt(random.nextInt(characters.length()));
            randomText.append(randomChar);
        }

        return randomText.toString();
    }
}
