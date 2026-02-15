package com.example.appbanhang.adapter.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbanhang.R;
import com.example.appbanhang.model.DetailOrders;
import com.example.appbanhang.util.Configure;

import java.text.DecimalFormat;
import java.util.List;

public class OrdersDetailAdapter extends RecyclerView.Adapter<OrdersDetailAdapter.MyViewHolder> {

    Context context;
    List<DetailOrders> dsDetailOrders;

    public OrdersDetailAdapter(Context context, List<DetailOrders> dsDetailOrders) {
        this.context = context;
        this.dsDetailOrders = dsDetailOrders;
    }

    public List<DetailOrders> getDsDetailOrders() {
        return dsDetailOrders;
    }

    public void setDsDetailOrders(List<DetailOrders> dsDetailOrders) {
        this.dsDetailOrders = dsDetailOrders;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txtTenDonHang, txtSoLuongDonHang, txtGiaDonHang;
        ImageView imgDonHang;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTenDonHang = itemView.findViewById(R.id.txtTenDonHang);
            txtSoLuongDonHang = itemView.findViewById(R.id.txtSoLuongDonHang);
            txtGiaDonHang = itemView.findViewById(R.id.txtGiaDonHang);
            imgDonHang = itemView.findViewById(R.id.imgDonHang);
        }
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_orders, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DetailOrders detailOrders = dsDetailOrders.get(position);
        if(detailOrders.getProduct() != null){
            holder.txtTenDonHang.setText("Tên đơn hàng: " + detailOrders.getProduct().getTensp());
            holder.txtSoLuongDonHang.setText("Số lượng: " + detailOrders.getQuantity());
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            holder.txtGiaDonHang.setText("Giá: " + decimalFormat.format(detailOrders.getPrice()) + "Đ");
            Glide.with(context).load(detailOrders.getProduct().getHinhanh()).into(holder.imgDonHang);

            String hinhAnhSp = detailOrders.getProduct().getHinhanh();
            String fullImageUrl = "";

            if (hinhAnhSp != null) {
                if (hinhAnhSp.contains("http")) {
                    fullImageUrl = hinhAnhSp;
                } else {
                    fullImageUrl = Configure.URL  + hinhAnhSp;
                }
            }
            Glide.with(context).load(fullImageUrl).into(holder.imgDonHang);
        }
    }

    @Override
    public int getItemCount() {
        return dsDetailOrders.size();
    }
}
