package com.example.appbanhang.activity.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.R;
import com.example.appbanhang.adapter.admin.AdminProductAdapter;
import com.example.appbanhang.api.ProductService;
import com.example.appbanhang.api.admin.ManagerProductService;
import com.example.appbanhang.model.Product;

import java.util.ArrayList;
import java.util.List;

import soup.neumorphism.NeumorphCardView;

public class ManagerActivity extends AppCompatActivity {
    Toolbar toolBarQuanLy;
    NeumorphCardView cardThemSP, cardThongKe, cardQuanLyDDH, cardQuanLyKho;
    RecyclerView recyclerViewAdmin;
    AdminProductAdapter adminProductAdapter;
    List<Product> dsProduct;
    ManagerProductService managerProductService;


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
        cardQuanLyDDH = findViewById(R.id.cardQuanLyDDH);
        cardQuanLyKho = findViewById(R.id.cardQuanLyKho);

        recyclerViewAdmin = findViewById(R.id.recyclerViewAdmin);
        recyclerViewAdmin.setLayoutManager(new LinearLayoutManager(this));
        dsProduct = new ArrayList<>();
        adminProductAdapter = new AdminProductAdapter(this, dsProduct);
        recyclerViewAdmin.setAdapter(adminProductAdapter);

        managerProductService = new ManagerProductService(this, dsProduct, adminProductAdapter);
    }

    private void addEvents(){
        cardThemSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagerActivity.this, AddProductActivity.class);
                startActivity(intent);
            }
        });

        cardThongKe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagerActivity.this, StatisticalActivity.class);
                startActivity(intent);
            }
        });

        cardQuanLyDDH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagerActivity.this, ManagerOrdersActivity.class);
                startActivity(intent);
            }
        });

        cardQuanLyKho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagerActivity.this, ManagerInventoryActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showListProduct(){
        managerProductService.getAllSanPhamAdmin();
    }

    public void deleteProduct(int id){
        managerProductService.deleteProduct(id);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showListProduct();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        managerProductService.clear();
    }
}