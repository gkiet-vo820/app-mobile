package com.example.appbanhang.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;


import com.example.appbanhang.R;
import com.example.appbanhang.adapter.OrdersAdapter;
import com.example.appbanhang.api.OrdersService;
import com.example.appbanhang.model.DetailOrders;
import com.example.appbanhang.model.Orders;
import com.example.appbanhang.model.ShoppingCart;
import com.example.appbanhang.util.CartStorage;
import com.example.appbanhang.util.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PaymentActivity extends AppCompatActivity {
    Toolbar toolbarThanhToan;
    TextView txtTongTien, txtEmail, txtSDT;
    TextInputEditText edtDiaChi;
    AppCompatButton btnDatHang;


    OrdersService ordersService;
    OrdersAdapter ordersAdapter;
    List<Orders> dsOrders;
    long tongtien;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment);

        addControls();
        getIntentData();
        addEvents();
        ActionBar();
        Log.d("CART_DEBUG", "Cart size = " + Utils.dsShoppingCart.size());

    }

    private void ActionBar() {
        setSupportActionBar(toolbarThanhToan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarThanhToan.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void addControls(){
        checkCart();

        toolbarThanhToan = findViewById(R.id.toolbarThanhToan);
        txtTongTien = findViewById(R.id.txtTongTien);
        txtEmail = findViewById(R.id.txtEmail);
        txtSDT = findViewById(R.id.txtSDT);
        edtDiaChi = findViewById(R.id.edtDiaChi);
        btnDatHang = findViewById(R.id.btnDatHang);

        dsOrders = new ArrayList<>();
        ordersAdapter = new OrdersAdapter(this, dsOrders);
        ordersService = new OrdersService(this, ordersAdapter, dsOrders);
        progressDialog = new ProgressDialog(this);
    }

    private void checkCart() {
        if (Utils.dsShoppingCart == null || Utils.dsShoppingCart.isEmpty()) {
            Utils.dsShoppingCart = CartStorage.loadCart(this);
        }
        if (Utils.dsShoppingCart == null) {
            Utils.dsShoppingCart = new ArrayList<>();
        }
    }

    private void getIntentData(){
        Intent intent = getIntent();
        tongtien = intent.getLongExtra("tongtien", 0);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        txtTongTien.setText(decimalFormat.format(tongtien)+"Đ");
        txtEmail.setText(Utils.user_current.getEmail());
        txtSDT.setText(Utils.user_current.getSdt());
    }
    private void addEvents(){
        btnDatHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order();
            }
        });
    }

    private void order(){
        if (Utils.dsShoppingCart == null || Utils.dsShoppingCart.isEmpty()) {
            Toast.makeText(this, "Giỏ hàng trống!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        String diachi = edtDiaChi.getText().toString().trim();
        if(diachi.isEmpty()){
            edtDiaChi.setError("Vui lòng nhập địa chỉ");
            edtDiaChi.requestFocus();
            return;
        }

        List<DetailOrders> details = new ArrayList<>();
        int totalQty = 0;
        for (ShoppingCart item : Utils.dsShoppingCart) {
            Log.d("ORDER_CHECK", "Sản phẩm: " + item.getTensp() + " | isSelected: " + item.isSelected());
            if(item.isSelected()){
                DetailOrders detail = new DetailOrders();
                detail.setProductId(item.getId());
                detail.setQuantity(item.getSoluong());
                detail.setPrice(item.getGiasp());
                details.add(detail);
                totalQty += item.getSoluong();
            }
        }
        if (details.isEmpty()) {
            Toast.makeText(this, "Giỏ hàng trống hoặc chưa có sản phẩm!", Toast.LENGTH_SHORT).show();
            return;
        }

        Orders order = new Orders();
        order.setUserId(Utils.user_current.getId());
        order.setEmail(Utils.user_current.getEmail());
        order.setNumberphone(Utils.user_current.getSdt());
        order.setAddress(diachi);
        order.setQuantity(totalQty);
        order.setTotalpayment(tongtien);
        order.setDetailOrders(details);

        progressDialog.setMessage("Đang xử lý đơn hàng...");
        progressDialog.setCancelable(false);
        ordersService.postOrder(order, new OrdersService.OrderCallback(){
            @Override
            public void onStart() {
                progressDialog.show();
            }

            @Override
            public void onFinish() {
                if(progressDialog != null && progressDialog.isShowing()){
                    progressDialog.dismiss();
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