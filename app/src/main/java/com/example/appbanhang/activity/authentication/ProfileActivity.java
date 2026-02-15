package com.example.appbanhang.activity.authentication;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.appbanhang.R;
import com.example.appbanhang.api.authentication.ChangePasswordService;
import com.example.appbanhang.api.authentication.ProfileService;
import com.example.appbanhang.model.User;
import com.example.appbanhang.util.Utils;
import com.google.android.material.textfield.TextInputEditText;

public class ProfileActivity extends AppCompatActivity {
    ImageView imgAnhDaiDien;
    TextView txtTenNguoiDung, txtQuayLai, txtDoiMatKhau;
    TextInputEditText edtUsername, edtSdt;
    AppCompatButton btnCapNhatThongTin;
    ChangePasswordService changePasswordService;
    ProfileService profileService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        addControls();
        addEvents();
        showInfo();
    }

    private void addControls(){
        imgAnhDaiDien = findViewById(R.id.imgAnhDaiDien);
        txtTenNguoiDung = findViewById(R.id.txtTenNguoiDung);
        txtQuayLai = findViewById(R.id.txtQuayLai);
        txtDoiMatKhau = findViewById(R.id.txtDoiMatKhau);
        edtUsername = findViewById(R.id.edtUsername);
        edtSdt = findViewById(R.id.edtSdt);
        btnCapNhatThongTin = findViewById(R.id.btnCapNhatThongTin);

        profileService = new ProfileService(this);
        changePasswordService = new ChangePasswordService(this);
    }

    // Hiện thông tin có sẵn
    private void showInfo(){
        if (Utils.user_current != null) {
            txtTenNguoiDung.setText(Utils.user_current.getUsername());
            edtUsername.setText(Utils.user_current.getUsername());
            edtSdt.setText(Utils.user_current.getSdt());
        }
    }

    private void addEvents(){
        txtQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtDoiMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogChangePassword();
            }
        });

        btnCapNhatThongTin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString().trim();
                String sdt = edtSdt.getText().toString().trim();
                User user = Utils.user_current;
                user.setUsername(username);
                user.setSdt(sdt);

                profileService.updateProfile(user);
            }
        });
    }

    private void showDialogChangePassword() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_change_password);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialog.setCancelable(true); // Cho phép bấm ra ngoài để đóng
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // Làm trong suốt phần thừa của background

        TextInputEditText edtPassCu = dialog.findViewById(R.id.edtPassCu);
        TextInputEditText edtPassMoi = dialog.findViewById(R.id.edtPassMoi);
        TextInputEditText edtPassReMoi = dialog.findViewById(R.id.edtPassReMoi);
        AppCompatButton btnXacNhan = dialog.findViewById(R.id.btnXacNhanDoiPass);

        btnXacNhan.setOnClickListener(view -> {
            String cu = edtPassCu.getText().toString().trim();
            String moi = edtPassMoi.getText().toString().trim();
            String reMoi = edtPassReMoi.getText().toString().trim();

            if (cu.isEmpty()) {
                edtPassCu.setError("Vui lòng nhập mật khẩu");
                edtPassCu.requestFocus();
                return;
            } else if(moi.isEmpty()){
                edtPassMoi.setError("Vui lòng nhập mật khẩu");
                edtPassMoi.requestFocus();
                return;
            }
            else if (!moi.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&.,])[A-Za-z\\d@$!%*?&.,]{8,}$")) {
                edtPassMoi.setError("Mật khẩu phải từ 8 ký tự trở lên, gồm chữ hoa, chữ thường, số và ký tự đặc biệt");
                edtPassMoi.requestFocus();
                return;
            }

            if (moi.equals(reMoi)) {
                changePasswordService.changePassword(Utils.user_current.getId(), cu, moi, dialog);
            } else {
                Toast.makeText(this, "Mật khẩu mới không khớp!", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    @Override
    protected void onDestroy() {
        if (profileService != null) profileService.clear();
        if (changePasswordService != null) changePasswordService.clear();
        super.onDestroy();
    }
}