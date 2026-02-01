package com.example.appbanhang.model.request;

import com.google.gson.annotations.SerializedName;

public class RegisterRequest {
    private String email;

    private String username;
    private String password;

    private String repassword;
    @SerializedName("numberphone")
    private String sdt;

    public RegisterRequest(String email, String username, String password, String repassword, String sdt) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.repassword = repassword;
        this.sdt = sdt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepassword() {
        return repassword;
    }

    public void setRepassword(String repassword) {
        this.repassword = repassword;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }
}
