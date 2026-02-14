package com.example.appbanhang.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.R;
import com.example.appbanhang.adapter.ChatAdapter;
import com.example.appbanhang.api.UserChatService;
import com.example.appbanhang.model.User;
import com.example.appbanhang.util.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    Toolbar toolBarTinNhan;
    RecyclerView recyclerViewTinNhan;
    ChatAdapter chatAdapter;
    List<User> userList;
    UserChatService userChatService;
    FloatingActionButton fabNewChat;
    boolean isSearching = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        addControls();
        ActionBar();
        showListUserChat();
        addEvents();
    }

    private void ActionBar() {
        setSupportActionBar(toolBarTinNhan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBarTinNhan.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addControls(){
        toolBarTinNhan = findViewById(R.id.toolBarTinNhan);
        recyclerViewTinNhan = findViewById(R.id.recyclerViewTinNhan);
        fabNewChat = findViewById(R.id.fabNewChat);

        recyclerViewTinNhan.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTinNhan.setHasFixedSize(true);
        userChatService = new UserChatService(this);
    }

    private void showListUserChat(){
        int myId = Utils.user_current.getId();
        int myRole = Utils.user_current.getRole();
        String myIdStr = String.valueOf(myId);

        DatabaseReference databaseReference = FirebaseDatabase
                    .getInstance("https://app-mobile-sells-device-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("contacts").child(myIdStr);

        databaseReference.orderByChild("timeStamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!isSearching){
                    userList = new ArrayList<>(); // Khởi tạo lại list
                    if (snapshot.exists()) {
                        for (DataSnapshot data : snapshot.getChildren()) {
                            User user = data.getValue(User.class);
                            if (user != null) {
                                userList.add(0, user);
                            }
                        }
                        chatAdapter = new ChatAdapter(ChatActivity.this, userList);
                        recyclerViewTinNhan.setAdapter(chatAdapter);
                    } else {
                        // CÁCH 3: Logic thông minh
                        if (myRole == 0) {
                            // Khách chưa chat bao giờ -> Hiện Admin từ MySQL để khách biết chỗ nhắn
                            getSuggestedContacts(myId, myRole);
                        } else {
                            // Admin chưa có ai nhắn -> Màn hình trắng sạch
                            userList.clear();
                            chatAdapter = new ChatAdapter(ChatActivity.this, userList);
                            recyclerViewTinNhan.setAdapter(chatAdapter);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase_Error", error.getMessage());
            }
        });
    }

    private void getSuggestedContacts(int id, int role) {
        userChatService.getChatList(id, role, new UserChatService.UserChatListener() {
            @Override
            public void onSuccess(List<User> dsUser) {
//                userList = dsUser;
//                chatAdapter = new ChatAdapter(ChatActivity.this, userList);
//                recyclerViewTinNhan.setAdapter(chatAdapter);
                List<User> chiLayAdmin = new ArrayList<>();

                for (User user : dsUser) {
                    // CHỈ THÊM VÀO NẾU LÀ ADMIN (ROLE = 1)
                    if (user.getRole() == 1) {
                        chiLayAdmin.add(user);
                    }
                }

                // Gán danh sách đã lọc cho userList
                userList = chiLayAdmin;

                chatAdapter = new ChatAdapter(ChatActivity.this, userList);
                recyclerViewTinNhan.setAdapter(chatAdapter);

                if (userList.isEmpty()) {
                    Toast.makeText(ChatActivity.this, "Hiện chưa có Admin nào trực", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String message) {
            }
        });
    }

    private void addEvents(){
        fabNewChat.setOnClickListener(v -> {
            // Khi nhấn nút này, ta gọi API lấy danh sách Admin từ MySQL
            // để khách chọn người muốn chat mới.
            isSearching = true;
            getSuggestedContacts(Utils.user_current.getId(), Utils.user_current.getRole());
            Toast.makeText(this, "Chọn người bạn muốn nhắn tin", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onDestroy() {
        if(userChatService != null){
            userChatService.clear();
        }
        super.onDestroy();
    }
}