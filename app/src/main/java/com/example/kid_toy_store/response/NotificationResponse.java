package com.example.kid_toy_store.response;

public class NotificationResponse {
    private String title;
    private String message;
    private String type;
    private String createdAt;
    private int userId;

    // Constructors
    public NotificationResponse(String title, String message, String type, String createdAt, int userId) {
        this.title = title;
        this.message = message;
        this.type = type;
        this.createdAt = createdAt;
        this.userId = userId;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
