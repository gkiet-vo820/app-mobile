package com.example.appbanhang.activity.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.appbanhang.R;
import com.example.appbanhang.activity.MainActivity;
import com.example.appbanhang.api.authentication.LoginService;
import com.example.appbanhang.model.User;
import com.example.appbanhang.util.Utils;
import com.google.android.material.textfield.TextInputEditText;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
    TextView txtDangKy, txtQuenMatKhau;
    TextInputEditText edtEmail, edtPassword;
    AppCompatButton btnDangNhap;
    LoginService loginService;
    private boolean isLoggingIn = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Paper.init(this);
        loadSaveUser();

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

        edtPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)){
                    login();
                    return true;
                }
                return false;
            }
        });
    }
    private void login(){
        if (isLoggingIn) return; // Nếu đang login thì không cho bấm nữa
        isLoggingIn = true;
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
            Log.d("TEST_DATA", "Email: " + email + " | Pass: " + password);
            loginService.login(email, password);
        }
    }

    private void loadSavedEmail() {
        String savedEmail = Paper.book().read("email");
        if (savedEmail != null) {
            edtEmail.setText(savedEmail);
        }
    }

    private void loadSaveUser(){
        User savedUser = Paper.book().read("user");
        if(savedUser != null){
            Utils.user_current = savedUser;;
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
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