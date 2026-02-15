package com.example.appbanhang.activity.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.R;
import com.example.appbanhang.adapter.product.ProductAdapter;
import com.example.appbanhang.api.product.ProductService;
import com.example.appbanhang.model.Product;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    Toolbar toolbarTimKiem;
    RecyclerView recyclerViewTimKiem;

    TextView txtSoLuongKetQuaTimKiem;
    ProductAdapter productAdapter;
    ProductService productService;
    List<Product> dsProduct;
    String keyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);

        addControls();
        getIntentData();
        ActionBar();

    }

    private void getIntentData(){
        Intent intent = getIntent();
        keyword = intent.getStringExtra("keyword");
        if(keyword != null){
            productService.searchProduct(keyword);
        }
    }

    private void ActionBar() {
        setSupportActionBar(toolbarTimKiem);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarTimKiem.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void addControls(){
        toolbarTimKiem = findViewById(R.id.toolbarTimKiem);
        recyclerViewTimKiem = findViewById(R.id.recyclerViewTimKiem);
        txtSoLuongKetQuaTimKiem = findViewById(R.id.txtSoLuongKetQuaTimKiem);

        dsProduct = new ArrayList<>();
        productAdapter = new ProductAdapter(this, dsProduct);
        recyclerViewTimKiem.setAdapter(productAdapter);
        recyclerViewTimKiem.setLayoutManager(new GridLayoutManager(this, 2));
        productService = new ProductService(this, productAdapter, dsProduct, txtSoLuongKetQuaTimKiem);
    }
}