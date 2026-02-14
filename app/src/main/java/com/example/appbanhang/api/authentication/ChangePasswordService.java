package com.example.appbanhang.api.authentication;

import android.app.Dialog;
import android.content.Context;
import android.widget.Toast;

import com.example.appbanhang.listener.GetApi;
import com.example.appbanhang.model.request.ChangePasswordRequest;
import com.example.appbanhang.service.RetrofitClient;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ChangePasswordService {
    private Context context;
    private GetApi getApi;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public ChangePasswordService(Context context) {
        this.context = context;
        getApi = RetrofitClient.getInstance().create(GetApi.class);
    }

    public void changePassword(int id, String oldPass, String newPass, Dialog dialog) {
        ChangePasswordRequest request = new ChangePasswordRequest(id, oldPass, newPass);

        compositeDisposable.add(
                getApi.changePassword(request)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                res -> {
                                    if (res.isSuccess()) {
                                        Toast.makeText(context, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                                        if (dialog != null) dialog.dismiss();
                                    } else {
                                        Toast.makeText(context, res.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                },
                                err -> {
                                    Toast.makeText(context, "Lỗi: " + err.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                        )
        );
    }

    public void clear() {
        compositeDisposable.clear();
    }
}
