package com.example.appbanhang.activity.product;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.appbanhang.R;
import com.example.appbanhang.activity.cart.ShoppingCartActivity;
import com.example.appbanhang.model.ShoppingCart;
import com.example.appbanhang.model.Product;
import com.example.appbanhang.util.CartStorage;
import com.example.appbanhang.util.Utils;
import com.nex3z.notificationbadge.NotificationBadge;

import java.text.DecimalFormat;

public class DetailActivity extends AppCompatActivity {

    // Khai báo toolbar
    Toolbar toolBarChiTietSP;
    FrameLayout frameLayoutGioHang;
    NotificationBadge notificationBadge;

    // Khai báo tên,.. của sản phẩm và nút
    TextView txtTenChiTietSP, txtGiaChiTietSP, txtMoTaChiTietSP;
    ImageView imgChiTietSP;
    AppCompatButton btnThemVaoGioHang;
    Spinner spinner;

    Product product;
//    int loai;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);

        addControls();
        ActionBar();
        getIntentData();
        addEvents();
    }

    // Nhận từ khóa từ các activity khác truyền qua
    private void getIntentData(){
        Intent intent = getIntent();
        product = (Product) intent.getSerializableExtra("chitiet");
        if (product == null) {
            finish();
            return;
        }
        txtTenChiTietSP.setText(product.getTensp());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        txtGiaChiTietSP.setText("Giá: " + decimalFormat.format(product.getGia()) + "Đ");
        txtMoTaChiTietSP.setText(product.getMota());
        Glide.with(this).load(product.getHinhanh()).into(imgChiTietSP);
        Integer[] so = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        ArrayAdapter<Integer> adapterSpinner = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item, so);
        spinner.setAdapter(adapterSpinner);
    }

    // Hiển thị toolbar
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

    // Ánh xạ
    private void addControls() {
        // Kiểm tra giỏ hàng
        checkCart();

        // ánh xạ toolbar
        toolBarChiTietSP = findViewById(R.id.toolBarChiTietSP);
        frameLayoutGioHang = findViewById(R.id.frameLayoutGioHang);
        notificationBadge = findViewById(R.id.menuSoLuong);

        // Ánh xạ tên,.. của sản phẩm và nút
        txtTenChiTietSP = findViewById(R.id.txtTenChiTietSP);
        txtGiaChiTietSP = findViewById(R.id.txtGiaChiTietSP);
        txtMoTaChiTietSP = findViewById(R.id.txtMoTaChiTietSP);
        imgChiTietSP = findViewById(R.id.imgChiTietSP);
        btnThemVaoGioHang = findViewById(R.id.btnThemVaoGioHang);
        spinner = findViewById(R.id.spinner);


//        loai = getIntent().getIntExtra("loai", 0);

    }

    private void checkCart() {
        if (Utils.dsShoppingCart == null || Utils.dsShoppingCart.isEmpty()) {
            Utils.dsShoppingCart = CartStorage.loadCart(this);
        }
    }

    private void addEvents(){
        btnThemVaoGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGioHang();
                setActiveButton(btnThemVaoGioHang);
            }
        });
        frameLayoutGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gioHang =  new Intent(DetailActivity.this, ShoppingCartActivity.class);
                startActivity(gioHang);
            }
        });
        if(Utils.dsShoppingCart != null){
            int totalItem = 0;
            for(int i = 0; i < Utils.dsShoppingCart.size(); i++){
                totalItem += Utils.dsShoppingCart.get(i).getSoluong();
            }
            notificationBadge.setText(String.valueOf(totalItem));
        }
    }

    private void addGioHang() {
        int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
        for (ShoppingCart item : Utils.dsShoppingCart) {
            if (item.getId() == (product.getId())) {
                item.setSoluong(item.getSoluong() + soluong);
                item.setSelected(true);
                CartStorage.saveCart(this, Utils.dsShoppingCart);
                updateBadge();
                return;
            }
        }

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(product.getId());
        shoppingCart.setTensp(product.getTensp());
        shoppingCart.setGiasp(product.getGia());
        shoppingCart.setHinhsp(product.getHinhanh());
        shoppingCart.setSoluong(soluong);

        shoppingCart.setSelected(true);
        Utils.dsShoppingCart.add(shoppingCart);
        CartStorage.saveCart(this, Utils.dsShoppingCart);
        updateBadge();
    }


    private void updateBadge() {
        int totalItem = 0;
        for (ShoppingCart item : Utils.dsShoppingCart) {
            totalItem += item.getSoluong();
        }
        notificationBadge.setText(String.valueOf(totalItem));
    }

    private void setActiveButton(Button button) {
        btnThemVaoGioHang.setBackgroundResource(R.drawable.button_inactive);

        btnThemVaoGioHang.setTextColor(getColor(android.R.color.black));

        button.setBackgroundResource(R.drawable.button_active);
        button.setTextColor(getColor(android.R.color.white));
    }
    @Override
    protected void onResume() {
        super.onResume();
        updateBadge();
    }
}