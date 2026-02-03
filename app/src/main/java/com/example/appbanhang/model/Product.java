package com.example.appbanhang.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Product implements Serializable {
    private int id;
    @SerializedName("name")
    private String tensp;
    @SerializedName("price")
    private long gia;
    @SerializedName("image")
    private String hinhanh;
    @SerializedName("description")
    private String mota;
    @SerializedName("category")
    private int loai;
    @SerializedName("createdAt")
    private String ngaytao;
    @SerializedName("soldQuantity")
    private int soluongban;

    public Product(int id, String tensp, long gia, String hinhanh, String mota, int loai, String ngaytao, int soluongban) {
        this.id = id;
        this.tensp = tensp;
        this.gia = gia;
        this.hinhanh = hinhanh;
        this.mota = mota;
        this.loai = loai;
        this.ngaytao = ngaytao;
        this.soluongban = soluongban;
    }

    public Product() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTensp() {
        return tensp;
    }

    public void setTensp(String tensp) {
        this.tensp = tensp;
    }

    public long getGia() {
        return gia;
    }

    public void setGia(long gia) {
        this.gia = gia;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public int getLoai() {
        return loai;
    }

    public void setLoai(int loai) {
        this.loai = loai;
    }

    public String getNgaytao() {
        return ngaytao;
    }

    public void setNgaytao(String ngaytao) {
        this.ngaytao = ngaytao;
    }

    public int getSoluongban() {
        return soluongban;
    }

    public void setSoluongban(int soluongban) {
        this.soluongban = soluongban;
    }
}
