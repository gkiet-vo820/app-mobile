package com.example.appbanhang.model;

import java.io.Serializable;
import java.util.List;

public class Orders implements Serializable {
    private int id;
    private int userId;
    private String email;
    private String numberphone;
    private String address;
    private int quantity;
    private long totalpayment;
    private List<DetailOrders> detailOrders;
    private int status;
    private String paymentMethod;
    private String note;
    private String createdAt;


    public Orders() {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumberphone() {
        return numberphone;
    }

    public void setNumberphone(String numberphone) {
        this.numberphone = numberphone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getTotalpayment() {
        return totalpayment;
    }

    public void setTotalpayment(long totalpayment) {
        this.totalpayment = totalpayment;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<DetailOrders> getDetailOrders() {
        return detailOrders;
    }

    public void setDetailOrders(List<DetailOrders> detailOrders) {
        this.detailOrders = detailOrders;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
