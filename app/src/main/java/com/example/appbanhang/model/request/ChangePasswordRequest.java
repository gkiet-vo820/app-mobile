package com.example.appbanhang.model.request;

public class ChangePasswordRequest {
    private int id;
    private String oldPass;
    private String newPass;

    public ChangePasswordRequest(int id, String oldPass, String newPass) {
        this.id = id;
        this.oldPass = oldPass;
        this.newPass = newPass;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOldPass() {
        return oldPass;
    }

    public void setOldPass(String oldPass) {
        this.oldPass = oldPass;
    }

    public String getNewPass() {
        return newPass;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }
}
