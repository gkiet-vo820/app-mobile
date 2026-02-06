package com.example.appbanhang.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;


import com.example.appbanhang.R;
import com.example.appbanhang.api.ForgotPasswordService;
import com.google.android.material.textfield.TextInputEditText;

public class ForgotPasswordActivity extends AppCompatActivity {
    TextInputEditText edtEmail;
    AppCompatButton btnGuiMa;
    TextView txtQuayLai;
    ForgotPasswordService forgotPasswordService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);

        addControls();
        addEvents();
    }

    private void addControls(){
        edtEmail = findViewById(R.id.edtEmail);
        btnGuiMa = findViewById(R.id.btnGuiMa);
        txtQuayLai = findViewById(R.id.txtQuayLai);

        forgotPasswordService = new ForgotPasswordService(this);
    }

    private void addEvents(){
        btnGuiMa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPassword();

            }
        });
        txtQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        edtEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)){
                    forgotPassword();
                    return true;
                }
                return false;
            }
        });
    }

    private void forgotPassword(){
        String email = edtEmail.getText().toString().trim();
        if(email.isEmpty()){
            edtEmail.setError("Vui lòng nhập email");
            edtEmail.requestFocus();
            return;
        }
        else{
            forgotPasswordService.sendEmail(email);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(forgotPasswordService != null){
            forgotPasswordService.clear();
        }
    }
}