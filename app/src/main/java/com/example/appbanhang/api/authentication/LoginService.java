package com.example.appbanhang.api.authentication;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.appbanhang.activity.MainActivity;
import com.example.appbanhang.model.request.LoginRequest;
import com.example.appbanhang.listener.GetApi;
import com.example.appbanhang.service.RetrofitClient;
import com.example.appbanhang.util.Utils;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginService {
    private Context context;
    private GetApi getApi;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public LoginService(Context context) {
        this.context = context;
        getApi = RetrofitClient.getInstance().create(GetApi.class);
    }
    public void login(String email, String password) {

        LoginRequest request = new LoginRequest(email, password);

        compositeDisposable.add(
                getApi.login(request)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                res -> {
                                    if (res.isSuccess()) {
                                        Paper.book().write("email", email);
                                        Utils.user_current = res.getUser();
                                        Paper.book().write("user", res.getUser());
                                        Paper.book().write("token", res.getToken());

                                        Intent intent = new Intent(context, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        context.startActivity(intent);
                                    } else {
                                        Toast.makeText(context, "Sai email hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                                    }
                                },
                                err -> {
                                    Toast.makeText(context,"Lỗi server", Toast.LENGTH_SHORT).show();
                                }
                        )
        );
    }

    public void clear() {
        compositeDisposable.clear();
    }
}
