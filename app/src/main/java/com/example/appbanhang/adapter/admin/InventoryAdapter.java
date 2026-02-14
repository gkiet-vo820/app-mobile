package com.example.appbanhang.adapter.admin;

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
import com.example.appbanhang.model.Product;
import com.example.appbanhang.util.Configure;

import java.text.DecimalFormat;
import java.util.List;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.MyViewHolder> {
    Context context;
    List<Product> dsProduct;
    private OnInventoryClickListener listener;

    public InventoryAdapter(Context context, List<Product> dsProduct, OnInventoryClickListener listener) {
        this.context = context;
        this.dsProduct = dsProduct;
        this.listener = listener;
    }

    public interface OnInventoryClickListener {
        void onUpdateStock(Product product);
        void onLongClick(Product product);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imgSanPham;
        TextView txtTenSanPham, txtGiaSanPham, txtSoLuongTonKho;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSanPham = itemView.findViewById(R.id.imgSanPham);
            txtTenSanPham = itemView.findViewById(R.id.txtTenSanPham);
            txtGiaSanPham = itemView.findViewById(R.id.txtGiaSanPham);
            txtSoLuongTonKho = itemView.findViewById(R.id.txtSoLuongTonKho);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inventory, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product product = dsProduct.get(position);
        holder.txtTenSanPham.setText(product.getTensp());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txtGiaSanPham.setText(decimalFormat.format(product.getGia()));
        //Glide.with(context).load(product.getHinhanh()).into(holder.imgSanPham);

        if (product.getSoluongtonkho() < 10) {
            holder.txtSoLuongTonKho.setTextColor(Color.RED);
            holder.txtSoLuongTonKho.setText(product.getSoluongtonkho() + " (Sắp hết!)");
        } else {
            holder.txtSoLuongTonKho.setTextColor(Color.BLACK);
            holder.txtSoLuongTonKho.setText(String.valueOf(product.getSoluongtonkho()));
        }

        String hinhAnhSp = product.getHinhanh();
        String fullImageUrl = "";

        if (hinhAnhSp != null) {
            if (hinhAnhSp.contains("http")) {
                fullImageUrl = hinhAnhSp;
            } else {
                fullImageUrl = Configure.URL  + hinhAnhSp;
            }
        }
        Glide.with(context).load(fullImageUrl).into(holder.imgSanPham);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onUpdateStock(product);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (listener != null) {
                listener.onLongClick(product);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return dsProduct.size();
    }


}
