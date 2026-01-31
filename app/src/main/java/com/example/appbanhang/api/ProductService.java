package com.example.appbanhang.api;

import android.content.Context;
import android.widget.Toast;

import com.example.appbanhang.adapter.ProductAdapter;
import com.example.appbanhang.model.Product;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class ProductService {
    private Context context;
    private ProductAdapter adapter;
    private List<Product> dsProduct;
    private ProductApi productApi;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public ProductService(Context context, ProductAdapter adapter, List<Product> dsProduct) {
        this.context = context;
        this.adapter = adapter;
        this.dsProduct = dsProduct;
        productApi = RetrofitClient.getInstance().create(ProductApi.class);
    }

    public void getAllSanPham() {
        compositeDisposable.add(
                productApi.getAllProduct()
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
