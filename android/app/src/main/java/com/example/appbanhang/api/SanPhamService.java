package com.example.appbanhang.api;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.appbanhang.adapter.SanPhamAdapter;
import com.example.appbanhang.model.SanPham;
import com.example.appbanhang.util.Configure;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SanPhamService {
    private RequestQueue requestQueue;
    private Context context;
    private SanPhamAdapter adapter;
    private List<SanPham> dsSanPham;

    public SanPhamService(Context context, SanPhamAdapter adapter,List<SanPham> dsSanPham) {
        this.requestQueue = Volley.newRequestQueue(context);
        this.context = context;
        this.adapter = adapter;
        this.dsSanPham = dsSanPham;
    }
    public void getAllSanPham() {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                Configure.URL_SP,
                null,
                response -> {
                    try {
                        if (response.getBoolean("success")) {
                            JSONArray array = response.getJSONArray("data");

                            dsSanPham.clear();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obj = array.getJSONObject(i);
                                dsSanPham.add(new SanPham(
                                        obj.getInt("id"),
                                        obj.getString("name"),
                                        obj.getDouble("price"),
                                        obj.getString("image"),
                                        obj.getString("description"),
                                        obj.getInt("category"),
                                        obj.getString("createdAt"),
                                        obj.getInt("soldQuantity")
                                ));
                            }
                            adapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Lỗi parse", Toast.LENGTH_SHORT).show();
                    }

                },
                error -> Toast.makeText(context, "Lỗi server", Toast.LENGTH_SHORT).show()
        );
        requestQueue.add(request);
    }
}
