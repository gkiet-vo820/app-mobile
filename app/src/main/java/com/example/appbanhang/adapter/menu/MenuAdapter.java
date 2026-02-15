package com.example.appbanhang.adapter.menu;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbanhang.R;
import com.example.appbanhang.listener.ItemClickListener;
import com.example.appbanhang.model.Menu;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MyViewHolder> {
    Context context;
    private List<Menu> dsMenu;
    boolean isDrawer;
    int selectedPosition = -1;

    private ItemClickListener itemClickListener;
    public MenuAdapter(Context context, List<Menu> dsMenu, boolean isDrawer){
        this.context = context;
        this.dsMenu = dsMenu;
        this.isDrawer = isDrawer;
    }
    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public void setSelectedPosition(int newPosition){
//        if(selectedPosition == newPosition)
//            return;
//
//        int position = selectedPosition;
//        selectedPosition = newPosition;
//        notifyItemChanged(position);
//        notifyItemChanged(selectedPosition);

        // Lưu lại vị trí cũ để xóa highlight
        int oldPosition = selectedPosition;
        selectedPosition = newPosition;

        // Thông báo cho Recycler View vẽ lại 2 vị trí bị thay đổi
        notifyItemChanged(oldPosition);
        notifyItemChanged(selectedPosition);
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtTenMenu;
        ImageView imgHinhAnh;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTenMenu = itemView.findViewById(R.id.txtTenMenu);
            imgHinhAnh = itemView.findViewById(R.id.imgHinhAnh);
        }
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(isDrawer){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_drawer, parent, false);
        }
        else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu, parent, false);
        }
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Menu menu = dsMenu.get(position);
        holder.txtTenMenu.setText(menu.getName());
        Glide.with(context).load(menu.getImage()).into(holder.imgHinhAnh);

        if(position == selectedPosition){
            holder.itemView.setBackgroundColor(Color.parseColor("#e0e0e0"));
            holder.txtTenMenu.setTextColor(Color.parseColor("#0091ea"));
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
            holder.txtTenMenu.setTextColor(Color.BLACK);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = holder.getAdapterPosition();
                setSelectedPosition(currentPosition);
                if (itemClickListener != null) {
                    itemClickListener.onClick(v, currentPosition, false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dsMenu.size();
    }
}
