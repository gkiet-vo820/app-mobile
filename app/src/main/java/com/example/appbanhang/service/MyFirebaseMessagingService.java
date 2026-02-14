package com.example.appbanhang.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.appbanhang.R;
import com.example.appbanhang.activity.OrderDetailActivity;
import com.example.appbanhang.activity.admin.ManagerOrdersActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
//    @Override
//    public void onMessageReceived(@NonNull RemoteMessage message) {
//        super.onMessageReceived(message);
//        if(message.getNotification() != null){
//            Log.d("FCM", "Tin nhắn: " + message.getNotification().getBody());
//        }
//    }


    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        Log.d("FCM_RECEIVE", "Data nhận được: " + message.getData().toString());

        if(message.getData().size() > 0){
            String title = message.getData().get("title");
            String body = message.getData().get("body");
            String type = message.getData().get("type");

            String orderId = message.getData().get("order_id");
            sendNotification(title, body, type, orderId);
        }
    }

    private void sendNotification(String title, String body, String type, String orderId){
        Intent intent;

        if("new_orders".equals(type)){
            intent = new Intent(this, ManagerOrdersActivity.class);
        } else {
            intent = new Intent(this, OrderDetailActivity.class);
            if(orderId != null && !orderId.isEmpty()){
                try {
                    intent.putExtra("order_id", Integer.parseInt(orderId));
                } catch (NumberFormatException e){
                    Log.e("FCM_ERROR", "orderId không hợp lệ: " + orderId);
                }
            }
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        int requestCode = (int) System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "app_ban_hang")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(requestCode, builder.build());
    }
}
