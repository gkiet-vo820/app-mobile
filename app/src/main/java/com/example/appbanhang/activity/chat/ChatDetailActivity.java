package com.example.appbanhang.activity.chat;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbanhang.R;
import com.example.appbanhang.adapter.chat.ChatDetailAdapter;
import com.example.appbanhang.model.ChatMesagge;
import com.example.appbanhang.util.Utils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatDetailActivity extends AppCompatActivity {
    Toolbar toolBarTinNhanChiTiet;
    RecyclerView recyclerViewTinNhanChiTiet;
    EditText edtNhanTin;
    ImageView imgGuiTinNhan;

    ChatDetailAdapter chatDetailAdapter;
    List<ChatMesagge> dsChatMessage;
    private String myId, friendId, friendName, idPhong, friendAvatar;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat_detail);

        addControls();
        ActionBar();
        createRoomChat();
        addEvents();
        listenMessage();
    }

    private void ActionBar() {
        setSupportActionBar(toolBarTinNhanChiTiet);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

            View view = getLayoutInflater().inflate(R.layout.custom_toolbar_chat, null);
            getSupportActionBar().setCustomView(view);
            getSupportActionBar().setDisplayShowCustomEnabled(true);

            CircleImageView imgAvatarToolbar = view.findViewById(R.id.imgAvatarToolbar);
            TextView txtTitleToolbar = view.findViewById(R.id.txtTitleToolbar);

            txtTitleToolbar.setText(friendName);
            Glide.with(this).load(friendAvatar).placeholder(R.drawable.ic_user).error(R.drawable.ic_user).into(imgAvatarToolbar);
        }

        toolBarTinNhanChiTiet.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addControls(){
        toolBarTinNhanChiTiet = findViewById(R.id.toolBarTinNhanChiTiet);
        recyclerViewTinNhanChiTiet = findViewById(R.id.recyclerViewTinNhanChiTiet);
        edtNhanTin = findViewById(R.id.edtNhanTin);
        imgGuiTinNhan = findViewById(R.id.imgGuiTinNhan);

        dsChatMessage = new ArrayList<>();
        myId = String.valueOf(Utils.user_current.getId());
        friendId = String.valueOf(getIntent().getIntExtra("id_nhan", 0));
        friendName = getIntent().getStringExtra("ten_nhan");
        friendAvatar = getIntent().getStringExtra("avatar_nhan");

        chatDetailAdapter = new ChatDetailAdapter(this, dsChatMessage,myId);
        recyclerViewTinNhanChiTiet.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTinNhanChiTiet.setAdapter(chatDetailAdapter);

        // Thêm dòng này để nhận nội dung mẫu nếu có
        String noiDungMau = getIntent().getStringExtra("noi_dung_mau");
        if (noiDungMau != null) {
            edtNhanTin.setText(noiDungMau);
        }
    }

    private void createRoomChat(){
        try {
            int id = Integer.parseInt(myId);
            int otherId = Integer.parseInt(friendId);

            if(id < otherId){
                idPhong = myId + "_" + friendId;
            } else {
                idPhong = friendId + "_" + myId;
            }
            databaseReference = FirebaseDatabase
                    .getInstance("https://app-mobile-sells-device-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("chats").child(idPhong);
        } catch (NumberFormatException e){
            Log.e("FirebaseError", "ID không hợp lệ: " + e.getMessage());
        }

    }

    private void addEvents(){
        imgGuiTinNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = edtNhanTin.getText().toString().trim();
                if(TextUtils.isEmpty(message))
                    return;

                String time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
                ChatMesagge chatMesagge = new ChatMesagge(myId, friendId, message, time);

                databaseReference.push().setValue(chatMesagge).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        updateContactList(message);
                        edtNhanTin.setText(""); // Gửi xong thì xóa chữ trong ô nhập
                    }
                });
            }
        });
    }

    private void listenMessage(){
        // Lắng nghe dữ liệu thay đổi trên Firebase theo thời gian thực
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ChatMesagge chatMesagge = snapshot.getValue(ChatMesagge.class);
                if(chatMesagge != null){
                    dsChatMessage.add(chatMesagge);
                    chatDetailAdapter.notifyDataSetChanged();
                    recyclerViewTinNhanChiTiet.scrollToPosition(dsChatMessage.size() - 1); // Luôn cuộn xuống tin nhắn mới nhất
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateContactList(String message){
        long currentTime = System.currentTimeMillis();
        String dbUrl = "https://app-mobile-sells-device-default-rtdb.asia-southeast1.firebasedatabase.app/";
        DatabaseReference myContact = FirebaseDatabase.getInstance(dbUrl).getReference("contacts").child(myId).child(friendId);

        // 1. Cập nhật cho chính mình (để hiện tin nhắn vừa gửi ở màn hình ngoài)
        Map<String, Object> myMap = new HashMap<>();
        myMap.put("id", Integer.parseInt(friendId));
        myMap.put("username", friendName);
        myMap.put("lastMessage", message);
        myMap.put("timeStamp", currentTime);
        myContact.setValue(myMap);

        // 2. Cập nhật cho người nhận (để họ thấy tin nhắn mới từ bạn nhảy lên đầu)
        DatabaseReference friendContact = FirebaseDatabase.getInstance(dbUrl).getReference("contacts").child(friendId).child(myId);

        Map<String, Object> friendMap = new HashMap<>();
        friendMap.put("id", Integer.parseInt(myId));
        friendMap.put("username", Utils.user_current.getUsername());
        friendMap.put("lastMessage", message);
        friendMap.put("timeStamp", currentTime);
        friendContact.setValue(friendMap);
    }
}