package com.example.appbanhang.api;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.appbanhang.activity.ResetPasswordActivity;
import com.example.appbanhang.util.GetApi;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class VerifyOtpService {
    private Context context;
    private GetApi getApi;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public VerifyOtpService(Context context) {
        this.context = context;
        this.getApi = RetrofitClient.getInstance().create(GetApi.class);;
    }
    public void verifyOtp(String email, String otp){
        Map<String, String> data = new HashMap<>();
        data.put("email", email);
        data.put("otp", otp);
        compositeDisposable.add(
                getApi.verifyOtp(data)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(res -> {
                            if (res.get("success") != null && (boolean) res.get("success")) {
                                Intent intent = new Intent(context, ResetPasswordActivity.class);
                                intent.putExtra("email", email);
                                intent.putExtra("otp", otp);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            } else {
                                String msg = res.get("message") != null ? res.get("message").toString() : "Mã không đúng";
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                            }
                        }, err -> {
                            Toast.makeText(context, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
                        })
        );
    }
    public void clear() {
        compositeDisposable.clear();
    }

}
