package com.example.appbanhang.api;

import android.content.Context;
import android.widget.Toast;

import com.example.appbanhang.adapter.ProductAdapter;
import com.example.appbanhang.model.Product;
import com.example.appbanhang.util.GetApi;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ProductService {
    private Context context;
    private ProductAdapter adapter;
    private List<Product> dsProduct;
    private GetApi getApi;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public ProductService(Context context, ProductAdapter adapter, List<Product> dsProduct) {
        this.context = context;
        this.adapter = adapter;
        this.dsProduct = dsProduct;
        getApi = RetrofitClient.getInstance().create(GetApi.class);
    }

    public void getAllSanPham() {
        compositeDisposable.add(
                getApi.getAllProduct()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                res -> {
                                    if (res.isSuccess()) {
                                        dsProduct.clear();
                                        for (Product product : res.getData()) {
                                            dsProduct.add(product);
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
