package com.example.kid_toy_store.response;

import com.example.kid_toy_store.model.User;

public class UserResponse {
    private String access_token;
    private String token_type;
    private User user;

    // Getters and Setters
    public String getAccessToken() {
        return access_token;
    }

    public void setAccessToken(String access_token) {
        this.access_token = access_token;
    }

    public String getTokenType() {
        return token_type;
    }

    public void setTokenType(String token_type) {
        this.token_type = token_type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
