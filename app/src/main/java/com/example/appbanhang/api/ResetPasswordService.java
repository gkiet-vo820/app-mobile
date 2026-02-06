package com.example.appbanhang.api;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.appbanhang.activity.LoginActivity;
import com.example.appbanhang.model.request.ResetPasswordRequest;
import com.example.appbanhang.util.GetApi;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ResetPasswordService {
    private Context context;
    private GetApi getApi;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public ResetPasswordService(Context context) {
        this.context = context;
        getApi = RetrofitClient.getInstance().create(GetApi.class);
    }

    public void resetPassword(String email, String otp, String password, String rePassword) {
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setEmail(email);
        request.setOtp(otp);
        request.setNewPassword(password);
        request.setReNewPassword(rePassword);

        compositeDisposable.add(
                getApi.resetPassword(request)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                res -> {
                                    Intent intent = new Intent(context, LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    context.startActivity(intent);
                                },
                                err -> {
                                    Toast.makeText(context, "Đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                                }
                        )
        );
    }

    public void clear() {
        compositeDisposable.clear();
    }
}
