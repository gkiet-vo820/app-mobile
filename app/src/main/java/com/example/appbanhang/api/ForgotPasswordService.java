package com.example.appbanhang.api;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.appbanhang.activity.VerifyOtpActivity;
import com.example.appbanhang.model.request.ForgotPasswordEmailRequest;
import com.example.appbanhang.util.GetApi;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ForgotPasswordService {
    private Context context;
    private GetApi getApi;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public ForgotPasswordService(Context context) {
        this.context = context;
        getApi = RetrofitClient.getInstance().create(GetApi.class);
    }

    public void sendEmail(String email) {
        ForgotPasswordEmailRequest request = new ForgotPasswordEmailRequest();
        request.setEmail(email);

        compositeDisposable.add(
                getApi.forgotPassword(request)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                res -> {
                                    Toast.makeText(context, "Mã OTP đã được gửi!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(context, VerifyOtpActivity.class);
                                    intent.putExtra("email", email);
                                    context.startActivity(intent);
                                },
                                err -> {
                                    Log.e("API_ERROR", err.getMessage());
                                    Toast.makeText(context,"Lỗi gửi email", Toast.LENGTH_SHORT).show();
                                }
                        )
        );
    }

    public void clear() {
        compositeDisposable.clear();
    }
}
