package com.example.appbanhang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbanhang.R;
import com.example.appbanhang.adapter.CategoriesAdapter;
import com.example.appbanhang.adapter.ItemClickListener;
import com.example.appbanhang.adapter.MenuAdapter;
import com.example.appbanhang.adapter.ProductAdapter;
import com.example.appbanhang.api.CategoriesService;
import com.example.appbanhang.api.MenuService;
import com.example.appbanhang.api.ProductService;
import com.example.appbanhang.model.Categories;
import com.example.appbanhang.model.Menu;
import com.example.appbanhang.model.Product;
import com.example.appbanhang.model.User;
import com.example.appbanhang.util.CartStorage;
import com.example.appbanhang.util.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ViewFlipper viewFlipper;
    RecyclerView recyclerViewManHinhChinh, recyclerMenuManHinhChinh, recyclerViewDrawer;
    NavigationView navigaionView;
    AppCompatButton btnTatCaSanPham, btnSanPhamMoiNhat, btnSanPhamBanChay;

    CategoriesAdapter categoriesAdapter;
    List<Categories> dsCategories;
    CategoriesService categoriesService;


    BottomSheetDialog bottomSheetDialog;
    ListView listViewSheetCategories;

    MenuAdapter menuAdapter, drawerAdapter;
    List<Menu> dsMenu, dsDrawerMenu;
    MenuService menuService;
    ProductAdapter productAdapter;
    List<Product> dsProduct;
    ProductService productService;


    NotificationBadge notificationBadge;
    FrameLayout frameLayoutManHinhChinh;

    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Paper.init(this);
        saveUserLogin();
        addControls();
        addEvents();
        ActionBar();
        ActionViewFlipper();

        showListMenu();
        showListSanPham();
    }

    private void saveUserLogin(){
        if(Paper.book().read("user") != null){
            User user = Paper.book().read("user");
            Utils.user_current = user;
        }
    }

    private void ActionViewFlipper() {
        List<String> advertise = new ArrayList<>();
        advertise.add("https://cdnv2.tgdd.vn/mwg-static/tgdd/Banner/b5/07/b507e2e48c5c06b20d947e5d20935739.jpg");
        advertise.add("https://cdnv2.tgdd.vn/mwg-static/tgdd/Banner/10/4c/104c303fc3d0c63beaa0f6517ff30e60.png");
        advertise.add("https://cdnv2.tgdd.vn/mwg-static/tgdd/Banner/f2/11/f211d09ae538f0998ee3f33314aaa8b7.png");
        for(int i = 0; i < advertise.size(); i++){
            ImageView imageView = new ImageView(this);
            Glide.with(this).load(advertise.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
        Animation slide_in = AnimationUtils.loadAnimation(this,R.anim.slide_in_right);
        Animation slide_out = AnimationUtils.loadAnimation(this,R.anim.slide_out_right);
        viewFlipper.setInAnimation(slide_in);
        viewFlipper.setOutAnimation(slide_out);

    }

    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void addControls(){
        checkCart();

        drawerLayout = findViewById(R.id.drawerLayout);
        toolbar = findViewById(R.id.toolBarManHinhChinh);
        viewFlipper = findViewById(R.id.viewFlipper);
        recyclerViewManHinhChinh = findViewById(R.id.recyclerViewManHinhChinh);
        navigaionView = findViewById(R.id.navigaionView);

        recyclerMenuManHinhChinh = findViewById(R.id.recyclerMenuManHinhChinh);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerMenuManHinhChinh.setLayoutManager(linearLayoutManager);
        dsMenu = new ArrayList<>();
        menuAdapter = new MenuAdapter(this, dsMenu, false);
        recyclerMenuManHinhChinh.setAdapter(menuAdapter);
        menuService = new MenuService(this, menuAdapter, dsMenu);

        recyclerViewDrawer = findViewById(R.id.recyclerViewDrawer);
        recyclerViewDrawer.setLayoutManager(new LinearLayoutManager(this));
        dsDrawerMenu = new ArrayList<>();
        setupStaticDrawerMenu();// Hàm tự tạo mục Đăng xuất/Admin
        drawerAdapter = new MenuAdapter(this, dsDrawerMenu, true);
        recyclerViewDrawer.setAdapter(drawerAdapter);

        bottomSheetDialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.layout_bottom_sheet_categories, null);
        bottomSheetDialog.setContentView(view);

        listViewSheetCategories = view.findViewById(R.id.listViewSheetCategories);
        dsCategories = new ArrayList<>();
        categoriesAdapter = new CategoriesAdapter(this, dsCategories);
        listViewSheetCategories.setAdapter(categoriesAdapter);

        categoriesService = new CategoriesService(this, categoriesAdapter);
        categoriesService.getAllCategories();

        dsProduct = new ArrayList<>();
        productAdapter = new ProductAdapter(this, dsProduct);
        recyclerViewManHinhChinh.setLayoutManager(new GridLayoutManager(this,2));
        recyclerViewManHinhChinh.setAdapter(productAdapter);
        productService = new ProductService(this, productAdapter, dsProduct);

        btnTatCaSanPham = findViewById(R.id.btnTatCaSanPham);
        btnSanPhamMoiNhat = findViewById(R.id.btnSanPhamMoiNhat);
        btnSanPhamBanChay = findViewById(R.id.btnSanPhamBanChay);

        notificationBadge = findViewById(R.id.menuSoLuong);
        frameLayoutManHinhChinh = findViewById(R.id.frameLayoutManHinhChinh);

        searchView = findViewById(R.id.searchView);
        countTotalItem();
    }

    private void setupStaticDrawerMenu() {
        dsDrawerMenu.clear();
        // Kiểm tra role từ bảng User
        if(Utils.user_current != null){
            dsDrawerMenu.add(new Menu("Thông tin cá nhân", ""));
            dsDrawerMenu.add(new Menu("Lịch sử mua hàng", ""));
        }
        if (Utils.user_current != null && Utils.user_current.getRole() == 1) {
            dsDrawerMenu.add(new Menu("Quản lý hệ thống", ""));
//            dsDrawerMenu.add(new Menu("Thống kê doanh thu", ""));
        }
        dsDrawerMenu.add(new Menu("Liên hệ và hỗ trợ", ""));
        dsDrawerMenu.add(new Menu("Đăng xuất", ""));
    }

    private void checkCart() {
        if (Utils.dsShoppingCart == null || Utils.dsShoppingCart.isEmpty()) {
            Utils.dsShoppingCart = CartStorage.loadCart(this);
        }
    }

    private void countTotalItem(){
        int totalItem = 0;
        for(int i = 0; i < Utils.dsShoppingCart.size(); i++){
            totalItem += Utils.dsShoppingCart.get(i).getSoluong();
        }
        notificationBadge.setText(String.valueOf(totalItem));
    }

    private void addEvents(){
        drawerAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                String action = dsDrawerMenu.get(position).getName();

                switch (action) {
                    case "Đăng xuất":
                        Paper.book().delete("user");
                        Utils.user_current = null;
                        Intent logout = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(logout);
                        finish();
                        break;
                    case "Quản lý hệ thống":
                        startActivity(new Intent(MainActivity.this, ManagerActivity.class));
                        break;
                    case "Lịch sử mua hàng":
                        if (Utils.user_current != null) {
                            Intent order = new Intent(MainActivity.this, OrdersActivity.class);
                            order.putExtra("id", Utils.user_current.getId());
                            startActivity(order);
                        } else {
                            Toast.makeText(MainActivity.this, "Bạn cần đăng nhập!", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case "Thông tin cá nhân":
                        // Chuyển sang ProfileActivity nếu có
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        menuAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                String tenMenu = dsMenu.get(position).getName();
                if (tenMenu.equalsIgnoreCase("Danh mục")) {
                    openCategoryDialog();
                } else if (tenMenu.equalsIgnoreCase("Trang chủ")) {
                    showListSanPham();
                    setActiveButton(btnTatCaSanPham);
                }
            }
        });

//        menuAdapter.setItemClickListener(new ItemClickListener() {
//            @Override
//            public void onClick(View view, int position, boolean isLongClick) {
//                switch (position) {
//                    case 0:
//                        showListCategories();
//                        showListMenu();
//                        showListSanPham();
//                        drawerLayout.closeDrawer(GravityCompat.START);
//                        break;
//                    case 2:
//                        if (Utils.user_current != null) {
//                            Intent order = new Intent(MainActivity.this, OrdersActivity.class);
//                            order.putExtra("id", Utils.user_current.getId());
//                            startActivity(order);
//                        } else {
//                            Toast.makeText(MainActivity.this, "Vui lòng đăng nhập!", Toast.LENGTH_SHORT).show();
//                        }
//                        drawerLayout.closeDrawer(GravityCompat.START);
//                        break;
//
//                    case 4:
//                        Paper.book().delete("user");
//                        Intent logout = new Intent(MainActivity.this, LoginActivity.class);
//                        startActivity(logout);
//                        finish();
//                        break;
//                }
//            }
//        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!query.trim().isEmpty()){
                    searchView.clearFocus();
                    Intent search = new Intent(MainActivity.this, SearchActivity.class);
                    search.putExtra("keyword", query);
                    search.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(search);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        btnTatCaSanPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showListSanPham();
                setActiveButton(btnTatCaSanPham);
            }
        });
        btnSanPhamMoiNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showListSanPhamMoiNhat();
                setActiveButton(btnSanPhamMoiNhat);

            }
        });
        btnSanPhamBanChay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showListSanPhamBanChay();
                setActiveButton(btnSanPhamBanChay);
            }
        });

        frameLayoutManHinhChinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gioHang = new Intent(MainActivity.this, ShoppingCartActivity.class);
                startActivity(gioHang);
            }
        });

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void openCategoryDialog() {
        if (dsCategories == null || dsCategories.isEmpty()) {
            Toast.makeText(this, "Đang tải danh mục, vui lòng đợi...", Toast.LENGTH_SHORT).show();
            categoriesService.getAllCategories();
            return;
        }
        listViewSheetCategories.setOnItemClickListener((parent, view1, position, id) -> {
            int maLoai = dsCategories.get(position).getId();
            String tenLoai = dsCategories.get(position).getTenLoai();
            Intent intent = null;
            switch (maLoai) {
                case 1:
                    intent = new Intent(MainActivity.this, PhoneActivity.class);
                    break;
                case 2:
                    intent = new Intent(MainActivity.this, TabletActivity.class);
                    break;
                case 3:
                    intent = new Intent(MainActivity.this, LaptopActivity.class);
                    break;
                default:
                    Toast.makeText(MainActivity.this, "Vui lòng chọn các mục trên", Toast.LENGTH_SHORT).show();
                    break;
            }

            if (intent != null) {
                intent.putExtra("loai", maLoai);
                intent.putExtra("tenloai", tenLoai);
                startActivity(intent);
                bottomSheetDialog.dismiss();
            } else {
                Toast.makeText(MainActivity.this, "Danh mục " + tenLoai + " chưa có Activity xử lý!", Toast.LENGTH_SHORT).show();
            }
        });
        bottomSheetDialog.show();
    }

    private void setActiveButton(Button button) {
        btnTatCaSanPham.setBackgroundResource(R.drawable.button_inactive);
        btnSanPhamMoiNhat.setBackgroundResource(R.drawable.button_inactive);
        btnSanPhamBanChay.setBackgroundResource(R.drawable.button_inactive);

        btnTatCaSanPham.setTextColor(getColor(android.R.color.black));
        btnSanPhamMoiNhat.setTextColor(getColor(android.R.color.black));
        btnSanPhamBanChay.setTextColor(getColor(android.R.color.black));

        button.setBackgroundResource(R.drawable.button_active);
        button.setTextColor(getColor(android.R.color.white));
    }
    private void showListCategories(){
        categoriesService.getAllCategories();
    }

    private void showListMenu(){
        menuService.getAllMenu();
    }
    private void showListSanPham(){
        productAdapter.setMode(ProductAdapter.MODE_ALL);
        productService.getAllSanPham();
    }

    private void showListSanPhamMoiNhat(){
        productAdapter.setMode(ProductAdapter.MODE_NEW);
        productService.getTop10New();
    }

    private void showListSanPhamBanChay(){
        productAdapter.setMode(ProductAdapter.MODE_HOT);
        productService.getTop10BestSeller();
    }

    @Override
    protected void onResume() {
        super.onResume();
        countTotalItem();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (categoriesService != null) categoriesService.clear();
        if (menuService != null) menuService.clear();
        if (productService != null) productService.clear();
    }
}