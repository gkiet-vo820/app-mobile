package com.example.appbanhang.model.response;

import com.example.appbanhang.model.Menu;

import java.util.List;

public class MenuResponse {
    private boolean success;
    private List<Menu> data;

    public boolean isSuccess() {
        return success;
    }

    public List<Menu> getData() {
        return data;
    }
}
