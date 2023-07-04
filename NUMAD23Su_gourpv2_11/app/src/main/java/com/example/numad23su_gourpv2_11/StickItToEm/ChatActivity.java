package com.example.numad23su_gourpv2_11.StickItToEm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.numad23su_gourpv2_11.R;
import com.example.numad23su_gourpv2_11.StickItToEm.models.Message;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class ChatActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    private TextView FriendUsername;

    private RecyclerView chatRecyclerView;

    private ImageButton smile_button;

    private ImageButton angry_button;

    private ImageButton crying_button;

    private ImageButton laugh_button;

    private List<Message> messages;
    private String current_username;
    private String friend_username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        messages = new ArrayList<Message>();
        chatRecyclerView = findViewById(R.id.recyclerview_messages);
        FriendUsername = findViewById(R.id.username_of_friend_message);
        smile_button = findViewById(R.id.smile_button);
        angry_button = findViewById(R.id.angry_button);
        crying_button = findViewById(R.id.crying_button);
        laugh_button = findViewById(R.id.laugh_button);
        Intent intent = getIntent();

        friend_username = intent.getExtras().getString("friend");

        SharedPreferences sharedPrefs = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE);
        current_username = sharedPrefs.getString("username", "USERNAME_COULD_NOT_BE_FOUND");
        //Log.d("some tag", "textSetter: |" + textSetter + "|");
        FriendUsername.setText(friend_username);
       // Log.d("some tag", "afterTextSet");
        smile_button.setOnClickListener(view -> {
            RecordImage(current_username, friend_username, 2131165428);
        });
        angry_button.setOnClickListener(view -> {
            RecordImage(current_username, friend_username, 2131165356);
        });
        crying_button.setOnClickListener(view -> {
            RecordImage(current_username, friend_username, 2131165334);
        });
        laugh_button.setOnClickListener(view -> {
            RecordImage(current_username, friend_username, 2131165304);
        });

    }

    private void RecordImage(String current_username, String friend_username, int imageType) {
        String nowTime =String.valueOf(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        mDatabase.child("messages").child(nowTime).child(current_username).child(friend_username).child("imageType").setValue(imageType);
    }
}