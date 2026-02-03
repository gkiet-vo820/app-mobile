package com.example.appbanhang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbanhang.R;
import com.example.appbanhang.model.Product;

import java.text.DecimalFormat;
import java.util.List;

public class LaptopAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context context;
    List<Product> dsProduct;

    private ItemClickListener itemClickListener;
    private static final int VIEW_TYPE_DATA = 0;
    private static final int VIEW_TYPE_LOADING = 1;

    public LaptopAdapter(Context context, List<Product> dsProduct) {
        this.context = context;
        this.dsProduct = dsProduct;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        return dsProduct.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_DATA;
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBar;
        public LoadingViewHolder(@NonNull View itemView){
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txtTenSpLT, txtGiaSpLT, txtMoTaSpLT;
        ImageView imgLaptop;
        public  MyViewHolder(@NonNull View itemView){
            super(itemView);
            txtTenSpLT = itemView.findViewById(R.id.txtTenSpLT);
            txtGiaSpLT = itemView.findViewById(R.id.txtGiaSpLT);
            txtMoTaSpLT = itemView.findViewById(R.id.txtMoTaSpLT);
            imgLaptop = itemView.findViewById(R.id.imgLaptop);
        }


    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_DATA){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_laptop,parent,false);
            return new MyViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading,parent,false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyViewHolder){
            MyViewHolder myViewHolder = (MyViewHolder) holder;

            Product product = dsProduct.get(position);
            myViewHolder.txtTenSpLT.setText(product.getTensp());
            if(product.getGia() > 0){
                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                myViewHolder.txtGiaSpLT.setText("Giá: " + decimalFormat.format(product.getGia()) + "Đ");
            } else {
                myViewHolder.txtGiaSpLT.setText("Giá đang cập nhật");
            }
            myViewHolder.txtMoTaSpLT.setText("Mô tả: " + product.getMota());
            Glide.with(context).load(product.getHinhanh()).into(myViewHolder.imgLaptop);

            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onClick(v, holder.getAdapterPosition(), false);
                    }
                }
            });
        }
        else{
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }


    @Override
    public int getItemCount() {
        return dsProduct.size();
    }
}
