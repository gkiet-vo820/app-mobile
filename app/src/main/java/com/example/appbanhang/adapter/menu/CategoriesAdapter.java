package com.example.appbanhang.adapter.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.appbanhang.R;
import com.example.appbanhang.model.Categories;

import java.util.List;

public class CategoriesAdapter extends ArrayAdapter<Categories> {

    Context context;

    public CategoriesAdapter(Context context, List<Categories> data) {
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
            convertView = inflater.inflate(R.layout.item_categories, parent, false);

            viewHolder.txtTenSp = convertView.findViewById(R.id.txtTenSp);
            viewHolder.imgHinhAnh = convertView.findViewById(R.id.imgHinhAnh);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Categories categories = getItem(position);
        if (categories != null) {
            viewHolder.txtTenSp.setText(categories.getTenLoai());
            Glide.with(context).load(categories.getHinhAnh()).into(viewHolder.imgHinhAnh);
        }
        return convertView;
    }
}
