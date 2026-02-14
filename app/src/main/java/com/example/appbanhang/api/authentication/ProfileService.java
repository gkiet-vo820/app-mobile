package com.example.appbanhang.api.authentication;

import android.content.Context;
import android.widget.Toast;

import com.example.appbanhang.listener.GetApi;
import com.example.appbanhang.model.User;
import com.example.appbanhang.service.RetrofitClient;
import com.example.appbanhang.util.Utils;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ProfileService {
    private Context context;
    private GetApi getApi;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public ProfileService(Context context) {
        this.context = context;
        getApi = RetrofitClient.getInstance().create(GetApi.class);
    }

    public void updateProfile(User user) {
        compositeDisposable.add(
                getApi.updateProfile(user)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                res -> {
                                    if(res.isSuccess()){
                                        Utils.user_current = res.getUser();
                                        Paper.book().write("user", res.getUser());
                                        Toast.makeText(context, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, res.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                },
                                err -> {
                                    Toast.makeText(context, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();                                }
                        )
        );
    }

    public void clear() {
        compositeDisposable.clear();
    }
}
