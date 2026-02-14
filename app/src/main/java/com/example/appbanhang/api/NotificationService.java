package com.example.appbanhang.api;

import android.content.Context;
import android.util.Log;

import com.example.appbanhang.listener.GetApi;
import com.example.appbanhang.model.Notification;
import com.example.appbanhang.service.RetrofitClient;
import com.example.appbanhang.util.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class NotificationService {
    private GetApi getApi;
    private Context context;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public NotificationService(Context context) {
        this.context = context;
        getApi = RetrofitClient.getInstance().create(GetApi.class);
    }

    public interface NotificationCallback {
        void onSuccess(List<Notification> dsNotifiation);
        void onFailure(String error);
    }

    public void updateToken(String email, String token) {
        Map<String, String> data = new HashMap<>();
        data.put("email", email);
        data.put("token", token);
        compositeDisposable.add(
            getApi.updateToken(data)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                    res -> Log.d("TOKEN_UPDATE", "Server đã cập nhật token thành công"),
                throwable -> Log.e("TOKEN_UPDATE", "Lỗi: " + throwable.getMessage())
            ));
        Log.d("FCM_ACTION", "Đang gửi token " + token + " cho user id: " + Utils.user_current.getId());
    }

    public void getNotification(int userdId, NotificationCallback callBack){
        compositeDisposable.add(
                getApi.getNotification(userdId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                res -> {
                                    if(res.isSuccess()){
                                        callBack.onSuccess(res.getData());
                                    } else {
                                        callBack.onFailure(res.getMessage());
                                    }
                                },
                                    throwable -> callBack.onFailure(throwable.getMessage())
                        )
        );
    }

    public void updateReadStatus(int id){
        compositeDisposable.add(
                getApi.readNotification(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                res ->
                                    Log.d("READ_STATUS", "Đã cập nhật trạng thái đã đọc lên Server"),
                                throwable -> Log.e("READ_STATUS", "Lỗi cập nhật: " + throwable.getMessage())
                        )
        );
    }

    public void clear() {
        compositeDisposable.clear();
    }
}
