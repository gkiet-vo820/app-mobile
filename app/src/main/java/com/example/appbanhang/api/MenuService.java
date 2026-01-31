package com.example.appbanhang.api;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.appbanhang.adapter.MenuAdapter;
import com.example.appbanhang.model.Menu;
import com.example.appbanhang.util.Configure;

import org.json.JSONArray;
import org.json.JSONObject;

public class MenuService {
    private RequestQueue requestQueue;
    private Context context;
    private MenuAdapter adapter;

    public MenuService(Context context, MenuAdapter adapter) {
        this.context = context;
        this.adapter = adapter;
        this.requestQueue = Volley.newRequestQueue(context);
    }

    public void getAllLoaiSanPham() {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                Configure.URL_MENU,
                null,
                response -> {
                    try {
                        if (response.getBoolean("success")) {
                            JSONArray array = response.getJSONArray("data");

                            adapter.clear();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obj = array.getJSONObject(i);
                                adapter.add(new Menu(
                                        obj.getInt("id"),
                                        obj.getString("name"),
                                        obj.getString("image")
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
