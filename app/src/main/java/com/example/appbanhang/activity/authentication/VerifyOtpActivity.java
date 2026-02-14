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
import com.example.appbanhang.api.VerifyOtpService;
import com.google.android.material.textfield.TextInputEditText;

public class VerifyOtpActivity extends AppCompatActivity {
    TextInputEditText edtMaOTP;
    AppCompatButton btnXacNhanMaOTP;
    TextView txtQuayLai, txtQuayLaiDangNhap;
    String email;
    VerifyOtpService verifyOtpService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verify_otp);

        addControls();
        addEvents();
    }

    private void addControls(){
        edtMaOTP = findViewById(R.id.edtMaOTP);
        btnXacNhanMaOTP = findViewById(R.id.btnXacNhanMaOTP);
        txtQuayLai = findViewById(R.id.txtQuayLai);
        txtQuayLaiDangNhap= findViewById(R.id.txtQuayLaiDangNhap);

        email = getIntent().getStringExtra("email");
        verifyOtpService = new VerifyOtpService(this);
    }

    private void addEvents(){
        btnXacNhanMaOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyOTP();
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
                Intent login = new Intent(VerifyOtpActivity.this, LoginActivity.class);
                startActivity(login);
            }
        });
        edtMaOTP.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)){
                    verifyOTP();
                    return true;
                }
                return false;
            }
        });

    }

    private void verifyOTP() {
        String otp = edtMaOTP.getText().toString().trim();

        if (otp.isEmpty() || otp.length() < 6) {
            edtMaOTP.setError("Vui lòng nhập mã OTP 6 số");
            return;
        }

        verifyOtpService.verifyOtp(email, otp);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (verifyOtpService != null) {
            verifyOtpService.clear();
        }
    }
}