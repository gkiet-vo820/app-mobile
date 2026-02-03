package com.example.appbanhang.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
import com.example.appbanhang.adapter.OrdersAdapter;
import com.example.appbanhang.api.OrdersService;
import com.example.appbanhang.model.Orders;
import com.example.appbanhang.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class OrdersActivity extends AppCompatActivity {

    Toolbar toolbarDonHang;
    RecyclerView recyclerViewDonHang;

    LinearLayoutManager linearLayoutManager;

    OrdersService ordersService;
    OrdersAdapter ordersAdapter;
    List<Orders> dsOrders;

    boolean isLoading = false;
    boolean isLastPage = false;
    int page = 1, limit = 10, totalPage = 1;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_orders);

        addControls();
        addEvents();
        ActionBar();
        checkUserId();
    }

    private void checkUserId(){
        if (Utils.user_current != null) {
            userId = getIntent().getIntExtra("id", Utils.user_current.getId());
            showListDonHang(userId);
        }
        else {
            Toast.makeText(this, "Vui lòng đăng nhập!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    private void ActionBar() {
        setSupportActionBar(toolbarDonHang);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarDonHang.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addControls(){
        toolbarDonHang = findViewById(R.id.toolbarDonHang);
        recyclerViewDonHang = findViewById(R.id.recyclerViewDonHang);

        dsOrders = new ArrayList<>();
        ordersAdapter = new OrdersAdapter(this, dsOrders);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewDonHang.setLayoutManager(linearLayoutManager);
        recyclerViewDonHang.setAdapter(ordersAdapter);
        ordersService = new OrdersService(this, ordersAdapter, dsOrders);
    }

    private void addEvents(){
        recyclerViewDonHang.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(isLoading == false && isLastPage == false && page < totalPage){
                    if(linearLayoutManager.findLastVisibleItemPosition() == dsOrders.size() - 1){
                        isLoading = true;
                        loadMore();
                    }
                }
            }
        });
    }
    private void loadMore(){
        dsOrders.add(null);
        ordersAdapter.notifyItemInserted(dsOrders.size() - 1);
        page++;

        ordersService.loadOrderHistory(userId, page, limit, (tp, count) -> {
            totalPage = tp;
            if (page >= totalPage) {
                isLastPage = true;
            }
            isLoading = false;
        });
    }

    private void showListDonHang(int userId){
        page = 1;
        isLastPage = false;
        isLoading = false;
        dsOrders.clear();
        ordersService.loadOrderHistory(userId, page, limit, (tp, count) -> {
            totalPage = tp;
            if (totalPage <= 1) {
                isLastPage = true;
            }
        });
    }
    @Override
    protected void onDestroy() {
        if (ordersService != null) {
            ordersService.clear();
        }
        super.onDestroy();
    }
}