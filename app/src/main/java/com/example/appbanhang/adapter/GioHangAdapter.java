package com.example.appbanhang.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbanhang.ImageClickListener;
import com.example.appbanhang.R;
import com.example.appbanhang.model.GioHang;
import com.example.appbanhang.model.eventbus.TinhTongEvent;
import com.example.appbanhang.util.Utils;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.MyViewHolder> {
    Context context;
    List<GioHang> gioHangList;
    private boolean isEditMode = false;


    public GioHangAdapter(Context context, List<GioHang> gioHangList) {
        this.context = context;
        this.gioHangList = gioHangList;
    }
    public void setEditMode(boolean mode) {
        this.isEditMode = mode;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtTenSp_GioHang, txtTenGiaSp_GioHang, txtSoLuongSP_GioHang, txtTenGiaSp2_GioHang;
        ImageView imgGioHang, imgTru_GioHang, imgCong_GioHang, imgDelete;
        private ImageClickListener imageClickListener;
        public MyViewHolder(@NonNull View itemView){
            super((itemView));
            txtTenSp_GioHang = itemView.findViewById(R.id.txtTenSp_GioHang);
            txtTenGiaSp_GioHang = itemView.findViewById(R.id.txtTenGiaSp_GioHang);
            txtTenGiaSp2_GioHang = itemView.findViewById(R.id.txtTenGiaSp2_GioHang);
            txtSoLuongSP_GioHang = itemView.findViewById(R.id.txtSoLuongSP_GioHang);

            imgGioHang = itemView.findViewById(R.id.imgGioHang);
            imgTru_GioHang = itemView.findViewById(R.id.imgTru_GioHang);
            imgCong_GioHang = itemView.findViewById(R.id.imgCong_GioHang);
            imgCong_GioHang.setOnClickListener(this);
            imgTru_GioHang.setOnClickListener(this);

            imgDelete = itemView.findViewById(R.id.imgDelete);
            imgDelete.setOnClickListener(this);

        }

        public void setImageClickListener(ImageClickListener imageClickListener) {
            this.imageClickListener = imageClickListener;
        }

        @Override
        public void onClick(View v) {
            if(v == imgTru_GioHang){
                imageClickListener.onImageClick(v, getAdapterPosition(), 1);
            }
            else if(v == imgCong_GioHang){
                imageClickListener.onImageClick(v, getAdapterPosition(), 2);
            }
            else if(v == imgDelete) {
                // Giá trị 3 đại diện cho hành động Xóa
                imageClickListener.onImageClick(v, getAdapterPosition(), 3);
            }
        }
    }
    @NonNull
    @Override
    public GioHangAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_giohang, parent,  false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GioHangAdapter.MyViewHolder holder, int position) {
        GioHang gioHang = gioHangList.get(position);
        holder.txtTenSp_GioHang.setText(gioHang.getTensp());
        holder.txtSoLuongSP_GioHang.setText(String.valueOf(gioHang.getSoluong()));
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txtTenGiaSp_GioHang.setText("Giá: " + decimalFormat.format(gioHang.getGiasp()) + "Đ");

        double tongTien = gioHang.getGiasp() * gioHang.getSoluong();
        holder.txtTenGiaSp2_GioHang.setText("Tổng tiền: " + decimalFormat.format(tongTien) + "Đ");
        Glide.with(context).load(gioHang.getHinhsp()).into(holder.imgGioHang);

        if (isEditMode) {
            holder.imgDelete.setVisibility(View.VISIBLE);
        } else {
            holder.imgDelete.setVisibility(View.GONE);
        }
        holder.setImageClickListener(new ImageClickListener() {
            @Override
            public void onImageClick(View view, int position, int value) {
                if(value == 1){
                    if(gioHangList.get(position).getSoluong() > 1) {
                        int soLuongMoi = gioHangList.get(position).getSoluong() - 1;
                        gioHangList.get(position).setSoluong(soLuongMoi);
                    }
                }
                else if(value == 2){
                    if(gioHangList.get(position).getSoluong() < 11){
                        int soLuongMoi = gioHangList.get(position).getSoluong() + 1;
                        gioHangList.get(position).setSoluong(soLuongMoi);
                    }
                }
                else if(value == 3){ // 4. Xử lý XÓA
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Thông báo");
                    builder.setMessage("Bạn có muốn xóa sản phẩm này khỏi giỏ hàng?");
                    builder.setPositiveButton("Đồng ý", (dialog, which) -> {
                        // Xóa khỏi danh sách Utils và danh sách tại chỗ
                        Utils.dsGioHang.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, gioHangList.size());
                        EventBus.getDefault().postSticky(new TinhTongEvent());
                    });
                    builder.setNegativeButton("Hủy", null);
                    builder.show();
                }
                if (value != 3) {
                    holder.txtSoLuongSP_GioHang.setText(String.valueOf(gioHangList.get(position).getSoluong()));
                    double giaMoi = gioHangList.get(position).getSoluong() * gioHangList.get(position).getGiasp();
                    holder.txtTenGiaSp2_GioHang.setText("Tổng tiền: " + decimalFormat.format(giaMoi) + "Đ");
                    EventBus.getDefault().postSticky(new TinhTongEvent());
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return gioHangList.size();
    }
}
