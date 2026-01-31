package com.example.appbanhang.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
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
    TextView gioHangTrong, txtTongTien, txtChinhSua;
    Toolbar toolbarGioHang;
    RecyclerView recyclerViewGioHang;
    Button btnMuaHang;

    ShoppingCartAdapter shoppingCartAdapter;
    List<ShoppingCart> shoppingCartList;

    boolean isEditing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shopping_cart);
        addControls();
        addEvents();
        ActionBar();
        tinhTongTien();
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

        if(Utils.dsShoppingCart.size() == 0){
            gioHangTrong.setVisibility(View.VISIBLE);
        }
        else{
            shoppingCartAdapter = new ShoppingCartAdapter(this,Utils.dsShoppingCart);
            recyclerViewGioHang.setAdapter(shoppingCartAdapter);
        }

        txtChinhSua = findViewById(R.id.txtChinhSua);
    }

    private void addEvents() {
        btnMuaHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setActiveButton(btnMuaHang);
            }
        });

        txtChinhSua.setOnClickListener(v -> {
            isEditing = !isEditing;
            if (isEditing) {
                txtChinhSua.setText(R.string.hoantat);
                if (shoppingCartAdapter != null) shoppingCartAdapter.setEditMode(true);
            } else {
                txtChinhSua.setText(R.string.chinhsua);
                if (shoppingCartAdapter != null) shoppingCartAdapter.setEditMode(false);
            }
        });
    }

    private void tinhTongTien(){
        double tongtien = 0;
        for (int i = 0; i < Utils.dsShoppingCart.size(); i++){
            tongtien += Utils.dsShoppingCart.get(i).getGiasp() * Utils.dsShoppingCart.get(i).getSoluong();
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
            tinhTongTien();
        }
    }
}