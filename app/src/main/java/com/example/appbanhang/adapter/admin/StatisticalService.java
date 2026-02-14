package com.example.appbanhang.adapter.admin;

import android.content.Context;
import android.util.Log;

import com.example.appbanhang.listener.GetApi;
import com.example.appbanhang.model.Statistical;
import com.example.appbanhang.service.RetrofitClient;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class StatisticalService {
    private Context context;
    private GetApi getApi;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public StatisticalService(Context context) {
        this.context = context;
        getApi = RetrofitClient.getInstance().create(GetApi.class);
    }

    public interface RevenueCallback {
        void onSuccess(List<Statistical> dsStatistical);
        void onError(String message);
    }

    public void getRevenue7Days(RevenueCallback callback){
        compositeDisposable.add(
                getApi.getRevenue7Days()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                res -> {
                                    if(res != null){
                                        callback.onSuccess(res);
                                    } else {
                                        callback.onError("Dữ liệu trống");
                                    }
                                },
                                err -> {
                                    Log.e("RevenueError", err.getMessage());
                                    callback.onError(err.getMessage());
                                }
                        )
        );
    }

    public void clear() {
        compositeDisposable.clear();
    }
}
