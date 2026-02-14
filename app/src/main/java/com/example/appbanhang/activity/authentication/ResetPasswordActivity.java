package com.example.appbanhang.activity.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.appbanhang.R;
import com.example.appbanhang.api.authentication.ResetPasswordService;
import com.google.android.material.textfield.TextInputEditText;

public class ResetPasswordActivity extends AppCompatActivity {
    TextInputEditText edtNewPassword, edtReNewPassword;
    AppCompatButton btnDoiMatKhau;
    TextView txtQuayLai, txtQuayLaiDangNhap;

    ResetPasswordService resetPasswordService;
    String email, otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_password);

        addControls();
        addEvents();
    }

    private void addControls(){
        edtNewPassword = findViewById(R.id.edtNewPassword);
        edtReNewPassword = findViewById(R.id.edtReNewPassword);
        btnDoiMatKhau = findViewById(R.id.btnDoiMatKhau);
        txtQuayLai = findViewById(R.id.txtQuayLai);
        txtQuayLaiDangNhap = findViewById(R.id.txtQuayLaiDangNhap);

        resetPasswordService = new ResetPasswordService(this);
        email = getIntent().getStringExtra("email");
        otp = getIntent().getStringExtra("otp");
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
        txtQuayLaiDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                startActivity(login);
            }
        });

        edtReNewPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)){
                    resetPassword();
                    return true;
                }
                return false;
            }
        });
    }

    private void resetPassword(){
        String newPassword = edtNewPassword.getText().toString().trim();
        String newRePassword = edtReNewPassword.getText().toString().trim();


        if (newPassword.isEmpty()) {
            edtNewPassword.setError("Vui lòng nhập mật khẩu");
            edtNewPassword.requestFocus();
            return;
        }
        else if (!newPassword.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&.,])[A-Za-z\\d@$!%*?&.,]{8,}$")) {
            edtNewPassword.setError("Mật khẩu phải từ 8 ký tự trở lên, gồm chữ hoa, chữ thường, số và ký tự đặc biệt");
            edtNewPassword.requestFocus();
            return;
        }

        else if (newRePassword.isEmpty()) {
            edtReNewPassword.setError("Vui lòng nhập lại mật khẩu");
            edtReNewPassword.requestFocus();
            return;
        }
        else if (!newPassword.equals(newRePassword)) {
            edtReNewPassword.setError("Mật khẩu nhập lại không khớp");
            edtReNewPassword.requestFocus();
            return;
        }
        else {
            resetPasswordService.resetPassword(email, otp, newPassword, newRePassword);
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