package com.example.appbanhang.api;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.appbanhang.activity.MainActivity;
import com.example.appbanhang.adapter.OrdersAdapter;
import com.example.appbanhang.model.Orders;
import com.example.appbanhang.model.ShoppingCart;
import com.example.appbanhang.util.CartStorage;
import com.example.appbanhang.util.GetApi;
import com.example.appbanhang.util.Utils;

import java.util.ArrayList;
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
        void onFinish();
    }

    public interface PageCallback {
        void onResult(int totalPage, int count);
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
                                Toast.makeText(context, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();
                                List<ShoppingCart> cartList = new ArrayList<>();
                                for (ShoppingCart item : Utils.dsShoppingCart) {
                                    if (!item.isSelected()) {
                                        cartList.add(item);
                                    }
                                }
                                Utils.dsShoppingCart = cartList;

                                CartStorage.saveCart(context, Utils.dsShoppingCart);

                                Intent intent = new Intent(context, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                context.startActivity(intent);

                                if (context instanceof Activity) {
                                    ((Activity) context).finish();
                                }
                            } else {
                                Toast.makeText(context, res.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> Toast.makeText(context, "Lỗi kết nối Server", Toast.LENGTH_SHORT).show()
                ));
    }

    public void loadOrderHistory(int userId, int page, int limit, PageCallback callback){
        compositeDisposable.add(getApi.getOrderHistory(userId, page, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        res -> {
                            if (dsOrders.size() > 0 && dsOrders.get(dsOrders.size() - 1) == null) {
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
                                Toast.makeText(context, "Không còn sản phẩm", Toast.LENGTH_SHORT).show();
                                callback.onResult(0, 0);
                            }
                        },
                        err -> {
                            if (dsOrders.size() > 0 && dsOrders.get(dsOrders.size() - 1) == null) {
                                dsOrders.remove(dsOrders.size() - 1);
                                adapter.notifyItemRemoved(dsOrders.size());
                            }
                            Toast.makeText(context, "Lỗi server", Toast.LENGTH_SHORT).show();
                        }
                )
        );
    }

    public void clear() {
        compositeDisposable.clear();
    }
}
