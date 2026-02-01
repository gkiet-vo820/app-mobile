package com.example.appbanhang.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appbanhang.R;
import com.example.appbanhang.api.RegisterService;
import com.example.appbanhang.util.GetApi;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {
    TextInputEditText edtEmail, edtUsername, edtPassword, edtRePassword, edtPhoneNumber;
    Button btnDangKy;
    TextView txtQuayLai;
    RegisterService registerService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        addControls();
        addEvents();
    }

    private void addControls(){
        edtEmail = findViewById(R.id.edtEmail);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtRePassword = findViewById(R.id.edtRePassword);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        btnDangKy = findViewById(R.id.btnDangKy);
        txtQuayLai = findViewById(R.id.txtQuayLai);

        registerService = new RegisterService(this);
    }

    private void addEvents(){
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
        txtQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void register(){
        String email = edtEmail.getText().toString().trim();
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String repassword = edtRePassword.getText().toString().trim();
        String sdt = edtPhoneNumber.getText().toString().trim();
        if(email.isEmpty()){
            edtEmail.setError("Vui lòng nhập email");
            edtEmail.requestFocus();
            return;
        }
        else if (!email.matches("^[A-Za-z0-9._%+-]+@gmail\\.com$")) {
            edtEmail.setError("Email không hợp lệ");
            edtEmail.requestFocus();
            return;
        }

        else if (username.isEmpty()) {
            edtUsername.setError("Vui lòng nhập tên tài khoản");
            edtUsername.requestFocus();
            return;
        }
        else if (!username.matches("^[a-zA-Z0-9_]+$")) {
            edtUsername.setError("Tên tài khoản chỉ gồm chữ, số và _");
            edtUsername.requestFocus();
            return;
        }

        else if (password.isEmpty()) {
            edtPassword.setError("Vui lòng nhập mật khẩu");
            edtPassword.requestFocus();
            return;
        }
        else if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&.,])[A-Za-z\\d@$!%*?&.,]{8,}$")) {
            edtPassword.setError("Mật khẩu phải từ 8 ký tự trở lên, gồm chữ hoa, chữ thường, số và ký tự đặc biệt");
            edtPassword.requestFocus();
            return;
        }

        else if (repassword.isEmpty()) {
            edtRePassword.setError("Vui lòng nhập lại mật khẩu");
            edtRePassword.requestFocus();
            return;
        }
        else if (!password.equals(repassword)) {
            edtRePassword.setError("Mật khẩu nhập lại không khớp");
            edtRePassword.requestFocus();
            return;
        }

        else if (sdt.isEmpty()) {
            edtPhoneNumber.setError("Vui lòng nhập số điện thoại");
            edtPhoneNumber.requestFocus();
            return;
        }
        else if (!sdt.matches("^0\\d{9}$")) {
            edtPhoneNumber.setError("Số điện thoại không hợp lệ");
            edtPhoneNumber.requestFocus();
            return;
        }

        else{
            registerService.register(email, username, password, repassword, sdt);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (registerService != null) {
            registerService.clear();
        }
    }
}