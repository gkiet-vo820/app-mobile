package com.example.appbanhang.activity.product;

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
import com.example.appbanhang.adapter.product.PhoneAdapter;
import com.example.appbanhang.api.product.PhoneService;
import com.example.appbanhang.model.Product;
import java.util.ArrayList;
import java.util.List;

public class PhoneActivity extends AppCompatActivity {

    TextView txtSoLuongKetQuaTimKiem;

    SearchView searchView;
    Toolbar toolBarDienThoai;
    RecyclerView recyclerViewDienThoai;
    PhoneAdapter phoneAdapter;
    List<Product> dsProduct;
    PhoneService phoneService;

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
        setContentView(R.layout.activity_phone);

        addControls();
        getIntentData();
        addEvents();
        ActionBar();
    }

    private void getIntentData(){
        //có thể nhận đặt mặc định là 0 và hứng trực tiếp
        loai = getIntent().getIntExtra("loai", -1);
        if (loai != -1) {
            showListDienThoai(loai);
        } else {
            Toast.makeText(this, "Không tìm thấy loại sản phẩm!", Toast.LENGTH_SHORT).show();
        }
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

        searchView = findViewById(R.id.searchView);
        txtSoLuongKetQuaTimKiem = findViewById(R.id.txtSoLuongKetQuaTimKiem);

        linearLayoutManager = new LinearLayoutManager(this);

        dsProduct = new ArrayList<>();
        phoneAdapter = new PhoneAdapter(this, dsProduct);
        recyclerViewDienThoai.setAdapter(phoneAdapter);
        recyclerViewDienThoai.setLayoutManager(linearLayoutManager);
        phoneService = new PhoneService(this, phoneAdapter, dsProduct, txtSoLuongKetQuaTimKiem);


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

        phoneAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (!isLongClick) {
                    Intent intent = new Intent(PhoneActivity.this, DetailActivity.class);
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
                    phoneService.searchDienThoai(query, loai);
                    searchView.clearFocus();
                    txtSoLuongKetQuaTimKiem.setVisibility(View.VISIBLE);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.trim().isEmpty()){
                    isSearching = false;
                    showListDienThoai(loai);
                    txtSoLuongKetQuaTimKiem.setVisibility(View.GONE);
                }
                return true;
            }
        });
    }

    private void loadMore(){
        dsProduct.add(null);
        phoneAdapter.notifyItemInserted(dsProduct.size() - 1);
        page++;

        phoneService.getAllDienThoai(loai, page, limit, (tp, count) -> {
            totalPage = tp;
            if (page >= totalPage) {
                isLastPage = true;
            }
            isLoading = false;
        });
    }
    private void showListDienThoai(int loai){
        page = 1;
        isLastPage = false;
        isLoading = false;
        dsProduct.clear();

        phoneService.getAllDienThoai(loai, page, limit, (tp, count) -> {
            totalPage = tp;
            if (totalPage <= 1) {
                isLastPage = true;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        phoneService.clear();
    }
}