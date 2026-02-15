package com.example.appbanhang.api.admin;

import android.content.Context;
import android.util.Log;

import com.example.appbanhang.adapter.admin.ManagerOrdersAdapter;
import com.example.appbanhang.api.order.OrdersService;
import com.example.appbanhang.listener.GetApi;
import com.example.appbanhang.model.Orders;
import com.example.appbanhang.service.RetrofitClient;

import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ManagerOrdersService {
    private Context context;
    private GetApi getApi;
    private ManagerOrdersAdapter adapter;
    private List<Orders> dsOrders;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public ManagerOrdersService(Context context, ManagerOrdersAdapter adapter, List<Orders> dsOrders) {
        this.context = context;
        this.adapter = adapter;
        this.dsOrders = dsOrders;
        getApi = RetrofitClient.getInstance().create(GetApi.class);
    }

    public interface OrderCallback {
        void onStart();
        void onFinish();
    }

    public interface StatusUpdateListener {
        void onUpdateSuccess(int position, int newStatus);
        void onUpdateError(String message);
    }

    public void loadAllOrdersForAdmin(int page, int limit, OrdersService.PageCallback callback) {
        compositeDisposable.add(getApi.getAllOrdersAdmin(page, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        res -> {
                            if (!dsOrders.isEmpty() && dsOrders.get(dsOrders.size() - 1) == null) {
                                dsOrders.remove(dsOrders.size() - 1);
                                adapter.notifyItemRemoved(dsOrders.size());
                            }

                            if (res.isSuccess() && res.getData() != null) {
                                Log.d("CHECK_API", "Kết nối thành công! Số lượng đơn: " + res.getData().size());
                                if (page == 1) {
                                    dsOrders.clear();
                                    dsOrders.addAll(res.getData());
                                    adapter.notifyDataSetChanged();
                                } else {
                                    Log.d("CHECK_API", "Server trả về success = false hoặc data null");
                                    int positionStart = dsOrders.size();
                                    dsOrders.addAll(res.getData());
                                    adapter.notifyItemRangeInserted(positionStart, res.getData().size());
                                }
                                callback.onResult(res.getTotalPages(), res.getData().size());
                            }
                        },
                        err -> {
                            if (!dsOrders.isEmpty() && dsOrders.get(dsOrders.size() - 1) == null) {
                                dsOrders.remove(dsOrders.size() - 1);
                                adapter.notifyItemRemoved(dsOrders.size());
                            }
                            callback.onError(err.getMessage());
                        }
                )
        );
    }

    public void updateStatus(int orderId, int status, int position, StatusUpdateListener listener) {
        getApi.updateOrderStatus(orderId, status).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (dsOrders.size() > position && dsOrders.get(position) != null) {
                        dsOrders.get(position).setStatus(status);
                    }
                    listener.onUpdateSuccess(position, status);
                } else {
                listener.onUpdateError("Không thể cập nhật trạng thái");
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                listener.onUpdateError(t.getMessage());
            }
        });
    }

    public void confirmPayment(int orderId, int position, StatusUpdateListener listener) {
        compositeDisposable.add(getApi.confirmPayment(orderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        res -> {
                            // Kiểm tra logic success từ server trước khi báo success cho UI
                            if (res != null && Boolean.TRUE.equals(res.get("success"))) {
                                listener.onUpdateSuccess(position, 1);
                            } else {
                                listener.onUpdateError("Xác nhận thất bại");
                            }
                        },
                        err -> listener.onUpdateError(err.getMessage())
                )
        );
    }

    public void getByStatusAdmin(int status, int page, int limit, OrdersService.PageCallback callback){
        compositeDisposable.add(
                getApi.getByStatusAdmin(status, page, limit)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                res -> {
                                    if(dsOrders.size() > 0 && dsOrders.get(dsOrders.size() - 1) == null){
                                        dsOrders.remove(dsOrders.size() - 1);
                                        adapter.notifyItemRemoved(dsOrders.size());
                                    }

                                    if(res.isSuccess() && res.getData() != null){
                                        if(page == 1){
                                            dsOrders.clear();
                                            dsOrders.addAll(res.getData());
                                            adapter.notifyDataSetChanged();
                                        } else {
                                            int positionStart = dsOrders.size();
                                            dsOrders.addAll(res.getData());
                                            adapter.notifyItemRangeInserted(positionStart, res.getData().size());
                                        }
                                        callback.onResult(res.getTotalPages(), res.getData().size());
                                    } else {
                                        callback.onError("Không có dữ liệu");
                                    }
                                },
                                    throwable -> {
                                        Log.e("ManagerOrdersService", "Lỗi lọc đơn: " + throwable.getMessage());
                                        callback.onError(throwable.getMessage());
                                    }
                        )
        );
    }

    public void clear() {
        compositeDisposable.clear();
    }
}
