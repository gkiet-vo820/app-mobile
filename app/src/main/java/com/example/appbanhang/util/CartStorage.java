package com.example.appbanhang.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.appbanhang.model.ShoppingCart;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class CartStorage {
    private static final String CART_KEY = "shopping_cart";

    public static void saveCart(Context context, List<ShoppingCart> cart) {
        SharedPreferences prefs = context.getSharedPreferences("CART_PREF", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String json = new Gson().toJson(cart);
        editor.putString(CART_KEY, json);
        editor.apply();
    }

    public static List<ShoppingCart> loadCart(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("CART_PREF", Context.MODE_PRIVATE);
        String json = prefs.getString(CART_KEY, null);
        if (json == null) return new ArrayList<>();
        return new Gson().fromJson(
                json,
                new com.google.gson.reflect.TypeToken<List<ShoppingCart>>() {}.getType()
        );
    }

    public static void clearCart(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("CART_PREF", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(CART_KEY);
        editor.apply();
    }
}
