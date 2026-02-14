package com.example.appbanhang.api;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbanhang.adapter.LaptopAdapter;
import com.example.appbanhang.model.Product;
import com.example.appbanhang.listener.GetApi;
import com.example.appbanhang.service.RetrofitClient;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LaptopService {
    private GetApi getApi;
    private Context context;
    private LaptopAdapter adapter;
    private List<Product> dsProduct;
    private TextView txtSoLuongKetQuaTimKiem;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    public LaptopService(Context context, LaptopAdapter adapter,List<Product> dsProduct) {
        this.context = context;
        this.adapter = adapter;
        this.dsProduct = dsProduct;
        getApi = RetrofitClient.getInstance().create(GetApi.class);
    }

    public LaptopService(Context context, LaptopAdapter adapter, List<Product> dsProduct, TextView txtSoLuongKetQuaTimKiem) {
        this(context, adapter, dsProduct);
        this.txtSoLuongKetQuaTimKiem = txtSoLuongKetQuaTimKiem;
    }

    public interface PageCallback {
        void onResult(int totalPage, int count);
    }
    public void getAllLaptop(int loai, int page, int limit, PageCallback callback) {
        compositeDisposable.add(
                getApi.getProductCategory(loai, page, limit)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                res -> {
                                    if (dsProduct.size() > 0 && dsProduct.get(dsProduct.size() - 1) == null) {
                                        dsProduct.remove(dsProduct.size() - 1);
                                        adapter.notifyItemRemoved(dsProduct.size());
                                    }
                                    if (res.isSuccess() && res.getData() != null) {
                                        if (page == 1) {
                                            dsProduct.clear();
                                            dsProduct.addAll(res.getData());
                                            adapter.notifyDataSetChanged();
                                        } else {
                                            int positionStart = dsProduct.size();
                                            dsProduct.addAll(res.getData());
                                            adapter.notifyItemRangeInserted(positionStart, res.getData().size());
                                        }
                                        callback.onResult(res.getTotalPage(), res.getData().size());
                                    } else {
                                        Toast.makeText(context, "Không còn sản phẩm", Toast.LENGTH_SHORT).show();
                                        callback.onResult(0, 0);
                                    }
                                },
                                err -> {
                                    if (dsProduct.size() > 0 && dsProduct.get(dsProduct.size() - 1) == null) {
                                        dsProduct.remove(dsProduct.size() - 1);
                                        adapter.notifyItemRemoved(dsProduct.size());
                                    }
                                    Toast.makeText(context, "Lỗi server", Toast.LENGTH_SHORT).show();
                                }
                        )
        );
    }

    public void searchLaptop(String keyword, int loai) {
        compositeDisposable.add(
                getApi.searchProductCategory(keyword, loai)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                res -> {
                                    dsProduct.clear();
                                    if (res.isSuccess() && res.getData() != null) {
                                        dsProduct.addAll(res.getData());
                                    }
                                    adapter.notifyDataSetChanged();

                                    if (txtSoLuongKetQuaTimKiem != null) {
                                        txtSoLuongKetQuaTimKiem.setVisibility(View.VISIBLE);
                                        if (res.isSuccess() && res.getData() != null && res.getData().size() > 0) {
                                            txtSoLuongKetQuaTimKiem.setText("Kết quả tìm kiếm được là: " +
                                                                res.getData().size() + " sản phẩm");
                                        } else {
                                            txtSoLuongKetQuaTimKiem.setText("Không tìm thấy kết quả cho '" + keyword + "'");
                                            Toast.makeText(context, "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                },
                                err -> {
                                    Toast.makeText(context, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
                                    if (txtSoLuongKetQuaTimKiem != null) {
                                        txtSoLuongKetQuaTimKiem.setVisibility(View.GONE);
                                    }
                                }

                        )
        );
    }

    public void clear() {
        compositeDisposable.clear();
    }
}
