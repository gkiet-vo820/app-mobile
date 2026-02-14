package com.example.appbanhang.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appbanhang.R;
import com.example.appbanhang.activity.authentication.LoginActivity;

import io.paperdb.Paper;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        Paper.init(this);
        Thread thread = new Thread() {
            public void run() {
                try {
                    sleep(1500);
                } catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    if(Paper.book().read("user") == null){
                        Intent login = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(login);
                        finish();
                    }
                    else {
                        Intent home = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(home);
                        finish();
                    }

                }
            }
        };
        thread.start();
    }


}