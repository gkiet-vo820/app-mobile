package com.example.appbanhang.model;

import com.google.gson.annotations.SerializedName;

public class Categories {
    private int id;
    @SerializedName("name")
    private String tenLoai;
    @SerializedName("image")
    private String hinhAnh;

    public Categories(int id, String tenLoai, String hinhAnh) {
        this.id = id;
        this.tenLoai = tenLoai;
        this.hinhAnh = hinhAnh;
    }

    public Categories() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenLoai() {
        return tenLoai;
    }

    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }
}
