package com.example.kid_toy_store.model;

public class UserProfile {
    private String name;
    private String number_phone;
    private String address;

    public UserProfile(String name, String number_phone, String address) {
        this.name = name;
        this.number_phone = number_phone;
        this.address = address;
    }

    // Getters và setters


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber_phone() {
        return number_phone;
    }

    public void setNumber_phone(String number_phone) {
        this.number_phone = number_phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

