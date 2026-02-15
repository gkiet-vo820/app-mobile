package com.example.appbanhang.activity.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.R;
import com.example.appbanhang.adapter.admin.ManagerOrdersAdapter;
import com.example.appbanhang.api.order.OrdersService;
import com.example.appbanhang.api.admin.ManagerOrdersService;
import com.example.appbanhang.model.Orders;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class ManagerOrdersActivity extends AppCompatActivity {
    Toolbar toolbarQuanLyDonHang;
    TabLayout tabLayoutStatus;
    RecyclerView recyclerViewQuanLyDonHang;
    LinearLayoutManager linearLayoutManager;
    ManagerOrdersService managerOrdersService;
    ManagerOrdersAdapter managerOrdersAdapter;
    List<Orders> dsOrders;

    boolean isLoading = false;
    boolean isLastPage = false;
    int page = 1, limit = 10, totalPage = 1, currentStatus = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manager_orders);

        addControls();
        addEvents();
        ActionBar();

        showOrdersByStatus(currentStatus);
    }
    private void ActionBar() {
        setSupportActionBar(toolbarQuanLyDonHang);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbarQuanLyDonHang.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addControls(){
        toolbarQuanLyDonHang = findViewById(R.id.toolbarQuanLyDonHang);
        tabLayoutStatus = findViewById(R.id.tabLayoutStatus);
        recyclerViewQuanLyDonHang = findViewById(R.id.recyclerViewQuanLyDonHang);

        dsOrders = new ArrayList<>();
        managerOrdersAdapter = new ManagerOrdersAdapter(this, dsOrders);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewQuanLyDonHang.setLayoutManager(linearLayoutManager);
        recyclerViewQuanLyDonHang.setAdapter(managerOrdersAdapter);

        managerOrdersService = new ManagerOrdersService(this, managerOrdersAdapter, dsOrders);
        managerOrdersAdapter.setManagerOrdersService(managerOrdersService);


        if (tabLayoutStatus.getTabAt(0) != null) {
            tabLayoutStatus.getTabAt(0).select();
        }
    }

    private void addEvents(){
        tabLayoutStatus.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                isLoading = false;
                currentStatus = position == 0 ? -1 : position - 1;
                android.util.Log.d("TAB_CLICK", "Bấm vào vị trí: " + position + " | Status gửi đi: " + currentStatus);
                showOrdersByStatus(currentStatus);

//                switch (position){
//                    case 0: // Tab Tất cả
//                        currentStatus = -1;
//                        showOrdersByStatus(currentStatus);
//                        break;
//                    case 1: // Tab Chờ thanh toán (Status 0)
//                        currentStatus = 0;
//                        showOrdersByStatus(0);
//                        break;
//                    case 2: // Tab Đã thanh toán (Status 1)
//                        currentStatus = 1;
//                        showOrdersByStatus(1);
//                        break;
//                    case 3: // Tab Chờ xử lý (Status 2)
//                        currentStatus = 2;
//                        showOrdersByStatus(2);
//                        break;
//                    case 4: // Tab Đang giao (Status 3)
//                        currentStatus = 3;
//                        showOrdersByStatus(3);
//                        break;
//                    case 5: // Tab Đã giao (Status 4)
//                        currentStatus = 4;
//                        showOrdersByStatus(4);
//                        break;
//                    case 6: // Tab Đã hủy (Status 5)
//                        currentStatus = 5;
//                        showOrdersByStatus(5);
//                        break;
//                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        recyclerViewQuanLyDonHang.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

    private void showOrdersByStatus(int status){
        page = 1;
        isLastPage = false;
        isLoading = false;
        dsOrders.clear();
        managerOrdersAdapter.notifyDataSetChanged();

        isLoading = true;
        getStatus(status);
    }

    private void loadMore() {
        if (isLoading) return;
        isLoading = true;
        recyclerViewQuanLyDonHang.post(() -> {
            if (!isLastPage) {
                dsOrders.add(null);
                managerOrdersAdapter.notifyItemInserted(dsOrders.size() - 1);
                page++;
                getStatus(currentStatus);
            }
        });
    }

    private void getStatus(int status){
        OrdersService.PageCallback callback = new OrdersService.PageCallback() {
            @Override
            public void onResult(int tp, int count) {
                totalPage = tp;
                isLoading = false;
                if (page >= totalPage) isLastPage = true;
            }

            @Override
            public void onError(String message) {
                isLoading = false;
                Toast.makeText(ManagerOrdersActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        };

        if (status == -1) {
            managerOrdersService.loadAllOrdersForAdmin(page, limit, callback);
        } else {
            managerOrdersService.getByStatusAdmin(status, page, limit, callback);
        }
    }

    @Override
    protected void onDestroy() {
        if (managerOrdersService != null) {
            managerOrdersService.clear();
        }
        super.onDestroy();
    }
}