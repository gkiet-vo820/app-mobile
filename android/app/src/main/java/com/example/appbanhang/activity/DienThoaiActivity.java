package com.example.appbanhang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.R;
import com.example.appbanhang.adapter.DienThoaiAdapter;
import com.example.appbanhang.api.DienThoaiService;
import com.example.appbanhang.model.SanPham;
import java.util.ArrayList;
import java.util.List;

public class DienThoaiActivity extends AppCompatActivity {

    Toolbar toolBarDienThoai;
    RecyclerView recyclerViewDienThoai;
    DienThoaiAdapter dienThoaiAdapter;
    List<SanPham> dsSanPham;
    DienThoaiService dienThoaiService;

    LinearLayoutManager linearLayoutManager;
    boolean isLoading = false;
    boolean isLastPage = false;

    int page = 0, limit = 10, totalPage = 1;
    int loai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dien_thoai);
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
        toolBarDienThoai = findViewById(R.id.toolBarDienThoai);
        recyclerViewDienThoai = findViewById(R.id.recyclerViewDienThoai);

        linearLayoutManager = new LinearLayoutManager(this);

        dsSanPham = new ArrayList<>();
        dienThoaiAdapter = new DienThoaiAdapter(this, dsSanPham);
        recyclerViewDienThoai.setAdapter(dienThoaiAdapter);
        recyclerViewDienThoai.setLayoutManager(linearLayoutManager);
        //recyclerViewDienThoai.setLayoutManager(new GridLayoutManager(this,1));
        dienThoaiService = new DienThoaiService(this,dienThoaiAdapter,dsSanPham);


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

        dienThoaiService.getAllDienThoai(loai, page, limit,
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


        dienThoaiService.getAllDienThoai(loai, page, limit,
                (tp, count) -> {
                    totalPage = tp;

                    if (totalPage <= 1 || count == 0) {
                        isLastPage = true;
                    }
                }
        );
    }
}