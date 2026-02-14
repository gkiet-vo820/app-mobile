package com.example.appbanhang.activity;

import android.content.Intent;
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

    Toolbar toolbarLichSuDatHang;
    RecyclerView recyclerViewLichSuDatHang;
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
        setSupportActionBar(toolbarLichSuDatHang);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbarLichSuDatHang.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addControls(){
        toolbarLichSuDatHang = findViewById(R.id.toolbarLichSuDatHang);
        recyclerViewLichSuDatHang = findViewById(R.id.recyclerViewLichSuDatHang);

        dsOrders = new ArrayList<>();
        ordersAdapter = new OrdersAdapter(this, dsOrders);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewLichSuDatHang.setLayoutManager(linearLayoutManager);
        recyclerViewLichSuDatHang.setAdapter(ordersAdapter);
        ordersService = new OrdersService(this, ordersAdapter, dsOrders);
    }

    private void addEvents(){
        recyclerViewLichSuDatHang.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

        ordersAdapter.setItemClickListener((view, position, isLongClick) -> {
            Orders orders = dsOrders.get(position);
            if(orders != null){
                Intent intent = new Intent(OrdersActivity.this, OrderDetailActivity.class);
                intent.putExtra("order_id", orders.getId());
                startActivity(intent);
            }
        });
    }
    private void loadMore() {
        dsOrders.add(null);
        ordersAdapter.notifyItemInserted(dsOrders.size() - 1);
        page++;

        ordersService.loadOrderHistory(userId, page, limit, new OrdersService.PageCallback() {
            @Override
            public void onResult(int tp, int count) {
                totalPage = tp;
                if (page >= totalPage) {
                    isLastPage = true;
                }
                isLoading = false;
            }

            @Override
            public void onError(String message) {
                isLoading = false;
                Toast.makeText(OrdersActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showListDonHang(int userId) {
        page = 1;
        isLastPage = false;
        isLoading = true;
        dsOrders.clear();
        ordersAdapter.notifyDataSetChanged();

        ordersService.loadOrderHistory(userId, page, limit, new OrdersService.PageCallback() {
            @Override
            public void onResult(int tp, int count) {
//                    totalPage = tp;
//                    isLoading = false;
//                    if (totalPage <= 1) {
//                        isLastPage = true;
//                    }
                totalPage = tp;
                isLoading = false;
                if (page >= totalPage) { // Dùng >= cho chắc chắn
                    isLastPage = true;
                }
            }

            @Override
            public void onError(String message) {
                isLoading = false;
                // Nếu là trang 1 mà lỗi thì mới báo "Không có đơn hàng"
                if (page == 1) {
                    Toast.makeText(OrdersActivity.this, "Không thể tải danh sách đơn hàng", Toast.LENGTH_SHORT).show();
                }
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