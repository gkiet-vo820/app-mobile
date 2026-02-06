package com.example.appbanhang.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.appbanhang.R;
import com.example.appbanhang.api.ProductService;
import com.example.appbanhang.model.Product;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AddProductActivity extends AppCompatActivity {

    Toolbar toolBarThemSP;
    EditText edtTenSP, edtGiaSp, edtMoTa, edtSoLuongTonKho;

    ImageView imgChonAnh;
    Spinner spinnerLoai;
    AppCompatButton btnLuuSP;
    List<String> dsTenLoai;
    String imagePath = null;
    Uri uriLinkAnh = null;
    ProductService productService;
    Product productUpdate = null;
    boolean isEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_product);

        addControls();
        ActionBar();
        checkMode();
        addEvents();
    }

    private void ActionBar() {
        setSupportActionBar(toolBarThemSP);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBarThemSP.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addControls(){
        toolBarThemSP = findViewById(R.id.toolBarThemSP);
        edtTenSP = findViewById(R.id.edtTenSP);
        edtGiaSp = findViewById(R.id.edtGiaSp);
        edtMoTa = findViewById(R.id.edtMoTa);
        edtSoLuongTonKho = findViewById(R.id.edtSoLuongTonKho);
        imgChonAnh = findViewById(R.id.imgChonAnh);
        btnLuuSP = findViewById(R.id.btnLuuSP);

        spinnerLoai = findViewById(R.id.spinnerLoai);
        dsTenLoai = new ArrayList<>();
        dsTenLoai.add("Vui lòng chọn loại"); // vị trí 0
        dsTenLoai.add("Điện thoại"); // vị trí 1
        dsTenLoai.add("Tablet"); // vị trí 2
        dsTenLoai.add("Laptop"); // vị trí 3
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item, dsTenLoai);
        spinnerLoai.setAdapter(adapterSpinner);

        productService = new ProductService(this);
    }

    private void checkMode(){
        productUpdate = (Product) getIntent().getSerializableExtra("update");
        if (productUpdate != null) {
            isEdit = true;
            btnLuuSP.setText("CẬP NHẬT SẢN PHẨM");
            toolBarThemSP.setTitle("Sửa sản phẩm");
            fillData();
        }
    }

    private void fillData() {
        edtTenSP.setText(productUpdate.getTensp());
        edtGiaSp.setText(String.valueOf(productUpdate.getGia()));
        edtMoTa.setText(productUpdate.getMota());
        edtSoLuongTonKho.setText(String.valueOf(productUpdate.getSoluongtonkho()));
        spinnerLoai.setSelection(productUpdate.getLoai());
        Glide.with(this).load(productUpdate.getHinhanh()).into(imgChonAnh);
    }

    private void saveProduct() {
        String ten = edtTenSP.getText().toString().trim();
        String gia = edtGiaSp.getText().toString().trim();
        String moTa = edtMoTa.getText().toString().trim();
        String soLuong = edtSoLuongTonKho.getText().toString().trim();
        int loai = spinnerLoai.getSelectedItemPosition();

        // 1. Kiểm tra đầu vào cơ bản
        if (ten.isEmpty() || gia.isEmpty() || loai == 0) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Nếu là THÊM MỚI mà không có ảnh thì báo lỗi
        if (!isEdit && uriLinkAnh == null) {
            Toast.makeText(this, "Vui lòng chọn ảnh", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Chuẩn bị các trường Text
            RequestBody name = RequestBody.create(MediaType.parse("text/plain"), ten);
            RequestBody price = RequestBody.create(MediaType.parse("text/plain"), gia);
            RequestBody description = RequestBody.create(MediaType.parse("text/plain"), moTa);
            RequestBody categoryId = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(loai));
            RequestBody stockQuantity = RequestBody.create(MediaType.parse("text/plain"), soLuong);

            // Xử lý File ảnh (nếu có chọn ảnh mới)
            MultipartBody.Part imagePart = null;
            if (uriLinkAnh != null) {
                InputStream inputStream = getContentResolver().openInputStream(uriLinkAnh);
                byte[] bytes = getBytes(inputStream);
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), bytes);
                // Lấy tên file thật hoặc dùng mặc định
                String fileName = (imagePath != null) ? new File(imagePath).getName() : "product.jpg";
                imagePart = MultipartBody.Part.createFormData("image", fileName, requestFile);
                inputStream.close();
            }

            if (isEdit) {
                // Chế độ SỬA
                RequestBody id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(productUpdate.getId()));
                productService.updateProduct(id, name, price, imagePart, description, categoryId, stockQuantity);
            } else {
                // Chế độ THÊM
                productService.addProduct(name, price, imagePart, description, categoryId, stockQuantity);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi xử lý dữ liệu", Toast.LENGTH_SHORT).show();
        }
    }

    private void addEvents(){
        imgChonAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 123);
            }
        });
        btnLuuSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProduct();
            }
        });
    }

