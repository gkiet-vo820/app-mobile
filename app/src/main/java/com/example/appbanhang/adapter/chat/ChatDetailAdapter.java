package com.example.appbanhang.adapter.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.R;
import com.example.appbanhang.model.ChatMesagge;

import java.util.List;

public class ChatDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<ChatMesagge> dsChatMessage;
    private String sendId;
    private static final int TYPE_SEND  = 1;
    private static final int TYPE_RECEIVE = 2;

    public ChatDetailAdapter(Context context, List<ChatMesagge> dsChatMessage, String sendId) {
        this.context = context;
        this.dsChatMessage = dsChatMessage;
        this.sendId = sendId;
    }

    @Override
    public int getItemViewType(int position) {
        if(dsChatMessage.get(position).getSendId().equals(sendId))
            return TYPE_SEND;
        else
            return TYPE_RECEIVE;
    }

    public class SendViewHolder extends RecyclerView.ViewHolder{
        TextView txtGuiTinNhan, txtThoiGianGui;

        public SendViewHolder(@NonNull View itemView) {
            super(itemView);
            txtGuiTinNhan = itemView.findViewById(R.id.txtGuiTinNhan);
            txtThoiGianGui = itemView.findViewById(R.id.txtThoiGianGui);
        }
    }

    public class ReceiveViewHolder extends RecyclerView.ViewHolder{
        TextView txtNhanTinNhan, txtThoiGianNhan;

        public ReceiveViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNhanTinNhan = itemView.findViewById(R.id.txtNhanTinNhan);
            txtThoiGianNhan = itemView.findViewById(R.id.txtThoiGianNhan);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == TYPE_SEND){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_send, parent, false);
            return new SendViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_receive, parent, false);
            return new ReceiveViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMesagge chatMesagge = dsChatMessage.get(position);
        if(holder instanceof SendViewHolder){
            SendViewHolder sendViewHolder = (SendViewHolder) holder;
            sendViewHolder.txtGuiTinNhan.setText(chatMesagge.getMessage());
            sendViewHolder.txtThoiGianGui.setText(chatMesagge.getDatetime());
        } else {
            ReceiveViewHolder receiveViewHolder = (ReceiveViewHolder) holder;
            receiveViewHolder.txtNhanTinNhan.setText(chatMesagge.getMessage());
            receiveViewHolder.txtThoiGianNhan.setText(chatMesagge.getDatetime());
        }
    }

    @Override
    public int getItemCount() {
        return dsChatMessage.size();
    }
}
