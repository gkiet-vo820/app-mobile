package com.example.appbanhang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.appbanhang.R;
import com.example.appbanhang.model.GioHang;
import com.example.appbanhang.model.SanPham;
import com.example.appbanhang.util.Utils;
import com.nex3z.notificationbadge.NotificationBadge;

import java.text.DecimalFormat;
import java.util.List;

public class ChiTietActivity extends AppCompatActivity {
    Toolbar toolBarChiTietSP;
    TextView txtTenChiTietSP, txtGiaChiTietSP, txtMoTaChiTietSP;
    ImageView imgChiTietSP;
    Button btnThemVaoGioHang;
    Spinner spinner;
    FrameLayout frameLayoutGioHang;

    SanPham sanPham;
    int loai;

    NotificationBadge notificationBadge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chi_tiet);
        addControls();
        ActionBar();
        getIntentData();
        addEvents();
    }

    private void getIntentData(){
        Intent intent = getIntent();
        sanPham = (SanPham) intent.getSerializableExtra("chitiet");
        txtTenChiTietSP.setText(sanPham.getTensp());
        String gia = String.valueOf(sanPham.getGia());
        if(gia != null && !gia.isEmpty()){
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            txtGiaChiTietSP.setText("Giá: " + decimalFormat.format(Double.parseDouble(gia)) + "Đ");
        }
        else{
            txtGiaChiTietSP.setText("Giá đang được cập nhật");
        }
        txtMoTaChiTietSP.setText(sanPham.getMota());
        Glide.with(this).load(sanPham.getHinhanh()).into(imgChiTietSP);
        Integer[] so = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        ArrayAdapter<Integer> adapterSpinner = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item, so);
        spinner.setAdapter(adapterSpinner);
    }
    private void ActionBar() {
        setSupportActionBar(toolBarChiTietSP);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBarChiTietSP.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            Intent intent;
//            if(loai == 1){
//                intent = new Intent(this, DienThoaiActivity.class);
//                intent.putExtra("loai", 1);
//            }
//            else if (loai == 2) {
//                intent = new Intent(this, LaptopActivity.class);
//                intent.putExtra("loai", 2);
//            }
//            else {
//                intent = new Intent(this, MainActivity.class);
//            }
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//            finish();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    private void addControls() {
        toolBarChiTietSP = findViewById(R.id.toolBarChiTietSP);
        txtTenChiTietSP = findViewById(R.id.txtTenChiTietSP);
        txtGiaChiTietSP = findViewById(R.id.txtGiaChiTietSP);
        txtMoTaChiTietSP = findViewById(R.id.txtMoTaChiTietSP);
        imgChiTietSP = findViewById(R.id.imgChiTietSP);
        btnThemVaoGioHang = findViewById(R.id.btnThemVaoGioHang);
        spinner = findViewById(R.id.spinner);
        frameLayoutGioHang = findViewById(R.id.frameLayoutGioHang);
        loai = getIntent().getIntExtra("loai", 0);

        notificationBadge = findViewById(R.id.menuSoLuong);

    }

    private void addEvents(){
        btnThemVaoGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                themGioHang();
            }
        });
        frameLayoutGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gioHang =  new Intent(ChiTietActivity.this, GioHangActivity.class);
                startActivity(gioHang);
            }
        });
        if(Utils.dsGioHang != null){
            int totalItem = 0;
            for(int i = 0; i < Utils.dsGioHang.size(); i++){
                totalItem += Utils.dsGioHang.get(i).getSoluong();
            }
            notificationBadge.setText(String.valueOf(totalItem));
        }
    }

    private void themGioHang(){
        if(Utils.dsGioHang.size() > 0){
            boolean flag = false;
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            for(int i = 0; i < Utils.dsGioHang.size(); i++){
                if(Utils.dsGioHang.get(i).getId() == sanPham.getId()){
                    Utils.dsGioHang.get(i).setSoluong(soluong + Utils.dsGioHang.get(i).getSoluong());
                    double gia = sanPham.getGia() * Utils.dsGioHang.get(i).getSoluong();
                    Utils.dsGioHang.get(i).setGiasp(gia);
                    flag = true;
                }
            }
            if(flag == false){
                double gia = sanPham.getGia() * soluong;
                GioHang gioHang = new GioHang();
                gioHang.setTensp(sanPham.getTensp());
                gioHang.setGiasp(gia);
                gioHang.setHinhsp(sanPham.getHinhanh());
                gioHang.setSoluong(soluong);
                Utils.dsGioHang.add(gioHang);
            }
        }
        else{
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            double gia = sanPham.getGia() * soluong;
            GioHang gioHang = new GioHang();
            gioHang.setTensp(sanPham.getTensp());
            gioHang.setGiasp(gia);
            gioHang.setHinhsp(sanPham.getHinhanh());
            gioHang.setSoluong(soluong);
            Utils.dsGioHang.add(gioHang);
        }
        notificationBadge.setText(String.valueOf(Utils.dsGioHang.size()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Utils.dsGioHang != null){
            int totalItem = 0;
            for(int i = 0; i < Utils.dsGioHang.size(); i++){
                totalItem += Utils.dsGioHang.get(i).getSoluong();
            }
            notificationBadge.setText(String.valueOf(totalItem));
        }

    }
}