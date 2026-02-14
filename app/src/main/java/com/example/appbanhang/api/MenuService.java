package com.example.appbanhang.api;

import android.content.Context;
import android.widget.Toast;

import com.example.appbanhang.adapter.MenuAdapter;
import com.example.appbanhang.model.Menu;
import com.example.appbanhang.listener.GetApi;
import com.example.appbanhang.service.RetrofitClient;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MenuService {
    private GetApi getApi;
    private Context context;
    private MenuAdapter adapter;

    private List<Menu> dsMenu;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public MenuService(Context context, MenuAdapter adapter, List<Menu> dsMenu) {
        this.context = context;
        this.adapter = adapter;
        this.dsMenu = dsMenu;
        getApi = RetrofitClient.getInstance().create(GetApi.class);
    }

    public void getAllMenu() {
        compositeDisposable.add(
                getApi.getAllMenu()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                res -> {
                                    if (res.isSuccess()) {
                                        dsMenu.clear();
                                        dsMenu.addAll(res.getData());
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
