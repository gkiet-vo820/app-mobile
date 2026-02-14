package com.example.appbanhang.api.authentication;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.appbanhang.activity.authentication.LoginActivity;
import com.example.appbanhang.listener.GetApi;
import com.example.appbanhang.model.request.RegisterRequest;
import com.example.appbanhang.service.RetrofitClient;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RegisterService {
    private Context context;
    private GetApi getApi;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public RegisterService(Context context) {
        this.context = context;
        getApi = RetrofitClient.getInstance().create(GetApi.class);
    }
    public void register(String email, String username, String password, String repassword, String sdt) {

        RegisterRequest request = new RegisterRequest(email, username, password, repassword, sdt);

        compositeDisposable.add(
                getApi.register(request)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                res -> {
                                    if (res.isSuccess()) {
                                        Intent intent = new Intent(context, LoginActivity.class);
                                        context.startActivity(intent);
                                    } else {
                                        Toast.makeText(context, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                                    }
                                },
                                err -> {
                                    Toast.makeText(context, "Lỗi server", Toast.LENGTH_SHORT).show();
                                }
                        )
        );
    }

    public void clear() {
        compositeDisposable.clear();
    }
}
