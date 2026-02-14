package com.example.appbanhang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.R;
import com.example.appbanhang.adapter.OrdersDetailAdapter;
import com.example.appbanhang.api.OrderDetailService;
import com.example.appbanhang.model.DetailOrders;
import com.example.appbanhang.model.Orders;
import com.example.appbanhang.util.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {

    Toolbar toolbarDonHang;
    RecyclerView recyclerViewDonHang;
    TextView txtMaDonHang, txtTrangThaiDonHang, txtDiaChiDonHang, txtTongTienDonHang;
    OrderDetailService orderDetailService;
    OrdersDetailAdapter ordersDetailAdapter;
    List<DetailOrders> dsDetailOrders;
    int orderId;
    AppCompatButton btnChat;
    String tenAdminChat = "triet";
    String avatarAdminChat = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_detail);

        addControls();
        getIntentData();
        ActionBar();
        addEvents();
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
        txtMaDonHang = findViewById(R.id.txtMaDonHang);
        txtTrangThaiDonHang = findViewById(R.id.txtTrangThaiDonHang);
        txtDiaChiDonHang = findViewById(R.id.txtDiaChiDonHang);
        txtTongTienDonHang = findViewById(R.id.txtTongTienDonHang);
        btnChat = findViewById(R.id.btnChat);

        dsDetailOrders = new ArrayList<>();
        ordersDetailAdapter = new OrdersDetailAdapter(this, dsDetailOrders);
        recyclerViewDonHang.setAdapter(ordersDetailAdapter);
        recyclerViewDonHang.setLayoutManager(new LinearLayoutManager(this));
        orderDetailService = new OrderDetailService(this);
    }

    private void getIntentData(){
        orderId = getIntent().getIntExtra("order_id", 0);

        if(orderId > 0){
            loadOrderDetail(orderId);
        } else {
            Toast.makeText(this, "Không tìm thấy đơn hàng", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadOrderDetail(int orderId){
        orderDetailService.getOrderDetail(orderId, new OrderDetailService.OrderDetailCallback() {
            @Override
            public void onSuccess(Orders orders) {
                getSupportActionBar().setTitle("Chi tiết đơn hàng #" + orders.getId());
                txtMaDonHang.setText("Đơn hàng: #" + orders.getId());

                Utils.setStatus(txtTrangThaiDonHang, orders.getStatus());
                txtDiaChiDonHang.setText("Địa chỉ: " + orders.getAddress());

                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                txtTongTienDonHang.setText("Tổng tiền: " + decimalFormat.format(orders.getTotalpayment()) + "Đ");

                ordersDetailAdapter = new OrdersDetailAdapter(OrderDetailActivity.this, orders.getDetailOrders());
                recyclerViewDonHang.setAdapter(ordersDetailAdapter);
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(OrderDetailActivity.this, "Lỗi: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addEvents(){
        if (Utils.user_current.getRole() == 1) {
            btnChat.setVisibility(View.GONE);
        }
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idAdmin = 3;

                Intent intent = new Intent(OrderDetailActivity.this, ChatDetailActivity.class);
                intent.putExtra("id_nhan", idAdmin);
                intent.putExtra("ten_nhan", tenAdminChat);
                intent.putExtra("avatar_nhan", avatarAdminChat);

                String maDon = txtMaDonHang.getText().toString();
                intent.putExtra("noi_dung_mau", "Tôi cần hỗ trợ về " + maDon);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        if(orderDetailService != null)
            orderDetailService.clear();
        super.onDestroy();
    }
}