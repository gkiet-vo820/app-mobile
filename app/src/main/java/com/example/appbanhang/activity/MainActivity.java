package com.example.appbanhang.activity;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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
import com.example.appbanhang.activity.admin.ManagerActivity;
import com.example.appbanhang.activity.authentication.LoginActivity;
import com.example.appbanhang.activity.authentication.ProfileActivity;
import com.example.appbanhang.activity.cart.ShoppingCartActivity;
import com.example.appbanhang.activity.chat.ChatActivity;
import com.example.appbanhang.activity.notification.NotificationActivity;
import com.example.appbanhang.activity.order.OrderDetailActivity;
import com.example.appbanhang.activity.order.OrdersActivity;
import com.example.appbanhang.activity.product.LaptopActivity;
import com.example.appbanhang.activity.product.PhoneActivity;
import com.example.appbanhang.activity.search.SearchActivity;
import com.example.appbanhang.adapter.menu.CategoriesAdapter;
import com.example.appbanhang.api.notification.NotificationService;
import com.example.appbanhang.listener.ItemClickListener;
import com.example.appbanhang.adapter.menu.MenuAdapter;
import com.example.appbanhang.adapter.product.ProductAdapter;
import com.example.appbanhang.api.menu.CategoriesService;
import com.example.appbanhang.api.menu.MenuService;
import com.example.appbanhang.api.product.ProductService;
import com.example.appbanhang.model.Categories;
import com.example.appbanhang.model.Menu;
import com.example.appbanhang.model.Product;
import com.example.appbanhang.model.User;
import com.example.appbanhang.model.eventbus.LogoutEvent;
import com.example.appbanhang.util.CartStorage;
import com.example.appbanhang.util.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nex3z.notificationbadge.NotificationBadge;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    MenuAdapter menuAdapter, drawerAdapter;
    List<Menu> dsMenu, dsDrawerMenu;
    MenuService menuService;
    ProductAdapter productAdapter;
    List<Product> dsProduct;
    ProductService productService;


    NotificationBadge notificationBadge;
    FrameLayout frameLayoutManHinhChinh;

    SearchView searchView;

    boolean isDrawer = false;

    View headerView;
    TextView txtName, txtEmail;
    ImageView imgAvatar;

    NotificationService notificationService;
    private static final int POS_TRANG_CHU = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Paper.init(this);
        saveUserLogin();

        createNotificationChannel();

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

        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (drawerAdapter != null) {
                    drawerAdapter.setSelectedPosition(-1); // Reset khi đóng bằng bất kỳ cách nào
                }
            }
        });

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

