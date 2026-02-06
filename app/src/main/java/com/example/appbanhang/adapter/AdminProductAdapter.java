package com.example.appbanhang.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbanhang.R;
import com.example.appbanhang.activity.AddProductActivity;
import com.example.appbanhang.activity.ManagerActivity;
import com.example.appbanhang.model.Product;
import com.example.appbanhang.util.Configure;

import java.text.DecimalFormat;
import java.util.List;

public class AdminProductAdapter extends RecyclerView.Adapter<AdminProductAdapter.MyViewHolder> {
    Context context;
    List<Product> dsProduct;

    public AdminProductAdapter(Context context, List<Product> dsProduct) {
        this.context = context;
        this.dsProduct = dsProduct;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txtTenSp, txtGiaSP, txtLoaiSP, txtSoLuongTonKho, txtMoTa;
        ImageView imgSP, imgSua, imgXoa;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTenSp = itemView.findViewById(R.id.txtTenSp);
            txtGiaSP = itemView.findViewById(R.id.txtGiaSP);
            txtLoaiSP = itemView.findViewById(R.id.txtLoaiSP);
            txtSoLuongTonKho = itemView.findViewById(R.id.txtSoLuongTonKho);
            txtMoTa = itemView.findViewById(R.id.txtMoTa);
            imgSP = itemView.findViewById(R.id.imgSP);
            imgSua = itemView.findViewById(R.id.imgSua);
            imgXoa = itemView.findViewById(R.id.imgXoa);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_admin, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product product = dsProduct.get(position);
        holder.txtTenSp.setText(product.getTensp());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txtGiaSP.setText("Giá: " + decimalFormat.format(product.getGia() ) + "Đ");
        holder.txtLoaiSP.setText("Loại: " + product.getLoai());
        holder.txtSoLuongTonKho.setText("Tồn kho: " + product.getSoluongtonkho());
        holder.txtMoTa.setText(product.getMota());
        //Glide.with(context).load(product.getHinhanh()).into(holder.imgSP);

        String hinhAnhSp = product.getHinhanh();
        String fullImageUrl = "";

        if (hinhAnhSp != null) {
            if (hinhAnhSp.contains("http")) {
                fullImageUrl = hinhAnhSp;
            } else {
                fullImageUrl = Configure.URL  + hinhAnhSp;
            }
        }
        Glide.with(context).load(fullImageUrl).into(holder.imgSP);


        holder.imgXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyXoa(product);
            }
        });

        holder.imgSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddProductActivity.class);
                intent.putExtra("update", product);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dsProduct.size();
    }

    private void xuLyXoa(Product product){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc muốn xóa " + product.getTensp() + "?");
        builder.setPositiveButton("Có", (dialog, which) -> {
            if (context instanceof ManagerActivity) {
                ((ManagerActivity) context).deleteProduct(product.getId());
            }
        });
        builder.setNegativeButton("Không", null);
        builder.show();

    }
}
