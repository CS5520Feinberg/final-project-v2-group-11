package com.example.numad23su_gourpv2_11.StickItToEm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.numad23su_gourpv2_11.R;
import com.example.numad23su_gourpv2_11.StickItToEm.adapters.ListAdapter;
import com.example.numad23su_gourpv2_11.StickItToEm.models.MessageModel;
import com.example.numad23su_gourpv2_11.StickItToEm.models.User;
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

        SharedPreferences sharedPrefs = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE);
        currentUser = sharedPrefs.getString("username", "USERNAME_COULD_NOT_BE_FOUND");
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
                List<MessageModel> messageModels = new ArrayList<>();
                List<MessageModel> receivedMsg = new ArrayList<>();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    MessageModel msg = dataSnapshot.getValue(MessageModel.class);
                    if(msg.getReceiver() != null && msg.getReceiver().equals(currentUser)){
                        receivedMsg.add(msg);
                    }
                    if (msg.getSender() != null && msg.getSender().equals(currentUser)) {
                        messageModels.add(msg);
                    }
                }
                List<String> stickers = new ArrayList<>();
                stickers.add("2131165428");
                stickers.add("2131165356");
                stickers.add("2131165334");
                stickers.add("2131165304");

                for (String sticker: stickers) {
                    int count = countNumStickers(messageModels, sticker);
                    stickerNum.put(sticker, count);
                    //Log.d(activityName, "Sticker: " + sticker);
                }

                //Log.d(activityName, "Number of stickers: " + stickerNum);

                for (String receivedSticker: stickers) {
                    int receivedCount = countNumStickers(receivedMsg, receivedSticker);
                    receivedNum.put(receivedSticker, receivedCount);
                    //Log.d(activityName, "Received Sticker: " + receivedSticker);
                }

                history.setOnClickListener(view -> {
                    Intent intent = new Intent(LoginButtonClicked.this, History.class);
                    intent.putExtra("stickerNum", stickerNum.toString());
                    intent.putExtra("receivedNum", receivedNum.toString());
                    startActivity(intent);
                });
                String lastMsgSender = "";
                String friendSender = "";
                for (DataSnapshot Datasnapshot : snapshot.getChildren()) {
                    lastMsgSender = Datasnapshot.getValue(MessageModel.class).getReceiver();
                    friendSender = Datasnapshot.getValue(MessageModel.class).getSender();
                }
                createNotification(String.format("%s sent you a new sticker in chat", friendSender), lastMsgSender);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(activityName, "Error reading value", error.toException());
            }
        });
    }

    public int countNumStickers(List<MessageModel> messageModels, String sticker) {
        int num = 0;

        for (MessageModel msg : messageModels) {
            if (msg.getSticker().equals(sticker) && msg.getSticker() != null) {
                num++;
            }
        }
        return num;
    }
    private void createNotification(String message, String f_username){
        String CHANNEL_ID = "Chat_channel_01";
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Chat Notification",
                    NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
        }
        Bitmap res;
        Context context;


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_g11_round) // your app icon
                .setContentTitle("New Sticker")
                .setContentText(message);

        mNotificationManager.notify(001, mBuilder.build());
    }
}