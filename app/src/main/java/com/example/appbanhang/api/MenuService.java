package com.example.appbanhang.api;

import android.content.Context;
import android.widget.Toast;

import com.example.appbanhang.adapter.MenuAdapter;
import com.example.appbanhang.model.Menu;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class MenuService {
    private ProductApi productApi;
    private Context context;
    private MenuAdapter adapter;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public MenuService(Context context, MenuAdapter adapter) {
        this.context = context;
        this.adapter = adapter;
        productApi = RetrofitClient.getInstance().create(ProductApi.class);
    }

    public void getAllLoaiSanPham() {
        compositeDisposable.add(
                productApi.getAllMenu()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                res -> {
                                    if (res.isSuccess()) {
                                        adapter.clear();
                                        for (Menu menu : res.getData()) {
                                            adapter.add(menu);
                                        }
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
