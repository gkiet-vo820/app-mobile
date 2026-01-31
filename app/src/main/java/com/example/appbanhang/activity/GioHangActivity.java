package com.example.appbanhang.activity;

import android.media.metrics.Event;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.R;
import com.example.appbanhang.adapter.GioHangAdapter;
import com.example.appbanhang.model.GioHang;
import com.example.appbanhang.model.eventbus.TinhTongEvent;
import com.example.appbanhang.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.List;

public class GioHangActivity extends AppCompatActivity {
    TextView gioHangTrong, txtTongTien, txtChinhSua;
    Toolbar toolbarGioHang;
    RecyclerView recyclerViewGioHang;
    Button btnMuaHang;

    GioHangAdapter gioHangAdapter;
    List<GioHang> gioHangList;

    boolean isEditing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gio_hang);
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

        if(Utils.dsGioHang.size() == 0){
            gioHangTrong.setVisibility(View.VISIBLE);
        }
        else{
            gioHangAdapter = new GioHangAdapter(this,Utils.dsGioHang);
            recyclerViewGioHang.setAdapter(gioHangAdapter);
        }

        txtChinhSua = findViewById(R.id.txtChinhSua);
    }

    private void addEvents() {
        btnMuaHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        txtChinhSua.setOnClickListener(v -> {
            isEditing = !isEditing;
            if (isEditing) {
                txtChinhSua.setText("Hoàn tất");
                if (gioHangAdapter != null) gioHangAdapter.setEditMode(true);
            } else {
                txtChinhSua.setText("Chỉnh sửa");
                if (gioHangAdapter != null) gioHangAdapter.setEditMode(false);
            }
        });
    }

    private void tinhTongTien(){
        double tongtien = 0;
        for (int i = 0; i < Utils.dsGioHang.size(); i++){
            tongtien += Utils.dsGioHang.get(i).getGiasp() * Utils.dsGioHang.get(i).getSoluong();
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        txtTongTien.setText(decimalFormat.format(tongtien)+ "Đ");
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
    public void eventTinhTien(TinhTongEvent tinhTongEvent){
        if(tinhTongEvent != null){
            tinhTongTien();
        }
    }
}