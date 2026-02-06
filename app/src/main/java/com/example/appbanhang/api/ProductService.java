package com.example.appbanhang.api;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbanhang.adapter.AdminProductAdapter;
import com.example.appbanhang.adapter.ProductAdapter;
import com.example.appbanhang.model.Product;
import com.example.appbanhang.model.response.ProductResponse;
import com.example.appbanhang.util.GetApi;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductService {

    private GetApi getApi;
    private Context context;
    private ProductAdapter adapter;
    private List<Product> dsProduct;
    private TextView txtSoLuongKetQuaTimKiem;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private AdminProductAdapter adminProductAdapter;

    //Xài cho addproductactivity
    public ProductService(Context context) {
        this.context = context;
        this.getApi = RetrofitClient.getInstance().create(GetApi.class);
    }

    //Xài cho User
    public ProductService(Context context, ProductAdapter adapter, List<Product> dsProduct) {
        this(context);
        this.adapter = adapter;
        this.dsProduct = dsProduct;
    }

    // xài cho manageractivity
    public ProductService(Context context, List<Product> dsProduct, AdminProductAdapter adminProductAdapter) {
        this(context);
        this.dsProduct = dsProduct;
        this.adminProductAdapter = adminProductAdapter;
    }

    //Xài cho Search
    public ProductService(Context context, ProductAdapter adapter, List<Product> dsProduct, TextView txtSoLuongKetQuaTimKiem) {
        this(context, adapter, dsProduct);
        this.txtSoLuongKetQuaTimKiem = txtSoLuongKetQuaTimKiem;
    }

    private void refreshRealtime() {
        if (dsProduct == null) return;
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        if (adminProductAdapter != null) {
            adminProductAdapter.notifyDataSetChanged();
        }
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
                                        //adapter.notifyDataSetChanged();
                                        refreshRealtime();
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
                                        //adapter.notifyDataSetChanged();
                                        refreshRealtime();
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
                                        //adapter.notifyDataSetChanged();
                                        refreshRealtime();
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
                                    //adapter.notifyDataSetChanged();
                                    refreshRealtime();
                                },
                                err -> Toast.makeText(context, "Lỗi server", Toast.LENGTH_SHORT).show()
                        )
        );
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
