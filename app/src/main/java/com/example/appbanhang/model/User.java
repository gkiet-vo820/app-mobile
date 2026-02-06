package com.example.appbanhang.model;

import com.google.gson.annotations.SerializedName;

public class User {
    private int id;
    private String email;
    private String password;
    private String username;
    @SerializedName("numberphone")
    private String sdt;

    private String avatar;
    private int role;
    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSdt() {
        return sdt;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }
}
