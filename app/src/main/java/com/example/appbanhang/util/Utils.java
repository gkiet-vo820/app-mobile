package com.example.appbanhang.util;

import android.graphics.Color;
import android.widget.TextView;

import com.example.appbanhang.model.ShoppingCart;
import com.example.appbanhang.model.User;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static List<ShoppingCart> dsShoppingCart = new ArrayList<>();
    public static User user_current = new User();

    public static void setStatus(TextView textView, int status) {
        String statusText;
        int statusColor;
        switch (status) {
            case 0:
                statusText = "Chờ thanh toán!";
                statusColor = Color.parseColor("#E67E22"); // Màu cam
                break;
            case 1:
                statusText = "Đã thanh toán!";
                statusColor = Color.BLUE;
                break;
            case 2:
                statusText = "Chờ xử lý!";
                statusColor = Color.parseColor("#F1C40F"); // Màu vàng
                break;
            case 3:
                statusText = "Đang giao!";
                statusColor = Color.parseColor("#1ABC9C");; // Màu xanh ngọc
                break;
            case 4:
                statusText = "Đã giao!";
                statusColor = Color.parseColor("#27AE60"); // Màu xanh lá
                break;
            case 5:
                statusText = "Đã hủy!";
                statusColor = Color.RED;
                break;
            default:
                statusText = "Trạng thái đơn hàng đã được cập nhật!";
                statusColor = Color.GRAY;
                break;
        }
        textView.setText("Trạng thái: " + statusText);
        textView.setTextColor(statusColor);
    }
}
