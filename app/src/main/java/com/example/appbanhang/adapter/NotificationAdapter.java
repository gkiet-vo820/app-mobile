package com.example.appbanhang.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.R;
import com.example.appbanhang.listener.ItemClickListener;
import com.example.appbanhang.model.Notification;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    Context context;
    List<Notification> dsNotification;
    private ItemClickListener itemClickListener;


    public NotificationAdapter(Context context, List<Notification> dsNotification) {
        this.context = context;
        this.dsNotification = dsNotification;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txtTieuDeThongBao, txtNoiDungThongBao, txtThoiGianThongBao;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTieuDeThongBao = itemView.findViewById(R.id.txtTieuDeThongBao);
            txtNoiDungThongBao = itemView.findViewById(R.id.txtNoiDungThongBao);
            txtThoiGianThongBao = itemView.findViewById(R.id.txtThoiGianThongBao);

        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Notification notification = dsNotification.get(position);
        holder.txtTieuDeThongBao.setText(notification.getTitle());
        holder.txtNoiDungThongBao.setText(notification.getContent());
        holder.txtThoiGianThongBao.setText(notification.getCreatedAt());

        if(notification.getIsRead() == 0){
            holder.itemView.setBackgroundColor(Color.parseColor("#e8f0fe"));
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = holder.getAdapterPosition();
                if(itemClickListener != null){
                    itemClickListener.onClick(v, currentPosition, false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dsNotification.size();
    }
}
