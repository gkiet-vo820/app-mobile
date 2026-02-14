package com.example.appbanhang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.R;
import com.example.appbanhang.adapter.NotificationAdapter;
import com.example.appbanhang.api.NotificationService;
import com.example.appbanhang.model.Notification;
import com.example.appbanhang.util.Utils;

import java.util.List;


public class NotificationActivity extends AppCompatActivity {
    Toolbar toolBarThongBao;
    RecyclerView recyclerViewThongBao;
    NotificationService notificationService;
    NotificationAdapter notificationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notification);

        addControls();
        ActionBar();
        loadNotification();
    }

    private void ActionBar() {
        setSupportActionBar(toolBarThongBao);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBarThongBao.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addControls(){
        toolBarThongBao = findViewById(R.id.toolBarThongBao);
        recyclerViewThongBao = findViewById(R.id.recyclerViewThongBao);
        recyclerViewThongBao.setLayoutManager(new LinearLayoutManager(this));

        notificationService = new NotificationService(this);
    }

    private void loadNotification(){
        if(Utils.user_current != null){
            notificationService.getNotification(Utils.user_current.getId(), new NotificationService.NotificationCallback() {
                @Override
                public void onSuccess(List<Notification> dsNotifiation) {
                    notificationAdapter = new NotificationAdapter(NotificationActivity.this, dsNotifiation);
                    recyclerViewThongBao.setAdapter(notificationAdapter);

                    notificationAdapter.setItemClickListener((view, position, isLongClick) -> {
                        Notification notification = dsNotifiation.get(position);
                        if(notification.getIsRead() == 0){
                            notification.setIsRead(1);
                            notificationAdapter.notifyItemChanged(position);
                            notificationService.updateReadStatus(notification.getId());
                        }

                        if(notification.getOrderId() > 0){
                            Intent intent = new Intent(NotificationActivity.this, OrderDetailActivity.class);
                            intent.putExtra("order_id", notification.getOrderId());
                            startActivity(intent);
                        }
                    });
                }

                @Override
                public void onFailure(String error) {
                    Toast.makeText(NotificationActivity.this, "Lỗi: " + error, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        notificationService.clear();
        super.onDestroy();
    }
}