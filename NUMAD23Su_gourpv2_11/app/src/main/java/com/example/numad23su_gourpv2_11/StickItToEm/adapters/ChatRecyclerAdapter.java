package com.example.numad23su_gourpv2_11.StickItToEm.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.example.numad23su_gourpv2_11.R;
import com.example.numad23su_gourpv2_11.StickItToEm.models.MessageModel;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ChatRecyclerAdapter extends RecyclerView.Adapter<ChatRecyclerAdapter.ChatViewHolder> {
    private final Context context;
    private final List<MessageModel> messageModels;
    private final String sender;
    private final RecyclerView recyclerView;
    private final String reciever;

    private ArrayList<String> sticker;

    public ChatRecyclerAdapter(Context context, List<MessageModel> messageModels, String sender, String receiver, RecyclerView recyclerView){
        this.context = context;
        this.messageModels = messageModels;
        this.sender = sender;
        this.reciever = receiver;
        this.recyclerView = recyclerView;
        this.sticker = new ArrayList<String>();
    }

    @NonNull
    @Override
    public ChatRecyclerAdapter.ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        LayoutInflater inflater =LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_chat_row, parent, false);
        return new ChatRecyclerAdapter.ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRecyclerAdapter.ChatViewHolder holder, int position) {
        Log.d("chatRecycler",String.valueOf(messageModels.size()));
        Log.d("chatRecycler", messageModels.get(position).toString());
        Log.d("chatRecycler",messageModels.get(position).getSticker());
        String ImagePath = "";
        Drawable res = null;
        switch(messageModels.get(position).getSticker()) {
            case "2131165428":
                res = ContextCompat.getDrawable(context, R.drawable.smile);
                break;
            case "2131165356":
                res = ContextCompat.getDrawable(context, R.drawable.angry);
                break;
            case "2131165334":
                res = ContextCompat.getDrawable(context, R.drawable.crying);
                break;
            case "2131165304":
                res = ContextCompat.getDrawable(context, R.drawable.laugh);
                break;
            default:
                res = ContextCompat.getDrawable(context, R.drawable.random);
        }
        if(sender.equals(messageModels.get(position).getSender())){
            holder.leftChatLayout.setVisibility(View.INVISIBLE);
            holder.messageSender.setImageDrawable(res);
            holder.rightChatLayout.setVisibility(View.VISIBLE);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a");
            LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(messageModels.get(position).getTimestamp()), ZoneId.systemDefault());
            String time = localDateTime.format(formatter);
            holder.rightChatTextView.setText(time);
        }else{
            holder.rightChatLayout.setVisibility(View.INVISIBLE);
            holder.messageReceiver.setImageDrawable(res);
            holder.leftChatLayout.setVisibility(View.VISIBLE);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a");
            LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(messageModels.get(position).getTimestamp()), ZoneId.systemDefault());
            String time = localDateTime.format(formatter);
            holder.leftChatTextView.setText(time);
        }
    }

    @Override
    public int getItemCount() {
        return messageModels.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {

        public ImageView messageSender;
        public LinearLayout rightChatLayout;
        public TextView rightChatTextView;
        public ImageView messageReceiver;
        public LinearLayout leftChatLayout;
        public TextView  leftChatTextView;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            messageSender = itemView.findViewById(R.id.sender_chat_image);
            rightChatLayout = itemView.findViewById(R.id.sender_chat_layout);
            rightChatTextView= itemView.findViewById(R.id.sender_message_layout);

            leftChatLayout = itemView.findViewById(R.id.receiver_chat_layout);
            leftChatTextView = itemView.findViewById(R.id.receiver_message_layout);
            messageReceiver = itemView.findViewById(R.id.reciever_chat_image);
        }
    }
}
