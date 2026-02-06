package com.example.appbanhang.model.response;

import com.example.appbanhang.model.Categories;
import com.example.appbanhang.model.Menu;

import java.util.List;

public class MenuResponse {
    private boolean success;
    private String message;
    private List<Menu> data;

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

    public List<Menu> getData() {
        return data;
    }

    public void setData(List<Menu> data) {
        this.data = data;
    }
}
