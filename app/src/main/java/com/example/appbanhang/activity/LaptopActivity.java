package com.example.appbanhang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.R;
import com.example.appbanhang.listener.ItemClickListener;
import com.example.appbanhang.adapter.LaptopAdapter;
import com.example.appbanhang.api.LaptopService;
import com.example.appbanhang.model.Product;

import java.util.ArrayList;
import java.util.List;

public class LaptopActivity extends AppCompatActivity {

    TextView txtSoLuongKetQuaTimKiem;
    SearchView searchView;
    Toolbar toolBarLaptop;
    RecyclerView recyclerViewLaptop;
    LaptopAdapter laptopAdapter;
    List<Product> dsProduct;
    LaptopService laptopService;

    LinearLayoutManager linearLayoutManager;

    boolean isLoading = false;
    boolean isLastPage = false;
    boolean isSearching = false;

    int page = 1, limit = 10, totalPage = 1;
    int loai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_laptop);

        addControls();
        getIntentData();
        addEvents();
        ActionBar();
    }

    private void getIntentData(){
        //có thể nhận đặt mặc định là 0 và hứng trực tiếp
        loai = getIntent().getIntExtra("loai", -1);
        if (loai != -1) {
            showListLaptop(loai);
        } else {
            Toast.makeText(this, "Không tìm thấy loại sản phẩm!", Toast.LENGTH_SHORT).show();
        }
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

    private void addControls(){
        toolBarLaptop = findViewById(R.id.toolBarLaptop);
        recyclerViewLaptop = findViewById(R.id.recyclerViewLaptop);

        searchView = findViewById(R.id.searchView);
        txtSoLuongKetQuaTimKiem = findViewById(R.id.txtSoLuongKetQuaTimKiem);

        linearLayoutManager = new LinearLayoutManager(this);
        dsProduct = new ArrayList<>();
        laptopAdapter = new LaptopAdapter(this, dsProduct);
        recyclerViewLaptop.setAdapter(laptopAdapter);
        recyclerViewLaptop.setLayoutManager(linearLayoutManager);
        laptopService = new LaptopService(this,laptopAdapter, dsProduct, txtSoLuongKetQuaTimKiem);

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
                if(!isSearching){
                    if(isLoading == false && isLastPage == false && page < totalPage){
                        if(linearLayoutManager.findLastVisibleItemPosition() == dsProduct.size() - 1){
                            isLoading = true;
                            loadMore();
                        }
                    }
                }
            }
        });

        laptopAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (!isLongClick) {
                    Intent intent = new Intent(LaptopActivity.this, DetailActivity.class);
                    intent.putExtra("chitiet", dsProduct.get(position));
                    startActivity(intent);
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!query.trim().isEmpty()){
                    isSearching = true;
                    laptopService.searchLaptop(query, loai);
                    searchView.clearFocus();
                    txtSoLuongKetQuaTimKiem.setVisibility(View.VISIBLE);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.trim().isEmpty()){
                    isSearching = false;
                    showListLaptop(loai);
                    txtSoLuongKetQuaTimKiem.setVisibility(View.GONE);
                }
                return true;
            }
        });
    }

    private void loadMore(){
        dsProduct.add(null);
        laptopAdapter.notifyItemInserted(dsProduct.size() - 1);
        page++;

        laptopService.getAllLaptop(loai, page, limit, (tp, count) -> {
            totalPage = tp;
            if (page >= totalPage) {
                isLastPage = true;
            }
            isLoading = false;
        });
    }
    private void showListLaptop(int loai){
        page = 1;
        isLastPage = false;
        isLoading = false;
        dsProduct.clear();

        laptopService.getAllLaptop(loai, page, limit, (tp, count) -> {
            totalPage = tp;
            if (totalPage <= 1) {
                isLastPage = true;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        laptopService.clear();
    }
}