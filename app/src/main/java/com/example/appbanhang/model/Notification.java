package com.example.appbanhang.model;

public class Notification {
    private int id;
    private int userId;
    private String title;
    private String content;
    private int orderId;
    private int isRead;
    private String createdAt;

    public Notification(int id, int userId, String title, String content, int orderId, int isRead, String createdAt) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.orderId = orderId;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    public Notification() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAd) {
        this.createdAt = createdAd;
    }
}
