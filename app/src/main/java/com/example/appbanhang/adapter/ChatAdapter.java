package com.example.appbanhang.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbanhang.R;
import com.example.appbanhang.activity.ChatDetailActivity;
import com.example.appbanhang.model.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    private Context context;
    private List<User> dsUser;

    public ChatAdapter(Context context, List<User> dsUser) {
        this.context = context;
        this.dsUser = dsUser;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imgUserChat;
        TextView txtUsernameChat, txtTinNhan;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUsernameChat = itemView.findViewById(R.id.txtUsernameChat);
            txtTinNhan = itemView.findViewById(R.id.txtTinNhan);
            imgUserChat = itemView.findViewById(R.id.imgUserChat);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User user = dsUser.get(position);
        holder.txtUsernameChat.setText(user.getUsername());
        holder.txtTinNhan.setText(user.getLastMessage());
        Glide.with(context).load(user.getAvatar()).placeholder(R.drawable.ic_user).error(R.drawable.ic_user).into(holder.imgUserChat);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatDetailActivity.class);
                intent.putExtra("id_nhan", user.getId());
                intent.putExtra("ten_nhan", user.getUsername());
                intent.putExtra("avatar_nhan", user.getAvatar());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dsUser.size();
    }
}
