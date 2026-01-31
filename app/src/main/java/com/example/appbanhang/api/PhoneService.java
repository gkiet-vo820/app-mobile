package com.example.appbanhang.api;

import android.content.Context;
import android.widget.Toast;

import com.example.appbanhang.adapter.PhoneAdapter;
import com.example.appbanhang.model.Product;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class PhoneService {
    private ProductApi productApi;
    private Context context;
    private PhoneAdapter adapter;
    private List<Product> dsProduct;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public PhoneService(Context context, PhoneAdapter adapter, List<Product> dsProduct) {
        this.context = context;
        this.adapter = adapter;
        this.dsProduct = dsProduct;
        productApi = RetrofitClient.getInstance().create(ProductApi.class);
    }

    public interface PageCallback {
        void onResult(int totalPage, int count);
    }

    public void getAllDienThoai(int loai, int page, int limit, PageCallback callback) {
        compositeDisposable.add(
                productApi.getProductCategory(loai, page, limit)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                res -> {
                                    if (res.isSuccess()) {
                                        if (page == 0) dsProduct.clear();
                                        dsProduct.addAll(res.getData());
                                        adapter.notifyDataSetChanged();
                                        callback.onResult(res.getTotalPage(), res.getData().size());
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