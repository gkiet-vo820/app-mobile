package com.example.appbanhang.model.response;

import com.example.appbanhang.model.Categories;

import java.util.List;

public class CategoriesResponse {
    private boolean success;
    private String message;
    private List<Categories> data;

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

    public List<Categories> getData() {
        return data;
    }

    public void setData(List<Categories> data) {
        this.data = data;
    }
}
