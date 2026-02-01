package com.example.appbanhang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;


import com.example.appbanhang.R;
import com.example.appbanhang.util.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.text.DecimalFormat;

public class PaymentActivity extends AppCompatActivity {
    Toolbar toolbarThanhToan;
    TextView txtTongTien, txtEmail, txtSDT;
    TextInputEditText edtDiaChi;
    AppCompatButton btnDatHang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment);

        addControls();
        getIntentData();
        addEvents();
        ActionBar();
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
        toolbarThanhToan = findViewById(R.id.toolbarThanhToan);
        txtTongTien = findViewById(R.id.txtTongTien);
        txtEmail = findViewById(R.id.txtEmail);
        txtSDT = findViewById(R.id.txtSDT);
        edtDiaChi = findViewById(R.id.edtDiaChi);
        btnDatHang = findViewById(R.id.btnDatHang);
    }

    private void getIntentData(){
        Intent intent = getIntent();
        double tongtien = intent.getDoubleExtra("tongtien", 0);
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
        String diachi = edtDiaChi.getText().toString().trim();
        if(diachi.isEmpty()){
            edtDiaChi.setError("Vui lòng nhập địa chỉ");
            edtDiaChi.requestFocus();
            return;
        }
        else {
            Log.d("test", new Gson().toJson(Utils.dsShoppingCart));
        }
    }
}