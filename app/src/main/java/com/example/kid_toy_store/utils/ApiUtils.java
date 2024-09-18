package com.example.kid_toy_store.utils;

public class ApiUtils {
    // Base IP
    public static final String IP_DEFAULT = "http://192.168.1.9:8000/";
    public static final String BASE_IP = IP_DEFAULT + "api/";

    // Các endpoint được ghép nối với BASE_IP
    public static final String USER_URL = BASE_IP + "u";
    public static final String LOGIN_URL = USER_URL + "/login";
    public static final String CATEGORIES_URL = USER_URL + "/categories";
    public static final String PRODUCTS_URL = USER_URL + "/products";
    public static final String TICKETS_URL = USER_URL + "/tickets";
    public static final String USERS_URL = USER_URL + "/user";
    public static final String REVIEWS_URL = USER_URL + "/reviews";
    public static final String CART_URL = USER_URL + "/cart";
}

