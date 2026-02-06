package com.example.appbanhang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.R;
import com.example.appbanhang.model.Orders;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    Context context;
    List<Orders> dsOrders;

    private static final int VIEW_TYPE_DATA = 0;
    private static final int VIEW_TYPE_LOADING = 1;

    public OrdersAdapter(Context context, List<Orders> dsOrders) {
        this.context = context;
        this.dsOrders = dsOrders;
    }

    @Override
    public int getItemViewType(int position) {
        return dsOrders.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_DATA;
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBar;
        public LoadingViewHolder(@NonNull View itemView){
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txtMaDonHang;
        RecyclerView recyclerViewDonHang;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMaDonHang = itemView.findViewById(R.id.txtMaDonHang);
            recyclerViewDonHang = itemView.findViewById(R.id.recyclerViewDonHang);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_DATA) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orders, parent, false);
            return new MyViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            MyViewHolder myHolder = (MyViewHolder) holder;
            Orders orders = dsOrders.get(position);
            myHolder.txtMaDonHang.setText("Đơn hàng: " + orders.getId());
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(myHolder.recyclerViewDonHang.getContext(),
                                                                            LinearLayoutManager.VERTICAL, false);
            linearLayoutManager.setInitialPrefetchItemCount(orders.getDetailOrders().size());

            DetailOrdersAdapter detailOrdersAdapter = new DetailOrdersAdapter(context, orders.getDetailOrders());
            myHolder.recyclerViewDonHang.setLayoutManager(linearLayoutManager);
            myHolder.recyclerViewDonHang.setNestedScrollingEnabled(false);
            myHolder.recyclerViewDonHang.setAdapter(detailOrdersAdapter);
            myHolder.recyclerViewDonHang.setRecycledViewPool(viewPool);
        }
        else {
            LoadingViewHolder loadingHolder = (LoadingViewHolder) holder;
            loadingHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return dsOrders.size();
    }
}
