package com.example.appbanhang.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appbanhang.R;
import com.example.appbanhang.api.ResetPasswordService;
import com.google.android.material.textfield.TextInputEditText;

public class ResetPasswordActivity extends AppCompatActivity {
    TextInputEditText edtPassword, edtRePassword;
    AppCompatButton btnDoiMatKhau;
    TextView txtQuayLai;
    ResetPasswordService resetPasswordService;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_password);

        addControls();
        getIntentData(getIntent());
        addEvents();
    }

    @Override
    protected void onNewIntent(@NonNull Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        getIntentData(intent);
    }
    private void getIntentData(Intent intent){
        Uri uri = intent.getData();
        if(uri != null && uri.getScheme().equals("appbanhang") && uri.getHost().equals("reset-password")){
            token = uri.getQueryParameter("token");
        }
        if (token == null || token.isEmpty()) {
            Toast.makeText(this, "Token không hợp lệ", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
    }
    private void addControls(){
        edtPassword = findViewById(R.id.edtPassword);
        edtRePassword = findViewById(R.id.edtRePassword);
        btnDoiMatKhau = findViewById(R.id.btnDoiMatKhau);
        txtQuayLai = findViewById(R.id.txtQuayLai);

        resetPasswordService = new ResetPasswordService(this);

    }



    private void addEvents(){
        btnDoiMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
        txtQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void resetPassword(){
        String password = edtPassword.getText().toString().trim();
        String repassword = edtRePassword.getText().toString().trim();

        if (password.isEmpty()) {
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
        else {
            resetPasswordService.resetPassword(token, password, repassword);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(resetPasswordService != null){
            resetPasswordService.clear();
        }
    }
}