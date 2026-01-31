package com.example.appbanhang.api;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.appbanhang.adapter.LaptopAdapter;
import com.example.appbanhang.model.Product;
import com.example.appbanhang.util.Configure;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class LaptopService {
    private RequestQueue requestQueue;
    private Context context;
    private LaptopAdapter adapter;
    private List<Product> dsProduct;
    public LaptopService(Context context, LaptopAdapter adapter,List<Product> dsProduct) {
        this.requestQueue = Volley.newRequestQueue(context);
        this.context = context;
        this.adapter = adapter;
        this.dsProduct = dsProduct;
    }

    public interface PageCallback {
        void onResult(int totalPage, int count);
    }
    public void getAllLaptop(int loai, int page, int limit, LaptopService.PageCallback callback) {
        String url = Configure.URL_CATEGORY + "?category=" + loai + "&page=" + page + "&limit=" + limit;;
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        if (response.getBoolean("success")) {
                            JSONArray array = response.getJSONArray("data");

                            if(page == 0){
                                dsProduct.clear();
                            }
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obj = array.getJSONObject(i);
                                dsProduct.add(new Product(
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
                            int totalPage = response.getInt("totalPage");
                            callback.onResult(totalPage, array.length());
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
