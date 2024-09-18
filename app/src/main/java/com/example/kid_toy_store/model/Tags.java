package com.example.kid_toy_store.model;

public class Tags {

    private String tag; // Văn bản hiển thị trên Chip
    private boolean isCloseable; // Trạng thái cho biết Chip có thể đóng hay không

    public Tags(String tag, boolean isCloseable) {
        this.tag = tag;
        this.isCloseable = isCloseable;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isCloseable() {
        return isCloseable;
    }

    public void setCloseable(boolean closeable) {
        isCloseable = closeable;
    }
}
