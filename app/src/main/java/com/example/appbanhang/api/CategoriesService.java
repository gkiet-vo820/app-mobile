package com.example.appbanhang.api;

import android.content.Context;
import android.widget.Toast;

import com.example.appbanhang.adapter.CategoriesAdapter;
import com.example.appbanhang.listener.GetApi;
import com.example.appbanhang.service.RetrofitClient;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CategoriesService {
    private GetApi getApi;
    private Context context;
    private CategoriesAdapter adapter;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public CategoriesService(Context context, CategoriesAdapter adapter) {
        this.context = context;
        this.adapter = adapter;
        getApi = RetrofitClient.getInstance().create(GetApi.class);
    }

    public void getAllCategories() {
        compositeDisposable.add(
                getApi.getAllCategories()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                res -> {
                                    if (res.isSuccess()) {
                                        adapter.clear();
                                        adapter.addAll(res.getData());
                                        adapter.notifyDataSetChanged();
                                    }
                                },
                                err -> Toast.makeText(context, "Lỗi server", Toast.LENGTH_SHORT).show()
                        )
        );
    }
    public void clear() {
        compositeDisposable.clear();
    }
}
