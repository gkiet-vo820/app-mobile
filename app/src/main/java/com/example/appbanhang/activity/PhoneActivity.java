package com.example.appbanhang.activity;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.R;
import com.example.appbanhang.adapter.PhoneAdapter;
import com.example.appbanhang.api.PhoneService;
import com.example.appbanhang.model.Product;
import java.util.ArrayList;
import java.util.List;

public class PhoneActivity extends AppCompatActivity {

    Toolbar toolBarDienThoai;
    RecyclerView recyclerViewDienThoai;
    PhoneAdapter phoneAdapter;
    List<Product> dsProduct;
    PhoneService phoneService;

    LinearLayoutManager linearLayoutManager;
    boolean isLoading = false;
    boolean isLastPage = false;

    int page = 0, limit = 10, totalPage = 1;
    int loai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_phone);
        addControls();
        addEvents();
        ActionBar();

        loai = getIntent().getIntExtra("loai", 1);
        showListDienThoai(loai);
    }

    private void ActionBar() {
        setSupportActionBar(toolBarDienThoai);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBarDienThoai.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void addControls(){
        toolBarDienThoai = findViewById(R.id.toolBarDienThoai);
        recyclerViewDienThoai = findViewById(R.id.recyclerViewDienThoai);

        linearLayoutManager = new LinearLayoutManager(this);

        dsProduct = new ArrayList<>();
        phoneAdapter = new PhoneAdapter(this, dsProduct);
        recyclerViewDienThoai.setAdapter(phoneAdapter);
        recyclerViewDienThoai.setLayoutManager(linearLayoutManager);
        //recyclerViewDienThoai.setLayoutManager(new GridLayoutManager(this,1));
        phoneService = new PhoneService(this, phoneAdapter, dsProduct);

    }

    protected void onDestroy() {
        super.onDestroy();
        phoneService.clear();
    }

    private void addEvents(){
        recyclerViewDienThoai.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(isLoading == false && isLastPage == false && page < totalPage - 1){
                    if(linearLayoutManager.findLastVisibleItemPosition() == dsProduct.size() - 1){
                        isLoading = true;
                        loadMore();
                    }
                }
            }
        });
    }

    private void loadMore(){
        page++;

        phoneService.getAllDienThoai(loai, page, limit,
                (tp, count) -> {
                    totalPage = tp;
                    if (page >= totalPage - 1 || count == 0) {
                        isLastPage = true;
                    }
                    isLoading = false;
                }
        );
    }
    private void showListDienThoai(int loai){
        page = 0;
        isLastPage = false;
        isLoading = false;


        phoneService.getAllDienThoai(loai, page, limit,
                (tp, count) -> {
                    totalPage = tp;

                    if (totalPage <= 1 || count == 0) {
                        isLastPage = true;
                    }
                }
        );
    }
}