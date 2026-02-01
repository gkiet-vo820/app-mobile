package com.example.appbanhang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appbanhang.R;
import com.example.appbanhang.api.LoginService;
import com.example.appbanhang.util.Utils;
import com.google.android.material.textfield.TextInputEditText;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
    TextView txtDangKy, txtQuenMatKhau;
    TextInputEditText edtEmail, edtPassword;
    AppCompatButton btnDangNhap;
    LoginService loginService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        addControls();
        addEvents();
        loadSavedEmail();
    }

    private void addControls(){
        txtDangKy = findViewById(R.id.txtDangKy);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnDangNhap = findViewById(R.id.btnDangNhap);
        txtQuenMatKhau = findViewById(R.id.txtQuenMatKhau);

        loginService = new LoginService(this);
        Paper.init(this);
    }

    private void addEvents(){
        txtDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        txtQuenMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }
    private void login(){
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if(email.isEmpty()){
            edtEmail.setError("Vui lòng nhập email");
            edtEmail.requestFocus();
            return;
        }

        else if (password.isEmpty()) {
            edtPassword.setError("Vui lòng nhập mật khẩu");
            edtPassword.requestFocus();
            return;
        }

        else {
            loginService.login(email, password);
        }
    }

    private void loadSavedEmail() {
        String savedEmail = Paper.book().read("email");
        if (savedEmail != null) {
            edtEmail.setText(savedEmail);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loginService != null) {
            loginService.clear();
        }
    }
}