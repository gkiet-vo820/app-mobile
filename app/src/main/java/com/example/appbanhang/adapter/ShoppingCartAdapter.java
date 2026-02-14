package com.example.appbanhang.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbanhang.R;
import com.example.appbanhang.listener.ImageClickListener;
import com.example.appbanhang.model.ShoppingCart;
import com.example.appbanhang.model.eventbus.TotalEvent;
import com.example.appbanhang.util.CartStorage;
import com.example.appbanhang.util.Configure;
import com.example.appbanhang.util.Utils;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.MyViewHolder> {
    Context context;
    List<ShoppingCart> shoppingCartList;
    private boolean isEditMode = false;


    public ShoppingCartAdapter(Context context, List<ShoppingCart> shoppingCartList) {
        this.context = context;
        this.shoppingCartList = shoppingCartList;
    }
    public void setEditMode(boolean mode) {
        this.isEditMode = mode;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtTenSp_GioHang, txtTenGiaSp_GioHang, txtSoLuongSP_GioHang, txtTenGiaSp2_GioHang;
        ImageView imgGioHang, imgTru_GioHang, imgCong_GioHang, imgDelete;
        CheckBox chkItem;
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

            chkItem = itemView.findViewById(R.id.chkItem);
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
                imageClickListener.onImageClick(v, getAdapterPosition(), 3);
            }
        }
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shoppingcart, parent,  false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ShoppingCart shoppingCart = shoppingCartList.get(position);
        holder.txtTenSp_GioHang.setText(shoppingCart.getTensp());
        holder.txtSoLuongSP_GioHang.setText(String.valueOf(shoppingCart.getSoluong()));
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txtTenGiaSp_GioHang.setText("Giá: " + decimalFormat.format(shoppingCart.getGiasp()) + "Đ");

        long tongTien = shoppingCart.getGiasp() * shoppingCart.getSoluong();
        holder.txtTenGiaSp2_GioHang.setText("Tổng tiền: " + decimalFormat.format(tongTien) + "Đ");



        String hinhAnhSp = shoppingCart.getHinhsp();
        String fullImageUrl = "";

        if (hinhAnhSp != null) {
            if (hinhAnhSp.contains("http")) {
                fullImageUrl = hinhAnhSp;
            } else {
                fullImageUrl = Configure.URL  + hinhAnhSp;
            }
        }
        Glide.with(context).load(fullImageUrl).into(holder.imgGioHang);
//        Glide.with(context).load(shoppingCart.getHinhsp()).into(holder.imgGioHang);


        if (isEditMode) {
            holder.imgDelete.setVisibility(View.VISIBLE);
            holder.chkItem.setVisibility(View.VISIBLE);
            holder.chkItem.setChecked(shoppingCart.isSelected());
        } else {
            holder.imgDelete.setVisibility(View.GONE);
            holder.chkItem.setVisibility(View.GONE);
        }

        holder.chkItem.setOnCheckedChangeListener(null);
        holder.chkItem.setChecked(shoppingCart.isSelected());

        holder.chkItem.setOnCheckedChangeListener((buttonView, isChecked) -> {
            shoppingCart.setSelected(isChecked);
            CartStorage.saveCart(context, Utils.dsShoppingCart);
            EventBus.getDefault().postSticky(new TotalEvent());
        });
        holder.setImageClickListener(new ImageClickListener() {
            @Override
            public void onImageClick(View view, int position, int value) {
                if(value == 1){
                    if(shoppingCartList.get(position).getSoluong() > 1) {
                        int soLuongMoi = shoppingCartList.get(position).getSoluong() - 1;
                        shoppingCartList.get(position).setSoluong(soLuongMoi);
                    }
                }
                else if(value == 2){
                    if(shoppingCartList.get(position).getSoluong() < 11){
                        int soLuongMoi = shoppingCartList.get(position).getSoluong() + 1;
                        shoppingCartList.get(position).setSoluong(soLuongMoi);
                    }
                }
                else if(value == 3){
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Thông báo");
                    builder.setMessage("Bạn có muốn xóa sản phẩm này khỏi giỏ hàng?");
                    builder.setPositiveButton("Đồng ý", (dialog, which) -> {
                        shoppingCartList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, shoppingCartList.size());
                        EventBus.getDefault().postSticky(new TotalEvent());
                    });
                    builder.setNegativeButton("Hủy", null);
                    builder.show();
                }
                if (value != 3) {
                    holder.txtSoLuongSP_GioHang.setText(String.valueOf(shoppingCartList.get(position).getSoluong()));
                    long giaMoi = shoppingCartList.get(position).getSoluong() * shoppingCartList.get(position).getGiasp();
                    holder.txtTenGiaSp2_GioHang.setText("Tổng tiền: " + decimalFormat.format(giaMoi) + "Đ");
                    EventBus.getDefault().postSticky(new TotalEvent());
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return shoppingCartList.size();
    }
}
