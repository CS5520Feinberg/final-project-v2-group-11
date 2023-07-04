package com.example.numad23su_gourpv2_11.StickItToEm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.numad23su_gourpv2_11.R;

import java.util.HashMap;

public class History extends AppCompatActivity {

    int stickerNum;
    int receivedNum;

    TextView sticker1;
    TextView sticker2;
    TextView sticker3;
    TextView sticker4;

    HashMap<String, Integer> num_send = new HashMap<>();
    HashMap<String, Integer> num_receive = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        sticker1 = findViewById(R.id.sticker1);
        sticker2 = findViewById(R.id.sticker2);
        sticker3 = findViewById(R.id.sticker3);
        sticker4 = findViewById(R.id.sticker4);

        String stickerCount = getIntent().getStringExtra("stickerNum");
        String receivedCount = getIntent().getStringExtra("receivedNum");

        num_send = stringToHash(stickerCount);
        num_receive = stringToHash(receivedCount);

        String[] keys = {"2131165428", "2131165356", "2131165334", "2131165304"};

        for (int i = 0; i<keys.length; i++) {
            if (num_send.containsKey(keys[i]) && num_receive.containsKey(keys[i])) {
                stickerNum = num_send.get(keys[i]);
                receivedNum = num_receive.get(keys[i]);
                switch (i) {
                    case 0:
                        sticker1.setText("Sent = " + stickerNum + "\nReceived = " + receivedNum);
                        break;
                    case 1:
                        sticker2.setText("Sent = " + stickerNum + "\nReceived = " + receivedNum);
                        break;
                    case 2:
                        sticker3.setText("Sent = " + stickerNum + "\nReceived = " + receivedNum);
                        break;
                    case 3:
                        sticker4.setText("Sent = " + stickerNum + "\nReceived = " + receivedNum);
                        break;
                }
            }
        }
    }

    public HashMap<String, Integer> stringToHash(String s) {
        HashMap<String, Integer> res = new HashMap<>();

        String[] pairs = s.substring(1, s.length() - 1).split(",");
        for (String pair : pairs) {
            String[] val = pair.trim().split("=");
            int x = Integer.parseInt(val[1].trim());
            res.put(val[0].trim(), x);
        }
        return res;
    }
}