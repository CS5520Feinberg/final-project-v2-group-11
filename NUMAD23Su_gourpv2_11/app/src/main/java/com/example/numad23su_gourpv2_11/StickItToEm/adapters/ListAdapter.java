package com.example.numad23su_gourpv2_11.StickItToEm.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.numad23su_gourpv2_11.R;
import com.example.numad23su_gourpv2_11.StickItToEm.ChatActivity;
import com.example.numad23su_gourpv2_11.StickItToEm.models.User;

import java.util.List;


public class ListAdapter extends RecyclerView.Adapter<ListAdapter.FriendHolder> {

    private final Context context;

    private final List<User> friendList;

    public ListAdapter(Context context, List<User> friendList) {
        this.context = context;
        this.friendList = friendList;
    }

    @NonNull
    @Override
    public FriendHolder onCreateViewHolder(ViewGroup parent, int type) {
        return new FriendHolder(LayoutInflater.from(context).inflate(R.layout.friend_item, parent, false));
    }

    @Override
    public void onBindViewHolder(FriendHolder holder, int pos) {
        holder.username.setText(friendList.get(holder.getAdapterPosition()).getUsername());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("friend", friendList.get(holder.getAdapterPosition()).getUsername());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public static class FriendHolder extends RecyclerView.ViewHolder {
        public TextView username;

        public FriendHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
        }
    }
}
