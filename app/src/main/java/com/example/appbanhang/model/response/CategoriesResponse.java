package com.example.appbanhang.model.response;

import com.example.appbanhang.model.Categories;

import java.util.List;

public class CategoriesResponse {
    private boolean success;
    private List<Categories> data;

    public boolean isSuccess() {
        return success;
    }

    public List<Categories> getData() {
        return data;
    }
}
