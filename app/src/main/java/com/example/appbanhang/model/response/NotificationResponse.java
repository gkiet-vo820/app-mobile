package com.example.appbanhang.model.response;

import com.example.appbanhang.model.Notification;

import java.util.List;

public class NotificationResponse {
    private boolean success;
    private String message;
    List<Notification> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Notification> getData() {
        return data;
    }

    public void setData(List<Notification> data) {
        this.data = data;
    }
}
