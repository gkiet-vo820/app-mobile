package com.example.appbanhang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.R;
import com.example.appbanhang.adapter.ShoppingCartAdapter;
import com.example.appbanhang.model.ShoppingCart;
import com.example.appbanhang.model.eventbus.TotalEvent;
import com.example.appbanhang.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.List;

public class ShoppingCartActivity extends AppCompatActivity {
    TextView gioHangTrong, txtTongTien, txtChinhSua, txtChonTatCa, txtXoaTatCa;
    Toolbar toolbarGioHang;
    RecyclerView recyclerViewGioHang;
    AppCompatButton btnMuaHang;

    ShoppingCartAdapter shoppingCartAdapter;

    boolean isEditing = false;
    double tongtien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shopping_cart);

        addControls();
        addEvents();
        ActionBar();
        totalPayment();
    }


    private void ActionBar() {
        setSupportActionBar(toolbarGioHang);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarGioHang.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void addControls(){
        gioHangTrong = findViewById(R.id.txtGioHangTrong);
        toolbarGioHang = findViewById(R.id.toolbarGioHang);
        recyclerViewGioHang = findViewById(R.id.recyclerViewGioHang);
        txtTongTien = findViewById(R.id.txtTongTien);
        btnMuaHang = findViewById(R.id.btnMuaHang);

        recyclerViewGioHang.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewGioHang.setLayoutManager(layoutManager);

        shoppingCartAdapter = new ShoppingCartAdapter(this,Utils.dsShoppingCart);
        recyclerViewGioHang.setAdapter(shoppingCartAdapter);
        checkEmptyCart();

        txtChinhSua = findViewById(R.id.txtChinhSua);
        txtChonTatCa = findViewById(R.id.txtChonTatCa);
        txtXoaTatCa = findViewById(R.id.txtXoaTatCa);
    }

    private void checkEmptyCart(){
        if(Utils.dsShoppingCart == null || Utils.dsShoppingCart.size() == 0){
            gioHangTrong.setVisibility(View.VISIBLE);
            recyclerViewGioHang.setVisibility(View.GONE);
            btnMuaHang.setEnabled(false);
            txtTongTien.setText("0Đ");
        }
        else{
            gioHangTrong.setVisibility(View.GONE);
            recyclerViewGioHang.setVisibility(View.VISIBLE);
            btnMuaHang.setEnabled(true);
        }
    }
    private void addEvents() {
        btnMuaHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShoppingCartActivity.this, PaymentActivity.class);
                intent.putExtra("tongtien", tongtien);
                startActivity(intent);
                setActiveButton(btnMuaHang);

            }
        });

        txtChinhSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEditing = !isEditing;
                if (isEditing) {
                    txtChinhSua.setText(R.string.hoantat);
                    txtChonTatCa.setVisibility(View.VISIBLE);
                    txtXoaTatCa.setVisibility(View.VISIBLE);
                    if (shoppingCartAdapter != null) shoppingCartAdapter.setEditMode(true);
                } else {
                    txtChinhSua.setText(R.string.chinhsua);
                    txtChonTatCa.setVisibility(View.GONE);
                    txtXoaTatCa.setVisibility(View.GONE);
                    if (shoppingCartAdapter != null) shoppingCartAdapter.setEditMode(false);
                }
            }
        });

        txtChonTatCa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (ShoppingCart item : Utils.dsShoppingCart) {
                    item.setSelected(true);
                }
                shoppingCartAdapter.notifyDataSetChanged();
            }
        });

        txtXoaTatCa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = Utils.dsShoppingCart.size() - 1; i >= 0; i--) {
                    if (Utils.dsShoppingCart.get(i).isSelected()) {
                        Utils.dsShoppingCart.remove(i);
                    }
                }
                shoppingCartAdapter.notifyDataSetChanged();
                EventBus.getDefault().postSticky(new TotalEvent());
            }
        });
    }

    private void totalPayment(){
        tongtien = 0;
        if(Utils.dsShoppingCart != null) {
            for (int i = 0; i < Utils.dsShoppingCart.size(); i++) {
                tongtien += Utils.dsShoppingCart.get(i).getGiasp() * Utils.dsShoppingCart.get(i).getSoluong();
            }
        }
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            txtTongTien.setText(decimalFormat.format(tongtien)+ "Đ");
    }

    private void setActiveButton(Button button) {
        btnMuaHang.setBackgroundResource(R.drawable.button_inactive);

        btnMuaHang.setTextColor(getColor(android.R.color.black));

        button.setBackgroundResource(R.drawable.button_active);
        button.setTextColor(getColor(android.R.color.white));
    }
    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void eventTinhTien(TotalEvent totalEvent){
        if(totalEvent != null){
            totalPayment();
            checkEmptyCart();
        }
    }
}