//    private void themSP() {
//        String ten = edtTenSP.getText().toString().trim();
//        String gia = edtGiaSp.getText().toString().trim();
//        String moTa = edtMoTa.getText().toString().trim();
//        String soLuong = edtSoLuongTonKho.getText().toString().trim();
//        int loai = spinnerLoai.getSelectedItemPosition();
//        if (ten.isEmpty() || gia.isEmpty() || moTa.isEmpty() || soLuong.isEmpty() || loai == 0 || imagePath == null) {
//            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        // xử lý file ảnh
//        try {
//            long giaSP = Long.parseLong(gia);
//            int soLuongTonKho = Integer.parseInt(soLuong);
//            int maLoai = spinnerLoai.getSelectedItemPosition() + 1;
//
//            // chuẩn bị các part để gửi multipart
//            RequestBody name = RequestBody.create(MediaType.parse("text/plain"), ten);
//            RequestBody price = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(giaSP));
//            RequestBody description = RequestBody.create(MediaType.parse("text/plain"), moTa);
//            RequestBody categoryId = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(maLoai));
//            RequestBody stockQuantity = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(soLuongTonKho));
//
//            // Xử lý file ảnh qua InputStream
//            InputStream inputStream = getContentResolver().openInputStream(uriLinkAnh);
//            if (inputStream != null) {
//                byte[] bytes = getBytes(inputStream);
//                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), bytes);
//
//                // Lấy tên file
//                String fileName = (imagePath != null) ? new File(imagePath).getName() : "product_image.jpg";
//                MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", fileName, requestFile);
//
//                // Gọi API gửi lên server
//                productService.addProduct(name, price, imagePart, description, categoryId, stockQuantity);
//
//                inputStream.close();
//            }
//        } catch (NumberFormatException e) {
//            Toast.makeText(this, "Giá hoặc số lượng phải là số", Toast.LENGTH_SHORT).show();
//        } catch (java.io.FileNotFoundException e) {
//            Toast.makeText(this, "Không tìm thấy file ảnh", Toast.LENGTH_SHORT).show();
//        } catch (java.io.IOException e) {
//            Toast.makeText(this, "Lỗi khi đọc file ảnh", Toast.LENGTH_SHORT).show();
//        }
//    }

    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int length = 0;
        while((length = inputStream.read(buffer)) != -1){
            byteArrayOutputStream.write(buffer, 0, length);
        }
        return byteArrayOutputStream.toByteArray();
    }
    private String getPath(Uri uri){
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if(cursor == null){
            return null;
        }
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s = cursor.getString(index);
        cursor.close();
        return s;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 123 && resultCode == RESULT_OK && data != null){
            uriLinkAnh = data.getData();
            // hiển thị ảnh lên giao diện
            imgChonAnh.setImageURI(uriLinkAnh);
            // lấy đường dẫn file thật
            imagePath = getPath(uriLinkAnh);
        }
    }
}