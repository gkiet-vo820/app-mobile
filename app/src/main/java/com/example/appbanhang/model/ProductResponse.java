package com.example.appbanhang.model;

import java.util.List;

public class ProductResponse {
    private boolean success;
    private List<Product> data;
    private int totalPage;

    public boolean isSuccess() {
        return success;
    }

    public List<Product> getData() {
        return data;
    }

    public int getTotalPage() {
        return totalPage;
    }
}
