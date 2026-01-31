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
import com.example.appbanhang.ItemClickListener;
import com.example.appbanhang.R;
import com.example.appbanhang.activity.ChiTietActivity;
import com.example.appbanhang.model.LoaiSp;
import com.example.appbanhang.model.SanPham;

import java.text.DecimalFormat;
import java.util.List;
import java.util.zip.Inflater;

public class DienThoaiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<SanPham> dsSanPham;

    private static final int VIEW_TYPE_DATA = 0;
    private static final int VIEW_TYPE_LOADING = 1;

    public DienThoaiAdapter(Context context, List<SanPham> dsSanPham) {
        this.context = context;
        this.dsSanPham = dsSanPham;
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dienthoai,parent,false);
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
            SanPham sanPham = dsSanPham.get(position);
            myViewHolder.txtTenSpDT.setText(sanPham.getTensp());
            String gia = String.valueOf(sanPham.getGia());
            if(gia != null && !gia.isEmpty()){
                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                myViewHolder.txtGiaSpDT.setText("Giá: " + decimalFormat.format(Double.parseDouble(gia)) + "Đ");
            }
            else{
                myViewHolder.txtGiaSpDT.setText("Giá đang được cập nhật");
            }
            myViewHolder.txtMoTaSpDT.setText("Mô tả: " + sanPham.getMota());
            Glide.with(context).load(sanPham.getHinhanh()).into(myViewHolder.imgDienThoai);
            myViewHolder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick) {
                    if(!isLongClick){
                        Intent intent = new Intent(context, ChiTietActivity.class);
                        intent.putExtra("chitiet", sanPham);
//                        intent.putExtra("loai", 1);
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
        return dsSanPham.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_DATA;
    }



    @Override
    public int getItemCount() {
        return dsSanPham.size();
    }



}
