package com.example.appbanhang.api;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbanhang.adapter.ProductAdapter;
import com.example.appbanhang.model.Product;
import com.example.appbanhang.util.GetApi;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ProductService {

    private GetApi getApi;
    private Context context;
    private ProductAdapter adapter;
    private List<Product> dsProduct;
    private TextView txtSoLuongKetQuaTimKiem;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    public ProductService(Context context, ProductAdapter adapter, List<Product> dsProduct) {
        this.context = context;
        this.adapter = adapter;
        this.dsProduct = dsProduct;
        getApi = RetrofitClient.getInstance().create(GetApi.class);
    }

    public ProductService(Context context, ProductAdapter adapter, List<Product> dsProduct, TextView txtSoLuongKetQuaTimKiem) {
        this(context, adapter, dsProduct);
        this.txtSoLuongKetQuaTimKiem = txtSoLuongKetQuaTimKiem;

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
                                        dsProduct.addAll(res.getData());
                                        adapter.notifyDataSetChanged();
                                    }
                                },
                                err -> Toast.makeText(context, "Lỗi server", Toast.LENGTH_SHORT).show()
                        )
        );
    }

    public void getTop10New() {
        compositeDisposable.add(
                getApi.getProductTop10New()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                res -> {
                                    if (res.isSuccess()) {
                                        dsProduct.clear();
                                        dsProduct.addAll(res.getData());
                                        adapter.notifyDataSetChanged();
                                    }
                                },
                                err -> Toast.makeText(context, "Lỗi server", Toast.LENGTH_SHORT).show()
                        )
        );
    }
    public void getTop10BestSeller() {
        compositeDisposable.add(
                getApi.getTop10BestSeller()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                res -> {
                                    if (res.isSuccess()) {
                                        dsProduct.clear();
                                        dsProduct.addAll(res.getData());
                                        adapter.notifyDataSetChanged();
                                    }
                                },
                                err -> Toast.makeText(context, "Lỗi server", Toast.LENGTH_SHORT).show()
                        )
        );
    }

    public void searchProduct(String keyword) {
        compositeDisposable.add(
                getApi.searchProduct(keyword)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                res -> {
                                    if (res.isSuccess()) {
                                        dsProduct.clear();
                                        dsProduct.addAll(res.getData());
                                        adapter.notifyDataSetChanged();

                                        if(txtSoLuongKetQuaTimKiem != null){
                                            txtSoLuongKetQuaTimKiem.setText("Kết quả tìm kiếm được là: " +
                                                                        res.getData().size() + " sản phẩm");
                                        }
                                    }
                                    else {
                                        dsProduct.clear();
                                        adapter.notifyDataSetChanged();
                                        Toast.makeText(context, "Rất tiếc, không tìm thấy: " + keyword, Toast.LENGTH_LONG).show();
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
