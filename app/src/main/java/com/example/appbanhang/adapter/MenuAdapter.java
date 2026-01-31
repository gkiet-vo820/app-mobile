package com.example.appbanhang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.appbanhang.R;
import com.example.appbanhang.model.Menu;

import java.util.List;

public class MenuAdapter extends ArrayAdapter<Menu> {

    Context context;

    public MenuAdapter(Context context, List<Menu> data) {
        super(context, 0, data);
        this.context = context;
    }

    static class ViewHolder {
        TextView txtTenSp;
        ImageView imgHinhAnh;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_menu, parent, false);

            viewHolder.txtTenSp = convertView.findViewById(R.id.txtTenSp);
            viewHolder.imgHinhAnh = convertView.findViewById(R.id.imgHinhAnh);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Menu menu = getItem(position);
        if (menu != null) {
            viewHolder.txtTenSp.setText(menu.getTensanpham());
            Glide.with(context).load(menu.getHinhanh()).into(viewHolder.imgHinhAnh);
        }
        return convertView;
    }
}
