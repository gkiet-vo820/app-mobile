package com.example.appbanhang.api;

import android.content.Context;

import com.example.appbanhang.listener.GetApi;
import com.example.appbanhang.model.Orders;
import com.example.appbanhang.service.RetrofitClient;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class OrderDetailService {
    Context context;
    GetApi getApi;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public OrderDetailService(Context context) {
        this.context = context;
        getApi = RetrofitClient.getInstance().create(GetApi.class);
    }

    public interface OrderDetailCallback {
        void onSuccess(Orders orders);
        void onFailure(String error);
    }

    public void getOrderDetail(int id, OrderDetailCallback callback){
        compositeDisposable.add(
                getApi.getOrderDetail(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                res -> {
                                    if(res.isSuccess() && res.getData() != null){
                                        callback.onSuccess(res.getData());
                                    } else {
                                        callback.onFailure(res.getMessage());
                                    }
                                },
                                err -> callback.onFailure(err.getMessage())
                        )
        );
    }

    public void clear() {
        compositeDisposable.clear();
    }
}
