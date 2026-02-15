package com.example.appbanhang.model.eventbus;

public class LogoutEvent {
    private String message;

    public LogoutEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
