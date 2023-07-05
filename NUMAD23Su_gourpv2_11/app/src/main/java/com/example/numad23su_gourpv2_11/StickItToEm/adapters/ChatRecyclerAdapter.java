package com.example.numad23su_gourpv2_11.StickItToEm.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.numad23su_gourpv2_11.R;
import com.example.numad23su_gourpv2_11.StickItToEm.models.MessageModel;

import java.util.List;

public class ChatRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private final List<MessageModel> messageModels;
    private MessageModel mostRecentStickerMessageModel;
    private final String sender;
    private final RecyclerView recyclerView;
    private final String reciever;
    private final int SENDER_VIEW_HOLDER = 0;
    private final int RECEIVER_VIEW_HOLDER = 1;

    public ChatRecyclerAdapter(Context context, List<MessageModel> messageModels, String sender, String reciever, RecyclerView recyclerView){
        this.context = context;
        this.messageModels = messageModels;
        this.sender = sender;
        this.reciever = reciever;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        if (viewType == RECEIVER_VIEW_HOLDER) {
            return new ChatReceiverViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chat_recieved, parent, false));
        }
        return new ChatSenderViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chat_sent, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case SENDER_VIEW_HOLDER:
                Drawable senderDrawable = context.getResources().getDrawable(Integer.parseInt(messageModels.get(position).getSticker()), context.getTheme());
                ((ChatSenderViewHolder) holder).messageImageView.setImageDrawable(senderDrawable);
                ((ChatSenderViewHolder) holder).messageImageView.setVisibility(View.VISIBLE);
                if (messageModels.get(position) == mostRecentStickerMessageModel) {
                    recyclerView.scrollToPosition(position);
                }
                break;

            case RECEIVER_VIEW_HOLDER:
                Drawable receiverDrawable = context.getResources().getDrawable(Integer.parseInt(messageModels.get(position).getSticker()), context.getTheme());
                ((ChatReceiverViewHolder) holder).message.setImageDrawable(receiverDrawable);
                ((ChatReceiverViewHolder) holder).message.setVisibility(View.VISIBLE);
                if (messageModels.get(position) == mostRecentStickerMessageModel) {
                    recyclerView.scrollToPosition(position);
                }
                break;
        }

//        recyclerView.smoothScrollToPosition(getItemCount() - 1);
    }

    @Override
    public int getItemViewType(int position){
        if (messageModels.get(position).getSender().equals(sender)) {
            return SENDER_VIEW_HOLDER;
        }
        return RECEIVER_VIEW_HOLDER;
    }


    @Override
    public int getItemCount() {
        return messageModels.size();
    }

    public static class ChatSenderViewHolder extends RecyclerView.ViewHolder {

        public ImageView messageImageView;
        public LinearLayout rightChatLayout;
        public TextView rightChatTextView;

        public ChatSenderViewHolder(@NonNull View itemView) {
            super(itemView);
            messageImageView = itemView.findViewById(R.id.sender_chat_image);
            rightChatLayout = itemView.findViewById(R.id.sender_chat_layout);
            rightChatTextView= itemView.findViewById(R.id.sender_message_layout);
        }
    }

    public static class ChatReceiverViewHolder extends RecyclerView.ViewHolder {

        public ImageView message;
        LinearLayout leftChatLayout;
        TextView  leftChatTextView;

        public ChatReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            leftChatLayout = itemView.findViewById(R.id.receiver_chat_layout);
            leftChatTextView = itemView.findViewById(R.id.receiver_message_layout);
            message = itemView.findViewById(R.id.reciever_chat_image);
        }

    }
}
