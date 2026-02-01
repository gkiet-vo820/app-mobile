package com.example.appbanhang.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.example.appbanhang.activity.DetailActivity;
import com.example.appbanhang.model.Product;

import java.text.DecimalFormat;
import java.util.List;

public class PhoneAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<Product> dsProduct;

    private static final int VIEW_TYPE_DATA = 0;
    private static final int VIEW_TYPE_LOADING = 1;

    public PhoneAdapter(Context context, List<Product> dsProduct) {
        this.context = context;
        this.dsProduct = dsProduct;
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBar;
        public LoadingViewHolder(@NonNull View itemView){
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txtTenSpDT, txtGiaSpDT, txtMoTaSpDT;
        ImageView imgDienThoai;
        private ItemClickListener itemClickListener;
        public  MyViewHolder(@NonNull View itemView){
            super(itemView);
            txtTenSpDT = itemView.findViewById(R.id.txtTenSpDT);
            txtGiaSpDT = itemView.findViewById(R.id.txtGiaSpDT);
            txtMoTaSpDT = itemView.findViewById(R.id.txtMoTaSpDT);
            imgDienThoai = itemView.findViewById(R.id.imgDienThoai);
            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener){
            this.itemClickListener = itemClickListener;
        }
        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), false);
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_DATA){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_phone,parent,false);
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
            myViewHolder.txtTenSpDT.setText(product.getTensp());
            String gia = String.valueOf(product.getGia());
            if(gia != null && !gia.isEmpty()){
                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                myViewHolder.txtGiaSpDT.setText("Giá: " + decimalFormat.format(Double.parseDouble(gia)) + "Đ");
            }
            else{
                myViewHolder.txtGiaSpDT.setText("Giá đang được cập nhật");
            }
            myViewHolder.txtMoTaSpDT.setText("Mô tả: " + product.getMota());
            Glide.with(context).load(product.getHinhanh()).into(myViewHolder.imgDienThoai);
            myViewHolder.setItemClickListener(new ItemClickListener() {
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
        }
        else{
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return dsProduct.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_DATA;
    }



    @Override
    public int getItemCount() {
        return dsProduct.size();
    }



}
