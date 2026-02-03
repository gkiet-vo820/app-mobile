package com.example.appbanhang.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbanhang.R;
import com.example.appbanhang.activity.DetailActivity;
import com.example.appbanhang.model.Product;

import java.text.DecimalFormat;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    Context context;
    List<Product> dsProduct;
    public static final int MODE_ALL = 0;
    public static final int MODE_NEW = 1;
    public static final int MODE_HOT = 2;
    private int mode = MODE_ALL;

    public ProductAdapter(Context context, List<Product> dsProduct) {
        this.context = context;
        this.dsProduct = dsProduct;
    }

    public void setMode(int mode) {
        this.mode = mode;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txtTenSp, txtGiaSp;
        ImageView imgSp, imgNew, imgHot;

        private ItemClickListener itemClickListener;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            txtTenSp = itemView.findViewById(R.id.txtTenSp);
            txtGiaSp = itemView.findViewById(R.id.txtGiaSp);
            imgSp = itemView.findViewById(R.id.imgSp);
            itemView.setOnClickListener(this);

            imgNew = itemView.findViewById(R.id.imgNew);
            imgHot = itemView.findViewById(R.id.imgHot);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),false);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product,parent,false);
        return  new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product product = dsProduct.get(position);
        holder.txtTenSp.setText(product.getTensp());

        String gia = String.valueOf(product.getGia());
        if (gia != null && !gia.isEmpty()) {
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            holder.txtGiaSp.setText("Giá: " + decimalFormat.format(Long.parseLong(gia)) + "Đ");
        } else {
            holder.txtGiaSp.setText("Giá: đang cập nhật");
        }
        Glide.with(context).load(product.getHinhanh()).into(holder.imgSp);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if(!isLongClick){
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("chitiet", product);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });

        holder.imgNew.setVisibility(View.GONE);
        holder.imgHot.setVisibility(View.GONE);
        if (mode == MODE_NEW) {
            holder.imgNew.setVisibility(View.VISIBLE);
        } else if (mode == MODE_HOT) {
            holder.imgHot.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return dsProduct.size();
    }


}
