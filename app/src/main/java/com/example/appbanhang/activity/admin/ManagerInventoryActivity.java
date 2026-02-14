package com.example.appbanhang.activity.admin;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.R;
import com.example.appbanhang.adapter.admin.InventoryAdapter;
import com.example.appbanhang.api.admin.ManagerInventoryService;
import com.example.appbanhang.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ManagerInventoryActivity extends AppCompatActivity implements InventoryAdapter.OnInventoryClickListener{
    Toolbar toolbarKhoHang;
    RecyclerView recyclerViewKhoHang;
    InventoryAdapter inventoryAdapter;
    List<Product> dsProduct;
    ManagerInventoryService managerInventoryService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manager_inventory);

        addControls();
        ActionBar();
        showListSanPham();
    }

    private void ActionBar() {
        setSupportActionBar(toolbarKhoHang);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarKhoHang.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addControls(){
        toolbarKhoHang = findViewById(R.id.toolbarKhoHang);
        recyclerViewKhoHang = findViewById(R.id.recyclerViewKhoHang);

        dsProduct = new ArrayList<>();
        inventoryAdapter = new InventoryAdapter(this, dsProduct, this);

        recyclerViewKhoHang.setAdapter(inventoryAdapter);
        recyclerViewKhoHang.setLayoutManager(new LinearLayoutManager(this));
        managerInventoryService = new ManagerInventoryService(this, dsProduct, inventoryAdapter);
    }

    private void showListSanPham(){
        managerInventoryService.getAllSanPhamInventory();
    }

    @Override
    public void onUpdateStock(Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cập nhật số lượng: " + product.getTensp());

        // Tạo EditText để nhập số
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setText(String.valueOf(product.getSoluongtonkho()));
        builder.setView(input);

        builder.setPositiveButton("Cập nhật", (dialog, which) -> {
            String val = input.getText().toString().trim();
            if (!val.isEmpty()) {
                int newStock = Integer.parseInt(val);
                // Gọi API từ Service bạn đã viết
                managerInventoryService.updateStock(product.getId(), newStock);

                // Cập nhật tạm thời trên UI để Admin thấy ngay
                product.setSoluongtonkho(newStock);
                inventoryAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    @Override
    public void onLongClick(Product product) {
        String[] options = {"Sửa chi tiết", "Xóa sản phẩm"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tùy chọn");
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                // Mở AddProductActivity ở chế độ Edit
                Intent intent = new Intent(ManagerInventoryActivity.this, AddProductActivity.class);
                intent.putExtra("isEdit", true);
                intent.putExtra("product_data", product);
                startActivity(intent);
            } else {
                confirmDelete(product);
            }
        });
        builder.show();
    }

    private void confirmDelete(Product product) {
        AlertDialog.Builder builder =  new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa " + product.getTensp() + "?");
        builder.setPositiveButton("Xóa", (dialog, which) -> {
            managerInventoryService.deleteInventory(product.getId());
        });
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_inventory, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_low_stock) {
            // Chỉ hiện hàng dưới 10 cái
            managerInventoryService.getLowStock(10);
            getSupportActionBar().setTitle("Cảnh báo kho ( < 10 )");
            return true;
        } else if (item.getItemId() == R.id.menu_all) {
            // Hiện lại tất cả
            managerInventoryService.getAllSanPhamInventory();
            getSupportActionBar().setTitle("Quản lý kho hàng");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (managerInventoryService != null) {
            managerInventoryService.clear();
        }
    }
}