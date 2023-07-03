package com.example.numad23su_gourpv2_11;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LoginButtonClicked extends AppCompatActivity {

    private DatabaseReference mDatabase;

    private ListAdapter friendAdapter;

    private RecyclerView friendRecycler;

    private final String activityName = LoginButtonClicked.class.getSimpleName();

    private List<User> friendList;

    private String currentUser;

    Button history;

    HashMap<String, Integer> receivedNum = new HashMap<>();
    HashMap<String, Integer> stickerNum = new HashMap<>();

    private TextView currentUserTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_button_clicked);

        history = findViewById(R.id.buttonHistory);
        currentUserTextView = findViewById(R.id.successName);

        friendRecycler = findViewById(R.id.friendList);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        currentUser = sharedPrefs.getString("username", "");
        currentUserTextView.setText(currentUser + " is signed in.");
        friendList = new ArrayList<>();

        friendAdapter = new ListAdapter(this, friendList);
        friendRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        friendRecycler.setAdapter(friendAdapter);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Log.e(activityName, "onChildAdded: dataSnapshot = " + snapshot.getValue().toString());
                        User user = snapshot.getValue(User.class);
                        if (!user.getUsername().equals(currentUser)) {
                            friendList.add(user);
                        }
                        friendAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Log.v(activityName, "onChildChanged: " + snapshot.getValue().toString());
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(activityName, "onCancelled: " + error);
                    }
                }
        );
        getMessageHistory();
    }

    private void getMessageHistory() {
        mDatabase.child("messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Message> messages = new ArrayList<>();
                List<Message> receivedMsg = new ArrayList<>();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Message msg = dataSnapshot.getValue(Message.class);
                    if(msg.getReceiver() != null && msg.getReceiver().equals(currentUser)){
                        receivedMsg.add(msg);
                    }
                    if (msg.getSender() != null && msg.getSender().equals(currentUser)) {
                        messages.add(msg);
                    }
                }
                List<String> stickers = new ArrayList<>();
                stickers.add("2131165428");
                stickers.add("2131165356");
                stickers.add("2131165334");
                stickers.add("2131165304");

                for (String sticker: stickers) {
                    int count = countNumStickers(messages, sticker);
                    stickerNum.put(sticker, count);
                    Log.d(activityName, "Sticker: " + sticker);
                }

                Log.d(activityName, "Number of stickers: " + stickerNum);

                for (String receivedSticker: stickers) {
                    int receivedCount = countNumStickers(receivedMsg, receivedSticker);
                    receivedNum.put(receivedSticker, receivedCount);
                    Log.d(activityName, "Received Sticker: " + receivedSticker);
                }

                history.setOnClickListener(view -> {
                    Intent intent = new Intent(LoginButtonClicked.this, History.class);
                    intent.putExtra("stickerNum", stickerNum.toString());
                    intent.putExtra("receivedNum", receivedNum.toString());
                    startActivity(intent);
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(activityName, "Error reading value", error.toException());
            }
        });
    }

    public int countNumStickers(List<Message> messages, String sticker) {
        int num = 0;
        for (Message msg : messages) {
            if (msg.getSticker().equals(sticker) && msg.getSticker() != null) {
                num++;
            }
        }
        return num;
    }
}