package com.example.appbanhang.api.admin;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.example.appbanhang.adapter.admin.AdminProductAdapter;
import com.example.appbanhang.listener.GetApi;
import com.example.appbanhang.model.Product;
import com.example.appbanhang.model.response.ProductResponse;
import com.example.appbanhang.service.RetrofitClient;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManagerProductService {
    private GetApi getApi;
    private Context context;
    private AdminProductAdapter adminProductAdapter;
    private List<Product> dsProduct;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    //Xài cho addproductactivity
    public ManagerProductService(Context context) {
        this.context = context;
        this.getApi = RetrofitClient.getInstance().create(GetApi.class);
    }
    // xài cho manageractivity
    public ManagerProductService(Context context, List<Product> dsProduct, AdminProductAdapter adminProductAdapter) {
        this(context);
        this.dsProduct = dsProduct;
        this.adminProductAdapter = adminProductAdapter;
    }

    public void getAllSanPhamAdmin() {
        compositeDisposable.add(getApi.getAllProduct()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(res -> {
                    if (res.isSuccess()) {
                        dsProduct.clear();
                        dsProduct.addAll(res.getData());
                        adminProductAdapter.notifyDataSetChanged();
                    }
                }, err -> Toast.makeText(context, "Lỗi server", Toast.LENGTH_SHORT).show()));
    }
    public void addProduct(RequestBody name, RequestBody price, MultipartBody.Part image, RequestBody description,
                           RequestBody categoryId, RequestBody stockQuantity){
        getApi.addProduct(name, price, image, description, categoryId, stockQuantity)
                .enqueue(new Callback<ProductResponse>() {
                    @Override
                    public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                        if(response.body().isSuccess()){
                            Toast.makeText(context, "Thêm sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                            if (context instanceof Activity) {
                                ((Activity) context).finish();
                            }
                        } else {
                            Toast.makeText(context, "Thêm thất bại: " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ProductResponse> call, Throwable t) {
                        Toast.makeText(context, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void updateProduct(RequestBody id, RequestBody name, RequestBody price, MultipartBody.Part image,
                              RequestBody description, RequestBody categoryId, RequestBody stockQuantity){
        getApi.updateProduct(id, name, price, image, description, categoryId, stockQuantity)
                .enqueue(new Callback<ProductResponse>() {
                    @Override
                    public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                        if(response.body().isSuccess()){
                            Toast.makeText(context, "Sửa sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                            if (context instanceof Activity) {
                                ((Activity) context).finish();
                            }
                        } else {
                            Toast.makeText(context, "Sửa thất bại: " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ProductResponse> call, Throwable t) {
                        Toast.makeText(context, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
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
                                        getAllSanPhamAdmin();
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
