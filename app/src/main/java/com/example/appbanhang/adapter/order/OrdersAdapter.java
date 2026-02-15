package com.example.appbanhang.adapter.order;

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
import com.example.appbanhang.listener.ItemClickListener;
import com.example.appbanhang.model.Orders;
import com.example.appbanhang.util.Utils;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    Context context;
    List<Orders> dsOrders;
    private static final int VIEW_TYPE_DATA = 0;
    private static final int VIEW_TYPE_LOADING = 1;
    private ItemClickListener itemClickListener;

    public OrdersAdapter(Context context, List<Orders> dsOrders) {
        this.context = context;
        this.dsOrders = dsOrders;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
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
        TextView txtMaDonHang, txtTinhTrangDonHang;
        RecyclerView recyclerViewDonHang;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMaDonHang = itemView.findViewById(R.id.txtMaDonHang);
            txtTinhTrangDonHang = itemView.findViewById(R.id.txtTinhTrangDonHang);
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

            Utils.setStatus(myHolder.txtTinhTrangDonHang, orders.getStatus());
            myHolder.txtTinhTrangDonHang.setEnabled(false);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(myHolder.recyclerViewDonHang.getContext(),
                    LinearLayoutManager.VERTICAL, false);
            linearLayoutManager.setInitialPrefetchItemCount(orders.getDetailOrders().size());

            OrdersDetailAdapter ordersDetailAdapter = new OrdersDetailAdapter(context, orders.getDetailOrders());
            myHolder.recyclerViewDonHang.setLayoutManager(linearLayoutManager);
            myHolder.recyclerViewDonHang.setNestedScrollingEnabled(false);
            myHolder.recyclerViewDonHang.setAdapter(ordersDetailAdapter);
            myHolder.recyclerViewDonHang.setRecycledViewPool(viewPool);

            myHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int currentPositon = myHolder.getAdapterPosition();
                    if(itemClickListener != null){
                        itemClickListener.onClick(v, currentPositon, false);
                    }
                }
            });

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
