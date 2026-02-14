package com.example.appbanhang.model;

public class ChatMesagge {
    private String sendId, receiveId, message, datetime;

    public ChatMesagge() {
    }

    public ChatMesagge(String sendId, String receiveId, String message, String datetime) {
        this.sendId = sendId;
        this.receiveId = receiveId;
        this.message = message;
        this.datetime = datetime;
    }

    public String getSendId() {
        return sendId;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
    }

    public String getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(String receiveId) {
        this.receiveId = receiveId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
