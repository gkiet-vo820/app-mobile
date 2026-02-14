package com.example.appbanhang.adapter.admin;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.R;
import com.example.appbanhang.adapter.OrdersDetailAdapter;
import com.example.appbanhang.api.admin.ManagerOrdersService;
import com.example.appbanhang.model.Orders;
import com.example.appbanhang.util.Utils;

import java.util.List;

public class ManagerOrdersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    Context context;
    List<Orders> dsOrders;
    private static final int VIEW_TYPE_DATA = 0;
    private static final int VIEW_TYPE_LOADING = 1;
    private ManagerOrdersService managerOrdersService;

    public ManagerOrdersAdapter(Context context, List<Orders> dsOrders) {
        this.context = context;
        this.dsOrders = dsOrders;
    }

    public void setManagerOrdersService(ManagerOrdersService service) {
        this.managerOrdersService = service;
    }

    @Override
    public int getItemViewType(int position) {
        return dsOrders.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_DATA;
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
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
        } else {
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

            myHolder.txtTinhTrangDonHang.setOnClickListener(v -> {
                showUpdateStatusDialog(orders.getId(), holder.getAdapterPosition());
            });

            myHolder.recyclerViewDonHang.setLayoutManager(new LinearLayoutManager(context));
            myHolder.recyclerViewDonHang.setAdapter(new OrdersDetailAdapter(context, orders.getDetailOrders()));
            myHolder.recyclerViewDonHang.setNestedScrollingEnabled(false);
            myHolder.recyclerViewDonHang.setRecycledViewPool(viewPool);
        }
    }

    @Override
    public int getItemCount() {
        return dsOrders.size();
    }

    private void showUpdateStatusDialog(int orderId, int position) {
        String[] status = {"Chờ thanh toán", "Đã thanh toán", "Chờ xử lý", "Đang giao", "Đã giao", "Đã hủy"};
        new AlertDialog.Builder(context)
                .setTitle("Cập nhật trạng thái đơn hàng")
                .setItems(status, (dialog, which) -> {
                    if(managerOrdersService != null){
                        managerOrdersService.updateStatus(orderId, which, position, new ManagerOrdersService.StatusUpdateListener() {
                            @Override
                            public void onUpdateSuccess(int pos, int newStatus) {
                                dsOrders.get(pos).setStatus(newStatus);
                                notifyItemChanged(pos);
                                Toast.makeText(context, "Thành công!", Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onUpdateError(String message) {
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                })
                .show();
    }
}
