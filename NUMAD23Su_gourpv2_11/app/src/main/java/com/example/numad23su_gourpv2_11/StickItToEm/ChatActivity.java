package com.example.numad23su_gourpv2_11.StickItToEm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.numad23su_gourpv2_11.R;
import com.example.numad23su_gourpv2_11.StickItToEm.adapters.ChatRecyclerAdapter;
import com.example.numad23su_gourpv2_11.StickItToEm.models.MessageModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private RecyclerView chatRecyclerView;
    private ImageButton smile_button;
    private ImageButton angry_button;
    private ImageButton crying_button;
    private ImageButton laugh_button;
    private List<MessageModel> messageModels;
    private String current_username;
    private String friend_username;
    private ChatRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mDatabase = FirebaseDatabase.getInstance().getReference("messages");
        messageModels = new ArrayList<MessageModel>();
        TextView friendUsername = findViewById(R.id.username_of_friend_message);
        smile_button = findViewById(R.id.smile_button);
        angry_button = findViewById(R.id.angry_button);
        crying_button = findViewById(R.id.crying_button);
        laugh_button = findViewById(R.id.laugh_button);
        Intent intent = getIntent();
        friend_username = intent.getExtras().getString("friend");
        SharedPreferences sharedPrefs = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE);
        current_username = sharedPrefs.getString("username", "USERNAME_COULD_NOT_BE_FOUND");
        friendUsername.setText(friend_username);
        smile_button.setOnClickListener(view -> {
            RecordImage(current_username, friend_username, "2131165428");
        });
        angry_button.setOnClickListener(view -> {
            RecordImage(current_username, friend_username, "2131165356");
        });
        crying_button.setOnClickListener(view -> {
            RecordImage(current_username, friend_username, "2131165334");
        });
        laugh_button.setOnClickListener(view -> {
            RecordImage(current_username, friend_username, "2131165304");
        });
        DatabaseReference nDatabase = mDatabase;
        chatRecyclerView = findViewById(R.id.recyclerview_messages);
        adapter = new ChatRecyclerAdapter(this, messageModels, current_username, friend_username, chatRecyclerView);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        chatRecyclerView.setAdapter(adapter);
        nDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messageModels.clear();
                chatRecyclerView.scrollToPosition(adapter.getItemCount() - 1);
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        MessageModel msg = snapshot.getValue(MessageModel.class);
                        if((msg.getSender().equals(current_username) && msg.getReceiver().equals(friend_username))
                                || msg.getSender().equals(friend_username) && msg.getReceiver().equals(current_username)) {
                            messageModels.add(msg);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }

    private void RecordImage(String current_username, String friend_username, String imageType) {
        LocalDateTime localDateTime = LocalDateTime.now();
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        long nowTime = instant.toEpochMilli();
        Map<String, Object> message = new HashMap<>();
        message.put("sender", current_username);
        message.put("receiver", friend_username);
        message.put("sticker", imageType);
        message.put("timestamp", nowTime);

        mDatabase.child(String.valueOf(nowTime)).setValue(message);
    }
}