package com.example.kid_toy_store.response;

import com.example.kid_toy_store.model.User;

public class UserProfileResponse {
    private String message;
    private User user;

    // Getter and Setter
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

