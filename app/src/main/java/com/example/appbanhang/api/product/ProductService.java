package com.example.appbanhang.api.product;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbanhang.adapter.product.ProductAdapter;
import com.example.appbanhang.model.Product;
import com.example.appbanhang.listener.GetApi;
import com.example.appbanhang.service.RetrofitClient;

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

    //Xài cho User
    public ProductService(Context context, ProductAdapter adapter, List<Product> dsProduct) {
        this.context = context;
        this.adapter = adapter;
        this.dsProduct = dsProduct;
        this.getApi = RetrofitClient.getInstance().create(GetApi.class);
    }

    //Xài cho Search
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
//                                        refreshRealtime();
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
//                                        refreshRealtime();
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
//                                        refreshRealtime();
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
                                    dsProduct.clear();
                                    if (res.isSuccess() && res.getData() != null) {
                                        dsProduct.addAll(res.getData());
                                        if (txtSoLuongKetQuaTimKiem != null) {
                                            txtSoLuongKetQuaTimKiem.setText("Kết quả tìm kiếm được là: " +
                                                    res.getData().size() + " sản phẩm");
                                        }
                                    } else {
                                        if (txtSoLuongKetQuaTimKiem != null) {
                                            txtSoLuongKetQuaTimKiem.setText("Không tìm thấy kết quả cho '" + keyword + "'");
                                        }
                                    }
                                    adapter.notifyDataSetChanged();
//                                    refreshRealtime();
                                },
                                err -> Toast.makeText(context, "Lỗi server", Toast.LENGTH_SHORT).show()
                        )
        );
    }

    public void deleteProduct(int id){
        compositeDisposable.add(
                getApi.deleteProduct(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                res -> {
                                    if(res.isSuccess()){
                                        Toast.makeText(context, "Đã xóa sản phẩm ", Toast.LENGTH_SHORT).show();
                                        getAllSanPham();
                                    } else {
                                        Toast.makeText(context, res.getMessage(), Toast.LENGTH_SHORT).show();
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
