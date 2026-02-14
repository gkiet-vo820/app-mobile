package com.example.appbanhang.api.admin;

import android.content.Context;
import android.widget.Toast;

import com.example.appbanhang.adapter.admin.InventoryAdapter;
import com.example.appbanhang.listener.GetApi;
import com.example.appbanhang.model.Product;
import com.example.appbanhang.service.RetrofitClient;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ManagerInventoryService {
    private GetApi getApi;
    private Context context;
    private InventoryAdapter inventoryAdapter;
    private List<Product> dsProduct;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public ManagerInventoryService(Context context, List<Product> dsProduct, InventoryAdapter inventoryAdapter) {
        this.context = context;
        this.dsProduct = dsProduct;
        this.inventoryAdapter = inventoryAdapter;
        this.getApi = RetrofitClient.getInstance().create(GetApi.class);
    }

    public void getAllSanPhamInventory() {
        compositeDisposable.add(getApi.getAllProduct()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(res -> {
                    if (res.isSuccess()) {
                        dsProduct.clear();
                        dsProduct.addAll(res.getData());
                        inventoryAdapter.notifyDataSetChanged();
                    }
                }, err -> Toast.makeText(context, "Lỗi server", Toast.LENGTH_SHORT).show()));
    }

    public void deleteInventory(int id){
        compositeDisposable.add(
                getApi.deleteProduct(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                res -> {
                                    if(res.isSuccess()){
                                        Toast.makeText(context, "Đã xóa sản phẩm ", Toast.LENGTH_SHORT).show();
                                        getAllSanPhamInventory();
                                    } else {
                                        Toast.makeText(context, res.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                },
                                err -> Toast.makeText(context, "Lỗi server", Toast.LENGTH_SHORT).show()
                        )
        );
    }

    public void updateStock(int id, int stock){
        compositeDisposable.add(
                getApi.updateStock(id, stock)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                res -> {
                                    if(res.isSuccess()){
                                        Toast.makeText(context, "Cập nhật kho thành công", Toast.LENGTH_SHORT).show();
                                        getAllSanPhamInventory();
                                    } else {
                                        Toast.makeText(context, res.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }, err -> {
                                    Toast.makeText(context, "Lỗi kết nối: " + err.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                        )
        );
    }

    public void getLowStock(int stock) {
        compositeDisposable.add(
                getApi.getLowStock(stock)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                res -> {
                                    if (res.isSuccess()) {
                                        dsProduct.clear();
                                        dsProduct.addAll(res.getData());
                                        inventoryAdapter.notifyDataSetChanged();
                                    }
                                },
                                err -> Toast.makeText(context, "Lỗi tải hàng sắp hết", Toast.LENGTH_SHORT).show()
                        )
        );
    }

    public void clear() {
        compositeDisposable.clear();
    }

}
