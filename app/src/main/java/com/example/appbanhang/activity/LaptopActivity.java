package com.example.appbanhang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.R;
import com.example.appbanhang.adapter.LaptopAdapter;
import com.example.appbanhang.api.LaptopService;
import com.example.appbanhang.model.SanPham;

import java.util.ArrayList;
import java.util.List;

public class LaptopActivity extends AppCompatActivity {

    Toolbar toolBarLaptop;
    RecyclerView recyclerViewLaptop;
    LaptopAdapter laptopAdapter;
    List<SanPham> dsSanPham;
    LaptopService laptopService;

    LinearLayoutManager linearLayoutManager;
    boolean isLoading = false;
    boolean isLastPage = false;

    int page = 0, limit = 10, totalPage = 1;
    int loai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_laptop);
        addControls();
        addEvents();
        ActionBar();

        loai = getIntent().getIntExtra("loai", 2);
        showListLaptop(loai);
    }

    private void ActionBar() {
        setSupportActionBar(toolBarLaptop);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBarLaptop.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            Intent intent = new Intent(this, MainActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//            finish();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
    private void addControls(){
        toolBarLaptop = findViewById(R.id.toolBarLaptop);
        recyclerViewLaptop = findViewById(R.id.recyclerViewLaptop);

        linearLayoutManager = new LinearLayoutManager(this);

        dsSanPham = new ArrayList<>();
        laptopAdapter = new LaptopAdapter(this, dsSanPham);
        recyclerViewLaptop.setAdapter(laptopAdapter);
        recyclerViewLaptop.setLayoutManager(linearLayoutManager);
        //recyclerViewLaptop.setLayoutManager(new GridLayoutManager(this,1));
        laptopService = new LaptopService(this,laptopAdapter,dsSanPham);


    }

    private void addEvents(){
        recyclerViewLaptop.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(isLoading == false && isLastPage == false && page < totalPage - 1){
                    if(linearLayoutManager.findLastVisibleItemPosition() == dsSanPham.size() - 1){
                        isLoading = true;
                        loadMore();
                    }
                }
            }
        });
    }

    private void loadMore(){
        page++;

        laptopService.getAllLaptop(loai, page, limit,
                (tp, count) -> {
                    totalPage = tp;
                    if (page >= totalPage - 1 || count == 0) {
                        isLastPage = true;
                    }
                    isLoading = false;
                }
        );
    }
    private void showListLaptop(int loai){
        page = 0;
        isLastPage = false;
        isLoading = false;


        laptopService.getAllLaptop(loai, page, limit,
                (tp, count) -> {
                    totalPage = tp;

                    if (totalPage <= 1 || count == 0) {
                        isLastPage = true;
                    }
                }
        );
    }
}