package com.example.appbanhang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.R;
import com.example.appbanhang.adapter.AdminProductAdapter;
import com.example.appbanhang.api.ProductService;
import com.example.appbanhang.model.Product;

import java.util.ArrayList;
import java.util.List;

import soup.neumorphism.NeumorphCardView;

public class ManagerActivity extends AppCompatActivity {
    Toolbar toolBarQuanLy;
    NeumorphCardView cardThemSP, cardThongKe;
    RecyclerView recyclerViewAdmin;
    AdminProductAdapter adminProductAdapter;
    List<Product> dsProduct;
    ProductService productService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manager);

        addControls();
        ActionBar();
        addEvents();
        showListProduct();
    }

    private void ActionBar() {
        setSupportActionBar(toolBarQuanLy);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBarQuanLy.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addControls(){
        toolBarQuanLy = findViewById(R.id.toolBarQuanLy);
        cardThemSP = findViewById(R.id.cardThemSP);
        cardThongKe = findViewById(R.id.cardThongKe);

        recyclerViewAdmin = findViewById(R.id.recyclerViewAdmin);
        recyclerViewAdmin.setLayoutManager(new LinearLayoutManager(this));
        dsProduct = new ArrayList<>();
        adminProductAdapter = new AdminProductAdapter(this, dsProduct);
        recyclerViewAdmin.setAdapter(adminProductAdapter);

        productService = new ProductService(this, dsProduct, adminProductAdapter);

    }

    private void addEvents(){
        cardThemSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent themsp = new Intent(ManagerActivity.this, AddProductActivity.class);
                startActivity(themsp);
            }
        });
    }

    private void showListProduct(){
        productService.getAllSanPham();
    }

    public void deleteProduct(int id){
        productService.deleteProduct(id);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showListProduct();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        productService.clear();
    }
}