//        listViewManHinhChinh = findViewById(R.id.listViewManHinhChinh);
//        dsCategories = new ArrayList<>();
//        categoriesAdapter = new CategoriesAdapter(this, dsCategories);
//        listViewManHinhChinh.setAdapter(categoriesAdapter);
//        categoriesService = new CategoriesService(this, categoriesAdapter);

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
        setupStaticDrawerMenu(); // Hàm tự tạo mục Đăng xuất/Admin
        drawerAdapter = new MenuAdapter(this, dsDrawerMenu, true);
        recyclerViewDrawer.setAdapter(drawerAdapter);

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


        // Ánh xạ Header View
        headerView = navigaionView.getHeaderView(0);
        txtName = headerView.findViewById(R.id.txtUserDisplayName);
        txtEmail = headerView.findViewById(R.id.txtUserEmail);
        imgAvatar = headerView.findViewById(R.id.imgAvatar);


        // Đổ dữ liệu từ Utils vào Header
        if (Utils.user_current != null) {
            txtName.setText(Utils.user_current.getUsername());
            txtEmail.setText(Utils.user_current.getEmail());
            // Nếu có link ảnh: Glide.with(this).load(Utils.user_current.getAvatar()).into(imgAvatar);
        } else {
            txtName.setText("Chưa đăng nhập");
            txtEmail.setText("Bấm để đăng nhập ngay");
            headerView.setOnClickListener(v -> {
                startActivity(new Intent(this, LoginActivity.class));
            });
        }


        notificationService = new NotificationService(this);
        getTokenFCM();

    }

    private void getTokenFCM(){
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        android.util.Log.w("FCM_TOKEN", "Lấy Token thất bại", task.getException());
                        return;
                    }

                    // Đây là mã Token bạn cần để gửi tin nhắn
                    String token = task.getResult();
                    android.util.Log.d("FCM_TOKEN", "Token của thiết bị là: " + token);

                    if(Utils.user_current != null){
                        notificationService.updateToken(Utils.user_current.getEmail(), token);
                    }
                });
    }

    private void sendTokenToServer(String token){
        Map<String, String> data = new HashMap<>();
        data.put("email", Utils.user_current.getEmail());
        data.put("token", token);
    }

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelId = "app_ban_hang";
            CharSequence name = "Thông báo đơn hàng";
            String description = "Nhận thông báo về trạng thái đơn hàng của bạn";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel notificationChannel = new NotificationChannel(channelId, name, importance);
            notificationChannel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if(notificationManager != null){
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
    }
    private void setupStaticDrawerMenu() {
        dsDrawerMenu.clear();
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
                if (drawerAdapter != null) {
                    drawerAdapter.setSelectedPosition(-1);
                }

                String action = dsDrawerMenu.get(position).getName();
                switch (action) {
                    case "Đăng xuất":
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Xác nhận");
                        builder.setMessage("Bạn có muốn đăng xuất khỏi tài khoản?");
                        builder.setPositiveButton("Đồng ý", (dialog, which) -> {
                            Paper.book().delete("user");
                            Paper.book().delete("token");
                            Utils.user_current = null;

                            Intent logout = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(logout);
                            finish();
                        });
                        builder.setNegativeButton("Hủy", null);
                        builder.show();
                        break;
                    case "Quản lý hệ thống":
                        Intent manager = new Intent(MainActivity.this, ManagerActivity.class);
                        startActivity(manager);
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
                        Intent profile = new Intent(MainActivity.this, ProfileActivity.class);
                        startActivity(profile);
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START); //Sau khi click xong thì đóng Drawer
                // Reset ngay lập tức để lần sau mở ra không bị xanh
                drawerAdapter.setSelectedPosition(-1);
            }
        });

        menuAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                // CẬP NHẬT HIGHLIGHT CHO CHÍNH NÓ
                menuAdapter.setSelectedPosition(position);

                // Reset Drawer để không bị xanh cùng lúc
                drawerAdapter.setSelectedPosition(-1);

                String tenMenu = dsMenu.get(position).getName(); // Lấy name từ bảng menu
                if (tenMenu.equalsIgnoreCase("Trang chủ")) {
                    showListSanPham();
                    showListMenu();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    setActiveButton(btnTatCaSanPham);
                } else if (tenMenu.equalsIgnoreCase("Danh mục")) {
                    openCategoryDialog();
                } else if (tenMenu.equalsIgnoreCase("Đơn hàng")) {
                    if (Utils.user_current == null) {
                        Toast.makeText(MainActivity.this, "Bạn cần đăng nhập để xem đơn hàng", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Integer idDonHangMoi = Paper.book().read("last_order_id", 0);
                    if(idDonHangMoi != null && idDonHangMoi > 0){
                        Intent intent = new Intent(MainActivity.this, OrderDetailActivity.class);
                        intent.putExtra("order_id", idDonHangMoi);
                        startActivity(intent);
                    } else {
                        if(Utils.user_current != null){
                            Intent intent = new Intent(MainActivity.this, OrdersActivity.class);
                            intent.putExtra("id", Utils.user_current.getId());
                            startActivity(intent);
                        } else {
                            Toast.makeText(MainActivity.this, "Bạn chưa đăng nhập!", Toast.LENGTH_SHORT).show();
                        }
                    }

                } else if (tenMenu.equalsIgnoreCase("Thông báo")) {
                    Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
                    startActivity(intent);
                } else if (tenMenu.equalsIgnoreCase("Tin nhắn")) {
                    Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                    startActivity(intent);

                }
            }
        });

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

    @Override
    public void onBackPressed() {
        // 1. Nếu Drawer đang mở thì đóng Drawer trước
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            // 2. Nếu không ở trang chủ, nhấn back sẽ quay về highlight Trang chủ (vị trí 0)
            if (menuAdapter.getSelectedPosition() != POS_TRANG_CHU) {
                menuAdapter.setSelectedPosition(POS_TRANG_CHU);

                // Thực hiện quay về logic hiển thị trang chủ
                showListSanPham();
                setActiveButton(btnTatCaSanPham);

                Toast.makeText(this, "Quay lại Trang chủ", Toast.LENGTH_SHORT).show();
            } else {
                // 3. Nếu đã ở trang chủ thì mới thoát App
                super.onBackPressed();
            }
        }
    }

    private void openCategoryDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.layout_bottom_sheet_categories, null);
        bottomSheetDialog.setContentView(view);

        ListView lv = view.findViewById(R.id.lvSheetCategories);
        dsCategories = new ArrayList<>();
        categoriesAdapter = new CategoriesAdapter(this, dsCategories);
        lv.setAdapter(categoriesAdapter);

        // Gọi CategoriesService để load dữ liệu từ MySQL
        categoriesService = new CategoriesService(this, categoriesAdapter);
        categoriesService.getAllCategories();

        lv.setOnItemClickListener((parent, view1, position, id) -> {
            int maLoai = dsCategories.get(position).getId();
            String tenLoai = dsCategories.get(position).getTenLoai();
            Intent intent = null;
            if (tenLoai.equalsIgnoreCase("Điện thoại")) {
                intent = new Intent(MainActivity.this, PhoneActivity.class);
            } else if (tenLoai.equalsIgnoreCase("Laptop")) {
                intent = new Intent(MainActivity.this, LaptopActivity.class);
            }

            if (intent != null) {
                intent.putExtra("loai", maLoai);
                startActivity(intent);
            } else {
                // Nếu rơi vào đây (ví dụ loại "Đồng hồ"), app sẽ báo cho người dùng biết
                Toast.makeText(MainActivity.this, "Sản phẩm " + tenLoai + " sẽ sớm ra mắt!", Toast.LENGTH_SHORT).show();
            }
            bottomSheetDialog.dismiss();
//            switch (position){
//                case 0:
//                    Intent dienthoai  = new Intent(MainActivity.this, PhoneActivity.class);
//                    dienthoai.putExtra("loai", 1);
//                    startActivity(dienthoai);
//                    drawerLayout.closeDrawer(GravityCompat.START);
//                    break;
//                case 1:
//
//                    break;
//                case 2:
//                    Intent laptop  = new Intent(MainActivity.this,LaptopActivity.class);
//                    laptop.putExtra("loai", 2);
//                    startActivity(laptop);
//                    drawerLayout.closeDrawer(GravityCompat.START);
//                    break;
//                case 3:
//
//                    break;
//                case  4:
//
//                    break;
//                default:
//                    Toast.makeText(MainActivity.this, "Lỗi!!!", Toast.LENGTH_SHORT).show();
//                    break;
//            }
//            bottomSheetDialog.dismiss();
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

        if (menuAdapter != null) {
            menuAdapter.setSelectedPosition(POS_TRANG_CHU); // POS_TRANG_CHU là 0
            // Nếu bạn muốn mỗi lần quay về từ màn hình khác, nó phải highlight đúng trang chủ
            // Kiểm tra xem hiện tại đang hiện cái gì, nếu là sản phẩm thì highlight vị trí 0
            menuAdapter.notifyDataSetChanged();
        }

        // Cập nhật lại tên/email ở Header nếu người dùng vừa sửa ở trang Profile
        if (Utils.user_current != null) {
            txtName.setText(Utils.user_current.getUsername());
            txtEmail.setText(Utils.user_current.getEmail());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Đừng hủy register ở onDestroy, hãy làm ở onStop để đảm bảo an toàn
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    // Hàm này sẽ tự động chạy khi Retrofit bắn ra LogoutEvent
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogoutEvent(LogoutEvent event) {
        // 1. Hiện thông báo
        Toast.makeText(this, event.getMessage(), Toast.LENGTH_LONG).show();

        // 2. Chuyển về màn hình Login
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (categoriesService != null) categoriesService.clear();
        if (menuService != null) menuService.clear();
        if (productService != null) productService.clear();
        if (notificationService != null) notificationService.clear();
    }
}