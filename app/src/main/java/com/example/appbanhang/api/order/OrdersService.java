package com.example.appbanhang.api.order;


import android.content.Context;

import com.example.appbanhang.adapter.order.OrdersAdapter;
import com.example.appbanhang.model.Orders;
import com.example.appbanhang.service.RetrofitClient;
import com.example.appbanhang.listener.GetApi;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class OrdersService {

    private Context context;
    private GetApi getApi;

    private OrdersAdapter adapter;

    private List<Orders> dsOrders;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public OrdersService(Context context, OrdersAdapter adapter, List<Orders> dsOrders) {
        this.context = context;
        this.adapter = adapter;
        this.dsOrders = dsOrders;
        getApi = RetrofitClient.getInstance().create(GetApi.class);
    }

    public interface OrderCallback {
        void onStart();
        void onSuccess(int orderId);
        void onError(String message);
        void onFinish();
    }

    public interface PageCallback {
        void onResult(int totalPage, int count);
        void onError(String message);
    }

    public interface StatusUpdateListener {
        void onUpdateSuccess(int position, int newStatus);
        void onUpdateError(String message);
    }

    public void postOrder(Orders order, OrderCallback callback) {
        callback.onStart();
        compositeDisposable.add(getApi.checkOut(order)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        res -> {
                            callback.onFinish();

                            if (res.isSuccess()) {
                                callback.onSuccess(res.getOrderId());
                            } else {
                                callback.onError(res.getMessage());
                            }
                        },
                        err -> {
                            callback.onFinish();
                            callback.onError("Lỗi kết nối: " + err.getMessage());
                        }
                ));
    }

    public void loadOrderHistory(int userId, int page, int limit, PageCallback callback){
        compositeDisposable.add(getApi.getOrderHistory(userId, page, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        res -> {
                            if (dsOrders != null && !dsOrders.isEmpty()) {
                                dsOrders.remove(dsOrders.size() - 1);
                                adapter.notifyItemRemoved(dsOrders.size());
                            }
                            if (res.isSuccess() && res.getData() != null) {
                                if (page == 1) {
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

    public void confirmPayment(int orderId, StatusUpdateListener listener) {
        compositeDisposable.add(getApi.confirmPayment(orderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        res -> {
                            // Kiểm tra logic success từ server trước khi báo success cho UI
                            if (res != null && Boolean.TRUE.equals(res.get("success"))) {
                                listener.onUpdateSuccess(0, 4);
                            } else {
                                listener.onUpdateError("Xác nhận thất bại");
                            }
                        },
                        err -> listener.onUpdateError(err.getMessage())
                )
        );
    }

    public void clear() {
        compositeDisposable.clear();
    }
}
