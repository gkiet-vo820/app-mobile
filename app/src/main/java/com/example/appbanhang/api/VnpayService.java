package com.example.appbanhang.api;

import android.content.Context;

import com.example.appbanhang.listener.GetApi;
import com.example.appbanhang.service.RetrofitClient;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class VnpayService {
    private Context context;
    private GetApi getApi;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    public VnpayService(Context context) {
        this.context = context;
        this.getApi = RetrofitClient.getInstance().create(GetApi.class);
    }

    public interface VnpayUrlCallback {
        void onUrlReceived(String url);
        void onError(String message);
    }

    public void getPaymentUrl(int orderId, long amount, VnpayUrlCallback callback) {
        Map<String, Object> data = new HashMap<>();
        data.put("orderId", orderId);
        data.put("amount", amount);

        compositeDisposable.add(
                getApi.checkOutVNPay(data)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        res -> {
                            if (res.containsKey("url")) {
                                callback.onUrlReceived(res.get("url"));
                            }
                        },
                            err -> {
                            callback.onError(err.getMessage());
                            }
                ));
    }

    public void clear() {
        compositeDisposable.clear();
    }
}