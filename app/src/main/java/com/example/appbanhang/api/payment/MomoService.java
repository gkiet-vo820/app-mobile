package com.example.appbanhang.api.payment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;

public class MomoService {
    private Activity activity;
    public static final String MOMO_PACKAGE_NAME = "vn.momo.platform.test";

    public MomoService(Activity activity) {
        this.activity = activity;
    }

    public boolean isAppInstalled(){
        PackageManager pm = activity.getPackageManager();
        try {
            pm.getPackageInfo(MOMO_PACKAGE_NAME, PackageManager.GET_ACTIVITIES);
            android.util.Log.d("MOMO_CHECK", "Tìm thấy App MoMo: " + MOMO_PACKAGE_NAME);
            activity.getPackageManager().getPackageInfo(MOMO_PACKAGE_NAME, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            android.util.Log.e("MOMO_CHECK", "Không tìm thấy package: " + MOMO_PACKAGE_NAME);
            return false;
        }
    }

    public void momoPayment(int amount, String orderId){
        Intent intent = new Intent("com.mservice.momo.PAYMENT");
        intent.setPackage(MOMO_PACKAGE_NAME);

//        Uri uri = Uri.parse("momotransfer://com.mservice.momodevelopment.PAYMENT");
//        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//        intent.setPackage(MOMO_PACKAGE_NAME);

//        Intent intent = new Intent("com.mservice.momodevelopment.PAYMENT");
//        intent.setPackage(MOMO_PACKAGE_NAME);

//        Intent intent = new Intent();
//        intent.setClassName("vn.momo.platform.test", "com.mservice.momodevelopment.PaymentActivity");

        intent.putExtra("merchantname", "App bán hàng");
        intent.putExtra("merchantcode", "MOMOBKUN20180810"); //mã mặc định cho sandbox
        intent.putExtra("amount", (long) amount);
        intent.putExtra("orderId",  orderId);
        intent.putExtra("orderLabel", "Thanh toán đơn hàng " + orderId);
        intent.putExtra("description", "Thanh toán cho đơn hàng tại App bán hàng");

        intent.putExtra("merchantnamelabel", "Dịch vụ");
        intent.putExtra("fee", 0);
        intent.putExtra("status", 0);

        // Quan trọng: Trạng thái và môi trường
        intent.putExtra("requestId", orderId + "_" + System.currentTimeMillis());
        intent.putExtra("partnerCode", "MOMOBKUN20180810");
        try {
            android.util.Log.d("MOMO_DEBUG", "Đang gọi MoMo với số tiền: " + amount);
            activity.startActivityForResult(intent, 1000);
        } catch (Exception e){
            android.util.Log.e("MOMO_ERROR", "Lỗi mở MoMo: " + e.getMessage());
            Toast.makeText(activity, "Vui lòng cài đặt App MoMo Sandbox!", Toast.LENGTH_SHORT).show();
        }
    }
}
